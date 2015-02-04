package Models;

import java.util.LinkedList;
import Nodes.ProcessNode;
import Nodes.ProcessEdge;

public abstract class ProcessModel {

	/**
	 * List of ProcessNodes
	 */
	private LinkedList<ProcessNode> processNodes = new LinkedList<ProcessNode>();
	/**
	 * List of Arcs
	 */
	private LinkedList<ProcessEdge> Arcs = new LinkedList<ProcessEdge>();
	
	
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
	public synchronized void addArc(Arc a) {
		Arcs.add(a);
	}
	
	
	/**
	 * deletes an Arc from the set of Arcs
	 * @param pn
	 */
	public synchronized void removeArc(Arc a) {
		Arcs.remove(a);
	}
	
	
	/**
	 * returns the set of Arcs
	 * @return
	 */
	public synchronized LinkedList<Arc> getArcs () {
		return Arcs;
	}
	
	
	/**
     * Returns the list of supported edge classes for this model. This
     * method needs to be implemented.
     * @return
     */
    public abstract LinkedList<Class<? extends Arc>> getSupportedEdgeClasses();
    
    
    /**
     * Returns the list of supported node classes for this model. This 
     * method needs to be implemented.
     * @return
     */
    public abstract LinkedList<Class<? extends ProcessNode>> getSupportedNodeClasses();
    
    
    /**
     * Returns a single string describing the kind of model this ProcessModel
     * supports.
     * @return
     */
    public abstract String getDescription();
}
