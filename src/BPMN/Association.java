package BPMN;

import Nodes.ProcessEdge;

public class Association extends ProcessEdge {

	 public final static String PROP_DIRECTION = "direction";

	    public final static String DIRECTION_SOURCE = "SOURCE";
	    public final static String DIRECTION_TARGET = "TARGET";
	    public final static String DIRECTION_BOTH = "BOTH";
	    public final static String DIRECTION_NONE = "NONE";

	    public Association() {
	        super();
	        initializeProperties();
	    }
	    
	    private void initializeProperties() {
	        setProperty(PROP_DIRECTION, DIRECTION_TARGET);
	       // String[] direction = { DIRECTION_NONE, DIRECTION_SOURCE, DIRECTION_TARGET, DIRECTION_BOTH };
	        //setPropertyEditor(PROP_DIRECTION, new ListSelectionPropertyEditor(direction));
	    }
	    
	    

	
}
