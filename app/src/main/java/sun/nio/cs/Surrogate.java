package sun.nio.cs;

import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

/* loaded from: rt.jar:sun/nio/cs/Surrogate.class */
public class Surrogate {
    public static final char MIN_HIGH = 55296;
    public static final char MAX_HIGH = 56319;
    public static final char MIN_LOW = 56320;
    public static final char MAX_LOW = 57343;
    public static final char MIN = 55296;
    public static final char MAX = 57343;
    public static final int UCS4_MIN = 65536;
    public static final int UCS4_MAX = 1114111;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Surrogate.class.desiredAssertionStatus();
    }

    private Surrogate() {
    }

    public static boolean isHigh(int i2) {
        return 55296 <= i2 && i2 <= 56319;
    }

    public static boolean isLow(int i2) {
        return 56320 <= i2 && i2 <= 57343;
    }

    public static boolean is(int i2) {
        return 55296 <= i2 && i2 <= 57343;
    }

    public static boolean neededFor(int i2) {
        return Character.isSupplementaryCodePoint(i2);
    }

    public static char high(int i2) {
        if ($assertionsDisabled || Character.isSupplementaryCodePoint(i2)) {
            return Character.highSurrogate(i2);
        }
        throw new AssertionError();
    }

    public static char low(int i2) {
        if ($assertionsDisabled || Character.isSupplementaryCodePoint(i2)) {
            return Character.lowSurrogate(i2);
        }
        throw new AssertionError();
    }

    public static int toUCS4(char c2, char c3) {
        if ($assertionsDisabled || (Character.isHighSurrogate(c2) && Character.isLowSurrogate(c3))) {
            return Character.toCodePoint(c2, c3);
        }
        throw new AssertionError();
    }

    /* loaded from: rt.jar:sun/nio/cs/Surrogate$Parser.class */
    public static class Parser {
        private int character;
        private CoderResult error = CoderResult.UNDERFLOW;
        private boolean isPair;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Surrogate.class.desiredAssertionStatus();
        }

        public int character() {
            if ($assertionsDisabled || this.error == null) {
                return this.character;
            }
            throw new AssertionError();
        }

        public boolean isPair() {
            if ($assertionsDisabled || this.error == null) {
                return this.isPair;
            }
            throw new AssertionError();
        }

        public int increment() {
            if ($assertionsDisabled || this.error == null) {
                return this.isPair ? 2 : 1;
            }
            throw new AssertionError();
        }

        public CoderResult error() {
            if ($assertionsDisabled || this.error != null) {
                return this.error;
            }
            throw new AssertionError();
        }

        public CoderResult unmappableResult() {
            if ($assertionsDisabled || this.error == null) {
                return CoderResult.unmappableForLength(this.isPair ? 2 : 1);
            }
            throw new AssertionError();
        }

        public int parse(char c2, CharBuffer charBuffer) {
            if (Character.isHighSurrogate(c2)) {
                if (!charBuffer.hasRemaining()) {
                    this.error = CoderResult.UNDERFLOW;
                    return -1;
                }
                char c3 = charBuffer.get();
                if (Character.isLowSurrogate(c3)) {
                    this.character = Character.toCodePoint(c2, c3);
                    this.isPair = true;
                    this.error = null;
                    return this.character;
                }
                this.error = CoderResult.malformedForLength(1);
                return -1;
            }
            if (Character.isLowSurrogate(c2)) {
                this.error = CoderResult.malformedForLength(1);
                return -1;
            }
            this.character = c2;
            this.isPair = false;
            this.error = null;
            return this.character;
        }

        public int parse(char c2, char[] cArr, int i2, int i3) {
            if (!$assertionsDisabled && cArr[i2] != c2) {
                throw new AssertionError();
            }
            if (Character.isHighSurrogate(c2)) {
                if (i3 - i2 < 2) {
                    this.error = CoderResult.UNDERFLOW;
                    return -1;
                }
                char c3 = cArr[i2 + 1];
                if (Character.isLowSurrogate(c3)) {
                    this.character = Character.toCodePoint(c2, c3);
                    this.isPair = true;
                    this.error = null;
                    return this.character;
                }
                this.error = CoderResult.malformedForLength(1);
                return -1;
            }
            if (Character.isLowSurrogate(c2)) {
                this.error = CoderResult.malformedForLength(1);
                return -1;
            }
            this.character = c2;
            this.isPair = false;
            this.error = null;
            return this.character;
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/Surrogate$Generator.class */
    public static class Generator {
        private CoderResult error = CoderResult.OVERFLOW;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Surrogate.class.desiredAssertionStatus();
        }

        public CoderResult error() {
            if ($assertionsDisabled || this.error != null) {
                return this.error;
            }
            throw new AssertionError();
        }

        public int generate(int i2, int i3, CharBuffer charBuffer) {
            if (Character.isBmpCodePoint(i2)) {
                char c2 = (char) i2;
                if (Character.isSurrogate(c2)) {
                    this.error = CoderResult.malformedForLength(i3);
                    return -1;
                }
                if (charBuffer.remaining() < 1) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                }
                charBuffer.put(c2);
                this.error = null;
                return 1;
            }
            if (Character.isValidCodePoint(i2)) {
                if (charBuffer.remaining() < 2) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                }
                charBuffer.put(Character.highSurrogate(i2));
                charBuffer.put(Character.lowSurrogate(i2));
                this.error = null;
                return 2;
            }
            this.error = CoderResult.unmappableForLength(i3);
            return -1;
        }

        public int generate(int i2, int i3, char[] cArr, int i4, int i5) {
            if (Character.isBmpCodePoint(i2)) {
                char c2 = (char) i2;
                if (Character.isSurrogate(c2)) {
                    this.error = CoderResult.malformedForLength(i3);
                    return -1;
                }
                if (i5 - i4 < 1) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                }
                cArr[i4] = c2;
                this.error = null;
                return 1;
            }
            if (Character.isValidCodePoint(i2)) {
                if (i5 - i4 < 2) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                }
                cArr[i4] = Character.highSurrogate(i2);
                cArr[i4 + 1] = Character.lowSurrogate(i2);
                this.error = null;
                return 2;
            }
            this.error = CoderResult.unmappableForLength(i3);
            return -1;
        }
    }
}
