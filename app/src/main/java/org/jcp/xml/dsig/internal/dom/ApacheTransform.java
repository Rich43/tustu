package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.transforms.Transform;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.NodeSetData;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.TransformService;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/ApacheTransform.class */
public abstract class ApacheTransform extends TransformService {
    private static final Logger LOG;
    private Transform apacheTransform;
    protected Document ownerDoc;
    protected Element transformElem;
    protected TransformParameterSpec params;

    static {
        Init.init();
        LOG = LoggerFactory.getLogger(ApacheTransform.class);
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

    @Override // javax.xml.crypto.dsig.Transform
    public Data transform(Data data, XMLCryptoContext xMLCryptoContext) throws TransformException {
        if (data == null) {
            throw new NullPointerException("data must not be null");
        }
        return transformIt(data, xMLCryptoContext, null);
    }

    @Override // javax.xml.crypto.dsig.Transform
    public Data transform(Data data, XMLCryptoContext xMLCryptoContext, OutputStream outputStream) throws TransformException {
        if (data == null) {
            throw new NullPointerException("data must not be null");
        }
        if (outputStream == null) {
            throw new NullPointerException("output stream must not be null");
        }
        return transformIt(data, xMLCryptoContext, outputStream);
    }

    private Data transformIt(Data data, XMLCryptoContext xMLCryptoContext, OutputStream outputStream) throws TransformException {
        XMLSignatureInput xMLSignatureInput;
        XMLSignatureInput xMLSignatureInputPerformTransform;
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
        if (Utils.secureValidation(xMLCryptoContext)) {
            String algorithm = getAlgorithm();
            if (Policy.restrictAlg(algorithm)) {
                throw new TransformException("Transform " + algorithm + " is forbidden when secure validation is enabled");
            }
        }
        if (data instanceof ApacheData) {
            LOG.debug("ApacheData = true");
            xMLSignatureInput = ((ApacheData) data).getXMLSignatureInput();
        } else if (data instanceof NodeSetData) {
            LOG.debug("isNodeSet() = true");
            if (data instanceof DOMSubTreeData) {
                LOG.debug("DOMSubTreeData = true");
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
            if (outputStream != null) {
                xMLSignatureInputPerformTransform = this.apacheTransform.performTransform(xMLSignatureInput, outputStream);
                if (!xMLSignatureInputPerformTransform.isNodeSet() && !xMLSignatureInputPerformTransform.isElement()) {
                    return null;
                }
            } else {
                xMLSignatureInputPerformTransform = this.apacheTransform.performTransform(xMLSignatureInput);
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
