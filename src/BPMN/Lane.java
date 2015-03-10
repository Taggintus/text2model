package BPMN;

import java.util.LinkedList;

import tools.ReferenceChooserRestriction;
import Nodes.Linkable;
import OrgChart.ManagerialRole;
import OrgChart.Person;
import OrgChart.Role;

public class Lane extends LaneableCluster implements Linkable {
	
	private LaneableCluster parent;

    private static ReferenceChooserRestriction restrictions;

    /**
     * needed for deserialization
     */
    public Lane() {
        this("", 100, null);
    }

    /**
     * @param string
     * @param integer
     */
    public Lane(String name, Integer size, LaneableCluster parent) {
    	init();
        setText(name);
        this.parent = parent;
    }

    @Override
    public void setProperty(String key, String value) {
    	super.setProperty(key, value);
    }
    
    public LaneableCluster getParent() {
        return parent;
    }

    public Pool getSurroundingPool() {
        if ( this.getParent() != null ) {
            if ( this.getParent() instanceof Pool )
                return (Pool) this.getParent();
            else if ( this.getParent() instanceof Lane )
                return ((Lane) this.getParent()).getSurroundingPool();
        }
        
        return null;
    }
    
    /**
     * switches the parent of this Lane.
     * The old and new parent will get notified of the change
     * @param parent
     */
    public void setParent(LaneableCluster parent) {
        this.parent = parent;
    }
    
    @Override
    public boolean isVertical() {
    	//always the same as the parent
    	if(parent != null) {
    		return parent.isVertical();
    	}
    	return super.isVertical();
    }

    public ReferenceChooserRestriction getReferenceRestrictions() {
        if ( restrictions == null ) {
            LinkedList<Class> classes = new LinkedList<Class>();
            classes.add(Role.class);
            classes.add(ManagerialRole.class);
            classes.add(Person.class);
            restrictions = new ReferenceChooserRestriction(null, classes);
        }

        return restrictions;
    }
    
}
