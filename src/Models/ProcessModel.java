package Models;

import java.util.LinkedList;
import java.util.List;

import processing.ProcessUtils;
import Nodes.ProcessNode;
import Nodes.ProcessEdge;

public abstract class ProcessModel {
	
	public static String ATTR_XMLNS = "xmlns";
	public static String TAG_MODEL = "model";
    public static String TAG_NODES = "nodes";
    public static String TAG_EDGES = "edges";
    public static String TAG_PROPERTIES = "properties";
    public static String ATTR_NAME = "name";
    public static String ATTR_TYPE = "type";
    public static String ATTR_ID = "id";
    
    /** A dirty flag */
    private boolean dirtyFlag = false;
	
	protected String id;
    /** The name of the ProcessModel */
    public static String PROP_PROCESS_NAME;
    /** The URI for this model (if applicable) */
    public static String PROP_PROCESS_URI = "#uri";
    /** A field for the owner of this model (if applicable */
    public static String PROP_EDITOR = "#editor";
    /** The creation date of the ProcessModel */
    public static String PROP_CREATE_DATE = "#creationDate";
    /** The author of this ProcessModel */
    public static String PROP_AUTHOR = "author";
    /** A comment for this ProcessModel */
    public static String PROP_COMMENT = "comment";
    /** The source version of this ProcessModel (optional) */
    public static String PROP_SOURCE_VERSION = "#source_version";
    /** An optional property holding the source folder alias */
    public static String PROP_FOLDERALIAS = "#folder";
    /** The last time this model was changed  */
    public static String PROP_LASTCHECKIN = "#LAST_CHECKIN_TIME";
    /** An instance of a sub-class of ProcessUtils */
    protected ProcessUtils processUtils = null;

    public ProcessModel () {}
    
    public ProcessModel (String name){
    	PROP_PROCESS_NAME = name;
    }

    public void markAsDirty(boolean dirty) {
        dirtyFlag = dirty;
    }
    
    public boolean isDirty() {
        return dirtyFlag;
    }
    
	/**
	 * List of ProcessNodes
	 */
	private LinkedList<ProcessNode> processNodes = new LinkedList<ProcessNode>();
	/**
	 * List of Arcs
	 */
	private LinkedList<ProcessEdge> ProcessEdges = new LinkedList<ProcessEdge>();
	
	
	
	
	/**
	 * adds a ProcessNode to the set of ProcessNodes
	 * @param pn
	 */
	public synchronized void addNode(ProcessNode pn) {
		processNodes.add(pn);
	}
	
	
	/**
	 * deletes a ProcessNode from the set of ProcessNodes
	 * @param pn
	 */
	public synchronized void removeNode(ProcessNode pn) {
		processNodes.remove(pn);
	}
	
	
	/**
	 * returns the set of ProcessNodes
	 * @return
	 */
	public synchronized LinkedList<ProcessNode> getNodes () {
		return processNodes;
	}
	
	
	/**
	 * adds an Arc to the set of Arcs
	 * @param pn
	 */
	public synchronized void addProcessEdge(ProcessEdge a) {
		ProcessEdges.add(a);
	}
	
	
	/**
	 * deletes an Arc from the set of Arcs
	 * @param pn
	 */
	public synchronized void removeProcessEdge(ProcessEdge a) {
		ProcessEdges.remove(a);
	}
	
	
	/**
	 * returns the set of Arcs
	 * @return
	 */
	public synchronized List<ProcessEdge> getProcessEdges () {
		return ProcessEdges;
	}
	
	
	/**
     * Returns the list of supported edge classes for this model. This
     * method needs to be implemented.
     * @return
     */
    public abstract List<Class<? extends ProcessEdge>> getSupportedEdgeClasses();
    
    
    /**
     * Returns the list of supported node classes for this model. This 
     * method needs to be implemented.
     * @return
     */
    public abstract List<Class<? extends ProcessNode>> getSupportedNodeClasses();
    
    
    /**
     * Returns a single string describing the kind of model this ProcessModel
     * supports.
     * @return
     */
    public abstract String getDescription();
    
    /**
     * Returns the ProcessNode of the model that belongs to a certain id.
     * @param id
     * @return 
     */
    public ProcessNode getNodeById(String id) {
        for (ProcessNode node : getNodes()) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }
    
    /**
     * Returns the ProcessNode(s) of the model that have the given name. More than one node
     * is potentially returned since multiple nodes can have the same name. Returns an empty
     * List if no node was found
     * @param id
     * @return 
     */
    public LinkedList<ProcessNode> getNodeByName(String name) {
        LinkedList<ProcessNode> result = new LinkedList<ProcessNode>(); 
        for (ProcessNode node : getNodes()) {
            if (node.getName().equals(name)) {
                result.add(node);
            }
        }
        return result;
    }
    
    /**
     * Returns the name of the process model.
     * @return
     */
    public String getProcessName() {
        return PROP_PROCESS_NAME;
    }
    
    /**
     * Returns the list of successor nodes for a node n.
     * @param n
     * @return
     */
    public List<ProcessNode> getSuccessors(ProcessNode n) {
        List<ProcessNode> result = new LinkedList<ProcessNode>();

        for (ProcessEdge e : getEdges()) {
            if (e.getSource() == n) {
                result.add(e.getTarget());
            }
        }

        return result;
    }
}
