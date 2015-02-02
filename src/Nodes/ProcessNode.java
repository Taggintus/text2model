package Nodes;

public class ProcessNode {
	
	private String ID;
	private String name;
	
	public ProcessNode(String x) {
		this.setName(x);
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
