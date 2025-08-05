package com.sun.xml.internal.ws.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/DOMUtil.class */
public class DOMUtil {
    private static DocumentBuilder db;

    public static Document createDom() {
        Document documentNewDocument;
        synchronized (DOMUtil.class) {
            if (db == null) {
                try {
                    DocumentBuilderFactory dbf = XmlUtil.newDocumentBuilderFactory();
                    db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException e2) {
                    throw new FactoryConfigurationError(e2);
                }
            }
            documentNewDocument = db.newDocument();
        }
        return documentNewDocument;
    }

    public static void serializeNode(Element node, XMLStreamWriter writer) throws XMLStreamException {
        writeTagWithAttributes(node, writer);
        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i2 = 0; i2 < children.getLength(); i2++) {
                Node child = children.item(i2);
                switch (child.getNodeType()) {
                    case 1:
                        serializeNode((Element) child, writer);
                        break;
                    case 3:
                        writer.writeCharacters(child.getNodeValue());
                        break;
                    case 4:
                        writer.writeCData(child.getNodeValue());
                        break;
                    case 7:
                        writer.writeProcessingInstruction(child.getNodeValue());
                        break;
                    case 8:
                        writer.writeComment(child.getNodeValue());
                        break;
                }
            }
        }
        writer.writeEndElement();
    }

    public static void writeTagWithAttributes(Element node, XMLStreamWriter writer) throws XMLStreamException {
        String nodePrefix = fixNull(node.getPrefix());
        String nodeNS = fixNull(node.getNamespaceURI());
        String nodeLocalName = node.getLocalName() == null ? node.getNodeName() : node.getLocalName();
        boolean prefixDecl = isPrefixDeclared(writer, nodeNS, nodePrefix);
        writer.writeStartElement(nodePrefix, nodeLocalName, nodeNS);
        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            int numOfAttributes = attrs.getLength();
            for (int i2 = 0; i2 < numOfAttributes; i2++) {
                Node attr = attrs.item(i2);
                String nsUri = fixNull(attr.getNamespaceURI());
                if (nsUri.equals("http://www.w3.org/2000/xmlns/")) {
                    String local = attr.getLocalName().equals("xmlns") ? "" : attr.getLocalName();
                    if (local.equals(nodePrefix) && attr.getNodeValue().equals(nodeNS)) {
                        prefixDecl = true;
                    }
                    if (local.equals("")) {
                        writer.writeDefaultNamespace(attr.getNodeValue());
                    } else {
                        writer.setPrefix(attr.getLocalName(), attr.getNodeValue());
                        writer.writeNamespace(attr.getLocalName(), attr.getNodeValue());
                    }
                }
            }
        }
        if (!prefixDecl) {
            writer.writeNamespace(nodePrefix, nodeNS);
        }
        if (node.hasAttributes()) {
            NamedNodeMap attrs2 = node.getAttributes();
            int numOfAttributes2 = attrs2.getLength();
            for (int i3 = 0; i3 < numOfAttributes2; i3++) {
                Node attr2 = attrs2.item(i3);
                String attrPrefix = fixNull(attr2.getPrefix());
                String attrNS = fixNull(attr2.getNamespaceURI());
                if (!attrNS.equals("http://www.w3.org/2000/xmlns/")) {
                    String localName = attr2.getLocalName();
                    if (localName == null) {
                        localName = attr2.getNodeName();
                    }
                    boolean attrPrefixDecl = isPrefixDeclared(writer, attrNS, attrPrefix);
                    if (!attrPrefix.equals("") && !attrPrefixDecl) {
                        writer.setPrefix(attr2.getLocalName(), attr2.getNodeValue());
                        writer.writeNamespace(attrPrefix, attrNS);
                    }
                    writer.writeAttribute(attrPrefix, attrNS, localName, attr2.getNodeValue());
                }
            }
        }
    }

    private static boolean isPrefixDeclared(XMLStreamWriter writer, String nsUri, String prefix) {
        boolean prefixDecl = false;
        NamespaceContext nscontext = writer.getNamespaceContext();
        Iterator prefixItr = nscontext.getPrefixes(nsUri);
        while (true) {
            if (!prefixItr.hasNext()) {
                break;
            }
            if (prefix.equals(prefixItr.next())) {
                prefixDecl = true;
                break;
            }
        }
        return prefixDecl;
    }

    public static Element getFirstChild(Element e2, String nsUri, String local) {
        Node firstChild = e2.getFirstChild();
        while (true) {
            Node n2 = firstChild;
            if (n2 != null) {
                if (n2.getNodeType() == 1) {
                    Element c2 = (Element) n2;
                    if (c2.getLocalName().equals(local) && c2.getNamespaceURI().equals(nsUri)) {
                        return c2;
                    }
                }
                firstChild = n2.getNextSibling();
            } else {
                return null;
            }
        }
    }

    @NotNull
    private static String fixNull(@Nullable String s2) {
        if (s2 == null) {
            return "";
        }
        return s2;
    }

    @Nullable
    public static Element getFirstElementChild(Node parent) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node n2 = firstChild;
            if (n2 != null) {
                if (n2.getNodeType() != 1) {
                    firstChild = n2.getNextSibling();
                } else {
                    return (Element) n2;
                }
            } else {
                return null;
            }
        }
    }

    @NotNull
    public static List<Element> getChildElements(Node parent) {
        List<Element> elements = new ArrayList<>();
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node n2 = firstChild;
            if (n2 != null) {
                if (n2.getNodeType() == 1) {
                    elements.add((Element) n2);
                }
                firstChild = n2.getNextSibling();
            } else {
                return elements;
            }
        }
    }
}
