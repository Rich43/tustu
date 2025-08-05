package org.jcp.xml.dsig.internal.dom;

import java.security.InvalidAlgorithmParameterException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXSLTTransform.class */
public final class DOMXSLTTransform extends ApacheTransform {
    @Override // javax.xml.crypto.dsig.TransformService
    public void init(TransformParameterSpec transformParameterSpec) throws InvalidAlgorithmParameterException {
        if (transformParameterSpec == null) {
            throw new InvalidAlgorithmParameterException("params are required");
        }
        if (!(transformParameterSpec instanceof XSLTTransformParameterSpec)) {
            throw new InvalidAlgorithmParameterException("unrecognized params");
        }
        this.params = transformParameterSpec;
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheTransform, javax.xml.crypto.dsig.TransformService
    public void init(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws InvalidAlgorithmParameterException {
        super.init(xMLStructure, xMLCryptoContext);
        unmarshalParams(DOMUtils.getFirstChildElement(this.transformElem));
    }

    private void unmarshalParams(Element element) {
        this.params = new XSLTTransformParameterSpec(new javax.xml.crypto.dom.DOMStructure(element));
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheTransform, javax.xml.crypto.dsig.TransformService
    public void marshalParams(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException, DOMException {
        super.marshalParams(xMLStructure, xMLCryptoContext);
        DOMUtils.appendChild(this.transformElem, ((javax.xml.crypto.dom.DOMStructure) ((XSLTTransformParameterSpec) getParameterSpec()).getStylesheet()).getNode());
    }
}
