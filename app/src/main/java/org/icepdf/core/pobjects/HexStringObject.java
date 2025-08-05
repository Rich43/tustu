package org.icepdf.core.pobjects;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.security.SecurityManager;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/HexStringObject.class */
public class HexStringObject implements StringObject {
    private static Logger logger = Logger.getLogger(HexStringObject.class.toString());
    private StringBuilder stringData;
    Reference reference;

    public HexStringObject(byte[] bytes) {
        this(new StringBuilder(bytes.length).append(new String(bytes)));
    }

    public HexStringObject(StringBuilder stringBuffer) {
        stringBuffer.deleteCharAt(0);
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        this.stringData = new StringBuilder(stringBuffer.length());
        this.stringData.append(normalizeHex(stringBuffer, 2).toString());
    }

    public HexStringObject(String string) {
        this.stringData = new StringBuilder(string.length());
        this.stringData.append(normalizeHex(new StringBuilder(string), 2).toString());
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public int getUnsignedInt(int start, int offset) {
        if (start < 0 || this.stringData.length() < start + offset) {
            return 0;
        }
        int unsignedInt = 0;
        try {
            unsignedInt = Integer.parseInt(this.stringData.substring(start, start + offset), 16);
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("Number Format Exception " + unsignedInt);
            }
        }
        return unsignedInt;
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public String toString() {
        return getLiteralString();
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public String getHexString() {
        return this.stringData.toString();
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public StringBuilder getHexStringBuffer() {
        return this.stringData;
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public StringBuilder getLiteralStringBuffer() {
        return hexToString(this.stringData);
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public String getLiteralString() {
        return hexToString(this.stringData).toString();
    }

    @Override // org.icepdf.core.pobjects.StringObject
    public StringBuilder getLiteralStringBuffer(int fontFormat, FontFile font) {
        int i2;
        if (fontFormat == 1 || font.isOneByteEncoding()) {
            this.stringData = new StringBuilder(normalizeHex(this.stringData, 2).toString());
            int length = getLength();
            StringBuilder tmp = new StringBuilder(length);
            int lastIndex = 0;
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 < length) {
                    int offset = lastIndex + 2;
                    int charValue = getUnsignedInt(i4 - lastIndex, offset);
                    if ((offset < length && charValue == 0) || !font.canDisplayEchar((char) charValue)) {
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
        } else if (fontFormat == 2) {
            this.stringData = new StringBuilder(normalizeHex(this.stringData, 4).toString());
            int length2 = getLength();
            StringBuilder tmp2 = new StringBuilder(length2);
            int i5 = 0;
            while (true) {
                int i6 = i5;
                if (i6 < length2) {
                    int charValue2 = getUnsignedInt(i6, 4);
                    if (font.canDisplayEchar((char) charValue2)) {
                        tmp2.append((char) charValue2);
                    }
                    i5 = i6 + 4;
                } else {
                    return tmp2;
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

    private static StringBuilder normalizeHex(StringBuilder hex, int step) {
        int length = hex.length();
        int i2 = 0;
        while (i2 < length) {
            if (isNoneHexChar(hex.charAt(i2))) {
                hex.deleteCharAt(i2);
                length--;
                i2--;
            }
            i2++;
        }
        int length2 = hex.length();
        if (step == 2 && length2 % 2 != 0) {
            hex.append('0');
        }
        if (step == 4 && length2 % 4 != 0) {
            hex.append("00");
        }
        return hex;
    }

    private static boolean isNoneHexChar(char c2) {
        return (c2 < '0' || c2 > '9') && (c2 < 'A' || c2 > 'F') && (c2 < 'a' || c2 > 'f');
    }

    private StringBuilder hexToString(StringBuilder hh) {
        if (hh != null && hh.length() == 0) {
            return new StringBuilder();
        }
        if ((hh.charAt(0) == 'F') | (hh.charAt(0) == 'f')) {
            if ((hh.charAt(1) == 'E') | (hh.charAt(1) == 'e')) {
                if ((hh.charAt(2) == 'F') | (hh.charAt(2) == 'f')) {
                    if ((hh.charAt(3) == 'F') | (hh.charAt(3) == 'f')) {
                        int length = hh.length();
                        StringBuilder sb = new StringBuilder(length / 4);
                        int i2 = 0;
                        while (true) {
                            int i3 = i2;
                            if (i3 < length) {
                                String subStr = hh.substring(i3, i3 + 4);
                                sb.append((char) Integer.parseInt(subStr, 16));
                                i2 = i3 + 4;
                            } else {
                                return sb;
                            }
                        }
                    }
                }
            }
        }
        int length2 = hh.length();
        StringBuilder sb2 = new StringBuilder(length2 / 2);
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < length2) {
                String subStr2 = hh.substring(i5, i5 + 2);
                sb2.append((char) Integer.parseInt(subStr2, 16));
                i4 = i5 + 2;
            } else {
                return sb2;
            }
        }
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
        if (securityManager != null && this.reference != null) {
            byte[] key = securityManager.getDecryptionKey();
            byte[] textBytes = Utils.convertByteCharSequenceToByteArray(getLiteralString());
            return Utils.convertByteArrayToByteString(securityManager.decrypt(this.reference, key, textBytes));
        }
        return getLiteralString();
    }
}
