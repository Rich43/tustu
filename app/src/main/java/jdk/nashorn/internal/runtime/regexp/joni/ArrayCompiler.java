package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/ArrayCompiler.class */
final class ArrayCompiler extends Compiler {
    private int[] code;
    private int codeLength;
    private char[][] templates;
    private int templateNum;
    private static final int REPEAT_RANGE_ALLOC = 8;
    private static final int QUANTIFIER_EXPAND_LIMIT_SIZE = 50;

    ArrayCompiler(Analyser analyser) {
        super(analyser);
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected final void prepare() {
        this.code = new int[8];
        this.codeLength = 0;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected final void finish() {
        addOpcode(1);
        addOpcode(0);
        this.regex.code = this.code;
        this.regex.codeLength = this.codeLength;
        this.regex.templates = this.templates;
        this.regex.templateNum = this.templateNum;
        this.regex.factory = MatcherFactory.DEFAULT;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileAltNode(ConsAltNode node) {
        ConsAltNode consAltNode;
        ConsAltNode consAltNode2;
        ConsAltNode aln = node;
        int len = 0;
        do {
            len += compileLengthTree(aln.car);
            if (aln.cdr != null) {
                len += 4;
            }
            consAltNode = aln.cdr;
            aln = consAltNode;
        } while (consAltNode != null);
        int pos = this.codeLength + len;
        ConsAltNode aln2 = node;
        do {
            int len2 = compileLengthTree(aln2.car);
            if (aln2.cdr != null) {
                addOpcodeRelAddr(56, len2 + 2);
            }
            compileTree(aln2.car);
            if (aln2.cdr != null) {
                int len3 = pos - (this.codeLength + 2);
                addOpcodeRelAddr(55, len3);
            }
            consAltNode2 = aln2.cdr;
            aln2 = consAltNode2;
        } while (consAltNode2 != null);
    }

    private static boolean isNeedStrLenOpExact(int op) {
        return op == 7 || op == 15;
    }

    private static boolean opTemplated(int op) {
        return isNeedStrLenOpExact(op);
    }

    private static int selectStrOpcode(int strLength, boolean ignoreCase) {
        int op;
        if (ignoreCase) {
            switch (strLength) {
                case 1:
                    op = 14;
                    break;
                default:
                    op = 15;
                    break;
            }
        } else {
            switch (strLength) {
                case 1:
                    op = 2;
                    break;
                case 2:
                    op = 3;
                    break;
                case 3:
                    op = 4;
                    break;
                case 4:
                    op = 5;
                    break;
                case 5:
                    op = 6;
                    break;
                default:
                    op = 7;
                    break;
            }
        }
        return op;
    }

    private void compileTreeEmptyCheck(Node node, int emptyInfo) {
        int savedNumNullCheck = this.regex.numNullCheck;
        if (emptyInfo != 0) {
            addOpcode(66);
            addMemNum(this.regex.numNullCheck);
            this.regex.numNullCheck++;
        }
        compileTree(node);
        if (emptyInfo != 0) {
            switch (emptyInfo) {
                case 1:
                    addOpcode(67);
                    break;
                case 2:
                    addOpcode(68);
                    break;
            }
            addMemNum(savedNumNullCheck);
        }
    }

    private static int addCompileStringlength(char[] chars, int p2, int strLength, boolean ignoreCase) {
        int len;
        int op = selectStrOpcode(strLength, ignoreCase);
        int len2 = 1;
        if (opTemplated(op)) {
            len = 1 + 3;
        } else {
            if (isNeedStrLenOpExact(op)) {
                len2 = 1 + 1;
            }
            len = len2 + strLength;
        }
        return len;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected final void addCompileString(char[] chars, int p2, int strLength, boolean ignoreCase) {
        int op = selectStrOpcode(strLength, ignoreCase);
        addOpcode(op);
        if (isNeedStrLenOpExact(op)) {
            addLength(strLength);
        }
        if (opTemplated(op)) {
            addInt(this.templateNum);
            addInt(p2);
            addTemplate(chars);
            return;
        }
        addChars(chars, p2, strLength);
    }

    private static int compileLengthStringNode(Node node) {
        StringNode sn = (StringNode) node;
        if (sn.length() <= 0) {
            return 0;
        }
        boolean ambig = sn.isAmbig();
        int p2 = sn.f12885p;
        int end = sn.end;
        char[] chars = sn.chars;
        int slen = 1;
        for (int p3 = p2 + 1; p3 < end; p3++) {
            slen++;
        }
        int r2 = addCompileStringlength(chars, p2, slen, ambig);
        int rlen = 0 + r2;
        return rlen;
    }

    private static int compileLengthStringRawNode(StringNode sn) {
        if (sn.length() <= 0) {
            return 0;
        }
        return addCompileStringlength(sn.chars, sn.f12885p, sn.length(), false);
    }

    private void addMultiByteCClass(CodeRangeBuffer mbuf) {
        addLength(mbuf.used);
        addInts(mbuf.f12878p, mbuf.used);
    }

    private static int compileLengthCClassNode(CClassNode cc) {
        int len;
        int len2;
        if (cc.isShare()) {
            return 2;
        }
        if (cc.mbuf == null) {
            len2 = 9;
        } else {
            if (cc.f12882bs.isEmpty()) {
                len = 1;
            } else {
                len = 9;
            }
            len2 = len + 1 + cc.mbuf.used;
        }
        return len2;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileCClassNode(CClassNode cc) {
        if (cc.isShare()) {
            addOpcode(22);
            addPointer(cc);
            return;
        }
        if (cc.mbuf == null) {
            if (cc.isNot()) {
                addOpcode(19);
            } else {
                addOpcode(16);
            }
            addInts(cc.f12882bs.bits, 8);
            return;
        }
        if (cc.f12882bs.isEmpty()) {
            if (cc.isNot()) {
                addOpcode(20);
            } else {
                addOpcode(17);
            }
            addMultiByteCClass(cc.mbuf);
            return;
        }
        if (cc.isNot()) {
            addOpcode(21);
        } else {
            addOpcode(18);
        }
        addInts(cc.f12882bs.bits, 8);
        addMultiByteCClass(cc.mbuf);
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileAnyCharNode() {
        if (Option.isMultiline(this.regex.options)) {
            addOpcode(24);
        } else {
            addOpcode(23);
        }
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileBackrefNode(BackRefNode node) {
        if (Option.isIgnoreCase(this.regex.options)) {
            addOpcode(44);
            addMemNum(node.backRef);
            return;
        }
        switch (node.backRef) {
            case 1:
                addOpcode(41);
                break;
            case 2:
                addOpcode(42);
                break;
            default:
                addOpcode(43);
                addOpcode(node.backRef);
                break;
        }
    }

    private void entryRepeatRange(int id, int lower, int upper) {
        if (this.regex.repeatRangeLo == null) {
            this.regex.repeatRangeLo = new int[8];
            this.regex.repeatRangeHi = new int[8];
        } else if (id >= this.regex.repeatRangeLo.length) {
            int[] tmp = new int[this.regex.repeatRangeLo.length + 8];
            System.arraycopy(this.regex.repeatRangeLo, 0, tmp, 0, this.regex.repeatRangeLo.length);
            this.regex.repeatRangeLo = tmp;
            int[] tmp2 = new int[this.regex.repeatRangeHi.length + 8];
            System.arraycopy(this.regex.repeatRangeHi, 0, tmp2, 0, this.regex.repeatRangeHi.length);
            this.regex.repeatRangeHi = tmp2;
        }
        this.regex.repeatRangeLo[id] = lower;
        this.regex.repeatRangeHi[id] = QuantifierNode.isRepeatInfinite(upper) ? Integer.MAX_VALUE : upper;
    }

    private void compileRangeRepeatNode(QuantifierNode qn, int targetLen, int emptyInfo) {
        int numRepeat = this.regex.numRepeat;
        addOpcode(qn.greedy ? 60 : 61);
        addMemNum(numRepeat);
        this.regex.numRepeat++;
        addRelAddr(targetLen + 2);
        entryRepeatRange(numRepeat, qn.lower, qn.upper);
        compileTreeEmptyCheck(qn.target, emptyInfo);
        if (qn.isInRepeat()) {
            addOpcode(qn.greedy ? 64 : 65);
        } else {
            addOpcode(qn.greedy ? 62 : 63);
        }
        addMemNum(numRepeat);
    }

    private static boolean cknOn(int ckn) {
        return ckn > 0;
    }

    private int compileNonCECLengthQuantifierNode(QuantifierNode qn) {
        int modTLen;
        int len;
        int len2;
        boolean infinite = QuantifierNode.isRepeatInfinite(qn.upper);
        int emptyInfo = qn.targetEmptyInfo;
        int tlen = compileLengthTree(qn.target);
        if (qn.target.getType() == 3 && qn.greedy && infinite) {
            if (qn.nextHeadExact != null) {
                return 2 + (tlen * qn.lower);
            }
            return 1 + (tlen * qn.lower);
        }
        if (emptyInfo != 0) {
            modTLen = tlen + 4;
        } else {
            modTLen = tlen;
        }
        if (infinite && (qn.lower <= 1 || tlen * qn.lower <= 50)) {
            if (qn.lower == 1 && tlen > 50) {
                len2 = 2;
            } else {
                len2 = tlen * qn.lower;
            }
            if (qn.greedy) {
                if (qn.headExact != null || qn.nextHeadExact != null) {
                    len = len2 + 3 + modTLen + 2;
                } else {
                    len = len2 + 2 + modTLen + 2;
                }
            } else {
                len = len2 + 2 + modTLen + 2;
            }
        } else if (qn.upper == 0 && qn.isRefered) {
            len = 2 + tlen;
        } else if (!infinite && qn.greedy && (qn.upper == 1 || (tlen + 2) * qn.upper <= 50)) {
            int len3 = tlen * qn.lower;
            len = len3 + ((2 + tlen) * (qn.upper - qn.lower));
        } else if (!qn.greedy && qn.upper == 1 && qn.lower == 0) {
            len = 4 + tlen;
        } else {
            len = 2 + modTLen + 1 + 1 + 1;
        }
        return len;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileNonCECQuantifierNode(QuantifierNode qn) {
        int modTLen;
        boolean infinite = QuantifierNode.isRepeatInfinite(qn.upper);
        int emptyInfo = qn.targetEmptyInfo;
        int tlen = compileLengthTree(qn.target);
        if (qn.isAnyCharStar()) {
            compileTreeNTimes(qn.target, qn.lower);
            if (qn.nextHeadExact != null) {
                if (Option.isMultiline(this.regex.options)) {
                    addOpcode(28);
                } else {
                    addOpcode(27);
                }
                StringNode sn = (StringNode) qn.nextHeadExact;
                addChars(sn.chars, sn.f12885p, 1);
                return;
            }
            if (Option.isMultiline(this.regex.options)) {
                addOpcode(26);
                return;
            } else {
                addOpcode(25);
                return;
            }
        }
        if (emptyInfo != 0) {
            modTLen = tlen + 4;
        } else {
            modTLen = tlen;
        }
        if (infinite && (qn.lower <= 1 || tlen * qn.lower <= 50)) {
            if (qn.lower == 1 && tlen > 50) {
                if (qn.greedy) {
                    if (qn.headExact != null || qn.nextHeadExact != null) {
                        addOpcodeRelAddr(55, 3);
                    } else {
                        addOpcodeRelAddr(55, 2);
                    }
                } else {
                    addOpcodeRelAddr(55, 2);
                }
            } else {
                compileTreeNTimes(qn.target, qn.lower);
            }
            if (qn.greedy) {
                if (qn.headExact != null) {
                    addOpcodeRelAddr(58, modTLen + 2);
                    StringNode sn2 = (StringNode) qn.headExact;
                    addChars(sn2.chars, sn2.f12885p, 1);
                    compileTreeEmptyCheck(qn.target, emptyInfo);
                    addOpcodeRelAddr(55, -(modTLen + 2 + 3));
                    return;
                }
                if (qn.nextHeadExact != null) {
                    addOpcodeRelAddr(59, modTLen + 2);
                    StringNode sn3 = (StringNode) qn.nextHeadExact;
                    addChars(sn3.chars, sn3.f12885p, 1);
                    compileTreeEmptyCheck(qn.target, emptyInfo);
                    addOpcodeRelAddr(55, -(modTLen + 2 + 3));
                    return;
                }
                addOpcodeRelAddr(56, modTLen + 2);
                compileTreeEmptyCheck(qn.target, emptyInfo);
                addOpcodeRelAddr(55, -(modTLen + 2 + 2));
                return;
            }
            addOpcodeRelAddr(55, modTLen);
            compileTreeEmptyCheck(qn.target, emptyInfo);
            addOpcodeRelAddr(56, -(modTLen + 2));
            return;
        }
        if (qn.upper == 0 && qn.isRefered) {
            addOpcodeRelAddr(55, tlen);
            compileTree(qn.target);
            return;
        }
        if (!infinite && qn.greedy && (qn.upper == 1 || (tlen + 2) * qn.upper <= 50)) {
            int n2 = qn.upper - qn.lower;
            compileTreeNTimes(qn.target, qn.lower);
            for (int i2 = 0; i2 < n2; i2++) {
                addOpcodeRelAddr(56, ((n2 - i2) * tlen) + (((n2 - i2) - 1) * 2));
                compileTree(qn.target);
            }
            return;
        }
        if (!qn.greedy && qn.upper == 1 && qn.lower == 0) {
            addOpcodeRelAddr(56, 2);
            addOpcodeRelAddr(55, tlen);
            compileTree(qn.target);
            return;
        }
        compileRangeRepeatNode(qn, modTLen, emptyInfo);
    }

    private int compileLengthOptionNode(EncloseNode node) {
        int prev = this.regex.options;
        this.regex.options = node.option;
        int tlen = compileLengthTree(node.target);
        this.regex.options = prev;
        if (Option.isDynamic(prev ^ node.option)) {
            return 5 + tlen + 2;
        }
        return tlen;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileOptionNode(EncloseNode node) {
        int prev = this.regex.options;
        if (Option.isDynamic(prev ^ node.option)) {
            addOpcodeOption(86, node.option);
            addOpcodeOption(87, prev);
            addOpcode(54);
        }
        this.regex.options = node.option;
        compileTree(node.target);
        this.regex.options = prev;
        if (Option.isDynamic(prev ^ node.option)) {
            addOpcodeOption(87, prev);
        }
    }

    private int compileLengthEncloseNode(EncloseNode node) {
        int tlen;
        int len;
        int len2;
        if (node.isOption()) {
            return compileLengthOptionNode(node);
        }
        if (node.target != null) {
            tlen = compileLengthTree(node.target);
        } else {
            tlen = 0;
        }
        switch (node.type) {
            case 1:
                if (BitStatus.bsAt(this.regex.btMemStart, node.regNum)) {
                    len2 = 2;
                } else {
                    len2 = 2;
                }
                len = len2 + tlen + (BitStatus.bsAt(this.regex.btMemEnd, node.regNum) ? 2 : 2);
                break;
            case 4:
                if (node.isStopBtSimpleRepeat()) {
                    QuantifierNode qn = (QuantifierNode) node.target;
                    int tlen2 = compileLengthTree(qn.target);
                    len = (tlen2 * qn.lower) + 2 + tlen2 + 1 + 2;
                    break;
                } else {
                    len = 1 + tlen + 1;
                    break;
                }
            default:
                newInternalException(ErrorMessages.ERR_PARSER_BUG);
                return 0;
        }
        return len;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileEncloseNode(EncloseNode node) {
        switch (node.type) {
            case 1:
                if (BitStatus.bsAt(this.regex.btMemStart, node.regNum)) {
                    addOpcode(49);
                } else {
                    addOpcode(48);
                }
                addMemNum(node.regNum);
                compileTree(node.target);
                if (BitStatus.bsAt(this.regex.btMemEnd, node.regNum)) {
                    addOpcode(50);
                } else {
                    addOpcode(52);
                }
                addMemNum(node.regNum);
                break;
            case 4:
                if (node.isStopBtSimpleRepeat()) {
                    QuantifierNode qn = (QuantifierNode) node.target;
                    compileTreeNTimes(qn.target, qn.lower);
                    int len = compileLengthTree(qn.target);
                    addOpcodeRelAddr(56, len + 1 + 2);
                    compileTree(qn.target);
                    addOpcode(57);
                    addOpcodeRelAddr(55, -(2 + len + 1 + 2));
                    break;
                } else {
                    addOpcode(74);
                    compileTree(node.target);
                    addOpcode(75);
                    break;
                }
            default:
                newInternalException(ErrorMessages.ERR_PARSER_BUG);
                break;
        }
    }

    private int compileLengthAnchorNode(AnchorNode node) {
        int tlen;
        int len;
        if (node.target != null) {
            tlen = compileLengthTree(node.target);
        } else {
            tlen = 0;
        }
        switch (node.type) {
            case 1024:
                len = 1 + tlen + 1;
                break;
            case 2048:
                len = 2 + tlen + 1;
                break;
            case 4096:
                len = 2 + tlen;
                break;
            case 8192:
                len = 3 + tlen + 1;
                break;
            default:
                len = 1;
                break;
        }
        return len;
    }

    @Override // jdk.nashorn.internal.runtime.regexp.joni.Compiler
    protected void compileAnchorNode(AnchorNode node) {
        int n2;
        int n3;
        switch (node.type) {
            case 1:
                addOpcode(35);
                break;
            case 2:
                addOpcode(37);
                break;
            case 4:
                addOpcode(40);
                break;
            case 8:
                addOpcode(36);
                break;
            case 16:
                addOpcode(39);
                break;
            case 32:
                addOpcode(38);
                break;
            case 64:
                addOpcode(31);
                break;
            case 128:
                addOpcode(32);
                break;
            case 256:
                addOpcode(33);
                break;
            case 512:
                addOpcode(34);
                break;
            case 1024:
                addOpcode(70);
                compileTree(node.target);
                addOpcode(71);
                break;
            case 2048:
                int len = compileLengthTree(node.target);
                addOpcodeRelAddr(72, len + 1);
                compileTree(node.target);
                addOpcode(73);
                break;
            case 4096:
                addOpcode(76);
                if (node.charLength < 0) {
                    n3 = this.analyser.getCharLengthTree(node.target);
                    if (this.analyser.returnCode != 0) {
                        newSyntaxException(ErrorMessages.ERR_INVALID_LOOK_BEHIND_PATTERN);
                    }
                } else {
                    n3 = node.charLength;
                }
                addLength(n3);
                compileTree(node.target);
                break;
            case 8192:
                int len2 = compileLengthTree(node.target);
                addOpcodeRelAddr(77, len2 + 1);
                if (node.charLength < 0) {
                    n2 = this.analyser.getCharLengthTree(node.target);
                    if (this.analyser.returnCode != 0) {
                        newSyntaxException(ErrorMessages.ERR_INVALID_LOOK_BEHIND_PATTERN);
                    }
                } else {
                    n2 = node.charLength;
                }
                addLength(n2);
                compileTree(node.target);
                addOpcode(78);
                break;
            default:
                newInternalException(ErrorMessages.ERR_PARSER_BUG);
                break;
        }
    }

    private int compileLengthTree(Node node) {
        ConsAltNode consAltNode;
        ConsAltNode consAltNode2;
        int len = 0;
        switch (node.getType()) {
            case 0:
                StringNode sn = (StringNode) node;
                if (sn.isRaw()) {
                    len = compileLengthStringRawNode(sn);
                    break;
                } else {
                    len = compileLengthStringNode(sn);
                    break;
                }
            case 1:
                len = compileLengthCClassNode((CClassNode) node);
                break;
            case 2:
            case 3:
                len = 1;
                break;
            case 4:
                BackRefNode br2 = (BackRefNode) node;
                len = (Option.isIgnoreCase(this.regex.options) || br2.backRef > 2) ? 2 : 1;
                break;
            case 5:
                len = compileNonCECLengthQuantifierNode((QuantifierNode) node);
                break;
            case 6:
                len = compileLengthEncloseNode((EncloseNode) node);
                break;
            case 7:
                len = compileLengthAnchorNode((AnchorNode) node);
                break;
            case 8:
                ConsAltNode lin = (ConsAltNode) node;
                do {
                    len += compileLengthTree(lin.car);
                    consAltNode2 = lin.cdr;
                    lin = consAltNode2;
                } while (consAltNode2 != null);
            case 9:
                ConsAltNode aln = (ConsAltNode) node;
                int n2 = 0;
                do {
                    len += compileLengthTree(aln.car);
                    n2++;
                    consAltNode = aln.cdr;
                    aln = consAltNode;
                } while (consAltNode != null);
                len += 4 * (n2 - 1);
                break;
            default:
                newInternalException(ErrorMessages.ERR_PARSER_BUG);
                break;
        }
        return len;
    }

    private void ensure(int size) {
        if (size >= this.code.length) {
            int length = this.code.length;
            while (true) {
                int length2 = length << 1;
                if (length2 <= size) {
                    length = length2;
                } else {
                    int[] tmp = new int[length2];
                    System.arraycopy(this.code, 0, tmp, 0, this.code.length);
                    this.code = tmp;
                    return;
                }
            }
        }
    }

    private void addInt(int i2) {
        if (this.codeLength >= this.code.length) {
            int[] tmp = new int[this.code.length << 1];
            System.arraycopy(this.code, 0, tmp, 0, this.code.length);
            this.code = tmp;
        }
        int[] iArr = this.code;
        int i3 = this.codeLength;
        this.codeLength = i3 + 1;
        iArr[i3] = i2;
    }

    void setInt(int i2, int offset) {
        ensure(offset);
        this.regex.code[offset] = i2;
    }

    private void addObject(Object o2) {
        if (this.regex.operands == null) {
            this.regex.operands = new Object[4];
        } else if (this.regex.operandLength >= this.regex.operands.length) {
            Object[] tmp = new Object[this.regex.operands.length << 1];
            System.arraycopy(this.regex.operands, 0, tmp, 0, this.regex.operands.length);
            this.regex.operands = tmp;
        }
        addInt(this.regex.operandLength);
        Object[] objArr = this.regex.operands;
        Regex regex = this.regex;
        int i2 = regex.operandLength;
        regex.operandLength = i2 + 1;
        objArr[i2] = o2;
    }

    private void addChars(char[] chars, int pp, int length) {
        ensure(this.codeLength + length);
        int p2 = pp;
        int end = p2 + length;
        while (p2 < end) {
            int[] iArr = this.code;
            int i2 = this.codeLength;
            this.codeLength = i2 + 1;
            int i3 = p2;
            p2++;
            iArr[i2] = chars[i3];
        }
    }

    private void addInts(int[] ints, int length) {
        ensure(this.codeLength + length);
        System.arraycopy(ints, 0, this.code, this.codeLength, length);
        this.codeLength += length;
    }

    private void addOpcode(int opcode) {
        addInt(opcode);
        switch (opcode) {
            case 25:
            case 26:
            case 27:
            case 28:
            case 49:
            case 50:
            case 51:
            case 53:
            case 56:
            case 58:
            case 59:
            case 60:
            case 61:
            case 63:
            case 64:
            case 65:
            case 66:
            case 69:
            case 70:
            case 72:
            case 74:
            case 77:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
                this.regex.stackNeeded = true;
                break;
        }
    }

    private void addStateCheckNum(int num) {
        addInt(num);
    }

    private void addRelAddr(int addr) {
        addInt(addr);
    }

    private void addAbsAddr(int addr) {
        addInt(addr);
    }

    private void addLength(int length) {
        addInt(length);
    }

    private void addMemNum(int num) {
        addInt(num);
    }

    private void addPointer(Object o2) {
        addObject(o2);
    }

    private void addOption(int option) {
        addInt(option);
    }

    private void addOpcodeRelAddr(int opcode, int addr) {
        addOpcode(opcode);
        addRelAddr(addr);
    }

    private void addOpcodeOption(int opcode, int option) {
        addOpcode(opcode);
        addOption(option);
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [char[], char[][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v9, types: [char[], char[][]] */
    private void addTemplate(char[] chars) {
        if (this.templateNum == 0) {
            this.templates = new char[2];
        } else if (this.templateNum == this.templates.length) {
            ?? r0 = new char[this.templateNum * 2];
            System.arraycopy(this.templates, 0, r0, 0, this.templateNum);
            this.templates = r0;
        }
        char[][] cArr = this.templates;
        int i2 = this.templateNum;
        this.templateNum = i2 + 1;
        cArr[i2] = chars;
    }
}
