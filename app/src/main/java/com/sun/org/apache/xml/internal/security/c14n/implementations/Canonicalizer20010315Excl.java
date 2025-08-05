package com.sun.org.apache.xml.internal.security.c14n.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.helper.C14nHelper;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/Canonicalizer20010315Excl.class */
public abstract class Canonicalizer20010315Excl extends CanonicalizerBase {
    private SortedSet<String> inclusiveNSSet;
    private boolean propagateDefaultNamespace;

    public Canonicalizer20010315Excl(boolean z2) {
        super(z2);
        this.propagateDefaultNamespace = false;
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase, com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node) throws CanonicalizationException {
        return engineCanonicalizeSubTree(node, "", (Node) null);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node, String str) throws CanonicalizationException {
        return engineCanonicalizeSubTree(node, str, (Node) null);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeSubTree(Node node, String str, boolean z2) throws CanonicalizationException {
        this.propagateDefaultNamespace = z2;
        return engineCanonicalizeSubTree(node, str, (Node) null);
    }

    public byte[] engineCanonicalizeSubTree(Node node, String str, Node node2) throws CanonicalizationException {
        this.inclusiveNSSet = InclusiveNamespaces.prefixStr2Set(str);
        return super.engineCanonicalizeSubTree(node, node2);
    }

    public byte[] engineCanonicalize(XMLSignatureInput xMLSignatureInput, String str) throws CanonicalizationException {
        this.inclusiveNSSet = InclusiveNamespaces.prefixStr2Set(str);
        return super.engineCanonicalize(xMLSignatureInput);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public byte[] engineCanonicalizeXPathNodeSet(Set<Node> set, String str) throws CanonicalizationException {
        this.inclusiveNSSet = InclusiveNamespaces.prefixStr2Set(str);
        return super.engineCanonicalizeXPathNodeSet(set);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputAttributesSubtree(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException {
        String prefix;
        TreeSet<Attr> treeSet = new TreeSet(COMPARE);
        TreeSet treeSet2 = new TreeSet();
        if (this.inclusiveNSSet != null && !this.inclusiveNSSet.isEmpty()) {
            treeSet2.addAll(this.inclusiveNSSet);
        }
        if (element.hasAttributes()) {
            NamedNodeMap attributes = element.getAttributes();
            int length = attributes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                Attr attr = (Attr) attributes.item(i2);
                String localName = attr.getLocalName();
                String nodeValue = attr.getNodeValue();
                if (!"http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {
                    String prefix2 = attr.getPrefix();
                    if (prefix2 != null && !prefix2.equals("xml") && !prefix2.equals("xmlns")) {
                        treeSet2.add(prefix2);
                    }
                    treeSet.add(attr);
                } else if ((!"xml".equals(localName) || !"http://www.w3.org/XML/1998/namespace".equals(nodeValue)) && nameSpaceSymbTable.addMapping(localName, nodeValue, attr) && C14nHelper.namespaceIsRelative(nodeValue)) {
                    throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", new Object[]{element.getTagName(), localName, attr.getNodeValue()});
                }
            }
        }
        if (this.propagateDefaultNamespace && nameSpaceSymbTable.getLevel() == 1 && this.inclusiveNSSet.contains("xmlns") && nameSpaceSymbTable.getMappingWithoutRendered("xmlns") == null) {
            nameSpaceSymbTable.removeMapping("xmlns");
            nameSpaceSymbTable.addMapping("xmlns", "", getNullNode(element.getOwnerDocument()));
        }
        if (element.getNamespaceURI() != null && element.getPrefix() != null && element.getPrefix().length() != 0) {
            prefix = element.getPrefix();
        } else {
            prefix = "xmlns";
        }
        treeSet2.add(prefix);
        Iterator<E> it = treeSet2.iterator();
        while (it.hasNext()) {
            Attr mapping = nameSpaceSymbTable.getMapping((String) it.next());
            if (mapping != null) {
                treeSet.add(mapping);
            }
        }
        OutputStream writer = getWriter();
        for (Attr attr2 : treeSet) {
            outputAttrToWriter(attr2.getNodeName(), attr2.getNodeValue(), writer, map);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void outputAttributes(Element element, NameSpaceSymbTable nameSpaceSymbTable, Map<String, byte[]> map) throws CanonicalizationException, DOMException, IOException {
        String prefix;
        Node nodeAddMappingAndRender;
        TreeSet<Attr> treeSet = new TreeSet(COMPARE);
        TreeSet treeSet2 = null;
        boolean z2 = isVisibleDO(element, nameSpaceSymbTable.getLevel()) == 1;
        if (z2) {
            treeSet2 = new TreeSet();
            if (this.inclusiveNSSet != null && !this.inclusiveNSSet.isEmpty()) {
                treeSet2.addAll(this.inclusiveNSSet);
            }
        }
        if (element.hasAttributes()) {
            NamedNodeMap attributes = element.getAttributes();
            int length = attributes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                Attr attr = (Attr) attributes.item(i2);
                String localName = attr.getLocalName();
                String nodeValue = attr.getNodeValue();
                if (!"http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {
                    if (isVisible(attr) && z2) {
                        String prefix2 = attr.getPrefix();
                        if (prefix2 != null && !prefix2.equals("xml") && !prefix2.equals("xmlns")) {
                            treeSet2.add(prefix2);
                        }
                        treeSet.add(attr);
                    }
                } else if (z2 && !isVisible(attr) && !"xmlns".equals(localName)) {
                    nameSpaceSymbTable.removeMappingIfNotRender(localName);
                } else {
                    if (!z2 && isVisible(attr) && this.inclusiveNSSet.contains(localName) && !nameSpaceSymbTable.removeMappingIfRender(localName) && (nodeAddMappingAndRender = nameSpaceSymbTable.addMappingAndRender(localName, nodeValue, attr)) != null) {
                        treeSet.add((Attr) nodeAddMappingAndRender);
                        if (C14nHelper.namespaceIsRelative(attr)) {
                            throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", new Object[]{element.getTagName(), localName, attr.getNodeValue()});
                        }
                    }
                    if (nameSpaceSymbTable.addMapping(localName, nodeValue, attr) && C14nHelper.namespaceIsRelative(nodeValue)) {
                        throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", new Object[]{element.getTagName(), localName, attr.getNodeValue()});
                    }
                }
            }
        }
        if (z2) {
            Attr attributeNodeNS = element.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
            if (attributeNodeNS != null && !isVisible(attributeNodeNS)) {
                nameSpaceSymbTable.addMapping("xmlns", "", getNullNode(attributeNodeNS.getOwnerDocument()));
            }
            if (element.getNamespaceURI() != null && element.getPrefix() != null && element.getPrefix().length() != 0) {
                prefix = element.getPrefix();
            } else {
                prefix = "xmlns";
            }
            treeSet2.add(prefix);
            Iterator it = treeSet2.iterator();
            while (it.hasNext()) {
                Attr mapping = nameSpaceSymbTable.getMapping((String) it.next());
                if (mapping != null) {
                    treeSet.add(mapping);
                }
            }
        }
        OutputStream writer = getWriter();
        for (Attr attr2 : treeSet) {
            outputAttrToWriter(attr2.getNodeName(), attr2.getNodeValue(), writer, map);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
    protected void circumventBugIfNeeded(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, ParserConfigurationException, DOMException, SAXException, IOException {
        Document ownerDocument;
        if (!xMLSignatureInput.isNeedsToBeExpanded() || this.inclusiveNSSet.isEmpty()) {
            return;
        }
        if (xMLSignatureInput.getSubNode() != null) {
            ownerDocument = XMLUtils.getOwnerDocument(xMLSignatureInput.getSubNode());
        } else {
            ownerDocument = XMLUtils.getOwnerDocument(xMLSignatureInput.getNodeSet());
        }
        XMLUtils.circumventBug2650(ownerDocument);
    }
}
