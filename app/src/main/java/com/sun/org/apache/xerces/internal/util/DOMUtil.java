package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.dom.AttrImpl;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xerces.internal.impl.xs.opti.ElementImpl;
import com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl;
import java.lang.reflect.Method;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/DOMUtil.class */
public class DOMUtil {
    protected DOMUtil() {
    }

    public static void copyInto(Node src, Node dest) throws DOMException {
        Node node;
        Document factory = dest.getOwnerDocument();
        boolean domimpl = factory instanceof DocumentImpl;
        Node parent = src;
        Node place = src;
        while (place != null) {
            int type = place.getNodeType();
            switch (type) {
                case 1:
                    Element element = factory.createElement(place.getNodeName());
                    node = element;
                    NamedNodeMap attrs = place.getAttributes();
                    int attrCount = attrs.getLength();
                    for (int i2 = 0; i2 < attrCount; i2++) {
                        Attr attr = (Attr) attrs.item(i2);
                        String attrName = attr.getNodeName();
                        String attrValue = attr.getNodeValue();
                        element.setAttribute(attrName, attrValue);
                        if (domimpl && !attr.getSpecified()) {
                            ((AttrImpl) element.getAttributeNode(attrName)).setSpecified(false);
                        }
                    }
                    break;
                case 2:
                case 6:
                default:
                    throw new IllegalArgumentException("can't copy node type, " + type + " (" + place.getNodeName() + ')');
                case 3:
                    node = factory.createTextNode(place.getNodeValue());
                    break;
                case 4:
                    node = factory.createCDATASection(place.getNodeValue());
                    break;
                case 5:
                    node = factory.createEntityReference(place.getNodeName());
                    break;
                case 7:
                    node = factory.createProcessingInstruction(place.getNodeName(), place.getNodeValue());
                    break;
                case 8:
                    node = factory.createComment(place.getNodeValue());
                    break;
            }
            dest.appendChild(node);
            if (place.hasChildNodes()) {
                parent = place;
                place = place.getFirstChild();
                dest = node;
            } else {
                place = place.getNextSibling();
                while (place == null && parent != src) {
                    place = parent.getNextSibling();
                    parent = parent.getParentNode();
                    dest = dest.getParentNode();
                }
            }
        }
    }

    public static Element getFirstChildElement(Node parent) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    return (Element) child;
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getFirstVisibleChildElement(Node parent) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1 && !isHidden(child)) {
                    return (Element) child;
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getFirstVisibleChildElement(Node parent, Map<Node, String> hiddenNodes) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1 && !isHidden(child, hiddenNodes)) {
                    return (Element) child;
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastChildElement(Node parent) {
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    return (Element) child;
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastVisibleChildElement(Node parent) {
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1 && !isHidden(child)) {
                    return (Element) child;
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastVisibleChildElement(Node parent, Map<Node, String> hiddenNodes) {
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1 && !isHidden(child, hiddenNodes)) {
                    return (Element) child;
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextSiblingElement(Node node) {
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1) {
                    return (Element) sibling;
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextVisibleSiblingElement(Node node) {
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1 && !isHidden(sibling)) {
                    return (Element) sibling;
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextVisibleSiblingElement(Node node, Map<Node, String> hiddenNodes) {
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1 && !isHidden(sibling, hiddenNodes)) {
                    return (Element) sibling;
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static void setHidden(Node node) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(true, false);
        } else if (node instanceof com.sun.org.apache.xerces.internal.dom.NodeImpl) {
            ((com.sun.org.apache.xerces.internal.dom.NodeImpl) node).setReadOnly(true, false);
        }
    }

    public static void setHidden(Node node, Map<Node, String> hiddenNodes) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(true, false);
        } else {
            hiddenNodes.put(node, "");
        }
    }

    public static void setVisible(Node node) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(false, false);
        } else if (node instanceof com.sun.org.apache.xerces.internal.dom.NodeImpl) {
            ((com.sun.org.apache.xerces.internal.dom.NodeImpl) node).setReadOnly(false, false);
        }
    }

    public static void setVisible(Node node, Map<Node, String> hiddenNodes) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(false, false);
        } else {
            hiddenNodes.remove(node);
        }
    }

    public static boolean isHidden(Node node) {
        if (node instanceof NodeImpl) {
            return ((NodeImpl) node).getReadOnly();
        }
        if (node instanceof com.sun.org.apache.xerces.internal.dom.NodeImpl) {
            return ((com.sun.org.apache.xerces.internal.dom.NodeImpl) node).getReadOnly();
        }
        return false;
    }

    public static boolean isHidden(Node node, Map<Node, String> hiddenNodes) {
        if (node instanceof NodeImpl) {
            return ((NodeImpl) node).getReadOnly();
        }
        return hiddenNodes.containsKey(node);
    }

    public static Element getFirstChildElement(Node parent, String elemName) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1 && child.getNodeName().equals(elemName)) {
                    return (Element) child;
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastChildElement(Node parent, String elemName) {
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1 && child.getNodeName().equals(elemName)) {
                    return (Element) child;
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextSiblingElement(Node node, String elemName) {
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1 && sibling.getNodeName().equals(elemName)) {
                    return (Element) sibling;
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getFirstChildElementNS(Node parent, String uri, String localpart) {
        String childURI;
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1 && (childURI = child.getNamespaceURI()) != null && childURI.equals(uri) && child.getLocalName().equals(localpart)) {
                    return (Element) child;
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastChildElementNS(Node parent, String uri, String localpart) {
        String childURI;
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1 && (childURI = child.getNamespaceURI()) != null && childURI.equals(uri) && child.getLocalName().equals(localpart)) {
                    return (Element) child;
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextSiblingElementNS(Node node, String uri, String localpart) {
        String siblingURI;
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1 && (siblingURI = sibling.getNamespaceURI()) != null && siblingURI.equals(uri) && sibling.getLocalName().equals(localpart)) {
                    return (Element) sibling;
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getFirstChildElement(Node parent, String[] elemNames) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    for (String str : elemNames) {
                        if (child.getNodeName().equals(str)) {
                            return (Element) child;
                        }
                    }
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastChildElement(Node parent, String[] elemNames) {
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    for (String str : elemNames) {
                        if (child.getNodeName().equals(str)) {
                            return (Element) child;
                        }
                    }
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextSiblingElement(Node node, String[] elemNames) {
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1) {
                    for (String str : elemNames) {
                        if (sibling.getNodeName().equals(str)) {
                            return (Element) sibling;
                        }
                    }
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getFirstChildElementNS(Node parent, String[][] elemNames) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    for (int i2 = 0; i2 < elemNames.length; i2++) {
                        String uri = child.getNamespaceURI();
                        if (uri != null && uri.equals(elemNames[i2][0]) && child.getLocalName().equals(elemNames[i2][1])) {
                            return (Element) child;
                        }
                    }
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastChildElementNS(Node parent, String[][] elemNames) {
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    for (int i2 = 0; i2 < elemNames.length; i2++) {
                        String uri = child.getNamespaceURI();
                        if (uri != null && uri.equals(elemNames[i2][0]) && child.getLocalName().equals(elemNames[i2][1])) {
                            return (Element) child;
                        }
                    }
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextSiblingElementNS(Node node, String[][] elemNames) {
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1) {
                    for (int i2 = 0; i2 < elemNames.length; i2++) {
                        String uri = sibling.getNamespaceURI();
                        if (uri != null && uri.equals(elemNames[i2][0]) && sibling.getLocalName().equals(elemNames[i2][1])) {
                            return (Element) sibling;
                        }
                    }
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getFirstChildElement(Node parent, String elemName, String attrName, String attrValue) {
        Node firstChild = parent.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    Element element = (Element) child;
                    if (element.getNodeName().equals(elemName) && element.getAttribute(attrName).equals(attrValue)) {
                        return element;
                    }
                }
                firstChild = child.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getLastChildElement(Node parent, String elemName, String attrName, String attrValue) {
        Node lastChild = parent.getLastChild();
        while (true) {
            Node child = lastChild;
            if (child != null) {
                if (child.getNodeType() == 1) {
                    Element element = (Element) child;
                    if (element.getNodeName().equals(elemName) && element.getAttribute(attrName).equals(attrValue)) {
                        return element;
                    }
                }
                lastChild = child.getPreviousSibling();
            } else {
                return null;
            }
        }
    }

    public static Element getNextSiblingElement(Node node, String elemName, String attrName, String attrValue) {
        Node nextSibling = node.getNextSibling();
        while (true) {
            Node sibling = nextSibling;
            if (sibling != null) {
                if (sibling.getNodeType() == 1) {
                    Element element = (Element) sibling;
                    if (element.getNodeName().equals(elemName) && element.getAttribute(attrName).equals(attrValue)) {
                        return element;
                    }
                }
                nextSibling = sibling.getNextSibling();
            } else {
                return null;
            }
        }
    }

    public static String getChildText(Node node) {
        if (node == null) {
            return null;
        }
        StringBuffer str = new StringBuffer();
        Node firstChild = node.getFirstChild();
        while (true) {
            Node child = firstChild;
            if (child != null) {
                short type = child.getNodeType();
                if (type == 3) {
                    str.append(child.getNodeValue());
                } else if (type == 4) {
                    str.append(getChildText(child));
                }
                firstChild = child.getNextSibling();
            } else {
                return str.toString();
            }
        }
    }

    public static String getName(Node node) {
        return node.getNodeName();
    }

    public static String getLocalName(Node node) {
        String name = node.getLocalName();
        return name != null ? name : node.getNodeName();
    }

    public static Element getParent(Element elem) {
        Node parent = elem.getParentNode();
        if (parent instanceof Element) {
            return (Element) parent;
        }
        return null;
    }

    public static Document getDocument(Node node) {
        return node.getOwnerDocument();
    }

    public static Element getRoot(Document doc) {
        return doc.getDocumentElement();
    }

    public static Attr getAttr(Element elem, String name) {
        return elem.getAttributeNode(name);
    }

    public static Attr getAttrNS(Element elem, String nsUri, String localName) {
        return elem.getAttributeNodeNS(nsUri, localName);
    }

    public static Attr[] getAttrs(Element elem) {
        NamedNodeMap attrMap = elem.getAttributes();
        Attr[] attrArray = new Attr[attrMap.getLength()];
        for (int i2 = 0; i2 < attrMap.getLength(); i2++) {
            attrArray[i2] = (Attr) attrMap.item(i2);
        }
        return attrArray;
    }

    public static String getValue(Attr attribute) {
        return attribute.getValue();
    }

    public static String getAttrValue(Element elem, String name) {
        return elem.getAttribute(name);
    }

    public static String getAttrValueNS(Element elem, String nsUri, String localName) {
        return elem.getAttributeNS(nsUri, localName);
    }

    public static String getPrefix(Node node) {
        return node.getPrefix();
    }

    public static String getNamespaceURI(Node node) {
        return node.getNamespaceURI();
    }

    public static String getAnnotation(Node node) {
        if (node instanceof ElementImpl) {
            return ((ElementImpl) node).getAnnotation();
        }
        return null;
    }

    public static String getSyntheticAnnotation(Node node) {
        if (node instanceof ElementImpl) {
            return ((ElementImpl) node).getSyntheticAnnotation();
        }
        return null;
    }

    public static DOMException createDOMException(short code, Throwable cause) {
        DOMException de2 = new DOMException(code, cause != null ? cause.getMessage() : null);
        if (cause != null && ThrowableMethods.fgThrowableMethodsAvailable) {
            try {
                ThrowableMethods.fgThrowableInitCauseMethod.invoke(de2, cause);
            } catch (Exception e2) {
            }
        }
        return de2;
    }

    public static LSException createLSException(short code, Throwable cause) {
        LSException lse = new LSException(code, cause != null ? cause.getMessage() : null);
        if (cause != null && ThrowableMethods.fgThrowableMethodsAvailable) {
            try {
                ThrowableMethods.fgThrowableInitCauseMethod.invoke(lse, cause);
            } catch (Exception e2) {
            }
        }
        return lse;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/DOMUtil$ThrowableMethods.class */
    static class ThrowableMethods {
        private static Method fgThrowableInitCauseMethod;
        private static boolean fgThrowableMethodsAvailable;

        static {
            fgThrowableInitCauseMethod = null;
            fgThrowableMethodsAvailable = false;
            try {
                fgThrowableInitCauseMethod = Throwable.class.getMethod("initCause", Throwable.class);
                fgThrowableMethodsAvailable = true;
            } catch (Exception e2) {
                fgThrowableInitCauseMethod = null;
                fgThrowableMethodsAvailable = false;
            }
        }

        private ThrowableMethods() {
        }
    }
}
