package org.slf4j.helpers;

import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;

/* loaded from: jssc.jar:org/slf4j/helpers/MessageFormatter.class */
public final class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static final FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg});
    }

    public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
        return arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        Throwable throwableCandidate = getThrowableCandidate(argArray);
        Object[] args = argArray;
        if (throwableCandidate != null) {
            args = trimmedCopy(argArray);
        }
        return arrayFormat(messagePattern, args, throwableCandidate);
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
        int i2;
        int i3;
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwable);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i4 = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int L2 = 0;
        while (L2 < argArray.length) {
            int j2 = messagePattern.indexOf(DELIM_STR, i4);
            if (j2 == -1) {
                if (i4 == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwable);
                }
                sbuf.append((CharSequence) messagePattern, i4, messagePattern.length());
                return new FormattingTuple(sbuf.toString(), argArray, throwable);
            }
            if (isEscapedDelimeter(messagePattern, j2)) {
                if (!isDoubleEscaped(messagePattern, j2)) {
                    L2--;
                    sbuf.append((CharSequence) messagePattern, i4, j2 - 1);
                    sbuf.append('{');
                    i2 = j2;
                    i3 = 1;
                } else {
                    sbuf.append((CharSequence) messagePattern, i4, j2 - 1);
                    deeplyAppendParameter(sbuf, argArray[L2], new HashMap());
                    i2 = j2;
                    i3 = 2;
                }
            } else {
                sbuf.append((CharSequence) messagePattern, i4, j2);
                deeplyAppendParameter(sbuf, argArray[L2], new HashMap());
                i2 = j2;
                i3 = 2;
            }
            i4 = i2 + i3;
            L2++;
        }
        sbuf.append((CharSequence) messagePattern, i4, messagePattern.length());
        return new FormattingTuple(sbuf.toString(), argArray, throwable);
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == '\\') {
            return true;
        }
        return false;
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\') {
            return true;
        }
        return false;
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o2, Map<Object[], Object> seenMap) {
        if (o2 == null) {
            sbuf.append(FXMLLoader.NULL_KEYWORD);
            return;
        }
        if (!o2.getClass().isArray()) {
            safeObjectAppend(sbuf, o2);
            return;
        }
        if (o2 instanceof boolean[]) {
            booleanArrayAppend(sbuf, (boolean[]) o2);
            return;
        }
        if (o2 instanceof byte[]) {
            byteArrayAppend(sbuf, (byte[]) o2);
            return;
        }
        if (o2 instanceof char[]) {
            charArrayAppend(sbuf, (char[]) o2);
            return;
        }
        if (o2 instanceof short[]) {
            shortArrayAppend(sbuf, (short[]) o2);
            return;
        }
        if (o2 instanceof int[]) {
            intArrayAppend(sbuf, (int[]) o2);
            return;
        }
        if (o2 instanceof long[]) {
            longArrayAppend(sbuf, (long[]) o2);
            return;
        }
        if (o2 instanceof float[]) {
            floatArrayAppend(sbuf, (float[]) o2);
        } else if (o2 instanceof double[]) {
            doubleArrayAppend(sbuf, (double[]) o2);
        } else {
            objectArrayAppend(sbuf, (Object[]) o2, seenMap);
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o2) {
        try {
            String oAsString = o2.toString();
            sbuf.append(oAsString);
        } catch (Throwable t2) {
            Util.report("SLF4J: Failed toString() invocation on an object of type [" + o2.getClass().getName() + "]", t2);
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a2, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a2)) {
            seenMap.put(a2, null);
            int len = a2.length;
            for (int i2 = 0; i2 < len; i2++) {
                deeplyAppendParameter(sbuf, a2[i2], seenMap);
                if (i2 != len - 1) {
                    sbuf.append(", ");
                }
            }
            seenMap.remove(a2);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append(a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append((int) a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append(a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append((int) a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append(a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append(a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append(a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i2 = 0; i2 < len; i2++) {
            sbuf.append(a2[i2]);
            if (i2 != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    public static Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }
        return null;
    }

    public static Object[] trimmedCopy(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        int trimmedLen = argArray.length - 1;
        Object[] trimmed = new Object[trimmedLen];
        if (trimmedLen > 0) {
            System.arraycopy(argArray, 0, trimmed, 0, trimmedLen);
        }
        return trimmed;
    }
}
