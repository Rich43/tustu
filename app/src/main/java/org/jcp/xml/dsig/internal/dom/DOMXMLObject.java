package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.XMLObject;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXMLObject.class */
public final class DOMXMLObject extends DOMStructure implements XMLObject {
    private final String id;
    private final String mimeType;
    private final String encoding;
    private final List<XMLStructure> content;
    private Element objectElem;

    public DOMXMLObject(List<? extends XMLStructure> list, String str, String str2, String str3) {
        if (list == null || list.isEmpty()) {
            this.content = Collections.emptyList();
        } else {
            this.content = Collections.unmodifiableList(new ArrayList(list));
            int size = this.content.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (!(this.content.get(i2) instanceof XMLStructure)) {
                    throw new ClassCastException("content[" + i2 + "] is not a valid type");
                }
            }
        }
        this.id = str;
        this.mimeType = str2;
        this.encoding = str3;
    }

    public DOMXMLObject(Element element, XMLCryptoContext xMLCryptoContext, Provider provider) throws MarshalException, DOMException {
        this.encoding = DOMUtils.getAttributeValue(element, Constants._ATT_ENCODING);
        Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            this.id = attributeNodeNS.getValue();
            element.setIdAttributeNode(attributeNodeNS, true);
        } else {
            this.id = null;
        }
        this.mimeType = DOMUtils.getAttributeValue(element, Constants._ATT_MIMETYPE);
        ArrayList arrayList = new ArrayList();
        Node firstChild = element.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node == null) {
                break;
            }
            if (node.getNodeType() == 1) {
                Element element2 = (Element) node;
                String localName = element2.getLocalName();
                String namespaceURI = element2.getNamespaceURI();
                if (Constants._TAG_MANIFEST.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(new DOMManifest(element2, xMLCryptoContext, provider));
                } else if (Constants._TAG_SIGNATUREPROPERTIES.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(new DOMSignatureProperties(element2));
                } else if (Constants._TAG_X509DATA.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(new DOMX509Data(element2));
                } else {
                    arrayList.add(new javax.xml.crypto.dom.DOMStructure(node));
                }
            } else {
                arrayList.add(new javax.xml.crypto.dom.DOMStructure(node));
            }
            firstChild = node.getNextSibling();
        }
        NamedNodeMap attributes = element.getAttributes();
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            Node nodeItem = attributes.item(i2);
            if (DOMUtils.isNamespace(nodeItem)) {
                arrayList.add(new javax.xml.crypto.dom.DOMStructure(nodeItem));
            }
        }
        if (arrayList.isEmpty()) {
            this.content = Collections.emptyList();
        } else {
            this.content = Collections.unmodifiableList(arrayList);
        }
        this.objectElem = element;
    }

    @Override // javax.xml.crypto.dsig.XMLObject
    public List<XMLStructure> getContent() {
        return this.content;
    }

    @Override // javax.xml.crypto.dsig.XMLObject
    public String getId() {
        return this.id;
    }

    @Override // javax.xml.crypto.dsig.XMLObject
    public String getMimeType() {
        return this.mimeType;
    }

    @Override // javax.xml.crypto.dsig.XMLObject
    public String getEncoding() {
        return this.encoding;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = this.objectElem != null ? this.objectElem : null;
        if (elementCreateElement == null) {
            elementCreateElement = DOMUtils.createElement(ownerDocument, "Object", "http://www.w3.org/2000/09/xmldsig#", str);
            DOMUtils.setAttributeID(elementCreateElement, Constants._ATT_ID, this.id);
            DOMUtils.setAttribute(elementCreateElement, Constants._ATT_MIMETYPE, this.mimeType);
            DOMUtils.setAttribute(elementCreateElement, Constants._ATT_ENCODING, this.encoding);
            for (XMLStructure xMLStructure : this.content) {
                if (xMLStructure instanceof DOMStructure) {
                    ((DOMStructure) xMLStructure).marshal(elementCreateElement, str, dOMCryptoContext);
                } else {
                    DOMUtils.appendChild(elementCreateElement, ((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode());
                }
            }
        }
        node.appendChild(elementCreateElement);
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        boolean zEquals2;
        boolean zEquals3;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XMLObject)) {
            return false;
        }
        XMLObject xMLObject = (XMLObject) obj;
        if (this.id == null) {
            zEquals = xMLObject.getId() == null;
        } else {
            zEquals = this.id.equals(xMLObject.getId());
        }
        boolean z2 = zEquals;
        if (this.encoding == null) {
            zEquals2 = xMLObject.getEncoding() == null;
        } else {
            zEquals2 = this.encoding.equals(xMLObject.getEncoding());
        }
        boolean z3 = zEquals2;
        if (this.mimeType == null) {
            zEquals3 = xMLObject.getMimeType() == null;
        } else {
            zEquals3 = this.mimeType.equals(xMLObject.getMimeType());
        }
        return z2 && z3 && zEquals3 && equalsContent(xMLObject.getContent());
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        if (this.encoding != null) {
            iHashCode = (31 * iHashCode) + this.encoding.hashCode();
        }
        if (this.mimeType != null) {
            iHashCode = (31 * iHashCode) + this.mimeType.hashCode();
        }
        return (31 * iHashCode) + this.content.hashCode();
    }

    private boolean equalsContent(List<XMLStructure> list) {
        if (this.content.size() != list.size()) {
            return false;
        }
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            XMLStructure xMLStructure = list.get(i2);
            XMLStructure xMLStructure2 = this.content.get(i2);
            if (xMLStructure instanceof javax.xml.crypto.dom.DOMStructure) {
                if (!(xMLStructure2 instanceof javax.xml.crypto.dom.DOMStructure)) {
                    return false;
                }
                if (!DOMUtils.nodesEqual(((javax.xml.crypto.dom.DOMStructure) xMLStructure2).getNode(), ((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode())) {
                    return false;
                }
            } else if (!xMLStructure2.equals(xMLStructure)) {
                return false;
            }
        }
        return true;
    }
}
