package sun.font;

/* loaded from: rt.jar:sun/font/ScriptRun.class */
public final class ScriptRun {
    private char[] text;
    private int textStart;
    private int textLimit;
    private int scriptStart;
    private int scriptLimit;
    private int scriptCode;
    private int[] stack;
    private int parenSP;
    static final int SURROGATE_START = 65536;
    static final int LEAD_START = 55296;
    static final int LEAD_LIMIT = 56320;
    static final int TAIL_START = 56320;
    static final int TAIL_LIMIT = 57344;
    static final int LEAD_SURROGATE_SHIFT = 10;
    static final int SURROGATE_OFFSET = -56613888;
    static final int DONE = -1;
    private static int[] pairedChars = {40, 41, 60, 62, 91, 93, 123, 125, 171, 187, 8216, 8217, 8220, 8221, 8249, 8250, 12296, 12297, 12298, 12299, 12300, 12301, 12302, 12303, 12304, 12305, 12308, 12309, 12310, 12311, 12312, 12313, 12314, 12315};
    private static final int pairedCharPower = 1 << highBit(pairedChars.length);
    private static final int pairedCharExtra = pairedChars.length - pairedCharPower;

    public ScriptRun() {
    }

    public ScriptRun(char[] cArr, int i2, int i3) {
        init(cArr, i2, i3);
    }

    public void init(char[] cArr, int i2, int i3) {
        if (cArr == null || i2 < 0 || i3 < 0 || i3 > cArr.length - i2) {
            throw new IllegalArgumentException();
        }
        this.text = cArr;
        this.textStart = i2;
        this.textLimit = i2 + i3;
        this.scriptStart = this.textStart;
        this.scriptLimit = this.textStart;
        this.scriptCode = -1;
        this.parenSP = 0;
    }

    public final int getScriptStart() {
        return this.scriptStart;
    }

    public final int getScriptLimit() {
        return this.scriptLimit;
    }

    public final int getScriptCode() {
        return this.scriptCode;
    }

    public final boolean next() {
        int i2 = this.parenSP;
        if (this.scriptLimit >= this.textLimit) {
            return false;
        }
        this.scriptCode = 0;
        this.scriptStart = this.scriptLimit;
        while (true) {
            int iNextCodePoint = nextCodePoint();
            if (iNextCodePoint != -1) {
                int script = ScriptRunData.getScript(iNextCodePoint);
                int pairIndex = script == 0 ? getPairIndex(iNextCodePoint) : -1;
                if (pairIndex >= 0) {
                    if ((pairIndex & 1) == 0) {
                        if (this.stack == null) {
                            this.stack = new int[32];
                        } else if (this.parenSP == this.stack.length) {
                            int[] iArr = new int[this.stack.length + 32];
                            System.arraycopy(this.stack, 0, iArr, 0, this.stack.length);
                            this.stack = iArr;
                        }
                        int[] iArr2 = this.stack;
                        int i3 = this.parenSP;
                        this.parenSP = i3 + 1;
                        iArr2[i3] = pairIndex;
                        int[] iArr3 = this.stack;
                        int i4 = this.parenSP;
                        this.parenSP = i4 + 1;
                        iArr3[i4] = this.scriptCode;
                    } else if (this.parenSP > 0) {
                        int i5 = pairIndex & (-2);
                        do {
                            int i6 = this.parenSP - 2;
                            this.parenSP = i6;
                            if (i6 < 0) {
                                break;
                            }
                        } while (this.stack[this.parenSP] != i5);
                        if (this.parenSP >= 0) {
                            script = this.stack[this.parenSP + 1];
                        } else {
                            this.parenSP = 0;
                        }
                        if (this.parenSP < i2) {
                            i2 = this.parenSP;
                        }
                    }
                }
                if (sameScript(this.scriptCode, script)) {
                    if (this.scriptCode <= 1 && script > 1) {
                        this.scriptCode = script;
                        while (i2 < this.parenSP) {
                            this.stack[i2 + 1] = this.scriptCode;
                            i2 += 2;
                        }
                    }
                    if (pairIndex > 0 && (pairIndex & 1) != 0 && this.parenSP > 0) {
                        this.parenSP -= 2;
                    }
                } else {
                    pushback(iNextCodePoint);
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [int] */
    private final int nextCodePoint() {
        char c2;
        if (this.scriptLimit >= this.textLimit) {
            return -1;
        }
        char[] cArr = this.text;
        int i2 = this.scriptLimit;
        this.scriptLimit = i2 + 1;
        char c3 = cArr[i2];
        if (c3 >= 55296 && c3 < 56320 && this.scriptLimit < this.textLimit && (c2 = this.text[this.scriptLimit]) >= 56320 && c2 < TAIL_LIMIT) {
            this.scriptLimit++;
            c3 = (c3 << '\n') + c2 + SURROGATE_OFFSET;
        }
        return c3;
    }

    private final void pushback(int i2) {
        if (i2 >= 0) {
            if (i2 >= 65536) {
                this.scriptLimit -= 2;
            } else {
                this.scriptLimit--;
            }
        }
    }

    private static boolean sameScript(int i2, int i3) {
        return i2 == i3 || i2 <= 1 || i3 <= 1;
    }

    private static final byte highBit(int i2) {
        if (i2 <= 0) {
            return (byte) -32;
        }
        byte b2 = 0;
        if (i2 >= 65536) {
            i2 >>= 16;
            b2 = (byte) (0 + 16);
        }
        if (i2 >= 256) {
            i2 >>= 8;
            b2 = (byte) (b2 + 8);
        }
        if (i2 >= 16) {
            i2 >>= 4;
            b2 = (byte) (b2 + 4);
        }
        if (i2 >= 4) {
            i2 >>= 2;
            b2 = (byte) (b2 + 2);
        }
        if (i2 >= 2) {
            int i3 = i2 >> 1;
            b2 = (byte) (b2 + 1);
        }
        return b2;
    }

    private static int getPairIndex(int i2) {
        int i3 = pairedCharPower;
        int i4 = 0;
        if (i2 >= pairedChars[pairedCharExtra]) {
            i4 = pairedCharExtra;
        }
        while (i3 > 1) {
            i3 >>= 1;
            if (i2 >= pairedChars[i4 + i3]) {
                i4 += i3;
            }
        }
        if (pairedChars[i4] != i2) {
            i4 = -1;
        }
        return i4;
    }
}
