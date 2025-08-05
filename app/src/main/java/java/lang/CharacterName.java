package java.lang;

import java.io.DataInputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.zip.InflaterInputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/lang/CharacterName.class */
class CharacterName {
    private static SoftReference<byte[]> refStrPool;
    private static int[][] lookup;

    CharacterName() {
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [int[], int[][]] */
    private static synchronized byte[] initNamePool() {
        byte[] bArr;
        if (refStrPool != null && (bArr = refStrPool.get()) != null) {
            return bArr;
        }
        DataInputStream dataInputStream = null;
        try {
            try {
                dataInputStream = new DataInputStream(new InflaterInputStream((InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: java.lang.CharacterName.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public InputStream run2() {
                        return getClass().getResourceAsStream("uniName.dat");
                    }
                })));
                lookup = new int[NormalizerImpl.JAMO_L_BASE];
                int i2 = dataInputStream.readInt();
                int i3 = dataInputStream.readInt();
                byte[] bArr2 = new byte[i3];
                dataInputStream.readFully(bArr2);
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                do {
                    int i7 = i5;
                    i5++;
                    int i8 = bArr2[i7] & 255;
                    if (i8 == 0) {
                        int i9 = i5 + 1;
                        i8 = bArr2[i5] & 255;
                        int i10 = i9 + 1;
                        int i11 = (bArr2[i9] & 255) << 16;
                        int i12 = i10 + 1;
                        int i13 = i11 | ((bArr2[i10] & 255) << 8);
                        i5 = i12 + 1;
                        i6 = i13 | (bArr2[i12] & 255);
                    } else {
                        i6++;
                    }
                    int i14 = i6 >> 8;
                    if (lookup[i14] == null) {
                        lookup[i14] = new int[256];
                    }
                    lookup[i14][i6 & 255] = (i4 << 8) | i8;
                    i4 += i8;
                } while (i5 < i3);
                byte[] bArr3 = new byte[i2 - i3];
                dataInputStream.readFully(bArr3);
                refStrPool = new SoftReference<>(bArr3);
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (Exception e2) {
                    }
                }
                return bArr3;
            } catch (Exception e3) {
                throw new InternalError(e3.getMessage(), e3);
            }
        } catch (Throwable th) {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (Exception e4) {
                    throw th;
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String get(int r8) {
        /*
            r0 = 0
            r9 = r0
            java.lang.ref.SoftReference<byte[]> r0 = java.lang.CharacterName.refStrPool
            if (r0 == 0) goto L16
            java.lang.ref.SoftReference<byte[]> r0 = java.lang.CharacterName.refStrPool
            java.lang.Object r0 = r0.get()
            byte[] r0 = (byte[]) r0
            r1 = r0
            r9 = r1
            if (r0 != 0) goto L1a
        L16:
            byte[] r0 = initNamePool()
            r9 = r0
        L1a:
            r0 = 0
            r10 = r0
            int[][] r0 = java.lang.CharacterName.lookup
            r1 = r8
            r2 = 8
            int r1 = r1 >> r2
            r0 = r0[r1]
            if (r0 == 0) goto L3a
            int[][] r0 = java.lang.CharacterName.lookup
            r1 = r8
            r2 = 8
            int r1 = r1 >> r2
            r0 = r0[r1]
            r1 = r8
            r2 = 255(0xff, float:3.57E-43)
            r1 = r1 & r2
            r0 = r0[r1]
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L3c
        L3a:
            r0 = 0
            return r0
        L3c:
            java.lang.String r0 = new java.lang.String
            r1 = r0
            r2 = r9
            r3 = 0
            r4 = r10
            r5 = 8
            int r4 = r4 >>> r5
            r5 = r10
            r6 = 255(0xff, float:3.57E-43)
            r5 = r5 & r6
            r1.<init>(r2, r3, r4, r5)
            r11 = r0
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.CharacterName.get(int):java.lang.String");
    }
}
