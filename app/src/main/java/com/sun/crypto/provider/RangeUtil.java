package com.sun.crypto.provider;

import java.security.ProviderException;
import java.util.List;
import java.util.function.BiFunction;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/RangeUtil.class */
final class RangeUtil {
    private static final BiFunction<String, List<Integer>, ArrayIndexOutOfBoundsException> AIOOBE_SUPPLIER = Preconditions.outOfBoundsExceptionFormatter(ArrayIndexOutOfBoundsException::new);

    RangeUtil() {
    }

    public static void blockSizeCheck(int i2, int i3) {
        if (i2 % i3 != 0) {
            throw new ProviderException("Internal error in input buffering");
        }
    }

    public static void nullAndBoundsCheck(byte[] bArr, int i2, int i3) {
        Preconditions.checkFromIndexSize(i2, i3, bArr.length, AIOOBE_SUPPLIER);
    }
}
