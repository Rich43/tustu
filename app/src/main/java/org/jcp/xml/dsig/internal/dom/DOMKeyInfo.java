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
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyInfo.class */
public final class DOMKeyInfo extends DOMStructure implements KeyInfo {
    private final String id;
    private final List<XMLStructure> keyInfoTypes;

    public static List<XMLStructure> getContent(KeyInfo keyInfo) {
        return keyInfo.getContent();
    }

    public DOMKeyInfo(List<? extends XMLStructure> list, String str) {
        if (list == null) {
            throw new NullPointerException("content cannot be null");
        }
        this.keyInfoTypes = Collections.unmodifiableList(new ArrayList(list));
        if (this.keyInfoTypes.isEmpty()) {
            throw new IllegalArgumentException("content cannot be empty");
        }
        int size = this.keyInfoTypes.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!(this.keyInfoTypes.get(i2) instanceof XMLStructure)) {
                throw new ClassCastException("content[" + i2 + "] is not a valid KeyInfo type");
            }
        }
        this.id = str;
    }

    public DOMKeyInfo(Element element, XMLCryptoContext xMLCryptoContext, Provider provider) throws MarshalException, DOMException {
        Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            this.id = attributeNodeNS.getValue();
            element.setIdAttributeNode(attributeNodeNS, true);
        } else {
            this.id = null;
        }
        ArrayList arrayList = new ArrayList();
        Node firstChild = element.getFirstChild();
        if (firstChild == null) {
            throw new MarshalException("KeyInfo must contain at least one type");
        }
        while (firstChild != null) {
            if (firstChild.getNodeType() == 1) {
                Element element2 = (Element) firstChild;
                String localName = element2.getLocalName();
                String namespaceURI = element2.getNamespaceURI();
                if (Constants._TAG_X509DATA.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(new DOMX509Data(element2));
                } else if (Constants._TAG_KEYNAME.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(new DOMKeyName(element2));
                } else if (Constants._TAG_KEYVALUE.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(DOMKeyValue.unmarshal(element2));
                } else if (Constants._TAG_RETRIEVALMETHOD.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(new DOMRetrievalMethod(element2, xMLCryptoContext, provider));
                } else if (Constants._TAG_PGPDATA.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                    arrayList.add(new DOMPGPData(element2));
                } else {
                    arrayList.add(new javax.xml.crypto.dom.DOMStructure(element2));
                }
            }
            firstChild = firstChild.getNextSibling();
        }
        this.keyInfoTypes = Collections.unmodifiableList(arrayList);
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfo
    public String getId() {
        return this.id;
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfo
    public List<XMLStructure> getContent() {
        return this.keyInfoTypes;
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyInfo
    public void marshal(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException, DOMException {
        if (xMLStructure == null) {
            throw new NullPointerException("parent is null");
        }
        if (!(xMLStructure instanceof javax.xml.crypto.dom.DOMStructure)) {
            throw new ClassCastException("parent must be of type DOMStructure");
        }
        Node node = ((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode();
        String signaturePrefix = DOMUtils.getSignaturePrefix(xMLCryptoContext);
        Element elementCreateElement = DOMUtils.createElement(DOMUtils.getOwnerDocument(node), Constants._TAG_KEYINFO, "http://www.w3.org/2000/09/xmldsig#", signaturePrefix);
        if (signaturePrefix == null || signaturePrefix.length() == 0) {
            elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
        } else {
            elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + signaturePrefix, "http://www.w3.org/2000/09/xmldsig#");
        }
        Node nextSibling = null;
        if (xMLCryptoContext instanceof DOMSignContext) {
            nextSibling = ((DOMSignContext) xMLCryptoContext).getNextSibling();
        }
        marshal(node, elementCreateElement, nextSibling, signaturePrefix, (DOMCryptoContext) xMLCryptoContext);
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        marshal(node, null, str, dOMCryptoContext);
    }

    public void marshal(Node node, Node node2, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        marshal(node, DOMUtils.createElement(DOMUtils.getOwnerDocument(node), Constants._TAG_KEYINFO, "http://www.w3.org/2000/09/xmldsig#", str), node2, str, dOMCryptoContext);
    }

    private void marshal(Node node, Element element, Node node2, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        for (XMLStructure xMLStructure : this.keyInfoTypes) {
            if (xMLStructure instanceof DOMStructure) {
                ((DOMStructure) xMLStructure).marshal(element, str, dOMCryptoContext);
            } else {
                DOMUtils.appendChild(element, ((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode());
            }
        }
        DOMUtils.setAttributeID(element, Constants._ATT_ID, this.id);
        node.insertBefore(element, node2);
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyInfo)) {
            return false;
        }
        KeyInfo keyInfo = (KeyInfo) obj;
        if (this.id == null) {
            zEquals = keyInfo.getId() == null;
        } else {
            zEquals = this.id.equals(keyInfo.getId());
        }
        return this.keyInfoTypes.equals(keyInfo.getContent()) && zEquals;
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        return (31 * iHashCode) + this.keyInfoTypes.hashCode();
    }
}
