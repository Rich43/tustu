package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.NodeSetData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dom.DOMURIReference;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMRetrievalMethod.class */
public final class DOMRetrievalMethod extends DOMStructure implements RetrievalMethod, DOMURIReference {
    private final List<Transform> transforms;
    private String uri;
    private String type;
    private Attr here;

    public DOMRetrievalMethod(String str, String str2, List<? extends Transform> list) {
        if (str == null) {
            throw new NullPointerException("uri cannot be null");
        }
        if (list == null || list.isEmpty()) {
            this.transforms = Collections.emptyList();
        } else {
            this.transforms = Collections.unmodifiableList(new ArrayList(list));
            int size = this.transforms.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (!(this.transforms.get(i2) instanceof Transform)) {
                    throw new ClassCastException("transforms[" + i2 + "] is not a valid type");
                }
            }
        }
        this.uri = str;
        if (!str.equals("")) {
            try {
                new URI(str);
            } catch (URISyntaxException e2) {
                throw new IllegalArgumentException(e2.getMessage());
            }
        }
        this.type = str2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x00e6, code lost:
    
        throw new javax.xml.crypto.MarshalException("Invalid element name: " + r0 + ", expected Transform");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public DOMRetrievalMethod(org.w3c.dom.Element r8, javax.xml.crypto.XMLCryptoContext r9, java.security.Provider r10) throws javax.xml.crypto.MarshalException {
        /*
            Method dump skipped, instructions count: 346
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jcp.xml.dsig.internal.dom.DOMRetrievalMethod.<init>(org.w3c.dom.Element, javax.xml.crypto.XMLCryptoContext, java.security.Provider):void");
    }

    @Override // javax.xml.crypto.dsig.keyinfo.RetrievalMethod, javax.xml.crypto.URIReference
    public String getURI() {
        return this.uri;
    }

    @Override // javax.xml.crypto.URIReference
    public String getType() {
        return this.type;
    }

    @Override // javax.xml.crypto.dsig.keyinfo.RetrievalMethod
    public List<Transform> getTransforms() {
        return this.transforms;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_RETRIEVALMETHOD, "http://www.w3.org/2000/09/xmldsig#", str);
        DOMUtils.setAttribute(elementCreateElement, Constants._ATT_URI, this.uri);
        DOMUtils.setAttribute(elementCreateElement, Constants._ATT_TYPE, this.type);
        if (!this.transforms.isEmpty()) {
            Element elementCreateElement2 = DOMUtils.createElement(ownerDocument, Constants._TAG_TRANSFORMS, "http://www.w3.org/2000/09/xmldsig#", str);
            elementCreateElement.appendChild(elementCreateElement2);
            Iterator<Transform> it = this.transforms.iterator();
            while (it.hasNext()) {
                ((DOMTransform) it.next()).marshal(elementCreateElement2, str, dOMCryptoContext);
            }
        }
        node.appendChild(elementCreateElement);
        this.here = elementCreateElement.getAttributeNodeNS(null, Constants._ATT_URI);
    }

    @Override // javax.xml.crypto.dom.DOMURIReference
    public Node getHere() {
        return this.here;
    }

    @Override // javax.xml.crypto.dsig.keyinfo.RetrievalMethod
    public Data dereference(XMLCryptoContext xMLCryptoContext) throws URIReferenceException {
        if (xMLCryptoContext == null) {
            throw new NullPointerException("context cannot be null");
        }
        URIDereferencer uRIDereferencer = xMLCryptoContext.getURIDereferencer();
        if (uRIDereferencer == null) {
            uRIDereferencer = DOMURIDereferencer.INSTANCE;
        }
        Data dataDereference = uRIDereferencer.dereference(this, xMLCryptoContext);
        try {
            Iterator<Transform> it = this.transforms.iterator();
            while (it.hasNext()) {
                dataDereference = ((DOMTransform) it.next()).transform(dataDereference, xMLCryptoContext);
            }
            if ((dataDereference instanceof NodeSetData) && Utils.secureValidation(xMLCryptoContext) && Policy.restrictRetrievalMethodLoops()) {
                Iterator it2 = ((NodeSetData) dataDereference).iterator();
                if (it2.hasNext() && Constants._TAG_RETRIEVALMETHOD.equals(((Node) it2.next()).getLocalName())) {
                    throw new URIReferenceException("It is forbidden to have one RetrievalMethod point to another when secure validation is enabled");
                }
            }
            return dataDereference;
        } catch (Exception e2) {
            throw new URIReferenceException(e2);
        }
    }

    public XMLStructure dereferenceAsXMLStructure(XMLCryptoContext xMLCryptoContext) throws URIReferenceException {
        boolean zSecureValidation = Utils.secureValidation(xMLCryptoContext);
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((ApacheData) dereference(xMLCryptoContext)).getXMLSignatureInput().getBytes());
            Throwable th = null;
            try {
                try {
                    Element documentElement = XMLUtils.createDocumentBuilder(false, zSecureValidation).parse(byteArrayInputStream).getDocumentElement();
                    if (documentElement.getLocalName().equals(Constants._TAG_X509DATA) && "http://www.w3.org/2000/09/xmldsig#".equals(documentElement.getNamespaceURI())) {
                        DOMX509Data dOMX509Data = new DOMX509Data(documentElement);
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
                        return dOMX509Data;
                    }
                    if (byteArrayInputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            byteArrayInputStream.close();
                        }
                    }
                    return null;
                } finally {
                }
            } finally {
            }
        } catch (Exception e2) {
            throw new URIReferenceException(e2);
        }
        throw new URIReferenceException(e2);
    }

    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RetrievalMethod)) {
            return false;
        }
        RetrievalMethod retrievalMethod = (RetrievalMethod) obj;
        if (this.type == null) {
            zEquals = retrievalMethod.getType() == null;
        } else {
            zEquals = this.type.equals(retrievalMethod.getType());
        }
        return this.uri.equals(retrievalMethod.getURI()) && this.transforms.equals(retrievalMethod.getTransforms()) && zEquals;
    }

    public int hashCode() {
        int iHashCode = 17;
        if (this.type != null) {
            iHashCode = (31 * 17) + this.type.hashCode();
        }
        return (31 * ((31 * iHashCode) + this.uri.hashCode())) + this.transforms.hashCode();
    }
}
