package BPMN;

import java.util.LinkedList;
import java.util.List;

import Nodes.FlowObject;
import Nodes.ProcessNode;

public class Artifact extends FlowObject {
	
	@Override
    public List<Class<? extends ProcessNode>> getVariants() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(DataObject.class);
        result.add(DataStore.class);
        result.add(TextAnnotation.class);
        result.add(Group.class);
        result.add(UserArtifact.class);
        return result;
    }

}
