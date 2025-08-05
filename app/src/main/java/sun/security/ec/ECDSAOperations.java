package sun.security.ec;

import java.security.ProviderException;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.util.Optional;
import sun.security.ec.ECOperations;
import sun.security.ec.point.AffinePoint;
import sun.security.util.ArrayUtil;
import sun.security.util.math.ImmutableIntegerModuloP;
import sun.security.util.math.IntegerFieldModuloP;
import sun.security.util.math.MutableIntegerModuloP;

/* loaded from: sunec.jar:sun/security/ec/ECDSAOperations.class */
public class ECDSAOperations {
    private final ECOperations ecOps;
    private final AffinePoint basePoint;

    /* loaded from: sunec.jar:sun/security/ec/ECDSAOperations$Seed.class */
    public static class Seed {
        private final byte[] seedValue;

        public Seed(byte[] bArr) {
            this.seedValue = bArr;
        }

        public byte[] getSeedValue() {
            return this.seedValue;
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/ECDSAOperations$Nonce.class */
    public static class Nonce {
        private final byte[] nonceValue;

        public Nonce(byte[] bArr) {
            this.nonceValue = bArr;
        }

        public byte[] getNonceValue() {
            return this.nonceValue;
        }
    }

    public ECDSAOperations(ECOperations eCOperations, ECPoint eCPoint) {
        this.ecOps = eCOperations;
        this.basePoint = toAffinePoint(eCPoint, eCOperations.getField());
    }

    public ECOperations getEcOperations() {
        return this.ecOps;
    }

    public AffinePoint basePointMultiply(byte[] bArr) {
        return this.ecOps.multiply(this.basePoint, bArr).asAffine();
    }

    public static AffinePoint toAffinePoint(ECPoint eCPoint, IntegerFieldModuloP integerFieldModuloP) {
        return new AffinePoint(integerFieldModuloP.getElement(eCPoint.getAffineX()), integerFieldModuloP.getElement(eCPoint.getAffineY()));
    }

    public static Optional<ECDSAOperations> forParameters(ECParameterSpec eCParameterSpec) {
        return ECOperations.forParameters(eCParameterSpec).map(eCOperations -> {
            return new ECDSAOperations(eCOperations, eCParameterSpec.getGenerator());
        });
    }

    public byte[] signDigest(byte[] bArr, byte[] bArr2, Seed seed) throws ECOperations.IntermediateValueException {
        return signDigest(bArr, bArr2, new Nonce(this.ecOps.seedToScalar(seed.getSeedValue())));
    }

    public byte[] signDigest(byte[] bArr, byte[] bArr2, Nonce nonce) throws ECOperations.IntermediateValueException {
        IntegerFieldModuloP orderField = this.ecOps.getOrderField();
        int iBitLength = orderField.getSize().bitLength();
        if (iBitLength % 8 != 0 && iBitLength < bArr2.length * 8) {
            throw new ProviderException("Invalid digest length");
        }
        byte[] nonceValue = nonce.getNonceValue();
        int iBitLength2 = (orderField.getSize().bitLength() + 7) / 8;
        if (nonceValue.length != iBitLength2) {
            throw new ProviderException("Incorrect nonce length");
        }
        ImmutableIntegerModuloP x2 = this.ecOps.multiply(this.basePoint, nonceValue).asAffine().getX();
        byte[] bArr3 = new byte[iBitLength2];
        x2.asByteArray(bArr3);
        ImmutableIntegerModuloP element = orderField.getElement(bArr3);
        element.asByteArray(bArr3);
        byte[] bArr4 = new byte[2 * iBitLength2];
        ArrayUtil.reverse(bArr3);
        System.arraycopy(bArr3, 0, bArr4, 0, iBitLength2);
        if (ECOperations.allZero(bArr3)) {
            throw new ECOperations.IntermediateValueException();
        }
        ImmutableIntegerModuloP element2 = orderField.getElement(bArr);
        int iMin = Math.min(iBitLength2, bArr2.length);
        byte[] bArr5 = new byte[iMin];
        System.arraycopy(bArr2, 0, bArr5, 0, iMin);
        ArrayUtil.reverse(bArr5);
        ImmutableIntegerModuloP element3 = orderField.getElement(bArr5);
        ImmutableIntegerModuloP immutableIntegerModuloPMultiplicativeInverse = orderField.getElement(nonceValue).multiplicativeInverse();
        MutableIntegerModuloP mutableIntegerModuloPMutable = element.mutable();
        mutableIntegerModuloPMutable.setProduct(element2).setSum(element3).setProduct(immutableIntegerModuloPMultiplicativeInverse);
        mutableIntegerModuloPMutable.asByteArray(bArr3);
        ArrayUtil.reverse(bArr3);
        System.arraycopy(bArr3, 0, bArr4, iBitLength2, iBitLength2);
        if (ECOperations.allZero(bArr3)) {
            throw new ECOperations.IntermediateValueException();
        }
        return bArr4;
    }
}
