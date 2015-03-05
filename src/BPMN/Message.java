package BPMN;

import tools.BooleanPropertyEditor;
import Nodes.FlowObject;

public class Message extends FlowObject{

	public static final String PROP_INITIATE = "initiate";

	public static final String INITIATE_TRUE = "1";
	public static final String INITIATE_FALSE = "0";
	
    public Message() {
        super();
        initializeProperties();
    }
    
    public Message(int x, int y, String label) {
        super();
        setText(label);
        initializeProperties();
    }

    protected void initializeProperties() {
        int w=30; int h=20;
        setProperty(PROP_INITIATE, INITIATE_TRUE);
        //setPropertyEditor(PROP_INITIATE, new BooleanPropertyEditor());
    }
}
