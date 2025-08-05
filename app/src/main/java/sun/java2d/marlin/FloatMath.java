package sun.java2d.marlin;

import sun.misc.FloatConsts;

/* loaded from: rt.jar:sun/java2d/marlin/FloatMath.class */
public final class FloatMath implements MarlinConst {
    static final boolean CHECK_OVERFLOW = true;
    static final boolean CHECK_NAN = true;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FloatMath.class.desiredAssertionStatus();
    }

    private FloatMath() {
    }

    static float max(float f2, float f3) {
        return f2 >= f3 ? f2 : f3;
    }

    static int max(int i2, int i3) {
        return i2 >= i3 ? i2 : i3;
    }

    static int min(int i2, int i3) {
        return i2 <= i3 ? i2 : i3;
    }

    public static float ceil_f(float f2) {
        int iFloatToRawIntBits = Float.floatToRawIntBits(f2);
        int i2 = ((iFloatToRawIntBits & FloatConsts.EXP_BIT_MASK) >> 23) - 127;
        if (i2 < 0) {
            return f2 == 0.0f ? f2 : f2 < 0.0f ? -0.0f : 1.0f;
        }
        if (i2 >= 23) {
            return f2;
        }
        if (!$assertionsDisabled && (i2 < 0 || i2 > 22)) {
            throw new AssertionError();
        }
        int i3 = iFloatToRawIntBits & ((FloatConsts.SIGNIF_BIT_MASK >> i2) ^ (-1));
        if (i3 == iFloatToRawIntBits) {
            return f2;
        }
        return Float.intBitsToFloat(i3) + ((i3 ^ (-1)) >>> 31);
    }

    public static float floor_f(float f2) {
        int iFloatToRawIntBits = Float.floatToRawIntBits(f2);
        int i2 = ((iFloatToRawIntBits & FloatConsts.EXP_BIT_MASK) >> 23) - 127;
        if (i2 < 0) {
            return f2 == 0.0f ? f2 : f2 < 0.0f ? -1.0f : 0.0f;
        }
        if (i2 >= 23) {
            return f2;
        }
        if (!$assertionsDisabled && (i2 < 0 || i2 > 22)) {
            throw new AssertionError();
        }
        int i3 = iFloatToRawIntBits & ((FloatConsts.SIGNIF_BIT_MASK >> i2) ^ (-1));
        if (i3 == iFloatToRawIntBits) {
            return f2;
        }
        return Float.intBitsToFloat(i3) + (i3 >> 31);
    }

    public static int ceil_int(float f2) {
        int i2 = (int) f2;
        if (f2 <= i2 || i2 == Integer.MAX_VALUE || Float.isNaN(f2)) {
            return i2;
        }
        return i2 + 1;
    }

    public static int floor_int(float f2) {
        int i2 = (int) f2;
        if (f2 >= i2 || i2 == Integer.MIN_VALUE || Float.isNaN(f2)) {
            return i2;
        }
        return i2 - 1;
    }
}
