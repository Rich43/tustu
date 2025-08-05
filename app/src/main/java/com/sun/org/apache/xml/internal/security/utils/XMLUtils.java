package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/XMLUtils.class */
public final class XMLUtils {
    private static boolean lineFeedOnly = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.lineFeedOnly"));
    })).booleanValue();
    private static boolean ignoreLineBreaks = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.ignoreLineBreaks"));
    })).booleanValue();
    private static volatile String dsPrefix = "ds";
    private static volatile String ds11Prefix = "dsig11";
    private static volatile String xencPrefix = "xenc";
    private static volatile String xenc11Prefix = "xenc11";
    private static final Logger LOG = LoggerFactory.getLogger(XMLUtils.class);
    private static final Base64.Encoder LF_ENCODER = java.util.Base64.getMimeEncoder(76, new byte[]{10});

    private XMLUtils() {
    }

    public static void setDsPrefix(String str) {
        JavaUtils.checkRegisterPermission();
        dsPrefix = str;
    }

    public static void setDs11Prefix(String str) {
        JavaUtils.checkRegisterPermission();
        ds11Prefix = str;
    }

    public static void setXencPrefix(String str) {
        JavaUtils.checkRegisterPermission();
        xencPrefix = str;
    }

    public static void setXenc11Prefix(String str) {
        JavaUtils.checkRegisterPermission();
        xenc11Prefix = str;
    }

    public static Element getNextElement(Node node) {
        Node node2;
        Node nextSibling = node;
        while (true) {
            node2 = nextSibling;
            if (node2 == null || node2.getNodeType() == 1) {
                break;
            }
            nextSibling = node2.getNextSibling();
        }
        return (Element) node2;
    }

    public static void getSet(Node node, Set<Node> set, Node node2, boolean z2) {
        if (node2 != null && isDescendantOrSelf(node2, node)) {
            return;
        }
        getSetRec(node, set, node2, z2);
    }

    private static void getSetRec(Node node, Set<Node> set, Node node2, boolean z2) {
        if (node == node2) {
            return;
        }
        switch (node.getNodeType()) {
            case 1:
                set.add(node);
                Element element = (Element) node;
                if (element.hasAttributes()) {
                    NamedNodeMap attributes = element.getAttributes();
                    int length = attributes.getLength();
                    for (int i2 = 0; i2 < length; i2++) {
                        set.add(attributes.item(i2));
                    }
                    break;
                }
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                set.add(node);
                return;
            case 8:
                if (z2) {
                    set.add(node);
                    return;
                }
                return;
            case 9:
                break;
            case 10:
                return;
        }
        Node firstChild = node.getFirstChild();
        while (true) {
            Node nextSibling = firstChild;
            if (nextSibling != null) {
                if (nextSibling.getNodeType() == 3) {
                    set.add(nextSibling);
                    while (nextSibling != null && nextSibling.getNodeType() == 3) {
                        nextSibling = nextSibling.getNextSibling();
                    }
                    if (nextSibling == null) {
                        return;
                    }
                }
                getSetRec(nextSibling, set, node2, z2);
                firstChild = nextSibling.getNextSibling();
            } else {
                return;
            }
        }
    }

    public static void outputDOM(Node node, OutputStream outputStream) {
        outputDOM(node, outputStream, false);
    }

    public static void outputDOM(Node node, OutputStream outputStream, boolean z2) {
        if (z2) {
            try {
                outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes(StandardCharsets.UTF_8));
            } catch (CanonicalizationException e2) {
                LOG.debug(e2.getMessage(), e2);
                return;
            } catch (InvalidCanonicalizerException e3) {
                LOG.debug(e3.getMessage(), e3);
                return;
            } catch (IOException e4) {
                LOG.debug(e4.getMessage(), e4);
                return;
            }
        }
        outputStream.write(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_PHYSICAL).canonicalizeSubtree(node));
    }

    public static void outputDOMc14nWithComments(Node node, OutputStream outputStream) {
        try {
            outputStream.write(Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments").canonicalizeSubtree(node));
        } catch (CanonicalizationException e2) {
            LOG.debug(e2.getMessage(), e2);
        } catch (InvalidCanonicalizerException e3) {
            LOG.debug(e3.getMessage(), e3);
        } catch (IOException e4) {
            LOG.debug(e4.getMessage(), e4);
        }
    }

    @Deprecated
    public static String getFullTextChildrenFromElement(Element element) {
        return getFullTextChildrenFromNode(element);
    }

    public static String getFullTextChildrenFromNode(Node node) {
        StringBuilder sb = new StringBuilder();
        Node firstChild = node.getFirstChild();
        while (true) {
            Node node2 = firstChild;
            if (node2 != null) {
                if (node2.getNodeType() == 3) {
                    sb.append(((Text) node2).getData());
                }
                firstChild = node2.getNextSibling();
            } else {
                return sb.toString();
            }
        }
    }

    public static Element createElementInSignatureSpace(Document document, String str) {
        if (document == null) {
            throw new RuntimeException("Document is null");
        }
        if (dsPrefix == null || dsPrefix.length() == 0) {
            return document.createElementNS("http://www.w3.org/2000/09/xmldsig#", str);
        }
        return document.createElementNS("http://www.w3.org/2000/09/xmldsig#", dsPrefix + CallSiteDescriptor.TOKEN_DELIMITER + str);
    }

    public static Element createElementInSignature11Space(Document document, String str) {
        if (document == null) {
            throw new RuntimeException("Document is null");
        }
        if (ds11Prefix == null || ds11Prefix.length() == 0) {
            return document.createElementNS(Constants.SignatureSpec11NS, str);
        }
        return document.createElementNS(Constants.SignatureSpec11NS, ds11Prefix + CallSiteDescriptor.TOKEN_DELIMITER + str);
    }

    public static boolean elementIsInSignatureSpace(Element element, String str) {
        return element != null && "http://www.w3.org/2000/09/xmldsig#".equals(element.getNamespaceURI()) && element.getLocalName().equals(str);
    }

    public static boolean elementIsInSignature11Space(Element element, String str) {
        return element != null && Constants.SignatureSpec11NS.equals(element.getNamespaceURI()) && element.getLocalName().equals(str);
    }

    public static Document getOwnerDocument(Node node) {
        if (node.getNodeType() == 9) {
            return (Document) node;
        }
        try {
            return node.getOwnerDocument();
        } catch (NullPointerException e2) {
            throw new NullPointerException(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + e2.getMessage() + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }

    public static Document getOwnerDocument(Set<Node> set) {
        NullPointerException nullPointerException = null;
        for (Node node : set) {
            short nodeType = node.getNodeType();
            if (nodeType == 9) {
                return (Document) node;
            }
            try {
                if (nodeType == 2) {
                    return ((Attr) node).getOwnerElement().getOwnerDocument();
                }
                return node.getOwnerDocument();
            } catch (NullPointerException e2) {
                nullPointerException = e2;
            }
        }
        throw new NullPointerException(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + (nullPointerException == null ? "" : nullPointerException.getMessage()) + PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    public static Element createDSctx(Document document, String str, String str2) throws DOMException {
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("You must supply a prefix");
        }
        Element elementCreateElementNS = document.createElementNS(null, "namespaceContext");
        elementCreateElementNS.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str.trim(), str2);
        return elementCreateElementNS;
    }

    public static void addReturnToElement(Element element) {
        if (!ignoreLineBreaks) {
            element.appendChild(element.getOwnerDocument().createTextNode("\n"));
        }
    }

    public static void addReturnToElement(Document document, HelperNodeList helperNodeList) throws IllegalArgumentException {
        if (!ignoreLineBreaks) {
            helperNodeList.appendChild(document.createTextNode("\n"));
        }
    }

    public static void addReturnBeforeChild(Element element, Node node) {
        if (!ignoreLineBreaks) {
            element.insertBefore(element.getOwnerDocument().createTextNode("\n"), node);
        }
    }

    public static String encodeToString(byte[] bArr) {
        if (ignoreLineBreaks) {
            return java.util.Base64.getEncoder().encodeToString(bArr);
        }
        if (lineFeedOnly) {
            return LF_ENCODER.encodeToString(bArr);
        }
        return java.util.Base64.getMimeEncoder().encodeToString(bArr);
    }

    public static byte[] decode(String str) {
        return java.util.Base64.getMimeDecoder().decode(str);
    }

    public static byte[] decode(byte[] bArr) {
        return java.util.Base64.getMimeDecoder().decode(bArr);
    }

    public static boolean isIgnoreLineBreaks() {
        return ignoreLineBreaks;
    }

    public static Set<Node> convertNodelistToSet(NodeList nodeList) {
        if (nodeList == null) {
            return new HashSet();
        }
        int length = nodeList.getLength();
        HashSet hashSet = new HashSet(length);
        for (int i2 = 0; i2 < length; i2++) {
            hashSet.add(nodeList.item(i2));
        }
        return hashSet;
    }

    public static void circumventBug2650(Document document) throws DOMException {
        Element documentElement = document.getDocumentElement();
        if (documentElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns") == null) {
            documentElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
        }
        circumventBug2650internal(document);
    }

    private static void circumventBug2650internal(Node node) throws DOMException {
        Node parentNode = null;
        Node nextSibling = null;
        while (true) {
            Node nextSibling2 = nextSibling;
            switch (node.getNodeType()) {
                case 1:
                    Element element = (Element) node;
                    if (element.hasChildNodes()) {
                        if (element.hasAttributes()) {
                            NamedNodeMap attributes = element.getAttributes();
                            int length = attributes.getLength();
                            Node firstChild = element.getFirstChild();
                            while (true) {
                                Node node2 = firstChild;
                                if (node2 != null) {
                                    if (node2.getNodeType() == 1) {
                                        Element element2 = (Element) node2;
                                        for (int i2 = 0; i2 < length; i2++) {
                                            Attr attr = (Attr) attributes.item(i2);
                                            if ("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI()) && !element2.hasAttributeNS("http://www.w3.org/2000/xmlns/", attr.getLocalName())) {
                                                element2.setAttributeNS("http://www.w3.org/2000/xmlns/", attr.getName(), attr.getNodeValue());
                                            }
                                        }
                                    }
                                    firstChild = node2.getNextSibling();
                                }
                            }
                        }
                    }
                    break;
                case 5:
                case 9:
                    parentNode = node;
                    nextSibling2 = node.getFirstChild();
                    break;
            }
            while (nextSibling2 == null && parentNode != null) {
                nextSibling2 = parentNode.getNextSibling();
                parentNode = parentNode.getParentNode();
            }
            if (nextSibling2 == null) {
                return;
            }
            node = nextSibling2;
            nextSibling = node.getNextSibling();
        }
    }

    public static Element selectDsNode(Node node, String str, int i2) {
        while (node != null) {
            if ("http://www.w3.org/2000/09/xmldsig#".equals(node.getNamespaceURI()) && node.getLocalName().equals(str)) {
                if (i2 == 0) {
                    return (Element) node;
                }
                i2--;
            }
            node = node.getNextSibling();
        }
        return null;
    }

    public static Element selectDs11Node(Node node, String str, int i2) {
        while (node != null) {
            if (Constants.SignatureSpec11NS.equals(node.getNamespaceURI()) && node.getLocalName().equals(str)) {
                if (i2 == 0) {
                    return (Element) node;
                }
                i2--;
            }
            node = node.getNextSibling();
        }
        return null;
    }

    public static Text selectDsNodeText(Node node, String str, int i2) {
        Node node2;
        Element elementSelectDsNode = selectDsNode(node, str, i2);
        if (elementSelectDsNode == null) {
            return null;
        }
        Node firstChild = elementSelectDsNode.getFirstChild();
        while (true) {
            node2 = firstChild;
            if (node2 == null || node2.getNodeType() == 3) {
                break;
            }
            firstChild = node2.getNextSibling();
        }
        return (Text) node2;
    }

    public static Text selectDs11NodeText(Node node, String str, int i2) {
        Node node2;
        Element elementSelectDs11Node = selectDs11Node(node, str, i2);
        if (elementSelectDs11Node == null) {
            return null;
        }
        Node firstChild = elementSelectDs11Node.getFirstChild();
        while (true) {
            node2 = firstChild;
            if (node2 == null || node2.getNodeType() == 3) {
                break;
            }
            firstChild = node2.getNextSibling();
        }
        return (Text) node2;
    }

    public static Text selectNodeText(Node node, String str, String str2, int i2) {
        Node node2;
        Element elementSelectNode = selectNode(node, str, str2, i2);
        if (elementSelectNode == null) {
            return null;
        }
        Node firstChild = elementSelectNode.getFirstChild();
        while (true) {
            node2 = firstChild;
            if (node2 == null || node2.getNodeType() == 3) {
                break;
            }
            firstChild = node2.getNextSibling();
        }
        return (Text) node2;
    }

    public static Element selectNode(Node node, String str, String str2, int i2) {
        while (node != null) {
            if (node.getNamespaceURI() != null && node.getNamespaceURI().equals(str) && node.getLocalName().equals(str2)) {
                if (i2 == 0) {
                    return (Element) node;
                }
                i2--;
            }
            node = node.getNextSibling();
        }
        return null;
    }

    public static Element[] selectDsNodes(Node node, String str) {
        return selectNodes(node, "http://www.w3.org/2000/09/xmldsig#", str);
    }

    public static Element[] selectDs11Nodes(Node node, String str) {
        return selectNodes(node, Constants.SignatureSpec11NS, str);
    }

    public static Element[] selectNodes(Node node, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        while (node != null) {
            if (node.getNamespaceURI() != null && node.getNamespaceURI().equals(str) && node.getLocalName().equals(str2)) {
                arrayList.add((Element) node);
            }
            node = node.getNextSibling();
        }
        return (Element[]) arrayList.toArray(new Element[arrayList.size()]);
    }

    public static Set<Node> excludeNodeFromSet(Node node, Set<Node> set) {
        HashSet hashSet = new HashSet();
        for (Node node2 : set) {
            if (!isDescendantOrSelf(node, node2)) {
                hashSet.add(node2);
            }
        }
        return hashSet;
    }

    public static String getStrFromNode(Node node) {
        if (node.getNodeType() == 3) {
            StringBuilder sb = new StringBuilder();
            Node firstChild = node.getParentNode().getFirstChild();
            while (true) {
                Node node2 = firstChild;
                if (node2 != null) {
                    if (node2.getNodeType() == 3) {
                        sb.append(((Text) node2).getData());
                    }
                    firstChild = node2.getNextSibling();
                } else {
                    return sb.toString();
                }
            }
        } else {
            if (node.getNodeType() == 2) {
                return node.getNodeValue();
            }
            if (node.getNodeType() == 7) {
                return node.getNodeValue();
            }
            return null;
        }
    }

    public static boolean isDescendantOrSelf(Node node, Node node2) {
        if (node == node2) {
            return true;
        }
        Node ownerElement = node2;
        while (true) {
            Node node3 = ownerElement;
            if (node3 == null) {
                return false;
            }
            if (node3 == node) {
                return true;
            }
            if (node3.getNodeType() == 2) {
                ownerElement = ((Attr) node3).getOwnerElement();
            } else {
                ownerElement = node3.getParentNode();
            }
        }
    }

    public static boolean ignoreLineBreaks() {
        return ignoreLineBreaks;
    }

    public static String getAttributeValue(Element element, String str) throws DOMException {
        Attr attributeNodeNS = element.getAttributeNodeNS(null, str);
        if (attributeNodeNS == null) {
            return null;
        }
        return attributeNodeNS.getValue();
    }

    public static boolean protectAgainstWrappingAttack(Node node, String str) {
        NamedNodeMap attributes;
        String strTrim = str.trim();
        if (!strTrim.isEmpty() && strTrim.charAt(0) == '#') {
            strTrim = strTrim.substring(1);
        }
        Node parentNode = null;
        Element ownerElement = null;
        if (node != null) {
            parentNode = node.getParentNode();
        }
        while (node != null) {
            if (node.getNodeType() == 1 && (attributes = ((Element) node).getAttributes()) != null) {
                int length = attributes.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    Attr attr = (Attr) attributes.item(i2);
                    if (attr.isId() && strTrim.equals(attr.getValue())) {
                        if (ownerElement == null) {
                            ownerElement = attr.getOwnerElement();
                        } else {
                            LOG.debug("Multiple elements with the same 'Id' attribute value!");
                            return false;
                        }
                    }
                }
            }
            Node parentNode2 = node;
            node = node.getFirstChild();
            if (node == null) {
                node = parentNode2.getNextSibling();
            }
            while (node == null) {
                parentNode2 = parentNode2.getParentNode();
                if (parentNode2 == parentNode) {
                    return true;
                }
                node = parentNode2.getNextSibling();
            }
        }
        return true;
    }

    public static boolean protectAgainstWrappingAttack(Node node, Element element, String str) {
        Element element2;
        NamedNodeMap attributes;
        String strTrim = str.trim();
        if (!strTrim.isEmpty() && strTrim.charAt(0) == '#') {
            strTrim = strTrim.substring(1);
        }
        Node parentNode = null;
        if (node != null) {
            parentNode = node.getParentNode();
        }
        while (node != null) {
            if (node.getNodeType() == 1 && (attributes = (element2 = (Element) node).getAttributes()) != null) {
                int length = attributes.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    Attr attr = (Attr) attributes.item(i2);
                    if (attr.isId() && strTrim.equals(attr.getValue()) && element2 != element) {
                        LOG.debug("Multiple elements with the same 'Id' attribute value!");
                        return false;
                    }
                }
            }
            Node parentNode2 = node;
            node = node.getFirstChild();
            if (node == null) {
                node = parentNode2.getNextSibling();
            }
            while (node == null) {
                parentNode2 = parentNode2.getParentNode();
                if (parentNode2 == parentNode) {
                    return true;
                }
                node = parentNode2.getNextSibling();
            }
        }
        return true;
    }

    public static DocumentBuilder createDocumentBuilder(boolean z2) throws ParserConfigurationException {
        return createDocumentBuilder(z2, true);
    }

    public static DocumentBuilder createDocumentBuilder(boolean z2, boolean z3) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactoryNewInstance = DocumentBuilderFactory.newInstance();
        documentBuilderFactoryNewInstance.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        if (z3) {
            documentBuilderFactoryNewInstance.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        }
        documentBuilderFactoryNewInstance.setValidating(z2);
        documentBuilderFactoryNewInstance.setNamespaceAware(true);
        return documentBuilderFactoryNewInstance.newDocumentBuilder();
    }

    public static byte[] getBytes(BigInteger bigInteger, int i2) {
        int i3 = ((i2 + 7) >> 3) << 3;
        if (i3 < bigInteger.bitLength()) {
            throw new IllegalArgumentException(I18n.translate("utils.Base64.IllegalBitlength"));
        }
        byte[] byteArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 != 0 && (bigInteger.bitLength() / 8) + 1 == i3 / 8) {
            return byteArray;
        }
        int i4 = 0;
        int length = byteArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            i4 = 1;
            length--;
        }
        int i5 = (i3 / 8) - length;
        byte[] bArr = new byte[i3 / 8];
        System.arraycopy(byteArray, i4, bArr, i5, length);
        return bArr;
    }
}
