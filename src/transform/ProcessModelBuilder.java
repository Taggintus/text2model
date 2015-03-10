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

import Nodes.Cluster;
//import net.frapu.code.visualization.Configuration;
//import net.frapu.code.visualization.LayoutUtils;
import Nodes.ProcessEdge;
import Nodes.ProcessNode;
import processing.ProcessUtils;
import BPMN.Association;
import Models.BPMNModel;
import Models.ProcessModel;
import BPMN.DataObject;
import BPMN.EndEvent;
import BPMN.EventBasedGateway;
import BPMN.ExclusiveGateway;
import Nodes.FlowObject;
import BPMN.Gateway;
import BPMN.InclusiveGateway;
import BPMN.IntermediateEvent;
import BPMN.Lane;
import BPMN.LaneableCluster;
import BPMN.MessageFlow;
import BPMN.ParallelGateway;
import BPMN.Pool;
import BPMN.SequenceFlow;
import BPMN.StartEvent;
import BPMN.Task;
import BPMN.TerminateEndEvent;
import tools.Configuration;
import transform.AnalyzedSentence;

//import com.inubit.research.layouter.gridLayouter.GridLayouter;

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

protected void put(HashMap<Action, List<String>> os, Action a, String dataObj) {
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
protected Set<String> getDataObjectCandidates(SpecifiedElement ob) {
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
protected Specifier containedReceiver(List<Specifier> specifiers) {
	return containsFrameElement(specifiers,ListUtils.getList("Donor","Source"));
}

/**
 * @param specifiers
 * @return
 */
protected Specifier containedSender(List<Specifier> specifiers) {
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
protected void processMetaActivities(WorldModel world) {
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
 * @param world 
 * 
 */
protected void removeDummies(WorldModel world) {
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

/**
 * @param a
 * @return
 */
protected String getEventText(Action a) {
	StringBuilder _b = new StringBuilder();
	boolean _actorPlural = false;
	if(a.getActorFrom() != null) {
		_b.append(getName(a.getActorFrom(),true));
		_b.append(' ');
		_actorPlural = a.getActorFrom().getName().endsWith("s");
	}	
	if(!WordNetWrapper.isWeakVerb(a.getName()) || (a.getCop() != null || 
			a.getObject() != null || a.getSpecifiers().size()>0 || a.isNegated())) {
		boolean _auxIsDo = (a.getAux() != null && WordNetWrapper.getBaseForm(a.getAux()).equals("do"));
		if(a.isNegated() && (!WordNetWrapper.isWeakVerb(a.getName())||_auxIsDo)) {
			if(a.getAux() != null && !WordNetWrapper.getBaseForm(a.getAux()).equals("be")) {
				_b.append(a.getAux());
			}else {
				_b.append(_actorPlural ? "do" : ProcessingUtils.get3rdPsing("do"));	
			}
			_b.append(" not ");
			_b.append(WordNetWrapper.getBaseForm(a.getName()));
			_b.append(' ');
		}else {
			if(a.getAux() != null) {
				if(a.getActorFrom() != null && !a.getActorFrom().getPassive()) {
					_b.append(a.getAux());
					_b.append(' ');
					_b.append(a.getName());
				}else{
					_b.append(ProcessingUtils.get3rdPsing(a.getName()));
				}
				
			}else {
				_b.append(_actorPlural ? WordNetWrapper.getBaseForm(a.getName()) : ProcessingUtils.get3rdPsing(a.getName()));
			}		
			if(a.isNegated()) {
				_b.append(" not ");
			}
		}
		_b.append(' ');
	}
	
	
	if(a.getCop() != null) {
		_b.append(a.getCop());
	}else {
		if(a.getObject() != null) {
			_b.append(getName(a.getObject(),true));
		}else {
			if(a.getSpecifiers().size() > 0) {
				_b.append(a.getSpecifiers().get(0).getPhrase());
			}
		}
	}
	return _b.toString();
}


/**
 * 
 */
protected void eventsToLabels() {
	for(ProcessNode node:new ArrayList<ProcessNode>(f_model.getNodes())) {
		if(node instanceof Gateway) {
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

private SequenceFlow removeNode(ProcessNode a) {
	ProcessNode _node = toProcessNode();
//	if(f_model.getPredecessors(_node).size() == 0) {
//		//add a start node in front
//		StartEvent _start = new StartEvent();
//		f_model.addNode(_start);
//		Cluster _lane = f_model.getClusterForNode(_node);
//		if(_lane != null) {
//			_lane.addProcessNode(_start);
//		}
//		SequenceFlow _sqf = new SequenceFlow(_start,_node);
//		f_model.addFlow(_sqf);
//	}
	return removeNode(_node);
}


private ProcessNode toProcessNode() {
	// TODO Auto-generated method stub
	return null;
}

/**
 * @param actorFrom
 * @return
 */
protected boolean hasHiddenAction(ExtractedObject obj) {
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
 * @param f
 * @param gate
 */
protected void addToPrevalentLane(Flow f, Gateway gate) {
	HashMap<Lane, Integer> _countMap = new HashMap<Lane, Integer>();
	if(!(f.getSingleObject() instanceof DummyAction)) {
		Lane _lane1 = getLaneForNode(toProcessNode(f.getSingleObject()));
		_countMap.put(_lane1, 1);
	}
	for(Action a:f.getMultipleObjects()) {
		if(!(a instanceof DummyAction)) {
			Lane _lane = getLaneForNode(toProcessNode(a));
			if(_countMap.containsKey(_lane)) {
				_countMap.put(_lane,_countMap.get(_lane)+1);
			}else {
				_countMap.put(_lane, 1);
			}
		}
	}
	Lane _best = (Lane) SearchUtils.getMaxCountElement(_countMap);
	//if best is null, there simply is no lane in the process!
	if(_best != null) {
		_best.addProcessNode(gate);
	}
}

/**
 * @return
 */
protected String createTaskText(Action a) {
	StringBuilder _b = new StringBuilder();		
	if(a.isNegated()) {
		if(a.getAux() != null) {
			_b.append(a.getAux());
			_b.append(' ');
		}
		_b.append("not");
		_b.append(' ');
	}
	if(WordNetWrapper.isWeakAction(a) && canBeTransformed(a)) {
		if(a.getActorFrom() != null && a.getActorFrom().isUnreal() && hasHiddenAction(a.getActorFrom())) {
			_b.append(transformToAction(a.getActorFrom()));					
		}else if(a.getObject() != null && ((a.getObject() instanceof Resource) ||  !((Actor)a.getObject()).isUnreal())) {
			_b.append(transformToAction(a.getObject()));					
		}			
	}else {
		boolean _weak = WordNetWrapper.isWeakVerb(a.getName());
		if(!_weak) {
			_b.append(WordNetWrapper.getBaseForm(a.getName()));
			if(a.getPrt()!= null) {
				_b.append(' ');
				_b.append(a.getPrt());
			}
			_b.append(' ');
		}else {
			//a weak verb which cannot be transformed hmmm.....
			if(REMOVE_LOW_ENTROPY_NODES && (a.getActorFrom() == null || a.getActorFrom().isMetaActor()) &&
					a.getXcomp() == null) {
				return "Dummy";					
			}else {
				if(a.getXcomp() == null) { //hm we should add something here or the label is empty
					_b.append(getEventText(a));
					return _b.toString().replaceAll("  ", " ");
				}
			}
		}
		boolean _xCompAdded = false;
		boolean _modAdded = false;
		if(a.getObject() != null) {
			if(a.getMod() != null && (a.getModPos() < a.getObject().getWordIndex())) {
				addMod(a,_b);	
				_b.append(' ');
				_modAdded = true;
			}
			if(a.getXcomp()!= null && (a.getXcomp().getWordIndex() < a.getObject().getWordIndex())) {
				addXComp(a, _b,!_weak);
				_b.append(' ');
				_xCompAdded = true;
			}			
			if(a.getSpecifiers(SpecifierType.IOBJ).size() > 0) {
				for(Specifier spec:a.getSpecifiers(SpecifierType.IOBJ)) {
					_b.append(spec.getPhrase());
					_b.append(' ');
				}
			}
			_b.append(getName(a.getObject(),true));
			//
			for(Specifier _dob : a.getSpecifiers(SpecifierType.DOBJ)) {
				_b.append(' ');
				_b.append(getName(_dob.getObject(),true));
			}
			
		}			
		if(!_modAdded) {
			addMod(a, _b);
		}			
		if(!_xCompAdded && a.getXcomp() != null) {
			addSpecifiers(a, _b,a.getXcomp().getWordIndex(),true);
			addXComp(a, _b,!_weak || a.getObject() != null);
		}
		addSpecifiers(a, _b,getXCompPos(a.getXcomp()),false);
		if(!(a.getObject() == null)) { //otherwise addSpecifiers already did the work!
			for(Specifier sp:a.getSpecifiers(SpecifierType.PP)) {
				if((sp.getName().startsWith("to") || sp.getName().startsWith("in") || sp.getName().startsWith("about"))
						&& !(SearchUtils.startsWithAny(sp.getPhrase(), Constants.f_conditionIndicators))) {
					_b.append(' ');
					if(sp.getObject() != null) {
						_b.append(sp.getHeadWord());
						_b.append(' ');
						_b.append(getName(sp.getObject(),true));
					}else {
						_b.append(sp.getName());
						break; // one is enough
					}						
				}
			}
		}
	}
	return _b.toString().replaceAll("  ", " ");
}


/**
 * @param xcomp
 * @return
 */
private int getXCompPos(Action xcomp) {
	if(xcomp == null) {
		return -1;
	}
	return xcomp.getWordIndex();
}

/**
 * @param a
 * @param _b
 */
private void addMod(Action a, StringBuilder _b) {
	if(a.getMod() != null){
		_b.append(' ');
		_b.append(a.getMod());
	}
}

private void addSpecifiers(Action a, StringBuilder _b, int limit,boolean smaller) {
	if(a.getObject() == null) {
		List<Specifier> _specs = a.getSpecifiers(SpecifierType.PP);
		if(a.getXcomp() == null) {
			_specs.addAll(a.getSpecifiers(SpecifierType.SBAR));
		}
		Collections.sort(_specs);			
		boolean _foundSth = false;
		for(Specifier spec:_specs) {
			if(spec.getType() == SpecifierType.SBAR && _foundSth == true) {
				break;
			}
			if(spec.getWordIndex() > a.getWordIndex()) {
				boolean _smaller = spec.getWordIndex() < limit;
				if(!(_smaller^smaller)) {
					if(considerPhrase(spec)){
						_foundSth = true;
						_b.append(' ');
						if(spec.getObject() != null) {
							_b.append(spec.getHeadWord());
							_b.append(' ');
							_b.append(getName(spec.getObject(),true));
						}else {
							_b.append(spec.getName());
						}
					}
				}
			}
		}
	}
}

/**
 * @param spec
 * @return
 */
protected boolean considerPhrase(Specifier spec) {
	if(spec.getPhraseType() == PhraseType.PERIPHERAL || spec.getPhraseType() == PhraseType.EXTRA_THEMATIC) {
		return false;
	}else {
		if(spec.getPhraseType() == PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
			return true;
		}
	}
	return true; //always accept core and genetive
}

private void addXComp(Action a, StringBuilder _b,boolean needsAux) {
	if(a.getXcomp() != null) {
		if(needsAux) {
			if(a.getXcomp().getAux() != null) {
				_b.append(' ');
				_b.append(a.getXcomp().getAux());
				_b.append(' ');
			}else {
				_b.append(" to ");
			}
		}
		_b.append(createTaskText( a.getXcomp()));
	}		
}

/**
 * @param actorFrom
 * @return
 */
private String transformToAction(ExtractedObject obj) {
	StringBuilder _b = new StringBuilder();
	for(String s:obj.getName().split(" ")) {
		String _der = WordNetWrapper.deriveVerb(s);
		if(_der != null) {
			_b.append(_der);
			break;
		}
	}
	for(Specifier spec:obj.getSpecifiers(SpecifierType.PP)) {
		if(spec.getPhrase().startsWith("of") && spec.getObject() != null) {
			_b.append(' ');
			_b.append(getName(spec.getObject(),true));
		}
	}
	return _b.toString();
}

protected String getName(ExtractedObject a,boolean addDet) {
	return getName(a, addDet, 1);
}

private String getName(ExtractedObject a,boolean addDet,int level) {
	return getName(a, addDet, level, false);
}


protected void addSpecifier(int level, StringBuilder _b, Specifier s,boolean compact) {
	_b.append(' ');
	if(s.getObject() != null) {
		_b.append(s.getHeadWord());
		_b.append(' ');
		_b.append(getName(s.getObject(),true,level+1,compact));
			
	}else {
		_b.append(s.getName());
	}
}


public abstract void layoutModel(ProcessModel _result);


public abstract ProcessModel createProcessModel(WorldModel world);

public abstract void buildDataObjects(WorldModel world);

public abstract DataObject createDataObject(Action targetAc,String doName,boolean arriving);

public abstract String getName(ExtractedObject a,boolean addDet,int level,boolean compact);

}
