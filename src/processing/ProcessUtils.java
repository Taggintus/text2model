package processing;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * This class provides static methods for drawing process elements.
 * 
 * @author frank
 */
public abstract class ProcessUtils {

    public final static String ATTR_NAME = "name";
    public final static String ATTR_VALUE = "value";
    public final static String TAG_PROPERTY = "property";
    public final static String TAG_PROPERTIES = "properties";

    public final static String TRANS_PROP_CREDENTIALS = "credentials";

    public enum Orientation {

        TOP, CENTER, RIGHT, LEFT
    }

    public enum Position {

        TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT
    }
  
    
    /**
     * helper method that writes a Properties Map to an xmlNode
     * @param xmlDoc
     * @param nodeToAddTo
     * @param properties
     */
    public static void writeProperties(Document xmlDoc, Element nodeToAddTo, HashMap<String, String> props) {
        // Insert all properties
        for (String key : props.keySet()) {
            Element property = xmlDoc.createElement(TAG_PROPERTY);
            property.setAttribute(ATTR_NAME, key);
            property.setAttribute(ATTR_VALUE, props.get(key));
            nodeToAddTo.appendChild(property);
        }
    }
}