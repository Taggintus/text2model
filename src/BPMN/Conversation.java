package BPMN;

import Nodes.FlowObject;

public class Conversation extends FlowObject{
	
	/** Determines whether this Conversation is compound or not (1,0)*/
    public final static String PROP_COMPOUND = "compound";
    /** Determines whether this Conversation is compound or not (1,0)*/
    public final static String PROP_CALL = "call";
    
    public Conversation() {
        super();
    }

    public Conversation(int xPos, int yPos, String text) {
        super();
        setText(text);
    }

    
    public String toString() {
        return "BPMN conversation";
    }

}
