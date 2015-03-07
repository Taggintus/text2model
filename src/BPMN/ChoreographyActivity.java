package BPMN;

import java.util.LinkedList;
import java.util.List;

import Nodes.FlowObject;
import Nodes.ProcessNode;

public class ChoreographyActivity extends FlowObject {

    @Override
    public List<Class<? extends ProcessNode>> getVariants() {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        result.add(ChoreographyTask.class);
        result.add(ChoreographySubProcess.class);
        return result;
    }


}
