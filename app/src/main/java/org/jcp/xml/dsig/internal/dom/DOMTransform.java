package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.TransformService;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMTransform.class */
public class DOMTransform extends DOMStructure implements Transform {
    protected TransformService spi;

    public DOMTransform(TransformService transformService) {
        this.spi = transformService;
    }

    public DOMTransform(Element element, XMLCryptoContext xMLCryptoContext, Provider provider) throws MarshalException, DOMException {
        String attributeValue = DOMUtils.getAttributeValue(element, Constants._ATT_ALGORITHM);
        if (provider == null) {
            try {
                this.spi = TransformService.getInstance(attributeValue, "DOM");
            } catch (NoSuchAlgorithmException e2) {
                throw new MarshalException(e2);
            }
        } else {
            try {
                this.spi = TransformService.getInstance(attributeValue, "DOM", provider);
            } catch (NoSuchAlgorithmException e3) {
                try {
                    this.spi = TransformService.getInstance(attributeValue, "DOM");
                } catch (NoSuchAlgorithmException e4) {
                    throw new MarshalException(e4);
                }
            }
        }
        try {
            this.spi.init(new javax.xml.crypto.dom.DOMStructure(element), xMLCryptoContext);
        } catch (InvalidAlgorithmParameterException e5) {
            throw new MarshalException(e5);
        }
    }

    @Override // javax.xml.crypto.dsig.Transform, javax.xml.crypto.AlgorithmMethod
    public final AlgorithmParameterSpec getParameterSpec() {
        return this.spi.getParameterSpec();
    }

    @Override // javax.xml.crypto.AlgorithmMethod
    public final String getAlgorithm() {
        return this.spi.getAlgorithm();
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Element elementCreateElement;
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        if (node.getLocalName().equals(Constants._TAG_TRANSFORMS)) {
            elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_TRANSFORM, "http://www.w3.org/2000/09/xmldsig#", str);
        } else {
            elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_CANONICALIZATIONMETHOD, "http://www.w3.org/2000/09/xmldsig#", str);
        }
        DOMUtils.setAttribute(elementCreateElement, Constants._ATT_ALGORITHM, getAlgorithm());
        this.spi.marshalParams(new javax.xml.crypto.dom.DOMStructure(elementCreateElement), dOMCryptoContext);
        node.appendChild(elementCreateElement);
    }

    @Override // javax.xml.crypto.dsig.Transform
    public Data transform(Data data, XMLCryptoContext xMLCryptoContext) throws TransformException {
        return this.spi.transform(data, xMLCryptoContext);
    }

    @Override // javax.xml.crypto.dsig.Transform
    public Data transform(Data data, XMLCryptoContext xMLCryptoContext, OutputStream outputStream) throws TransformException {
        return this.spi.transform(data, xMLCryptoContext, outputStream);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Transform)) {
            return false;
        }
        Transform transform = (Transform) obj;
        return getAlgorithm().equals(transform.getAlgorithm()) && DOMUtils.paramsEqual(getParameterSpec(), transform.getParameterSpec());
    }

    public int hashCode() {
        int iHashCode = (31 * 17) + getAlgorithm().hashCode();
        AlgorithmParameterSpec parameterSpec = getParameterSpec();
        if (parameterSpec != null) {
            iHashCode = (31 * iHashCode) + parameterSpec.hashCode();
        }
        return iHashCode;
    }

    Data transform(Data data, XMLCryptoContext xMLCryptoContext, DOMSignContext dOMSignContext) throws TransformException, MarshalException, DOMException {
        marshal(dOMSignContext.getParent(), DOMUtils.getSignaturePrefix(dOMSignContext), dOMSignContext);
        return transform(data, xMLCryptoContext);
    }
}
