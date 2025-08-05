package sun.security.ssl;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;

/* loaded from: jsse.jar:sun/security/ssl/SSLKeyDerivation.class */
interface SSLKeyDerivation {
    SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException;
}
