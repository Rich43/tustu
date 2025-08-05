package sun.security.util.math;

import java.nio.ByteBuffer;

/* loaded from: rt.jar:sun/security/util/math/MutableIntegerModuloP.class */
public interface MutableIntegerModuloP extends IntegerModuloP {
    void conditionalSet(IntegerModuloP integerModuloP, int i2);

    void conditionalSwapWith(MutableIntegerModuloP mutableIntegerModuloP, int i2);

    MutableIntegerModuloP setValue(IntegerModuloP integerModuloP);

    MutableIntegerModuloP setValue(byte[] bArr, int i2, int i3, byte b2);

    MutableIntegerModuloP setValue(ByteBuffer byteBuffer, int i2, byte b2);

    MutableIntegerModuloP setSquare();

    MutableIntegerModuloP setSum(IntegerModuloP integerModuloP);

    MutableIntegerModuloP setDifference(IntegerModuloP integerModuloP);

    MutableIntegerModuloP setProduct(IntegerModuloP integerModuloP);

    MutableIntegerModuloP setProduct(SmallValue smallValue);

    MutableIntegerModuloP setAdditiveInverse();

    MutableIntegerModuloP setReduced();
}
