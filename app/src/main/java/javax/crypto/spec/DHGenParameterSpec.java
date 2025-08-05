package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/spec/DHGenParameterSpec.class */
public class DHGenParameterSpec implements AlgorithmParameterSpec {
    private int primeSize;
    private int exponentSize;

    public DHGenParameterSpec(int i2, int i3) {
        this.primeSize = i2;
        this.exponentSize = i3;
    }

    public int getPrimeSize() {
        return this.primeSize;
    }

    public int getExponentSize() {
        return this.exponentSize;
    }
}
