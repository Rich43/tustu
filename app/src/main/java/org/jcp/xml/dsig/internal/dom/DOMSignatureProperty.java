package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.SignatureProperty;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignatureProperty.class */
public final class DOMSignatureProperty extends DOMStructure implements SignatureProperty {
    private final String id;
    private final String target;
    private final List<XMLStructure> content;

    public DOMSignatureProperty(List<? extends XMLStructure> list, String str, String str2) {
        if (str == null) {
            throw new NullPointerException("target cannot be null");
        }
        if (list == null) {
            throw new NullPointerException("content cannot be null");
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("content cannot be empty");
        }
        this.content = Collections.unmodifiableList(new ArrayList(list));
        int size = this.content.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!(this.content.get(i2) instanceof XMLStructure)) {
                throw new ClassCastException("content[" + i2 + "] is not a valid type");
            }
        }
        this.target = str;
        this.id = str2;
    }

    public DOMSignatureProperty(Element element) throws MarshalException, DOMException {
        this.target = DOMUtils.getAttributeValue(element, Constants._ATT_TARGET);
        if (this.target == null) {
            throw new MarshalException("target cannot be null");
        }
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
            if (node == null) {
                break;
            }
            arrayList.add(new javax.xml.crypto.dom.DOMStructure(node));
            firstChild = node.getNextSibling();
        }
        if (arrayList.isEmpty()) {
            throw new MarshalException("content cannot be empty");
        }
        this.content = Collections.unmodifiableList(arrayList);
    }

    @Override // javax.xml.crypto.dsig.SignatureProperty
    public List<XMLStructure> getContent() {
        return this.content;
    }

    @Override // javax.xml.crypto.dsig.SignatureProperty
    public String getId() {
        return this.id;
    }

    @Override // javax.xml.crypto.dsig.SignatureProperty
    public String getTarget() {
        return this.target;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Element elementCreateElement = DOMUtils.createElement(DOMUtils.getOwnerDocument(node), Constants._TAG_SIGNATUREPROPERTY, "http://www.w3.org/2000/09/xmldsig#", str);
        DOMUtils.setAttributeID(elementCreateElement, Constants._ATT_ID, this.id);
        DOMUtils.setAttribute(elementCreateElement, Constants._ATT_TARGET, this.target);
        Iterator<XMLStructure> it = this.content.iterator();
        while (it.hasNext()) {
            DOMUtils.appendChild(elementCreateElement, ((javax.xml.crypto.dom.DOMStructure) it.next()).getNode());
        }
        node.appendChild(elementCreateElement);
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SignatureProperty)) {
            return false;
        }
        SignatureProperty signatureProperty = (SignatureProperty) obj;
        if (this.id == null) {
            zEquals = signatureProperty.getId() == null;
        } else {
            zEquals = this.id.equals(signatureProperty.getId());
        }
        return equalsContent(signatureProperty.getContent()) && this.target.equals(signatureProperty.getTarget()) && zEquals;
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        return (31 * ((31 * iHashCode) + this.target.hashCode())) + this.content.hashCode();
    }

    private boolean equalsContent(List<XMLStructure> list) {
        int size = list.size();
        if (this.content.size() != size) {
            return false;
        }
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
