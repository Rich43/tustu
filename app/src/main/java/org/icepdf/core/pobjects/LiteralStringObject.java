package org.icepdf.core.pobjects;

import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.security.SecurityManager;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/LiteralStringObject.class */
public class LiteralStringObject implements StringObject {
    private StringBuilder stringData;
    private static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    Reference reference;

    public LiteralStringObject(byte[] bytes) {
        this(new StringBuilder(bytes.length).append(new String(bytes)));
    }

    public LiteralStringObject(StringBuilder chars, boolean dif) {
        this.stringData = chars;
    }

    public LiteralStringObject(String string) {
        this.stringData = new StringBuilder(string.replaceAll("(?=[()\\\\])", "\\\\"));
    }

    public LiteralStringObject(String string, Reference reference, SecurityManager securityManager) {
        this.reference = reference;
        this.stringData = new StringBuilder(encryption(string, false, securityManager));
    }

    public LiteralStringObject(StringBuilder stringBuffer) {
        stringBuffer.deleteCharAt(0);
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        this.stringData = new StringBuilder(stringBuffer.length());
        this.stringData.append(stringBuffer.toString());
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public int getUnsignedInt(int start, int offset) {
        if (start < 0 || this.stringData.length() < start + offset) {
            return this.stringData.charAt(0);
        }
        if (offset == 1) {
            return this.stringData.charAt(start);
        }
        if (offset == 2) {
            return ((this.stringData.charAt(start) & 255) << 8) | (this.stringData.charAt(start + 1) & 255);
        }
        if (offset == 4) {
            return ((this.stringData.charAt(start) & 255) << 24) | ((this.stringData.charAt(start + 1) & 255) << 16) | ((this.stringData.charAt(start + 2) & 255) << 8) | (this.stringData.charAt(start + 3) & 255);
        }
        return 0;
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public String toString() {
        return this.stringData.toString();
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public String getHexString() {
        return stringToHex(this.stringData).toString();
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public StringBuilder getHexStringBuffer() {
        return stringToHex(this.stringData);
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public StringBuilder getLiteralStringBuffer() {
        return this.stringData;
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public String getLiteralString() {
        return this.stringData.toString();
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public StringBuilder getLiteralStringBuffer(int fontFormat, FontFile font) {
        int i2;
        if (fontFormat == 1 || font.isOneByteEncoding()) {
            return this.stringData;
        }
        if (fontFormat == 2) {
            int length = getLength();
            StringBuilder tmp = new StringBuilder(length);
            int lastIndex = 0;
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 < length) {
                    int charValue = getUnsignedInt(i4 - lastIndex, lastIndex + 2);
                    if (charValue <= 0 || !font.canDisplayEchar((char) charValue)) {
                        i2 = lastIndex + 2;
                    } else {
                        tmp.append((char) charValue);
                        i2 = 0;
                    }
                    lastIndex = i2;
                    i3 = i4 + 2;
                } else {
                    return tmp;
                }
            }
        } else {
            return null;
        }
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public int getLength() {
        return this.stringData.length();
    }

    private StringBuilder stringToHex(StringBuilder string) {
        StringBuilder hh = new StringBuilder(string.length() * 2);
        int max = string.length();
        for (int i2 = 0; i2 < max; i2++) {
            int charCode = string.charAt(i2);
            hh.append(hexChar[(charCode & 240) >>> 4]);
            hh.append(hexChar[charCode & 15]);
        }
        return hh;
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public void setReference(Reference reference) {
        this.reference = reference;
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public Reference getReference() {
        return this.reference;
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public String getDecryptedLiteralString(SecurityManager securityManager) {
        return encryption(this.stringData.toString(), true, securityManager);
    }

    public String encryption(String string, boolean decrypt, SecurityManager securityManager) {
        byte[] textBytes;
        if (securityManager != null && this.reference != null) {
            byte[] key = securityManager.getDecryptionKey();
            byte[] textBytes2 = Utils.convertByteCharSequenceToByteArray(string);
            if (decrypt) {
                textBytes = securityManager.decrypt(this.reference, key, textBytes2);
            } else {
                textBytes = securityManager.encrypt(this.reference, key, textBytes2);
            }
            return Utils.convertByteArrayToByteString(textBytes);
        }
        return string;
    }
}
