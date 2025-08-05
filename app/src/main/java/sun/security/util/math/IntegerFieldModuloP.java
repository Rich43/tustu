package sun.security.util.math;

import java.math.BigInteger;

/* loaded from: rt.jar:sun/security/util/math/IntegerFieldModuloP.class */
public interface IntegerFieldModuloP {
    BigInteger getSize();

    ImmutableIntegerModuloP get0();

    ImmutableIntegerModuloP get1();

    ImmutableIntegerModuloP getElement(BigInteger bigInteger);

    SmallValue getSmallValue(int i2);

    ImmutableIntegerModuloP getElement(byte[] bArr, int i2, int i3, byte b2);

    default ImmutableIntegerModuloP getElement(byte[] bArr) {
        return getElement(bArr, 0, bArr.length, (byte) 0);
    }
}
