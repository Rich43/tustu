package javax.xml.crypto;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:javax/xml/crypto/AlgorithmMethod.class */
public interface AlgorithmMethod {
    String getAlgorithm();

    AlgorithmParameterSpec getParameterSpec();
}
