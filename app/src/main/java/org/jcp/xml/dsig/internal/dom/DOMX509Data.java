package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMX509Data.class */
public final class DOMX509Data extends DOMStructure implements X509Data {
    private final List<Object> content;
    private CertificateFactory cf;

    /* JADX WARN: Multi-variable type inference failed */
    public DOMX509Data(List<?> list) {
        if (list == null) {
            throw new NullPointerException("content cannot be null");
        }
        ArrayList arrayList = new ArrayList(list);
        if (arrayList.isEmpty()) {
            throw new IllegalArgumentException("content cannot be empty");
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            E e2 = arrayList.get(i2);
            if (e2 instanceof String) {
                new X500Principal((String) e2);
            } else if (!(e2 instanceof byte[]) && !(e2 instanceof X509Certificate) && !(e2 instanceof X509CRL) && !(e2 instanceof XMLStructure)) {
                throw new ClassCastException("content[" + i2 + "] is not a valid X509Data type");
            }
        }
        this.content = Collections.unmodifiableList(arrayList);
    }

    public DOMX509Data(Element element) throws MarshalException {
        ArrayList arrayList = new ArrayList();
        Node firstChild = element.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1) {
                    Element element2 = (Element) node;
                    String localName = element2.getLocalName();
                    String namespaceURI = element2.getNamespaceURI();
                    if (Constants._TAG_X509CERTIFICATE.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        arrayList.add(unmarshalX509Certificate(element2));
                    } else if (Constants._TAG_X509ISSUERSERIAL.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        arrayList.add(new DOMX509IssuerSerial(element2));
                    } else if (Constants._TAG_X509SUBJECTNAME.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        arrayList.add(element2.getFirstChild().getNodeValue());
                    } else if (Constants._TAG_X509SKI.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        arrayList.add(XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(element2)));
                    } else if (Constants._TAG_X509CRL.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        arrayList.add(unmarshalX509CRL(element2));
                    } else {
                        arrayList.add(new javax.xml.crypto.dom.DOMStructure(element2));
                    }
                }
                firstChild = node.getNextSibling();
            } else {
                this.content = Collections.unmodifiableList(arrayList);
                return;
            }
        }
    }

    @Override // javax.xml.crypto.dsig.keyinfo.X509Data
    public List<Object> getContent() {
        return this.content;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_X509DATA, "http://www.w3.org/2000/09/xmldsig#", str);
        int size = this.content.size();
        for (int i2 = 0; i2 < size; i2++) {
            Object obj = this.content.get(i2);
            if (obj instanceof X509Certificate) {
                marshalCert((X509Certificate) obj, elementCreateElement, ownerDocument, str);
            } else if (obj instanceof XMLStructure) {
                if (obj instanceof X509IssuerSerial) {
                    ((DOMX509IssuerSerial) obj).marshal(elementCreateElement, str, dOMCryptoContext);
                } else {
                    DOMUtils.appendChild(elementCreateElement, ((javax.xml.crypto.dom.DOMStructure) obj).getNode());
                }
            } else if (obj instanceof byte[]) {
                marshalSKI((byte[]) obj, elementCreateElement, ownerDocument, str);
            } else if (obj instanceof String) {
                marshalSubjectName((String) obj, elementCreateElement, ownerDocument, str);
            } else if (obj instanceof X509CRL) {
                marshalCRL((X509CRL) obj, elementCreateElement, ownerDocument, str);
            }
        }
        node.appendChild(elementCreateElement);
    }

    private void marshalSKI(byte[] bArr, Node node, Document document, String str) throws DOMException {
        Element elementCreateElement = DOMUtils.createElement(document, Constants._TAG_X509SKI, "http://www.w3.org/2000/09/xmldsig#", str);
        elementCreateElement.appendChild(document.createTextNode(XMLUtils.encodeToString(bArr)));
        node.appendChild(elementCreateElement);
    }

    private void marshalSubjectName(String str, Node node, Document document, String str2) throws DOMException {
        Element elementCreateElement = DOMUtils.createElement(document, Constants._TAG_X509SUBJECTNAME, "http://www.w3.org/2000/09/xmldsig#", str2);
        elementCreateElement.appendChild(document.createTextNode(str));
        node.appendChild(elementCreateElement);
    }

    private void marshalCert(X509Certificate x509Certificate, Node node, Document document, String str) throws MarshalException, DOMException {
        Element elementCreateElement = DOMUtils.createElement(document, Constants._TAG_X509CERTIFICATE, "http://www.w3.org/2000/09/xmldsig#", str);
        try {
            elementCreateElement.appendChild(document.createTextNode(XMLUtils.encodeToString(x509Certificate.getEncoded())));
            node.appendChild(elementCreateElement);
        } catch (CertificateEncodingException e2) {
            throw new MarshalException("Error encoding X509Certificate", e2);
        }
    }

    private void marshalCRL(X509CRL x509crl, Node node, Document document, String str) throws MarshalException, DOMException {
        Element elementCreateElement = DOMUtils.createElement(document, Constants._TAG_X509CRL, "http://www.w3.org/2000/09/xmldsig#", str);
        try {
            elementCreateElement.appendChild(document.createTextNode(XMLUtils.encodeToString(x509crl.getEncoded())));
            node.appendChild(elementCreateElement);
        } catch (CRLException e2) {
            throw new MarshalException("Error encoding X509CRL", e2);
        }
    }

    private X509Certificate unmarshalX509Certificate(Element element) throws MarshalException {
        try {
            ByteArrayInputStream byteArrayInputStreamUnmarshalBase64Binary = unmarshalBase64Binary(element);
            Throwable th = null;
            try {
                try {
                    X509Certificate x509Certificate = (X509Certificate) this.cf.generateCertificate(byteArrayInputStreamUnmarshalBase64Binary);
                    if (byteArrayInputStreamUnmarshalBase64Binary != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStreamUnmarshalBase64Binary.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            byteArrayInputStreamUnmarshalBase64Binary.close();
                        }
                    }
                    return x509Certificate;
                } finally {
                }
            } finally {
            }
        } catch (IOException e2) {
            throw new MarshalException("Error closing stream", e2);
        } catch (CertificateException e3) {
            throw new MarshalException("Cannot create X509Certificate", e3);
        }
    }

    private X509CRL unmarshalX509CRL(Element element) throws MarshalException {
        try {
            ByteArrayInputStream byteArrayInputStreamUnmarshalBase64Binary = unmarshalBase64Binary(element);
            Throwable th = null;
            try {
                try {
                    X509CRL x509crl = (X509CRL) this.cf.generateCRL(byteArrayInputStreamUnmarshalBase64Binary);
                    if (byteArrayInputStreamUnmarshalBase64Binary != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStreamUnmarshalBase64Binary.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            byteArrayInputStreamUnmarshalBase64Binary.close();
                        }
                    }
                    return x509crl;
                } finally {
                }
            } finally {
            }
        } catch (IOException e2) {
            throw new MarshalException("Error closing stream", e2);
        } catch (CRLException e3) {
            throw new MarshalException("Cannot create X509CRL", e3);
        }
    }

    private ByteArrayInputStream unmarshalBase64Binary(Element element) throws MarshalException {
        try {
            if (this.cf == null) {
                this.cf = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
            }
            return new ByteArrayInputStream(XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(element)));
        } catch (CertificateException e2) {
            throw new MarshalException("Cannot create CertificateFactory", e2);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X509Data)) {
            return false;
        }
        List content = ((X509Data) obj).getContent();
        int size = this.content.size();
        if (size != content.size()) {
            return false;
        }
        for (int i2 = 0; i2 < size; i2++) {
            Object obj2 = this.content.get(i2);
            Object obj3 = content.get(i2);
            if (obj2 instanceof byte[]) {
                if (!(obj3 instanceof byte[]) || !Arrays.equals((byte[]) obj2, (byte[]) obj3)) {
                    return false;
                }
            } else if (!obj2.equals(obj3)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return (31 * 17) + this.content.hashCode();
    }
}
