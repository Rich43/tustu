package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.keyinfo.KeyName;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMKeyName.class */
public final class DOMKeyName extends DOMStructure implements KeyName {
    private final String name;

    public DOMKeyName(String str) {
        if (str == null) {
            throw new NullPointerException("name cannot be null");
        }
        this.name = str;
    }

    public DOMKeyName(Element element) {
        this.name = element.getFirstChild().getNodeValue();
    }

    @Override // javax.xml.crypto.dsig.keyinfo.KeyName
    public String getName() {
        return this.name;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_KEYNAME, "http://www.w3.org/2000/09/xmldsig#", str);
        elementCreateElement.appendChild(ownerDocument.createTextNode(this.name));
        node.appendChild(elementCreateElement);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyName)) {
            return false;
        }
        return this.name.equals(((KeyName) obj).getName());
    }

    public int hashCode() {
        return (31 * 17) + this.name.hashCode();
    }
}
