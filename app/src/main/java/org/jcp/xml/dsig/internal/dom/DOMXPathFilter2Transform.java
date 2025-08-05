package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.transforms.params.XPath2FilterContainer;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec;
import javax.xml.crypto.dsig.spec.XPathType;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMXPathFilter2Transform.class */
public final class DOMXPathFilter2Transform extends ApacheTransform {
    @Override // javax.xml.crypto.dsig.TransformService
    public void init(TransformParameterSpec transformParameterSpec) throws InvalidAlgorithmParameterException {
        if (transformParameterSpec == null) {
            throw new InvalidAlgorithmParameterException("params are required");
        }
        if (!(transformParameterSpec instanceof XPathFilter2ParameterSpec)) {
            throw new InvalidAlgorithmParameterException("params must be of type XPathFilter2ParameterSpec");
        }
        this.params = transformParameterSpec;
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheTransform, javax.xml.crypto.dsig.TransformService
    public void init(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws DOMException, InvalidAlgorithmParameterException {
        super.init(xMLStructure, xMLCryptoContext);
        try {
            unmarshalParams(DOMUtils.getFirstChildElement(this.transformElem));
        } catch (MarshalException e2) {
            throw new InvalidAlgorithmParameterException(e2);
        }
    }

    private void unmarshalParams(Element element) throws MarshalException, DOMException {
        XPathType.Filter filter;
        ArrayList arrayList = new ArrayList();
        Element nextSiblingElement = element;
        while (true) {
            Element element2 = nextSiblingElement;
            if (element2 != null) {
                String nodeValue = element2.getFirstChild().getNodeValue();
                String attributeValue = DOMUtils.getAttributeValue(element2, PdfOps.F_NAME);
                if (attributeValue == null) {
                    throw new MarshalException("filter cannot be null");
                }
                if (XPath2FilterContainer.INTERSECT.equals(attributeValue)) {
                    filter = XPathType.Filter.INTERSECT;
                } else if (XPath2FilterContainer.SUBTRACT.equals(attributeValue)) {
                    filter = XPathType.Filter.SUBTRACT;
                } else if ("union".equals(attributeValue)) {
                    filter = XPathType.Filter.UNION;
                } else {
                    throw new MarshalException("Unknown XPathType filter type" + attributeValue);
                }
                NamedNodeMap attributes = element2.getAttributes();
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
                    arrayList.add(new XPathType(nodeValue, filter, map));
                } else {
                    arrayList.add(new XPathType(nodeValue, filter));
                }
                nextSiblingElement = DOMUtils.getNextSiblingElement(element2);
            } else {
                this.params = new XPathFilter2ParameterSpec(arrayList);
                return;
            }
        }
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheTransform, javax.xml.crypto.dsig.TransformService
    public void marshalParams(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException, DOMException {
        super.marshalParams(xMLStructure, xMLCryptoContext);
        XPathFilter2ParameterSpec xPathFilter2ParameterSpec = (XPathFilter2ParameterSpec) getParameterSpec();
        String nSPrefix = DOMUtils.getNSPrefix(xMLCryptoContext, "http://www.w3.org/2002/06/xmldsig-filter2");
        String str = (nSPrefix == null || nSPrefix.length() == 0) ? "xmlns" : "xmlns:" + nSPrefix;
        for (XPathType xPathType : xPathFilter2ParameterSpec.getXPathList()) {
            Element elementCreateElement = DOMUtils.createElement(this.ownerDoc, "XPath", "http://www.w3.org/2002/06/xmldsig-filter2", nSPrefix);
            elementCreateElement.appendChild(this.ownerDoc.createTextNode(xPathType.getExpression()));
            DOMUtils.setAttribute(elementCreateElement, PdfOps.F_NAME, xPathType.getFilter().toString());
            elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", str, "http://www.w3.org/2002/06/xmldsig-filter2");
            for (Map.Entry entry : xPathType.getNamespaceMap().entrySet()) {
                elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + ((String) entry.getKey()), (String) entry.getValue());
            }
            this.transformElem.appendChild(elementCreateElement);
        }
    }
}
