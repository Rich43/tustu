package sun.security.ec;

import java.math.BigInteger;
import java.security.ProviderException;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import sun.security.ec.point.AffinePoint;
import sun.security.ec.point.MutablePoint;
import sun.security.ec.point.Point;
import sun.security.ec.point.ProjectivePoint;
import sun.security.util.math.ImmutableIntegerModuloP;
import sun.security.util.math.IntegerFieldModuloP;
import sun.security.util.math.IntegerModuloP;
import sun.security.util.math.MutableIntegerModuloP;
import sun.security.util.math.SmallValue;
import sun.security.util.math.intpoly.IntegerPolynomialP256;
import sun.security.util.math.intpoly.IntegerPolynomialP384;
import sun.security.util.math.intpoly.IntegerPolynomialP521;
import sun.security.util.math.intpoly.P256OrderField;
import sun.security.util.math.intpoly.P384OrderField;
import sun.security.util.math.intpoly.P521OrderField;

/* loaded from: sunec.jar:sun/security/ec/ECOperations.class */
public class ECOperations {
    static final Map<BigInteger, IntegerFieldModuloP> fields;
    static final Map<BigInteger, IntegerFieldModuloP> orderFields;

    /* renamed from: b, reason: collision with root package name */
    final ImmutableIntegerModuloP f13602b;
    final SmallValue one;
    final SmallValue two;
    final SmallValue three;
    final SmallValue four;
    final ProjectivePoint.Immutable neutral;
    private final IntegerFieldModuloP orderField;

    /* loaded from: sunec.jar:sun/security/ec/ECOperations$IntermediateValueException.class */
    static class IntermediateValueException extends Exception {
        private static final long serialVersionUID = 1;

        IntermediateValueException() {
        }
    }

    static {
        HashMap map = new HashMap();
        map.put(IntegerPolynomialP256.MODULUS, new IntegerPolynomialP256());
        map.put(IntegerPolynomialP384.MODULUS, new IntegerPolynomialP384());
        map.put(IntegerPolynomialP521.MODULUS, new IntegerPolynomialP521());
        fields = Collections.unmodifiableMap(map);
        HashMap map2 = new HashMap();
        map2.put(P256OrderField.MODULUS, new P256OrderField());
        map2.put(P384OrderField.MODULUS, new P384OrderField());
        map2.put(P521OrderField.MODULUS, new P521OrderField());
        orderFields = Collections.unmodifiableMap(map2);
    }

    public static Optional<ECOperations> forParameters(ECParameterSpec eCParameterSpec) {
        EllipticCurve curve = eCParameterSpec.getCurve();
        if (!(curve.getField() instanceof ECFieldFp)) {
            return Optional.empty();
        }
        ECFieldFp eCFieldFp = (ECFieldFp) curve.getField();
        if (!eCFieldFp.getP().subtract(curve.getA()).equals(BigInteger.valueOf(3L))) {
            return Optional.empty();
        }
        IntegerFieldModuloP integerFieldModuloP = fields.get(eCFieldFp.getP());
        if (integerFieldModuloP == null) {
            return Optional.empty();
        }
        IntegerFieldModuloP integerFieldModuloP2 = orderFields.get(eCParameterSpec.getOrder());
        if (integerFieldModuloP2 == null) {
            return Optional.empty();
        }
        return Optional.of(new ECOperations(integerFieldModuloP.getElement(curve.getB()), integerFieldModuloP2));
    }

    public ECOperations(IntegerModuloP integerModuloP, IntegerFieldModuloP integerFieldModuloP) {
        this.f13602b = integerModuloP.fixed();
        this.orderField = integerFieldModuloP;
        this.one = integerModuloP.getField().getSmallValue(1);
        this.two = integerModuloP.getField().getSmallValue(2);
        this.three = integerModuloP.getField().getSmallValue(3);
        this.four = integerModuloP.getField().getSmallValue(4);
        IntegerFieldModuloP field = integerModuloP.getField();
        this.neutral = new ProjectivePoint.Immutable(field.get0(), field.get1(), field.get0());
    }

    public IntegerFieldModuloP getField() {
        return this.f13602b.getField();
    }

    public IntegerFieldModuloP getOrderField() {
        return this.orderField;
    }

    protected ProjectivePoint.Immutable getNeutral() {
        return this.neutral;
    }

    public boolean isNeutral(Point point) {
        IntegerModuloP z2 = ((ProjectivePoint) point).getZ();
        return allZero(z2.asByteArray((z2.getField().getSize().bitLength() + 7) / 8));
    }

    byte[] seedToScalar(byte[] bArr) throws IntermediateValueException {
        int iBitLength = this.orderField.getSize().bitLength() + 64;
        if (bArr.length * 8 < iBitLength) {
            throw new ProviderException("Incorrect seed length: " + (bArr.length * 8) + " < " + iBitLength);
        }
        int i2 = iBitLength % 8;
        if (i2 != 0) {
            int i3 = iBitLength / 8;
            bArr[i3] = (byte) (bArr[i3] & ((byte) (255 >>> (8 - i2))));
        }
        ImmutableIntegerModuloP element = this.orderField.getElement(bArr, 0, (iBitLength + 7) / 8, (byte) 0);
        byte[] bArr2 = new byte[(this.orderField.getSize().bitLength() + 7) / 8];
        element.asByteArray(bArr2);
        if (allZero(bArr2)) {
            throw new IntermediateValueException();
        }
        return bArr2;
    }

    public static boolean allZero(byte[] bArr) {
        byte b2 = 0;
        for (byte b3 : bArr) {
            b2 = (byte) (b2 | b3);
        }
        return b2 == 0;
    }

    private void lookup4(ProjectivePoint.Immutable[] immutableArr, int i2, ProjectivePoint.Mutable mutable, IntegerModuloP integerModuloP) {
        for (int i3 = 0; i3 < 16; i3++) {
            int i4 = i2 ^ i3;
            mutable.conditionalSet((Point) immutableArr[i3], 1 - ((((i4 & 1) | ((i4 & 2) >>> 1)) | ((i4 & 4) >>> 2)) | ((i4 & 8) >>> 3)));
        }
    }

    private void double4(ProjectivePoint.Mutable mutable, MutableIntegerModuloP mutableIntegerModuloP, MutableIntegerModuloP mutableIntegerModuloP2, MutableIntegerModuloP mutableIntegerModuloP3, MutableIntegerModuloP mutableIntegerModuloP4, MutableIntegerModuloP mutableIntegerModuloP5) {
        for (int i2 = 0; i2 < 4; i2++) {
            setDouble(mutable, mutableIntegerModuloP, mutableIntegerModuloP2, mutableIntegerModuloP3, mutableIntegerModuloP4, mutableIntegerModuloP5);
        }
    }

    public MutablePoint multiply(AffinePoint affinePoint, byte[] bArr) {
        IntegerFieldModuloP field = affinePoint.getX().getField();
        ImmutableIntegerModuloP immutableIntegerModuloP = field.get0();
        MutableIntegerModuloP mutableIntegerModuloPMutable = immutableIntegerModuloP.mutable();
        MutableIntegerModuloP mutableIntegerModuloPMutable2 = immutableIntegerModuloP.mutable();
        MutableIntegerModuloP mutableIntegerModuloPMutable3 = immutableIntegerModuloP.mutable();
        MutableIntegerModuloP mutableIntegerModuloPMutable4 = immutableIntegerModuloP.mutable();
        MutableIntegerModuloP mutableIntegerModuloPMutable5 = immutableIntegerModuloP.mutable();
        ProjectivePoint.Mutable mutable = new ProjectivePoint.Mutable(field);
        mutable.getY().setValue(field.get1().mutable());
        ProjectivePoint.Immutable[] immutableArr = new ProjectivePoint.Immutable[16];
        immutableArr[0] = mutable.fixed();
        ProjectivePoint.Mutable mutable2 = new ProjectivePoint.Mutable(field);
        mutable2.setValue(affinePoint);
        immutableArr[1] = mutable2.fixed();
        for (int i2 = 2; i2 < 16; i2++) {
            setSum(mutable2, affinePoint, mutableIntegerModuloPMutable, mutableIntegerModuloPMutable2, mutableIntegerModuloPMutable3, mutableIntegerModuloPMutable4, mutableIntegerModuloPMutable5);
            immutableArr[i2] = mutable2.fixed();
        }
        ProjectivePoint.Mutable mutable3 = mutable2.mutable();
        for (int length = bArr.length - 1; length >= 0; length--) {
            double4(mutable, mutableIntegerModuloPMutable, mutableIntegerModuloPMutable2, mutableIntegerModuloPMutable3, mutableIntegerModuloPMutable4, mutableIntegerModuloPMutable5);
            lookup4(immutableArr, (255 & bArr[length]) >>> 4, mutable3, immutableIntegerModuloP);
            setSum(mutable, mutable3, mutableIntegerModuloPMutable, mutableIntegerModuloPMutable2, mutableIntegerModuloPMutable3, mutableIntegerModuloPMutable4, mutableIntegerModuloPMutable5);
            double4(mutable, mutableIntegerModuloPMutable, mutableIntegerModuloPMutable2, mutableIntegerModuloPMutable3, mutableIntegerModuloPMutable4, mutableIntegerModuloPMutable5);
            lookup4(immutableArr, 15 & bArr[length], mutable3, immutableIntegerModuloP);
            setSum(mutable, mutable3, mutableIntegerModuloPMutable, mutableIntegerModuloPMutable2, mutableIntegerModuloPMutable3, mutableIntegerModuloPMutable4, mutableIntegerModuloPMutable5);
        }
        return mutable;
    }

    private void setDouble(ProjectivePoint.Mutable mutable, MutableIntegerModuloP mutableIntegerModuloP, MutableIntegerModuloP mutableIntegerModuloP2, MutableIntegerModuloP mutableIntegerModuloP3, MutableIntegerModuloP mutableIntegerModuloP4, MutableIntegerModuloP mutableIntegerModuloP5) {
        mutableIntegerModuloP.setValue(mutable.getX()).setSquare();
        mutableIntegerModuloP2.setValue(mutable.getY()).setSquare();
        mutableIntegerModuloP3.setValue(mutable.getZ()).setSquare();
        mutableIntegerModuloP4.setValue(mutable.getX()).setProduct(mutable.getY());
        mutableIntegerModuloP5.setValue(mutable.getY()).setProduct(mutable.getZ());
        mutableIntegerModuloP4.setSum(mutableIntegerModuloP4);
        mutable.getZ().setProduct(mutable.getX());
        mutable.getZ().setProduct(this.two);
        mutable.getY().setValue(mutableIntegerModuloP3).setProduct(this.f13602b);
        mutable.getY().setDifference(mutable.getZ());
        mutable.getX().setValue(mutable.getY()).setProduct(this.two);
        mutable.getY().setSum(mutable.getX());
        mutable.getY().setReduced();
        mutable.getX().setValue(mutableIntegerModuloP2).setDifference(mutable.getY());
        mutable.getY().setSum(mutableIntegerModuloP2);
        mutable.getY().setProduct(mutable.getX());
        mutable.getX().setProduct(mutableIntegerModuloP4);
        mutableIntegerModuloP4.setValue(mutableIntegerModuloP3).setProduct(this.two);
        mutableIntegerModuloP3.setSum(mutableIntegerModuloP4);
        mutable.getZ().setProduct(this.f13602b);
        mutableIntegerModuloP3.setReduced();
        mutable.getZ().setDifference(mutableIntegerModuloP3);
        mutable.getZ().setDifference(mutableIntegerModuloP);
        mutableIntegerModuloP4.setValue(mutable.getZ()).setProduct(this.two);
        mutable.getZ().setReduced();
        mutable.getZ().setSum(mutableIntegerModuloP4);
        mutableIntegerModuloP.setProduct(this.three);
        mutableIntegerModuloP.setDifference(mutableIntegerModuloP3);
        mutableIntegerModuloP.setProduct(mutable.getZ());
        mutable.getY().setSum(mutableIntegerModuloP);
        mutableIntegerModuloP5.setSum(mutableIntegerModuloP5);
        mutable.getZ().setProduct(mutableIntegerModuloP5);
        mutable.getX().setDifference(mutable.getZ());
        mutable.getZ().setValue(mutableIntegerModuloP5).setProduct(mutableIntegerModuloP2);
        mutable.getZ().setProduct(this.four);
    }

    public void setSum(MutablePoint mutablePoint, AffinePoint affinePoint) {
        ImmutableIntegerModuloP immutableIntegerModuloP = mutablePoint.getField().get0();
        setSum((ProjectivePoint.Mutable) mutablePoint, affinePoint, immutableIntegerModuloP.mutable(), immutableIntegerModuloP.mutable(), immutableIntegerModuloP.mutable(), immutableIntegerModuloP.mutable(), immutableIntegerModuloP.mutable());
    }

    private void setSum(ProjectivePoint.Mutable mutable, AffinePoint affinePoint, MutableIntegerModuloP mutableIntegerModuloP, MutableIntegerModuloP mutableIntegerModuloP2, MutableIntegerModuloP mutableIntegerModuloP3, MutableIntegerModuloP mutableIntegerModuloP4, MutableIntegerModuloP mutableIntegerModuloP5) {
        mutableIntegerModuloP.setValue(mutable.getX()).setProduct(affinePoint.getX());
        mutableIntegerModuloP2.setValue(mutable.getY()).setProduct(affinePoint.getY());
        mutableIntegerModuloP4.setValue(affinePoint.getX()).setSum(affinePoint.getY());
        mutableIntegerModuloP5.setValue(mutable.getX()).setSum(mutable.getY());
        mutable.getX().setReduced();
        mutableIntegerModuloP4.setProduct(mutableIntegerModuloP5);
        mutableIntegerModuloP5.setValue(mutableIntegerModuloP).setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP4.setDifference(mutableIntegerModuloP5);
        mutableIntegerModuloP5.setValue(affinePoint.getY()).setProduct(mutable.getZ());
        mutableIntegerModuloP5.setSum(mutable.getY());
        mutable.getY().setValue(affinePoint.getX()).setProduct(mutable.getZ());
        mutable.getY().setSum(mutable.getX());
        mutableIntegerModuloP3.setValue(mutable.getZ());
        mutable.getZ().setProduct(this.f13602b);
        mutable.getX().setValue(mutable.getY()).setDifference(mutable.getZ());
        mutable.getX().setReduced();
        mutable.getZ().setValue(mutable.getX()).setProduct(this.two);
        mutable.getX().setSum(mutable.getZ());
        mutable.getZ().setValue(mutableIntegerModuloP2).setDifference(mutable.getX());
        mutable.getX().setSum(mutableIntegerModuloP2);
        mutable.getY().setProduct(this.f13602b);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP3).setProduct(this.two);
        mutableIntegerModuloP3.setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP3.setReduced();
        mutable.getY().setDifference(mutableIntegerModuloP3);
        mutable.getY().setDifference(mutableIntegerModuloP);
        mutable.getY().setReduced();
        mutableIntegerModuloP2.setValue(mutable.getY()).setProduct(this.two);
        mutable.getY().setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP).setProduct(this.two);
        mutableIntegerModuloP.setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP.setDifference(mutableIntegerModuloP3);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP5).setProduct(mutable.getY());
        mutableIntegerModuloP3.setValue(mutableIntegerModuloP).setProduct(mutable.getY());
        mutable.getY().setValue(mutable.getX()).setProduct(mutable.getZ());
        mutable.getY().setSum(mutableIntegerModuloP3);
        mutable.getX().setProduct(mutableIntegerModuloP4);
        mutable.getX().setDifference(mutableIntegerModuloP2);
        mutable.getZ().setProduct(mutableIntegerModuloP5);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP4).setProduct(mutableIntegerModuloP);
        mutable.getZ().setSum(mutableIntegerModuloP2);
    }

    private void setSum(ProjectivePoint.Mutable mutable, ProjectivePoint.Mutable mutable2, MutableIntegerModuloP mutableIntegerModuloP, MutableIntegerModuloP mutableIntegerModuloP2, MutableIntegerModuloP mutableIntegerModuloP3, MutableIntegerModuloP mutableIntegerModuloP4, MutableIntegerModuloP mutableIntegerModuloP5) {
        mutableIntegerModuloP.setValue(mutable.getX()).setProduct(mutable2.getX());
        mutableIntegerModuloP2.setValue(mutable.getY()).setProduct(mutable2.getY());
        mutableIntegerModuloP3.setValue(mutable.getZ()).setProduct(mutable2.getZ());
        mutableIntegerModuloP4.setValue(mutable.getX()).setSum(mutable.getY());
        mutableIntegerModuloP5.setValue(mutable2.getX()).setSum(mutable2.getY());
        mutableIntegerModuloP4.setProduct(mutableIntegerModuloP5);
        mutableIntegerModuloP5.setValue(mutableIntegerModuloP).setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP4.setDifference(mutableIntegerModuloP5);
        mutableIntegerModuloP5.setValue(mutable.getY()).setSum(mutable.getZ());
        mutable.getY().setValue(mutable2.getY()).setSum(mutable2.getZ());
        mutableIntegerModuloP5.setProduct(mutable.getY());
        mutable.getY().setValue(mutableIntegerModuloP2).setSum(mutableIntegerModuloP3);
        mutableIntegerModuloP5.setDifference(mutable.getY());
        mutable.getX().setSum(mutable.getZ());
        mutable.getY().setValue(mutable2.getX()).setSum(mutable2.getZ());
        mutable.getX().setProduct(mutable.getY());
        mutable.getY().setValue(mutableIntegerModuloP).setSum(mutableIntegerModuloP3);
        mutable.getY().setAdditiveInverse().setSum(mutable.getX());
        mutable.getY().setReduced();
        mutable.getZ().setValue(mutableIntegerModuloP3).setProduct(this.f13602b);
        mutable.getX().setValue(mutable.getY()).setDifference(mutable.getZ());
        mutable.getZ().setValue(mutable.getX()).setProduct(this.two);
        mutable.getX().setSum(mutable.getZ());
        mutable.getX().setReduced();
        mutable.getZ().setValue(mutableIntegerModuloP2).setDifference(mutable.getX());
        mutable.getX().setSum(mutableIntegerModuloP2);
        mutable.getY().setProduct(this.f13602b);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP3).setSum(mutableIntegerModuloP3);
        mutableIntegerModuloP3.setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP3.setReduced();
        mutable.getY().setDifference(mutableIntegerModuloP3);
        mutable.getY().setDifference(mutableIntegerModuloP);
        mutable.getY().setReduced();
        mutableIntegerModuloP2.setValue(mutable.getY()).setSum(mutable.getY());
        mutable.getY().setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP).setProduct(this.two);
        mutableIntegerModuloP.setSum(mutableIntegerModuloP2);
        mutableIntegerModuloP.setDifference(mutableIntegerModuloP3);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP5).setProduct(mutable.getY());
        mutableIntegerModuloP3.setValue(mutableIntegerModuloP).setProduct(mutable.getY());
        mutable.getY().setValue(mutable.getX()).setProduct(mutable.getZ());
        mutable.getY().setSum(mutableIntegerModuloP3);
        mutable.getX().setProduct(mutableIntegerModuloP4);
        mutable.getX().setDifference(mutableIntegerModuloP2);
        mutable.getZ().setProduct(mutableIntegerModuloP5);
        mutableIntegerModuloP2.setValue(mutableIntegerModuloP4).setProduct(mutableIntegerModuloP);
        mutable.getZ().setSum(mutableIntegerModuloP2);
    }
}
