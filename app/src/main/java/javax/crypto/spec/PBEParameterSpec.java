package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/spec/PBEParameterSpec.class */
public class PBEParameterSpec implements AlgorithmParameterSpec {
    private byte[] salt;
    private int iterationCount;
    private AlgorithmParameterSpec paramSpec;

    public PBEParameterSpec(byte[] bArr, int i2) {
        this.paramSpec = null;
        this.salt = (byte[]) bArr.clone();
        this.iterationCount = i2;
    }

    public PBEParameterSpec(byte[] bArr, int i2, AlgorithmParameterSpec algorithmParameterSpec) {
        this.paramSpec = null;
        this.salt = (byte[]) bArr.clone();
        this.iterationCount = i2;
        this.paramSpec = algorithmParameterSpec;
    }

    public byte[] getSalt() {
        return (byte[]) this.salt.clone();
    }

    public int getIterationCount() {
        return this.iterationCount;
    }

    public AlgorithmParameterSpec getParameterSpec() {
        return this.paramSpec;
    }
}
