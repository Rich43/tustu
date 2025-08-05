package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.transforms.params.XPath2FilterContainer04;
import com.sun.org.apache.xml.internal.security.transforms.params.XPathFilterCHGPContainer;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/ElementProxy.class */
public abstract class ElementProxy {
    private Element wrappedElement;
    protected String baseURI;
    private Document wrappedDoc;
    protected static final Logger LOG = LoggerFactory.getLogger(ElementProxy.class);
    private static Map<String, String> prefixMappings = new ConcurrentHashMap();

    public abstract String getBaseNamespace();

    public abstract String getBaseLocalName();

    public ElementProxy() {
    }

    public ElementProxy(Document document) {
        if (document == null) {
            throw new RuntimeException("Document is null");
        }
        this.wrappedDoc = document;
        this.wrappedElement = createElementForFamilyLocal(getBaseNamespace(), getBaseLocalName());
    }

    public ElementProxy(Element element, String str) throws XMLSecurityException {
        if (element == null) {
            throw new XMLSecurityException("ElementProxy.nullElement");
        }
        LOG.debug("setElement(\"{}\", \"{}\")", element.getTagName(), str);
        setElement(element);
        this.baseURI = str;
        guaranteeThatElementInCorrectSpace();
    }

    protected Element createElementForFamilyLocal(String str, String str2) throws DOMException {
        Element elementCreateElementNS;
        Document document = getDocument();
        if (str == null) {
            elementCreateElementNS = document.createElementNS(null, str2);
        } else {
            String defaultPrefix = getDefaultPrefix(getBaseNamespace());
            if (defaultPrefix == null || defaultPrefix.length() == 0) {
                elementCreateElementNS = document.createElementNS(str, str2);
                elementCreateElementNS.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", str);
            } else {
                elementCreateElementNS = document.createElementNS(str, defaultPrefix + CallSiteDescriptor.TOKEN_DELIMITER + str2);
                elementCreateElementNS.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + defaultPrefix, str);
            }
        }
        return elementCreateElementNS;
    }

    public static Element createElementForFamily(Document document, String str, String str2) throws DOMException {
        Element elementCreateElementNS;
        String defaultPrefix = getDefaultPrefix(str);
        if (str == null) {
            elementCreateElementNS = document.createElementNS(null, str2);
        } else if (defaultPrefix == null || defaultPrefix.length() == 0) {
            elementCreateElementNS = document.createElementNS(str, str2);
            elementCreateElementNS.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", str);
        } else {
            elementCreateElementNS = document.createElementNS(str, defaultPrefix + CallSiteDescriptor.TOKEN_DELIMITER + str2);
            elementCreateElementNS.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + defaultPrefix, str);
        }
        return elementCreateElementNS;
    }

    public void setElement(Element element, String str) throws XMLSecurityException {
        if (element == null) {
            throw new XMLSecurityException("ElementProxy.nullElement");
        }
        LOG.debug("setElement({}, \"{}\")", element.getTagName(), str);
        setElement(element);
        this.baseURI = str;
    }

    public final Element getElement() {
        return this.wrappedElement;
    }

    public final NodeList getElementPlusReturns() throws IllegalArgumentException {
        HelperNodeList helperNodeList = new HelperNodeList();
        helperNodeList.appendChild(createText("\n"));
        helperNodeList.appendChild(getElement());
        helperNodeList.appendChild(createText("\n"));
        return helperNodeList;
    }

    protected Text createText(String str) {
        return this.wrappedDoc.createTextNode(str);
    }

    public Document getDocument() {
        if (this.wrappedDoc == null) {
            this.wrappedDoc = XMLUtils.getOwnerDocument(this.wrappedElement);
        }
        return this.wrappedDoc;
    }

    public String getBaseURI() {
        return this.baseURI;
    }

    void guaranteeThatElementInCorrectSpace() throws XMLSecurityException {
        String baseLocalName = getBaseLocalName();
        String baseNamespace = getBaseNamespace();
        String localName = getElement().getLocalName();
        String namespaceURI = getElement().getNamespaceURI();
        if (!baseNamespace.equals(namespaceURI) && !baseLocalName.equals(localName)) {
            throw new XMLSecurityException("xml.WrongElement", new Object[]{namespaceURI + CallSiteDescriptor.TOKEN_DELIMITER + localName, baseNamespace + CallSiteDescriptor.TOKEN_DELIMITER + baseLocalName});
        }
    }

    public void addBigIntegerElement(BigInteger bigInteger, String str) {
        if (bigInteger != null) {
            Element elementCreateElementInSignatureSpace = XMLUtils.createElementInSignatureSpace(getDocument(), str);
            elementCreateElementInSignatureSpace.appendChild(elementCreateElementInSignatureSpace.getOwnerDocument().createTextNode(XMLUtils.encodeToString(XMLUtils.getBytes(bigInteger, bigInteger.bitLength()))));
            appendSelf(elementCreateElementInSignatureSpace);
            addReturnToSelf();
        }
    }

    protected void addReturnToSelf() {
        XMLUtils.addReturnToElement(getElement());
    }

    public void addBase64Element(byte[] bArr, String str) {
        if (bArr != null) {
            Element elementCreateElementInSignatureSpace = XMLUtils.createElementInSignatureSpace(getDocument(), str);
            elementCreateElementInSignatureSpace.appendChild(getDocument().createTextNode(XMLUtils.encodeToString(bArr)));
            appendSelf(elementCreateElementInSignatureSpace);
            if (!XMLUtils.ignoreLineBreaks()) {
                appendSelf(createText("\n"));
            }
        }
    }

    public void addTextElement(String str, String str2) {
        Element elementCreateElementInSignatureSpace = XMLUtils.createElementInSignatureSpace(getDocument(), str2);
        appendOther(elementCreateElementInSignatureSpace, createText(str));
        appendSelf(elementCreateElementInSignatureSpace);
        addReturnToSelf();
    }

    public void addBase64Text(byte[] bArr) {
        Text textCreateText;
        if (bArr != null) {
            if (XMLUtils.ignoreLineBreaks()) {
                textCreateText = createText(XMLUtils.encodeToString(bArr));
            } else {
                textCreateText = createText("\n" + XMLUtils.encodeToString(bArr) + "\n");
            }
            appendSelf(textCreateText);
        }
    }

    protected void appendSelf(ElementProxy elementProxy) {
        getElement().appendChild(elementProxy.getElement());
    }

    protected void appendSelf(Node node) {
        getElement().appendChild(node);
    }

    protected void appendOther(Element element, Node node) {
        element.appendChild(node);
    }

    public void addText(String str) {
        if (str != null) {
            appendSelf(createText(str));
        }
    }

    public BigInteger getBigIntegerFromChildElement(String str, String str2) {
        Element elementSelectNode = XMLUtils.selectNode(getFirstChild(), str2, str, 0);
        if (elementSelectNode != null) {
            return new BigInteger(1, XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(elementSelectNode)));
        }
        return null;
    }

    public String getTextFromChildElement(String str, String str2) {
        return XMLUtils.selectNode(getFirstChild(), str2, str, 0).getTextContent();
    }

    public byte[] getBytesFromTextChild() throws XMLSecurityException {
        return XMLUtils.decode(getTextFromTextChild());
    }

    public String getTextFromTextChild() {
        return XMLUtils.getFullTextChildrenFromNode(getElement());
    }

    public int length(String str, String str2) {
        int i2 = 0;
        Node firstChild = getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (str2.equals(node.getLocalName()) && str.equals(node.getNamespaceURI())) {
                    i2++;
                }
                firstChild = node.getNextSibling();
            } else {
                return i2;
            }
        }
    }

    public void setXPathNamespaceContext(String str, String str2) throws DOMException, XMLSecurityException {
        String str3;
        if (str == null || str.length() == 0) {
            throw new XMLSecurityException("defaultNamespaceCannotBeSetHere");
        }
        if ("xmlns".equals(str)) {
            throw new XMLSecurityException("defaultNamespaceCannotBeSetHere");
        }
        if (str.startsWith("xmlns:")) {
            str3 = str;
        } else {
            str3 = "xmlns:" + str;
        }
        Attr attributeNodeNS = getElement().getAttributeNodeNS("http://www.w3.org/2000/xmlns/", str3);
        if (attributeNodeNS != null) {
            if (!attributeNodeNS.getNodeValue().equals(str2)) {
                throw new XMLSecurityException("namespacePrefixAlreadyUsedByOtherURI", new Object[]{str3, getElement().getAttributeNS(null, str3)});
            }
        } else {
            getElement().setAttributeNS("http://www.w3.org/2000/xmlns/", str3, str2);
        }
    }

    public static void setDefaultPrefix(String str, String str2) throws XMLSecurityException {
        JavaUtils.checkRegisterPermission();
        setNamespacePrefix(str, str2);
    }

    private static void setNamespacePrefix(String str, String str2) throws XMLSecurityException {
        if (prefixMappings.containsValue(str2)) {
            String str3 = prefixMappings.get(str);
            if (!str3.equals(str2)) {
                throw new XMLSecurityException("prefix.AlreadyAssigned", new Object[]{str2, str, str3});
            }
        }
        if ("http://www.w3.org/2000/09/xmldsig#".equals(str)) {
            XMLUtils.setDsPrefix(str2);
        } else if (Constants.SignatureSpec11NS.equals(str)) {
            XMLUtils.setDs11Prefix(str2);
        } else if (EncryptionConstants.EncryptionSpecNS.equals(str)) {
            XMLUtils.setXencPrefix(str2);
        }
        prefixMappings.put(str, str2);
    }

    public static void registerDefaultPrefixes() throws XMLSecurityException {
        setNamespacePrefix("http://www.w3.org/2000/09/xmldsig#", "ds");
        setNamespacePrefix(EncryptionConstants.EncryptionSpecNS, "xenc");
        setNamespacePrefix("http://www.w3.org/2009/xmlenc11#", "xenc11");
        setNamespacePrefix("http://www.xmlsecurity.org/experimental#", "experimental");
        setNamespacePrefix(XPath2FilterContainer04.XPathFilter2NS, "dsig-xpath-old");
        setNamespacePrefix("http://www.w3.org/2002/06/xmldsig-filter2", "dsig-xpath");
        setNamespacePrefix("http://www.w3.org/2001/10/xml-exc-c14n#", "ec");
        setNamespacePrefix(XPathFilterCHGPContainer.TRANSFORM_XPATHFILTERCHGP, "xx");
        setNamespacePrefix(Constants.SignatureSpec11NS, "dsig11");
    }

    public static String getDefaultPrefix(String str) {
        return prefixMappings.get(str);
    }

    protected void setElement(Element element) {
        this.wrappedElement = element;
    }

    protected void setDocument(Document document) {
        this.wrappedDoc = document;
    }

    protected String getLocalAttribute(String str) {
        return getElement().getAttributeNS(null, str);
    }

    protected void setLocalAttribute(String str, String str2) throws DOMException {
        getElement().setAttributeNS(null, str, str2);
    }

    protected void setLocalIdAttribute(String str, String str2) throws DOMException {
        if (str2 != null) {
            Attr attrCreateAttributeNS = getDocument().createAttributeNS(null, str);
            attrCreateAttributeNS.setValue(str2);
            getElement().setAttributeNodeNS(attrCreateAttributeNS);
            getElement().setIdAttributeNode(attrCreateAttributeNS, true);
            return;
        }
        getElement().removeAttributeNS(null, str);
    }

    protected Node getFirstChild() {
        return getElement().getFirstChild();
    }
}
