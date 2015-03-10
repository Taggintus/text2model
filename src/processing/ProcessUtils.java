package processing;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Nodes.ProcessEdge;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

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
    
    public static HashMap<String, String> readProperties(XPath xpath, Node node) {
        HashMap<String, String> props = new HashMap<String, String>();
        String query;
        Object res;
        // Get all properties
        query = "./" + TAG_PROPERTY;
        try {
            res = xpath.evaluate(query, node, XPathConstants.NODESET);
        } catch (Exception ex) {
            ex.printStackTrace();
            return props;
        }
        NodeList propertyNodes = (NodeList) res;

        for (int i1 = 0; i1 < propertyNodes.getLength(); i1++) {
            Element property = (Element) propertyNodes.item(i1);

            String key = property.getAttribute(ATTR_NAME);
            String value = property.getAttribute(ATTR_VALUE);

            // Hack to update old ProcessModels with editable sources and targets
            if (key.equals("sourceNode")) {
                key = ProcessEdge.PROP_SOURCENODE;
            }
            if (key.equals("targetNode")) {
                key = ProcessEdge.PROP_TARGETNODE;
            }

            props.put(key, value);
        }
        return props;
    }
}