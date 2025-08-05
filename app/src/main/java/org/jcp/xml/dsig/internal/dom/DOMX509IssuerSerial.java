package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.math.BigInteger;
import javax.security.auth.x500.X500Principal;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMX509IssuerSerial.class */
public final class DOMX509IssuerSerial extends DOMStructure implements X509IssuerSerial {
    private final String issuerName;
    private final BigInteger serialNumber;

    public DOMX509IssuerSerial(String str, BigInteger bigInteger) {
        if (str == null) {
            throw new NullPointerException("issuerName cannot be null");
        }
        if (bigInteger == null) {
            throw new NullPointerException("serialNumber cannot be null");
        }
        new X500Principal(str);
        this.issuerName = str;
        this.serialNumber = bigInteger;
    }

    public DOMX509IssuerSerial(Element element) throws MarshalException {
        Element firstChildElement = DOMUtils.getFirstChildElement(element, Constants._TAG_X509ISSUERNAME, "http://www.w3.org/2000/09/xmldsig#");
        Element nextSiblingElement = DOMUtils.getNextSiblingElement(firstChildElement, Constants._TAG_X509SERIALNUMBER, "http://www.w3.org/2000/09/xmldsig#");
        this.issuerName = firstChildElement.getFirstChild().getNodeValue();
        this.serialNumber = new BigInteger(nextSiblingElement.getFirstChild().getNodeValue());
    }

    @Override // javax.xml.crypto.dsig.keyinfo.X509IssuerSerial
    public String getIssuerName() {
        return this.issuerName;
    }

    @Override // javax.xml.crypto.dsig.keyinfo.X509IssuerSerial
    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_X509ISSUERSERIAL, "http://www.w3.org/2000/09/xmldsig#", str);
        Element elementCreateElement2 = DOMUtils.createElement(ownerDocument, Constants._TAG_X509ISSUERNAME, "http://www.w3.org/2000/09/xmldsig#", str);
        Element elementCreateElement3 = DOMUtils.createElement(ownerDocument, Constants._TAG_X509SERIALNUMBER, "http://www.w3.org/2000/09/xmldsig#", str);
        elementCreateElement2.appendChild(ownerDocument.createTextNode(this.issuerName));
        elementCreateElement3.appendChild(ownerDocument.createTextNode(this.serialNumber.toString()));
        elementCreateElement.appendChild(elementCreateElement2);
        elementCreateElement.appendChild(elementCreateElement3);
        node.appendChild(elementCreateElement);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X509IssuerSerial)) {
            return false;
        }
        X509IssuerSerial x509IssuerSerial = (X509IssuerSerial) obj;
        return this.issuerName.equals(x509IssuerSerial.getIssuerName()) && this.serialNumber.equals(x509IssuerSerial.getSerialNumber());
    }

    public int hashCode() {
        return (31 * ((31 * 17) + this.issuerName.hashCode())) + this.serialNumber.hashCode();
    }
}
