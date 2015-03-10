/**
 * copyright
 * Inubit AG
 * Schoeneberger Ufer 89
 * 10785 Berlin
 * Germany
 */
package OrgChart;

import java.util.List;
import java.util.LinkedList;

import Models.ProcessModel;
import Nodes.ProcessEdge;
import Nodes.ProcessNode;
import processing.ProcessUtils;


/**
 * @author ff
 *
 */
public class OrgChartUtils extends ProcessUtils {

    @Override
    public ProcessEdge createDefaultEdge(ProcessNode source, ProcessNode target) {
        return new Connection(source, target);
    }

    @Override
    public List<Class<? extends ProcessNode>> getNextNodesRecommendation(
            ProcessModel model, ProcessNode node) {
        List<Class<? extends ProcessNode>> result = new LinkedList<Class<? extends ProcessNode>>();
        if (node instanceof OrgUnit) {
            result.add(OrgUnit.class);
            result.add(ManagerialRole.class);
            result.add(Role.class);
        }
        if (node instanceof ManagerialRole | node instanceof Role) {
            result.add(Person.class);
            result.add(Substitute.class);
        }

        return result;
    }
}
