package java.security.spec;

/* loaded from: rt.jar:java/security/spec/EncodedKeySpec.class */
public abstract class EncodedKeySpec implements KeySpec {
    private byte[] encodedKey;

    public abstract String getFormat();

    public EncodedKeySpec(byte[] bArr) {
        this.encodedKey = (byte[]) bArr.clone();
    }

    public byte[] getEncoded() {
        return (byte[]) this.encodedKey.clone();
    }
}
