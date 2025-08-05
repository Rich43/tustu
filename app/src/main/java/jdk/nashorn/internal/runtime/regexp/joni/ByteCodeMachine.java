package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/ByteCodeMachine.class */
class ByteCodeMachine extends StackMachine {
    private int bestLen;

    /* renamed from: s, reason: collision with root package name */
    private int f12877s;
    private int range;
    private int sprev;
    private int sstart;
    private int sbegin;
    private final int[] code;
    private int ip;

    ByteCodeMachine(Regex regex, char[] chars, int p2, int end) {
        super(regex, chars, p2, end);
        this.f12877s = 0;
        this.code = regex.code;
    }

    private boolean stringCmpIC(int caseFlodFlag, int s1p, IntHolder ps2, int mbLen, int textEnd) {
        int s1 = s1p;
        int s2 = ps2.value;
        int end1 = s1 + mbLen;
        while (s1 < end1) {
            int i2 = s1;
            s1++;
            char c1 = EncodingHelper.toLowerCase(this.chars[i2]);
            int i3 = s2;
            s2++;
            char c2 = EncodingHelper.toLowerCase(this.chars[i3]);
            if (c1 != c2) {
                return false;
            }
        }
        ps2.value = s2;
        return true;
    }

    private void debugMatchBegin() {
        Config.log.println("match_at: str: " + this.str + ", end: " + this.end + ", start: " + this.sstart + ", sprev: " + this.sprev);
        Config.log.println("size: " + (this.end - this.str) + ", start offset: " + (this.sstart - this.str));
    }

    private void debugMatchLoop() {
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Matcher
    protected final int matchAt(int r2, int ss, int sp) {
        this.range = r2;
        this.sstart = ss;
        this.sprev = sp;
        this.stk = 0;
        this.ip = 0;
        init();
        this.bestLen = -1;
        this.f12877s = ss;
        int[] c2 = this.code;
        while (true) {
            this.sbegin = this.f12877s;
            int i2 = this.ip;
            this.ip = i2 + 1;
            switch (c2[i2]) {
                case 0:
                    return finish();
                case 1:
                    if (!opEnd()) {
                        break;
                    } else {
                        return finish();
                    }
                case 2:
                    opExact1();
                    break;
                case 3:
                    opExact2();
                    break;
                case 4:
                    opExact3();
                    break;
                case 5:
                    opExact4();
                    break;
                case 6:
                    opExact5();
                    break;
                case 7:
                    opExactN();
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 69:
                default:
                    throw new InternalException(ErrorMessages.ERR_UNDEFINED_BYTECODE);
                case 14:
                    opExact1IC();
                    break;
                case 15:
                    opExactNIC();
                    break;
                case 16:
                    opCClass();
                    break;
                case 17:
                    opCClassMB();
                    break;
                case 18:
                    opCClassMIX();
                    break;
                case 19:
                    opCClassNot();
                    break;
                case 20:
                    opCClassMBNot();
                    break;
                case 21:
                    opCClassMIXNot();
                    break;
                case 22:
                    opCClassNode();
                    break;
                case 23:
                    opAnyChar();
                    break;
                case 24:
                    opAnyCharML();
                    break;
                case 25:
                    opAnyCharStar();
                    break;
                case 26:
                    opAnyCharMLStar();
                    break;
                case 27:
                    opAnyCharStarPeekNext();
                    break;
                case 28:
                    opAnyCharMLStarPeekNext();
                    break;
                case 29:
                    opWord();
                    break;
                case 30:
                    opNotWord();
                    break;
                case 31:
                    opWordBound();
                    break;
                case 32:
                    opNotWordBound();
                    break;
                case 33:
                    opWordBegin();
                    break;
                case 34:
                    opWordEnd();
                    break;
                case 35:
                    opBeginBuf();
                    break;
                case 36:
                    opEndBuf();
                    break;
                case 37:
                    opBeginLine();
                    break;
                case 38:
                    opEndLine();
                    break;
                case 39:
                    opSemiEndBuf();
                    break;
                case 40:
                    opBeginPosition();
                    break;
                case 41:
                    opBackRef1();
                    break;
                case 42:
                    opBackRef2();
                    break;
                case 43:
                    opBackRefN();
                    break;
                case 44:
                    opBackRefNIC();
                    break;
                case 45:
                    opBackRefMulti();
                    break;
                case 46:
                    opBackRefMultiIC();
                    break;
                case 47:
                    opBackRefAtLevel();
                    break;
                case 48:
                    opMemoryStart();
                    break;
                case 49:
                    opMemoryStartPush();
                    break;
                case 50:
                    opMemoryEndPush();
                    break;
                case 51:
                    opMemoryEndPushRec();
                    break;
                case 52:
                    opMemoryEnd();
                    break;
                case 53:
                    opMemoryEndRec();
                    break;
                case 54:
                    opFail();
                    break;
                case 55:
                    opJump();
                    break;
                case 56:
                    opPush();
                    break;
                case 57:
                    opPop();
                    break;
                case 58:
                    opPushOrJumpExact1();
                    break;
                case 59:
                    opPushIfPeekNext();
                    break;
                case 60:
                    opRepeat();
                    break;
                case 61:
                    opRepeatNG();
                    break;
                case 62:
                    opRepeatInc();
                    break;
                case 63:
                    opRepeatIncNG();
                    break;
                case 64:
                    opRepeatIncSG();
                    break;
                case 65:
                    opRepeatIncNGSG();
                    break;
                case 66:
                    opNullCheckStart();
                    break;
                case 67:
                    opNullCheckEnd();
                    break;
                case 68:
                    opNullCheckEndMemST();
                    break;
                case 70:
                    opPushPos();
                    break;
                case 71:
                    opPopPos();
                    break;
                case 72:
                    opPushPosNot();
                    break;
                case 73:
                    opFailPos();
                    break;
                case 74:
                    opPushStopBT();
                    break;
                case 75:
                    opPopStopBT();
                    break;
                case 76:
                    opLookBehind();
                    break;
                case 77:
                    opPushLookBehindNot();
                    break;
                case 78:
                    opFailLookBehindNot();
                    break;
            }
        }
    }

    private boolean opEnd() {
        int n2 = this.f12877s - this.sstart;
        if (n2 > this.bestLen) {
            if (Option.isFindLongest(this.regex.options)) {
                if (n2 > this.msaBestLen) {
                    this.msaBestLen = n2;
                    this.msaBestS = this.sstart;
                } else {
                    return endBestLength();
                }
            }
            this.bestLen = n2;
            Region region = this.msaRegion;
            if (region != null) {
                int[] iArr = region.beg;
                int i2 = this.sstart - this.str;
                this.msaBegin = i2;
                iArr[0] = i2;
                int[] iArr2 = region.end;
                int i3 = this.f12877s - this.str;
                this.msaEnd = i3;
                iArr2[0] = i3;
                for (int i4 = 1; i4 <= this.regex.numMem; i4++) {
                    if (this.repeatStk[this.memEndStk + i4] != -1) {
                        region.beg[i4] = BitStatus.bsAt(this.regex.btMemStart, i4) ? this.stack[this.repeatStk[this.memStartStk + i4]].getMemPStr() - this.str : this.repeatStk[this.memStartStk + i4] - this.str;
                        region.end[i4] = BitStatus.bsAt(this.regex.btMemEnd, i4) ? this.stack[this.repeatStk[this.memEndStk + i4]].getMemPStr() : this.repeatStk[this.memEndStk + i4] - this.str;
                    } else {
                        region.end[i4] = -1;
                        region.beg[i4] = -1;
                    }
                }
            } else {
                this.msaBegin = this.sstart - this.str;
                this.msaEnd = this.f12877s - this.str;
            }
        } else {
            Region region2 = this.msaRegion;
            if (region2 != null) {
                region2.clear();
            } else {
                this.msaEnd = 0;
                this.msaBegin = 0;
            }
        }
        return endBestLength();
    }

    private boolean endBestLength() {
        if (Option.isFindCondition(this.regex.options)) {
            if (Option.isFindNotEmpty(this.regex.options) && this.f12877s == this.sstart) {
                this.bestLen = -1;
                opFail();
                return false;
            }
            if (Option.isFindLongest(this.regex.options) && this.f12877s < this.range) {
                opFail();
                return false;
            }
            return true;
        }
        return true;
    }

    private void opExact1() {
        if (this.f12877s < this.range) {
            int i2 = this.code[this.ip];
            char[] cArr = this.chars;
            int i3 = this.f12877s;
            this.f12877s = i3 + 1;
            if (i2 == cArr[i3]) {
                this.ip++;
                this.sprev = this.sbegin;
                return;
            }
        }
        opFail();
    }

    private void opExact2() {
        if (this.f12877s + 2 > this.range) {
            opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.sprev = this.f12877s;
        this.ip++;
        this.f12877s++;
    }

    private void opExact3() {
        if (this.f12877s + 3 > this.range) {
            opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.sprev = this.f12877s;
        this.ip++;
        this.f12877s++;
    }

    private void opExact4() {
        if (this.f12877s + 4 > this.range) {
            opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.sprev = this.f12877s;
        this.ip++;
        this.f12877s++;
    }

    private void opExact5() {
        if (this.f12877s + 5 > this.range) {
            opFail();
            return;
        }
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.ip++;
        this.f12877s++;
        if (this.code[this.ip] != this.chars[this.f12877s]) {
            opFail();
            return;
        }
        this.sprev = this.f12877s;
        this.ip++;
        this.f12877s++;
    }

    private void opExactN() {
        char c2;
        char[] cArr;
        int i2;
        int[] iArr = this.code;
        int i3 = this.ip;
        this.ip = i3 + 1;
        int tlen = iArr[i3];
        if (this.f12877s + tlen > this.range) {
            opFail();
            return;
        }
        char[][] cArr2 = this.regex.templates;
        int[] iArr2 = this.code;
        int i4 = this.ip;
        this.ip = i4 + 1;
        char[] bs2 = cArr2[iArr2[i4]];
        int[] iArr3 = this.code;
        int i5 = this.ip;
        this.ip = i5 + 1;
        int ps = iArr3[i5];
        do {
            int i6 = tlen;
            tlen--;
            if (i6 > 0) {
                int i7 = ps;
                ps++;
                c2 = bs2[i7];
                cArr = this.chars;
                i2 = this.f12877s;
                this.f12877s = i2 + 1;
            } else {
                this.sprev = this.f12877s - 1;
                return;
            }
        } while (c2 == cArr[i2]);
        opFail();
    }

    private void opExact1IC() {
        if (this.f12877s < this.range) {
            int i2 = this.code[this.ip];
            char[] cArr = this.chars;
            int i3 = this.f12877s;
            this.f12877s = i3 + 1;
            if (i2 == EncodingHelper.toLowerCase(cArr[i3])) {
                this.ip++;
                this.sprev = this.sbegin;
                return;
            }
        }
        opFail();
    }

    private void opExactNIC() {
        char c2;
        char[] cArr;
        int i2;
        int[] iArr = this.code;
        int i3 = this.ip;
        this.ip = i3 + 1;
        int tlen = iArr[i3];
        if (this.f12877s + tlen > this.range) {
            opFail();
            return;
        }
        char[][] cArr2 = this.regex.templates;
        int[] iArr2 = this.code;
        int i4 = this.ip;
        this.ip = i4 + 1;
        char[] bs2 = cArr2[iArr2[i4]];
        int[] iArr3 = this.code;
        int i5 = this.ip;
        this.ip = i5 + 1;
        int ps = iArr3[i5];
        do {
            int i6 = tlen;
            tlen--;
            if (i6 > 0) {
                int i7 = ps;
                ps++;
                c2 = bs2[i7];
                cArr = this.chars;
                i2 = this.f12877s;
                this.f12877s = i2 + 1;
            } else {
                this.sprev = this.f12877s - 1;
                return;
            }
        } while (c2 == EncodingHelper.toLowerCase(cArr[i2]));
        opFail();
    }

    private boolean isInBitSet() {
        char c2 = this.chars[this.f12877s];
        return c2 <= 255 && (this.code[this.ip + (c2 >>> BitSet.ROOM_SHIFT)] & (1 << c2)) != 0;
    }

    private void opCClass() {
        if (this.f12877s >= this.range || !isInBitSet()) {
            opFail();
            return;
        }
        this.ip += 8;
        this.f12877s++;
        this.sprev = this.sbegin;
    }

    private boolean isInClassMB() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int tlen = iArr[i2];
        if (this.f12877s >= this.range) {
            return false;
        }
        int ss = this.f12877s;
        this.f12877s++;
        if (!EncodingHelper.isInCodeRange(this.code, this.ip, this.chars[ss])) {
            return false;
        }
        this.ip += tlen;
        return true;
    }

    private void opCClassMB() {
        if (this.f12877s >= this.range || this.chars[this.f12877s] <= 255) {
            opFail();
        } else if (isInClassMB()) {
            this.sprev = this.sbegin;
        } else {
            opFail();
        }
    }

    private void opCClassMIX() {
        if (this.f12877s >= this.range) {
            opFail();
            return;
        }
        if (this.chars[this.f12877s] > 255) {
            this.ip += 8;
            if (!isInClassMB()) {
                opFail();
                return;
            }
        } else {
            if (!isInBitSet()) {
                opFail();
                return;
            }
            this.ip += 8;
            int[] iArr = this.code;
            int i2 = this.ip;
            this.ip = i2 + 1;
            int tlen = iArr[i2];
            this.ip += tlen;
            this.f12877s++;
        }
        this.sprev = this.sbegin;
    }

    private void opCClassNot() {
        if (this.f12877s >= this.range || isInBitSet()) {
            opFail();
            return;
        }
        this.ip += 8;
        this.f12877s++;
        this.sprev = this.sbegin;
    }

    private boolean isNotInClassMB() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int tlen = iArr[i2];
        if (this.f12877s + 1 > this.range) {
            if (this.f12877s >= this.range) {
                return false;
            }
            this.f12877s = this.end;
            this.ip += tlen;
            return true;
        }
        int ss = this.f12877s;
        this.f12877s++;
        if (EncodingHelper.isInCodeRange(this.code, this.ip, this.chars[ss])) {
            return false;
        }
        this.ip += tlen;
        return true;
    }

    private void opCClassMBNot() {
        if (this.f12877s >= this.range) {
            opFail();
            return;
        }
        if (this.chars[this.f12877s] <= 255) {
            this.f12877s++;
            int[] iArr = this.code;
            int i2 = this.ip;
            this.ip = i2 + 1;
            int tlen = iArr[i2];
            this.ip += tlen;
            this.sprev = this.sbegin;
            return;
        }
        if (isNotInClassMB()) {
            this.sprev = this.sbegin;
        } else {
            opFail();
        }
    }

    private void opCClassMIXNot() {
        if (this.f12877s >= this.range) {
            opFail();
            return;
        }
        if (this.chars[this.f12877s] > 255) {
            this.ip += 8;
            if (!isNotInClassMB()) {
                opFail();
                return;
            }
        } else {
            if (isInBitSet()) {
                opFail();
                return;
            }
            this.ip += 8;
            int[] iArr = this.code;
            int i2 = this.ip;
            this.ip = i2 + 1;
            int tlen = iArr[i2];
            this.ip += tlen;
            this.f12877s++;
        }
        this.sprev = this.sbegin;
    }

    private void opCClassNode() {
        if (this.f12877s >= this.range) {
            opFail();
            return;
        }
        Object[] objArr = this.regex.operands;
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        CClassNode cc = (CClassNode) objArr[iArr[i2]];
        int ss = this.f12877s;
        this.f12877s++;
        if (cc.isCodeInCCLength(this.chars[ss])) {
            this.sprev = this.sbegin;
        } else {
            opFail();
        }
    }

    private void opAnyChar() {
        if (this.f12877s >= this.range) {
            opFail();
        } else if (EncodingHelper.isNewLine(this.chars[this.f12877s])) {
            opFail();
        } else {
            this.f12877s++;
            this.sprev = this.sbegin;
        }
    }

    private void opAnyCharML() {
        if (this.f12877s >= this.range) {
            opFail();
        } else {
            this.f12877s++;
            this.sprev = this.sbegin;
        }
    }

    private void opAnyCharStar() {
        char[] ch = this.chars;
        while (this.f12877s < this.range) {
            pushAlt(this.ip, this.f12877s, this.sprev);
            if (EncodingHelper.isNewLine(ch, this.f12877s, this.end)) {
                opFail();
                return;
            } else {
                this.sprev = this.f12877s;
                this.f12877s++;
            }
        }
    }

    private void opAnyCharMLStar() {
        while (this.f12877s < this.range) {
            pushAlt(this.ip, this.f12877s, this.sprev);
            this.sprev = this.f12877s;
            this.f12877s++;
        }
    }

    private void opAnyCharStarPeekNext() {
        char c2 = (char) this.code[this.ip];
        char[] ch = this.chars;
        while (this.f12877s < this.range) {
            char b2 = ch[this.f12877s];
            if (c2 == b2) {
                pushAlt(this.ip + 1, this.f12877s, this.sprev);
            }
            if (EncodingHelper.isNewLine(b2)) {
                opFail();
                return;
            } else {
                this.sprev = this.f12877s;
                this.f12877s++;
            }
        }
        this.ip++;
        this.sprev = this.sbegin;
    }

    private void opAnyCharMLStarPeekNext() {
        char c2 = (char) this.code[this.ip];
        char[] ch = this.chars;
        while (this.f12877s < this.range) {
            if (c2 == ch[this.f12877s]) {
                pushAlt(this.ip + 1, this.f12877s, this.sprev);
            }
            this.sprev = this.f12877s;
            this.f12877s++;
        }
        this.ip++;
        this.sprev = this.sbegin;
    }

    private void opWord() {
        if (this.f12877s >= this.range || !EncodingHelper.isWord(this.chars[this.f12877s])) {
            opFail();
        } else {
            this.f12877s++;
            this.sprev = this.sbegin;
        }
    }

    private void opNotWord() {
        if (this.f12877s >= this.range || EncodingHelper.isWord(this.chars[this.f12877s])) {
            opFail();
        } else {
            this.f12877s++;
            this.sprev = this.sbegin;
        }
    }

    private void opWordBound() {
        if (this.f12877s == this.str) {
            if (this.f12877s >= this.range || !EncodingHelper.isWord(this.chars[this.f12877s])) {
                opFail();
                return;
            }
            return;
        }
        if (this.f12877s == this.end) {
            if (this.sprev >= this.end || !EncodingHelper.isWord(this.chars[this.sprev])) {
                opFail();
                return;
            }
            return;
        }
        if (EncodingHelper.isWord(this.chars[this.f12877s]) == EncodingHelper.isWord(this.chars[this.sprev])) {
            opFail();
        }
    }

    private void opNotWordBound() {
        if (this.f12877s == this.str) {
            if (this.f12877s >= this.range || !EncodingHelper.isWord(this.chars[this.f12877s])) {
                return;
            }
            opFail();
            return;
        }
        if (this.f12877s == this.end) {
            if (this.sprev >= this.end || !EncodingHelper.isWord(this.chars[this.sprev])) {
                return;
            }
            opFail();
            return;
        }
        if (EncodingHelper.isWord(this.chars[this.f12877s]) != EncodingHelper.isWord(this.chars[this.sprev])) {
            opFail();
        }
    }

    private void opWordBegin() {
        if (this.f12877s < this.range && EncodingHelper.isWord(this.chars[this.f12877s]) && (this.f12877s == this.str || !EncodingHelper.isWord(this.chars[this.sprev]))) {
            return;
        }
        opFail();
    }

    private void opWordEnd() {
        if (this.f12877s != this.str && EncodingHelper.isWord(this.chars[this.sprev]) && (this.f12877s == this.end || !EncodingHelper.isWord(this.chars[this.f12877s]))) {
            return;
        }
        opFail();
    }

    private void opBeginBuf() {
        if (this.f12877s != this.str) {
            opFail();
        }
    }

    private void opEndBuf() {
        if (this.f12877s != this.end) {
            opFail();
        }
    }

    private void opBeginLine() {
        if (this.f12877s == this.str) {
            if (Option.isNotBol(this.msaOptions)) {
                opFail();
            }
        } else {
            if (EncodingHelper.isNewLine(this.chars, this.sprev, this.end) && this.f12877s != this.end) {
                return;
            }
            opFail();
        }
    }

    private void opEndLine() {
        if (this.f12877s == this.end) {
            if ((this.str == this.end || !EncodingHelper.isNewLine(this.chars, this.sprev, this.end)) && Option.isNotEol(this.msaOptions)) {
                opFail();
                return;
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.f12877s, this.end)) {
            return;
        }
        opFail();
    }

    private void opSemiEndBuf() {
        if (this.f12877s == this.end) {
            if ((this.str == this.end || !EncodingHelper.isNewLine(this.chars, this.sprev, this.end)) && Option.isNotEol(this.msaOptions)) {
                opFail();
                return;
            }
            return;
        }
        if (EncodingHelper.isNewLine(this.chars, this.f12877s, this.end) && this.f12877s + 1 == this.end) {
            return;
        }
        opFail();
    }

    private void opBeginPosition() {
        if (this.f12877s != this.msaStart) {
            opFail();
        }
    }

    private void opMemoryStartPush() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        pushMemStart(mem, this.f12877s);
    }

    private void opMemoryStart() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        this.repeatStk[this.memStartStk + mem] = this.f12877s;
    }

    private void opMemoryEndPush() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        pushMemEnd(mem, this.f12877s);
    }

    private void opMemoryEnd() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        this.repeatStk[this.memEndStk + mem] = this.f12877s;
    }

    private void opMemoryEndPushRec() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int stkp = getMemStart(mem);
        pushMemEnd(mem, this.f12877s);
        this.repeatStk[this.memStartStk + mem] = stkp;
    }

    private void opMemoryEndRec() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        this.repeatStk[this.memEndStk + mem] = this.f12877s;
        int stkp = getMemStart(mem);
        if (BitStatus.bsAt(this.regex.btMemStart, mem)) {
            this.repeatStk[this.memStartStk + mem] = stkp;
        } else {
            this.repeatStk[this.memStartStk + mem] = this.stack[stkp].getMemPStr();
        }
        pushMemEndMark(mem);
    }

    private boolean backrefInvalid(int mem) {
        return this.repeatStk[this.memEndStk + mem] == -1 || this.repeatStk[this.memStartStk + mem] == -1;
    }

    private int backrefStart(int mem) {
        return BitStatus.bsAt(this.regex.btMemStart, mem) ? this.stack[this.repeatStk[this.memStartStk + mem]].getMemPStr() : this.repeatStk[this.memStartStk + mem];
    }

    private int backrefEnd(int mem) {
        return BitStatus.bsAt(this.regex.btMemEnd, mem) ? this.stack[this.repeatStk[this.memEndStk + mem]].getMemPStr() : this.repeatStk[this.memEndStk + mem];
    }

    private void backref(int mem) {
        char c2;
        char[] cArr;
        int i2;
        if (mem > this.regex.numMem || backrefInvalid(mem)) {
            opFail();
            return;
        }
        int pstart = backrefStart(mem);
        int pend = backrefEnd(mem);
        int n2 = pend - pstart;
        if (this.f12877s + n2 > this.range) {
            opFail();
            return;
        }
        this.sprev = this.f12877s;
        do {
            int i3 = n2;
            n2--;
            if (i3 > 0) {
                int i4 = pstart;
                pstart++;
                c2 = this.chars[i4];
                cArr = this.chars;
                i2 = this.f12877s;
                this.f12877s = i2 + 1;
            } else {
                if (this.sprev < this.range) {
                    while (this.sprev + 1 < this.f12877s) {
                        this.sprev++;
                    }
                    return;
                }
                return;
            }
        } while (c2 == cArr[i2]);
        opFail();
    }

    private void opBackRef1() {
        backref(1);
    }

    private void opBackRef2() {
        backref(2);
    }

    private void opBackRefN() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        backref(iArr[i2]);
    }

    private void opBackRefNIC() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        if (mem > this.regex.numMem || backrefInvalid(mem)) {
            opFail();
            return;
        }
        int pstart = backrefStart(mem);
        int pend = backrefEnd(mem);
        int n2 = pend - pstart;
        if (this.f12877s + n2 > this.range) {
            opFail();
            return;
        }
        this.sprev = this.f12877s;
        this.value = this.f12877s;
        if (!stringCmpIC(this.regex.caseFoldFlag, pstart, this, n2, this.end)) {
            opFail();
            return;
        }
        this.f12877s = this.value;
        while (this.sprev + 1 < this.f12877s) {
            this.sprev++;
        }
    }

    private void opBackRefMulti() {
        int i2;
        int i3;
        int[] iArr = this.code;
        int i4 = this.ip;
        this.ip = i4 + 1;
        int tlen = iArr[i4];
        int i5 = 0;
        while (true) {
            if (i5 >= tlen) {
                break;
            }
            int[] iArr2 = this.code;
            int i6 = this.ip;
            this.ip = i6 + 1;
            int mem = iArr2[i6];
            if (!backrefInvalid(mem)) {
                int pstart = backrefStart(mem);
                int pend = backrefEnd(mem);
                int n2 = pend - pstart;
                if (this.f12877s + n2 > this.range) {
                    opFail();
                    return;
                }
                this.sprev = this.f12877s;
                int swork = this.f12877s;
                do {
                    int i7 = n2;
                    n2--;
                    if (i7 > 0) {
                        i2 = pstart;
                        pstart++;
                        i3 = swork;
                        swork++;
                    } else {
                        this.f12877s = swork;
                        if (this.sprev < this.range) {
                            while (this.sprev + 1 < this.f12877s) {
                                this.sprev++;
                            }
                        }
                        this.ip += (tlen - i5) - 1;
                    }
                } while (this.chars[i2] == this.chars[i3]);
            }
            i5++;
        }
        if (i5 == tlen) {
            opFail();
        }
    }

    private void opBackRefMultiIC() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int tlen = iArr[i2];
        int i3 = 0;
        while (true) {
            if (i3 >= tlen) {
                break;
            }
            int[] iArr2 = this.code;
            int i4 = this.ip;
            this.ip = i4 + 1;
            int mem = iArr2[i4];
            if (!backrefInvalid(mem)) {
                int pstart = backrefStart(mem);
                int pend = backrefEnd(mem);
                int n2 = pend - pstart;
                if (this.f12877s + n2 > this.range) {
                    opFail();
                    return;
                }
                this.sprev = this.f12877s;
                this.value = this.f12877s;
                if (stringCmpIC(this.regex.caseFoldFlag, pstart, this, n2, this.end)) {
                    this.f12877s = this.value;
                    while (this.sprev + 1 < this.f12877s) {
                        this.sprev++;
                    }
                    this.ip += (tlen - i3) - 1;
                }
            }
            i3++;
        }
        if (i3 == tlen) {
            opFail();
        }
    }

    private boolean memIsInMemp(int mem, int num, int mempp) {
        int memp = mempp;
        for (int i2 = 0; i2 < num; i2++) {
            int i3 = memp;
            memp++;
            int m2 = this.code[i3];
            if (mem == m2) {
                return true;
            }
        }
        return false;
    }

    private boolean backrefMatchAtNestedLevel(boolean ignoreCase, int caseFoldFlag, int nest, int memNum, int memp) {
        int pend = -1;
        int level = 0;
        for (int k2 = this.stk - 1; k2 >= 0; k2--) {
            StackEntry e2 = this.stack[k2];
            if (e2.type == 2048) {
                level--;
            } else if (e2.type == 2304) {
                level++;
            } else if (level != nest) {
                continue;
            } else if (e2.type == 256) {
                if (memIsInMemp(e2.getMemNum(), memNum, memp)) {
                    int pstart = e2.getMemPStr();
                    if (pend != -1) {
                        if (pend - pstart > this.end - this.f12877s) {
                            return false;
                        }
                        int p2 = pstart;
                        this.value = this.f12877s;
                        if (ignoreCase) {
                            if (!stringCmpIC(caseFoldFlag, pstart, this, pend - pstart, this.end)) {
                                return false;
                            }
                        } else {
                            while (p2 < pend) {
                                int i2 = p2;
                                p2++;
                                char c2 = this.chars[i2];
                                char[] cArr = this.chars;
                                int i3 = this.value;
                                this.value = i3 + 1;
                                if (c2 != cArr[i3]) {
                                    return false;
                                }
                            }
                        }
                        this.f12877s = this.value;
                        return true;
                    }
                } else {
                    continue;
                }
            } else if (e2.type == 33280 && memIsInMemp(e2.getMemNum(), memNum, memp)) {
                pend = e2.getMemPStr();
            }
        }
        return false;
    }

    private void opBackRefAtLevel() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int ic = iArr[i2];
        int[] iArr2 = this.code;
        int i3 = this.ip;
        this.ip = i3 + 1;
        int level = iArr2[i3];
        int[] iArr3 = this.code;
        int i4 = this.ip;
        this.ip = i4 + 1;
        int tlen = iArr3[i4];
        this.sprev = this.f12877s;
        if (backrefMatchAtNestedLevel(ic != 0, this.regex.caseFoldFlag, level, tlen, this.ip)) {
            while (this.sprev + 1 < this.f12877s) {
                this.sprev++;
            }
            this.ip += tlen;
            return;
        }
        opFail();
    }

    private void opNullCheckStart() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        pushNullCheckStart(mem, this.f12877s);
    }

    private void nullCheckFound() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        switch (iArr[i2]) {
            case 55:
            case 56:
                this.ip++;
                return;
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            default:
                throw new InternalException(ErrorMessages.ERR_UNEXPECTED_BYTECODE);
            case 62:
            case 63:
            case 64:
            case 65:
                this.ip++;
                return;
        }
    }

    private void opNullCheckEnd() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int isNull = nullCheck(mem, this.f12877s);
        if (isNull != 0) {
            nullCheckFound();
        }
    }

    private void opNullCheckEndMemST() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int isNull = nullCheckMemSt(mem, this.f12877s);
        if (isNull != 0) {
            if (isNull == -1) {
                opFail();
            } else {
                nullCheckFound();
            }
        }
    }

    private void opJump() {
        this.ip += this.code[this.ip] + 1;
    }

    private void opPush() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int addr = iArr[i2];
        pushAlt(this.ip + addr, this.f12877s, this.sprev);
    }

    private void opPop() {
        popOne();
    }

    private void opPushOrJumpExact1() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int addr = iArr[i2];
        if (this.f12877s < this.range && this.code[this.ip] == this.chars[this.f12877s]) {
            this.ip++;
            pushAlt(this.ip + addr, this.f12877s, this.sprev);
        } else {
            this.ip += addr + 1;
        }
    }

    private void opPushIfPeekNext() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int addr = iArr[i2];
        if (this.f12877s < this.range && this.code[this.ip] == this.chars[this.f12877s]) {
            this.ip++;
            pushAlt(this.ip + addr, this.f12877s, this.sprev);
        } else {
            this.ip++;
        }
    }

    private void opRepeat() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int[] iArr2 = this.code;
        int i3 = this.ip;
        this.ip = i3 + 1;
        int addr = iArr2[i3];
        this.repeatStk[mem] = this.stk;
        pushRepeat(mem, this.ip);
        if (this.regex.repeatRangeLo[mem] == 0) {
            pushAlt(this.ip + addr, this.f12877s, this.sprev);
        }
    }

    private void opRepeatNG() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int[] iArr2 = this.code;
        int i3 = this.ip;
        this.ip = i3 + 1;
        int addr = iArr2[i3];
        this.repeatStk[mem] = this.stk;
        pushRepeat(mem, this.ip);
        if (this.regex.repeatRangeLo[mem] == 0) {
            pushAlt(this.ip, this.f12877s, this.sprev);
            this.ip += addr;
        }
    }

    private void repeatInc(int mem, int si) {
        StackEntry e2 = this.stack[si];
        e2.increaseRepeatCount();
        if (e2.getRepeatCount() < this.regex.repeatRangeHi[mem]) {
            if (e2.getRepeatCount() >= this.regex.repeatRangeLo[mem]) {
                pushAlt(this.ip, this.f12877s, this.sprev);
                this.ip = e2.getRepeatPCode();
            } else {
                this.ip = e2.getRepeatPCode();
            }
        }
        pushRepeatInc(si);
    }

    private void opRepeatInc() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int si = this.repeatStk[mem];
        repeatInc(mem, si);
    }

    private void opRepeatIncSG() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int si = getRepeat(mem);
        repeatInc(mem, si);
    }

    private void repeatIncNG(int mem, int si) {
        StackEntry e2 = this.stack[si];
        e2.increaseRepeatCount();
        if (e2.getRepeatCount() < this.regex.repeatRangeHi[mem]) {
            if (e2.getRepeatCount() >= this.regex.repeatRangeLo[mem]) {
                int pcode = e2.getRepeatPCode();
                pushRepeatInc(si);
                pushAlt(pcode, this.f12877s, this.sprev);
                return;
            } else {
                this.ip = e2.getRepeatPCode();
                pushRepeatInc(si);
                return;
            }
        }
        if (e2.getRepeatCount() == this.regex.repeatRangeHi[mem]) {
            pushRepeatInc(si);
        }
    }

    private void opRepeatIncNG() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int si = this.repeatStk[mem];
        repeatIncNG(mem, si);
    }

    private void opRepeatIncNGSG() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int mem = iArr[i2];
        int si = getRepeat(mem);
        repeatIncNG(mem, si);
    }

    private void opPushPos() {
        pushPos(this.f12877s, this.sprev);
    }

    private void opPopPos() {
        StackEntry e2 = this.stack[posEnd()];
        this.f12877s = e2.getStatePStr();
        this.sprev = e2.getStatePStrPrev();
    }

    private void opPushPosNot() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int addr = iArr[i2];
        pushPosNot(this.ip + addr, this.f12877s, this.sprev);
    }

    private void opFailPos() {
        popTilPosNot();
        opFail();
    }

    private void opPushStopBT() {
        pushStopBT();
    }

    private void opPopStopBT() {
        stopBtEnd();
    }

    private void opLookBehind() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int tlen = iArr[i2];
        this.f12877s = EncodingHelper.stepBack(this.str, this.f12877s, tlen);
        if (this.f12877s == -1) {
            opFail();
        } else {
            this.sprev = EncodingHelper.prevCharHead(this.str, this.f12877s);
        }
    }

    private void opPushLookBehindNot() {
        int[] iArr = this.code;
        int i2 = this.ip;
        this.ip = i2 + 1;
        int addr = iArr[i2];
        int[] iArr2 = this.code;
        int i3 = this.ip;
        this.ip = i3 + 1;
        int tlen = iArr2[i3];
        int q2 = EncodingHelper.stepBack(this.str, this.f12877s, tlen);
        if (q2 == -1) {
            this.ip += addr;
            return;
        }
        pushLookBehindNot(this.ip + addr, this.f12877s, this.sprev);
        this.f12877s = q2;
        this.sprev = EncodingHelper.prevCharHead(this.str, this.f12877s);
    }

    private void opFailLookBehindNot() {
        popTilLookBehindNot();
        opFail();
    }

    private void opFail() {
        if (this.stack == null) {
            this.ip = this.regex.codeLength - 1;
            return;
        }
        StackEntry e2 = pop();
        this.ip = e2.getStatePCode();
        this.f12877s = e2.getStatePStr();
        this.sprev = e2.getStatePStrPrev();
    }

    private int finish() {
        return this.bestLen;
    }
}
