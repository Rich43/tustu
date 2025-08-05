package org.jcp.xml.dsig.internal.dom;

import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.TransformService;
import org.w3c.dom.Element;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMCanonicalizationMethod.class */
public class DOMCanonicalizationMethod extends DOMTransform implements CanonicalizationMethod {
    public DOMCanonicalizationMethod(TransformService transformService) throws InvalidAlgorithmParameterException {
        super(transformService);
        if (!(transformService instanceof ApacheCanonicalizer) && !isC14Nalg(transformService.getAlgorithm())) {
            throw new InvalidAlgorithmParameterException("Illegal CanonicalizationMethod");
        }
    }

    public DOMCanonicalizationMethod(Element element, XMLCryptoContext xMLCryptoContext, Provider provider) throws MarshalException {
        super(element, xMLCryptoContext, provider);
        if (!(this.spi instanceof ApacheCanonicalizer) && !isC14Nalg(this.spi.getAlgorithm())) {
            throw new MarshalException("Illegal CanonicalizationMethod");
        }
    }

    public Data canonicalize(Data data, XMLCryptoContext xMLCryptoContext) throws TransformException {
        return transform(data, xMLCryptoContext);
    }

    public Data canonicalize(Data data, XMLCryptoContext xMLCryptoContext, OutputStream outputStream) throws TransformException {
        return transform(data, xMLCryptoContext, outputStream);
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMTransform
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CanonicalizationMethod)) {
            return false;
        }
        CanonicalizationMethod canonicalizationMethod = (CanonicalizationMethod) obj;
        return getAlgorithm().equals(canonicalizationMethod.getAlgorithm()) && DOMUtils.paramsEqual(getParameterSpec(), canonicalizationMethod.getParameterSpec());
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMTransform
    public int hashCode() {
        int iHashCode = (31 * 17) + getAlgorithm().hashCode();
        AlgorithmParameterSpec parameterSpec = getParameterSpec();
        if (parameterSpec != null) {
            iHashCode = (31 * iHashCode) + parameterSpec.hashCode();
        }
        return iHashCode;
    }

    private static boolean isC14Nalg(String str) {
        return isInclusiveC14Nalg(str) || isExclusiveC14Nalg(str) || isC14N11alg(str);
    }

    private static boolean isInclusiveC14Nalg(String str) {
        return str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315") || str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
    }

    private static boolean isExclusiveC14Nalg(String str) {
        return str.equals("http://www.w3.org/2001/10/xml-exc-c14n#") || str.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments");
    }

    private static boolean isC14N11alg(String str) {
        return str.equals("http://www.w3.org/2006/12/xml-c14n11") || str.equals("http://www.w3.org/2006/12/xml-c14n11#WithComments");
    }
}
