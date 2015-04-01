package EPC;

import Models.ProcessModel;
import Nodes.FlowObject;
import Nodes.ProcessNode;
import Nodes.ProcessObject;

public class Event extends FlowObject {
	
	/** The sub-type of the event. Possible values are "Catching" or "Throwing" **/
    public final static String PROP_EVENT_SUBTYPE = "event_subtype";
    /** Catching Intermediate Event */
    public final static String EVENT_SUBTYPE_CATCHING = "Catching";
    /** Throwing Intermediate Event */
    public final static String EVENT_SUBTYPE_THROWING = "Throwing";
    /** The interruption type of the event. Possible values are "0" or "Throwing" **/
    public final static String PROP_NON_INTERRUPTING = "non_interupting";
    public final static String EVENT_NON_INTERRUPTING_FALSE = "0";
    public final static String EVENT_NON_INTERRUPTING_TRUE = "1";
    /** The parent node */
    public final static String PROP_SOURCE_NODE = "#source";
    
    private boolean isThrowable = false;
	
    public Event() {
        super();
        initializeProperties();
    }

    public Event(int x, int y, String label) {
        super();
        setText(label);
        initializeProperties();
    }
    
    public Event(String label) {
        super();
        setText(label);
        initializeProperties();
    }

    protected void initializeProperties() {
        setProperty(PROP_EVENT_SUBTYPE, EVENT_SUBTYPE_CATCHING);
        String[] subtype = { EVENT_SUBTYPE_CATCHING , EVENT_SUBTYPE_THROWING };
        setProperty(PROP_NON_INTERRUPTING, EVENT_NON_INTERRUPTING_FALSE);
        setProperty(PROP_SOURCE_NODE, "");
    }
	
	/**
     * tells you whether this intermediate event can be set to a throwing state or not
     * @return
     */
	public boolean isThrowable() {
    	return isThrowable;
    }

    public void setThrowable(boolean value) {
    	isThrowable = value;    	
    }
    
    @Override
    /**
     * overriding handling of throwable/catching
     */
    public void setProperty(String key, String value) {
    	if(key.equals(PROP_EVENT_SUBTYPE) && !isThrowable) {
    		super.setProperty(PROP_EVENT_SUBTYPE, EVENT_SUBTYPE_CATCHING);    		
    	}else {
    		super.setProperty(key, value);
    	}
    	
    }
    
    public ProcessNode getParentNode(ProcessModel model) {
        return model.getNodeById(getProperty(PROP_SOURCE_NODE));
    }
	
	public void setParentNode(ProcessNode node) {
        String id = "";
        if (node!=null) id = node.getProperty(ProcessObject.PROP_ID);
        setProperty(PROP_SOURCE_NODE, id==null?"":id);
    }

    public String getParentNodeId() {
        String result = getProperty(PROP_SOURCE_NODE);
        return result;
    }

    public String toString() {
        return "Event ("+getText()+")";
    }

}
