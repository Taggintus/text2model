/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package transform;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.frapu.code.visualization.Cluster;
import net.frapu.code.visualization.Configuration;
import net.frapu.code.visualization.LayoutUtils;
import Nodes.ProcessEdge;
import Nodes.ProcessNode;
import processing.ProcessUtils;
import BPMN.Association;
import Models.BPMNModel;
import Models.ProcessModel;
import BPMN.DataObject;
import BPMN.EndEvent;
import BPMN.ErrorIntermediateEvent;
import BPMN.EventBasedGateway;
import BPMN.ExclusiveGateway;
import Nodes.FlowObject;
import BPMN.Gateway;
import BPMN.InclusiveGateway;
import BPMN.IntermediateEvent;
import BPMN.Lane;
import BPMN.LaneableCluster;
import BPMN.MessageFlow;
import BPMN.MessageIntermediateEvent;
import BPMN.ParallelGateway;
import BPMN.Pool;
import BPMN.SequenceFlow;
import BPMN.StartEvent;
import BPMN.Task;
import BPMN.TerminateEndEvent;
import BPMN.TimerIntermediateEvent;
import transform.AnalyzedSentence;

import com.inubit.research.layouter.gridLayouter.GridLayouter;

import etc.Constants;
import etc.TextToProcess;
import processing.ProcessingUtils;
import processing.WordNetWrapper;
import processing.FrameNetWrapper.PhraseType;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Flow;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;
import worldModel.Flow.FlowDirection;
import worldModel.Flow.FlowType;
import worldModel.Specifier.SpecifierType;

/**
 * @author ff
 *
 */
public abstract class ProcessModelBuilder {
	
	private Configuration f_config = Configuration.getInstance();
	
	//Nodes
	private final boolean EVENTS_TO_LABELS = true;
	private final boolean REMOVE_LOW_ENTROPY_NODES = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_REMOVE_LOW_ENT_NODES));
	private final boolean HIGHLIGHT_LOW_ENTROPY = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_HIGHLIGHT_META_ACTIONS));
	//Labeling
	private final boolean ADD_UNKNOWN_PHRASETYPES = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_ADD_UNKNOWN_PT));
	private final int MAX_NAME_DEPTH = 3;
	//Model in General
	private final boolean BUILD_BLACK_BOX_POOL_COMMUNICATION = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_BB_POOLS));
	private final boolean BUILD_DATA_OBJECTS = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_DATA_OBJECTS));
	
	
	
	private TextToProcess f_parent;
	
	private BPMNModel f_model = new BPMNModel("generated Model");
	
	
	private HashMap<Actor, String> f_ActorToName = new HashMap<Actor, String>();
	private HashMap<String, Lane> f_NameToPool = new HashMap<String, Lane>();		
	private HashMap<Action, FlowObject> f_elementsMap = new HashMap<Action, FlowObject>();
	private HashMap<FlowObject, Action> f_elementsMap2 = new HashMap<FlowObject,Action>();
	
	private ArrayList<FlowObject> f_notAssigned = new ArrayList<FlowObject>();
	private Lane f_lastPool = null;
	private LaneableCluster f_mainPool;
	
	//for black box pools
	private HashMap<ProcessNode,String> f_CommLinks = new HashMap<ProcessNode, String>();
	private HashMap<String, Pool> f_bbPoolcache = new HashMap<String, Pool>();

	
	/**
	 * 
	 */
/**
 * @param os
 * @param _a
 * @param _b
 * @param s
 */

private void put(HashMap<Action, List<String>> os, Action a, String dataObj) {
	if(!os.containsKey(a)) {
		LinkedList<String> _list = new LinkedList<String>();
		os.put(a, _list);
	}
	os.get(a).add(dataObj);		
}

public ProcessModelBuilder(TextToProcess parent) {
	f_parent = parent;

}
/**
 * @param _a
 * @return
 */
private Set<String> getDataObjectCandidates(SpecifiedElement ob) {
	if(ob == null) {
		return new HashSet<String>(0);
	}		
	HashSet<String> _result = new HashSet<String>();
	if(ob instanceof Resource) {
		if(((Resource) ob).needsResolve()) {
			_result.addAll(getDataObjectCandidates(((ExtractedObject)ob).getReference()));
			return _result;
		}else {
			String _name = getName((ExtractedObject)ob,false, 1, true);
			if(WordNetWrapper.canBeDataObject(_name,ob.getName())) {
				_result.add(_name);		
				return _result;
			}
		}
	}else if(ob instanceof Actor) {
		Actor _actor = (Actor) ob;
		if(_actor.isUnreal()) {
			String _name = getName(_actor,false, 1, true);
			if(WordNetWrapper.canBeDataObject(_name,_actor.getName())) {
				_result.add(_name);
				return _result;
			}
		}else if(_actor.needsResolve()) {
			_result.addAll(getDataObjectCandidates(_actor.getReference()));
			return _result;
		}
	}else if(ob instanceof Action) {
		Action _action = (Action) ob;
		_result.addAll(getDataObjectCandidates(_action.getActorFrom()));
		_result.addAll(getDataObjectCandidates(_action.getObject()));
		_result.addAll(getDataObjectCandidates(_action.getXcomp()));
	}
	//checking specifiers
	for(Specifier spec:ob.getSpecifiers()) {
		if(spec.getObject() != null && !"of".equals(spec.getHeadWord())) {
			_result.addAll(getDataObjectCandidates(spec.getObject()));
		}
	}
	return _result;
}

public Map<ProcessNode, String> getCommLinks(){
	return f_CommLinks;
}


/**
 * @param specifiers
 * @return
 */
private Specifier containedReceiver(List<Specifier> specifiers) {
	return containsFrameElement(specifiers,ListUtils.getList("Donor","Source"));
}

/**
 * @param specifiers
 * @return
 */
private Specifier containedSender(List<Specifier> specifiers) {
	return containsFrameElement(specifiers,ListUtils.getList("Addressee","Recipient"));
}

/**
 * @param specifiers
 * @param list
 * @return
 */
private Specifier containsFrameElement(List<Specifier> specifiers,
		List<String> list) {
	for(Specifier sp:specifiers) {
		if(sp.getFrameElement() != null) {
			if(list.contains(sp.getFrameElement().getName())) {
				return sp;
			}
		}
	}
	return null;
	
}


/**
 * @param world 
 * 
 */
private void processMetaActivities(WorldModel world) {
	for(Action a:world.getActions()) {
		if(a.getActorFrom() != null && a.getActorFrom().isMetaActor()) {					
			if(WordNetWrapper.isVerbOfType(a.getName(),"end")){
				//found an end verb
				ProcessNode _pnode = f_elementsMap.get(a);
				List<ProcessNode> _succs = f_model.getSuccessors(_pnode);
//				boolean _allEnd = true;
//				for(ProcessNode n:_succs) {
//					if(!(n instanceof EndEvent)) {
//						_allEnd = false;
//						break;
//					}
//				}
//				if(_allEnd) {
					removeNode(a);
					if(a.getName().equals("terminate") && _succs.size()==1) {
						EndEvent _ee = (EndEvent) _succs.get(0);
						try {
							ProcessUtils.refactorNode(f_model, _ee, TerminateEndEvent.class);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
//				}					
			}else if(WordNetWrapper.isVerbOfType(a.getName(),"start")) {
				ProcessNode _pnode = f_elementsMap.get(a);
				List<ProcessNode> _preds = f_model.getPredecessors(_pnode);
				if(_preds.size() == 1 && _preds.get(0) instanceof StartEvent) {
					//we do not need this node
					removeNode(a);
				}
			}
		}
	}
}


/**
 * 
 */
private void eventsToLabels() {
	for(ProcessNode node:new ArrayList<ProcessNode>(f_model.getNodes())) {
		if(node instanceof Gateway || node instanceof ErrorIntermediateEvent) {
			List<ProcessNode> _succs = f_model.getSuccessors(node);
			for(ProcessNode _succ : _succs) {
				if(_succ.getClass().getSimpleName().equals("IntermediateEvent")) { //only simple intermediate events
					List<ProcessNode> _succsIE = f_model.getSuccessors(_succ);
					if(_succsIE.size() == 1) {
						String _lbl = _succ.getName();
						SequenceFlow _newFlow = removeNode(_succ);
						_newFlow.setLabel(_lbl);
					}
				}else if(_succ instanceof Task) {
					Action _action = f_elementsMap2.get(_succ);
					List<Specifier> _specs = _action.getSpecifiers(SpecifierType.PP);
					if(_action.getActorFrom() != null) {
						_specs.addAll(_action.getActorFrom().getSpecifiers(SpecifierType.PP));
					}
					for(Specifier spec:_specs) {
						if(SearchUtils.startsWithAny(spec.getPhrase(),Constants.f_conditionIndicators)
								&& !Constants.f_conditionIndicators.contains(spec.getPhrase())) { //it should only be the start of a phrase, not the whole phrase!
							//found the phrase which can serve as a label
							SequenceFlow _sqf = (SequenceFlow) f_model.getConnectingEdge(node, _succ);
							_sqf.setLabel(spec.getPhrase());
							break;
						}
					}
					//}
					
				}
			}
		}
	}
}



/**
 * @param actorFrom
 * @return
 */
private boolean hasHiddenAction(ExtractedObject obj) {
	boolean _canBeGerund = false;
	for(Specifier spec:obj.getSpecifiers(SpecifierType.PP)) {
		if(spec.getName().startsWith("of")) {
			_canBeGerund = true;
		}
	}
	if(!_canBeGerund) {
		return false;
	}
	if(obj != null) {
		for(String s:obj.getName().split(" ")) {
			if(WordNetWrapper.deriveVerb(s) != null) {
				return true;
			}
		}
	}
	return false;
}

/**
 * @param world 
 * 
 */
private void removeDummies(WorldModel world) {
	for(Action a:world.getActions()) {
		if(a instanceof DummyAction || a.getTransient()) {
			removeNode(a);
		}else {
			if(f_elementsMap.get(a).getText().equals("Dummy")) {
				removeNode(a);
			}				
		}
	}
}


public abstract ProcessModel createProcessModel(WorldModel world);

public abstract void buildDataObjects(WorldModel world);

public abstract DataObject createDataObject(Action targetAc,String doName,boolean arriving);

public abstract String getName(ExtractedObject a,boolean addDet,int level,boolean compact);

}