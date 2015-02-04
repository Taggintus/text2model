package Nodes;

public class ProcessNode {
	
	private String ID = null;
	private String name = null;
	
	public ProcessNode(String x) {
		this.setName(x);
	}
	
	public ProcessNode() {
	}
	
	public String getID () {
		return ID;
	}
	
	public void setID (String x) {
		ID = x;
	}
	
	public String getName () {
		return name;
	}
	
	public void setName (String x) {
		name = x;
	}

	
}
