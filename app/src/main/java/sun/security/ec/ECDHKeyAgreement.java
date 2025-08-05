package sun.security.ec;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;
import java.util.Optional;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import sun.security.ec.point.AffinePoint;
import sun.security.ec.point.MutablePoint;
import sun.security.util.ArrayUtil;
import sun.security.util.ECUtil;
import sun.security.util.math.IntegerFieldModuloP;
import sun.security.util.math.MutableIntegerModuloP;

/* loaded from: sunec.jar:sun/security/ec/ECDHKeyAgreement.class */
public final class ECDHKeyAgreement extends KeyAgreementSpi {
    private ECPrivateKey privateKey;
    private ECPublicKey publicKey;
    private int secretLen;

    private static native byte[] deriveKey(byte[] bArr, byte[] bArr2, byte[] bArr3) throws GeneralSecurityException;

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        if (!(key instanceof PrivateKey)) {
            throw new InvalidKeyException("Key must be instance of PrivateKey");
        }
        this.privateKey = (ECPrivateKey) ECKeyFactory.toECKey(key);
        this.publicKey = null;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("Parameters not supported");
        }
        engineInit(key, secureRandom);
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected Key engineDoPhase(Key key, boolean z2) throws IllegalStateException, InvalidKeyException {
        if (this.privateKey == null) {
            throw new IllegalStateException("Not initialized");
        }
        if (this.publicKey != null) {
            throw new IllegalStateException("Phase already executed");
        }
        if (!z2) {
            throw new IllegalStateException("Only two party agreement supported, lastPhase must be true");
        }
        if (!(key instanceof ECPublicKey)) {
            throw new InvalidKeyException("Key must be a PublicKey with algorithm EC");
        }
        this.publicKey = (ECPublicKey) key;
        this.secretLen = (this.publicKey.getParams().getCurve().getField().getFieldSize() + 7) >> 3;
        return null;
    }

    private static void validateCoordinate(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger.compareTo(BigInteger.ZERO) < 0) {
            throw new ProviderException("invalid coordinate");
        }
        if (bigInteger.compareTo(bigInteger2) >= 0) {
            throw new ProviderException("invalid coordinate");
        }
    }

    private static void validate(ECOperations eCOperations, ECPublicKey eCPublicKey) {
        BigInteger affineX = eCPublicKey.getW().getAffineX();
        BigInteger affineY = eCPublicKey.getW().getAffineY();
        BigInteger size = eCOperations.getField().getSize();
        validateCoordinate(affineX, size);
        validateCoordinate(affineY, size);
        EllipticCurve curve = eCPublicKey.getParams().getCurve();
        if (!affineX.modPow(BigInteger.valueOf(3L), size).add(curve.getA().multiply(affineX)).add(curve.getB()).mod(size).equals(affineY.modPow(BigInteger.valueOf(2L), size).mod(size))) {
            throw new ProviderException("point is not on curve");
        }
        AffinePoint affinePoint = new AffinePoint(eCOperations.getField().getElement(affineX), eCOperations.getField().getElement(affineY));
        byte[] byteArray = eCPublicKey.getParams().getOrder().toByteArray();
        ArrayUtil.reverse(byteArray);
        if (!eCOperations.isNeutral(eCOperations.multiply(affinePoint, byteArray))) {
            throw new ProviderException("point has incorrect order");
        }
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.privateKey == null || this.publicKey == null) {
            throw new IllegalStateException("Not initialized correctly");
        }
        return deriveKeyImpl(this.privateKey, this.publicKey).orElseGet(() -> {
            return deriveKeyNative(this.privateKey, this.publicKey);
        });
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected int engineGenerateSecret(byte[] bArr, int i2) throws IllegalStateException, ShortBufferException {
        if (i2 + this.secretLen > bArr.length) {
            throw new ShortBufferException("Need " + this.secretLen + " bytes, only " + (bArr.length - i2) + " available");
        }
        byte[] bArrEngineGenerateSecret = engineGenerateSecret();
        System.arraycopy(bArrEngineGenerateSecret, 0, bArr, i2, bArrEngineGenerateSecret.length);
        return bArrEngineGenerateSecret.length;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected SecretKey engineGenerateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        if (str == null) {
            throw new NoSuchAlgorithmException("Algorithm must not be null");
        }
        if (!str.equals("TlsPremasterSecret")) {
            throw new NoSuchAlgorithmException("Only supported for algorithm TlsPremasterSecret");
        }
        return new SecretKeySpec(engineGenerateSecret(), "TlsPremasterSecret");
    }

    private static Optional<byte[]> deriveKeyImpl(ECPrivateKey eCPrivateKey, ECPublicKey eCPublicKey) {
        ECParameterSpec params = eCPrivateKey.getParams();
        EllipticCurve curve = params.getCurve();
        Optional<ECOperations> optionalForParameters = ECOperations.forParameters(params);
        if (!optionalForParameters.isPresent()) {
            return Optional.empty();
        }
        ECOperations eCOperations = optionalForParameters.get();
        if (!(eCPrivateKey instanceof ECPrivateKeyImpl)) {
            return Optional.empty();
        }
        byte[] arrayS = ((ECPrivateKeyImpl) eCPrivateKey).getArrayS();
        validate(eCOperations, eCPublicKey);
        IntegerFieldModuloP field = eCOperations.getField();
        MutableIntegerModuloP mutableIntegerModuloPMutable = field.getElement(arrayS).mutable();
        mutableIntegerModuloPMutable.setProduct(field.getSmallValue(eCPrivateKey.getParams().getCofactor()));
        int fieldSize = (curve.getField().getFieldSize() + 7) / 8;
        MutablePoint mutablePointMultiply = eCOperations.multiply(new AffinePoint(field.getElement(eCPublicKey.getW().getAffineX()), field.getElement(eCPublicKey.getW().getAffineY())), mutableIntegerModuloPMutable.asByteArray(fieldSize));
        if (eCOperations.isNeutral(mutablePointMultiply)) {
            throw new ProviderException("Product is zero");
        }
        byte[] bArrAsByteArray = mutablePointMultiply.asAffine().getX().asByteArray(fieldSize);
        ArrayUtil.reverse(bArrAsByteArray);
        return Optional.of(bArrAsByteArray);
    }

    private static byte[] deriveKeyNative(ECPrivateKey eCPrivateKey, ECPublicKey eCPublicKey) {
        byte[] bArrEncodePoint;
        ECParameterSpec params = eCPrivateKey.getParams();
        byte[] byteArray = eCPrivateKey.getS().toByteArray();
        byte[] bArrEncodeECParameterSpec = ECUtil.encodeECParameterSpec(null, params);
        if (eCPublicKey instanceof ECPublicKeyImpl) {
            bArrEncodePoint = ((ECPublicKeyImpl) eCPublicKey).getEncodedPublicValue();
        } else {
            bArrEncodePoint = ECUtil.encodePoint(eCPublicKey.getW(), params.getCurve());
        }
        try {
            return deriveKey(byteArray, bArrEncodePoint, bArrEncodeECParameterSpec);
        } catch (GeneralSecurityException e2) {
            throw new ProviderException("Could not derive key", e2);
        }
    }
}
