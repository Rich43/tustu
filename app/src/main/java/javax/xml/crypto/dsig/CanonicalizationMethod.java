package javax.xml.crypto.dsig;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:javax/xml/crypto/dsig/CanonicalizationMethod.class */
public interface CanonicalizationMethod extends Transform {
    public static final String INCLUSIVE = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
    public static final String INCLUSIVE_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
    public static final String EXCLUSIVE = "http://www.w3.org/2001/10/xml-exc-c14n#";
    public static final String EXCLUSIVE_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";

    @Override // javax.xml.crypto.dsig.Transform, javax.xml.crypto.AlgorithmMethod
    AlgorithmParameterSpec getParameterSpec();
}
