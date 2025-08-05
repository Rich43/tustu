package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Set;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.NodeSetData;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.TransformService;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/ApacheCanonicalizer.class */
public abstract class ApacheCanonicalizer extends TransformService {
    private static final Logger LOG;
    protected Canonicalizer apacheCanonicalizer;
    private Transform apacheTransform;
    protected String inclusiveNamespaces;
    protected C14NMethodParameterSpec params;
    protected Document ownerDoc;
    protected Element transformElem;

    static {
        Init.init();
        LOG = LoggerFactory.getLogger(ApacheCanonicalizer.class);
    }

    @Override // javax.xml.crypto.dsig.Transform, javax.xml.crypto.AlgorithmMethod
    public final AlgorithmParameterSpec getParameterSpec() {
        return this.params;
    }

    @Override // javax.xml.crypto.dsig.TransformService
    public void init(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws InvalidAlgorithmParameterException {
        if (xMLCryptoContext != null && !(xMLCryptoContext instanceof DOMCryptoContext)) {
            throw new ClassCastException("context must be of type DOMCryptoContext");
        }
        if (xMLStructure == null) {
            throw new NullPointerException();
        }
        if (!(xMLStructure instanceof javax.xml.crypto.dom.DOMStructure)) {
            throw new ClassCastException("parent must be of type DOMStructure");
        }
        this.transformElem = (Element) ((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode();
        this.ownerDoc = DOMUtils.getOwnerDocument(this.transformElem);
    }

    @Override // javax.xml.crypto.dsig.TransformService
    public void marshalParams(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException {
        if (xMLCryptoContext != null && !(xMLCryptoContext instanceof DOMCryptoContext)) {
            throw new ClassCastException("context must be of type DOMCryptoContext");
        }
        if (xMLStructure == null) {
            throw new NullPointerException();
        }
        if (!(xMLStructure instanceof javax.xml.crypto.dom.DOMStructure)) {
            throw new ClassCastException("parent must be of type DOMStructure");
        }
        this.transformElem = (Element) ((javax.xml.crypto.dom.DOMStructure) xMLStructure).getNode();
        this.ownerDoc = DOMUtils.getOwnerDocument(this.transformElem);
    }

    public Data canonicalize(Data data, XMLCryptoContext xMLCryptoContext) throws TransformException {
        return canonicalize(data, xMLCryptoContext, null);
    }

    public Data canonicalize(Data data, XMLCryptoContext xMLCryptoContext, OutputStream outputStream) throws TransformException {
        Set<Node> nodeSet;
        if (this.apacheCanonicalizer == null) {
            try {
                this.apacheCanonicalizer = Canonicalizer.getInstance(getAlgorithm());
                this.apacheCanonicalizer.setSecureValidation(Utils.secureValidation(xMLCryptoContext));
                LOG.debug("Created canonicalizer for algorithm: {}", getAlgorithm());
            } catch (InvalidCanonicalizerException e2) {
                throw new TransformException("Couldn't find Canonicalizer for: " + getAlgorithm() + ": " + e2.getMessage(), e2);
            }
        }
        if (outputStream != null) {
            this.apacheCanonicalizer.setWriter(outputStream);
        } else {
            this.apacheCanonicalizer.setWriter(new ByteArrayOutputStream());
        }
        try {
            if (data instanceof ApacheData) {
                XMLSignatureInput xMLSignatureInput = ((ApacheData) data).getXMLSignatureInput();
                if (xMLSignatureInput.isElement()) {
                    if (this.inclusiveNamespaces != null) {
                        return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(xMLSignatureInput.getSubNode(), this.inclusiveNamespaces)));
                    }
                    return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(xMLSignatureInput.getSubNode())));
                }
                if (xMLSignatureInput.isNodeSet()) {
                    nodeSet = xMLSignatureInput.getNodeSet();
                } else {
                    return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalize(Utils.readBytesFromStream(xMLSignatureInput.getOctetStream()))));
                }
            } else {
                if (data instanceof DOMSubTreeData) {
                    DOMSubTreeData dOMSubTreeData = (DOMSubTreeData) data;
                    if (this.inclusiveNamespaces != null) {
                        return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(dOMSubTreeData.getRoot(), this.inclusiveNamespaces)));
                    }
                    return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(dOMSubTreeData.getRoot())));
                }
                if (data instanceof NodeSetData) {
                    nodeSet = Utils.toNodeSet(((NodeSetData) data).iterator());
                    LOG.debug("Canonicalizing {} nodes", Integer.valueOf(nodeSet.size()));
                } else {
                    return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalize(Utils.readBytesFromStream(((OctetStreamData) data).getOctetStream()))));
                }
            }
            if (this.inclusiveNamespaces != null) {
                return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeXPathNodeSet(nodeSet, this.inclusiveNamespaces)));
            }
            return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeXPathNodeSet(nodeSet)));
        } catch (Exception e3) {
            throw new TransformException(e3);
        }
    }

    @Override // javax.xml.crypto.dsig.Transform
    public Data transform(Data data, XMLCryptoContext xMLCryptoContext, OutputStream outputStream) throws TransformException {
        XMLSignatureInput xMLSignatureInput;
        if (data == null) {
            throw new NullPointerException("data must not be null");
        }
        if (outputStream == null) {
            throw new NullPointerException("output stream must not be null");
        }
        if (this.ownerDoc == null) {
            throw new TransformException("transform must be marshalled");
        }
        if (this.apacheTransform == null) {
            try {
                this.apacheTransform = new Transform(this.ownerDoc, getAlgorithm(), this.transformElem.getChildNodes());
                this.apacheTransform.setElement(this.transformElem, xMLCryptoContext.getBaseURI());
                this.apacheTransform.setSecureValidation(Utils.secureValidation(xMLCryptoContext));
                LOG.debug("Created transform for algorithm: {}", getAlgorithm());
            } catch (Exception e2) {
                throw new TransformException("Couldn't find Transform for: " + getAlgorithm(), e2);
            }
        }
        if (data instanceof ApacheData) {
            LOG.debug("ApacheData = true");
            xMLSignatureInput = ((ApacheData) data).getXMLSignatureInput();
        } else if (data instanceof NodeSetData) {
            LOG.debug("isNodeSet() = true");
            if (data instanceof DOMSubTreeData) {
                DOMSubTreeData dOMSubTreeData = (DOMSubTreeData) data;
                xMLSignatureInput = new XMLSignatureInput(dOMSubTreeData.getRoot());
                xMLSignatureInput.setExcludeComments(dOMSubTreeData.excludeComments());
            } else {
                xMLSignatureInput = new XMLSignatureInput(Utils.toNodeSet(((NodeSetData) data).iterator()));
            }
        } else {
            LOG.debug("isNodeSet() = false");
            try {
                xMLSignatureInput = new XMLSignatureInput(((OctetStreamData) data).getOctetStream());
            } catch (Exception e3) {
                throw new TransformException(e3);
            }
        }
        xMLSignatureInput.setSecureValidation(Utils.secureValidation(xMLCryptoContext));
        try {
            XMLSignatureInput xMLSignatureInputPerformTransform = this.apacheTransform.performTransform(xMLSignatureInput, outputStream);
            if (!xMLSignatureInputPerformTransform.isNodeSet() && !xMLSignatureInputPerformTransform.isElement()) {
                return null;
            }
            if (xMLSignatureInputPerformTransform.isOctetStream()) {
                return new ApacheOctetStreamData(xMLSignatureInputPerformTransform);
            }
            return new ApacheNodeSetData(xMLSignatureInputPerformTransform);
        } catch (Exception e4) {
            throw new TransformException(e4);
        }
    }

    @Override // javax.xml.crypto.XMLStructure
    public final boolean isFeatureSupported(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        return false;
    }
}
