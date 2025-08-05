package sun.invoke.util;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/invoke/util/BytecodeName.class */
public class BytecodeName {
    static char ESCAPE_C;
    static char NULL_ESCAPE_C;
    static String NULL_ESCAPE;
    static final String DANGEROUS_CHARS = "\\/.;:$[]<>";
    static final String REPLACEMENT_CHARS = "-|,?!%{}^_";
    static final int DANGEROUS_CHAR_FIRST_INDEX = 1;
    static char[] DANGEROUS_CHARS_A;
    static char[] REPLACEMENT_CHARS_A;
    static final Character[] DANGEROUS_CHARS_CA;
    static final long[] SPECIAL_BITMAP;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BytecodeName.class.desiredAssertionStatus();
        ESCAPE_C = '\\';
        NULL_ESCAPE_C = '=';
        NULL_ESCAPE = ESCAPE_C + "" + NULL_ESCAPE_C;
        DANGEROUS_CHARS_A = DANGEROUS_CHARS.toCharArray();
        REPLACEMENT_CHARS_A = REPLACEMENT_CHARS.toCharArray();
        Character[] chArr = new Character[DANGEROUS_CHARS.length()];
        for (int i2 = 0; i2 < chArr.length; i2++) {
            chArr[i2] = Character.valueOf(DANGEROUS_CHARS.charAt(i2));
        }
        DANGEROUS_CHARS_CA = chArr;
        SPECIAL_BITMAP = new long[2];
        for (char c2 : "\\/.;:$[]<>-|,?!%{}^_".toCharArray()) {
            long[] jArr = SPECIAL_BITMAP;
            int i3 = c2 >>> 6;
            jArr[i3] = jArr[i3] | (1 << c2);
        }
    }

    private BytecodeName() {
    }

    public static String toBytecodeName(String str) {
        String strMangle = mangle(str);
        if (!$assertionsDisabled && strMangle != str && !looksMangled(strMangle)) {
            throw new AssertionError((Object) strMangle);
        }
        if ($assertionsDisabled || str.equals(toSourceName(strMangle))) {
            return strMangle;
        }
        throw new AssertionError((Object) str);
    }

    public static String toSourceName(String str) throws IllegalArgumentException {
        checkSafeBytecodeName(str);
        String strDemangle = str;
        if (looksMangled(str)) {
            strDemangle = demangle(str);
            if (!$assertionsDisabled && !str.equals(mangle(strDemangle))) {
                throw new AssertionError((Object) (str + " => " + strDemangle + " => " + mangle(strDemangle)));
            }
        }
        return strDemangle;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003c A[PHI: r13
  0x003c: PHI (r13v1 int) = (r13v0 int), (r13v2 int) binds: [B:9:0x0023, B:11:0x0036] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Object[] parseBytecodeName(java.lang.String r6) {
        /*
            r0 = r6
            int r0 = r0.length()
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
        L9:
            r0 = r9
            r1 = 1
            if (r0 > r1) goto Laf
            r0 = 0
            r10 = r0
            r0 = 0
            r11 = r0
            r0 = 0
            r12 = r0
        L17:
            r0 = r12
            r1 = r7
            if (r0 > r1) goto L82
            r0 = -1
            r13 = r0
            r0 = r12
            r1 = r7
            if (r0 >= r1) goto L3c
            java.lang.String r0 = "\\/.;:$[]<>"
            r1 = r6
            r2 = r12
            char r1 = r1.charAt(r2)
            int r0 = r0.indexOf(r1)
            r13 = r0
            r0 = r13
            r1 = 1
            if (r0 >= r1) goto L3c
            goto L7c
        L3c:
            r0 = r11
            r1 = r12
            if (r0 >= r1) goto L5f
            r0 = r9
            if (r0 == 0) goto L56
            r0 = r8
            r1 = r10
            r2 = r6
            r3 = r11
            r4 = r12
            java.lang.String r2 = r2.substring(r3, r4)
            java.lang.String r2 = toSourceName(r2)
            r0[r1] = r2
        L56:
            int r10 = r10 + 1
            r0 = r12
            r1 = 1
            int r0 = r0 + r1
            r11 = r0
        L5f:
            r0 = r13
            r1 = 1
            if (r0 < r1) goto L7c
            r0 = r9
            if (r0 == 0) goto L73
            r0 = r8
            r1 = r10
            java.lang.Character[] r2 = sun.invoke.util.BytecodeName.DANGEROUS_CHARS_CA
            r3 = r13
            r2 = r2[r3]
            r0[r1] = r2
        L73:
            int r10 = r10 + 1
            r0 = r12
            r1 = 1
            int r0 = r0 + r1
            r11 = r0
        L7c:
            int r12 = r12 + 1
            goto L17
        L82:
            r0 = r9
            if (r0 == 0) goto L89
            goto Laf
        L89:
            r0 = r10
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r8 = r0
            r0 = r10
            r1 = 1
            if (r0 > r1) goto La9
            r0 = r11
            if (r0 != 0) goto La9
            r0 = r10
            if (r0 == 0) goto Laf
            r0 = r8
            r1 = 0
            r2 = r6
            java.lang.String r2 = toSourceName(r2)
            r0[r1] = r2
            goto Laf
        La9:
            int r9 = r9 + 1
            goto L9
        Laf:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.invoke.util.BytecodeName.parseBytecodeName(java.lang.String):java.lang.Object[]");
    }

    public static String unparseBytecodeName(Object[] objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            Object obj = objArr[i2];
            if (obj instanceof String) {
                String bytecodeName = toBytecodeName((String) obj);
                if (i2 == 0 && objArr.length == 1) {
                    return bytecodeName;
                }
                if (bytecodeName != obj) {
                    if (objArr == objArr) {
                        objArr = (Object[]) objArr.clone();
                    }
                    objArr[i2] = bytecodeName;
                }
            }
        }
        return appendAll(objArr);
    }

    private static String appendAll(Object[] objArr) {
        if (objArr.length <= 1) {
            if (objArr.length == 1) {
                return String.valueOf(objArr[0]);
            }
            return "";
        }
        int length = 0;
        for (Object obj : objArr) {
            if (obj instanceof String) {
                length += String.valueOf(obj).length();
            } else {
                length++;
            }
        }
        StringBuilder sb = new StringBuilder(length);
        for (Object obj2 : objArr) {
            sb.append(obj2);
        }
        return sb.toString();
    }

    public static String toDisplayName(String str) {
        Object[] bytecodeName = parseBytecodeName(str);
        for (int i2 = 0; i2 < bytecodeName.length; i2++) {
            if (bytecodeName[i2] instanceof String) {
                String str2 = (String) bytecodeName[i2];
                if (!isJavaIdent(str2) || str2.indexOf(36) >= 0) {
                    bytecodeName[i2] = quoteDisplay(str2);
                }
            }
        }
        return appendAll(bytecodeName);
    }

    private static boolean isJavaIdent(String str) {
        int length = str.length();
        if (length == 0 || !Character.isJavaIdentifierStart(str.charAt(0))) {
            return false;
        }
        for (int i2 = 1; i2 < length; i2++) {
            if (!Character.isJavaIdentifierPart(str.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    private static String quoteDisplay(String str) {
        return PdfOps.SINGLE_QUOTE_TOKEN + str.replaceAll("['\\\\]", "\\\\$0") + PdfOps.SINGLE_QUOTE_TOKEN;
    }

    private static void checkSafeBytecodeName(String str) throws IllegalArgumentException {
        if (!isSafeBytecodeName(str)) {
            throw new IllegalArgumentException(str);
        }
    }

    public static boolean isSafeBytecodeName(String str) {
        if (str.length() == 0) {
            return false;
        }
        for (char c2 : DANGEROUS_CHARS_A) {
            if (c2 != ESCAPE_C && str.indexOf(c2) >= 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSafeBytecodeChar(char c2) {
        return DANGEROUS_CHARS.indexOf(c2) < 1;
    }

    private static boolean looksMangled(String str) {
        return str.charAt(0) == ESCAPE_C;
    }

    private static String mangle(String str) {
        if (str.length() == 0) {
            return NULL_ESCAPE;
        }
        StringBuilder sb = null;
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            boolean zIsDangerous = false;
            if (cCharAt == ESCAPE_C) {
                if (i2 + 1 < length) {
                    char cCharAt2 = str.charAt(i2 + 1);
                    if ((i2 == 0 && cCharAt2 == NULL_ESCAPE_C) || cCharAt2 != originalOfReplacement(cCharAt2)) {
                        zIsDangerous = true;
                    }
                }
            } else {
                zIsDangerous = isDangerous(cCharAt);
            }
            if (!zIsDangerous) {
                if (sb != null) {
                    sb.append(cCharAt);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder(str.length() + 10);
                    if (str.charAt(0) != ESCAPE_C && i2 > 0) {
                        sb.append(NULL_ESCAPE);
                    }
                    sb.append(str.substring(0, i2));
                }
                sb.append(ESCAPE_C);
                sb.append(replacementOf(cCharAt));
            }
        }
        return sb != null ? sb.toString() : str;
    }

    private static String demangle(String str) {
        char cCharAt;
        char cOriginalOfReplacement;
        StringBuilder sb = null;
        int i2 = 0;
        if (str.startsWith(NULL_ESCAPE)) {
            i2 = 2;
        }
        int i3 = i2;
        int length = str.length();
        while (i3 < length) {
            char cCharAt2 = str.charAt(i3);
            if (cCharAt2 == ESCAPE_C && i3 + 1 < length && (cOriginalOfReplacement = originalOfReplacement((cCharAt = str.charAt(i3 + 1)))) != cCharAt) {
                if (sb == null) {
                    sb = new StringBuilder(str.length());
                    sb.append(str.substring(i2, i3));
                }
                i3++;
                cCharAt2 = cOriginalOfReplacement;
            }
            if (sb != null) {
                sb.append(cCharAt2);
            }
            i3++;
        }
        return sb != null ? sb.toString() : str.substring(i2);
    }

    static boolean isSpecial(char c2) {
        return (c2 >>> 6) < SPECIAL_BITMAP.length && ((SPECIAL_BITMAP[c2 >>> 6] >> c2) & 1) != 0;
    }

    static char replacementOf(char c2) {
        int iIndexOf;
        if (isSpecial(c2) && (iIndexOf = DANGEROUS_CHARS.indexOf(c2)) >= 0) {
            return REPLACEMENT_CHARS.charAt(iIndexOf);
        }
        return c2;
    }

    static char originalOfReplacement(char c2) {
        int iIndexOf;
        if (isSpecial(c2) && (iIndexOf = REPLACEMENT_CHARS.indexOf(c2)) >= 0) {
            return DANGEROUS_CHARS.charAt(iIndexOf);
        }
        return c2;
    }

    static boolean isDangerous(char c2) {
        return isSpecial(c2) && DANGEROUS_CHARS.indexOf(c2) >= 1;
    }

    static int indexOfDangerousChar(String str, int i2) {
        int length = str.length();
        for (int i3 = i2; i3 < length; i3++) {
            if (isDangerous(str.charAt(i3))) {
                return i3;
            }
        }
        return -1;
    }

    static int lastIndexOfDangerousChar(String str, int i2) {
        for (int iMin = Math.min(i2, str.length() - 1); iMin >= 0; iMin--) {
            if (isDangerous(str.charAt(iMin))) {
                return iMin;
            }
        }
        return -1;
    }
}
