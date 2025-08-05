package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.I18n;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/Manifest.class */
public class Manifest extends SignatureElementProxy {
    public static final int MAXIMUM_REFERENCE_COUNT = 30;
    private static final Logger LOG = LoggerFactory.getLogger(Manifest.class);
    private static Integer referenceCount = (Integer) AccessController.doPrivileged(() -> {
        return Integer.valueOf(Integer.parseInt(System.getProperty("com.sun.org.apache.xml.internal.security.maxReferences", Integer.toString(30))));
    });
    private List<Reference> references;
    private Element[] referencesEl;
    private List<VerifiedReference> verificationResults;
    private Map<String, String> resolverProperties;
    private List<ResourceResolver> perManifestResolvers;
    private boolean secureValidation;

    public Manifest(Document document) {
        super(document);
        addReturnToSelf();
        this.references = new ArrayList();
    }

    public Manifest(Element element, String str) throws XMLSecurityException {
        this(element, str, true);
    }

    public Manifest(Element element, String str, boolean z2) throws DOMException, XMLSecurityException {
        super(element, str);
        Attr attributeNodeNS = element.getAttributeNodeNS(null, Constants._ATT_ID);
        if (attributeNodeNS != null) {
            element.setIdAttributeNode(attributeNodeNS, true);
        }
        this.secureValidation = z2;
        this.referencesEl = XMLUtils.selectDsNodes(getFirstChild(), Constants._TAG_REFERENCE);
        int length = this.referencesEl.length;
        if (length == 0) {
            throw new DOMException((short) 4, I18n.translate("xml.WrongContent", new Object[]{Constants._TAG_REFERENCE, Constants._TAG_MANIFEST}));
        }
        if (z2 && length > referenceCount.intValue()) {
            throw new XMLSecurityException("signature.tooManyReferences", new Object[]{Integer.valueOf(length), referenceCount});
        }
        this.references = new ArrayList(length);
        for (int i2 = 0; i2 < length; i2++) {
            Element element2 = this.referencesEl[i2];
            Attr attributeNodeNS2 = element2.getAttributeNodeNS(null, Constants._ATT_ID);
            if (attributeNodeNS2 != null) {
                element2.setIdAttributeNode(attributeNodeNS2, true);
            }
            this.references.add(null);
        }
    }

    public void addDocument(String str, String str2, Transforms transforms, String str3, String str4, String str5) throws XMLSignatureException {
        Reference reference = new Reference(getDocument(), str, str2, this, transforms, str3);
        if (str4 != null) {
            reference.setId(str4);
        }
        if (str5 != null) {
            reference.setType(str5);
        }
        this.references.add(reference);
        appendSelf(reference);
        addReturnToSelf();
    }

    public void generateDigestValues() throws XMLSignatureException {
        for (int i2 = 0; i2 < getLength(); i2++) {
            this.references.get(i2).generateDigestValue();
        }
    }

    public int getLength() {
        return this.references.size();
    }

    public Reference item(int i2) throws XMLSecurityException {
        if (this.references.get(i2) == null) {
            this.references.set(i2, new Reference(this.referencesEl[i2], this.baseURI, this, this.secureValidation));
        }
        return this.references.get(i2);
    }

    public void setId(String str) {
        if (str != null) {
            setLocalIdAttribute(Constants._ATT_ID, str);
        }
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    public boolean verifyReferences() throws XMLSecurityException {
        return verifyReferences(false);
    }

    public boolean verifyReferences(boolean z2) throws XMLSecurityException {
        if (this.referencesEl == null) {
            this.referencesEl = XMLUtils.selectDsNodes(getFirstChild(), Constants._TAG_REFERENCE);
        }
        LOG.debug("verify {} References", Integer.valueOf(this.referencesEl.length));
        Logger logger = LOG;
        Object[] objArr = new Object[1];
        objArr[0] = z2 ? "" : Keywords.FUNC_NOT_STRING;
        logger.debug("I am {} requested to follow nested Manifests", objArr);
        if (this.referencesEl.length == 0) {
            throw new XMLSecurityException(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_EMPTY_STRING, new Object[]{"References are empty"});
        }
        if (this.secureValidation && this.referencesEl.length > referenceCount.intValue()) {
            throw new XMLSecurityException("signature.tooManyReferences", new Object[]{Integer.valueOf(this.referencesEl.length), referenceCount});
        }
        this.verificationResults = new ArrayList(this.referencesEl.length);
        boolean z3 = true;
        for (int i2 = 0; i2 < this.referencesEl.length; i2++) {
            Reference reference = new Reference(this.referencesEl[i2], this.baseURI, this, this.secureValidation);
            this.references.set(i2, reference);
            try {
                boolean zVerify = reference.verify();
                if (!zVerify) {
                    z3 = false;
                }
                LOG.debug("The Reference has Type {}", reference.getType());
                List<VerifiedReference> listEmptyList = Collections.emptyList();
                if (z3 && z2 && reference.typeIsReferenceToManifest()) {
                    LOG.debug("We have to follow a nested Manifest");
                    try {
                        try {
                            XMLSignatureInput xMLSignatureInputDereferenceURIandPerformTransforms = reference.dereferenceURIandPerformTransforms(null);
                            Manifest manifest = null;
                            for (Node node : xMLSignatureInputDereferenceURIandPerformTransforms.getNodeSet()) {
                                if (node.getNodeType() == 1 && ((Element) node).getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#") && ((Element) node).getLocalName().equals(Constants._TAG_MANIFEST)) {
                                    try {
                                        manifest = new Manifest((Element) node, xMLSignatureInputDereferenceURIandPerformTransforms.getSourceURI(), this.secureValidation);
                                        break;
                                    } catch (XMLSecurityException e2) {
                                        LOG.debug(e2.getMessage(), e2);
                                    }
                                }
                            }
                            if (manifest == null) {
                                throw new MissingResourceFailureException(reference, com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_EMPTY_STRING, new Object[]{"No Manifest found"});
                            }
                            manifest.perManifestResolvers = this.perManifestResolvers;
                            manifest.resolverProperties = this.resolverProperties;
                            if (!manifest.verifyReferences(z2)) {
                                z3 = false;
                                LOG.warn("The nested Manifest was invalid (bad)");
                            } else {
                                LOG.debug("The nested Manifest was valid (good)");
                            }
                            listEmptyList = manifest.getVerificationResults();
                        } catch (ParserConfigurationException e3) {
                            throw new ReferenceNotInitializedException(e3);
                        } catch (SAXException e4) {
                            throw new ReferenceNotInitializedException(e4);
                        }
                    } catch (IOException e5) {
                        throw new ReferenceNotInitializedException(e5);
                    }
                }
                this.verificationResults.add(new VerifiedReference(zVerify, reference.getURI(), listEmptyList));
            } catch (ReferenceNotInitializedException e6) {
                throw new MissingResourceFailureException(e6, reference, "signature.Verification.Reference.NoInput", new Object[]{reference.getURI()});
            }
        }
        return z3;
    }

    public boolean getVerificationResult(int i2) throws XMLSecurityException {
        if (i2 < 0 || i2 > getLength() - 1) {
            throw new XMLSecurityException(new IndexOutOfBoundsException(I18n.translate("signature.Verification.IndexOutOfBounds", new Object[]{Integer.toString(i2), Integer.toString(getLength())})));
        }
        if (this.verificationResults == null) {
            try {
                verifyReferences();
            } catch (Exception e2) {
                throw new XMLSecurityException(e2);
            }
        }
        return ((VerifiedReference) ((ArrayList) this.verificationResults).get(i2)).isValid();
    }

    public List<VerifiedReference> getVerificationResults() {
        if (this.verificationResults == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.verificationResults);
    }

    public void addResourceResolver(ResourceResolver resourceResolver) {
        if (resourceResolver == null) {
            return;
        }
        if (this.perManifestResolvers == null) {
            this.perManifestResolvers = new ArrayList();
        }
        this.perManifestResolvers.add(resourceResolver);
    }

    public void addResourceResolver(ResourceResolverSpi resourceResolverSpi) {
        if (resourceResolverSpi == null) {
            return;
        }
        if (this.perManifestResolvers == null) {
            this.perManifestResolvers = new ArrayList();
        }
        this.perManifestResolvers.add(new ResourceResolver(resourceResolverSpi));
    }

    public List<ResourceResolver> getPerManifestResolvers() {
        return this.perManifestResolvers;
    }

    public Map<String, String> getResolverProperties() {
        return this.resolverProperties;
    }

    public void setResolverProperty(String str, String str2) {
        if (this.resolverProperties == null) {
            this.resolverProperties = new HashMap(10);
        }
        this.resolverProperties.put(str, str2);
    }

    public String getResolverProperty(String str) {
        return this.resolverProperties.get(str);
    }

    public byte[] getSignedContentItem(int i2) throws XMLSignatureException {
        try {
            return getReferencedContentAfterTransformsItem(i2).getBytes();
        } catch (CanonicalizationException e2) {
            throw new XMLSignatureException(e2);
        } catch (InvalidCanonicalizerException e3) {
            throw new XMLSignatureException(e3);
        } catch (XMLSecurityException e4) {
            throw new XMLSignatureException(e4);
        } catch (IOException e5) {
            throw new XMLSignatureException(e5);
        }
    }

    public XMLSignatureInput getReferencedContentBeforeTransformsItem(int i2) throws XMLSecurityException {
        return item(i2).getContentsBeforeTransformation();
    }

    public XMLSignatureInput getReferencedContentAfterTransformsItem(int i2) throws XMLSecurityException {
        return item(i2).getContentsAfterTransformation();
    }

    public int getSignedContentLength() {
        return getLength();
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_MANIFEST;
    }

    public boolean isSecureValidation() {
        return this.secureValidation;
    }
}
