package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/ScannerSupport.class */
abstract class ScannerSupport extends IntHolder implements ErrorMessages {
    protected final char[] chars;

    /* renamed from: p, reason: collision with root package name */
    protected int f12880p;
    protected int stop;
    private int lastFetched;

    /* renamed from: c, reason: collision with root package name */
    protected int f12881c;
    private final int begin;
    private final int end;
    protected int _p;
    private static final int INT_SIGN_BIT = Integer.MIN_VALUE;

    protected ScannerSupport(char[] chars, int p2, int end) {
        this.chars = chars;
        this.begin = p2;
        this.end = end;
        reset();
    }

    protected int getBegin() {
        return this.begin;
    }

    protected int getEnd() {
        return this.end;
    }

    protected final int scanUnsignedNumber() {
        int onum;
        int last = this.f12881c;
        int num = 0;
        do {
            if (left()) {
                fetch();
                if (Character.isDigit(this.f12881c)) {
                    onum = num;
                    num = (num * 10) + EncodingHelper.digitVal(this.f12881c);
                } else {
                    unfetch();
                }
            }
            this.f12881c = last;
            return num;
        } while (((onum ^ num) & Integer.MIN_VALUE) == 0);
        return -1;
    }

    protected final int scanUnsignedHexadecimalNumber(int maxLength) {
        int onum;
        int last = this.f12881c;
        int num = 0;
        int ml = maxLength;
        do {
            if (left()) {
                int i2 = ml;
                ml--;
                if (i2 != 0) {
                    fetch();
                    if (EncodingHelper.isXDigit(this.f12881c)) {
                        onum = num;
                        int val = EncodingHelper.xdigitVal(this.f12881c);
                        num = (num << 4) + val;
                    } else {
                        unfetch();
                    }
                }
            }
            this.f12881c = last;
            return num;
        } while (((onum ^ num) & Integer.MIN_VALUE) == 0);
        return -1;
    }

    protected final int scanUnsignedOctalNumber(int maxLength) {
        int last = this.f12881c;
        int num = 0;
        int ml = maxLength;
        while (left()) {
            int i2 = ml;
            ml--;
            if (i2 == 0) {
                break;
            }
            fetch();
            if (Character.isDigit(this.f12881c) && this.f12881c < 56) {
                int onum = num;
                int val = EncodingHelper.odigitVal(this.f12881c);
                num = (num << 3) + val;
                if (((onum ^ num) & Integer.MIN_VALUE) != 0) {
                    return -1;
                }
            } else {
                unfetch();
                break;
            }
        }
        this.f12881c = last;
        return num;
    }

    protected final void reset() {
        this.f12880p = this.begin;
        this.stop = this.end;
    }

    protected final void mark() {
        this._p = this.f12880p;
    }

    protected final void restore() {
        this.f12880p = this._p;
    }

    protected final void inc() {
        this.lastFetched = this.f12880p;
        this.f12880p++;
    }

    protected final void fetch() {
        this.lastFetched = this.f12880p;
        char[] cArr = this.chars;
        int i2 = this.f12880p;
        this.f12880p = i2 + 1;
        this.f12881c = cArr[i2];
    }

    protected int fetchTo() {
        this.lastFetched = this.f12880p;
        char[] cArr = this.chars;
        int i2 = this.f12880p;
        this.f12880p = i2 + 1;
        return cArr[i2];
    }

    protected final void unfetch() {
        this.f12880p = this.lastFetched;
    }

    protected final int peek() {
        if (this.f12880p < this.stop) {
            return this.chars[this.f12880p];
        }
        return 0;
    }

    protected final boolean peekIs(int ch) {
        return peek() == ch;
    }

    protected final boolean left() {
        return this.f12880p < this.stop;
    }
}
