package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.SignatureProperties;
import javax.xml.crypto.dsig.SignatureProperty;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureProperties.class */
public final class DOMSignatureProperties extends DOMStructure implements SignatureProperties {
    private final String id;
    private final List<SignatureProperty> properties;

    public DOMSignatureProperties(List<? extends SignatureProperty> list, String str) {
        if (list == null) {
            throw new NullPointerException("properties cannot be null");
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("properties cannot be empty");
        }
        this.properties = Collections.unmodifiableList(new ArrayList(list));
        int size = this.properties.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!(this.properties.get(i2) instanceof SignatureProperty)) {
                throw new ClassCastException("properties[" + i2 + "] is not a valid type");
            }
        }
        this.id = str;
    }

    public DOMSignatureProperties(Element element) throws MarshalException, DOMException {
        String localName;
        String namespaceURI;
        Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            this.id = attributeNodeNS.getValue();
            element.setIdAttributeNode(attributeNodeNS, true);
        } else {
            this.id = null;
        }
        ArrayList arrayList = new ArrayList();
        Node firstChild = element.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1) {
                    localName = node.getLocalName();
                    namespaceURI = node.getNamespaceURI();
                    if (!Constants._TAG_SIGNATUREPROPERTY.equals(localName) || !"http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        break;
                    } else {
                        arrayList.add(new DOMSignatureProperty((Element) node));
                    }
                }
                firstChild = node.getNextSibling();
            } else {
                if (arrayList.isEmpty()) {
                    throw new MarshalException("properties cannot be empty");
                }
                this.properties = Collections.unmodifiableList(arrayList);
                return;
            }
        }
        throw new MarshalException("Invalid element name: " + namespaceURI + CallSiteDescriptor.TOKEN_DELIMITER + localName + ", expected SignatureProperty");
    }

    @Override // javax.xml.crypto.dsig.SignatureProperties
    public List<SignatureProperty> getProperties() {
        return this.properties;
    }

    @Override // javax.xml.crypto.dsig.SignatureProperties
    public String getId() {
        return this.id;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Element elementCreateElement = DOMUtils.createElement(DOMUtils.getOwnerDocument(node), Constants._TAG_SIGNATUREPROPERTIES, "http://www.w3.org/2000/09/xmldsig#", str);
        DOMUtils.setAttributeID(elementCreateElement, Constants._ATT_ID, this.id);
        Iterator<SignatureProperty> it = this.properties.iterator();
        while (it.hasNext()) {
            ((DOMSignatureProperty) it.next()).marshal(elementCreateElement, str, dOMCryptoContext);
        }
        node.appendChild(elementCreateElement);
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SignatureProperties)) {
            return false;
        }
        SignatureProperties signatureProperties = (SignatureProperties) obj;
        if (this.id == null) {
            zEquals = signatureProperties.getId() == null;
        } else {
            zEquals = this.id.equals(signatureProperties.getId());
        }
        return this.properties.equals(signatureProperties.getProperties()) && zEquals;
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        return (31 * iHashCode) + this.properties.hashCode();
    }
}
