package java.lang;

import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;

/* loaded from: rt.jar:java/lang/CharacterData0E.class */
class CharacterData0E extends CharacterData {
    static final CharacterData instance;

    /* renamed from: X, reason: collision with root package name */
    static final char[] f12432X;

    /* renamed from: Y, reason: collision with root package name */
    static final char[] f12433Y;

    /* renamed from: A, reason: collision with root package name */
    static final int[] f12434A;
    static final String A_DATA = "砀��䠀တ砀��砀��䠀တ䠀တ䀀〆䀀〆";

    /* renamed from: B, reason: collision with root package name */
    static final char[] f12435B;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CharacterData0E.class.desiredAssertionStatus();
        instance = new CharacterData0E();
        f12432X = "��\u0010\u0010\u0010    0000000@                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ".toCharArray();
        f12433Y = "��\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0006\u0002\u0002\u0002\u0002\u0002\u0002\u0002\u0002".toCharArray();
        f12434A = new int[8];
        f12435B = "����������������".toCharArray();
        char[] charArray = A_DATA.toCharArray();
        if (!$assertionsDisabled && charArray.length != 16) {
            throw new AssertionError();
        }
        int i2 = 0;
        int i3 = 0;
        while (i2 < 16) {
            int i4 = i2;
            int i5 = i2 + 1;
            int i6 = charArray[i4] << 16;
            int i7 = i3;
            i3++;
            i2 = i5 + 1;
            f12434A[i7] = i6 | charArray[i5];
        }
    }

    @Override // java.lang.CharacterData
    int getProperties(int i2) {
        char c2 = (char) i2;
        return f12434A[f12433Y[f12432X[c2 >> 5] | ((c2 >> 1) & 15)] | (c2 & 1)];
    }

    int getPropertiesEx(int i2) {
        char c2 = (char) i2;
        return f12435B[f12433Y[f12432X[c2 >> 5] | ((c2 >> 1) & 15)] | (c2 & 1)];
    }

    @Override // java.lang.CharacterData
    boolean isOtherLowercase(int i2) {
        return (getPropertiesEx(i2) & 1) != 0;
    }

    @Override // java.lang.CharacterData
    boolean isOtherUppercase(int i2) {
        return (getPropertiesEx(i2) & 2) != 0;
    }

    @Override // java.lang.CharacterData
    boolean isOtherAlphabetic(int i2) {
        return (getPropertiesEx(i2) & 4) != 0;
    }

    @Override // java.lang.CharacterData
    boolean isIdeographic(int i2) {
        return (getPropertiesEx(i2) & 16) != 0;
    }

    @Override // java.lang.CharacterData
    int getType(int i2) {
        return getProperties(i2) & 31;
    }

    @Override // java.lang.CharacterData
    boolean isJavaIdentifierStart(int i2) {
        return (getProperties(i2) & CharacterType.SPACE_MASK) >= 20480;
    }

    @Override // java.lang.CharacterData
    boolean isJavaIdentifierPart(int i2) {
        return (getProperties(i2) & StackType.NULL_CHECK_START) != 0;
    }

    @Override // java.lang.CharacterData
    boolean isUnicodeIdentifierStart(int i2) {
        return (getProperties(i2) & CharacterType.SPACE_MASK) == 28672;
    }

    @Override // java.lang.CharacterData
    boolean isUnicodeIdentifierPart(int i2) {
        return (getProperties(i2) & 4096) != 0;
    }

    @Override // java.lang.CharacterData
    boolean isIdentifierIgnorable(int i2) {
        return (getProperties(i2) & CharacterType.SPACE_MASK) == 4096;
    }

    @Override // java.lang.CharacterData
    int toLowerCase(int i2) {
        int i3 = i2;
        int properties = getProperties(i2);
        if ((properties & 131072) != 0) {
            i3 = i2 + ((properties << 5) >> 23);
        }
        return i3;
    }

    @Override // java.lang.CharacterData
    int toUpperCase(int i2) {
        int i3 = i2;
        int properties = getProperties(i2);
        if ((properties & 65536) != 0) {
            i3 = i2 - ((properties << 5) >> 23);
        }
        return i3;
    }

    @Override // java.lang.CharacterData
    int toTitleCase(int i2) {
        int upperCase = i2;
        int properties = getProperties(i2);
        if ((properties & 32768) != 0) {
            if ((properties & 65536) == 0) {
                upperCase = i2 + 1;
            } else if ((properties & 131072) == 0) {
                upperCase = i2 - 1;
            }
        } else if ((properties & 65536) != 0) {
            upperCase = toUpperCase(i2);
        }
        return upperCase;
    }

    @Override // java.lang.CharacterData
    int digit(int i2, int i3) {
        int i4 = -1;
        if (i3 >= 2 && i3 <= 36) {
            int properties = getProperties(i2);
            if ((properties & 31) == 9) {
                i4 = (i2 + ((properties & 992) >> 5)) & 31;
            } else if ((properties & 3072) == 3072) {
                i4 = ((i2 + ((properties & 992) >> 5)) & 31) + 10;
            }
        }
        if (i4 < i3) {
            return i4;
        }
        return -1;
    }

    @Override // java.lang.CharacterData
    int getNumericValue(int i2) {
        int i3;
        int properties = getProperties(i2);
        switch (properties & 3072) {
            case 0:
            default:
                i3 = -1;
                break;
            case 1024:
                i3 = (i2 + ((properties & 992) >> 5)) & 31;
                break;
            case 2048:
                i3 = -2;
                break;
            case 3072:
                i3 = ((i2 + ((properties & 992) >> 5)) & 31) + 10;
                break;
        }
        return i3;
    }

    @Override // java.lang.CharacterData
    boolean isWhitespace(int i2) {
        return (getProperties(i2) & CharacterType.SPACE_MASK) == 16384;
    }

    @Override // java.lang.CharacterData
    byte getDirectionality(int i2) {
        byte properties = (byte) ((getProperties(i2) & 2013265920) >> 27);
        if (properties == 15) {
            properties = -1;
        }
        return properties;
    }

    @Override // java.lang.CharacterData
    boolean isMirrored(int i2) {
        return (getProperties(i2) & Integer.MIN_VALUE) != 0;
    }

    private CharacterData0E() {
    }
}
