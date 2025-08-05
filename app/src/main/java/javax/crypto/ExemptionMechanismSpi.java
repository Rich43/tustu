package javax.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/ExemptionMechanismSpi.class */
public abstract class ExemptionMechanismSpi {
    protected abstract int engineGetOutputSize(int i2);

    protected abstract void engineInit(Key key) throws ExemptionMechanismException, InvalidKeyException;

    protected abstract void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws ExemptionMechanismException, InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract void engineInit(Key key, AlgorithmParameters algorithmParameters) throws ExemptionMechanismException, InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract byte[] engineGenExemptionBlob() throws ExemptionMechanismException;

    protected abstract int engineGenExemptionBlob(byte[] bArr, int i2) throws ExemptionMechanismException, ShortBufferException;
}
