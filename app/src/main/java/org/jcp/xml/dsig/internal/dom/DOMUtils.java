package org.jcp.xml.dsig.internal.dom;

import java.security.spec.AlgorithmParameterSpec;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import javax.xml.crypto.dsig.spec.XPathType;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMUtils.class */
public final class DOMUtils {
    private DOMUtils() {
    }

    public static Document getOwnerDocument(Node node) {
        if (node.getNodeType() == 9) {
            return (Document) node;
        }
        return node.getOwnerDocument();
    }

    public static String getQNameString(String str, String str2) {
        return (str == null || str.length() == 0) ? str2 : str + CallSiteDescriptor.TOKEN_DELIMITER + str2;
    }

    public static Element createElement(Document document, String str, String str2, String str3) {
        return document.createElementNS(str2, (str3 == null || str3.length() == 0) ? str : str3 + CallSiteDescriptor.TOKEN_DELIMITER + str);
    }

    public static void setAttribute(Element element, String str, String str2) throws DOMException {
        if (str2 == null) {
            return;
        }
        element.setAttributeNS(null, str, str2);
    }

    public static void setAttributeID(Element element, String str, String str2) throws DOMException {
        if (str2 == null) {
            return;
        }
        element.setAttributeNS(null, str, str2);
        element.setIdAttributeNS(null, str, true);
    }

    public static Element getFirstChildElement(Node node) {
        Node node2;
        Node firstChild = node.getFirstChild();
        while (true) {
            node2 = firstChild;
            if (node2 == null || node2.getNodeType() == 1) {
                break;
            }
            firstChild = node2.getNextSibling();
        }
        return (Element) node2;
    }

    @Deprecated
    public static Element getFirstChildElement(Node node, String str) throws MarshalException {
        return verifyElement(getFirstChildElement(node), str);
    }

    public static Element getFirstChildElement(Node node, String str, String str2) throws MarshalException {
        return verifyElement(getFirstChildElement(node), str, str2);
    }

    private static Element verifyElement(Element element, String str) throws MarshalException {
        if (element == null) {
            throw new MarshalException("Missing " + str + " element");
        }
        String localName = element.getLocalName();
        if (!localName.equals(str)) {
            throw new MarshalException("Invalid element name: " + localName + ", expected " + str);
        }
        return element;
    }

    private static Element verifyElement(Element element, String str, String str2) throws MarshalException {
        if (element == null) {
            throw new MarshalException("Missing " + str + " element");
        }
        String localName = element.getLocalName();
        String namespaceURI = element.getNamespaceURI();
        if (!localName.equals(str) || ((namespaceURI == null && str2 != null) || (namespaceURI != null && !namespaceURI.equals(str2)))) {
            throw new MarshalException("Invalid element name: " + namespaceURI + CallSiteDescriptor.TOKEN_DELIMITER + localName + ", expected " + str2 + CallSiteDescriptor.TOKEN_DELIMITER + str);
        }
        return element;
    }

    public static Element getLastChildElement(Node node) {
        Node node2;
        Node lastChild = node.getLastChild();
        while (true) {
            node2 = lastChild;
            if (node2 == null || node2.getNodeType() == 1) {
                break;
            }
            lastChild = node2.getPreviousSibling();
        }
        return (Element) node2;
    }

    public static Element getNextSiblingElement(Node node) {
        Node node2;
        Node nextSibling = node.getNextSibling();
        while (true) {
            node2 = nextSibling;
            if (node2 == null || node2.getNodeType() == 1) {
                break;
            }
            nextSibling = node2.getNextSibling();
        }
        return (Element) node2;
    }

    @Deprecated
    public static Element getNextSiblingElement(Node node, String str) throws MarshalException {
        return verifyElement(getNextSiblingElement(node), str);
    }

    public static Element getNextSiblingElement(Node node, String str, String str2) throws MarshalException {
        return verifyElement(getNextSiblingElement(node), str, str2);
    }

    public static String getAttributeValue(Element element, String str) throws DOMException {
        Attr attributeNodeNS = element.getAttributeNodeNS(null, str);
        if (attributeNodeNS == null) {
            return null;
        }
        return attributeNodeNS.getValue();
    }

    public static <N> String getIdAttributeValue(Element element, String str) throws DOMException {
        Attr attributeNodeNS = element.getAttributeNodeNS(null, str);
        if (attributeNodeNS != null && !attributeNodeNS.isId()) {
            element.setIdAttributeNode(attributeNodeNS, true);
        }
        if (attributeNodeNS == null) {
            return null;
        }
        return attributeNodeNS.getValue();
    }

    public static Set<Node> nodeSet(NodeList nodeList) {
        return new NodeSet(nodeList);
    }

    /* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMUtils$NodeSet.class */
    static class NodeSet extends AbstractSet<Node> {
        private NodeList nl;

        public NodeSet(NodeList nodeList) {
            this.nl = nodeList;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.nl.getLength();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Node> iterator() {
            return new Iterator<Node>() { // from class: org.jcp.xml.dsig.internal.dom.DOMUtils.NodeSet.1
                private int index;

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Node next() {
                    if (hasNext()) {
                        NodeList nodeList = NodeSet.this.nl;
                        int i2 = this.index;
                        this.index = i2 + 1;
                        return nodeList.item(i2);
                    }
                    throw new NoSuchElementException();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.index < NodeSet.this.nl.getLength();
                }
            };
        }
    }

    public static String getNSPrefix(XMLCryptoContext xMLCryptoContext, String str) {
        if (xMLCryptoContext != null) {
            return xMLCryptoContext.getNamespacePrefix(str, xMLCryptoContext.getDefaultNamespacePrefix());
        }
        return null;
    }

    public static String getSignaturePrefix(XMLCryptoContext xMLCryptoContext) {
        return getNSPrefix(xMLCryptoContext, "http://www.w3.org/2000/09/xmldsig#");
    }

    public static void removeAllChildren(Node node) throws DOMException {
        Node firstChild = node.getFirstChild();
        while (firstChild != null) {
            Node node2 = firstChild;
            firstChild = firstChild.getNextSibling();
            node.removeChild(node2);
        }
    }

    public static boolean nodesEqual(Node node, Node node2) {
        if (node != node2 && node.getNodeType() != node2.getNodeType()) {
            return false;
        }
        return true;
    }

    public static void appendChild(Node node, Node node2) throws DOMException {
        Document ownerDocument = getOwnerDocument(node);
        if (node2.getOwnerDocument() != ownerDocument) {
            node.appendChild(ownerDocument.importNode(node2, true));
        } else {
            node.appendChild(node2);
        }
    }

    public static boolean paramsEqual(AlgorithmParameterSpec algorithmParameterSpec, AlgorithmParameterSpec algorithmParameterSpec2) {
        if (algorithmParameterSpec == algorithmParameterSpec2) {
            return true;
        }
        if ((algorithmParameterSpec instanceof XPathFilter2ParameterSpec) && (algorithmParameterSpec2 instanceof XPathFilter2ParameterSpec)) {
            return paramsEqual((XPathFilter2ParameterSpec) algorithmParameterSpec, (XPathFilter2ParameterSpec) algorithmParameterSpec2);
        }
        if ((algorithmParameterSpec instanceof ExcC14NParameterSpec) && (algorithmParameterSpec2 instanceof ExcC14NParameterSpec)) {
            return paramsEqual((ExcC14NParameterSpec) algorithmParameterSpec, (ExcC14NParameterSpec) algorithmParameterSpec2);
        }
        if ((algorithmParameterSpec instanceof XPathFilterParameterSpec) && (algorithmParameterSpec2 instanceof XPathFilterParameterSpec)) {
            return paramsEqual((XPathFilterParameterSpec) algorithmParameterSpec, (XPathFilterParameterSpec) algorithmParameterSpec2);
        }
        if ((algorithmParameterSpec instanceof XSLTTransformParameterSpec) && (algorithmParameterSpec2 instanceof XSLTTransformParameterSpec)) {
            return paramsEqual((XSLTTransformParameterSpec) algorithmParameterSpec, (XSLTTransformParameterSpec) algorithmParameterSpec2);
        }
        return false;
    }

    private static boolean paramsEqual(XPathFilter2ParameterSpec xPathFilter2ParameterSpec, XPathFilter2ParameterSpec xPathFilter2ParameterSpec2) {
        List xPathList = xPathFilter2ParameterSpec.getXPathList();
        List xPathList2 = xPathFilter2ParameterSpec2.getXPathList();
        int size = xPathList.size();
        if (size != xPathList2.size()) {
            return false;
        }
        for (int i2 = 0; i2 < size; i2++) {
            XPathType xPathType = (XPathType) xPathList.get(i2);
            XPathType xPathType2 = (XPathType) xPathList2.get(i2);
            if (!xPathType.getExpression().equals(xPathType2.getExpression()) || !xPathType.getNamespaceMap().equals(xPathType2.getNamespaceMap()) || xPathType.getFilter() != xPathType2.getFilter()) {
                return false;
            }
        }
        return true;
    }

    private static boolean paramsEqual(ExcC14NParameterSpec excC14NParameterSpec, ExcC14NParameterSpec excC14NParameterSpec2) {
        return excC14NParameterSpec.getPrefixList().equals(excC14NParameterSpec2.getPrefixList());
    }

    private static boolean paramsEqual(XPathFilterParameterSpec xPathFilterParameterSpec, XPathFilterParameterSpec xPathFilterParameterSpec2) {
        return xPathFilterParameterSpec.getXPath().equals(xPathFilterParameterSpec2.getXPath()) && xPathFilterParameterSpec.getNamespaceMap().equals(xPathFilterParameterSpec2.getNamespaceMap());
    }

    private static boolean paramsEqual(XSLTTransformParameterSpec xSLTTransformParameterSpec, XSLTTransformParameterSpec xSLTTransformParameterSpec2) {
        XMLStructure stylesheet = xSLTTransformParameterSpec2.getStylesheet();
        if (!(stylesheet instanceof javax.xml.crypto.dom.DOMStructure)) {
            return false;
        }
        return nodesEqual(((javax.xml.crypto.dom.DOMStructure) xSLTTransformParameterSpec.getStylesheet()).getNode(), ((javax.xml.crypto.dom.DOMStructure) stylesheet).getNode());
    }

    public static boolean isNamespace(Node node) {
        if (node.getNodeType() == 2) {
            return "http://www.w3.org/2000/xmlns/".equals(node.getNamespaceURI());
        }
        return false;
    }
}
