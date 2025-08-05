package com.sun.org.apache.xml.internal.security.c14n.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.helper.C14nHelper;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/Canonicalizer20010315.class */
public abstract class Canonicalizer20010315 extends CanonicalizerBase {
    private boolean firstCall;
    private final XmlAttrStack xmlattrStack;
    private final boolean c14n11;

    public Canonicalizer20010315(boolean z2) {
        this(z2, false);
    }

    public Canonicalizer20010315(boolean z2, boolean z3) {
        super(z2);
        this.firstCall = true;
        this.xmlattrStack = new XmlAttrStack(z3);
        this.c14n11 = z3;
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeXPathNodeSet(Set<Node> set, String str) throws CanonicalizationException {
        throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node, String str) throws CanonicalizationException {
        throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node, String str, boolean z2) throws CanonicalizationException {
        throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputAttributesSubtree(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException {
        Node nodeAddMappingAndRender;
        if (!element.hasAttributes() && !this.firstCall) {
            return;
        }
        TreeSet<Attr> treeSet = new TreeSet(COMPARE);
        if (element.hasAttributes()) {
            NamedNodeMap attributes = element.getAttributes();
            int length = attributes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                Attr attr = (Attr) attributes.item(i2);
                String namespaceURI = attr.getNamespaceURI();
                String localName = attr.getLocalName();
                String value = attr.getValue();
                if (!"http://www.w3.org/2000/xmlns/".equals(namespaceURI)) {
                    treeSet.add(attr);
                } else if ((!"xml".equals(localName) || !"http://www.w3.org/XML/1998/namespace".equals(value)) && (nodeAddMappingAndRender = nameSpaceSymbTable.addMappingAndRender(localName, value, attr)) != null) {
                    treeSet.add((Attr) nodeAddMappingAndRender);
                    if (C14nHelper.namespaceIsRelative(attr)) {
                        throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", new Object[]{element.getTagName(), localName, attr.getNodeValue()});
                    }
                }
            }
        }
        if (this.firstCall) {
            nameSpaceSymbTable.getUnrenderedNodes(treeSet);
            this.xmlattrStack.getXmlnsAttr(treeSet);
            this.firstCall = false;
        }
        OutputStream writer = getWriter();
        for (Attr attr2 : treeSet) {
            outputAttrToWriter(attr2.getNodeName(), attr2.getNodeValue(), writer, map);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputAttributes(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException {
        Node nodeAddMappingAndRender;
        this.xmlattrStack.push(nameSpaceSymbTable.getLevel());
        boolean z2 = isVisibleDO(element, nameSpaceSymbTable.getLevel()) == 1;
        TreeSet<Attr> treeSet = new TreeSet(COMPARE);
        if (element.hasAttributes()) {
            NamedNodeMap attributes = element.getAttributes();
            int length = attributes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                Attr attr = (Attr) attributes.item(i2);
                String namespaceURI = attr.getNamespaceURI();
                String localName = attr.getLocalName();
                String value = attr.getValue();
                if (!"http://www.w3.org/2000/xmlns/".equals(namespaceURI)) {
                    if ("http://www.w3.org/XML/1998/namespace".equals(namespaceURI)) {
                        if (this.c14n11 && "id".equals(localName)) {
                            if (z2) {
                                treeSet.add(attr);
                            }
                        } else {
                            this.xmlattrStack.addXmlnsAttr(attr);
                        }
                    } else if (z2) {
                        treeSet.add(attr);
                    }
                } else if (!"xml".equals(localName) || !"http://www.w3.org/XML/1998/namespace".equals(value)) {
                    if (isVisible(attr)) {
                        if ((z2 || !nameSpaceSymbTable.removeMappingIfRender(localName)) && (nodeAddMappingAndRender = nameSpaceSymbTable.addMappingAndRender(localName, value, attr)) != null) {
                            treeSet.add((Attr) nodeAddMappingAndRender);
                            if (C14nHelper.namespaceIsRelative(attr)) {
                                throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", new Object[]{element.getTagName(), localName, attr.getNodeValue()});
                            }
                        }
                    } else if (z2 && !"xmlns".equals(localName)) {
                        nameSpaceSymbTable.removeMapping(localName);
                    } else {
                        nameSpaceSymbTable.addMapping(localName, value, attr);
                    }
                }
            }
        }
        if (z2) {
            Node attributeNodeNS = element.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
            Node nodeAddMappingAndRender2 = null;
            if (attributeNodeNS == null) {
                nodeAddMappingAndRender2 = nameSpaceSymbTable.getMapping("xmlns");
            } else if (!isVisible(attributeNodeNS)) {
                nodeAddMappingAndRender2 = nameSpaceSymbTable.addMappingAndRender("xmlns", "", getNullNode(attributeNodeNS.getOwnerDocument()));
            }
            if (nodeAddMappingAndRender2 != null) {
                treeSet.add((Attr) nodeAddMappingAndRender2);
            }
            this.xmlattrStack.getXmlnsAttr(treeSet);
            nameSpaceSymbTable.getUnrenderedNodes(treeSet);
        }
        OutputStream writer = getWriter();
        for (Attr attr2 : treeSet) {
            outputAttrToWriter(attr2.getNodeName(), attr2.getNodeValue(), writer, map);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void circumventBugIfNeeded(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, ParserConfigurationException, DOMException, SAXException, IOException {
        Document ownerDocument;
        if (!xMLSignatureInput.isNeedsToBeExpanded()) {
            return;
        }
        if (xMLSignatureInput.getSubNode() != null) {
            ownerDocument = XMLUtils.getOwnerDocument(xMLSignatureInput.getSubNode());
        } else {
            ownerDocument = XMLUtils.getOwnerDocument(xMLSignatureInput.getNodeSet());
        }
        XMLUtils.circumventBug2650(ownerDocument);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void handleParent(Element element, NameSpaceSymbTable nameSpaceSymbTable) throws DOMException {
        String str;
        if (!element.hasAttributes() && element.getNamespaceURI() == null) {
            return;
        }
        this.xmlattrStack.push(-1);
        NamedNodeMap attributes = element.getAttributes();
        int length = attributes.getLength();
        for (int i2 = 0; i2 < length; i2++) {
            Attr attr = (Attr) attributes.item(i2);
            String localName = attr.getLocalName();
            String nodeValue = attr.getNodeValue();
            if ("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {
                if (!"xml".equals(localName) || !"http://www.w3.org/XML/1998/namespace".equals(nodeValue)) {
                    nameSpaceSymbTable.addMapping(localName, nodeValue, attr);
                }
            } else if ("http://www.w3.org/XML/1998/namespace".equals(attr.getNamespaceURI()) && (!this.c14n11 || !"id".equals(localName))) {
                this.xmlattrStack.addXmlnsAttr(attr);
            }
        }
        if (element.getNamespaceURI() != null) {
            String prefix = element.getPrefix();
            String namespaceURI = element.getNamespaceURI();
            if (prefix == null || prefix.equals("")) {
                prefix = "xmlns";
                str = "xmlns";
            } else {
                str = "xmlns:" + prefix;
            }
            Attr attrCreateAttributeNS = element.getOwnerDocument().createAttributeNS("http://www.w3.org/2000/xmlns/", str);
            attrCreateAttributeNS.setValue(namespaceURI);
            nameSpaceSymbTable.addMapping(prefix, namespaceURI, attrCreateAttributeNS);
        }
    }
}
