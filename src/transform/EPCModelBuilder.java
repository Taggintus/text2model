package transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.Configuration;
import worldModel.Action;
import worldModel.ExtractedObject;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;
import BPMN.DataObject;
import BPMN.SequenceFlow;
import Models.ProcessModel;
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

	public EPCModelBuilder(TextToProcess parent) {
		f_parent = parent;
	}
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Set<String> getDataObjectCandidates(SpecifiedElement ob) {
		// TODO Auto-generated method stub
		return null;
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
