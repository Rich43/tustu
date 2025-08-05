package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.List;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMExcC14NMethod.class */
public final class DOMExcC14NMethod extends ApacheCanonicalizer {
    @Override // javax.xml.crypto.dsig.TransformService
    public void init(TransformParameterSpec transformParameterSpec) throws InvalidAlgorithmParameterException {
        if (transformParameterSpec != null) {
            if (!(transformParameterSpec instanceof ExcC14NParameterSpec)) {
                throw new InvalidAlgorithmParameterException("params must be of type ExcC14NParameterSpec");
            }
            this.params = (C14NMethodParameterSpec) transformParameterSpec;
        }
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheCanonicalizer, javax.xml.crypto.dsig.TransformService
    public void init(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws DOMException, InvalidAlgorithmParameterException {
        super.init(xMLStructure, xMLCryptoContext);
        Element firstChildElement = DOMUtils.getFirstChildElement(this.transformElem);
        if (firstChildElement == null) {
            this.params = null;
            this.inclusiveNamespaces = null;
        } else {
            unmarshalParams(firstChildElement);
        }
    }

    private void unmarshalParams(Element element) throws DOMException {
        String attributeNS = element.getAttributeNS(null, InclusiveNamespaces._ATT_EC_PREFIXLIST);
        this.inclusiveNamespaces = attributeNS;
        int i2 = 0;
        int iIndexOf = attributeNS.indexOf(32);
        ArrayList arrayList = new ArrayList();
        while (iIndexOf != -1) {
            arrayList.add(attributeNS.substring(i2, iIndexOf));
            i2 = iIndexOf + 1;
            iIndexOf = attributeNS.indexOf(32, i2);
        }
        if (i2 <= attributeNS.length()) {
            arrayList.add(attributeNS.substring(i2));
        }
        this.params = new ExcC14NParameterSpec(arrayList);
    }

    public List<String> getParameterSpecPrefixList(ExcC14NParameterSpec excC14NParameterSpec) {
        return excC14NParameterSpec.getPrefixList();
    }

    @Override // org.jcp.xml.dsig.internal.dom.ApacheCanonicalizer, javax.xml.crypto.dsig.TransformService
    public void marshalParams(XMLStructure xMLStructure, XMLCryptoContext xMLCryptoContext) throws MarshalException, DOMException {
        super.marshalParams(xMLStructure, xMLCryptoContext);
        AlgorithmParameterSpec parameterSpec = getParameterSpec();
        if (parameterSpec == null) {
            return;
        }
        String nSPrefix = DOMUtils.getNSPrefix(xMLCryptoContext, "http://www.w3.org/2001/10/xml-exc-c14n#");
        Element elementCreateElement = DOMUtils.createElement(this.ownerDoc, InclusiveNamespaces._TAG_EC_INCLUSIVENAMESPACES, "http://www.w3.org/2001/10/xml-exc-c14n#", nSPrefix);
        if (nSPrefix == null || nSPrefix.length() == 0) {
            elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2001/10/xml-exc-c14n#");
        } else {
            elementCreateElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + nSPrefix, "http://www.w3.org/2001/10/xml-exc-c14n#");
        }
        ExcC14NParameterSpec excC14NParameterSpec = (ExcC14NParameterSpec) parameterSpec;
        StringBuffer stringBuffer = new StringBuffer("");
        List<String> parameterSpecPrefixList = getParameterSpecPrefixList(excC14NParameterSpec);
        int size = parameterSpecPrefixList.size();
        for (int i2 = 0; i2 < size; i2++) {
            stringBuffer.append(parameterSpecPrefixList.get(i2));
            if (i2 < size - 1) {
                stringBuffer.append(" ");
            }
        }
        DOMUtils.setAttribute(elementCreateElement, InclusiveNamespaces._ATT_EC_PREFIXLIST, stringBuffer.toString());
        this.inclusiveNamespaces = stringBuffer.toString();
        this.transformElem.appendChild(elementCreateElement);
    }

    public String getParamsNSURI() {
        return "http://www.w3.org/2001/10/xml-exc-c14n#";
    }

    @Override // javax.xml.crypto.dsig.Transform
    public Data transform(Data data, XMLCryptoContext xMLCryptoContext) throws TransformException {
        if ((data instanceof DOMSubTreeData) && ((DOMSubTreeData) data).excludeComments()) {
            try {
                this.apacheCanonicalizer = Canonicalizer.getInstance("http://www.w3.org/2001/10/xml-exc-c14n#");
                this.apacheCanonicalizer.setSecureValidation(Utils.secureValidation(xMLCryptoContext));
            } catch (InvalidCanonicalizerException e2) {
                throw new TransformException("Couldn't find Canonicalizer for: http://www.w3.org/2001/10/xml-exc-c14n#: " + e2.getMessage(), e2);
            }
        }
        return canonicalize(data, xMLCryptoContext);
    }
}
