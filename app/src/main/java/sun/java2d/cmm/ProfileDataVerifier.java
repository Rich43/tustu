package sun.java2d.cmm;

/* loaded from: rt.jar:sun/java2d/cmm/ProfileDataVerifier.class */
public class ProfileDataVerifier {
    private static final int MAX_TAG_COUNT = 100;
    private static final int HEADER_SIZE = 128;
    private static final int TOC_OFFSET = 132;
    private static final int TOC_RECORD_SIZE = 12;
    private static final int PROFILE_FILE_SIGNATURE = 1633907568;

    public static void verify(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("Invalid ICC Profile Data");
        }
        if (bArr.length < 132) {
            throw new IllegalArgumentException("Invalid ICC Profile Data");
        }
        int int32 = readInt32(bArr, 0);
        int int322 = readInt32(bArr, 128);
        if (int322 < 0 || int322 > 100) {
            throw new IllegalArgumentException("Invalid ICC Profile Data");
        }
        if (int32 < 132 + (int322 * 12) || int32 > bArr.length) {
            throw new IllegalArgumentException("Invalid ICC Profile Data");
        }
        if (PROFILE_FILE_SIGNATURE != readInt32(bArr, 36)) {
            throw new IllegalArgumentException("Invalid ICC Profile Data");
        }
        for (int i2 = 0; i2 < int322; i2++) {
            int tagOffset = getTagOffset(i2, bArr);
            int tagSize = getTagSize(i2, bArr);
            if (tagOffset < 132 || tagOffset > int32) {
                throw new IllegalArgumentException("Invalid ICC Profile Data");
            }
            if (tagSize < 0 || tagSize > Integer.MAX_VALUE - tagOffset || tagSize + tagOffset > int32) {
                throw new IllegalArgumentException("Invalid ICC Profile Data");
            }
        }
    }

    private static int getTagOffset(int i2, byte[] bArr) {
        return readInt32(bArr, 132 + (i2 * 12) + 4);
    }

    private static int getTagSize(int i2, byte[] bArr) {
        return readInt32(bArr, 132 + (i2 * 12) + 8);
    }

    private static int readInt32(byte[] bArr, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < 4; i4++) {
            int i5 = i2;
            i2++;
            i3 = (i3 << 8) | (255 & bArr[i5]);
        }
        return i3;
    }
}
