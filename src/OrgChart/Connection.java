/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package OrgChart;


import Nodes.ProcessEdge;
import Nodes.ProcessNode;


/**
 * @author ff
 *
 */
public class Connection extends ProcessEdge {

	
	/**
	 * 
	 */
	public Connection() {
		super();
	}
	
	/**
	 * @param source
	 * @param target
	 */
	public Connection(ProcessNode source, ProcessNode target) {
		super(source,target);
	}


}
