package org.jcp.xml.dsig.internal.dom;

import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXPathTransform.class */
public final class DOMXPathTransform extends ApacheTransform {
    @Override // javax.xml.crypto.dsig.TransformService
    public void init(TransformParameterSpec transformParameterSpec) throws InvalidAlgorithmParameterException {
        if (transformParameterSpec == null) {
            throw new InvalidAlgorithmParameterException("params are required");
        }
        if (!(transformParameterSpec instanceof XPathFilterParameterSpec)) {
            throw new InvalidAlgorithmParameterException("params must be of type XPathFilterParameterSpec");
        }
        this.params = transformParameterSpec;
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheTransform, javax.xml.crypto.dsig.TransformService
    public void init(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws DOMException, InvalidAlgorithmParameterException {
        super.init(xMLStructure, xMLCryptoContext);
        unmarshalParams(DOMUtils.getFirstChildElement(this.transformElem));
    }

    private void unmarshalParams(Element element) throws DOMException {
        String nodeValue = element.getFirstChild().getNodeValue();
        NamedNodeMap attributes = element.getAttributes();
        if (attributes != null) {
            int length = attributes.getLength();
            HashMap map = new HashMap(length);
            for (int i2 = 0; i2 < length; i2++) {
                Attr attr = (Attr) attributes.item(i2);
                String prefix = attr.getPrefix();
                if (prefix != null && "xmlns".equals(prefix)) {
                    map.put(attr.getLocalName(), attr.getValue());
                }
            }
            this.params = new XPathFilterParameterSpec(nodeValue, map);
            return;
        }
        this.params = new XPathFilterParameterSpec(nodeValue);
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheTransform, javax.xml.crypto.dsig.TransformService
    public void marshalParams(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException, DOMException {
        super.marshalParams(xMLStructure, xMLCryptoContext);
        XPathFilterParameterSpec xPathFilterParameterSpec = (XPathFilterParameterSpec) getParameterSpec();
        Element elementCreateElement = DOMUtils.createElement(this.ownerDoc, "XPath", "http://www.w3.org/2000/09/xmldsig#", DOMUtils.getSignaturePrefix(xMLCryptoContext));
        elementCreateElement.appendChild(this.ownerDoc.createTextNode(xPathFilterParameterSpec.getXPath()));
        for (Map.Entry entry : xPathFilterParameterSpec.getNamespaceMap().entrySet()) {
            elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + ((String) entry.getKey()), (String) entry.getValue());
        }
        this.transformElem.appendChild(elementCreateElement);
    }
}
