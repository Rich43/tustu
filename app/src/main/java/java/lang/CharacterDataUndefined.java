package java.lang;

/* loaded from: rt.jar:java/lang/CharacterDataUndefined.class */
class CharacterDataUndefined extends CharacterData {
    static final CharacterData instance = new CharacterDataUndefined();

    @Override // java.lang.CharacterData
    int getProperties(int i2) {
        return 0;
    }

    @Override // java.lang.CharacterData
    int getType(int i2) {
        return 0;
    }

    @Override // java.lang.CharacterData
    boolean isJavaIdentifierStart(int i2) {
        return false;
    }

    @Override // java.lang.CharacterData
    boolean isJavaIdentifierPart(int i2) {
        return false;
    }

    @Override // java.lang.CharacterData
    boolean isUnicodeIdentifierStart(int i2) {
        return false;
    }

    @Override // java.lang.CharacterData
    boolean isUnicodeIdentifierPart(int i2) {
        return false;
    }

    @Override // java.lang.CharacterData
    boolean isIdentifierIgnorable(int i2) {
        return false;
    }

    @Override // java.lang.CharacterData
    int toLowerCase(int i2) {
        return i2;
    }

    @Override // java.lang.CharacterData
    int toUpperCase(int i2) {
        return i2;
    }

    @Override // java.lang.CharacterData
    int toTitleCase(int i2) {
        return i2;
    }

    @Override // java.lang.CharacterData
    int digit(int i2, int i3) {
        return -1;
    }

    @Override // java.lang.CharacterData
    int getNumericValue(int i2) {
        return -1;
    }

    @Override // java.lang.CharacterData
    boolean isWhitespace(int i2) {
        return false;
    }

    @Override // java.lang.CharacterData
    byte getDirectionality(int i2) {
        return (byte) -1;
    }

    @Override // java.lang.CharacterData
    boolean isMirrored(int i2) {
        return false;
    }

    private CharacterDataUndefined() {
    }
}
