package transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import processing.WordNetWrapper;
import tools.Configuration;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;
import BPMN.DataObject;
import BPMN.Lane;
import BPMN.LaneableCluster;
import BPMN.Pool;
import BPMN.SequenceFlow;
import EPC.Organisation;
import EPC.OrganisationCluster;
import Models.BPMNModel;
import Models.EPCModel;
import Models.ProcessModel;
import Nodes.FlowObject;
import Nodes.ProcessNode;
import etc.Constants;
import etc.TextToProcess;

public class EPCModelBuilder extends ProcessModelBuilder {
	
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
	
	private EPCModel f_model = new EPCModel("generated Model");
	
	
	private HashMap<Actor, String> f_ActorToName = new HashMap<Actor, String>();
	private HashMap<String, Organisation> f_NameToPool = new HashMap<String, Organisation>();		
	private HashMap<Action, FlowObject> f_elementsMap = new HashMap<Action, FlowObject>();
	private HashMap<FlowObject, Action> f_elementsMap2 = new HashMap<FlowObject,Action>();
	
	private ArrayList<FlowObject> f_notAssigned = new ArrayList<FlowObject>();
	private Organisation f_lastOrg = null;
	private OrganisationCluster f_mainOrg;
	
	//for black box pools
	private HashMap<ProcessNode,String> f_CommLinks = new HashMap<ProcessNode, String>();
	private HashMap<String, Organisation> f_bbOrgcache = new HashMap<String, Organisation>();

	public EPCModelBuilder(TextToProcess parent) {
		f_parent = parent;
	}

	@Override
	public ProcessModel createProcessModel(WorldModel world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildDataObjects(WorldModel world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DataObject createDataObject(Action targetAc, String doName,
			boolean arriving) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(ExtractedObject a, boolean addDet, int level,
			boolean compact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void layoutModel(ProcessModel _result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void put(HashMap<Action, List<String>> os, Action a,
			String dataObj) {
		if(!os.containsKey(a)) {
			LinkedList<String> _list = new LinkedList<String>();
			os.put(a, _list);
		}
		os.get(a).add(dataObj);
	}

	@Override
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

	@Override
	public Map<ProcessNode, String> getCommLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Specifier containedReceiver(List<Specifier> specifiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Specifier containedSender(List<Specifier> specifiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void processMetaActivities(WorldModel world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeDummies(WorldModel world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getEventText(Action a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void eventsToLabels() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected SequenceFlow removeNode(Action a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SequenceFlow removeNode(ProcessNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean hasHiddenAction(ExtractedObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String createTaskText(Action a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean considerPhrase(Specifier spec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getName(ExtractedObject a, boolean addDet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addSpecifier(int level, StringBuilder _b, Specifier s,
			boolean compact) {
		// TODO Auto-generated method stub
		
	}



}
