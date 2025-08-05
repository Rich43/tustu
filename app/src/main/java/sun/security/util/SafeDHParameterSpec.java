package sun.security.util;

import java.math.BigInteger;
import javax.crypto.spec.DHParameterSpec;

/* loaded from: rt.jar:sun/security/util/SafeDHParameterSpec.class */
public final class SafeDHParameterSpec extends DHParameterSpec {
    public SafeDHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2) {
        super(bigInteger, bigInteger2);
    }

    public SafeDHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2, int i2) {
        super(bigInteger, bigInteger2, i2);
    }
}
