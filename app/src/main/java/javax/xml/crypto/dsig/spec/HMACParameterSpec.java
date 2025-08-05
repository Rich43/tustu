package javax.xml.crypto.dsig.spec;

/* loaded from: rt.jar:javax/xml/crypto/dsig/spec/HMACParameterSpec.class */
public final class HMACParameterSpec implements SignatureMethodParameterSpec {
    private int outputLength;

    public HMACParameterSpec(int i2) {
        this.outputLength = i2;
    }

    public int getOutputLength() {
        return this.outputLength;
    }
}
