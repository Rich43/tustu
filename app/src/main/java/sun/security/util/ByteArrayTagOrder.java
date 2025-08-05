package sun.security.util;

import java.util.Comparator;

/* loaded from: rt.jar:sun/security/util/ByteArrayTagOrder.class */
public class ByteArrayTagOrder implements Comparator<byte[]> {
    @Override // java.util.Comparator
    public final int compare(byte[] bArr, byte[] bArr2) {
        return (bArr[0] | 32) - (bArr2[0] | 32);
    }
}
