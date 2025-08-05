package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.keys.content.RetrievalMethod;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/implementations/RetrievalMethodResolver.class */
public class RetrievalMethodResolver extends KeyResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(RetrievalMethodResolver.class);

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public PublicKey engineLookupAndResolvePublicKey(Element element, String str, StorageResolver storageResolver) {
        if (!XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_RETRIEVALMETHOD)) {
            return null;
        }
        try {
            RetrievalMethod retrievalMethod = new RetrievalMethod(element, str);
            String type = retrievalMethod.getType();
            XMLSignatureInput xMLSignatureInputResolveInput = resolveInput(retrievalMethod, str, this.secureValidation);
            if ("http://www.w3.org/2000/09/xmldsig#rawX509Certificate".equals(type)) {
                X509Certificate rawCertificate = getRawCertificate(xMLSignatureInputResolveInput);
                if (rawCertificate != null) {
                    return rawCertificate.getPublicKey();
                }
                return null;
            }
            Element elementObtainReferenceElement = obtainReferenceElement(xMLSignatureInputResolveInput, this.secureValidation);
            if (XMLUtils.elementIsInSignatureSpace(elementObtainReferenceElement, Constants._TAG_RETRIEVALMETHOD)) {
                if (this.secureValidation) {
                    if (!LOG.isDebugEnabled()) {
                        return null;
                    }
                    LOG.debug("Error: It is forbidden to have one RetrievalMethod point to another with secure validation");
                    return null;
                }
                if (obtainReferenceElement(resolveInput(new RetrievalMethod(elementObtainReferenceElement, str), str, this.secureValidation), this.secureValidation) == element) {
                    LOG.debug("Error: Can't have RetrievalMethods pointing to each other");
                    return null;
                }
            }
            return resolveKey(elementObtainReferenceElement, str, storageResolver);
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        } catch (IOException e3) {
            LOG.debug("IOException", e3);
            return null;
        } catch (CertificateException e4) {
            LOG.debug("CertificateException", e4);
            return null;
        } catch (ParserConfigurationException e5) {
            LOG.debug("ParserConfigurationException", e5);
            return null;
        } catch (SAXException e6) {
            LOG.debug("SAXException", e6);
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public X509Certificate engineLookupResolveX509Certificate(Element element, String str, StorageResolver storageResolver) {
        if (!XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_RETRIEVALMETHOD)) {
            return null;
        }
        try {
            RetrievalMethod retrievalMethod = new RetrievalMethod(element, str);
            String type = retrievalMethod.getType();
            XMLSignatureInput xMLSignatureInputResolveInput = resolveInput(retrievalMethod, str, this.secureValidation);
            if ("http://www.w3.org/2000/09/xmldsig#rawX509Certificate".equals(type)) {
                return getRawCertificate(xMLSignatureInputResolveInput);
            }
            Element elementObtainReferenceElement = obtainReferenceElement(xMLSignatureInputResolveInput, this.secureValidation);
            if (XMLUtils.elementIsInSignatureSpace(elementObtainReferenceElement, Constants._TAG_RETRIEVALMETHOD)) {
                if (this.secureValidation) {
                    if (!LOG.isDebugEnabled()) {
                        return null;
                    }
                    LOG.debug("Error: It is forbidden to have one RetrievalMethod point to another with secure validation");
                    return null;
                }
                if (obtainReferenceElement(resolveInput(new RetrievalMethod(elementObtainReferenceElement, str), str, this.secureValidation), this.secureValidation) == element) {
                    LOG.debug("Error: Can't have RetrievalMethods pointing to each other");
                    return null;
                }
            }
            return resolveCertificate(elementObtainReferenceElement, str, storageResolver);
        } catch (XMLSecurityException e2) {
            LOG.debug("XMLSecurityException", e2);
            return null;
        } catch (IOException e3) {
            LOG.debug("IOException", e3);
            return null;
        } catch (CertificateException e4) {
            LOG.debug("CertificateException", e4);
            return null;
        } catch (ParserConfigurationException e5) {
            LOG.debug("ParserConfigurationException", e5);
            return null;
        } catch (SAXException e6) {
            LOG.debug("SAXException", e6);
            return null;
        }
    }

    private static X509Certificate resolveCertificate(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Now we have a {" + element.getNamespaceURI() + "}" + element.getLocalName() + " Element");
        }
        if (element != null) {
            return KeyResolver.getX509Certificate(element, str, storageResolver);
        }
        return null;
    }

    private static PublicKey resolveKey(Element element, String str, StorageResolver storageResolver) throws KeyResolverException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Now we have a {" + element.getNamespaceURI() + "}" + element.getLocalName() + " Element");
        }
        if (element != null) {
            return KeyResolver.getPublicKey(element, str, storageResolver);
        }
        return null;
    }

    private static Element obtainReferenceElement(XMLSignatureInput xMLSignatureInput, boolean z2) throws CanonicalizationException, KeyResolverException, ParserConfigurationException, SAXException, IOException {
        Element docFromBytes;
        if (xMLSignatureInput.isElement()) {
            docFromBytes = (Element) xMLSignatureInput.getSubNode();
        } else if (xMLSignatureInput.isNodeSet()) {
            docFromBytes = getDocumentElement(xMLSignatureInput.getNodeSet());
        } else {
            byte[] bytes = xMLSignatureInput.getBytes();
            docFromBytes = getDocFromBytes(bytes, z2);
            LOG.debug("we have to parse {} bytes", Integer.valueOf(bytes.length));
        }
        return docFromBytes;
    }

    private static X509Certificate getRawCertificate(XMLSignatureInput xMLSignatureInput) throws CanonicalizationException, IOException, CertificateException {
        byte[] bytes = xMLSignatureInput.getBytes();
        CertificateFactory certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Throwable th = null;
        try {
            try {
                X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
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
                return x509Certificate;
            } finally {
            }
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
    }

    private static XMLSignatureInput resolveInput(RetrievalMethod retrievalMethod, String str, boolean z2) throws XMLSecurityException {
        Attr uRIAttr = retrievalMethod.getURIAttr();
        Transforms transforms = retrievalMethod.getTransforms();
        XMLSignatureInput xMLSignatureInputResolve = ResourceResolver.getInstance(uRIAttr, str, z2).resolve(uRIAttr, str, z2);
        if (transforms != null) {
            LOG.debug("We have Transforms");
            xMLSignatureInputResolve = transforms.performTransforms(xMLSignatureInputResolve);
        }
        return xMLSignatureInputResolve;
    }

    @Override // com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
    public SecretKey engineLookupAndResolveSecretKey(Element element, String str, StorageResolver storageResolver) {
        return null;
    }

    private static Element getDocumentElement(Set<Node> set) {
        Iterator<Node> it = set.iterator();
        Element element = null;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Node next = it.next();
            if (next != null && 1 == next.getNodeType()) {
                element = (Element) next;
                break;
            }
        }
        ArrayList arrayList = new ArrayList();
        while (element != null) {
            arrayList.add(element);
            Node parentNode = element.getParentNode();
            if (parentNode == null || 1 != parentNode.getNodeType()) {
                break;
            }
            element = (Element) parentNode;
        }
        ListIterator<E> listIterator = arrayList.listIterator(arrayList.size() - 1);
        while (listIterator.hasPrevious()) {
            Element element2 = (Element) listIterator.previous();
            if (set.contains(element2)) {
                return element2;
            }
        }
        return null;
    }
}
