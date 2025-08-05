package javax.xml.bind;

/* loaded from: rt.jar:javax/xml/bind/WhiteSpaceProcessor.class */
abstract class WhiteSpaceProcessor {
    WhiteSpaceProcessor() {
    }

    public static String replace(String text) {
        return replace((CharSequence) text).toString();
    }

    public static CharSequence replace(CharSequence text) {
        int i2 = text.length() - 1;
        while (i2 >= 0 && !isWhiteSpaceExceptSpace(text.charAt(i2))) {
            i2--;
        }
        if (i2 < 0) {
            return text;
        }
        StringBuilder buf = new StringBuilder(text);
        int i3 = i2;
        buf.setCharAt(i3, ' ');
        for (int i4 = i2 - 1; i4 >= 0; i4--) {
            if (isWhiteSpaceExceptSpace(buf.charAt(i4))) {
                buf.setCharAt(i4, ' ');
            }
        }
        return new String(buf);
    }

    public static CharSequence trim(CharSequence text) {
        int len = text.length();
        int start = 0;
        while (start < len && isWhiteSpace(text.charAt(start))) {
            start++;
        }
        int end = len - 1;
        while (end > start && isWhiteSpace(text.charAt(end))) {
            end--;
        }
        if (start == 0 && end == len - 1) {
            return text;
        }
        return text.subSequence(start, end + 1);
    }

    public static String collapse(String text) {
        return collapse((CharSequence) text).toString();
    }

    public static CharSequence collapse(CharSequence text) {
        int len = text.length();
        int s2 = 0;
        while (s2 < len && !isWhiteSpace(text.charAt(s2))) {
            s2++;
        }
        if (s2 == len) {
            return text;
        }
        StringBuilder result = new StringBuilder(len);
        if (s2 != 0) {
            for (int i2 = 0; i2 < s2; i2++) {
                result.append(text.charAt(i2));
            }
            result.append(' ');
        }
        boolean inStripMode = true;
        for (int i3 = s2 + 1; i3 < len; i3++) {
            char ch = text.charAt(i3);
            boolean b2 = isWhiteSpace(ch);
            if (!inStripMode || !b2) {
                inStripMode = b2;
                if (inStripMode) {
                    result.append(' ');
                } else {
                    result.append(ch);
                }
            }
        }
        int len2 = result.length();
        if (len2 > 0 && result.charAt(len2 - 1) == ' ') {
            result.setLength(len2 - 1);
        }
        return result;
    }

    public static final boolean isWhiteSpace(CharSequence s2) {
        for (int i2 = s2.length() - 1; i2 >= 0; i2--) {
            if (!isWhiteSpace(s2.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    public static final boolean isWhiteSpace(char ch) {
        if (ch > ' ') {
            return false;
        }
        return ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ';
    }

    protected static final boolean isWhiteSpaceExceptSpace(char ch) {
        if (ch >= ' ') {
            return false;
        }
        return ch == '\t' || ch == '\n' || ch == '\r';
    }
}
