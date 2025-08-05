package javax.crypto;

/* loaded from: jce.jar:javax/crypto/NullCipher.class */
public class NullCipher extends Cipher {
    public NullCipher() {
        super(new NullCipherSpi(), null);
    }
}
