package Models;

import java.util.LinkedList;

import Nodes.ProcessEdge;
import Nodes.ProcessNode;

public class EPCModel extends ProcessModel {

	@Override
	public LinkedList<Class<? extends ProcessEdge>> getSupportedEdgeClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Class<? extends ProcessNode>> getSupportedNodeClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "EPML 2.0";
	}

}
