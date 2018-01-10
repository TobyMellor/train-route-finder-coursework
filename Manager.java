package com.TobyMellor.TrainRouteFinder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Abstract <code>Manager</code> Class contains abstract methods and functions all
 * of the managers use.
 *
 * Used for managing XML files
 *
 * @author <a href="mailto:T.Mellor-16@student.lboro.ac.uk">Toby Mellor (B619693)</a>
 *
 * @version $Revision: 1.0.0 $, $Date: 2017/03/27 19:32:00 $
 */

abstract public class Manager {
    /**
     * A required function by any manager that extends this class
     */
    abstract public void save();

    /**
     * Creates an XML element with a name and content for a given document
     *
     * Function compacts the code needed to create an XML element
     *
     * @param document    the XML document
     * @param elementName what the XML tagname will be
     * @param content     the text content contained within the element
     *
     * @return Element
     */
    protected Element createXMLElement(Document document, String elementName, String content) {
        Element element = document.createElement(elementName);
        element.setTextContent(content);

        return element;
    }

    /**
     * Replaces a given file with a new XML document
     *
     * @param document the XML document
     * @param file     an existing XML file that will be overridden
     */
    protected void writeXML(Document document, File file) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
