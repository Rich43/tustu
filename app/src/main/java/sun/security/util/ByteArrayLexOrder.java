package sun.security.util;

import java.util.Comparator;

/* loaded from: rt.jar:sun/security/util/ByteArrayLexOrder.class */
public class ByteArrayLexOrder implements Comparator<byte[]> {
    @Override // java.util.Comparator
    public final int compare(byte[] bArr, byte[] bArr2) {
        for (int i2 = 0; i2 < bArr.length && i2 < bArr2.length; i2++) {
            int i3 = (bArr[i2] & 255) - (bArr2[i2] & 255);
            if (i3 != 0) {
                return i3;
            }
        }
        return bArr.length - bArr2.length;
    }
}
