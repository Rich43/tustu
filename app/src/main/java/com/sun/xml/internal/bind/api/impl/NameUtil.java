package com.sun.xml.internal.bind.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/impl/NameUtil.class */
class NameUtil {
    protected static final int UPPER_LETTER = 0;
    protected static final int LOWER_LETTER = 1;
    protected static final int OTHER_LETTER = 2;
    protected static final int DIGIT = 3;
    protected static final int OTHER = 4;
    private static final byte[] actionTable = new byte[25];
    private static final byte ACTION_CHECK_PUNCT = 0;
    private static final byte ACTION_CHECK_C2 = 1;
    private static final byte ACTION_BREAK = 2;
    private static final byte ACTION_NOBREAK = 3;

    NameUtil() {
    }

    protected boolean isPunct(char c2) {
        return c2 == '-' || c2 == '.' || c2 == ':' || c2 == '_' || c2 == 183 || c2 == 903 || c2 == 1757 || c2 == 1758;
    }

    protected static boolean isDigit(char c2) {
        return (c2 >= '0' && c2 <= '9') || Character.isDigit(c2);
    }

    protected static boolean isUpper(char c2) {
        return (c2 >= 'A' && c2 <= 'Z') || Character.isUpperCase(c2);
    }

    protected static boolean isLower(char c2) {
        return (c2 >= 'a' && c2 <= 'z') || Character.isLowerCase(c2);
    }

    protected boolean isLetter(char c2) {
        return (c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z') || Character.isLetter(c2);
    }

    private String toLowerCase(String s2) {
        return s2.toLowerCase(Locale.ENGLISH);
    }

    private String toUpperCase(char c2) {
        return String.valueOf(c2).toUpperCase(Locale.ENGLISH);
    }

    private String toUpperCase(String s2) {
        return s2.toUpperCase(Locale.ENGLISH);
    }

    public String capitalize(String s2) {
        if (!isLower(s2.charAt(0))) {
            return s2;
        }
        StringBuilder sb = new StringBuilder(s2.length());
        sb.append(toUpperCase(s2.charAt(0)));
        sb.append(toLowerCase(s2.substring(1)));
        return sb.toString();
    }

    private int nextBreak(String s2, int start) {
        int n2 = s2.length();
        int t1 = classify(s2.charAt(start));
        for (int i2 = start + 1; i2 < n2; i2++) {
            int t0 = t1;
            char c1 = s2.charAt(i2);
            t1 = classify(c1);
            switch (actionTable[(t0 * 5) + t1]) {
                case 0:
                    if (isPunct(c1)) {
                        return i2;
                    }
                    break;
                case 1:
                    if (i2 < n2 - 1) {
                        char c2 = s2.charAt(i2 + 1);
                        if (isLower(c2)) {
                            return i2;
                        }
                        break;
                    } else {
                        continue;
                    }
                case 2:
                    return i2;
            }
        }
        return -1;
    }

    static {
        for (int t0 = 0; t0 < 5; t0++) {
            for (int t1 = 0; t1 < 5; t1++) {
                actionTable[(t0 * 5) + t1] = decideAction(t0, t1);
            }
        }
    }

    private static byte decideAction(int t0, int t1) {
        if (t0 == 4 && t1 == 4) {
            return (byte) 0;
        }
        if (!xor(t0 == 3, t1 == 3)) {
            return (byte) 2;
        }
        if (t0 == 1 && t1 != 1) {
            return (byte) 2;
        }
        if (!xor(t0 <= 2, t1 <= 2)) {
            return (byte) 2;
        }
        if (xor(t0 == 2, t1 == 2)) {
            return (t0 == 0 && t1 == 0) ? (byte) 1 : (byte) 3;
        }
        return (byte) 2;
    }

    private static boolean xor(boolean x2, boolean y2) {
        return (x2 && y2) || !(x2 || y2);
    }

    protected int classify(char c0) {
        switch (Character.getType(c0)) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
            case 4:
            case 5:
                return 2;
            case 6:
            case 7:
            case 8:
            default:
                return 4;
            case 9:
                return 3;
        }
    }

    public List<String> toWordList(String s2) {
        ArrayList<String> ss = new ArrayList<>();
        int n2 = s2.length();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= n2) {
                break;
            }
            while (i3 < n2 && isPunct(s2.charAt(i3))) {
                i3++;
            }
            if (i3 >= n2) {
                break;
            }
            int b2 = nextBreak(s2, i3);
            String w2 = b2 == -1 ? s2.substring(i3) : s2.substring(i3, b2);
            ss.add(escape(capitalize(w2)));
            if (b2 == -1) {
                break;
            }
            i2 = b2;
        }
        return ss;
    }

    protected String toMixedCaseName(List<String> ss, boolean startUpper) {
        StringBuilder sb = new StringBuilder();
        if (!ss.isEmpty()) {
            sb.append(startUpper ? ss.get(0) : toLowerCase(ss.get(0)));
            for (int i2 = 1; i2 < ss.size(); i2++) {
                sb.append(ss.get(i2));
            }
        }
        return sb.toString();
    }

    protected String toMixedCaseVariableName(String[] ss, boolean startUpper, boolean cdrUpper) {
        if (cdrUpper) {
            for (int i2 = 1; i2 < ss.length; i2++) {
                ss[i2] = capitalize(ss[i2]);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (ss.length > 0) {
            sb.append(startUpper ? ss[0] : toLowerCase(ss[0]));
            for (int i3 = 1; i3 < ss.length; i3++) {
                sb.append(ss[i3]);
            }
        }
        return sb.toString();
    }

    public String toConstantName(String s2) {
        return toConstantName(toWordList(s2));
    }

    public String toConstantName(List<String> ss) {
        StringBuilder sb = new StringBuilder();
        if (!ss.isEmpty()) {
            sb.append(toUpperCase(ss.get(0)));
            for (int i2 = 1; i2 < ss.size(); i2++) {
                sb.append('_');
                sb.append(toUpperCase(ss.get(i2)));
            }
        }
        return sb.toString();
    }

    public static void escape(StringBuilder sb, String s2, int start) {
        int n2 = s2.length();
        for (int i2 = start; i2 < n2; i2++) {
            char c2 = s2.charAt(i2);
            if (Character.isJavaIdentifierPart(c2)) {
                sb.append(c2);
            } else {
                sb.append('_');
                if (c2 <= 15) {
                    sb.append("000");
                } else if (c2 <= 255) {
                    sb.append("00");
                } else if (c2 <= 4095) {
                    sb.append('0');
                }
                sb.append(Integer.toString(c2, 16));
            }
        }
    }

    private static String escape(String s2) {
        int n2 = s2.length();
        for (int i2 = 0; i2 < n2; i2++) {
            if (!Character.isJavaIdentifierPart(s2.charAt(i2))) {
                StringBuilder sb = new StringBuilder(s2.substring(0, i2));
                escape(sb, s2, i2);
                return sb.toString();
            }
        }
        return s2;
    }
}
