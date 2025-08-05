package jdk.nashorn.internal.runtime.regexp.joni;

import com.sun.org.apache.xalan.internal.templates.Constants;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/CodeRangeBuffer.class */
public final class CodeRangeBuffer implements Cloneable {
    private static final int INIT_MULTI_BYTE_RANGE_SIZE = 5;
    private static final int ALL_MULTI_BYTE_RANGE = Integer.MAX_VALUE;

    /* renamed from: p, reason: collision with root package name */
    int[] f12878p;
    int used;

    public CodeRangeBuffer() {
        this.f12878p = new int[5];
        writeCodePoint(0, 0);
    }

    public boolean isInCodeRange(int code) {
        int low = 0;
        int n2 = this.f12878p[0];
        int high = n2;
        while (low < high) {
            int x2 = (low + high) >> 1;
            if (code > this.f12878p[(x2 << 1) + 2]) {
                low = x2 + 1;
            } else {
                high = x2;
            }
        }
        return low < n2 && code >= this.f12878p[(low << 1) + 1];
    }

    private CodeRangeBuffer(CodeRangeBuffer orig) {
        this.f12878p = new int[orig.f12878p.length];
        System.arraycopy(orig.f12878p, 0, this.f12878p, 0, this.f12878p.length);
        this.used = orig.used;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("CodeRange");
        buf.append("\n  used: ").append(this.used);
        buf.append("\n  code point: ").append(this.f12878p[0]);
        buf.append("\n  ranges: ");
        for (int i2 = 0; i2 < this.f12878p[0]; i2++) {
            buf.append("[").append(rangeNumToString(this.f12878p[(i2 * 2) + 1])).append(Constants.ATTRVAL_PARENT).append(rangeNumToString(this.f12878p[(i2 * 2) + 2])).append("]");
            if (i2 > 0 && i2 % 6 == 0) {
                buf.append("\n          ");
            }
        }
        return buf.toString();
    }

    private static String rangeNumToString(int num) {
        return "0x" + Integer.toString(num, 16);
    }

    public void expand(int low) {
        int length = this.f12878p.length;
        do {
            length <<= 1;
        } while (length < low);
        int[] tmp = new int[length];
        System.arraycopy(this.f12878p, 0, tmp, 0, this.used);
        this.f12878p = tmp;
    }

    public void ensureSize(int size) {
        int length;
        int length2 = this.f12878p.length;
        while (true) {
            length = length2;
            if (length >= size) {
                break;
            } else {
                length2 = length << 1;
            }
        }
        if (this.f12878p.length != length) {
            int[] tmp = new int[length];
            System.arraycopy(this.f12878p, 0, tmp, 0, this.used);
            this.f12878p = tmp;
        }
    }

    private void moveRight(int from, int to, int n2) {
        if (to + n2 > this.f12878p.length) {
            expand(to + n2);
        }
        System.arraycopy(this.f12878p, from, this.f12878p, to, n2);
        if (to + n2 > this.used) {
            this.used = to + n2;
        }
    }

    protected void moveLeft(int from, int to, int n2) {
        System.arraycopy(this.f12878p, from, this.f12878p, to, n2);
    }

    private void moveLeftAndReduce(int from, int to) {
        System.arraycopy(this.f12878p, from, this.f12878p, to, this.used - from);
        this.used -= from - to;
    }

    public void writeCodePoint(int pos, int b2) {
        int u2 = pos + 1;
        if (this.f12878p.length < u2) {
            expand(u2);
        }
        this.f12878p[pos] = b2;
        if (this.used < u2) {
            this.used = u2;
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CodeRangeBuffer m4907clone() {
        return new CodeRangeBuffer(this);
    }

    public static CodeRangeBuffer addCodeRangeToBuff(CodeRangeBuffer pbufp, int fromp, int top) {
        int from = fromp;
        int to = top;
        CodeRangeBuffer pbuf = pbufp;
        if (from > to) {
            from = to;
            to = from;
        }
        if (pbuf == null) {
            pbuf = new CodeRangeBuffer();
        }
        int[] p2 = pbuf.f12878p;
        int n2 = p2[0];
        int low = 0;
        int bound = n2;
        while (low < bound) {
            int x2 = (low + bound) >>> 1;
            if (from > p2[(x2 * 2) + 2]) {
                low = x2 + 1;
            } else {
                bound = x2;
            }
        }
        int high = low;
        int bound2 = n2;
        while (high < bound2) {
            int x3 = (high + bound2) >>> 1;
            if (to >= p2[(x3 * 2) + 1] - 1) {
                high = x3 + 1;
            } else {
                bound2 = x3;
            }
        }
        int incN = (low + 1) - high;
        if (n2 + incN > 10000) {
            throw new ValueException(ErrorMessages.ERR_TOO_MANY_MULTI_BYTE_RANGES);
        }
        if (incN != 1) {
            if (from > p2[(low * 2) + 1]) {
                from = p2[(low * 2) + 1];
            }
            if (to < p2[((high - 1) * 2) + 2]) {
                to = p2[((high - 1) * 2) + 2];
            }
        }
        if (incN != 0 && high < n2) {
            int fromPos = 1 + (high * 2);
            int toPos = 1 + ((low + 1) * 2);
            int size = (n2 - high) * 2;
            if (incN > 0) {
                pbuf.moveRight(fromPos, toPos, size);
            } else {
                pbuf.moveLeftAndReduce(fromPos, toPos);
            }
        }
        int pos = 1 + (low * 2);
        pbuf.writeCodePoint(pos, from);
        pbuf.writeCodePoint(pos + 1, to);
        pbuf.writeCodePoint(0, n2 + incN);
        return pbuf;
    }

    public static CodeRangeBuffer addCodeRange(CodeRangeBuffer pbuf, ScanEnvironment env, int from, int to) {
        if (from > to) {
            if (env.syntax.allowEmptyRangeInCC()) {
                return pbuf;
            }
            throw new ValueException(ErrorMessages.ERR_EMPTY_RANGE_IN_CHAR_CLASS);
        }
        return addCodeRangeToBuff(pbuf, from, to);
    }

    protected static CodeRangeBuffer setAllMultiByteRange(CodeRangeBuffer pbuf) {
        return addCodeRangeToBuff(pbuf, EncodingHelper.mbcodeStartPosition(), Integer.MAX_VALUE);
    }

    public static CodeRangeBuffer addAllMultiByteRange(CodeRangeBuffer pbuf) {
        return setAllMultiByteRange(pbuf);
    }

    public static CodeRangeBuffer notCodeRangeBuff(CodeRangeBuffer bbuf) {
        CodeRangeBuffer pbuf = null;
        if (bbuf == null) {
            return setAllMultiByteRange(null);
        }
        int[] p2 = bbuf.f12878p;
        int n2 = p2[0];
        if (n2 <= 0) {
            return setAllMultiByteRange(null);
        }
        int pre = EncodingHelper.mbcodeStartPosition();
        int to = 0;
        for (int i2 = 0; i2 < n2; i2++) {
            int from = p2[(i2 * 2) + 1];
            to = p2[(i2 * 2) + 2];
            if (pre <= from - 1) {
                pbuf = addCodeRangeToBuff(pbuf, pre, from - 1);
            }
            if (to == Integer.MAX_VALUE) {
                break;
            }
            pre = to + 1;
        }
        if (to < Integer.MAX_VALUE) {
            pbuf = addCodeRangeToBuff(pbuf, to + 1, Integer.MAX_VALUE);
        }
        return pbuf;
    }

    public static CodeRangeBuffer orCodeRangeBuff(CodeRangeBuffer bbuf1p, boolean not1p, CodeRangeBuffer bbuf2p, boolean not2p) {
        CodeRangeBuffer pbuf = null;
        CodeRangeBuffer bbuf1 = bbuf1p;
        CodeRangeBuffer bbuf2 = bbuf2p;
        boolean not1 = not1p;
        boolean not2 = not2p;
        if (bbuf1 == null && bbuf2 == null) {
            if (not1 || not2) {
                return setAllMultiByteRange(null);
            }
            return null;
        }
        if (bbuf2 == null) {
            not1 = not2;
            not2 = not1;
            bbuf1 = bbuf2;
            bbuf2 = bbuf1;
        }
        if (bbuf1 == null) {
            if (not1) {
                return setAllMultiByteRange(null);
            }
            if (!not2) {
                return bbuf2.m4907clone();
            }
            return notCodeRangeBuff(bbuf2);
        }
        if (not1) {
            boolean tnot = not1;
            not1 = not2;
            not2 = tnot;
            CodeRangeBuffer tbuf = bbuf1;
            bbuf1 = bbuf2;
            bbuf2 = tbuf;
        }
        if (!not2 && !not1) {
            pbuf = bbuf2.m4907clone();
        } else if (!not1) {
            pbuf = notCodeRangeBuff(bbuf2);
        }
        int[] p1 = bbuf1.f12878p;
        int n1 = p1[0];
        for (int i2 = 0; i2 < n1; i2++) {
            int from = p1[(i2 * 2) + 1];
            int to = p1[(i2 * 2) + 2];
            pbuf = addCodeRangeToBuff(pbuf, from, to);
        }
        return pbuf;
    }

    public static CodeRangeBuffer andCodeRange1(CodeRangeBuffer pbufp, int from1p, int to1p, int[] data, int n2) {
        CodeRangeBuffer pbuf = pbufp;
        int from1 = from1p;
        int to1 = to1p;
        for (int i2 = 0; i2 < n2; i2++) {
            int from2 = data[(i2 * 2) + 1];
            int to2 = data[(i2 * 2) + 2];
            if (from2 < from1) {
                if (to2 < from1) {
                    continue;
                } else {
                    from1 = to2 + 1;
                }
            } else if (from2 <= to1) {
                if (to2 < to1) {
                    if (from1 <= from2 - 1) {
                        pbuf = addCodeRangeToBuff(pbuf, from1, from2 - 1);
                    }
                    from1 = to2 + 1;
                } else {
                    to1 = from2 - 1;
                }
            } else {
                from1 = from2;
            }
            if (from1 > to1) {
                break;
            }
        }
        if (from1 <= to1) {
            pbuf = addCodeRangeToBuff(pbuf, from1, to1);
        }
        return pbuf;
    }

    public static CodeRangeBuffer andCodeRangeBuff(CodeRangeBuffer bbuf1p, boolean not1p, CodeRangeBuffer bbuf2p, boolean not2p) {
        CodeRangeBuffer pbuf = null;
        CodeRangeBuffer bbuf1 = bbuf1p;
        CodeRangeBuffer bbuf2 = bbuf2p;
        boolean not1 = not1p;
        boolean not2 = not2p;
        if (bbuf1 == null) {
            if (not1 && bbuf2 != null) {
                return bbuf2.m4907clone();
            }
            return null;
        }
        if (bbuf2 == null) {
            if (not2) {
                return bbuf1.m4907clone();
            }
            return null;
        }
        if (not1) {
            not1 = not2;
            not2 = not1;
            bbuf1 = bbuf2;
            bbuf2 = bbuf1;
        }
        int[] p1 = bbuf1.f12878p;
        int n1 = p1[0];
        int[] p2 = bbuf2.f12878p;
        int n2 = p2[0];
        if (!not2 && !not1) {
            for (int i2 = 0; i2 < n1; i2++) {
                int from1 = p1[(i2 * 2) + 1];
                int to1 = p1[(i2 * 2) + 2];
                for (int j2 = 0; j2 < n2; j2++) {
                    int from2 = p2[(j2 * 2) + 1];
                    int to2 = p2[(j2 * 2) + 2];
                    if (from2 > to1) {
                        break;
                    }
                    if (to2 >= from1) {
                        int from = from1 > from2 ? from1 : from2;
                        int to = to1 < to2 ? to1 : to2;
                        pbuf = addCodeRangeToBuff(pbuf, from, to);
                    }
                }
            }
        } else if (!not1) {
            for (int i3 = 0; i3 < n1; i3++) {
                pbuf = andCodeRange1(pbuf, p1[(i3 * 2) + 1], p1[(i3 * 2) + 2], p2, n2);
            }
        }
        return pbuf;
    }
}
