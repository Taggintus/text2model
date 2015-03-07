package BPMN;

import Nodes.ProcessEdge;
import Nodes.ProcessNode;

public class MessageFlow extends ProcessEdge {
	
	 public MessageFlow() {
	        super();
	        initializeProperties();
	    }

	    public MessageFlow(ProcessNode source, ProcessNode target) {
	        super();
	        initializeProperties();
	    }

	    private void initializeProperties() {
	        // empty yet
	    }

}
