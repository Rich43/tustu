package com.sun.org.apache.xerces.internal.impl.xpath.regex;

import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegexParser.class */
class RegexParser {
    static final int T_CHAR = 0;
    static final int T_EOF = 1;
    static final int T_OR = 2;
    static final int T_STAR = 3;
    static final int T_PLUS = 4;
    static final int T_QUESTION = 5;
    static final int T_LPAREN = 6;
    static final int T_RPAREN = 7;
    static final int T_DOT = 8;
    static final int T_LBRACKET = 9;
    static final int T_BACKSOLIDUS = 10;
    static final int T_CARET = 11;
    static final int T_DOLLAR = 12;
    static final int T_LPAREN2 = 13;
    static final int T_LOOKAHEAD = 14;
    static final int T_NEGATIVELOOKAHEAD = 15;
    static final int T_LOOKBEHIND = 16;
    static final int T_NEGATIVELOOKBEHIND = 17;
    static final int T_INDEPENDENT = 18;
    static final int T_SET_OPERATIONS = 19;
    static final int T_POSIX_CHARCLASS_START = 20;
    static final int T_COMMENT = 21;
    static final int T_MODIFIERS = 22;
    static final int T_CONDITION = 23;
    static final int T_XMLSCHEMA_CC_SUBTRACTION = 24;
    int offset;
    String regex;
    int regexlen;
    int options;
    ResourceBundle resources;
    int chardata;
    int nexttoken;
    protected static final int S_NORMAL = 0;
    protected static final int S_INBRACKETS = 1;
    protected static final int S_INXBRACKETS = 2;
    boolean hasBackReferences;
    int context = 0;
    int parenOpened = 1;
    int parennumber = 1;
    Vector references = null;
    int parenCount = 0;

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/RegexParser$ReferencePosition.class */
    static class ReferencePosition {
        int refNumber;
        int position;

        ReferencePosition(int n2, int pos) {
            this.refNumber = n2;
            this.position = pos;
        }
    }

    public RegexParser() {
        setLocale(Locale.getDefault());
    }

    public RegexParser(Locale locale) {
        setLocale(locale);
    }

    public void setLocale(Locale locale) {
        try {
            if (locale != null) {
                this.resources = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.xpath.regex.message", locale);
            } else {
                this.resources = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.xpath.regex.message");
            }
        } catch (MissingResourceException mre) {
            throw new RuntimeException("Installation Problem???  Couldn't load messages: " + mre.getMessage());
        }
    }

    final ParseException ex(String key, int loc) {
        return new ParseException(this.resources.getString(key), loc);
    }

    protected final boolean isSet(int flag) {
        return (this.options & flag) == flag;
    }

    synchronized Token parse(String regex, int options) throws ParseException {
        this.options = options;
        this.offset = 0;
        setContext(0);
        this.parennumber = 1;
        this.parenOpened = 1;
        this.hasBackReferences = false;
        this.regex = regex;
        if (isSet(16)) {
            this.regex = REUtil.stripExtendedComment(this.regex);
        }
        this.regexlen = this.regex.length();
        next();
        Token ret = parseRegex();
        if (this.offset != this.regexlen) {
            throw ex("parser.parse.1", this.offset);
        }
        if (this.parenCount < 0) {
            throw ex("parser.factor.0", this.offset);
        }
        if (this.references != null) {
            for (int i2 = 0; i2 < this.references.size(); i2++) {
                ReferencePosition position = (ReferencePosition) this.references.elementAt(i2);
                if (this.parennumber <= position.refNumber) {
                    throw ex("parser.parse.2", position.position);
                }
            }
            this.references.removeAllElements();
        }
        return ret;
    }

    protected final void setContext(int con) {
        this.context = con;
    }

    final int read() {
        return this.nexttoken;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0323  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0330  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final void next() {
        /*
            Method dump skipped, instructions count: 953
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser.next():void");
    }

    Token parseRegex() throws ParseException {
        Token tok = parseTerm();
        Token parent = null;
        while (read() == 2) {
            next();
            if (parent == null) {
                parent = Token.createUnion();
                parent.addChild(tok);
                tok = parent;
            }
            tok.addChild(parseTerm());
        }
        return tok;
    }

    Token parseTerm() throws ParseException {
        Token tok;
        int ch = read();
        if (ch == 2 || ch == 7 || ch == 1) {
            tok = Token.createEmpty();
        } else {
            tok = parseFactor();
            Token concat = null;
            while (true) {
                int i2 = read();
                ch = i2;
                if (i2 == 2 || ch == 7 || ch == 1) {
                    break;
                }
                if (concat == null) {
                    concat = Token.createConcat();
                    concat.addChild(tok);
                    tok = concat;
                }
                concat.addChild(parseFactor());
            }
        }
        if (ch == 7) {
            this.parenCount--;
        }
        return tok;
    }

    Token processCaret() throws ParseException {
        next();
        return Token.token_linebeginning;
    }

    Token processDollar() throws ParseException {
        next();
        return Token.token_lineend;
    }

    Token processLookahead() throws ParseException {
        next();
        Token tok = Token.createLook(20, parseRegex());
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return tok;
    }

    Token processNegativelookahead() throws ParseException {
        next();
        Token tok = Token.createLook(21, parseRegex());
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return tok;
    }

    Token processLookbehind() throws ParseException {
        next();
        Token tok = Token.createLook(22, parseRegex());
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return tok;
    }

    Token processNegativelookbehind() throws ParseException {
        next();
        Token tok = Token.createLook(23, parseRegex());
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return tok;
    }

    Token processBacksolidus_A() throws ParseException {
        next();
        return Token.token_stringbeginning;
    }

    Token processBacksolidus_Z() throws ParseException {
        next();
        return Token.token_stringend2;
    }

    Token processBacksolidus_z() throws ParseException {
        next();
        return Token.token_stringend;
    }

    Token processBacksolidus_b() throws ParseException {
        next();
        return Token.token_wordedge;
    }

    Token processBacksolidus_B() throws ParseException {
        next();
        return Token.token_not_wordedge;
    }

    Token processBacksolidus_lt() throws ParseException {
        next();
        return Token.token_wordbeginning;
    }

    Token processBacksolidus_gt() throws ParseException {
        next();
        return Token.token_wordend;
    }

    Token processStar(Token tok) throws ParseException {
        next();
        if (read() == 5) {
            next();
            return Token.createNGClosure(tok);
        }
        return Token.createClosure(tok);
    }

    Token processPlus(Token tok) throws ParseException {
        next();
        if (read() == 5) {
            next();
            return Token.createConcat(tok, Token.createNGClosure(tok));
        }
        return Token.createConcat(tok, Token.createClosure(tok));
    }

    Token processQuestion(Token tok) throws ParseException {
        next();
        Token par = Token.createUnion();
        if (read() == 5) {
            next();
            par.addChild(Token.createEmpty());
            par.addChild(tok);
        } else {
            par.addChild(tok);
            par.addChild(Token.createEmpty());
        }
        return par;
    }

    boolean checkQuestion(int off) {
        return off < this.regexlen && this.regex.charAt(off) == '?';
    }

    Token processParen() throws ParseException {
        next();
        int p2 = this.parenOpened;
        this.parenOpened = p2 + 1;
        Token tok = Token.createParen(parseRegex(), p2);
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        this.parennumber++;
        next();
        return tok;
    }

    Token processParen2() throws ParseException {
        next();
        Token tok = Token.createParen(parseRegex(), 0);
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return tok;
    }

    Token processCondition() throws ParseException {
        int ch;
        if (this.offset + 1 >= this.regexlen) {
            throw ex("parser.factor.4", this.offset);
        }
        int refno = -1;
        Token condition = null;
        int ch2 = this.regex.charAt(this.offset);
        if (49 <= ch2 && ch2 <= 57) {
            refno = ch2 - 48;
            int finalRefno = refno;
            if (this.parennumber <= refno) {
                throw ex("parser.parse.2", this.offset);
            }
            while (this.offset + 1 < this.regexlen && 49 <= (ch = this.regex.charAt(this.offset + 1)) && ch <= 57) {
                refno = (refno * 10) + (ch - 48);
                if (refno >= this.parennumber) {
                    break;
                }
                finalRefno = refno;
                this.offset++;
            }
            this.hasBackReferences = true;
            if (this.references == null) {
                this.references = new Vector();
            }
            this.references.addElement(new ReferencePosition(finalRefno, this.offset));
            this.offset++;
            if (this.regex.charAt(this.offset) != ')') {
                throw ex("parser.factor.1", this.offset);
            }
            this.offset++;
        } else {
            if (ch2 == 63) {
                this.offset--;
            }
            next();
            condition = parseFactor();
            switch (condition.type) {
                case 8:
                    if (read() != 7) {
                        throw ex("parser.factor.1", this.offset - 1);
                    }
                    break;
                case 20:
                case 21:
                case 22:
                case 23:
                    break;
                default:
                    throw ex("parser.factor.5", this.offset);
            }
        }
        next();
        Token yesPattern = parseRegex();
        Token noPattern = null;
        if (yesPattern.type == 2) {
            if (yesPattern.size() != 2) {
                throw ex("parser.factor.6", this.offset);
            }
            noPattern = yesPattern.getChild(1);
            yesPattern = yesPattern.getChild(0);
        }
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return Token.createCondition(refno, condition, yesPattern, noPattern);
    }

    Token processModifiers() throws ParseException {
        Token tok;
        int add = 0;
        int mask = 0;
        int ch = -1;
        while (this.offset < this.regexlen) {
            ch = this.regex.charAt(this.offset);
            int v2 = REUtil.getOptionValue(ch);
            if (v2 == 0) {
                break;
            }
            add |= v2;
            this.offset++;
        }
        if (this.offset >= this.regexlen) {
            throw ex("parser.factor.2", this.offset - 1);
        }
        if (ch == 45) {
            this.offset++;
            while (this.offset < this.regexlen) {
                ch = this.regex.charAt(this.offset);
                int v3 = REUtil.getOptionValue(ch);
                if (v3 == 0) {
                    break;
                }
                mask |= v3;
                this.offset++;
            }
            if (this.offset >= this.regexlen) {
                throw ex("parser.factor.2", this.offset - 1);
            }
        }
        if (ch == 58) {
            this.offset++;
            next();
            tok = Token.createModifierGroup(parseRegex(), add, mask);
            if (read() != 7) {
                throw ex("parser.factor.1", this.offset - 1);
            }
            next();
        } else if (ch == 41) {
            this.offset++;
            next();
            tok = Token.createModifierGroup(parseRegex(), add, mask);
        } else {
            throw ex("parser.factor.3", this.offset);
        }
        return tok;
    }

    Token processIndependent() throws ParseException {
        next();
        Token tok = Token.createLook(24, parseRegex());
        if (read() != 7) {
            throw ex("parser.factor.1", this.offset - 1);
        }
        next();
        return tok;
    }

    Token processBacksolidus_c() throws ParseException {
        if (this.offset < this.regexlen) {
            String str = this.regex;
            int i2 = this.offset;
            this.offset = i2 + 1;
            int ch2 = str.charAt(i2);
            if ((ch2 & 65504) == 64) {
                next();
                return Token.createChar(ch2 - 64);
            }
        }
        throw ex("parser.atom.1", this.offset - 1);
    }

    Token processBacksolidus_C() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    Token processBacksolidus_i() throws ParseException {
        Token tok = Token.createChar(105);
        next();
        return tok;
    }

    Token processBacksolidus_I() throws ParseException {
        throw ex("parser.process.1", this.offset);
    }

    Token processBacksolidus_g() throws ParseException {
        next();
        return Token.getGraphemePattern();
    }

    Token processBacksolidus_X() throws ParseException {
        next();
        return Token.getCombiningCharacterSequence();
    }

    Token processBackreference() throws ParseException {
        int ch;
        int refnum = this.chardata - 48;
        int finalRefnum = refnum;
        if (this.parennumber <= refnum) {
            throw ex("parser.parse.2", this.offset - 2);
        }
        while (this.offset < this.regexlen && 49 <= (ch = this.regex.charAt(this.offset)) && ch <= 57) {
            refnum = (refnum * 10) + (ch - 48);
            if (refnum >= this.parennumber) {
                break;
            }
            this.offset++;
            finalRefnum = refnum;
            this.chardata = ch;
        }
        Token tok = Token.createBackReference(finalRefnum);
        this.hasBackReferences = true;
        if (this.references == null) {
            this.references = new Vector();
        }
        this.references.addElement(new ReferencePosition(finalRefnum, this.offset - 2));
        next();
        return tok;
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0237  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.sun.org.apache.xerces.internal.impl.xpath.regex.Token parseFactor() throws com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException {
        /*
            Method dump skipped, instructions count: 618
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser.parseFactor():com.sun.org.apache.xerces.internal.impl.xpath.regex.Token");
    }

    Token parseAtom() throws ParseException {
        Token tok;
        int ch = read();
        switch (ch) {
            case 0:
                if (this.chardata == 93 || this.chardata == 123 || this.chardata == 125) {
                    throw ex("parser.atom.4", this.offset - 1);
                }
                tok = Token.createChar(this.chardata);
                int high = this.chardata;
                next();
                if (REUtil.isHighSurrogate(high) && read() == 0 && REUtil.isLowSurrogate(this.chardata)) {
                    char[] sur = {(char) high, (char) this.chardata};
                    tok = Token.createParen(Token.createString(new String(sur)), 0);
                    next();
                    break;
                }
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 20:
            case 21:
            default:
                throw ex("parser.atom.4", this.offset - 1);
            case 6:
                return processParen();
            case 8:
                next();
                tok = Token.token_dot;
                break;
            case 9:
                return parseCharacterClass(true);
            case 10:
                switch (this.chardata) {
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                        return processBackreference();
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 74:
                    case 75:
                    case 76:
                    case 77:
                    case 78:
                    case 79:
                    case 81:
                    case 82:
                    case 84:
                    case 85:
                    case 86:
                    case 89:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 104:
                    case 106:
                    case 107:
                    case 108:
                    case 109:
                    case 111:
                    case 113:
                    default:
                        tok = Token.createChar(this.chardata);
                        break;
                    case 67:
                        return processBacksolidus_C();
                    case 68:
                    case 83:
                    case 87:
                    case 100:
                    case 115:
                    case 119:
                        Token tok2 = getTokenForShorthand(this.chardata);
                        next();
                        return tok2;
                    case 73:
                        return processBacksolidus_I();
                    case 80:
                    case 112:
                        int pstart = this.offset;
                        tok = processBacksolidus_pP(this.chardata);
                        if (tok == null) {
                            throw ex("parser.atom.5", pstart);
                        }
                        break;
                    case 88:
                        return processBacksolidus_X();
                    case 99:
                        return processBacksolidus_c();
                    case 101:
                    case 102:
                    case 110:
                    case 114:
                    case 116:
                    case 117:
                    case 118:
                    case 120:
                        int ch2 = decodeEscaped();
                        if (ch2 < 65536) {
                            tok = Token.createChar(ch2);
                            break;
                        } else {
                            tok = Token.createString(REUtil.decomposeToSurrogates(ch2));
                            break;
                        }
                    case 103:
                        return processBacksolidus_g();
                    case 105:
                        return processBacksolidus_i();
                }
                next();
                break;
            case 13:
                return processParen2();
            case 18:
                return processIndependent();
            case 19:
                return parseSetOperations();
            case 22:
                return processModifiers();
            case 23:
                return processCondition();
        }
        return tok;
    }

    protected RangeToken processBacksolidus_pP(int c2) throws ParseException {
        next();
        if (read() != 0 || this.chardata != 123) {
            throw ex("parser.atom.2", this.offset - 1);
        }
        boolean positive = c2 == 112;
        int namestart = this.offset;
        int nameend = this.regex.indexOf(125, namestart);
        if (nameend < 0) {
            throw ex("parser.atom.3", this.offset);
        }
        String pname = this.regex.substring(namestart, nameend);
        this.offset = nameend + 1;
        return Token.getRange(pname, positive, isSet(512));
    }

    int processCIinCharacterClass(RangeToken tok, int c2) {
        return decodeEscaped();
    }

    /* JADX WARN: Code restructure failed: missing block: B:126:0x036a, code lost:
    
        if (read() != 1) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x0377, code lost:
    
        throw ex("parser.cc.2", r5.offset);
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0379, code lost:
    
        if (r6 != false) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x037d, code lost:
    
        if (r7 == false) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0380, code lost:
    
        r8.subtractRanges(r9);
        r9 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0389, code lost:
    
        r9.sortRanges();
        r9.compactRanges();
        setContext(0);
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x039e, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01e2, code lost:
    
        throw ex("parser.cc.1", r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected com.sun.org.apache.xerces.internal.impl.xpath.regex.RangeToken parseCharacterClass(boolean r6) throws com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException {
        /*
            Method dump skipped, instructions count: 927
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xpath.regex.RegexParser.parseCharacterClass(boolean):com.sun.org.apache.xerces.internal.impl.xpath.regex.RangeToken");
    }

    protected RangeToken parseSetOperations() throws ParseException {
        RangeToken tok = parseCharacterClass(false);
        while (true) {
            int type = read();
            if (type != 7) {
                int ch = this.chardata;
                if ((type == 0 && (ch == 45 || ch == 38)) || type == 4) {
                    next();
                    if (read() != 9) {
                        throw ex("parser.ope.1", this.offset - 1);
                    }
                    RangeToken t2 = parseCharacterClass(false);
                    if (type == 4) {
                        tok.mergeRanges(t2);
                    } else if (ch == 45) {
                        tok.subtractRanges(t2);
                    } else if (ch == 38) {
                        tok.intersectRanges(t2);
                    } else {
                        throw new RuntimeException("ASSERT");
                    }
                } else {
                    throw ex("parser.ope.2", this.offset - 1);
                }
            } else {
                next();
                return tok;
            }
        }
    }

    Token getTokenForShorthand(int ch) {
        Token tok;
        switch (ch) {
            case 68:
                tok = isSet(32) ? Token.getRange("Nd", false) : Token.token_not_0to9;
                break;
            case 83:
                tok = isSet(32) ? Token.getRange("IsSpace", false) : Token.token_not_spaces;
                break;
            case 87:
                tok = isSet(32) ? Token.getRange("IsWord", false) : Token.token_not_wordchars;
                break;
            case 100:
                tok = isSet(32) ? Token.getRange("Nd", true) : Token.token_0to9;
                break;
            case 115:
                tok = isSet(32) ? Token.getRange("IsSpace", true) : Token.token_spaces;
                break;
            case 119:
                tok = isSet(32) ? Token.getRange("IsWord", true) : Token.token_wordchars;
                break;
            default:
                throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(ch, 16));
        }
        return tok;
    }

    int decodeEscaped() throws ParseException {
        int v1;
        int v12;
        int v13;
        int v14;
        int v15;
        int v16;
        int v17;
        int v18;
        int v19;
        int v110;
        int v111;
        int v112;
        if (read() != 10) {
            throw ex("parser.next.1", this.offset - 1);
        }
        int c2 = this.chardata;
        switch (c2) {
            case 65:
            case 90:
            case 122:
                throw ex("parser.descape.5", this.offset - 2);
            case 101:
                c2 = 27;
                break;
            case 102:
                c2 = 12;
                break;
            case 110:
                c2 = 10;
                break;
            case 114:
                c2 = 13;
                break;
            case 116:
                c2 = 9;
                break;
            case 117:
                next();
                if (read() != 0 || (v17 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                next();
                if (read() != 0 || (v18 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                int uv = (v17 * 16) + v18;
                next();
                if (read() != 0 || (v19 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                int uv2 = (uv * 16) + v19;
                next();
                if (read() != 0 || (v110 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                c2 = (uv2 * 16) + v110;
                break;
                break;
            case 118:
                next();
                if (read() != 0 || (v1 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                next();
                if (read() != 0 || (v12 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                int uv3 = (v1 * 16) + v12;
                next();
                if (read() != 0 || (v13 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                int uv4 = (uv3 * 16) + v13;
                next();
                if (read() != 0 || (v14 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                int uv5 = (uv4 * 16) + v14;
                next();
                if (read() != 0 || (v15 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                int uv6 = (uv5 * 16) + v15;
                next();
                if (read() != 0 || (v16 = hexChar(this.chardata)) < 0) {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                int uv7 = (uv6 * 16) + v16;
                if (uv7 <= 1114111) {
                    c2 = uv7;
                    break;
                } else {
                    throw ex("parser.descappe.4", this.offset - 1);
                }
                break;
            case 120:
                next();
                if (read() == 0) {
                    if (this.chardata == 123) {
                        int i2 = 0;
                        while (true) {
                            int uv8 = i2;
                            next();
                            if (read() != 0) {
                                throw ex("parser.descape.1", this.offset - 1);
                            }
                            int v113 = hexChar(this.chardata);
                            if (v113 >= 0) {
                                if (uv8 > uv8 * 16) {
                                    throw ex("parser.descape.2", this.offset - 1);
                                }
                                i2 = (uv8 * 16) + v113;
                            } else if (this.chardata == 125) {
                                if (uv8 <= 1114111) {
                                    c2 = uv8;
                                    break;
                                } else {
                                    throw ex("parser.descape.4", this.offset - 1);
                                }
                            } else {
                                throw ex("parser.descape.3", this.offset - 1);
                            }
                        }
                    } else {
                        if (read() != 0 || (v111 = hexChar(this.chardata)) < 0) {
                            throw ex("parser.descape.1", this.offset - 1);
                        }
                        next();
                        if (read() != 0 || (v112 = hexChar(this.chardata)) < 0) {
                            throw ex("parser.descape.1", this.offset - 1);
                        }
                        int uv9 = (v111 * 16) + v112;
                        c2 = uv9;
                        break;
                    }
                } else {
                    throw ex("parser.descape.1", this.offset - 1);
                }
                break;
        }
        return c2;
    }

    private static final int hexChar(int ch) {
        if (ch < 48 || ch > 102) {
            return -1;
        }
        if (ch <= 57) {
            return ch - 48;
        }
        if (ch < 65) {
            return -1;
        }
        if (ch <= 70) {
            return (ch - 65) + 10;
        }
        if (ch < 97) {
            return -1;
        }
        return (ch - 97) + 10;
    }

    protected static final void addCaseInsensitiveChar(RangeToken tok, int c2) {
        int[] caseMap = CaseInsensitiveMap.get(c2);
        tok.addRange(c2, c2);
        if (caseMap != null) {
            for (int i2 = 0; i2 < caseMap.length; i2 += 2) {
                tok.addRange(caseMap[i2], caseMap[i2]);
            }
        }
    }

    protected static final void addCaseInsensitiveCharRange(RangeToken tok, int start, int end) {
        int r1;
        int r2;
        if (start <= end) {
            r1 = start;
            r2 = end;
        } else {
            r1 = end;
            r2 = start;
        }
        tok.addRange(r1, r2);
        for (int ch = r1; ch <= r2; ch++) {
            int[] caseMap = CaseInsensitiveMap.get(ch);
            if (caseMap != null) {
                for (int i2 = 0; i2 < caseMap.length; i2 += 2) {
                    tok.addRange(caseMap[i2], caseMap[i2]);
                }
            }
        }
    }
}
