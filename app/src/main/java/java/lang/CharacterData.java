package java.lang;

/* loaded from: rt.jar:java/lang/CharacterData.class */
abstract class CharacterData {
    abstract int getProperties(int i2);

    abstract int getType(int i2);

    abstract boolean isWhitespace(int i2);

    abstract boolean isMirrored(int i2);

    abstract boolean isJavaIdentifierStart(int i2);

    abstract boolean isJavaIdentifierPart(int i2);

    abstract boolean isUnicodeIdentifierStart(int i2);

    abstract boolean isUnicodeIdentifierPart(int i2);

    abstract boolean isIdentifierIgnorable(int i2);

    abstract int toLowerCase(int i2);

    abstract int toUpperCase(int i2);

    abstract int toTitleCase(int i2);

    abstract int digit(int i2, int i3);

    abstract int getNumericValue(int i2);

    abstract byte getDirectionality(int i2);

    CharacterData() {
    }

    int toUpperCaseEx(int i2) {
        return toUpperCase(i2);
    }

    char[] toUpperCaseCharArray(int i2) {
        return null;
    }

    boolean isOtherLowercase(int i2) {
        return false;
    }

    boolean isOtherUppercase(int i2) {
        return false;
    }

    boolean isOtherAlphabetic(int i2) {
        return false;
    }

    boolean isIdeographic(int i2) {
        return false;
    }

    static final CharacterData of(int i2) {
        if ((i2 >>> 8) == 0) {
            return CharacterDataLatin1.instance;
        }
        switch (i2 >>> 16) {
            case 0:
                return CharacterData00.instance;
            case 1:
                return CharacterData01.instance;
            case 2:
                return CharacterData02.instance;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            default:
                return CharacterDataUndefined.instance;
            case 14:
                return CharacterData0E.instance;
            case 15:
            case 16:
                return CharacterDataPrivateUse.instance;
        }
    }
}
