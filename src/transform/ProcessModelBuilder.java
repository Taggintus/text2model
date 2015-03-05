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
import BPMN.FlowObject;
import BPMN.bpmn.Gateway;
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
public Set<String> getDataObjectCandidates(SpecifiedElement ob) {
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


	/**
	 * @param a
	 * @return
	 */
	private String getName(ExtractedObject a,boolean addDet,int level,boolean compact) {
		if(a == null) {
			return "null";
		}
		if(a.needsResolve() && a.getReference() instanceof ExtractedObject) {
			return getName((ExtractedObject)a.getReference(),addDet);
		}
		StringBuilder _b = new StringBuilder();
		if(addDet && Constants.f_wantedDeterminers.contains(a.getDeterminer())) {
			_b.append(a.getDeterminer());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.AMOD)) {
			_b.append(s.getName());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.NUM)) {
			_b.append(s.getName());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.NN)) {
			_b.append(s.getName());
			_b.append(' ');
		}		
		_b.append(a.getName());
		for(Specifier s:a.getSpecifiers(SpecifierType.NNAFTER)) {
			_b.append(' ');
			_b.append(s.getName());
		}
		if(level <= MAX_NAME_DEPTH)
		for(Specifier s:a.getSpecifiers(SpecifierType.PP)) {
			if(s.getPhraseType() == PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
				if(s.getName().startsWith("of") || 
						(!compact && s.getName().startsWith("into")) || 
						(!compact && s.getName().startsWith("under")) ||
						(!compact && s.getName().startsWith("about"))) {
					addSpecifier(level, _b, s,compact);
				}	
			}else if(considerPhrase(s)) {
				addSpecifier(level, _b, s,compact);
			}
			
		}
		if(!compact) {
			for(Specifier s:a.getSpecifiers(SpecifierType.INFMOD)) {
				_b.append(' ');
				_b.append(s.getName());
			}for(Specifier s:a.getSpecifiers(SpecifierType.PARTMOD)) {
				_b.append(' ');
				_b.append(s.getName());
			}
		}
		return _b.toString();
	}



public abstract ProcessModel createProcessModel(WorldModel world);

public abstract void buildDataObjects(WorldModel world);

public abstract DataObject createDataObject(Action targetAc,String doName,boolean arriving);

}