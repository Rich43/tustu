package javax.crypto;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/NullCipherSpi.class */
final class NullCipherSpi extends CipherSpi {
    protected NullCipherSpi() {
    }

    @Override // javax.crypto.CipherSpi
    public void engineSetMode(String str) {
    }

    @Override // javax.crypto.CipherSpi
    public void engineSetPadding(String str) {
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 1;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        return i2;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        return new byte[8];
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) {
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i2, bArr2, 0, i3);
        return bArr2;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        if (bArr == null) {
            return 0;
        }
        System.arraycopy(bArr, i2, bArr2, i4, i3);
        return i3;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) {
        return engineUpdate(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        return engineUpdate(bArr, i2, i3, bArr2, i4);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) {
        return 0;
    }
}
