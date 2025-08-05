package sun.security.util.math;

import java.math.BigInteger;

/* loaded from: rt.jar:sun/security/util/math/IntegerModuloP.class */
public interface IntegerModuloP {
    IntegerFieldModuloP getField();

    BigInteger asBigInteger();

    ImmutableIntegerModuloP fixed();

    MutableIntegerModuloP mutable();

    ImmutableIntegerModuloP add(IntegerModuloP integerModuloP);

    ImmutableIntegerModuloP additiveInverse();

    ImmutableIntegerModuloP multiply(IntegerModuloP integerModuloP);

    void addModPowerTwo(IntegerModuloP integerModuloP, byte[] bArr);

    void asByteArray(byte[] bArr);

    default byte[] addModPowerTwo(IntegerModuloP integerModuloP, int i2) {
        byte[] bArr = new byte[i2];
        addModPowerTwo(integerModuloP, bArr);
        return bArr;
    }

    default byte[] asByteArray(int i2) {
        byte[] bArr = new byte[i2];
        asByteArray(bArr);
        return bArr;
    }

    default ImmutableIntegerModuloP multiplicativeInverse() {
        return pow(getField().getSize().subtract(BigInteger.valueOf(2L)));
    }

    default ImmutableIntegerModuloP subtract(IntegerModuloP integerModuloP) {
        return add(integerModuloP.additiveInverse());
    }

    default ImmutableIntegerModuloP square() {
        return multiply(this);
    }

    default ImmutableIntegerModuloP pow(BigInteger bigInteger) {
        MutableIntegerModuloP mutableIntegerModuloPMutable = getField().get1().mutable();
        MutableIntegerModuloP mutableIntegerModuloPMutable2 = mutable();
        int iBitLength = bigInteger.bitLength();
        for (int i2 = 0; i2 < iBitLength; i2++) {
            if (bigInteger.testBit(i2)) {
                mutableIntegerModuloPMutable.setProduct(mutableIntegerModuloPMutable2);
            }
            mutableIntegerModuloPMutable2.setSquare();
        }
        return mutableIntegerModuloPMutable.fixed();
    }
}
