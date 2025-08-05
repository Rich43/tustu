package java.lang;

import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;

/* loaded from: rt.jar:java/lang/CharacterDataLatin1.class */
class CharacterDataLatin1 extends CharacterData {
    static char[] sharpsMap;
    static final CharacterDataLatin1 instance;

    /* renamed from: A, reason: collision with root package name */
    static final int[] f12436A;
    static final String A_DATA = "䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ堀䀏倀䀏堀䀏怀䀏倀䀏䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ倀䀏倀䀏倀䀏堀䀏怀䀌栀\u0018栀\u0018⠀\u0018⠀怚⠀\u0018栀\u0018栀\u0018\ue800\u0015\ue800\u0016栀\u0018\u2000\u0019㠀\u0018\u2000\u0014㠀\u0018㠀\u0018᠀㘉᠀㘉᠀㘉᠀㘉᠀㘉᠀㘉᠀㘉᠀㘉᠀㘉᠀㘉㠀\u0018栀\u0018\ue800\u0019栀\u0019\ue800\u0019栀\u0018栀\u0018\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\u0082翡\ue800\u0015栀\u0018\ue800\u0016栀\u001b栀倗栀\u001b\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\u0081翢\ue800\u0015栀\u0019\ue800\u0016栀\u0019䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ倀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ䠀ဏ㠀\f栀\u0018⠀怚⠀怚⠀怚⠀怚栀\u001c栀\u0018栀\u001b栀\u001c��瀅\ue800\u001d栀\u0019䠀တ栀\u001c栀\u001b⠀\u001c⠀\u0019᠀؋᠀؋栀\u001b߽瀂栀\u0018栀\u0018栀\u001b᠀ԋ��瀅\ue800\u001e栀ࠋ栀ࠋ栀ࠋ栀\u0018\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁栀\u0019\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁\u0082瀁߽瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂栀\u0019\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂\u0081瀂؝瀂";

    /* renamed from: B, reason: collision with root package name */
    static final char[] f12437B;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CharacterDataLatin1.class.desiredAssertionStatus();
        sharpsMap = new char[]{'S', 'S'};
        instance = new CharacterDataLatin1();
        f12436A = new int[256];
        f12437B = "����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������\u0001������������������������������\u0001������������������������������������������������������������������������������������������������������������������������������������������".toCharArray();
        char[] charArray = A_DATA.toCharArray();
        if (!$assertionsDisabled && charArray.length != 512) {
            throw new AssertionError();
        }
        int i2 = 0;
        int i3 = 0;
        while (i2 < 512) {
            int i4 = i2;
            int i5 = i2 + 1;
            int i6 = charArray[i4] << 16;
            int i7 = i3;
            i3++;
            i2 = i5 + 1;
            f12436A[i7] = i6 | charArray[i5];
        }
    }

    @Override // java.lang.CharacterData
    int getProperties(int i2) {
        return f12436A[(char) i2];
    }

    int getPropertiesEx(int i2) {
        return f12437B[(char) i2];
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
        if ((properties & 131072) != 0 && (properties & 133955584) != 133955584) {
            i3 = i2 + ((properties << 5) >> 23);
        }
        return i3;
    }

    @Override // java.lang.CharacterData
    int toUpperCase(int i2) {
        int i3 = i2;
        int properties = getProperties(i2);
        if ((properties & 65536) != 0) {
            if ((properties & 133955584) != 133955584) {
                i3 = i2 - ((properties << 5) >> 23);
            } else if (i2 == 181) {
                i3 = 924;
            }
        }
        return i3;
    }

    @Override // java.lang.CharacterData
    int toTitleCase(int i2) {
        return toUpperCase(i2);
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

    @Override // java.lang.CharacterData
    int toUpperCaseEx(int i2) {
        int i3 = i2;
        int properties = getProperties(i2);
        if ((properties & 65536) != 0) {
            if ((properties & 133955584) != 133955584) {
                i3 = i2 - ((properties << 5) >> 23);
            } else {
                switch (i2) {
                    case 181:
                        i3 = 924;
                        break;
                    default:
                        i3 = -1;
                        break;
                }
            }
        }
        return i3;
    }

    @Override // java.lang.CharacterData
    char[] toUpperCaseCharArray(int i2) {
        char[] cArr = {(char) i2};
        if (i2 == 223) {
            cArr = sharpsMap;
        }
        return cArr;
    }

    private CharacterDataLatin1() {
    }
}
