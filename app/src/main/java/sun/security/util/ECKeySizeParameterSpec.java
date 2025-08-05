package sun.security.util;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:sun/security/util/ECKeySizeParameterSpec.class */
public class ECKeySizeParameterSpec implements AlgorithmParameterSpec {
    private int keySize;

    public ECKeySizeParameterSpec(int i2) {
        this.keySize = i2;
    }

    public int getKeySize() {
        return this.keySize;
    }
}
