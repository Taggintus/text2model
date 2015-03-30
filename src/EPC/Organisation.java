package EPC;

import java.util.LinkedList;

import tools.ReferenceChooserRestriction;
import Nodes.Linkable;

public class Organisation extends OrganisationCluster implements Linkable{

	private OrganisationCluster parent;

    private static ReferenceChooserRestriction restrictions;

    /**
     * needed for deserialization
     */
    public Organisation() {
        this("", 100, null);
    }

    /**
     * @param string
     * @param integer
     */
    public Organisation(String name, Integer size, OrganisationCluster parent) {
    	init();
        setText(name);
        this.parent = parent;
    }

    @Override
    public void setProperty(String key, String value) {
    	super.setProperty(key, value);
    }
    
    public OrganisationCluster getParent() {
        return parent;
    }

    public OrgCollection getSurroundingOrgCollection() {
        if ( this.getParent() != null ) {
            if ( this.getParent() instanceof OrgCollection )
                return (OrgCollection) this.getParent();
            else if ( this.getParent() instanceof Organisation )
                return ((Organisation) this.getParent()).getSurroundingOrgCollection();
        }
        
        return null;
    }
    
    /**
     * switches the parent of this Lane.
     * The old and new parent will get notified of the change
     * @param parent
     */
    public void setParent(OrganisationCluster parent) {
        this.parent = parent;
    }
    
    @Override
    public ReferenceChooserRestriction getReferenceRestrictions() {
        if ( restrictions == null ) {
            LinkedList<Class> classes = new LinkedList<Class>();
//            classes.add(Role.class);
//            classes.add(ManagerialRole.class);
//            classes.add(Person.class);
            restrictions = new ReferenceChooserRestriction(null, classes);
        }

        return restrictions;
    }
	
}
