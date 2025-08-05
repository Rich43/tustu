package sun.text.normalizer;

import java.util.HashMap;

/* loaded from: rt.jar:sun/text/normalizer/VersionInfo.class */
public final class VersionInfo {
    private int m_version_;
    private static final HashMap<Integer, Object> MAP_ = new HashMap<>();
    private static final String INVALID_VERSION_NUMBER_ = "Invalid version number: Version number may be negative or greater than 255";

    public static VersionInfo getInstance(String str) {
        int length = str.length();
        int[] iArr = new int[4];
        iArr[0] = 0;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 0;
        int i2 = 0;
        int i3 = 0;
        while (i2 < 4 && i3 < length) {
            char cCharAt = str.charAt(i3);
            if (cCharAt == '.') {
                i2++;
            } else {
                char c2 = (char) (cCharAt - '0');
                if (c2 < 0 || c2 > '\t') {
                    throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
                }
                int i4 = i2;
                iArr[i4] = iArr[i4] * 10;
                int i5 = i2;
                iArr[i5] = iArr[i5] + c2;
            }
            i3++;
        }
        if (i3 != length) {
            throw new IllegalArgumentException("Invalid version number: String '" + str + "' exceeds version format");
        }
        for (int i6 = 0; i6 < 4; i6++) {
            if (iArr[i6] < 0 || iArr[i6] > 255) {
                throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
            }
        }
        return getInstance(iArr[0], iArr[1], iArr[2], iArr[3]);
    }

    public static VersionInfo getInstance(int i2, int i3, int i4, int i5) {
        if (i2 < 0 || i2 > 255 || i3 < 0 || i3 > 255 || i4 < 0 || i4 > 255 || i5 < 0 || i5 > 255) {
            throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
        }
        int i6 = getInt(i2, i3, i4, i5);
        Integer numValueOf = Integer.valueOf(i6);
        Object versionInfo = MAP_.get(numValueOf);
        if (versionInfo == null) {
            versionInfo = new VersionInfo(i6);
            MAP_.put(numValueOf, versionInfo);
        }
        return (VersionInfo) versionInfo;
    }

    public int compareTo(VersionInfo versionInfo) {
        return this.m_version_ - versionInfo.m_version_;
    }

    private VersionInfo(int i2) {
        this.m_version_ = i2;
    }

    private static int getInt(int i2, int i3, int i4, int i5) {
        return (i2 << 24) | (i3 << 16) | (i4 << 8) | i5;
    }
}
