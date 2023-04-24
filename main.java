import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.*;

// Mahmoud Hamdy Mohamed Mostafa 2001300
public class Main {
    private static List<Node> sort(NodeList classesNodeList) {
        List<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < classesNodeList.getLength(); i++) {
            nodes.add(classesNodeList.item(i));
        }
        nodes.sort(new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                return ((Element) o1).getElementsByTagName("SHORT-NAME").item(0).getTextContent().compareTo(
                        ((Element) o2).getElementsByTagName("SHORT-NAME").item(0).getTextContent());
            }
        });

        return nodes;
    }
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, TransformerException, EmptyAutosarFileException, NotValidAutosarFileException {
        String filename = args[0];
        if (!(filename.endsWith(".arxml"))) {
            throw new NotValidAutosarFileException("File is not .arxml");
        }

        File file = new File(filename);

        if(file.length() == 0)
        {
            throw new EmptyAutosarFileException("EmptyFile");
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("CONTAINER");

        List<Node> sorted = sort(nList);

        Document sortedARXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = sortedARXML.createElement("AUTOSAR");
        sortedARXML.appendChild(root);

        for (Node node : sorted) {
            Node copyNode = sortedARXML.importNode(node, true);
            root.appendChild(copyNode);
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(new File(filename.substring(0, filename.length()-6) + "_mod.arxml"));
        Source input = new DOMSource(sortedARXML);

        transformer.transform(input, output);
    }
}

class EmptyAutosarFileException extends RuntimeException {
    public EmptyAutosarFileException(String s)
    {
        super(s);
    }
}

class NotValidAutosarFileException extends Exception {
    public NotValidAutosarFileException(String s)
    {
        super(s);
    }
}
