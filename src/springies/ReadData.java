package springies;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.HashMap;
import jboxGlue.Assembly;
import jboxGlue.Factory.Factory;
import jboxGlue.Factory.AllFactories;


/**
 * Class to read XML file. It's more flexible cause it can specifies
 * Different files for different features. So clients can make combination.
 * readEnvironment read environment variables.
 * readAssembly read a new Assembly.
 */
public class ReadData {

    
    private static final String LINKS_XML_NAME = "links";
    private static final String NODES_XML_NAME = "nodes";
    private static final String WALL_XML_NAME = "wall";
    private static final String DIRECTION_XML_NAME = "direction";
    private static final String GRAVITY_XML_NAME = "gravity";
    private static final String EXPONENT_XML_NAME = "exponent";
    private static final String CENTERMASS_XML_NAME = "centermass";
    private static final String MAGNITUDE_XML_NAME = "magnitude";
    private static final String VISCOSITY_XML_NAME = "viscosity";
    private AllFactories factoryHashMap = new AllFactories();

    /**
     * Constructor
     */
    protected ReadData () {

    }

    /**
     * @param myXml: input File
     * @return: document to further read the data
     */
    private Document createDoc (File myXml) {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(myXml);
            doc.getDocumentElement().normalize();
            return doc;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * read a new Assembly by a file. Then add the assembly to the environment
     * so that we can keep track of them.
     * 
     * @param xml File
     * 
     */
    protected void readAssembly (File xml) {
        Document doc = createDoc(xml);
        Assembly newAssembly = new Assembly();
        EnvironmentVariables.addToAssemblyList(newAssembly);
        readPhysicalObjects(NODES_XML_NAME, newAssembly, doc);
        readPhysicalObjects(LINKS_XML_NAME, newAssembly, doc);
        
    }

    /**
     * call read a list to all the nodes whose parent node has the name string.
     * 
     * @param name the node name wants to read.
     * @param assembly: assembly where to put the objects readed
     * @param doc: document
     */
    private void readPhysicalObjects (String name, Assembly assembly, Document doc) {
        NodeList nList = doc.getElementsByTagName(name);
        nList = nList.item(0).getChildNodes();
        readAList(nList, assembly);
    }

    /**
     * Add walls. The different with addWalls is because it is
     * always called in environment. So just pass a document is
     * OK.
     * 
     * @param doc : the doc for walls.
     */
    private void readWalls (Document doc) {
        NodeList nList = doc.getElementsByTagName(WALL_XML_NAME);
        readAList(nList, null);
    }

    /**
     * read all the node in a list with a certain method
     * 
     */
    private void readAList (NodeList nList, Assembly assembly) {
        for (int nodeIndex = 0; nodeIndex < nList.getLength(); nodeIndex++) {
            Node nNode = nList.item(nodeIndex);
            readOneItem(nNode, assembly);

        }
    }

    /**
     * read one Item read all the information in a node. Put all the information in a hashmap and
     * pass
     * it to the factory.
     * Use the node name to decide which factory to use.
     * CLOSE the readdata Class because when adding new kinds of object, just add a new kind of
     * factory
     * and put this kind of factory in factory hashmap is OK.Let the polymorphism to do the rest.
     * 
     * @param nNode the node which is read
     * @param assembly: where to put the object
     */
    private void readOneItem (Node nNode, Assembly assembly) {
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

            Element eElement = (Element) nNode;
            Factory factory = factoryHashMap.getFactory(nNode.getNodeName());
            NamedNodeMap oneElement = eElement.getAttributes();
            HashMap<String, String> factoryInformation = new HashMap<String, String>();
            for (int i = 0; i < oneElement.getLength(); i++) {
                String key = oneElement.item(i).getNodeName();
                String value = oneElement.item(i).getNodeValue();
                factoryInformation.put(key, value);
            }
            factory.create(factoryInformation, assembly);

        }
    }

    /**
     * read all the environment variables
     * 
     * @param filename: filename which defines environment variables
     */
    protected void readEnvironment (File xml) {
        if (xml == null) { return; }
        Document doc = createDoc(xml);

        EnvironmentVariables.setViscosity(getEnvironmentValue(VISCOSITY_XML_NAME, MAGNITUDE_XML_NAME, doc));
        EnvironmentVariables.setCenterMassMagnitude(getEnvironmentValue(CENTERMASS_XML_NAME, MAGNITUDE_XML_NAME,
                                                                        doc));
        EnvironmentVariables.setCenterMassExponent(getEnvironmentValue(CENTERMASS_XML_NAME, EXPONENT_XML_NAME,
                                                                       doc));
        double gravityMagnitude = getEnvironmentValue(GRAVITY_XML_NAME, MAGNITUDE_XML_NAME, doc);
        double gravityDirection = getEnvironmentValue(GRAVITY_XML_NAME, DIRECTION_XML_NAME, doc);
        EnvironmentVariables.initGravity(gravityMagnitude, gravityDirection);
        readWalls(doc);

    }

    /**
     * read a attribution of a certain node. If no attribution found
     * or no valid node found return 0.0. Suitable to read environment
     * variable because there's usually one unique attribution and the
     * default value is set to be 0.0 for all these environment variables.
     * 
     * @param name : the name of node
     * @param attrib: the name of the attribution
     * @param doc: the document
     * @return value of the attribution
     */
    private double getEnvironmentValue (String name, String attribute, Document doc) {
        NodeList tempNodeList = doc.getElementsByTagName(name);
        if (tempNodeList.item(0).getNodeType() != Node.ELEMENT_NODE) { return 0.0; }
        String stringValue = ((Element) (tempNodeList.item(0))).getAttribute(attribute);
        if (stringValue.equals("")) {
            return 0.0; 
        }    
        return Double.parseDouble(stringValue);

    }

}
