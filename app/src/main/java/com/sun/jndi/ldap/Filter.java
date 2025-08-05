package com.sun.jndi.ldap;

import java.io.IOException;
import javax.naming.NamingException;
import javax.naming.directory.InvalidSearchFilterException;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/jndi/ldap/Filter.class */
final class Filter {
    private static final boolean dbg = false;
    private static int dbgIndent = 0;
    static final int LDAP_FILTER_AND = 160;
    static final int LDAP_FILTER_OR = 161;
    static final int LDAP_FILTER_NOT = 162;
    static final int LDAP_FILTER_EQUALITY = 163;
    static final int LDAP_FILTER_SUBSTRINGS = 164;
    static final int LDAP_FILTER_GE = 165;
    static final int LDAP_FILTER_LE = 166;
    static final int LDAP_FILTER_PRESENT = 135;
    static final int LDAP_FILTER_APPROX = 168;
    static final int LDAP_FILTER_EXT = 169;
    static final int LDAP_FILTER_EXT_RULE = 129;
    static final int LDAP_FILTER_EXT_TYPE = 130;
    static final int LDAP_FILTER_EXT_VAL = 131;
    static final int LDAP_FILTER_EXT_DN = 132;
    static final int LDAP_SUBSTRING_INITIAL = 128;
    static final int LDAP_SUBSTRING_ANY = 129;
    static final int LDAP_SUBSTRING_FINAL = 130;

    Filter() {
    }

    static void encodeFilterString(BerEncoder berEncoder, String str, boolean z2) throws IOException, NamingException {
        byte[] bytes;
        if (str == null || str.equals("")) {
            throw new InvalidSearchFilterException("Empty filter");
        }
        if (z2) {
            bytes = str.getBytes(InternalZipConstants.CHARSET_UTF8);
        } else {
            bytes = str.getBytes("8859_1");
        }
        encodeFilter(berEncoder, bytes, 0, bytes.length);
    }

    private static void encodeFilter(BerEncoder berEncoder, byte[] bArr, int i2, int i3) throws IOException, NamingException {
        if (i3 - i2 <= 0) {
            throw new InvalidSearchFilterException("Empty filter");
        }
        int i4 = 0;
        int[] iArr = {i2};
        while (iArr[0] < i3) {
            switch (bArr[iArr[0]]) {
                case 32:
                    iArr[0] = iArr[0] + 1;
                    break;
                case 40:
                    iArr[0] = iArr[0] + 1;
                    int i5 = i4 + 1;
                    switch (bArr[iArr[0]]) {
                        case 33:
                            encodeComplexFilter(berEncoder, bArr, 162, iArr, i3);
                            i4 = i5 - 1;
                            break;
                        case 38:
                            encodeComplexFilter(berEncoder, bArr, 160, iArr, i3);
                            i4 = i5 - 1;
                            break;
                        case 124:
                            encodeComplexFilter(berEncoder, bArr, 161, iArr, i3);
                            i4 = i5 - 1;
                            break;
                        default:
                            int i6 = 1;
                            boolean z2 = false;
                            int i7 = iArr[0];
                            while (i7 < i3 && i6 > 0) {
                                if (!z2) {
                                    if (bArr[i7] == 40) {
                                        i6++;
                                    } else if (bArr[i7] == 41) {
                                        i6--;
                                    }
                                }
                                if (bArr[i7] == 92 && !z2) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                if (i6 > 0) {
                                    i7++;
                                }
                            }
                            if (i6 != 0) {
                                throw new InvalidSearchFilterException("Unbalanced parenthesis");
                            }
                            encodeSimpleFilter(berEncoder, bArr, iArr[0], i7);
                            iArr[0] = i7 + 1;
                            i4 = i5 - 1;
                            break;
                            break;
                    }
                case 41:
                    berEncoder.endSeq();
                    iArr[0] = iArr[0] + 1;
                    i4--;
                    break;
                default:
                    encodeSimpleFilter(berEncoder, bArr, iArr[0], i3);
                    iArr[0] = i3;
                    break;
            }
            if (i4 < 0) {
                throw new InvalidSearchFilterException("Unbalanced parenthesis");
            }
        }
        if (i4 != 0) {
            throw new InvalidSearchFilterException("Unbalanced parenthesis");
        }
    }

    private static int hexchar2int(byte b2) {
        if (b2 >= 48 && b2 <= 57) {
            return b2 - 48;
        }
        if (b2 >= 65 && b2 <= 70) {
            return (b2 - 65) + 10;
        }
        if (b2 >= 97 && b2 <= 102) {
            return (b2 - 97) + 10;
        }
        return -1;
    }

    static byte[] unescapeFilterValue(byte[] bArr, int i2, int i3) throws NamingException {
        boolean z2 = false;
        boolean z3 = false;
        byte[] bArr2 = new byte[i3 - i2];
        int i4 = 0;
        for (int i5 = i2; i5 < i3; i5++) {
            byte b2 = bArr[i5];
            if (z2) {
                int iHexchar2int = hexchar2int(b2);
                if (iHexchar2int < 0) {
                    if (z3) {
                        z2 = false;
                        int i6 = i4;
                        i4++;
                        bArr2[i6] = b2;
                    } else {
                        throw new InvalidSearchFilterException("invalid escape sequence: " + ((Object) bArr));
                    }
                } else if (z3) {
                    bArr2[i4] = (byte) (iHexchar2int << 4);
                    z3 = false;
                } else {
                    int i7 = i4;
                    i4++;
                    bArr2[i7] = (byte) (bArr2[i7] | ((byte) iHexchar2int));
                    z2 = false;
                }
            } else if (b2 != 92) {
                int i8 = i4;
                i4++;
                bArr2[i8] = b2;
                z2 = false;
            } else {
                z2 = true;
                z3 = true;
            }
        }
        byte[] bArr3 = new byte[i4];
        System.arraycopy(bArr2, 0, bArr3, 0, i4);
        return bArr3;
    }

    private static int indexOf(byte[] bArr, char c2, int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            if (bArr[i4] == c2) {
                return i4;
            }
        }
        return -1;
    }

    private static int indexOf(byte[] bArr, String str, int i2, int i3) {
        int iIndexOf = indexOf(bArr, str.charAt(0), i2, i3);
        if (iIndexOf >= 0) {
            for (int i4 = 1; i4 < str.length(); i4++) {
                if (bArr[iIndexOf + i4] != str.charAt(i4)) {
                    return -1;
                }
            }
        }
        return iIndexOf;
    }

    private static int findUnescaped(byte[] bArr, char c2, int i2, int i3) {
        while (i2 < i3) {
            int iIndexOf = indexOf(bArr, c2, i2, i3);
            int i4 = 0;
            int i5 = iIndexOf - 1;
            while (i5 >= i2 && bArr[i5] == 92) {
                i5--;
                i4++;
            }
            if (iIndexOf == i2 || iIndexOf == -1 || i4 % 2 == 0) {
                return iIndexOf;
            }
            i2 = iIndexOf + 1;
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:132:0x02ba, code lost:
    
        throw new javax.naming.directory.InvalidSearchFilterException("invalid attribute description");
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x040d, code lost:
    
        throw new javax.naming.directory.InvalidSearchFilterException("invalid attribute description");
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x0424, code lost:
    
        r22 = r22 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x01e0, code lost:
    
        throw new javax.naming.directory.InvalidSearchFilterException("invalid attribute description");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void encodeSimpleFilter(com.sun.jndi.ldap.BerEncoder r7, byte[] r8, int r9, int r10) throws java.io.IOException, javax.naming.NamingException {
        /*
            Method dump skipped, instructions count: 1264
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jndi.ldap.Filter.encodeSimpleFilter(com.sun.jndi.ldap.BerEncoder, byte[], int, int):void");
    }

    private static void encodeSubstringFilter(BerEncoder berEncoder, byte[] bArr, int i2, int i3, int i4, int i5) throws IOException, NamingException {
        int i6;
        berEncoder.beginSeq(164);
        berEncoder.encodeOctetString(bArr, 4, i2, i3 - i2);
        berEncoder.beginSeq(48);
        int i7 = i4;
        while (true) {
            i6 = i7;
            int iFindUnescaped = findUnescaped(bArr, '*', i6, i5);
            if (iFindUnescaped == -1) {
                break;
            }
            if (i6 == i4) {
                if (i6 < iFindUnescaped) {
                    berEncoder.encodeOctetString(unescapeFilterValue(bArr, i6, iFindUnescaped), 128);
                }
            } else if (i6 < iFindUnescaped) {
                berEncoder.encodeOctetString(unescapeFilterValue(bArr, i6, iFindUnescaped), 129);
            }
            i7 = iFindUnescaped + 1;
        }
        if (i6 < i5) {
            berEncoder.encodeOctetString(unescapeFilterValue(bArr, i6, i5), 130);
        }
        berEncoder.endSeq();
        berEncoder.endSeq();
    }

    private static void encodeComplexFilter(BerEncoder berEncoder, byte[] bArr, int i2, int[] iArr, int i3) throws IOException, NamingException {
        iArr[0] = iArr[0] + 1;
        berEncoder.beginSeq(i2);
        int[] iArrFindRightParen = findRightParen(bArr, iArr, i3);
        encodeFilterList(berEncoder, bArr, i2, iArrFindRightParen[0], iArrFindRightParen[1]);
        berEncoder.endSeq();
    }

    private static int[] findRightParen(byte[] bArr, int[] iArr, int i2) throws IOException, NamingException {
        int i3 = 1;
        boolean z2 = false;
        int i4 = iArr[0];
        while (i4 < i2 && i3 > 0) {
            if (!z2) {
                if (bArr[i4] == 40) {
                    i3++;
                } else if (bArr[i4] == 41) {
                    i3--;
                }
            }
            if (bArr[i4] == 92 && !z2) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (i3 > 0) {
                i4++;
            }
        }
        if (i3 != 0) {
            throw new InvalidSearchFilterException("Unbalanced parenthesis");
        }
        int[] iArr2 = {iArr[0], i4};
        iArr[0] = i4 + 1;
        return iArr2;
    }

    private static void encodeFilterList(BerEncoder berEncoder, byte[] bArr, int i2, int i3, int i4) throws IOException, NamingException {
        int i5 = 0;
        int[] iArr = {i3};
        while (iArr[0] < i4) {
            if (!Character.isSpaceChar((char) bArr[iArr[0]])) {
                if (i2 == 162 && i5 > 0) {
                    throw new InvalidSearchFilterException("Filter (!) cannot be followed by more than one filters");
                }
                if (bArr[iArr[0]] != 40) {
                    int[] iArrFindRightParen = findRightParen(bArr, iArr, i4);
                    int i6 = iArrFindRightParen[1] - iArrFindRightParen[0];
                    byte[] bArr2 = new byte[i6 + 2];
                    System.arraycopy(bArr, iArrFindRightParen[0], bArr2, 1, i6);
                    bArr2[0] = 40;
                    bArr2[i6 + 1] = 41;
                    encodeFilter(berEncoder, bArr2, 0, bArr2.length);
                    i5++;
                }
            }
            iArr[0] = iArr[0] + 1;
        }
    }

    private static void encodeExtensibleMatch(BerEncoder berEncoder, byte[] bArr, int i2, int i3, int i4, int i5) throws IOException, NamingException {
        boolean z2 = false;
        berEncoder.beginSeq(169);
        int iIndexOf = indexOf(bArr, ':', i2, i3);
        if (iIndexOf >= 0) {
            int iIndexOf2 = indexOf(bArr, ":dn", iIndexOf, i3);
            if (iIndexOf2 >= 0) {
                z2 = true;
            }
            int iIndexOf3 = indexOf(bArr, ':', iIndexOf + 1, i3);
            if (iIndexOf3 >= 0 || iIndexOf2 == -1) {
                if (iIndexOf2 == iIndexOf) {
                    berEncoder.encodeOctetString(bArr, 129, iIndexOf3 + 1, i3 - (iIndexOf3 + 1));
                } else if (iIndexOf2 == iIndexOf3 && iIndexOf2 >= 0) {
                    berEncoder.encodeOctetString(bArr, 129, iIndexOf + 1, iIndexOf3 - (iIndexOf + 1));
                } else {
                    berEncoder.encodeOctetString(bArr, 129, iIndexOf + 1, i3 - (iIndexOf + 1));
                }
            }
            if (iIndexOf > i2) {
                berEncoder.encodeOctetString(bArr, 130, i2, iIndexOf - i2);
            }
        } else {
            berEncoder.encodeOctetString(bArr, 130, i2, i3 - i2);
        }
        berEncoder.encodeOctetString(unescapeFilterValue(bArr, i4, i5), 131);
        berEncoder.encodeBoolean(z2, 132);
        berEncoder.endSeq();
    }

    private static void dprint(String str) {
        dprint(str, new byte[0], 0, 0);
    }

    private static void dprint(String str, byte[] bArr) {
        dprint(str, bArr, 0, bArr.length);
    }

    private static void dprint(String str, byte[] bArr, int i2, int i3) {
        String str2 = Constants.INDENT;
        int i4 = dbgIndent;
        while (true) {
            int i5 = i4;
            i4--;
            if (i5 <= 0) {
                break;
            } else {
                str2 = str2 + Constants.INDENT;
            }
        }
        System.err.print(str2 + str);
        for (int i6 = i2; i6 < i3; i6++) {
            System.err.print((char) bArr[i6]);
        }
        System.err.println();
    }
}
