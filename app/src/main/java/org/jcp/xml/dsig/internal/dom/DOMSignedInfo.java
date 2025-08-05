package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.XMLSignatureException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMSignedInfo.class */
public final class DOMSignedInfo extends DOMStructure implements SignedInfo {
    private static final Logger LOG = LoggerFactory.getLogger(DOMSignedInfo.class);
    private List<Reference> references;
    private CanonicalizationMethod canonicalizationMethod;
    private SignatureMethod signatureMethod;
    private String id;
    private Document ownerDoc;
    private Element localSiElem;
    private InputStream canonData;

    public DOMSignedInfo(CanonicalizationMethod canonicalizationMethod, SignatureMethod signatureMethod, List<? extends Reference> list) {
        if (canonicalizationMethod == null || signatureMethod == null || list == null) {
            throw new NullPointerException();
        }
        this.canonicalizationMethod = canonicalizationMethod;
        this.signatureMethod = signatureMethod;
        this.references = Collections.unmodifiableList(new ArrayList(list));
        if (this.references.isEmpty()) {
            throw new IllegalArgumentException("list of references must contain at least one entry");
        }
        int size = this.references.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!(this.references.get(i2) instanceof Reference)) {
                throw new ClassCastException("list of references contains an illegal type");
            }
        }
    }

    public DOMSignedInfo(CanonicalizationMethod canonicalizationMethod, SignatureMethod signatureMethod, List<? extends Reference> list, String str) {
        this(canonicalizationMethod, signatureMethod, list);
        this.id = str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x010b, code lost:
    
        throw new javax.xml.crypto.MarshalException("Invalid element name: " + r0 + jdk.internal.dynalink.CallSiteDescriptor.TOKEN_DELIMITER + r0 + ", expected Reference");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public DOMSignedInfo(org.w3c.dom.Element r8, javax.xml.crypto.XMLCryptoContext r9, java.security.Provider r10) throws javax.xml.crypto.MarshalException {
        /*
            Method dump skipped, instructions count: 359
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jcp.xml.dsig.internal.dom.DOMSignedInfo.<init>(org.w3c.dom.Element, javax.xml.crypto.XMLCryptoContext, java.security.Provider):void");
    }

    @Override // javax.xml.crypto.dsig.SignedInfo
    public CanonicalizationMethod getCanonicalizationMethod() {
        return this.canonicalizationMethod;
    }

    @Override // javax.xml.crypto.dsig.SignedInfo
    public SignatureMethod getSignatureMethod() {
        return this.signatureMethod;
    }

    @Override // javax.xml.crypto.dsig.SignedInfo
    public String getId() {
        return this.id;
    }

    @Override // javax.xml.crypto.dsig.SignedInfo
    public List<Reference> getReferences() {
        return this.references;
    }

    @Override // javax.xml.crypto.dsig.SignedInfo
    public InputStream getCanonicalizedData() {
        return this.canonData;
    }

    public void canonicalize(XMLCryptoContext xMLCryptoContext, ByteArrayOutputStream byteArrayOutputStream) throws XMLSignatureException {
        if (xMLCryptoContext == null) {
            throw new NullPointerException("context cannot be null");
        }
        DOMSubTreeData dOMSubTreeData = new DOMSubTreeData(this.localSiElem, true);
        try {
            UnsyncBufferedOutputStream unsyncBufferedOutputStream = new UnsyncBufferedOutputStream(byteArrayOutputStream);
            Throwable th = null;
            try {
                ((DOMCanonicalizationMethod) this.canonicalizationMethod).canonicalize(dOMSubTreeData, xMLCryptoContext, unsyncBufferedOutputStream);
                unsyncBufferedOutputStream.flush();
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Canonicalized SignedInfo:");
                    StringBuilder sb = new StringBuilder(byteArray.length);
                    for (byte b2 : byteArray) {
                        sb.append((char) b2);
                    }
                    LOG.debug(sb.toString());
                    LOG.debug("Data to be signed/verified:" + XMLUtils.encodeToString(byteArray));
                }
                this.canonData = new ByteArrayInputStream(byteArray);
                if (unsyncBufferedOutputStream != null) {
                    if (0 != 0) {
                        try {
                            unsyncBufferedOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        unsyncBufferedOutputStream.close();
                    }
                }
            } catch (Throwable th3) {
                if (unsyncBufferedOutputStream != null) {
                    if (0 != 0) {
                        try {
                            unsyncBufferedOutputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        unsyncBufferedOutputStream.close();
                    }
                }
                throw th3;
            }
        } catch (IOException e2) {
            LOG.debug(e2.getMessage(), e2);
        } catch (TransformException e3) {
            throw new XMLSignatureException(e3);
        }
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        this.ownerDoc = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = DOMUtils.createElement(this.ownerDoc, Constants._TAG_SIGNEDINFO, "http://www.w3.org/2000/09/xmldsig#", str);
        ((DOMCanonicalizationMethod) this.canonicalizationMethod).marshal(elementCreateElement, str, dOMCryptoContext);
        ((DOMStructure) this.signatureMethod).marshal(elementCreateElement, str, dOMCryptoContext);
        Iterator<Reference> it = this.references.iterator();
        while (it.hasNext()) {
            ((DOMReference) it.next()).marshal(elementCreateElement, str, dOMCryptoContext);
        }
        DOMUtils.setAttributeID(elementCreateElement, Constants._ATT_ID, this.id);
        node.appendChild(elementCreateElement);
        this.localSiElem = elementCreateElement;
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SignedInfo)) {
            return false;
        }
        SignedInfo signedInfo = (SignedInfo) obj;
        if (this.id == null) {
            zEquals = signedInfo.getId() == null;
        } else {
            zEquals = this.id.equals(signedInfo.getId());
        }
        return this.canonicalizationMethod.equals(signedInfo.getCanonicalizationMethod()) && this.signatureMethod.equals(signedInfo.getSignatureMethod()) && this.references.equals(signedInfo.getReferences()) && zEquals;
    }

    public static List<Reference> getSignedInfoReferences(SignedInfo signedInfo) {
        return signedInfo.getReferences();
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.id != null) {
            iHashCode = (31 * 17) + this.id.hashCode();
        }
        return (31 * ((31 * ((31 * iHashCode) + this.canonicalizationMethod.hashCode())) + this.signatureMethod.hashCode())) + this.references.hashCode();
    }
}
