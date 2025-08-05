package sun.misc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:sun/misc/JavaSecuritySignatureAccess.class */
public interface JavaSecuritySignatureAccess {
    void initVerify(Signature signature, PublicKey publicKey, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException;

    void initVerify(Signature signature, Certificate certificate, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException;

    void initSign(Signature signature, PrivateKey privateKey, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException;
}
