package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.algorithms.Algorithm;
import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceData;
import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceNodeSetData;
import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceOctetStreamData;
import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceSubTreeData;
import com.sun.org.apache.xml.internal.security.transforms.InvalidTransformException;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.DigesterOutputStream;
import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/Reference.class */
public class Reference extends SignatureElementProxy {
    public static final String OBJECT_URI = "http://www.w3.org/2000/09/xmldsig#Object";
    public static final String MANIFEST_URI = "http://www.w3.org/2000/09/xmldsig#Manifest";
    public static final int MAXIMUM_TRANSFORM_COUNT = 5;
    private boolean secureValidation;
    private static boolean useC14N11 = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.useC14N11"));
    })).booleanValue();
    private static final Logger LOG = LoggerFactory.getLogger(Reference.class);
    private Manifest manifest;
    private XMLSignatureInput transformsOutput;
    private Transforms transforms;
    private Element digestMethodElem;
    private Element digestValueElement;
    private ReferenceData referenceData;

    protected Reference(Document document, String str, String str2, Manifest manifest, Transforms transforms, String str3) throws XMLSignatureException {
        super(document);
        addReturnToSelf();
        this.baseURI = str;
        this.manifest = manifest;
        setURI(str2);
        if (transforms != null) {
            this.transforms = transforms;
            appendSelf(transforms);
            addReturnToSelf();
        }
        this.digestMethodElem = new Algorithm(getDocument(), str3) { // from class: com.sun.org.apache.xml.internal.security.signature.Reference.1
            @Override // com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy, com.sun.org.apache.xml.internal.security.utils.ElementProxy
            public String getBaseNamespace() {
                return "http://www.w3.org/2000/09/xmldsig#";
            }

            @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
            public String getBaseLocalName() {
                return Constants._TAG_DIGESTMETHOD;
            }
        }.getElement();
        appendSelf(this.digestMethodElem);
        addReturnToSelf();
        this.digestValueElement = XMLUtils.createElementInSignatureSpace(getDocument(), Constants._TAG_DIGESTVALUE);
        appendSelf(this.digestValueElement);
        addReturnToSelf();
    }

    protected Reference(Element element, String str, Manifest manifest) throws XMLSecurityException {
        this(element, str, manifest, true);
    }

    protected Reference(Element element, String str, Manifest manifest, boolean z2) throws XMLSecurityException {
        super(element, str);
        this.secureValidation = z2;
        this.baseURI = str;
        Element nextElement = XMLUtils.getNextElement(element.getFirstChild());
        if (nextElement != null && Constants._TAG_TRANSFORMS.equals(nextElement.getLocalName()) && "http://www.w3.org/2000/09/xmldsig#".equals(nextElement.getNamespaceURI())) {
            this.transforms = new Transforms(nextElement, this.baseURI);
            this.transforms.setSecureValidation(z2);
            if (z2 && this.transforms.getLength() > 5) {
                throw new XMLSecurityException("signature.tooManyTransforms", new Object[]{Integer.valueOf(this.transforms.getLength()), 5});
            }
            nextElement = XMLUtils.getNextElement(nextElement.getNextSibling());
        }
        this.digestMethodElem = nextElement;
        if (this.digestMethodElem == null) {
            throw new XMLSecurityException("signature.Reference.NoDigestMethod");
        }
        this.digestValueElement = XMLUtils.getNextElement(this.digestMethodElem.getNextSibling());
        if (this.digestValueElement == null) {
            throw new XMLSecurityException("signature.Reference.NoDigestValue");
        }
        this.manifest = manifest;
    }

    public MessageDigestAlgorithm getMessageDigestAlgorithm() throws DOMException, XMLSignatureException {
        if (this.digestMethodElem == null) {
            return null;
        }
        String attributeNS = this.digestMethodElem.getAttributeNS(null, Constants._ATT_ALGORITHM);
        if ("".equals(attributeNS)) {
            return null;
        }
        if (this.secureValidation && MessageDigestAlgorithm.ALGO_ID_DIGEST_NOT_RECOMMENDED_MD5.equals(attributeNS)) {
            throw new XMLSignatureException("signature.signatureAlgorithm", new Object[]{attributeNS});
        }
        return MessageDigestAlgorithm.getInstance(getDocument(), attributeNS);
    }

    public void setURI(String str) {
        if (str != null) {
            setLocalAttribute(Constants._ATT_URI, str);
        }
    }

    public String getURI() {
        return getLocalAttribute(Constants._ATT_URI);
    }

    public void setId(String str) {
        if (str != null) {
            setLocalIdAttribute(Constants._ATT_ID, str);
        }
    }

    public String getId() {
        return getLocalAttribute(Constants._ATT_ID);
    }

    public void setType(String str) {
        if (str != null) {
            setLocalAttribute(Constants._ATT_TYPE, str);
        }
    }

    public String getType() {
        return getLocalAttribute(Constants._ATT_TYPE);
    }

    public boolean typeIsReferenceToObject() {
        if ("http://www.w3.org/2000/09/xmldsig#Object".equals(getType())) {
            return true;
        }
        return false;
    }

    public boolean typeIsReferenceToManifest() {
        if ("http://www.w3.org/2000/09/xmldsig#Manifest".equals(getType())) {
            return true;
        }
        return false;
    }

    private void setDigestValueElement(byte[] bArr) {
        Node firstChild = this.digestValueElement.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                this.digestValueElement.removeChild(node);
                firstChild = node.getNextSibling();
            } else {
                this.digestValueElement.appendChild(createText(XMLUtils.encodeToString(bArr)));
                return;
            }
        }
    }

    public void generateDigestValue() throws XMLSignatureException {
        setDigestValueElement(calculateDigest(false));
    }

    public XMLSignatureInput getContentsBeforeTransformation() throws DOMException, ReferenceNotInitializedException {
        try {
            Attr attributeNodeNS = getElement().getAttributeNodeNS(null, Constants._ATT_URI);
            ResourceResolver resourceResolver = ResourceResolver.getInstance(attributeNodeNS, this.baseURI, this.manifest.getPerManifestResolvers(), this.secureValidation);
            resourceResolver.addProperties(this.manifest.getResolverProperties());
            return resourceResolver.resolve(attributeNodeNS, this.baseURI, this.secureValidation);
        } catch (ResourceResolverException e2) {
            throw new ReferenceNotInitializedException(e2);
        }
    }

    private XMLSignatureInput getContentsAfterTransformation(XMLSignatureInput xMLSignatureInput, OutputStream outputStream) throws XMLSignatureException {
        XMLSignatureInput xMLSignatureInputPerformTransforms;
        try {
            Transforms transforms = getTransforms();
            if (transforms != null) {
                xMLSignatureInputPerformTransforms = transforms.performTransforms(xMLSignatureInput, outputStream);
                this.transformsOutput = xMLSignatureInputPerformTransforms;
            } else {
                xMLSignatureInputPerformTransforms = xMLSignatureInput;
            }
            return xMLSignatureInputPerformTransforms;
        } catch (CanonicalizationException e2) {
            throw new XMLSignatureException(e2);
        } catch (InvalidCanonicalizerException e3) {
            throw new XMLSignatureException(e3);
        } catch (TransformationException e4) {
            throw new XMLSignatureException(e4);
        } catch (ResourceResolverException e5) {
            throw new XMLSignatureException(e5);
        } catch (XMLSecurityException e6) {
            throw new XMLSignatureException(e6);
        }
    }

    public XMLSignatureInput getContentsAfterTransformation() throws DOMException, XMLSignatureException {
        XMLSignatureInput contentsBeforeTransformation = getContentsBeforeTransformation();
        cacheDereferencedElement(contentsBeforeTransformation);
        return getContentsAfterTransformation(contentsBeforeTransformation, null);
    }

    public XMLSignatureInput getNodesetBeforeFirstCanonicalization() throws DOMException, XMLSignatureException {
        try {
            XMLSignatureInput contentsBeforeTransformation = getContentsBeforeTransformation();
            cacheDereferencedElement(contentsBeforeTransformation);
            XMLSignatureInput xMLSignatureInputPerformTransform = contentsBeforeTransformation;
            Transforms transforms = getTransforms();
            if (transforms != null) {
                for (int i2 = 0; i2 < transforms.getLength(); i2++) {
                    Transform transformItem = transforms.item(i2);
                    String uri = transformItem.getURI();
                    if (uri.equals("http://www.w3.org/2001/10/xml-exc-c14n#") || uri.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments") || uri.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315") || uri.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments") || uri.equals("http://www.w3.org/2006/12/xml-c14n11") || uri.equals("http://www.w3.org/2006/12/xml-c14n11#WithComments")) {
                        break;
                    }
                    xMLSignatureInputPerformTransform = transformItem.performTransform(xMLSignatureInputPerformTransform, null);
                }
                xMLSignatureInputPerformTransform.setSourceURI(contentsBeforeTransformation.getSourceURI());
            }
            return xMLSignatureInputPerformTransform;
        } catch (CanonicalizationException e2) {
            throw new XMLSignatureException(e2);
        } catch (InvalidCanonicalizerException e3) {
            throw new XMLSignatureException(e3);
        } catch (TransformationException e4) {
            throw new XMLSignatureException(e4);
        } catch (ResourceResolverException e5) {
            throw new XMLSignatureException(e5);
        } catch (XMLSecurityException e6) {
            throw new XMLSignatureException(e6);
        } catch (IOException e7) {
            throw new XMLSignatureException(e7);
        }
    }

    public String getHTMLRepresentation() throws DOMException, XMLSignatureException {
        try {
            XMLSignatureInput nodesetBeforeFirstCanonicalization = getNodesetBeforeFirstCanonicalization();
            Transforms transforms = getTransforms();
            Transform transform = null;
            if (transforms != null) {
                for (int i2 = 0; i2 < transforms.getLength(); i2++) {
                    Transform transformItem = transforms.item(i2);
                    String uri = transformItem.getURI();
                    if (uri.equals("http://www.w3.org/2001/10/xml-exc-c14n#") || uri.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")) {
                        transform = transformItem;
                        break;
                    }
                }
            }
            Set<String> hashSet = new HashSet();
            if (transform != null && transform.length("http://www.w3.org/2001/10/xml-exc-c14n#", InclusiveNamespaces._TAG_EC_INCLUSIVENAMESPACES) == 1) {
                hashSet = InclusiveNamespaces.prefixStr2Set(new InclusiveNamespaces(XMLUtils.selectNode(transform.getElement().getFirstChild(), "http://www.w3.org/2001/10/xml-exc-c14n#", InclusiveNamespaces._TAG_EC_INCLUSIVENAMESPACES, 0), getBaseURI()).getInclusiveNamespaces());
            }
            return nodesetBeforeFirstCanonicalization.getHTMLRepresentation(hashSet);
        } catch (InvalidTransformException e2) {
            throw new XMLSignatureException(e2);
        } catch (TransformationException e3) {
            throw new XMLSignatureException(e3);
        } catch (XMLSecurityException e4) {
            throw new XMLSignatureException(e4);
        }
    }

    public XMLSignatureInput getTransformsOutput() {
        return this.transformsOutput;
    }

    public ReferenceData getReferenceData() {
        return this.referenceData;
    }

    protected XMLSignatureInput dereferenceURIandPerformTransforms(OutputStream outputStream) throws XMLSignatureException {
        try {
            XMLSignatureInput contentsBeforeTransformation = getContentsBeforeTransformation();
            cacheDereferencedElement(contentsBeforeTransformation);
            XMLSignatureInput contentsAfterTransformation = getContentsAfterTransformation(contentsBeforeTransformation, outputStream);
            this.transformsOutput = contentsAfterTransformation;
            return contentsAfterTransformation;
        } catch (XMLSecurityException e2) {
            throw new ReferenceNotInitializedException(e2);
        }
    }

    private void cacheDereferencedElement(XMLSignatureInput xMLSignatureInput) {
        if (xMLSignatureInput.isNodeSet()) {
            try {
                final Set<Node> nodeSet = xMLSignatureInput.getNodeSet();
                this.referenceData = new ReferenceNodeSetData() { // from class: com.sun.org.apache.xml.internal.security.signature.Reference.2
                    @Override // com.sun.org.apache.xml.internal.security.signature.reference.ReferenceNodeSetData
                    public Iterator<Node> iterator() {
                        return new Iterator<Node>() { // from class: com.sun.org.apache.xml.internal.security.signature.Reference.2.1
                            Iterator<Node> sIterator;

                            {
                                this.sIterator = nodeSet.iterator();
                            }

                            @Override // java.util.Iterator
                            public boolean hasNext() {
                                return this.sIterator.hasNext();
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.Iterator
                            public Node next() {
                                return this.sIterator.next();
                            }

                            @Override // java.util.Iterator
                            public void remove() {
                                throw new UnsupportedOperationException();
                            }
                        };
                    }
                };
                return;
            } catch (Exception e2) {
                LOG.warn("cannot cache dereferenced data: " + ((Object) e2));
                return;
            }
        }
        if (xMLSignatureInput.isElement()) {
            this.referenceData = new ReferenceSubTreeData(xMLSignatureInput.getSubNode(), xMLSignatureInput.isExcludeComments());
            return;
        }
        if (xMLSignatureInput.isOctetStream() || xMLSignatureInput.isByteArray()) {
            try {
                this.referenceData = new ReferenceOctetStreamData(xMLSignatureInput.getOctetStream(), xMLSignatureInput.getSourceURI(), xMLSignatureInput.getMIMEType());
            } catch (IOException e3) {
                LOG.warn("cannot cache dereferenced data: " + ((Object) e3));
            }
        }
    }

    public Transforms getTransforms() throws XMLSecurityException {
        return this.transforms;
    }

    public byte[] getReferencedBytes() throws XMLSignatureException {
        try {
            return dereferenceURIandPerformTransforms(null).getBytes();
        } catch (CanonicalizationException e2) {
            throw new ReferenceNotInitializedException(e2);
        } catch (IOException e3) {
            throw new ReferenceNotInitializedException(e3);
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r10v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r9v2 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 10, insn: 0x015b: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:63:0x015b */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x0156: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:61:0x0156 */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r9v2, types: [com.sun.org.apache.xml.internal.security.utils.DigesterOutputStream] */
    private byte[] calculateDigest(boolean z2) throws DOMException, XMLSignatureException {
        ?? r9;
        ?? r10;
        XMLSignatureInput contentsBeforeTransformation = getContentsBeforeTransformation();
        if (contentsBeforeTransformation.isPreCalculatedDigest()) {
            return getPreCalculatedDigest(contentsBeforeTransformation);
        }
        cacheDereferencedElement(contentsBeforeTransformation);
        MessageDigestAlgorithm messageDigestAlgorithm = getMessageDigestAlgorithm();
        messageDigestAlgorithm.reset();
        try {
            try {
                DigesterOutputStream digesterOutputStream = new DigesterOutputStream(messageDigestAlgorithm);
                Throwable th = null;
                UnsyncBufferedOutputStream unsyncBufferedOutputStream = new UnsyncBufferedOutputStream(digesterOutputStream);
                Throwable th2 = null;
                try {
                    try {
                        XMLSignatureInput contentsAfterTransformation = getContentsAfterTransformation(contentsBeforeTransformation, unsyncBufferedOutputStream);
                        this.transformsOutput = contentsAfterTransformation;
                        if (!useC14N11 || z2 || contentsAfterTransformation.isOutputStreamSet() || contentsAfterTransformation.isOctetStream()) {
                            contentsAfterTransformation.updateOutputStream(unsyncBufferedOutputStream);
                        } else {
                            if (this.transforms == null) {
                                this.transforms = new Transforms(getDocument());
                                this.transforms.setSecureValidation(this.secureValidation);
                                getElement().insertBefore(this.transforms.getElement(), this.digestMethodElem);
                            }
                            this.transforms.addTransform("http://www.w3.org/2006/12/xml-c14n11");
                            contentsAfterTransformation.updateOutputStream(unsyncBufferedOutputStream, true);
                        }
                        unsyncBufferedOutputStream.flush();
                        if (contentsAfterTransformation.getOctetStreamReal() != null) {
                            contentsAfterTransformation.getOctetStreamReal().close();
                        }
                        byte[] digestValue = digesterOutputStream.getDigestValue();
                        if (unsyncBufferedOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    unsyncBufferedOutputStream.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                unsyncBufferedOutputStream.close();
                            }
                        }
                        if (digesterOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    digesterOutputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                digesterOutputStream.close();
                            }
                        }
                        return digestValue;
                    } catch (Throwable th5) {
                        if (unsyncBufferedOutputStream != null) {
                            if (th2 != null) {
                                try {
                                    unsyncBufferedOutputStream.close();
                                } catch (Throwable th6) {
                                    th2.addSuppressed(th6);
                                }
                            } else {
                                unsyncBufferedOutputStream.close();
                            }
                        }
                        throw th5;
                    }
                } finally {
                }
            } catch (XMLSecurityException e2) {
                throw new ReferenceNotInitializedException(e2);
            } catch (IOException e3) {
                throw new ReferenceNotInitializedException(e3);
            }
        } catch (Throwable th7) {
            if (r9 != 0) {
                if (r10 != 0) {
                    try {
                        r9.close();
                    } catch (Throwable th8) {
                        r10.addSuppressed(th8);
                    }
                } else {
                    r9.close();
                }
            }
            throw th7;
        }
    }

    private byte[] getPreCalculatedDigest(XMLSignatureInput xMLSignatureInput) throws ReferenceNotInitializedException {
        LOG.debug("Verifying element with pre-calculated digest");
        return XMLUtils.decode(xMLSignatureInput.getPreCalculatedDigest());
    }

    public byte[] getDigestValue() throws XMLSecurityException {
        if (this.digestValueElement == null) {
            throw new XMLSecurityException("signature.Verification.NoSignatureElement", new Object[]{Constants._TAG_DIGESTVALUE, "http://www.w3.org/2000/09/xmldsig#"});
        }
        return XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(this.digestValueElement));
    }

    public boolean verify() throws XMLSecurityException {
        byte[] digestValue = getDigestValue();
        byte[] bArrCalculateDigest = calculateDigest(true);
        boolean zIsEqual = MessageDigestAlgorithm.isEqual(digestValue, bArrCalculateDigest);
        if (!zIsEqual) {
            LOG.warn("Verification failed for URI \"" + getURI() + PdfOps.DOUBLE_QUOTE__TOKEN);
            LOG.warn("Expected Digest: " + XMLUtils.encodeToString(digestValue));
            LOG.warn("Actual Digest: " + XMLUtils.encodeToString(bArrCalculateDigest));
        } else {
            LOG.debug("Verification successful for URI \"{}\"", getURI());
        }
        return zIsEqual;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.ElementProxy
    public String getBaseLocalName() {
        return Constants._TAG_REFERENCE;
    }
}
