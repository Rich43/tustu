package sun.security.ec;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.Provider;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.InvalidParameterSpecException;
import java.util.Optional;
import sun.security.ec.ECOperations;
import sun.security.ec.point.AffinePoint;
import sun.security.jca.JCAUtil;
import sun.security.util.ECUtil;
import sun.security.util.SecurityProviderConstants;
import sun.security.util.math.IntegerFieldModuloP;

/* loaded from: sunec.jar:sun/security/ec/ECKeyPairGenerator.class */
public final class ECKeyPairGenerator extends KeyPairGeneratorSpi {
    private static final int KEY_SIZE_MIN = 112;
    private static final int KEY_SIZE_MAX = 571;
    private SecureRandom random;
    private int keySize;
    private AlgorithmParameterSpec params = null;

    private static native boolean isCurveSupported(byte[] bArr);

    private static native Object[] generateECKeyPair(int i2, byte[] bArr, byte[] bArr2) throws GeneralSecurityException;

    public ECKeyPairGenerator() throws InvalidParameterException {
        initialize(SecurityProviderConstants.DEF_EC_KEY_SIZE, (SecureRandom) null);
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(int i2, SecureRandom secureRandom) throws InvalidParameterException {
        checkKeySize(i2);
        this.params = ECUtil.getECParameterSpec((Provider) null, i2);
        if (this.params == null) {
            throw new InvalidParameterException("No EC parameters available for key size " + i2 + " bits");
        }
        this.random = secureRandom;
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        ECParameterSpec eCParameterSpec;
        if (algorithmParameterSpec instanceof ECParameterSpec) {
            eCParameterSpec = ECUtil.getECParameterSpec((Provider) null, (ECParameterSpec) algorithmParameterSpec);
            if (eCParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("Unsupported curve: " + ((Object) algorithmParameterSpec));
            }
        } else if (algorithmParameterSpec instanceof ECGenParameterSpec) {
            String name = ((ECGenParameterSpec) algorithmParameterSpec).getName();
            eCParameterSpec = ECUtil.getECParameterSpec((Provider) null, name);
            if (eCParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("Unknown curve name: " + name);
            }
        } else {
            throw new InvalidAlgorithmParameterException("ECParameterSpec or ECGenParameterSpec required for EC");
        }
        ensureCurveIsSupported(eCParameterSpec);
        this.params = eCParameterSpec;
        this.keySize = eCParameterSpec.getCurve().getField().getFieldSize();
        this.random = secureRandom;
    }

    private static void ensureCurveIsSupported(ECParameterSpec eCParameterSpec) throws InvalidAlgorithmParameterException {
        AlgorithmParameters eCParameters = ECUtil.getECParameters(null);
        try {
            eCParameters.init(eCParameterSpec);
            if (!isCurveSupported(eCParameters.getEncoded())) {
                throw new InvalidAlgorithmParameterException("Unsupported curve: " + eCParameters.toString());
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } catch (InvalidParameterSpecException e3) {
            throw new InvalidAlgorithmParameterException("Unsupported curve: " + eCParameterSpec.toString());
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        if (this.random == null) {
            this.random = JCAUtil.getSecureRandom();
        }
        try {
            Optional<KeyPair> optionalGenerateKeyPairImpl = generateKeyPairImpl(this.random);
            if (optionalGenerateKeyPairImpl.isPresent()) {
                return optionalGenerateKeyPairImpl.get();
            }
            return generateKeyPairNative(this.random);
        } catch (Exception e2) {
            throw new ProviderException(e2);
        }
    }

    private byte[] generatePrivateScalar(SecureRandom secureRandom, ECOperations eCOperations, int i2) {
        byte[] bArr = new byte[i2];
        for (int i3 = 0; i3 < 128; i3++) {
            secureRandom.nextBytes(bArr);
            try {
                return eCOperations.seedToScalar(bArr);
            } catch (ECOperations.IntermediateValueException e2) {
            }
        }
        throw new ProviderException("Unable to produce private key after 128 attempts");
    }

    private Optional<KeyPair> generateKeyPairImpl(SecureRandom secureRandom) throws InvalidKeyException {
        ECParameterSpec eCParameterSpec = (ECParameterSpec) this.params;
        Optional<ECOperations> optionalForParameters = ECOperations.forParameters(eCParameterSpec);
        if (!optionalForParameters.isPresent()) {
            return Optional.empty();
        }
        ECOperations eCOperations = optionalForParameters.get();
        IntegerFieldModuloP field = eCOperations.getField();
        byte[] bArrGeneratePrivateScalar = generatePrivateScalar(secureRandom, eCOperations, ((eCParameterSpec.getOrder().bitLength() + 64) + 7) / 8);
        ECPoint generator = eCParameterSpec.getGenerator();
        AffinePoint affinePointAsAffine = eCOperations.multiply(new AffinePoint(field.getElement(generator.getAffineX()), field.getElement(generator.getAffineY())), bArrGeneratePrivateScalar).asAffine();
        return Optional.of(new KeyPair(new ECPublicKeyImpl(new ECPoint(affinePointAsAffine.getX().asBigInteger(), affinePointAsAffine.getY().asBigInteger()), eCParameterSpec), new ECPrivateKeyImpl(bArrGeneratePrivateScalar, eCParameterSpec)));
    }

    private KeyPair generateKeyPairNative(SecureRandom secureRandom) throws Exception {
        ECParameterSpec eCParameterSpec = (ECParameterSpec) this.params;
        byte[] bArrEncodeECParameterSpec = ECUtil.encodeECParameterSpec(null, eCParameterSpec);
        byte[] bArr = new byte[(((this.keySize + 7) >> 3) + 1) * 2];
        secureRandom.nextBytes(bArr);
        Object[] objArrGenerateECKeyPair = generateECKeyPair(this.keySize, bArrEncodeECParameterSpec, bArr);
        return new KeyPair(new ECPublicKeyImpl(ECUtil.decodePoint((byte[]) objArrGenerateECKeyPair[1], eCParameterSpec.getCurve()), eCParameterSpec), new ECPrivateKeyImpl(new BigInteger(1, (byte[]) objArrGenerateECKeyPair[0]), eCParameterSpec));
    }

    private void checkKeySize(int i2) throws InvalidParameterException {
        if (i2 < 112) {
            throw new InvalidParameterException("Key size must be at least 112 bits");
        }
        if (i2 > KEY_SIZE_MAX) {
            throw new InvalidParameterException("Key size must be at most 571 bits");
        }
        this.keySize = i2;
    }
}
