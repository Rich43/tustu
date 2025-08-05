package com.sun.org.apache.xml.internal.security.keys.content;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509CRL;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Digest;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509IssuerSerial;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SubjectName;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/content/X509Data.class */
public class X509Data extends SignatureElementProxy implements KeyInfoContent {
    private static final Logger LOG = LoggerFactory.getLogger(X509Data.class);

    public X509Data(Document document) {
        super(document);
        addReturnToSelf();
    }

    public X509Data(Element element, String str) throws XMLSecurityException {
        Node node;
        super(element, str);
        Node firstChild = getFirstChild();
        while (true) {
            node = firstChild;
            if (node == null || node.getNodeType() == 1) {
                break;
            } else {
                firstChild = node.getNextSibling();
            }
        }
        if (node == null || node.getNodeType() != 1) {
            throw new XMLSecurityException("xml.WrongContent", new Object[]{"Elements", Constants._TAG_X509DATA});
        }
    }

    public void addIssuerSerial(String str, BigInteger bigInteger) {
        add(new XMLX509IssuerSerial(getDocument(), str, bigInteger));
    }

    public void addIssuerSerial(String str, String str2) {
        add(new XMLX509IssuerSerial(getDocument(), str, str2));
    }

    public void addIssuerSerial(String str, int i2) {
        add(new XMLX509IssuerSerial(getDocument(), str, i2));
    }

    public void add(XMLX509IssuerSerial xMLX509IssuerSerial) {
        appendSelf(xMLX509IssuerSerial);
        addReturnToSelf();
    }

    public void addSKI(byte[] bArr) {
        add(new XMLX509SKI(getDocument(), bArr));
    }

    public void addSKI(X509Certificate x509Certificate) throws XMLSecurityException {
        add(new XMLX509SKI(getDocument(), x509Certificate));
    }

    public void add(XMLX509SKI xmlx509ski) {
        appendSelf(xmlx509ski);
        addReturnToSelf();
    }

    public void addSubjectName(String str) {
        add(new XMLX509SubjectName(getDocument(), str));
    }

    public void addSubjectName(X509Certificate x509Certificate) {
        add(new XMLX509SubjectName(getDocument(), x509Certificate));
    }

    public void add(XMLX509SubjectName xMLX509SubjectName) {
        appendSelf(xMLX509SubjectName);
        addReturnToSelf();
    }

    public void addCertificate(X509Certificate x509Certificate) throws XMLSecurityException {
        add(new XMLX509Certificate(getDocument(), x509Certificate));
    }

    public void addCertificate(byte[] bArr) {
        add(new XMLX509Certificate(getDocument(), bArr));
    }

    public void add(XMLX509Certificate xMLX509Certificate) {
        appendSelf(xMLX509Certificate);
        addReturnToSelf();
    }

    public void addCRL(byte[] bArr) {
        add(new XMLX509CRL(getDocument(), bArr));
    }

    public void add(XMLX509CRL xmlx509crl) {
        appendSelf(xmlx509crl);
        addReturnToSelf();
    }

    public void addDigest(X509Certificate x509Certificate, String str) throws XMLSecurityException {
        add(new XMLX509Digest(getDocument(), x509Certificate, str));
    }

    public void addDigest(byte[] bArr, String str) {
        add(new XMLX509Digest(getDocument(), bArr, str));
    }

    public void add(XMLX509Digest xMLX509Digest) {
        appendSelf(xMLX509Digest);
        addReturnToSelf();
    }

    public void addUnknownElement(Element element) {
        appendSelf(element);
        addReturnToSelf();
    }

    public int lengthIssuerSerial() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_X509ISSUERSERIAL);
    }

    public int lengthSKI() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_X509SKI);
    }

    public int lengthSubjectName() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_X509SUBJECTNAME);
    }

    public int lengthCertificate() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_X509CERTIFICATE);
    }

    public int lengthCRL() {
        return length("http://www.w3.org/2000/09/xmldsig#", Constants._TAG_X509CRL);
    }

    public int lengthDigest() {
        return length(Constants.SignatureSpec11NS, Constants._TAG_X509DIGEST);
    }

    public int lengthUnknownElement() {
        int i2 = 0;
        Node firstChild = getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1 && !node.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) {
                    i2++;
                }
                firstChild = node.getNextSibling();
            } else {
                return i2;
            }
        }
    }

    public XMLX509IssuerSerial itemIssuerSerial(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_X509ISSUERSERIAL, i2);
        if (elementSelectDsNode != null) {
            return new XMLX509IssuerSerial(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public XMLX509SKI itemSKI(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_X509SKI, i2);
        if (elementSelectDsNode != null) {
            return new XMLX509SKI(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public XMLX509SubjectName itemSubjectName(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_X509SUBJECTNAME, i2);
        if (elementSelectDsNode != null) {
            return new XMLX509SubjectName(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public XMLX509Certificate itemCertificate(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_X509CERTIFICATE, i2);
        if (elementSelectDsNode != null) {
            return new XMLX509Certificate(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public XMLX509CRL itemCRL(int i2) throws XMLSecurityException {
        Element elementSelectDsNode = XMLUtils.selectDsNode(getFirstChild(), Constants._TAG_X509CRL, i2);
        if (elementSelectDsNode != null) {
            return new XMLX509CRL(elementSelectDsNode, this.baseURI);
        }
        return null;
    }

    public XMLX509Digest itemDigest(int i2) throws XMLSecurityException {
        Element elementSelectDs11Node = XMLUtils.selectDs11Node(getFirstChild(), Constants._TAG_X509DIGEST, i2);
        if (elementSelectDs11Node != null) {
            return new XMLX509Digest(elementSelectDs11Node, this.baseURI);
        }
        return null;
    }

    public Element itemUnknownElement(int i2) {
        LOG.debug("itemUnknownElement not implemented: {}", Integer.valueOf(i2));
        return null;
    }

    public boolean containsIssuerSerial() {
        return lengthIssuerSerial() > 0;
    }

    public boolean containsSKI() {
        return lengthSKI() > 0;
    }

    public boolean containsSubjectName() {
        return lengthSubjectName() > 0;
    }

    public boolean containsCertificate() {
        return lengthCertificate() > 0;
    }

    public boolean containsDigest() {
        return lengthDigest() > 0;
    }

    public boolean containsCRL() {
        return lengthCRL() > 0;
    }

    public boolean containsUnknownElement() {
        return lengthUnknownElement() > 0;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_X509DATA;
    }
}
