package jdk.internal.dynalink.support;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/NameCodec.class */
public class NameCodec {
    private static final char ESCAPE_CHAR = '\\';
    private static final char EMPTY_ESCAPE = '=';
    private static final String EMPTY_NAME;
    private static final char EMPTY_CHAR = 65279;
    private static final int MIN_ENCODING = 36;
    private static final int MAX_ENCODING = 93;
    private static final char[] ENCODING;
    private static final int MIN_DECODING = 33;
    private static final int MAX_DECODING = 125;
    private static final char[] DECODING;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NameCodec.class.desiredAssertionStatus();
        EMPTY_NAME = "\\=";
        ENCODING = new char[58];
        DECODING = new char[93];
        addEncoding('/', '|');
        addEncoding('.', ',');
        addEncoding(';', '?');
        addEncoding('$', '%');
        addEncoding('<', '^');
        addEncoding('>', '_');
        addEncoding('[', '{');
        addEncoding(']', '}');
        addEncoding(':', '!');
        addEncoding('\\', '-');
        DECODING[28] = 65279;
    }

    private NameCodec() {
    }

    public static String encode(String name) {
        char e2;
        int l2 = name.length();
        if (l2 == 0) {
            return EMPTY_NAME;
        }
        StringBuilder b2 = null;
        int lastEscape = -1;
        for (int i2 = 0; i2 < l2; i2++) {
            int encodeIndex = name.charAt(i2) - '$';
            if (encodeIndex >= 0 && encodeIndex < ENCODING.length && (e2 = ENCODING[encodeIndex]) != 0) {
                if (b2 == null) {
                    b2 = new StringBuilder(name.length() + 3);
                    if (name.charAt(0) != '\\' && i2 > 0) {
                        b2.append(EMPTY_NAME);
                    }
                    b2.append((CharSequence) name, 0, i2);
                } else {
                    b2.append((CharSequence) name, lastEscape + 1, i2);
                }
                b2.append('\\').append(e2);
                lastEscape = i2;
            }
        }
        if (b2 == null) {
            return name.toString();
        }
        if (!$assertionsDisabled && lastEscape == -1) {
            throw new AssertionError();
        }
        b2.append((CharSequence) name, lastEscape + 1, l2);
        return b2.toString();
    }

    public static String decode(String name) {
        if (name.charAt(0) != '\\') {
            return name;
        }
        int l2 = name.length();
        if (l2 == 2 && name.charAt(1) == EMPTY_CHAR) {
            return "";
        }
        StringBuilder b2 = new StringBuilder(name.length());
        int lastEscape = -2;
        int i2 = -1;
        while (true) {
            int lastBackslash = i2;
            int nextBackslash = name.indexOf(92, lastBackslash + 1);
            if (nextBackslash == -1 || nextBackslash == l2 - 1) {
                break;
            }
            int decodeIndex = name.charAt(nextBackslash + 1) - '!';
            if (decodeIndex >= 0 && decodeIndex < DECODING.length) {
                char d2 = DECODING[decodeIndex];
                if (d2 == EMPTY_CHAR) {
                    if (nextBackslash == 0) {
                        lastEscape = 0;
                    }
                } else if (d2 != 0) {
                    b2.append((CharSequence) name, lastEscape + 2, nextBackslash).append(d2);
                    lastEscape = nextBackslash;
                }
            }
            i2 = nextBackslash;
        }
        b2.append((CharSequence) name, lastEscape + 2, l2);
        return b2.toString();
    }

    private static void addEncoding(char from, char to) {
        ENCODING[from - '$'] = to;
        DECODING[to - '!'] = from;
    }
}
