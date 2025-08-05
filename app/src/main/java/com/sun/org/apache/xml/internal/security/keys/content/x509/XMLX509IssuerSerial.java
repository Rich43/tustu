package com.sun.org.apache.xml.internal.security.keys.content.x509;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.RFC2253Parser;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/x509/XMLX509IssuerSerial.class */
public class XMLX509IssuerSerial extends SignatureElementProxy implements XMLX509DataContent {
    private static final Logger LOG = LoggerFactory.getLogger(XMLX509IssuerSerial.class);

    public XMLX509IssuerSerial(Element element, String str) throws XMLSecurityException {
        super(element, str);
    }

    public XMLX509IssuerSerial(Document document, String str, BigInteger bigInteger) {
        super(document);
        addReturnToSelf();
        addTextElement(str, Constants._TAG_X509ISSUERNAME);
        addTextElement(bigInteger.toString(), Constants._TAG_X509SERIALNUMBER);
    }

    public XMLX509IssuerSerial(Document document, String str, String str2) {
        this(document, str, new BigInteger(str2));
    }

    public XMLX509IssuerSerial(Document document, String str, int i2) {
        this(document, str, new BigInteger(Integer.toString(i2)));
    }

    public XMLX509IssuerSerial(Document document, X509Certificate x509Certificate) {
        this(document, x509Certificate.getIssuerX500Principal().getName(), x509Certificate.getSerialNumber());
    }

    public BigInteger getSerialNumber() {
        String textFromChildElement = getTextFromChildElement(Constants._TAG_X509SERIALNUMBER, "http://www.w3.org/2000/09/xmldsig#");
        LOG.debug("X509SerialNumber text: {}", textFromChildElement);
        return new BigInteger(textFromChildElement);
    }

    public int getSerialNumberInteger() {
        return getSerialNumber().intValue();
    }

    public String getIssuerName() {
        return RFC2253Parser.normalize(getTextFromChildElement(Constants._TAG_X509ISSUERNAME, "http://www.w3.org/2000/09/xmldsig#"));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof XMLX509IssuerSerial)) {
            return false;
        }
        XMLX509IssuerSerial xMLX509IssuerSerial = (XMLX509IssuerSerial) obj;
        return getSerialNumber().equals(xMLX509IssuerSerial.getSerialNumber()) && getIssuerName().equals(xMLX509IssuerSerial.getIssuerName());
    }

    public int hashCode() {
        return (31 * ((31 * 17) + getSerialNumber().hashCode())) + getIssuerName().hashCode();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_X509ISSUERSERIAL;
    }
}
