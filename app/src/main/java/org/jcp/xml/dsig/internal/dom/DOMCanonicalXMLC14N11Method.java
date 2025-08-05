package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import java.security.InvalidAlgorithmParameterException;
import javax.xml.crypto.Data;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMCanonicalXMLC14N11Method.class */
public final class DOMCanonicalXMLC14N11Method extends ApacheCanonicalizer {
    public static final String C14N_11 = "http://www.w3.org/2006/12/xml-c14n11";
    public static final String C14N_11_WITH_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11#WithComments";

    @Override // javax.xml.crypto.dsig.TransformService
    public void init(TransformParameterSpec transformParameterSpec) throws InvalidAlgorithmParameterException {
        if (transformParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("no parameters should be specified for Canonical XML 1.1 algorithm");
        }
    }

    @Override // javax.xml.crypto.dsig.Transform
    public Data transform(Data data, XMLCryptoContext xMLCryptoContext) throws TransformException {
        if ((data instanceof DOMSubTreeData) && ((DOMSubTreeData) data).excludeComments()) {
            try {
                this.apacheCanonicalizer = Canonicalizer.getInstance("http://www.w3.org/2006/12/xml-c14n11");
                this.apacheCanonicalizer.setSecureValidation(Utils.secureValidation(xMLCryptoContext));
            } catch (InvalidCanonicalizerException e2) {
                throw new TransformException("Couldn't find Canonicalizer for: http://www.w3.org/2006/12/xml-c14n11: " + e2.getMessage(), e2);
            }
        }
        return canonicalize(data, xMLCryptoContext);
    }
}
