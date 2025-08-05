package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.NodeSetData;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dom.DOMURIReference;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.TransformService;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLValidateContext;
import org.jcp.xml.dsig.internal.DigesterOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMReference.class */
public final class DOMReference extends DOMStructure implements Reference, DOMURIReference {
    public static final int MAXIMUM_TRANSFORM_COUNT = 5;
    private static boolean useC14N11 = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.useC14N11"));
    })).booleanValue();
    private static final Logger LOG = LoggerFactory.getLogger(DOMReference.class);
    private final DigestMethod digestMethod;
    private final String id;
    private final List<Transform> transforms;
    private List<Transform> allTransforms;
    private final Data appliedTransformData;
    private Attr here;
    private final String uri;
    private final String type;
    private byte[] digestValue;
    private byte[] calcDigestValue;
    private Element refElem;
    private boolean digested;
    private boolean validated;
    private boolean validationStatus;
    private Data derefData;
    private InputStream dis;
    private MessageDigest md;
    private Provider provider;

    public DOMReference(String str, String str2, DigestMethod digestMethod, List<? extends Transform> list, String str3, Provider provider) {
        this(str, str2, digestMethod, null, null, list, str3, null, provider);
    }

    public DOMReference(String str, String str2, DigestMethod digestMethod, List<? extends Transform> list, Data data, List<? extends Transform> list2, String str3, Provider provider) {
        this(str, str2, digestMethod, list, data, list2, str3, null, provider);
    }

    public DOMReference(String str, String str2, DigestMethod digestMethod, List<? extends Transform> list, Data data, List<? extends Transform> list2, String str3, byte[] bArr, Provider provider) {
        this.digested = false;
        this.validated = false;
        if (digestMethod == null) {
            throw new NullPointerException("DigestMethod must be non-null");
        }
        if (list == null) {
            this.allTransforms = new ArrayList();
        } else {
            this.allTransforms = new ArrayList(list);
            int size = this.allTransforms.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (!(this.allTransforms.get(i2) instanceof Transform)) {
                    throw new ClassCastException("appliedTransforms[" + i2 + "] is not a valid type");
                }
            }
        }
        if (list2 == null) {
            this.transforms = Collections.emptyList();
        } else {
            this.transforms = new ArrayList(list2);
            int size2 = this.transforms.size();
            for (int i3 = 0; i3 < size2; i3++) {
                if (!(this.transforms.get(i3) instanceof Transform)) {
                    throw new ClassCastException("transforms[" + i3 + "] is not a valid type");
                }
            }
            this.allTransforms.addAll(this.transforms);
        }
        this.digestMethod = digestMethod;
        this.uri = str;
        if (str != null && !str.equals("")) {
            try {
                new URI(str);
            } catch (URISyntaxException e2) {
                throw new IllegalArgumentException(e2.getMessage());
            }
        }
        this.type = str2;
        this.id = str3;
        if (bArr != null) {
            this.digestValue = (byte[]) bArr.clone();
            this.digested = true;
        }
        this.appliedTransformData = data;
        this.provider = provider;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x00b2, code lost:
    
        throw new javax.xml.crypto.MarshalException("Invalid element name: " + r0 + ", expected Transform");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public DOMReference(org.w3c.dom.Element r8, javax.xml.crypto.XMLCryptoContext r9, java.security.Provider r10) throws javax.xml.crypto.MarshalException, org.w3c.dom.DOMException {
        /*
            Method dump skipped, instructions count: 561
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jcp.xml.dsig.internal.dom.DOMReference.<init>(org.w3c.dom.Element, javax.xml.crypto.XMLCryptoContext, java.security.Provider):void");
    }

    @Override // javax.xml.crypto.dsig.Reference
    public DigestMethod getDigestMethod() {
        return this.digestMethod;
    }

    @Override // javax.xml.crypto.dsig.Reference
    public String getId() {
        return this.id;
    }

    @Override // javax.xml.crypto.URIReference
    public String getURI() {
        return this.uri;
    }

    @Override // javax.xml.crypto.URIReference
    public String getType() {
        return this.type;
    }

    @Override // javax.xml.crypto.dsig.Reference
    public List<Transform> getTransforms() {
        return Collections.unmodifiableList(this.allTransforms);
    }

    @Override // javax.xml.crypto.dsig.Reference
    public byte[] getDigestValue() {
        if (this.digestValue == null) {
            return null;
        }
        return (byte[]) this.digestValue.clone();
    }

    @Override // javax.xml.crypto.dsig.Reference
    public byte[] getCalculatedDigestValue() {
        if (this.calcDigestValue == null) {
            return null;
        }
        return (byte[]) this.calcDigestValue.clone();
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        LOG.debug("Marshalling Reference");
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        this.refElem = DOMUtils.createElement(ownerDocument, Constants._TAG_REFERENCE, "http://www.w3.org/2000/09/xmldsig#", str);
        DOMUtils.setAttributeID(this.refElem, Constants._ATT_ID, this.id);
        DOMUtils.setAttribute(this.refElem, Constants._ATT_URI, this.uri);
        DOMUtils.setAttribute(this.refElem, Constants._ATT_TYPE, this.type);
        if (!this.allTransforms.isEmpty()) {
            Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_TRANSFORMS, "http://www.w3.org/2000/09/xmldsig#", str);
            this.refElem.appendChild(elementCreateElement);
            Iterator<Transform> it = this.allTransforms.iterator();
            while (it.hasNext()) {
                ((DOMStructure) ((Transform) it.next())).marshal(elementCreateElement, str, dOMCryptoContext);
            }
        }
        ((DOMDigestMethod) this.digestMethod).marshal(this.refElem, str, dOMCryptoContext);
        LOG.debug("Adding digestValueElem");
        Element elementCreateElement2 = DOMUtils.createElement(ownerDocument, Constants._TAG_DIGESTVALUE, "http://www.w3.org/2000/09/xmldsig#", str);
        if (this.digestValue != null) {
            elementCreateElement2.appendChild(ownerDocument.createTextNode(XMLUtils.encodeToString(this.digestValue)));
        }
        this.refElem.appendChild(elementCreateElement2);
        node.appendChild(this.refElem);
        this.here = this.refElem.getAttributeNodeNS(null, Constants._ATT_URI);
    }

    public void digest(XMLSignContext xMLSignContext) throws DOMException, XMLSignatureException {
        Data dataDereference;
        if (this.appliedTransformData == null) {
            dataDereference = dereference(xMLSignContext);
        } else {
            dataDereference = this.appliedTransformData;
        }
        this.digestValue = transform(dataDereference, xMLSignContext);
        String strEncodeToString = XMLUtils.encodeToString(this.digestValue);
        LOG.debug("Reference object uri = {}", this.uri);
        Element lastChildElement = DOMUtils.getLastChildElement(this.refElem);
        if (lastChildElement == null) {
            throw new XMLSignatureException("DigestValue element expected");
        }
        DOMUtils.removeAllChildren(lastChildElement);
        lastChildElement.appendChild(this.refElem.getOwnerDocument().createTextNode(strEncodeToString));
        this.digested = true;
        LOG.debug("Reference digesting completed");
    }

    @Override // javax.xml.crypto.dsig.Reference
    public boolean validate(XMLValidateContext xMLValidateContext) throws XMLSignatureException {
        if (xMLValidateContext == null) {
            throw new NullPointerException("validateContext cannot be null");
        }
        if (this.validated) {
            return this.validationStatus;
        }
        this.calcDigestValue = transform(dereference(xMLValidateContext), xMLValidateContext);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Expected digest: " + XMLUtils.encodeToString(this.digestValue));
            LOG.debug("Actual digest: " + XMLUtils.encodeToString(this.calcDigestValue));
        }
        this.validationStatus = Arrays.equals(this.digestValue, this.calcDigestValue);
        this.validated = true;
        return this.validationStatus;
    }

    @Override // javax.xml.crypto.dsig.Reference
    public Data getDereferencedData() {
        return this.derefData;
    }

    @Override // javax.xml.crypto.dsig.Reference
    public InputStream getDigestInputStream() {
        return this.dis;
    }

    private Data dereference(XMLCryptoContext xMLCryptoContext) throws XMLSignatureException {
        URIDereferencer uRIDereferencer = xMLCryptoContext.getURIDereferencer();
        if (uRIDereferencer == null) {
            uRIDereferencer = DOMURIDereferencer.INSTANCE;
        }
        try {
            Data dataDereference = uRIDereferencer.dereference(this, xMLCryptoContext);
            LOG.debug("URIDereferencer class name: {}", uRIDereferencer.getClass().getName());
            LOG.debug("Data class name: {}", dataDereference.getClass().getName());
            return dataDereference;
        } catch (URIReferenceException e2) {
            throw new XMLSignatureException(e2);
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r11v5 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r12v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 11, insn: 0x030e: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:115:0x030e */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x0313: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:117:0x0313 */
    /* JADX WARN: Type inference failed for: r11v5, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.lang.Throwable] */
    private byte[] transform(Data data, XMLCryptoContext xMLCryptoContext) throws XMLSignatureException {
        DigesterOutputStream digesterOutputStream;
        ?? r11;
        ?? r12;
        TransformService transformService;
        XMLSignatureInput xMLSignatureInput;
        TransformService transformService2;
        Element firstChildElement;
        if (this.md == null) {
            try {
                this.md = MessageDigest.getInstance(((DOMDigestMethod) this.digestMethod).getMessageDigestAlgorithm());
            } catch (NoSuchAlgorithmException e2) {
                throw new XMLSignatureException(e2);
            }
        }
        this.md.reset();
        Boolean bool = (Boolean) xMLCryptoContext.getProperty("javax.xml.crypto.dsig.cacheReference");
        if (bool != null && bool.booleanValue()) {
            this.derefData = copyDerefData(data);
            digesterOutputStream = new DigesterOutputStream(this.md, true);
        } else {
            digesterOutputStream = new DigesterOutputStream(this.md);
        }
        Data dataTransform = data;
        try {
            try {
                try {
                    try {
                        try {
                            UnsyncBufferedOutputStream unsyncBufferedOutputStream = new UnsyncBufferedOutputStream(digesterOutputStream);
                            Throwable th = null;
                            int size = this.transforms.size();
                            for (int i2 = 0; i2 < size; i2++) {
                                DOMTransform dOMTransform = (DOMTransform) this.transforms.get(i2);
                                if (i2 < size - 1) {
                                    dataTransform = dOMTransform.transform(dataTransform, xMLCryptoContext);
                                } else {
                                    dataTransform = dOMTransform.transform(dataTransform, xMLCryptoContext, unsyncBufferedOutputStream);
                                }
                            }
                            if (dataTransform != null) {
                                boolean z2 = useC14N11;
                                String str = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
                                if (xMLCryptoContext instanceof XMLSignContext) {
                                    if (!z2) {
                                        Boolean bool2 = (Boolean) xMLCryptoContext.getProperty("com.sun.org.apache.xml.internal.security.useC14N11");
                                        z2 = bool2 != null && bool2.booleanValue();
                                        if (z2) {
                                            str = "http://www.w3.org/2006/12/xml-c14n11";
                                        }
                                    } else {
                                        str = "http://www.w3.org/2006/12/xml-c14n11";
                                    }
                                }
                                if (dataTransform instanceof ApacheData) {
                                    xMLSignatureInput = ((ApacheData) dataTransform).getXMLSignatureInput();
                                } else if (dataTransform instanceof OctetStreamData) {
                                    xMLSignatureInput = new XMLSignatureInput(((OctetStreamData) dataTransform).getOctetStream());
                                } else if (dataTransform instanceof NodeSetData) {
                                    if (this.provider == null) {
                                        transformService = TransformService.getInstance(str, "DOM");
                                    } else {
                                        try {
                                            transformService = TransformService.getInstance(str, "DOM", this.provider);
                                        } catch (NoSuchAlgorithmException e3) {
                                            transformService = TransformService.getInstance(str, "DOM");
                                        }
                                    }
                                    xMLSignatureInput = new XMLSignatureInput(((OctetStreamData) transformService.transform(dataTransform, xMLCryptoContext)).getOctetStream());
                                } else {
                                    throw new XMLSignatureException("unrecognized Data type");
                                }
                                try {
                                    xMLSignatureInput.setSecureValidation(Utils.secureValidation(xMLCryptoContext));
                                    if ((xMLCryptoContext instanceof XMLSignContext) && z2 && !xMLSignatureInput.isOctetStream() && !xMLSignatureInput.isOutputStreamSet()) {
                                        if (this.provider == null) {
                                            transformService2 = TransformService.getInstance(str, "DOM");
                                        } else {
                                            try {
                                                transformService2 = TransformService.getInstance(str, "DOM", this.provider);
                                            } catch (NoSuchAlgorithmException e4) {
                                                transformService2 = TransformService.getInstance(str, "DOM");
                                            }
                                        }
                                        DOMTransform dOMTransform2 = new DOMTransform(transformService2);
                                        String signaturePrefix = DOMUtils.getSignaturePrefix(xMLCryptoContext);
                                        if (this.allTransforms.isEmpty()) {
                                            firstChildElement = DOMUtils.createElement(this.refElem.getOwnerDocument(), Constants._TAG_TRANSFORMS, "http://www.w3.org/2000/09/xmldsig#", signaturePrefix);
                                            this.refElem.insertBefore(firstChildElement, DOMUtils.getFirstChildElement(this.refElem));
                                        } else {
                                            firstChildElement = DOMUtils.getFirstChildElement(this.refElem);
                                        }
                                        dOMTransform2.marshal(firstChildElement, signaturePrefix, (DOMCryptoContext) xMLCryptoContext);
                                        this.allTransforms.add(dOMTransform2);
                                        xMLSignatureInput.updateOutputStream(unsyncBufferedOutputStream, true);
                                    } else {
                                        xMLSignatureInput.updateOutputStream(unsyncBufferedOutputStream);
                                    }
                                    if (xMLSignatureInput.getOctetStreamReal() != null) {
                                        xMLSignatureInput.getOctetStreamReal().close();
                                    }
                                } catch (Throwable th2) {
                                    if (xMLSignatureInput.getOctetStreamReal() != null) {
                                        xMLSignatureInput.getOctetStreamReal().close();
                                    }
                                    throw th2;
                                }
                            }
                            unsyncBufferedOutputStream.flush();
                            if (bool != null && bool.booleanValue()) {
                                this.dis = digesterOutputStream.getInputStream();
                            }
                            byte[] digestValue = digesterOutputStream.getDigestValue();
                            if (unsyncBufferedOutputStream != null) {
                                if (0 != 0) {
                                    try {
                                        unsyncBufferedOutputStream.close();
                                    } catch (Throwable th3) {
                                        th.addSuppressed(th3);
                                    }
                                } else {
                                    unsyncBufferedOutputStream.close();
                                }
                            }
                            if (digesterOutputStream != null) {
                                try {
                                    digesterOutputStream.close();
                                } catch (IOException e5) {
                                    throw new XMLSignatureException(e5);
                                }
                            }
                            return digestValue;
                        } catch (NoSuchAlgorithmException e6) {
                            throw new XMLSignatureException(e6);
                        } catch (MarshalException e7) {
                            throw new XMLSignatureException(e7);
                        }
                    } catch (CanonicalizationException e8) {
                        throw new XMLSignatureException(e8);
                    } catch (IOException e9) {
                        throw new XMLSignatureException(e9);
                    }
                } catch (TransformException e10) {
                    throw new XMLSignatureException(e10);
                }
            } catch (Throwable th4) {
                if (r11 != 0) {
                    if (r12 != 0) {
                        try {
                            r11.close();
                        } catch (Throwable th5) {
                            r12.addSuppressed(th5);
                        }
                    } else {
                        r11.close();
                    }
                }
                throw th4;
            }
        } catch (Throwable th6) {
            if (digesterOutputStream != null) {
                try {
                    digesterOutputStream.close();
                } catch (IOException e11) {
                    throw new XMLSignatureException(e11);
                }
            }
            throw th6;
        }
    }

    @Override // javax.xml.crypto.dom.DOMURIReference
    public Node getHere() {
        return this.here;
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        boolean zEquals2;
        boolean zEquals3;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Reference)) {
            return false;
        }
        Reference reference = (Reference) obj;
        if (this.id == null) {
            zEquals = reference.getId() == null;
        } else {
            zEquals = this.id.equals(reference.getId());
        }
        boolean z2 = zEquals;
        if (this.uri == null) {
            zEquals2 = reference.getURI() == null;
        } else {
            zEquals2 = this.uri.equals(reference.getURI());
        }
        boolean z3 = zEquals2;
        if (this.type == null) {
            zEquals3 = reference.getType() == null;
        } else {
            zEquals3 = this.type.equals(reference.getType());
        }
        return this.digestMethod.equals(reference.getDigestMethod()) && z2 && z3 && zEquals3 && this.allTransforms.equals(reference.getTransforms()) && Arrays.equals(this.digestValue, reference.getDigestValue());
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        if (this.uri != null) {
            iHashCode = (31 * iHashCode) + this.uri.hashCode();
        }
        if (this.type != null) {
            iHashCode = (31 * iHashCode) + this.type.hashCode();
        }
        if (this.digestValue != null) {
            iHashCode = (31 * iHashCode) + Arrays.hashCode(this.digestValue);
        }
        return (31 * ((31 * iHashCode) + this.digestMethod.hashCode())) + this.allTransforms.hashCode();
    }

    boolean isDigested() {
        return this.digested;
    }

    private static Data copyDerefData(Data data) {
        if (data instanceof ApacheData) {
            XMLSignatureInput xMLSignatureInput = ((ApacheData) data).getXMLSignatureInput();
            if (xMLSignatureInput.isNodeSet()) {
                try {
                    final Set<Node> nodeSet = xMLSignatureInput.getNodeSet();
                    return new NodeSetData() { // from class: org.jcp.xml.dsig.internal.dom.DOMReference.1
                        @Override // javax.xml.crypto.NodeSetData
                        public Iterator<Node> iterator() {
                            return nodeSet.iterator();
                        }
                    };
                } catch (Exception e2) {
                    LOG.warn("cannot cache dereferenced data: " + ((Object) e2));
                    return null;
                }
            }
            if (xMLSignatureInput.isElement()) {
                return new DOMSubTreeData(xMLSignatureInput.getSubNode(), xMLSignatureInput.isExcludeComments());
            }
            if (xMLSignatureInput.isOctetStream() || xMLSignatureInput.isByteArray()) {
                try {
                    return new OctetStreamData(xMLSignatureInput.getOctetStream(), xMLSignatureInput.getSourceURI(), xMLSignatureInput.getMIMEType());
                } catch (IOException e3) {
                    LOG.warn("cannot cache dereferenced data: " + ((Object) e3));
                    return null;
                }
            }
        }
        return data;
    }
}
