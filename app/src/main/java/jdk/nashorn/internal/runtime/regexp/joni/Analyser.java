package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AnchorType;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.ObjPtr;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/Analyser.class */
final class Analyser extends Parser {
    private static final int GET_CHAR_LEN_VARLEN = -1;
    private static final int GET_CHAR_LEN_TOP_ALT_VARLEN = -2;
    private static final int THRESHOLD_CASE_FOLD_ALT_FOR_EXPANSION = 8;
    private static final int IN_ALT = 1;
    private static final int IN_NOT = 2;
    private static final int IN_REPEAT = 4;
    private static final int IN_VAR_REPEAT = 8;
    private static final int EXPAND_STRING_MAX_LENGTH = 100;
    private static final int MAX_NODE_OPT_INFO_REF_COUNT = 5;

    protected Analyser(ScanEnvironment env, char[] chars, int p2, int end) {
        super(env, chars, p2, end);
    }

    protected final void compile() {
        reset();
        this.regex.numMem = 0;
        this.regex.numRepeat = 0;
        this.regex.numNullCheck = 0;
        this.regex.repeatRangeLo = null;
        this.regex.repeatRangeHi = null;
        parse();
        this.root = setupTree(this.root, 0);
        this.regex.captureHistory = this.env.captureHistory;
        this.regex.btMemStart = this.env.btMemStart;
        this.regex.btMemEnd = this.env.btMemEnd;
        if (Option.isFindCondition(this.regex.options)) {
            this.regex.btMemEnd = BitStatus.bsAll();
        } else {
            this.regex.btMemEnd = this.env.btMemEnd;
            this.regex.btMemEnd |= this.regex.captureHistory;
        }
        this.regex.clearOptimizeInfo();
        setOptimizedInfoFromTree(this.root);
        this.env.memNodes = null;
        if (this.regex.numRepeat != 0 || this.regex.btMemEnd != 0) {
            this.regex.stackPopLevel = 2;
        } else if (this.regex.btMemStart != 0) {
            this.regex.stackPopLevel = 1;
        } else {
            this.regex.stackPopLevel = 0;
        }
    }

    private void swap(Node a2, Node b2) {
        a2.swap(b2);
        if (this.root == b2) {
            this.root = a2;
        } else if (this.root == a2) {
            this.root = b2;
        }
    }

    private int quantifiersMemoryInfo(Node node) {
        ConsAltNode consAltNode;
        int info = 0;
        switch (node.getType()) {
            case 5:
                QuantifierNode qn = (QuantifierNode) node;
                if (qn.upper != 0) {
                    info = quantifiersMemoryInfo(qn.target);
                    break;
                }
                break;
            case 6:
                EncloseNode en = (EncloseNode) node;
                switch (en.type) {
                    case 1:
                        return 2;
                    case 2:
                    case 4:
                        info = quantifiersMemoryInfo(en.target);
                        break;
                }
            case 8:
            case 9:
                ConsAltNode can = (ConsAltNode) node;
                do {
                    int v2 = quantifiersMemoryInfo(can.car);
                    if (v2 > info) {
                        info = v2;
                    }
                    consAltNode = can.cdr;
                    can = consAltNode;
                } while (consAltNode != null);
        }
        return info;
    }

    private int getMinMatchLength(Node node) {
        ConsAltNode consAltNode;
        ConsAltNode consAltNode2;
        int min = 0;
        switch (node.getType()) {
            case 0:
                min = ((StringNode) node).length();
                break;
            case 1:
            case 3:
                min = 1;
                break;
            case 2:
                min = 1;
                break;
            case 4:
                BackRefNode br2 = (BackRefNode) node;
                if (!br2.isRecursion()) {
                    if (br2.backRef > this.env.numMem) {
                        throw new ValueException(ErrorMessages.ERR_INVALID_BACKREF);
                    }
                    min = getMinMatchLength(this.env.memNodes[br2.backRef]);
                    break;
                }
                break;
            case 5:
                QuantifierNode qn = (QuantifierNode) node;
                if (qn.lower > 0) {
                    min = MinMaxLen.distanceMultiply(getMinMatchLength(qn.target), qn.lower);
                    break;
                }
                break;
            case 6:
                EncloseNode en = (EncloseNode) node;
                switch (en.type) {
                    case 1:
                        if (en.isMinFixed()) {
                            min = en.minLength;
                            break;
                        } else {
                            min = getMinMatchLength(en.target);
                            en.minLength = min;
                            en.setMinFixed();
                            break;
                        }
                    case 2:
                    case 4:
                        min = getMinMatchLength(en.target);
                        break;
                }
            case 8:
                ConsAltNode can = (ConsAltNode) node;
                do {
                    min += getMinMatchLength(can.car);
                    consAltNode2 = can.cdr;
                    can = consAltNode2;
                } while (consAltNode2 != null);
            case 9:
                ConsAltNode y2 = (ConsAltNode) node;
                do {
                    Node x2 = y2.car;
                    int tmin = getMinMatchLength(x2);
                    if (y2 == node || min > tmin) {
                        min = tmin;
                    }
                    consAltNode = y2.cdr;
                    y2 = consAltNode;
                } while (consAltNode != null);
                break;
        }
        return min;
    }

    private int getMaxMatchLength(Node node) {
        ConsAltNode consAltNode;
        ConsAltNode consAltNode2;
        int max = 0;
        switch (node.getType()) {
            case 0:
                max = ((StringNode) node).length();
                break;
            case 1:
            case 3:
                max = 1;
                break;
            case 2:
                max = 1;
                break;
            case 4:
                BackRefNode br2 = (BackRefNode) node;
                if (br2.isRecursion()) {
                    max = Integer.MAX_VALUE;
                    break;
                } else {
                    if (br2.backRef > this.env.numMem) {
                        throw new ValueException(ErrorMessages.ERR_INVALID_BACKREF);
                    }
                    int tmax = getMaxMatchLength(this.env.memNodes[br2.backRef]);
                    if (0 < tmax) {
                        max = tmax;
                        break;
                    }
                }
                break;
            case 5:
                QuantifierNode qn = (QuantifierNode) node;
                if (qn.upper != 0) {
                    max = getMaxMatchLength(qn.target);
                    if (max != 0) {
                        if (!QuantifierNode.isRepeatInfinite(qn.upper)) {
                            max = MinMaxLen.distanceMultiply(max, qn.upper);
                            break;
                        } else {
                            max = Integer.MAX_VALUE;
                            break;
                        }
                    }
                }
                break;
            case 6:
                EncloseNode en = (EncloseNode) node;
                switch (en.type) {
                    case 1:
                        if (en.isMaxFixed()) {
                            max = en.maxLength;
                            break;
                        } else {
                            max = getMaxMatchLength(en.target);
                            en.maxLength = max;
                            en.setMaxFixed();
                            break;
                        }
                    case 2:
                    case 4:
                        max = getMaxMatchLength(en.target);
                        break;
                }
            case 8:
                ConsAltNode ln = (ConsAltNode) node;
                do {
                    max = MinMaxLen.distanceAdd(max, getMaxMatchLength(ln.car));
                    consAltNode2 = ln.cdr;
                    ln = consAltNode2;
                } while (consAltNode2 != null);
            case 9:
                ConsAltNode an2 = (ConsAltNode) node;
                do {
                    int tmax2 = getMaxMatchLength(an2.car);
                    if (max < tmax2) {
                        max = tmax2;
                    }
                    consAltNode = an2.cdr;
                    an2 = consAltNode;
                } while (consAltNode != null);
        }
        return max;
    }

    protected final int getCharLengthTree(Node node) {
        return getCharLengthTree(node, 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getCharLengthTree(jdk.nashorn.internal.runtime.regexp.joni.ast.Node r5, int r6) {
        /*
            Method dump skipped, instructions count: 445
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.runtime.regexp.joni.Analyser.getCharLengthTree(jdk.nashorn.internal.runtime.regexp.joni.ast.Node, int):int");
    }

    private static boolean isNotIncluded(Node xn, Node yn) {
        Node x2 = xn;
        Node node = yn;
        while (true) {
            Node y2 = node;
            int yType = y2.getType();
            switch (x2.getType()) {
                case 0:
                    StringNode xs = (StringNode) x2;
                    if (xs.length() != 0) {
                        switch (yType) {
                            case 0:
                                StringNode ys = (StringNode) y2;
                                int len = xs.length();
                                if (len > ys.length()) {
                                    len = ys.length();
                                }
                                if (xs.isAmbig() || ys.isAmbig()) {
                                    return false;
                                }
                                int i2 = 0;
                                int pt = ys.f12885p;
                                int q2 = xs.f12885p;
                                while (i2 < len) {
                                    if (ys.chars[pt] == xs.chars[q2]) {
                                        i2++;
                                        pt++;
                                        q2++;
                                    } else {
                                        return true;
                                    }
                                }
                                return false;
                            case 1:
                                CClassNode cc = (CClassNode) y2;
                                return !cc.isCodeInCC(xs.chars[xs.f12885p]);
                            default:
                                return false;
                        }
                    }
                    return false;
                case 1:
                    CClassNode xc = (CClassNode) x2;
                    switch (yType) {
                        case 0:
                            Node tmp = x2;
                            x2 = y2;
                            node = tmp;
                            break;
                        case 1:
                            CClassNode yc = (CClassNode) y2;
                            for (int i3 = 0; i3 < 256; i3++) {
                                boolean v2 = xc.f12882bs.at(i3);
                                if ((v2 && !xc.isNot()) || (!v2 && xc.isNot())) {
                                    boolean v3 = yc.f12882bs.at(i3);
                                    if (v3 && !yc.isNot()) {
                                        return false;
                                    }
                                    if (!v3 && yc.isNot()) {
                                        return false;
                                    }
                                }
                            }
                            if (xc.mbuf == null && !xc.isNot()) {
                                return true;
                            }
                            if (yc.mbuf == null && !yc.isNot()) {
                                return true;
                            }
                            return false;
                        default:
                            return false;
                    }
                case 2:
                    switch (yType) {
                        case 0:
                            Node tmp2 = x2;
                            x2 = y2;
                            node = tmp2;
                            break;
                        case 1:
                            Node tmp3 = x2;
                            x2 = y2;
                            node = tmp3;
                            break;
                        default:
                            return false;
                    }
                default:
                    return false;
            }
        }
    }

    private Node getHeadValueNode(Node node, boolean exact) {
        Node n2 = null;
        switch (node.getType()) {
            case 0:
                StringNode sn = (StringNode) node;
                if (sn.end > sn.f12885p && (!exact || sn.isRaw() || !Option.isIgnoreCase(this.regex.options))) {
                    n2 = node;
                    break;
                }
                break;
            case 1:
            case 2:
                if (!exact) {
                    n2 = node;
                    break;
                }
                break;
            case 5:
                QuantifierNode qn = (QuantifierNode) node;
                if (qn.lower > 0) {
                    if (qn.headExact != null) {
                        n2 = qn.headExact;
                        break;
                    } else {
                        n2 = getHeadValueNode(qn.target, exact);
                        break;
                    }
                }
                break;
            case 6:
                EncloseNode en = (EncloseNode) node;
                switch (en.type) {
                    case 1:
                    case 4:
                        n2 = getHeadValueNode(en.target, exact);
                        break;
                    case 2:
                        int options = this.regex.options;
                        this.regex.options = en.option;
                        n2 = getHeadValueNode(en.target, exact);
                        this.regex.options = options;
                        break;
                }
            case 7:
                AnchorNode an2 = (AnchorNode) node;
                if (an2.type == 1024) {
                    n2 = getHeadValueNode(an2.target, exact);
                    break;
                }
                break;
            case 8:
                n2 = getHeadValueNode(((ConsAltNode) node).car, exact);
                break;
        }
        return n2;
    }

    private boolean checkTypeTree(Node node, int typeMask, int encloseMask, int anchorMask) {
        ConsAltNode consAltNode;
        if ((node.getType2Bit() & typeMask) == 0) {
            return true;
        }
        boolean invalid = false;
        switch (node.getType()) {
            case 5:
                invalid = checkTypeTree(((QuantifierNode) node).target, typeMask, encloseMask, anchorMask);
                break;
            case 6:
                EncloseNode en = (EncloseNode) node;
                if ((en.type & encloseMask) == 0) {
                    return true;
                }
                invalid = checkTypeTree(en.target, typeMask, encloseMask, anchorMask);
                break;
            case 7:
                AnchorNode an2 = (AnchorNode) node;
                if ((an2.type & anchorMask) == 0) {
                    return true;
                }
                if (an2.target != null) {
                    invalid = checkTypeTree(an2.target, typeMask, encloseMask, anchorMask);
                    break;
                }
                break;
            case 8:
            case 9:
                ConsAltNode can = (ConsAltNode) node;
                do {
                    invalid = checkTypeTree(can.car, typeMask, encloseMask, anchorMask);
                    if (invalid) {
                        break;
                    } else {
                        consAltNode = can.cdr;
                        can = consAltNode;
                    }
                } while (consAltNode != null);
        }
        return invalid;
    }

    private Node divideLookBehindAlternatives(Node nodep) {
        ConsAltNode consAltNode;
        AnchorNode an2 = (AnchorNode) nodep;
        int anchorType = an2.type;
        Node head = an2.target;
        Node np = ((ConsAltNode) head).car;
        swap(nodep, head);
        ((ConsAltNode) head).setCar(nodep);
        ((AnchorNode) nodep).setTarget(np);
        Node np2 = head;
        while (true) {
            ConsAltNode consAltNode2 = ((ConsAltNode) np2).cdr;
            np2 = consAltNode2;
            if (consAltNode2 == null) {
                break;
            }
            AnchorNode insert = new AnchorNode(anchorType);
            insert.setTarget(((ConsAltNode) np2).car);
            ((ConsAltNode) np2).setCar(insert);
        }
        if (anchorType == 8192) {
            Node np3 = head;
            do {
                ((ConsAltNode) np3).toListNode();
                consAltNode = ((ConsAltNode) np3).cdr;
                np3 = consAltNode;
            } while (consAltNode != null);
        }
        return head;
    }

    private Node setupLookBehind(Node node) {
        AnchorNode an2 = (AnchorNode) node;
        int len = getCharLengthTree(an2.target);
        switch (this.returnCode) {
            case -2:
                if (this.syntax.differentLengthAltLookBehind()) {
                    return divideLookBehindAlternatives(node);
                }
                throw new SyntaxException(ErrorMessages.ERR_INVALID_LOOK_BEHIND_PATTERN);
            case -1:
                throw new SyntaxException(ErrorMessages.ERR_INVALID_LOOK_BEHIND_PATTERN);
            case 0:
                an2.charLength = len;
                break;
        }
        return node;
    }

    private void nextSetup(Node nodep, Node nextNode) {
        Node x2;
        Node y2;
        Node node = nodep;
        while (true) {
            Node node2 = node;
            int type = node2.getType();
            if (type == 5) {
                QuantifierNode qn = (QuantifierNode) node2;
                if (qn.greedy && QuantifierNode.isRepeatInfinite(qn.upper)) {
                    StringNode n2 = (StringNode) getHeadValueNode(nextNode, true);
                    if (n2 != null && n2.chars[n2.f12885p] != 0) {
                        qn.nextHeadExact = n2;
                    }
                    if (qn.lower <= 1 && qn.target.isSimple() && (x2 = getHeadValueNode(qn.target, false)) != null && (y2 = getHeadValueNode(nextNode, false)) != null && isNotIncluded(x2, y2)) {
                        EncloseNode en = new EncloseNode(4);
                        en.setStopBtSimpleRepeat();
                        swap(node2, en);
                        en.setTarget(node2);
                        return;
                    }
                    return;
                }
                return;
            }
            if (type == 6) {
                EncloseNode en2 = (EncloseNode) node2;
                if (en2.isMemory()) {
                    node = en2.target;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void updateStringNodeCaseFoldMultiByte(StringNode sn) {
        char[] ch = sn.chars;
        int end = sn.end;
        this.value = sn.f12885p;
        int sp = 0;
        while (this.value < end) {
            int ovalue = this.value;
            int i2 = this.value;
            this.value = i2 + 1;
            char buf = EncodingHelper.toLowerCase(ch[i2]);
            if (ch[ovalue] != buf) {
                char[] sbuf = new char[sn.length() << 1];
                System.arraycopy(ch, sn.f12885p, sbuf, 0, ovalue - sn.f12885p);
                this.value = ovalue;
                while (this.value < end) {
                    int i3 = this.value;
                    this.value = i3 + 1;
                    char buf2 = EncodingHelper.toLowerCase(ch[i3]);
                    if (sp >= sbuf.length) {
                        char[] tmp = new char[sbuf.length << 1];
                        System.arraycopy(sbuf, 0, tmp, 0, sbuf.length);
                        sbuf = tmp;
                    }
                    int i4 = sp;
                    sp++;
                    sbuf[i4] = buf2;
                }
                sn.set(sbuf, 0, sp);
                return;
            }
            sp++;
        }
    }

    private void updateStringNodeCaseFold(Node node) {
        StringNode sn = (StringNode) node;
        updateStringNodeCaseFoldMultiByte(sn);
    }

    private Node expandCaseFoldMakeRemString(char[] ch, int pp, int end) {
        StringNode node = new StringNode(ch, pp, end);
        updateStringNodeCaseFold(node);
        node.setAmbig();
        node.setDontGetOptInfo();
        return node;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T, jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode] */
    private static boolean expandCaseFoldStringAlt(int itemNum, char[] items, char[] chars, int p2, int slen, int end, ObjPtr<Node> node) {
        ?? NewAltNode = ConsAltNode.newAltNode(null, null);
        ConsAltNode altNode = NewAltNode;
        node.f12890p = NewAltNode;
        altNode.setCar(new StringNode(chars, p2, p2 + slen));
        for (int i2 = 0; i2 < itemNum; i2++) {
            StringNode snode = new StringNode();
            snode.catCode(items[i2]);
            ConsAltNode an2 = ConsAltNode.newAltNode(null, null);
            an2.setCar(snode);
            altNode.setCdr(an2);
            altNode = an2;
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v29, types: [T, jdk.nashorn.internal.runtime.regexp.joni.ast.Node] */
    /* JADX WARN: Type inference failed for: r1v26, types: [T, jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode] */
    private Node expandCaseFoldString(Node node) {
        StringNode sn = (StringNode) node;
        if (sn.isAmbig() || sn.length() <= 0) {
            return node;
        }
        char[] chars1 = sn.chars;
        int pt = sn.f12885p;
        int end = sn.end;
        int altNum = 1;
        ConsAltNode topRoot = null;
        ConsAltNode r2 = null;
        ObjPtr<Node> prevNode = new ObjPtr<>();
        StringNode stringNode = null;
        while (pt < end) {
            char[] items = EncodingHelper.caseFoldCodesByString(this.regex.caseFoldFlag, chars1[pt]);
            if (items.length == 0) {
                if (stringNode == null) {
                    if (r2 == null && prevNode.f12890p != null) {
                        ConsAltNode consAltNodeListAdd = ConsAltNode.listAdd(null, prevNode.f12890p);
                        r2 = consAltNodeListAdd;
                        topRoot = consAltNodeListAdd;
                    }
                    ?? stringNode2 = new StringNode();
                    stringNode = stringNode2;
                    prevNode.f12890p = stringNode2;
                    if (r2 != null) {
                        ConsAltNode.listAdd(r2, stringNode);
                    }
                }
                stringNode.cat(chars1, pt, pt + 1);
            } else {
                altNum *= items.length + 1;
                if (altNum > 8) {
                    break;
                }
                if (r2 == null && prevNode.f12890p != null) {
                    ConsAltNode consAltNodeListAdd2 = ConsAltNode.listAdd(null, prevNode.f12890p);
                    r2 = consAltNodeListAdd2;
                    topRoot = consAltNodeListAdd2;
                }
                expandCaseFoldStringAlt(items.length, items, chars1, pt, 1, end, prevNode);
                if (r2 != null) {
                    ConsAltNode.listAdd(r2, prevNode.f12890p);
                }
                stringNode = null;
            }
            pt++;
        }
        if (pt < end) {
            ?? ExpandCaseFoldMakeRemString = expandCaseFoldMakeRemString(chars1, pt, end);
            if (prevNode.f12890p != null && r2 == null) {
                ConsAltNode consAltNodeListAdd3 = ConsAltNode.listAdd(null, prevNode.f12890p);
                r2 = consAltNodeListAdd3;
                topRoot = consAltNodeListAdd3;
            }
            if (r2 == null) {
                prevNode.f12890p = ExpandCaseFoldMakeRemString;
            } else {
                ConsAltNode.listAdd(r2, ExpandCaseFoldMakeRemString);
            }
        }
        Node xnode = topRoot != null ? topRoot : prevNode.f12890p;
        swap(node, xnode);
        return xnode;
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0223  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected final jdk.nashorn.internal.runtime.regexp.joni.ast.Node setupTree(jdk.nashorn.internal.runtime.regexp.joni.ast.Node r7, int r8) {
        /*
            Method dump skipped, instructions count: 1059
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.runtime.regexp.joni.Analyser.setupTree(jdk.nashorn.internal.runtime.regexp.joni.ast.Node, int):jdk.nashorn.internal.runtime.regexp.joni.ast.Node");
    }

    private void optimizeNodeLeft(Node node, NodeOptInfo opt, OptEnvironment oenv) {
        int max;
        int max2;
        ConsAltNode consAltNode;
        ConsAltNode consAltNode2;
        opt.clear();
        opt.setBoundNode(oenv.mmd);
        switch (node.getType()) {
            case 0:
                StringNode sn = (StringNode) node;
                int slen = sn.length();
                if (!sn.isAmbig()) {
                    opt.exb.concatStr(sn.chars, sn.f12885p, sn.end, sn.isRaw());
                    if (slen > 0) {
                        opt.map.addChar(sn.chars[sn.f12885p]);
                    }
                    opt.length.set(slen, slen);
                } else {
                    if (sn.isDontGetOptInfo()) {
                        max2 = sn.length();
                    } else {
                        opt.exb.concatStr(sn.chars, sn.f12885p, sn.end, sn.isRaw());
                        opt.exb.ignoreCase = true;
                        if (slen > 0) {
                            opt.map.addCharAmb(sn.chars, sn.f12885p, sn.end, oenv.caseFoldFlag);
                        }
                        max2 = slen;
                    }
                    opt.length.set(slen, max2);
                }
                if (opt.exb.length == slen) {
                    opt.exb.reachEnd = true;
                    return;
                }
                return;
            case 1:
                CClassNode cc = (CClassNode) node;
                if (cc.mbuf != null || cc.isNot()) {
                    opt.length.set(1, 1);
                    return;
                }
                for (int i2 = 0; i2 < 256; i2++) {
                    boolean z2 = cc.f12882bs.at(i2);
                    if ((z2 && !cc.isNot()) || (!z2 && cc.isNot())) {
                        opt.map.addChar(i2);
                    }
                }
                opt.length.set(1, 1);
                return;
            case 2:
            default:
                throw new InternalException(ErrorMessages.ERR_PARSER_BUG);
            case 3:
                opt.length.set(1, 1);
                return;
            case 4:
                BackRefNode br2 = (BackRefNode) node;
                if (br2.isRecursion()) {
                    opt.length.set(0, Integer.MAX_VALUE);
                    return;
                }
                Node[] nodes = oenv.scanEnv.memNodes;
                int min = getMinMatchLength(nodes[br2.backRef]);
                int max3 = getMaxMatchLength(nodes[br2.backRef]);
                opt.length.set(min, max3);
                return;
            case 5:
                NodeOptInfo nopt = new NodeOptInfo();
                QuantifierNode qn = (QuantifierNode) node;
                optimizeNodeLeft(qn.target, nopt, oenv);
                if (qn.lower == 0 && QuantifierNode.isRepeatInfinite(qn.upper)) {
                    if (oenv.mmd.max == 0 && qn.target.getType() == 3 && qn.greedy) {
                        if (Option.isMultiline(oenv.options)) {
                            opt.anchor.add(32768);
                        } else {
                            opt.anchor.add(16384);
                        }
                    }
                } else if (qn.lower > 0) {
                    opt.copy(nopt);
                    if (nopt.exb.length > 0 && nopt.exb.reachEnd) {
                        int i3 = 2;
                        while (i3 <= qn.lower && !opt.exb.isFull()) {
                            opt.exb.concat(nopt.exb);
                            i3++;
                        }
                        if (i3 < qn.lower) {
                            opt.exb.reachEnd = false;
                        }
                    }
                    if (qn.lower != qn.upper) {
                        opt.exb.reachEnd = false;
                        opt.exm.reachEnd = false;
                    }
                    if (qn.lower > 1) {
                        opt.exm.reachEnd = false;
                    }
                }
                int min2 = MinMaxLen.distanceMultiply(nopt.length.min, qn.lower);
                if (QuantifierNode.isRepeatInfinite(qn.upper)) {
                    max = nopt.length.max > 0 ? Integer.MAX_VALUE : 0;
                } else {
                    max = MinMaxLen.distanceMultiply(nopt.length.max, qn.upper);
                }
                opt.length.set(min2, max);
                return;
            case 6:
                EncloseNode en = (EncloseNode) node;
                switch (en.type) {
                    case 1:
                        int i4 = en.optCount + 1;
                        en.optCount = i4;
                        if (i4 > 5) {
                            int min3 = 0;
                            int max4 = Integer.MAX_VALUE;
                            if (en.isMinFixed()) {
                                min3 = en.minLength;
                            }
                            if (en.isMaxFixed()) {
                                max4 = en.maxLength;
                            }
                            opt.length.set(min3, max4);
                            return;
                        }
                        optimizeNodeLeft(en.target, opt, oenv);
                        if (opt.anchor.isSet(AnchorType.ANYCHAR_STAR_MASK) && BitStatus.bsAt(oenv.scanEnv.backrefedMem, en.regNum)) {
                            opt.anchor.remove(AnchorType.ANYCHAR_STAR_MASK);
                            return;
                        }
                        return;
                    case 2:
                        int save = oenv.options;
                        oenv.options = en.option;
                        optimizeNodeLeft(en.target, opt, oenv);
                        oenv.options = save;
                        return;
                    case 3:
                    default:
                        return;
                    case 4:
                        optimizeNodeLeft(en.target, opt, oenv);
                        return;
                }
            case 7:
                AnchorNode an2 = (AnchorNode) node;
                switch (an2.type) {
                    case 1:
                    case 2:
                    case 4:
                    case 8:
                    case 16:
                    case 32:
                        opt.anchor.add(an2.type);
                        return;
                    case 1024:
                        NodeOptInfo nopt2 = new NodeOptInfo();
                        optimizeNodeLeft(an2.target, nopt2, oenv);
                        if (nopt2.exb.length > 0) {
                            opt.expr.copy(nopt2.exb);
                        } else if (nopt2.exm.length > 0) {
                            opt.expr.copy(nopt2.exm);
                        }
                        opt.expr.reachEnd = false;
                        if (nopt2.map.value > 0) {
                            opt.map.copy(nopt2.map);
                            return;
                        }
                        return;
                    case 2048:
                    case 4096:
                    case 8192:
                    default:
                        return;
                }
            case 8:
                OptEnvironment nenv = new OptEnvironment();
                NodeOptInfo nopt3 = new NodeOptInfo();
                nenv.copy(oenv);
                ConsAltNode lin = (ConsAltNode) node;
                do {
                    optimizeNodeLeft(lin.car, nopt3, nenv);
                    nenv.mmd.add(nopt3.length);
                    opt.concatLeftNode(nopt3);
                    consAltNode2 = lin.cdr;
                    lin = consAltNode2;
                } while (consAltNode2 != null);
                return;
            case 9:
                NodeOptInfo nopt4 = new NodeOptInfo();
                ConsAltNode aln = (ConsAltNode) node;
                do {
                    optimizeNodeLeft(aln.car, nopt4, oenv);
                    if (aln == node) {
                        opt.copy(nopt4);
                    } else {
                        opt.altMerge(nopt4, oenv);
                    }
                    consAltNode = aln.cdr;
                    aln = consAltNode;
                } while (consAltNode != null);
                return;
        }
    }

    protected final void setOptimizedInfoFromTree(Node node) {
        NodeOptInfo opt = new NodeOptInfo();
        OptEnvironment oenv = new OptEnvironment();
        oenv.options = this.regex.options;
        oenv.caseFoldFlag = this.regex.caseFoldFlag;
        oenv.scanEnv = this.env;
        oenv.mmd.clear();
        optimizeNodeLeft(node, opt, oenv);
        this.regex.anchor = opt.anchor.leftAnchor & 49157;
        this.regex.anchor |= opt.anchor.rightAnchor & 24;
        if ((this.regex.anchor & 24) != 0) {
            this.regex.anchorDmin = opt.length.min;
            this.regex.anchorDmax = opt.length.max;
        }
        if (opt.exb.length > 0 || opt.exm.length > 0) {
            opt.exb.select(opt.exm);
            if (opt.map.value > 0 && opt.exb.compare(opt.map) > 0) {
                this.regex.setOptimizeMapInfo(opt.map);
                this.regex.setSubAnchor(opt.map.anchor);
                return;
            } else {
                this.regex.setExactInfo(opt.exb);
                this.regex.setSubAnchor(opt.exb.anchor);
                return;
            }
        }
        if (opt.map.value > 0) {
            this.regex.setOptimizeMapInfo(opt.map);
            this.regex.setSubAnchor(opt.map.anchor);
            return;
        }
        this.regex.subAnchor |= opt.anchor.leftAnchor & 2;
        if (opt.length.max == 0) {
            this.regex.subAnchor |= opt.anchor.rightAnchor & 32;
        }
    }
}
