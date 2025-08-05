package javax.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/KeyAgreementSpi.class */
public abstract class KeyAgreementSpi {
    protected abstract void engineInit(Key key, SecureRandom secureRandom) throws InvalidKeyException;

    protected abstract void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract Key engineDoPhase(Key key, boolean z2) throws IllegalStateException, InvalidKeyException;

    protected abstract byte[] engineGenerateSecret() throws IllegalStateException;

    protected abstract int engineGenerateSecret(byte[] bArr, int i2) throws IllegalStateException, ShortBufferException;

    protected abstract SecretKey engineGenerateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException;
}
