/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package OrgChart;

import Nodes.ProcessNode;


public abstract class OrgChartElement extends ProcessNode {

	
	private boolean f_hasLine = false;
	
	/**
	 * @return the f_hasLine
	 */
	public boolean hasLine() {
		return f_hasLine;
	}

	/**
	 * @param line the f_hasLine to set
	 */
	public void setHasLine(boolean line) {
		f_hasLine = line;
	}

	
	/**
	 * 
	 */
	public OrgChartElement() {
	}

}
