package com.sun.org.apache.xerces.internal.impl.xpath.regex;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Op;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Token;
import java.io.Serializable;
import java.text.CharacterIterator;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegularExpression.class */
public class RegularExpression implements Serializable {
    private static final long serialVersionUID = 6242499334195006401L;
    static final boolean DEBUG = false;
    String regex;
    int options;
    int nofparen;
    Token tokentree;
    boolean hasBackReferences;
    transient int minlength;
    transient Op operations;
    transient int numberOfClosures;
    transient Context context;
    transient RangeToken firstChar;
    transient String fixedString;
    transient int fixedStringOptions;
    transient BMPattern fixedStringTable;
    transient boolean fixedStringOnly;
    static final int IGNORE_CASE = 2;
    static final int SINGLE_LINE = 4;
    static final int MULTIPLE_LINES = 8;
    static final int EXTENDED_COMMENT = 16;
    static final int USE_UNICODE_CATEGORY = 32;
    static final int UNICODE_WORD_BOUNDARY = 64;
    static final int PROHIBIT_HEAD_CHARACTER_OPTIMIZATION = 128;
    static final int PROHIBIT_FIXED_STRING_OPTIMIZATION = 256;
    static final int XMLSCHEMA_MODE = 512;
    static final int SPECIAL_COMMA = 1024;
    private static final int WT_IGNORE = 0;
    private static final int WT_LETTER = 1;
    private static final int WT_OTHER = 2;
    static final int LINE_FEED = 10;
    static final int CARRIAGE_RETURN = 13;
    static final int LINE_SEPARATOR = 8232;
    static final int PARAGRAPH_SEPARATOR = 8233;

    private synchronized void compile(Token tok) {
        if (this.operations != null) {
            return;
        }
        this.numberOfClosures = 0;
        this.operations = compile(tok, null, false);
    }

    private Op compile(Token tok, Op next, boolean reverse) {
        Op ret;
        Op.ChildOp op;
        switch (tok.type) {
            case 0:
                ret = Op.createChar(tok.getChar());
                ret.next = next;
                break;
            case 1:
                ret = next;
                if (!reverse) {
                    for (int i2 = tok.size() - 1; i2 >= 0; i2--) {
                        ret = compile(tok.getChild(i2), ret, false);
                    }
                    break;
                } else {
                    for (int i3 = 0; i3 < tok.size(); i3++) {
                        ret = compile(tok.getChild(i3), ret, true);
                    }
                    break;
                }
            case 2:
                Op.UnionOp uni = Op.createUnion(tok.size());
                for (int i4 = 0; i4 < tok.size(); i4++) {
                    uni.addElement(compile(tok.getChild(i4), next, reverse));
                }
                ret = uni;
                break;
            case 3:
            case 9:
                Token child = tok.getChild(0);
                int min = tok.getMin();
                int max = tok.getMax();
                if (min >= 0 && min == max) {
                    ret = next;
                    for (int i5 = 0; i5 < min; i5++) {
                        ret = compile(child, ret, reverse);
                    }
                    break;
                } else {
                    if (min > 0 && max > 0) {
                        max -= min;
                    }
                    if (max > 0) {
                        ret = next;
                        for (int i6 = 0; i6 < max; i6++) {
                            Op.ChildOp q2 = Op.createQuestion(tok.type == 9);
                            q2.next = next;
                            q2.setChild(compile(child, ret, reverse));
                            ret = q2;
                        }
                    } else {
                        if (tok.type == 9) {
                            op = Op.createNonGreedyClosure();
                        } else {
                            int i7 = this.numberOfClosures;
                            this.numberOfClosures = i7 + 1;
                            op = Op.createClosure(i7);
                        }
                        op.next = next;
                        op.setChild(compile(child, op, reverse));
                        ret = op;
                    }
                    if (min > 0) {
                        for (int i8 = 0; i8 < min; i8++) {
                            ret = compile(child, ret, reverse);
                        }
                        break;
                    }
                }
                break;
            case 4:
            case 5:
                ret = Op.createRange(tok);
                ret.next = next;
                break;
            case 6:
                if (tok.getParenNumber() == 0) {
                    ret = compile(tok.getChild(0), next, reverse);
                    break;
                } else if (reverse) {
                    ret = Op.createCapture(-tok.getParenNumber(), compile(tok.getChild(0), Op.createCapture(tok.getParenNumber(), next), reverse));
                    break;
                } else {
                    ret = Op.createCapture(tok.getParenNumber(), compile(tok.getChild(0), Op.createCapture(-tok.getParenNumber(), next), reverse));
                    break;
                }
            case 7:
                ret = next;
                break;
            case 8:
                ret = Op.createAnchor(tok.getChar());
                ret.next = next;
                break;
            case 10:
                ret = Op.createString(tok.getString());
                ret.next = next;
                break;
            case 11:
                ret = Op.createDot();
                ret.next = next;
                break;
            case 12:
                ret = Op.createBackReference(tok.getReferenceNumber());
                ret.next = next;
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
                throw new RuntimeException("Unknown token type: " + tok.type);
            case 20:
                ret = Op.createLook(20, next, compile(tok.getChild(0), null, false));
                break;
            case 21:
                ret = Op.createLook(21, next, compile(tok.getChild(0), null, false));
                break;
            case 22:
                ret = Op.createLook(22, next, compile(tok.getChild(0), null, true));
                break;
            case 23:
                ret = Op.createLook(23, next, compile(tok.getChild(0), null, true));
                break;
            case 24:
                ret = Op.createIndependent(next, compile(tok.getChild(0), null, reverse));
                break;
            case 25:
                ret = Op.createModifier(next, compile(tok.getChild(0), null, reverse), ((Token.ModifierToken) tok).getOptions(), ((Token.ModifierToken) tok).getOptionsMask());
                break;
            case 26:
                Token.ConditionToken ctok = (Token.ConditionToken) tok;
                int ref = ctok.refNumber;
                Op condition = ctok.condition == null ? null : compile(ctok.condition, null, reverse);
                Op yes = compile(ctok.yes, next, reverse);
                Op no = ctok.no == null ? null : compile(ctok.no, next, reverse);
                ret = Op.createCondition(next, ref, condition, yes, no);
                break;
        }
        return ret;
    }

    public boolean matches(char[] target) {
        return matches(target, 0, target.length, (Match) null);
    }

    public boolean matches(char[] target, int start, int end) {
        return matches(target, start, end, (Match) null);
    }

    public boolean matches(char[] target, Match match) {
        return matches(target, 0, target.length, match);
    }

    public boolean matches(char[] target, int start, int end, Match match) {
        Context con;
        int matchStart;
        boolean z2;
        synchronized (this) {
            if (this.operations == null) {
                prepare();
            }
            if (this.context == null) {
                this.context = new Context();
            }
        }
        synchronized (this.context) {
            con = this.context.inuse ? new Context() : this.context;
            con.reset(target, start, end, this.numberOfClosures);
        }
        if (match != null) {
            match.setNumberOfGroups(this.nofparen);
            match.setSource(target);
        } else if (this.hasBackReferences) {
            match = new Match();
            match.setNumberOfGroups(this.nofparen);
        }
        con.match = match;
        if (isSet(this.options, 512)) {
            int matchEnd = match(con, this.operations, con.start, 1, this.options);
            if (matchEnd == con.limit) {
                if (con.match != null) {
                    con.match.setBeginning(0, con.start);
                    con.match.setEnd(0, matchEnd);
                }
                con.setInUse(false);
                return true;
            }
            return false;
        }
        if (this.fixedStringOnly) {
            int o2 = this.fixedStringTable.matches(target, con.start, con.limit);
            if (o2 >= 0) {
                if (con.match != null) {
                    con.match.setBeginning(0, o2);
                    con.match.setEnd(0, o2 + this.fixedString.length());
                }
                con.setInUse(false);
                return true;
            }
            con.setInUse(false);
            return false;
        }
        if (this.fixedString != null && this.fixedStringTable.matches(target, con.start, con.limit) < 0) {
            con.setInUse(false);
            return false;
        }
        int limit = con.limit - this.minlength;
        int matchEnd2 = -1;
        if (this.operations != null && this.operations.type == 7 && this.operations.getChild().type == 0) {
            if (isSet(this.options, 4)) {
                matchStart = con.start;
                matchEnd2 = match(con, this.operations, con.start, 1, this.options);
            } else {
                boolean previousIsEOL = true;
                matchStart = con.start;
                while (matchStart <= limit) {
                    if (isEOLChar(target[matchStart])) {
                        z2 = true;
                    } else {
                        if (previousIsEOL) {
                            int iMatch = match(con, this.operations, matchStart, 1, this.options);
                            matchEnd2 = iMatch;
                            if (0 <= iMatch) {
                                break;
                            }
                        }
                        z2 = false;
                    }
                    previousIsEOL = z2;
                    matchStart++;
                }
            }
        } else if (this.firstChar != null) {
            RangeToken range = this.firstChar;
            matchStart = con.start;
            while (matchStart <= limit) {
                int ch = target[matchStart];
                if (REUtil.isHighSurrogate(ch) && matchStart + 1 < con.limit) {
                    ch = REUtil.composeFromSurrogates(ch, target[matchStart + 1]);
                }
                if (range.match(ch)) {
                    int iMatch2 = match(con, this.operations, matchStart, 1, this.options);
                    matchEnd2 = iMatch2;
                    if (0 <= iMatch2) {
                        break;
                    }
                }
                matchStart++;
            }
        } else {
            matchStart = con.start;
            while (matchStart <= limit) {
                int iMatch3 = match(con, this.operations, matchStart, 1, this.options);
                matchEnd2 = iMatch3;
                if (0 <= iMatch3) {
                    break;
                }
                matchStart++;
            }
        }
        if (matchEnd2 >= 0) {
            if (con.match != null) {
                con.match.setBeginning(0, matchStart);
                con.match.setEnd(0, matchEnd2);
            }
            con.setInUse(false);
            return true;
        }
        con.setInUse(false);
        return false;
    }

    public boolean matches(String target) {
        return matches(target, 0, target.length(), (Match) null);
    }

    public boolean matches(String target, int start, int end) {
        return matches(target, start, end, (Match) null);
    }

    public boolean matches(String target, Match match) {
        return matches(target, 0, target.length(), match);
    }

    public boolean matches(String target, int start, int end, Match match) {
        Context con;
        int matchStart;
        boolean z2;
        synchronized (this) {
            if (this.operations == null) {
                prepare();
            }
            if (this.context == null) {
                this.context = new Context();
            }
        }
        synchronized (this.context) {
            con = this.context.inuse ? new Context() : this.context;
            con.reset(target, start, end, this.numberOfClosures);
        }
        if (match != null) {
            match.setNumberOfGroups(this.nofparen);
            match.setSource(target);
        } else if (this.hasBackReferences) {
            match = new Match();
            match.setNumberOfGroups(this.nofparen);
        }
        con.match = match;
        if (isSet(this.options, 512)) {
            int matchEnd = match(con, this.operations, con.start, 1, this.options);
            if (matchEnd == con.limit) {
                if (con.match != null) {
                    con.match.setBeginning(0, con.start);
                    con.match.setEnd(0, matchEnd);
                }
                con.setInUse(false);
                return true;
            }
            return false;
        }
        if (this.fixedStringOnly) {
            int o2 = this.fixedStringTable.matches(target, con.start, con.limit);
            if (o2 >= 0) {
                if (con.match != null) {
                    con.match.setBeginning(0, o2);
                    con.match.setEnd(0, o2 + this.fixedString.length());
                }
                con.setInUse(false);
                return true;
            }
            con.setInUse(false);
            return false;
        }
        if (this.fixedString != null && this.fixedStringTable.matches(target, con.start, con.limit) < 0) {
            con.setInUse(false);
            return false;
        }
        int limit = con.limit - this.minlength;
        int matchEnd2 = -1;
        if (this.operations != null && this.operations.type == 7 && this.operations.getChild().type == 0) {
            if (isSet(this.options, 4)) {
                matchStart = con.start;
                matchEnd2 = match(con, this.operations, con.start, 1, this.options);
            } else {
                boolean previousIsEOL = true;
                matchStart = con.start;
                while (matchStart <= limit) {
                    if (isEOLChar(target.charAt(matchStart))) {
                        z2 = true;
                    } else {
                        if (previousIsEOL) {
                            int iMatch = match(con, this.operations, matchStart, 1, this.options);
                            matchEnd2 = iMatch;
                            if (0 <= iMatch) {
                                break;
                            }
                        }
                        z2 = false;
                    }
                    previousIsEOL = z2;
                    matchStart++;
                }
            }
        } else if (this.firstChar != null) {
            RangeToken range = this.firstChar;
            matchStart = con.start;
            while (matchStart <= limit) {
                int ch = target.charAt(matchStart);
                if (REUtil.isHighSurrogate(ch) && matchStart + 1 < con.limit) {
                    ch = REUtil.composeFromSurrogates(ch, target.charAt(matchStart + 1));
                }
                if (range.match(ch)) {
                    int iMatch2 = match(con, this.operations, matchStart, 1, this.options);
                    matchEnd2 = iMatch2;
                    if (0 <= iMatch2) {
                        break;
                    }
                }
                matchStart++;
            }
        } else {
            matchStart = con.start;
            while (matchStart <= limit) {
                int iMatch3 = match(con, this.operations, matchStart, 1, this.options);
                matchEnd2 = iMatch3;
                if (0 <= iMatch3) {
                    break;
                }
                matchStart++;
            }
        }
        if (matchEnd2 >= 0) {
            if (con.match != null) {
                con.match.setBeginning(0, matchStart);
                con.match.setEnd(0, matchEnd2);
            }
            con.setInUse(false);
            return true;
        }
        con.setInUse(false);
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x02af, code lost:
    
        throw new java.lang.RuntimeException("Internal Error: Reference number must be more than zero: " + r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:245:0x06f1 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0599 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int match(com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.Context r8, com.sun.org.apache.xerces.internal.impl.xpath.regex.Op r9, int r10, int r11, int r12) {
        /*
            Method dump skipped, instructions count: 1844
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.match(com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression$Context, com.sun.org.apache.xerces.internal.impl.xpath.regex.Op, int, int, int):int");
    }

    private boolean matchChar(int ch, int other, boolean ignoreCase) {
        return ignoreCase ? matchIgnoreCase(ch, other) : ch == other;
    }

    boolean matchAnchor(ExpressionTarget target, Op op, Context con, int offset, int opts) {
        boolean go;
        int after;
        switch (op.getData()) {
            case 36:
                if (isSet(opts, 8)) {
                    if (offset != con.limit) {
                        if (offset >= con.limit || !isEOLChar(target.charAt(offset))) {
                        }
                    }
                } else if (offset != con.limit) {
                    if (offset + 1 != con.limit || !isEOLChar(target.charAt(offset))) {
                        if (offset + 2 != con.limit || target.charAt(offset) != '\r' || target.charAt(offset + 1) != '\n') {
                        }
                    }
                }
                break;
            case 60:
                if (con.length == 0 || offset == con.limit || getWordType(target, con.start, con.limit, offset, opts) != 1 || getPreviousWordType(target, con.start, con.limit, offset, opts) != 2) {
                }
                break;
            case 62:
                if (con.length == 0 || offset == con.start || getWordType(target, con.start, con.limit, offset, opts) != 2 || getPreviousWordType(target, con.start, con.limit, offset, opts) != 1) {
                }
                break;
            case 64:
                if (offset != con.start) {
                    if (offset <= con.start || !isEOLChar(target.charAt(offset - 1))) {
                    }
                }
                break;
            case 65:
                if (offset != con.start) {
                }
                break;
            case 66:
                if (con.length == 0) {
                    go = true;
                } else {
                    int after2 = getWordType(target, con.start, con.limit, offset, opts);
                    go = after2 == 0 || after2 == getPreviousWordType(target, con.start, con.limit, offset, opts);
                }
                if (!go) {
                }
                break;
            case 90:
                if (offset != con.limit) {
                    if (offset + 1 != con.limit || !isEOLChar(target.charAt(offset))) {
                        if (offset + 2 != con.limit || target.charAt(offset) != '\r' || target.charAt(offset + 1) != '\n') {
                        }
                    }
                }
                break;
            case 94:
                if (isSet(opts, 8)) {
                    if (offset != con.start) {
                        if (offset <= con.start || offset >= con.limit || !isEOLChar(target.charAt(offset - 1))) {
                        }
                    }
                } else if (offset != con.start) {
                }
                break;
            case 98:
                if (con.length != 0 && (after = getWordType(target, con.start, con.limit, offset, opts)) != 0) {
                    int before = getPreviousWordType(target, con.start, con.limit, offset, opts);
                    if (after == before) {
                    }
                }
                break;
            case 122:
                if (offset != con.limit) {
                }
                break;
        }
        return false;
    }

    private static final int getPreviousWordType(ExpressionTarget target, int begin, int end, int offset, int opts) {
        int offset2 = offset - 1;
        int wordType = getWordType(target, begin, end, offset2, opts);
        while (true) {
            int ret = wordType;
            if (ret == 0) {
                offset2--;
                wordType = getWordType(target, begin, end, offset2, opts);
            } else {
                return ret;
            }
        }
    }

    private static final int getWordType(ExpressionTarget target, int begin, int end, int offset, int opts) {
        if (offset < begin || offset >= end) {
            return 2;
        }
        return getWordType0(target.charAt(offset), opts);
    }

    public boolean matches(CharacterIterator target) {
        return matches(target, (Match) null);
    }

    public boolean matches(CharacterIterator target, Match match) {
        Context con;
        int matchStart;
        boolean z2;
        int start = target.getBeginIndex();
        int end = target.getEndIndex();
        synchronized (this) {
            if (this.operations == null) {
                prepare();
            }
            if (this.context == null) {
                this.context = new Context();
            }
        }
        synchronized (this.context) {
            con = this.context.inuse ? new Context() : this.context;
            con.reset(target, start, end, this.numberOfClosures);
        }
        if (match != null) {
            match.setNumberOfGroups(this.nofparen);
            match.setSource(target);
        } else if (this.hasBackReferences) {
            match = new Match();
            match.setNumberOfGroups(this.nofparen);
        }
        con.match = match;
        if (isSet(this.options, 512)) {
            int matchEnd = match(con, this.operations, con.start, 1, this.options);
            if (matchEnd == con.limit) {
                if (con.match != null) {
                    con.match.setBeginning(0, con.start);
                    con.match.setEnd(0, matchEnd);
                }
                con.setInUse(false);
                return true;
            }
            return false;
        }
        if (this.fixedStringOnly) {
            int o2 = this.fixedStringTable.matches(target, con.start, con.limit);
            if (o2 >= 0) {
                if (con.match != null) {
                    con.match.setBeginning(0, o2);
                    con.match.setEnd(0, o2 + this.fixedString.length());
                }
                con.setInUse(false);
                return true;
            }
            con.setInUse(false);
            return false;
        }
        if (this.fixedString != null && this.fixedStringTable.matches(target, con.start, con.limit) < 0) {
            con.setInUse(false);
            return false;
        }
        int limit = con.limit - this.minlength;
        int matchEnd2 = -1;
        if (this.operations != null && this.operations.type == 7 && this.operations.getChild().type == 0) {
            if (isSet(this.options, 4)) {
                matchStart = con.start;
                matchEnd2 = match(con, this.operations, con.start, 1, this.options);
            } else {
                boolean previousIsEOL = true;
                matchStart = con.start;
                while (matchStart <= limit) {
                    if (isEOLChar(target.setIndex(matchStart))) {
                        z2 = true;
                    } else {
                        if (previousIsEOL) {
                            int iMatch = match(con, this.operations, matchStart, 1, this.options);
                            matchEnd2 = iMatch;
                            if (0 <= iMatch) {
                                break;
                            }
                        }
                        z2 = false;
                    }
                    previousIsEOL = z2;
                    matchStart++;
                }
            }
        } else if (this.firstChar != null) {
            RangeToken range = this.firstChar;
            matchStart = con.start;
            while (matchStart <= limit) {
                int ch = target.setIndex(matchStart);
                if (REUtil.isHighSurrogate(ch) && matchStart + 1 < con.limit) {
                    ch = REUtil.composeFromSurrogates(ch, target.setIndex(matchStart + 1));
                }
                if (range.match(ch)) {
                    int iMatch2 = match(con, this.operations, matchStart, 1, this.options);
                    matchEnd2 = iMatch2;
                    if (0 <= iMatch2) {
                        break;
                    }
                }
                matchStart++;
            }
        } else {
            matchStart = con.start;
            while (matchStart <= limit) {
                int iMatch3 = match(con, this.operations, matchStart, 1, this.options);
                matchEnd2 = iMatch3;
                if (0 <= iMatch3) {
                    break;
                }
                matchStart++;
            }
        }
        if (matchEnd2 >= 0) {
            if (con.match != null) {
                con.match.setBeginning(0, matchStart);
                con.match.setEnd(0, matchEnd2);
            }
            con.setInUse(false);
            return true;
        }
        con.setInUse(false);
        return false;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegularExpression$ExpressionTarget.class */
    static abstract class ExpressionTarget {
        abstract char charAt(int i2);

        abstract boolean regionMatches(boolean z2, int i2, int i3, String str, int i4);

        abstract boolean regionMatches(boolean z2, int i2, int i3, int i4, int i5);

        ExpressionTarget() {
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegularExpression$StringTarget.class */
    static final class StringTarget extends ExpressionTarget {
        private String target;

        StringTarget(String target) {
            this.target = target;
        }

        final void resetTarget(String target) {
            this.target = target;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final char charAt(int index) {
            return this.target.charAt(index);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final boolean regionMatches(boolean ignoreCase, int offset, int limit, String part, int partlen) {
            if (limit - offset < partlen) {
                return false;
            }
            return ignoreCase ? this.target.regionMatches(true, offset, part, 0, partlen) : this.target.regionMatches(offset, part, 0, partlen);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final boolean regionMatches(boolean ignoreCase, int offset, int limit, int offset2, int partlen) {
            if (limit - offset < partlen) {
                return false;
            }
            return ignoreCase ? this.target.regionMatches(true, offset, this.target, offset2, partlen) : this.target.regionMatches(offset, this.target, offset2, partlen);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegularExpression$CharArrayTarget.class */
    static final class CharArrayTarget extends ExpressionTarget {
        char[] target;

        CharArrayTarget(char[] target) {
            this.target = target;
        }

        final void resetTarget(char[] target) {
            this.target = target;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        char charAt(int index) {
            return this.target[index];
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final boolean regionMatches(boolean ignoreCase, int offset, int limit, String part, int partlen) {
            if (offset < 0 || limit - offset < partlen) {
                return false;
            }
            return ignoreCase ? regionMatchesIgnoreCase(offset, limit, part, partlen) : regionMatches(offset, limit, part, partlen);
        }

        private final boolean regionMatches(int offset, int limit, String part, int partlen) {
            int i2;
            int i3;
            int i4 = 0;
            do {
                int i5 = partlen;
                partlen--;
                if (i5 <= 0) {
                    return true;
                }
                i2 = offset;
                offset++;
                i3 = i4;
                i4++;
            } while (this.target[i2] == part.charAt(i3));
            return false;
        }

        private final boolean regionMatchesIgnoreCase(int offset, int limit, String part, int partlen) {
            char uch1;
            char uch2;
            int i2 = 0;
            while (true) {
                int i3 = partlen;
                partlen--;
                if (i3 > 0) {
                    int i4 = offset;
                    offset++;
                    char ch1 = this.target[i4];
                    int i5 = i2;
                    i2++;
                    char ch2 = part.charAt(i5);
                    if (ch1 != ch2 && (uch1 = Character.toUpperCase(ch1)) != (uch2 = Character.toUpperCase(ch2)) && Character.toLowerCase(uch1) != Character.toLowerCase(uch2)) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final boolean regionMatches(boolean ignoreCase, int offset, int limit, int offset2, int partlen) {
            if (offset < 0 || limit - offset < partlen) {
                return false;
            }
            return ignoreCase ? regionMatchesIgnoreCase(offset, limit, offset2, partlen) : regionMatches(offset, limit, offset2, partlen);
        }

        private final boolean regionMatches(int offset, int limit, int offset2, int partlen) {
            int i2;
            int i3;
            int i4 = offset2;
            do {
                int i5 = partlen;
                partlen--;
                if (i5 <= 0) {
                    return true;
                }
                i2 = offset;
                offset++;
                i3 = i4;
                i4++;
            } while (this.target[i2] == this.target[i3]);
            return false;
        }

        private final boolean regionMatchesIgnoreCase(int offset, int limit, int offset2, int partlen) {
            char uch1;
            char uch2;
            int i2 = offset2;
            while (true) {
                int i3 = partlen;
                partlen--;
                if (i3 > 0) {
                    int i4 = offset;
                    offset++;
                    char ch1 = this.target[i4];
                    int i5 = i2;
                    i2++;
                    char ch2 = this.target[i5];
                    if (ch1 != ch2 && (uch1 = Character.toUpperCase(ch1)) != (uch2 = Character.toUpperCase(ch2)) && Character.toLowerCase(uch1) != Character.toLowerCase(uch2)) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegularExpression$CharacterIteratorTarget.class */
    static final class CharacterIteratorTarget extends ExpressionTarget {
        CharacterIterator target;

        CharacterIteratorTarget(CharacterIterator target) {
            this.target = target;
        }

        final void resetTarget(CharacterIterator target) {
            this.target = target;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final char charAt(int index) {
            return this.target.setIndex(index);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final boolean regionMatches(boolean ignoreCase, int offset, int limit, String part, int partlen) {
            if (offset < 0 || limit - offset < partlen) {
                return false;
            }
            return ignoreCase ? regionMatchesIgnoreCase(offset, limit, part, partlen) : regionMatches(offset, limit, part, partlen);
        }

        private final boolean regionMatches(int offset, int limit, String part, int partlen) {
            int i2;
            int i3;
            int i4 = 0;
            do {
                int i5 = partlen;
                partlen--;
                if (i5 <= 0) {
                    return true;
                }
                i2 = offset;
                offset++;
                i3 = i4;
                i4++;
            } while (this.target.setIndex(i2) == part.charAt(i3));
            return false;
        }

        private final boolean regionMatchesIgnoreCase(int offset, int limit, String part, int partlen) {
            char uch1;
            char uch2;
            int i2 = 0;
            while (true) {
                int i3 = partlen;
                partlen--;
                if (i3 > 0) {
                    int i4 = offset;
                    offset++;
                    char ch1 = this.target.setIndex(i4);
                    int i5 = i2;
                    i2++;
                    char ch2 = part.charAt(i5);
                    if (ch1 != ch2 && (uch1 = Character.toUpperCase(ch1)) != (uch2 = Character.toUpperCase(ch2)) && Character.toLowerCase(uch1) != Character.toLowerCase(uch2)) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression.ExpressionTarget
        final boolean regionMatches(boolean ignoreCase, int offset, int limit, int offset2, int partlen) {
            if (offset < 0 || limit - offset < partlen) {
                return false;
            }
            return ignoreCase ? regionMatchesIgnoreCase(offset, limit, offset2, partlen) : regionMatches(offset, limit, offset2, partlen);
        }

        private final boolean regionMatches(int offset, int limit, int offset2, int partlen) {
            int i2;
            int i3;
            int i4 = offset2;
            do {
                int i5 = partlen;
                partlen--;
                if (i5 <= 0) {
                    return true;
                }
                i2 = offset;
                offset++;
                i3 = i4;
                i4++;
            } while (this.target.setIndex(i2) == this.target.setIndex(i3));
            return false;
        }

        private final boolean regionMatchesIgnoreCase(int offset, int limit, int offset2, int partlen) {
            char uch1;
            char uch2;
            int i2 = offset2;
            while (true) {
                int i3 = partlen;
                partlen--;
                if (i3 > 0) {
                    int i4 = offset;
                    offset++;
                    char ch1 = this.target.setIndex(i4);
                    int i5 = i2;
                    i2++;
                    char ch2 = this.target.setIndex(i5);
                    if (ch1 != ch2 && (uch1 = Character.toUpperCase(ch1)) != (uch2 = Character.toUpperCase(ch2)) && Character.toLowerCase(uch1) != Character.toLowerCase(uch2)) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegularExpression$ClosureContext.class */
    static final class ClosureContext {
        int[] offsets = new int[4];
        int currentIndex = 0;

        ClosureContext() {
        }

        boolean contains(int offset) {
            for (int i2 = 0; i2 < this.currentIndex; i2++) {
                if (this.offsets[i2] == offset) {
                    return true;
                }
            }
            return false;
        }

        void reset() {
            this.currentIndex = 0;
        }

        void addOffset(int offset) {
            if (this.currentIndex == this.offsets.length) {
                this.offsets = expandOffsets();
            }
            int[] iArr = this.offsets;
            int i2 = this.currentIndex;
            this.currentIndex = i2 + 1;
            iArr[i2] = offset;
        }

        private int[] expandOffsets() {
            int len = this.offsets.length;
            int newLen = len << 1;
            int[] newOffsets = new int[newLen];
            System.arraycopy(this.offsets, 0, newOffsets, 0, this.currentIndex);
            return newOffsets;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegularExpression$Context.class */
    static final class Context {
        int start;
        int limit;
        int length;
        Match match;
        boolean inuse = false;
        ClosureContext[] closureContexts;
        private StringTarget stringTarget;
        private CharArrayTarget charArrayTarget;
        private CharacterIteratorTarget characterIteratorTarget;
        ExpressionTarget target;

        Context() {
        }

        private void resetCommon(int nofclosures) {
            this.length = this.limit - this.start;
            setInUse(true);
            this.match = null;
            if (this.closureContexts == null || this.closureContexts.length != nofclosures) {
                this.closureContexts = new ClosureContext[nofclosures];
            }
            for (int i2 = 0; i2 < nofclosures; i2++) {
                if (this.closureContexts[i2] == null) {
                    this.closureContexts[i2] = new ClosureContext();
                } else {
                    this.closureContexts[i2].reset();
                }
            }
        }

        void reset(CharacterIterator target, int start, int limit, int nofclosures) {
            if (this.characterIteratorTarget == null) {
                this.characterIteratorTarget = new CharacterIteratorTarget(target);
            } else {
                this.characterIteratorTarget.resetTarget(target);
            }
            this.target = this.characterIteratorTarget;
            this.start = start;
            this.limit = limit;
            resetCommon(nofclosures);
        }

        void reset(String target, int start, int limit, int nofclosures) {
            if (this.stringTarget == null) {
                this.stringTarget = new StringTarget(target);
            } else {
                this.stringTarget.resetTarget(target);
            }
            this.target = this.stringTarget;
            this.start = start;
            this.limit = limit;
            resetCommon(nofclosures);
        }

        void reset(char[] target, int start, int limit, int nofclosures) {
            if (this.charArrayTarget == null) {
                this.charArrayTarget = new CharArrayTarget(target);
            } else {
                this.charArrayTarget.resetTarget(target);
            }
            this.target = this.charArrayTarget;
            this.start = start;
            this.limit = limit;
            resetCommon(nofclosures);
        }

        synchronized void setInUse(boolean inUse) {
            this.inuse = inUse;
        }
    }

    void prepare() {
        compile(this.tokentree);
        this.minlength = this.tokentree.getMinLength();
        this.firstChar = null;
        if (!isSet(this.options, 128) && !isSet(this.options, 512)) {
            RangeToken firstChar = Token.createRange();
            int fresult = this.tokentree.analyzeFirstCharacter(firstChar, this.options);
            if (fresult == 1) {
                firstChar.compactRanges();
                this.firstChar = firstChar;
            }
        }
        if (this.operations != null && ((this.operations.type == 6 || this.operations.type == 1) && this.operations.next == null)) {
            this.fixedStringOnly = true;
            if (this.operations.type == 6) {
                this.fixedString = this.operations.getString();
            } else if (this.operations.getData() >= 65536) {
                this.fixedString = REUtil.decomposeToSurrogates(this.operations.getData());
            } else {
                char[] ac2 = {(char) this.operations.getData()};
                this.fixedString = new String(ac2);
            }
            this.fixedStringOptions = this.options;
            this.fixedStringTable = new BMPattern(this.fixedString, 256, isSet(this.fixedStringOptions, 2));
            return;
        }
        if (!isSet(this.options, 256) && !isSet(this.options, 512)) {
            Token.FixedStringContainer container = new Token.FixedStringContainer();
            this.tokentree.findFixedString(container, this.options);
            this.fixedString = container.token == null ? null : container.token.getString();
            this.fixedStringOptions = container.options;
            if (this.fixedString != null && this.fixedString.length() < 2) {
                this.fixedString = null;
            }
            if (this.fixedString != null) {
                this.fixedStringTable = new BMPattern(this.fixedString, 256, isSet(this.fixedStringOptions, 2));
            }
        }
    }

    private static final boolean isSet(int options, int flag) {
        return (options & flag) == flag;
    }

    public RegularExpression(String regex) throws ParseException {
        this(regex, null);
    }

    public RegularExpression(String regex, String options) throws ParseException {
        this.hasBackReferences = false;
        this.operations = null;
        this.context = null;
        this.firstChar = null;
        this.fixedString = null;
        this.fixedStringTable = null;
        this.fixedStringOnly = false;
        setPattern(regex, options);
    }

    public RegularExpression(String regex, String options, Locale locale) throws ParseException {
        this.hasBackReferences = false;
        this.operations = null;
        this.context = null;
        this.firstChar = null;
        this.fixedString = null;
        this.fixedStringTable = null;
        this.fixedStringOnly = false;
        setPattern(regex, options, locale);
    }

    RegularExpression(String regex, Token tok, int parens, boolean hasBackReferences, int options) {
        this.hasBackReferences = false;
        this.operations = null;
        this.context = null;
        this.firstChar = null;
        this.fixedString = null;
        this.fixedStringTable = null;
        this.fixedStringOnly = false;
        this.regex = regex;
        this.tokentree = tok;
        this.nofparen = parens;
        this.options = options;
        this.hasBackReferences = hasBackReferences;
    }

    public void setPattern(String newPattern) throws ParseException {
        setPattern(newPattern, Locale.getDefault());
    }

    public void setPattern(String newPattern, Locale locale) throws ParseException {
        setPattern(newPattern, this.options, locale);
    }

    private void setPattern(String newPattern, int options, Locale locale) throws ParseException {
        this.regex = newPattern;
        this.options = options;
        RegexParser rp = isSet(this.options, 512) ? new ParserForXMLSchema(locale) : new RegexParser(locale);
        this.tokentree = rp.parse(this.regex, this.options);
        this.nofparen = rp.parennumber;
        this.hasBackReferences = rp.hasBackReferences;
        this.operations = null;
        this.context = null;
    }

    public void setPattern(String newPattern, String options) throws ParseException {
        setPattern(newPattern, options, Locale.getDefault());
    }

    public void setPattern(String newPattern, String options, Locale locale) throws ParseException {
        setPattern(newPattern, REUtil.parseOptions(options), locale);
    }

    public String getPattern() {
        return this.regex;
    }

    public String toString() {
        return this.tokentree.toString(this.options);
    }

    public String getOptions() {
        return REUtil.createOptionString(this.options);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RegularExpression)) {
            return false;
        }
        RegularExpression r2 = (RegularExpression) obj;
        return this.regex.equals(r2.regex) && this.options == r2.options;
    }

    boolean equals(String pattern, int options) {
        return this.regex.equals(pattern) && this.options == options;
    }

    public int hashCode() {
        return (this.regex + "/" + getOptions()).hashCode();
    }

    public int getNumberOfGroups() {
        return this.nofparen;
    }

    private static final int getWordType0(char ch, int opts) {
        if (!isSet(opts, 64)) {
            return isSet(opts, 32) ? Token.getRange("IsWord", true).match(ch) ? 1 : 2 : isWordChar(ch) ? 1 : 2;
        }
        switch (Character.getType(ch)) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 8:
            case 9:
            case 10:
            case 11:
                return 1;
            case 6:
            case 7:
            case 16:
                return 0;
            case 12:
            case 13:
            case 14:
            default:
                return 2;
            case 15:
                switch (ch) {
                    case '\t':
                    case '\n':
                    case 11:
                    case '\f':
                    case '\r':
                        return 2;
                    default:
                        return 0;
                }
        }
    }

    private static final boolean isEOLChar(int ch) {
        return ch == 10 || ch == 13 || ch == LINE_SEPARATOR || ch == PARAGRAPH_SEPARATOR;
    }

    private static final boolean isWordChar(int ch) {
        if (ch == 95) {
            return true;
        }
        if (ch < 48 || ch > 122) {
            return false;
        }
        if (ch <= 57) {
            return true;
        }
        if (ch < 65) {
            return false;
        }
        return ch <= 90 || ch >= 97;
    }

    private static final boolean matchIgnoreCase(int chardata, int ch) {
        if (chardata == ch) {
            return true;
        }
        if (chardata > 65535 || ch > 65535) {
            return false;
        }
        char uch1 = Character.toUpperCase((char) chardata);
        char uch2 = Character.toUpperCase((char) ch);
        return uch1 == uch2 || Character.toLowerCase(uch1) == Character.toLowerCase(uch2);
    }
}
