package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/SignedInfo.class */
public class SignedInfo extends Manifest {
    private SignatureAlgorithm signatureAlgorithm;
    private byte[] c14nizedBytes;
    private Element c14nMethod;
    private Element signatureMethod;

    public SignedInfo(Document document) throws XMLSecurityException {
        this(document, "http://www.w3.org/2000/09/xmldsig#dsa-sha1", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
    }

    public SignedInfo(Document document, String str, String str2) throws XMLSecurityException {
        this(document, str, 0, str2);
    }

    public SignedInfo(Document document, String str, int i2, String str2) throws DOMException, XMLSecurityException {
        super(document);
        this.c14nMethod = XMLUtils.createElementInSignatureSpace(getDocument(), Constants._TAG_CANONICALIZATIONMETHOD);
        this.c14nMethod.setAttributeNS(null, Constants._ATT_ALGORITHM, str2);
        appendSelf(this.c14nMethod);
        addReturnToSelf();
        if (i2 > 0) {
            this.signatureAlgorithm = new SignatureAlgorithm(getDocument(), str, i2);
        } else {
            this.signatureAlgorithm = new SignatureAlgorithm(getDocument(), str);
        }
        this.signatureMethod = this.signatureAlgorithm.getElement();
        appendSelf(this.signatureMethod);
        addReturnToSelf();
    }

    public SignedInfo(Document document, Element element, Element element2) throws XMLSecurityException {
        super(document);
        this.c14nMethod = element2;
        appendSelf(this.c14nMethod);
        addReturnToSelf();
        this.signatureAlgorithm = new SignatureAlgorithm(element, (String) null);
        this.signatureMethod = this.signatureAlgorithm.getElement();
        appendSelf(this.signatureMethod);
        addReturnToSelf();
    }

    public SignedInfo(Element element, String str) throws XMLSecurityException {
        this(element, str, true);
    }

    public SignedInfo(Element element, String str, boolean z2) throws XMLSecurityException {
        super(reparseSignedInfoElem(element, z2), str, z2);
        this.c14nMethod = XMLUtils.getNextElement(element.getFirstChild());
        this.signatureMethod = XMLUtils.getNextElement(this.c14nMethod.getNextSibling());
        this.signatureAlgorithm = new SignatureAlgorithm(this.signatureMethod, getBaseURI(), z2);
    }

    private static Element reparseSignedInfoElem(Element element, boolean z2) throws DOMException, XMLSecurityException {
        String attributeNS = XMLUtils.getNextElement(element.getFirstChild()).getAttributeNS(null, Constants._ATT_ALGORITHM);
        if (!attributeNS.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315") && !attributeNS.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments") && !attributeNS.equals("http://www.w3.org/2001/10/xml-exc-c14n#") && !attributeNS.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments") && !attributeNS.equals("http://www.w3.org/2006/12/xml-c14n11") && !attributeNS.equals("http://www.w3.org/2006/12/xml-c14n11#WithComments")) {
            try {
                Canonicalizer canonicalizer = Canonicalizer.getInstance(attributeNS);
                canonicalizer.setSecureValidation(z2);
                byte[] bArrCanonicalizeSubtree = canonicalizer.canonicalizeSubtree(element);
                DocumentBuilder documentBuilderCreateDocumentBuilder = XMLUtils.createDocumentBuilder(false, z2);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArrCanonicalizeSubtree);
                Throwable th = null;
                try {
                    try {
                        Node nodeImportNode = element.getOwnerDocument().importNode(documentBuilderCreateDocumentBuilder.parse(byteArrayInputStream).getDocumentElement(), true);
                        element.getParentNode().replaceChild(nodeImportNode, element);
                        Element element2 = (Element) nodeImportNode;
                        if (byteArrayInputStream != null) {
                            if (0 != 0) {
                                try {
                                    byteArrayInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                byteArrayInputStream.close();
                            }
                        }
                        return element2;
                    } catch (Throwable th3) {
                        if (byteArrayInputStream != null) {
                            if (th != null) {
                                try {
                                    byteArrayInputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                byteArrayInputStream.close();
                            }
                        }
                        throw th3;
                    }
                } finally {
                }
            } catch (IOException e2) {
                throw new XMLSecurityException(e2);
            } catch (ParserConfigurationException e3) {
                throw new XMLSecurityException(e3);
            } catch (SAXException e4) {
                throw new XMLSecurityException(e4);
            }
        }
        return element;
    }

    public boolean verify() throws XMLSecurityException {
        return super.verifyReferences(false);
    }

    public boolean verify(boolean z2) throws XMLSecurityException {
        return super.verifyReferences(z2);
    }

    public byte[] getCanonicalizedOctetStream() throws XMLSecurityException {
        if (this.c14nizedBytes == null) {
            Canonicalizer canonicalizer = Canonicalizer.getInstance(getCanonicalizationMethodURI());
            canonicalizer.setSecureValidation(isSecureValidation());
            String inclusiveNamespaces = getInclusiveNamespaces();
            if (inclusiveNamespaces == null) {
                this.c14nizedBytes = canonicalizer.canonicalizeSubtree(getElement());
            } else {
                this.c14nizedBytes = canonicalizer.canonicalizeSubtree(getElement(), inclusiveNamespaces);
            }
        }
        return (byte[]) this.c14nizedBytes.clone();
    }

    public void signInOctetStream(OutputStream outputStream) throws XMLSecurityException {
        if (this.c14nizedBytes == null) {
            Canonicalizer canonicalizer = Canonicalizer.getInstance(getCanonicalizationMethodURI());
            canonicalizer.setSecureValidation(isSecureValidation());
            canonicalizer.setWriter(outputStream);
            String inclusiveNamespaces = getInclusiveNamespaces();
            if (inclusiveNamespaces == null) {
                canonicalizer.canonicalizeSubtree(getElement());
                return;
            } else {
                canonicalizer.canonicalizeSubtree(getElement(), inclusiveNamespaces);
                return;
            }
        }
        try {
            outputStream.write(this.c14nizedBytes);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public String getCanonicalizationMethodURI() {
        return this.c14nMethod.getAttributeNS(null, Constants._ATT_ALGORITHM);
    }

    public String getSignatureMethodURI() {
        Element signatureMethodElement = getSignatureMethodElement();
        if (signatureMethodElement != null) {
            return signatureMethodElement.getAttributeNS(null, Constants._ATT_ALGORITHM);
        }
        return null;
    }

    public Element getSignatureMethodElement() {
        return this.signatureMethod;
    }

    public SecretKey createSecretKey(byte[] bArr) {
        return new SecretKeySpec(bArr, this.signatureAlgorithm.getJCEAlgorithmString());
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }

    @Override // com.sun.org.apache.xml.internal.security.signature.Manifest, com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_SIGNEDINFO;
    }

    public String getInclusiveNamespaces() {
        Element nextElement;
        String canonicalizationMethodURI = getCanonicalizationMethodURI();
        if ((canonicalizationMethodURI.equals("http://www.w3.org/2001/10/xml-exc-c14n#") || canonicalizationMethodURI.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")) && (nextElement = XMLUtils.getNextElement(this.c14nMethod.getFirstChild())) != null) {
            try {
                return new InclusiveNamespaces(nextElement, "http://www.w3.org/2001/10/xml-exc-c14n#").getInclusiveNamespaces();
            } catch (XMLSecurityException e2) {
                return null;
            }
        }
        return null;
    }
}
