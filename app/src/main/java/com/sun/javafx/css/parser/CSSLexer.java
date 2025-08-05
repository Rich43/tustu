package com.sun.javafx.css.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/CSSLexer.class */
final class CSSLexer {
    static final int STRING = 10;
    static final int IDENT = 11;
    static final int FUNCTION = 12;
    static final int NUMBER = 13;
    static final int CM = 14;
    static final int EMS = 15;
    static final int EXS = 16;
    static final int IN = 17;
    static final int MM = 18;
    static final int PC = 19;
    static final int PT = 20;
    static final int PX = 21;
    static final int PERCENTAGE = 22;
    static final int DEG = 23;
    static final int GRAD = 24;
    static final int RAD = 25;
    static final int TURN = 26;
    static final int GREATER = 27;
    static final int LBRACE = 28;
    static final int RBRACE = 29;
    static final int SEMI = 30;
    static final int COLON = 31;
    static final int SOLIDUS = 32;
    static final int STAR = 33;
    static final int LPAREN = 34;
    static final int RPAREN = 35;
    static final int COMMA = 36;
    static final int HASH = 37;
    static final int DOT = 38;
    static final int IMPORTANT_SYM = 39;
    static final int WS = 40;
    static final int NL = 41;
    static final int FONT_FACE = 42;
    static final int URL = 43;
    static final int IMPORT = 44;
    static final int SECONDS = 45;
    static final int MS = 46;
    static final int AT_KEYWORD = 47;
    private int ch;
    private Reader reader;
    private Token token;

    /* renamed from: A, reason: collision with root package name */
    private final Recognizer f11853A = c2 -> {
        return c2 == 97 || c2 == 65;
    };

    /* renamed from: B, reason: collision with root package name */
    private final Recognizer f11854B = c2 -> {
        return c2 == 98 || c2 == 66;
    };

    /* renamed from: C, reason: collision with root package name */
    private final Recognizer f11855C = c2 -> {
        return c2 == 99 || c2 == 67;
    };

    /* renamed from: D, reason: collision with root package name */
    private final Recognizer f11856D = c2 -> {
        return c2 == 100 || c2 == 68;
    };

    /* renamed from: E, reason: collision with root package name */
    private final Recognizer f11857E = c2 -> {
        return c2 == 101 || c2 == 69;
    };

    /* renamed from: F, reason: collision with root package name */
    private final Recognizer f11858F = c2 -> {
        return c2 == 102 || c2 == 70;
    };

    /* renamed from: G, reason: collision with root package name */
    private final Recognizer f11859G = c2 -> {
        return c2 == 103 || c2 == 71;
    };

    /* renamed from: H, reason: collision with root package name */
    private final Recognizer f11860H = c2 -> {
        return c2 == 104 || c2 == 72;
    };

    /* renamed from: I, reason: collision with root package name */
    private final Recognizer f11861I = c2 -> {
        return c2 == 105 || c2 == 73;
    };

    /* renamed from: J, reason: collision with root package name */
    private final Recognizer f11862J = c2 -> {
        return c2 == 106 || c2 == 74;
    };

    /* renamed from: K, reason: collision with root package name */
    private final Recognizer f11863K = c2 -> {
        return c2 == 107 || c2 == 75;
    };

    /* renamed from: L, reason: collision with root package name */
    private final Recognizer f11864L = c2 -> {
        return c2 == 108 || c2 == 76;
    };

    /* renamed from: M, reason: collision with root package name */
    private final Recognizer f11865M = c2 -> {
        return c2 == 109 || c2 == 77;
    };

    /* renamed from: N, reason: collision with root package name */
    private final Recognizer f11866N = c2 -> {
        return c2 == 110 || c2 == 78;
    };

    /* renamed from: O, reason: collision with root package name */
    private final Recognizer f11867O = c2 -> {
        return c2 == 111 || c2 == 79;
    };

    /* renamed from: P, reason: collision with root package name */
    private final Recognizer f11868P = c2 -> {
        return c2 == 112 || c2 == 80;
    };

    /* renamed from: Q, reason: collision with root package name */
    private final Recognizer f11869Q = c2 -> {
        return c2 == 113 || c2 == 81;
    };

    /* renamed from: R, reason: collision with root package name */
    private final Recognizer f11870R = c2 -> {
        return c2 == 114 || c2 == 82;
    };

    /* renamed from: S, reason: collision with root package name */
    private final Recognizer f11871S = c2 -> {
        return c2 == 115 || c2 == 83;
    };

    /* renamed from: T, reason: collision with root package name */
    private final Recognizer f11872T = c2 -> {
        return c2 == 116 || c2 == 84;
    };

    /* renamed from: U, reason: collision with root package name */
    private final Recognizer f11873U = c2 -> {
        return c2 == 117 || c2 == 85;
    };

    /* renamed from: V, reason: collision with root package name */
    private final Recognizer f11874V = c2 -> {
        return c2 == 118 || c2 == 86;
    };

    /* renamed from: W, reason: collision with root package name */
    private final Recognizer f11875W = c2 -> {
        return c2 == 119 || c2 == 87;
    };

    /* renamed from: X, reason: collision with root package name */
    private final Recognizer f11876X = c2 -> {
        return c2 == 120 || c2 == 88;
    };

    /* renamed from: Y, reason: collision with root package name */
    private final Recognizer f11877Y = c2 -> {
        return c2 == 121 || c2 == 89;
    };

    /* renamed from: Z, reason: collision with root package name */
    private final Recognizer f11878Z = c2 -> {
        return c2 == 122 || c2 == 90;
    };
    private final Recognizer ALPHA = c2 -> {
        return (97 <= c2 && c2 <= 122) || (65 <= c2 && c2 <= 90);
    };
    private final Recognizer NON_ASCII = c2 -> {
        return 128 <= c2 && c2 <= 65535;
    };
    private final Recognizer DOT_CHAR = c2 -> {
        return c2 == 46;
    };
    private final Recognizer GREATER_CHAR = c2 -> {
        return c2 == 62;
    };
    private final Recognizer LBRACE_CHAR = c2 -> {
        return c2 == 123;
    };
    private final Recognizer RBRACE_CHAR = c2 -> {
        return c2 == 125;
    };
    private final Recognizer SEMI_CHAR = c2 -> {
        return c2 == 59;
    };
    private final Recognizer COLON_CHAR = c2 -> {
        return c2 == 58;
    };
    private final Recognizer SOLIDUS_CHAR = c2 -> {
        return c2 == 47;
    };
    private final Recognizer MINUS_CHAR = c2 -> {
        return c2 == 45;
    };
    private final Recognizer PLUS_CHAR = c2 -> {
        return c2 == 43;
    };
    private final Recognizer STAR_CHAR = c2 -> {
        return c2 == 42;
    };
    private final Recognizer LPAREN_CHAR = c2 -> {
        return c2 == 40;
    };
    private final Recognizer RPAREN_CHAR = c2 -> {
        return c2 == 41;
    };
    private final Recognizer COMMA_CHAR = c2 -> {
        return c2 == 44;
    };
    private final Recognizer UNDERSCORE_CHAR = c2 -> {
        return c2 == 95;
    };
    private final Recognizer HASH_CHAR = c2 -> {
        return c2 == 35;
    };
    private final Recognizer WS_CHARS = c2 -> {
        return c2 == 32 || c2 == 9 || c2 == 13 || c2 == 10 || c2 == 12;
    };
    private final Recognizer NL_CHARS = c2 -> {
        return c2 == 13 || c2 == 10;
    };
    private final Recognizer DIGIT = c2 -> {
        return 48 <= c2 && c2 <= 57;
    };
    private final Recognizer HEX_DIGIT = c2 -> {
        return (48 <= c2 && c2 <= 57) || (97 <= c2 && c2 <= 102) || (65 <= c2 && c2 <= 70);
    };
    final LexerState initState = new LexerState("initState", null, new Recognizer[0]) { // from class: com.sun.javafx.css.parser.CSSLexer.1
        @Override // com.sun.javafx.css.parser.LexerState
        public boolean accepts(int c2) {
            return true;
        }
    };
    final LexerState hashState = new LexerState("hashState", this.HASH_CHAR, new Recognizer[0]);
    final LexerState minusState = new LexerState("minusState", this.MINUS_CHAR, new Recognizer[0]);
    final LexerState plusState = new LexerState("plusState", this.PLUS_CHAR, new Recognizer[0]);
    final LexerState dotState = new LexerState(38, "dotState", this.DOT_CHAR, new Recognizer[0]);
    final LexerState nmStartState = new LexerState(11, "nmStartState", this.UNDERSCORE_CHAR, this.ALPHA);
    final LexerState nmCharState = new LexerState(11, "nmCharState", this.UNDERSCORE_CHAR, this.ALPHA, this.DIGIT, this.MINUS_CHAR);
    final LexerState hashNameCharState = new LexerState(37, "hashNameCharState", this.UNDERSCORE_CHAR, this.ALPHA, this.DIGIT, this.MINUS_CHAR);
    final LexerState lparenState = new LexerState(12, "lparenState", this.LPAREN_CHAR, new Recognizer[0]) { // from class: com.sun.javafx.css.parser.CSSLexer.2
        @Override // com.sun.javafx.css.parser.LexerState
        public int getType() {
            if (CSSLexer.this.text.indexOf("url(") == 0) {
                try {
                    return CSSLexer.this.consumeUrl();
                } catch (IOException e2) {
                    return 0;
                }
            }
            return super.getType();
        }
    };
    final LexerState leadingDigitsState = new LexerState(13, "leadingDigitsState", this.DIGIT, new Recognizer[0]);
    final LexerState decimalMarkState = new LexerState("decimalMarkState", this.DOT_CHAR, new Recognizer[0]);
    final LexerState trailingDigitsState = new LexerState(13, "trailingDigitsState", this.DIGIT, new Recognizer[0]);
    final LexerState unitsState = new UnitsState();
    private int pos = 0;
    private int offset = 0;
    private int line = 1;
    private int lastc = -1;
    private boolean charNotConsumed = false;
    private final Map<LexerState, LexerState[]> stateMap = createStateMap();
    private final StringBuilder text = new StringBuilder(64);
    private LexerState currentState = this.initState;

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/CSSLexer$InstanceHolder.class */
    private static class InstanceHolder {
        static final CSSLexer INSTANCE = new CSSLexer();

        private InstanceHolder() {
        }
    }

    public static CSSLexer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private Map<LexerState, LexerState[]> createStateMap() {
        Map<LexerState, LexerState[]> map = new HashMap<>();
        map.put(this.initState, new LexerState[]{this.hashState, this.minusState, this.nmStartState, this.plusState, this.minusState, this.leadingDigitsState, this.dotState});
        map.put(this.minusState, new LexerState[]{this.nmStartState, this.leadingDigitsState, this.decimalMarkState});
        map.put(this.hashState, new LexerState[]{this.hashNameCharState});
        map.put(this.hashNameCharState, new LexerState[]{this.hashNameCharState});
        map.put(this.nmStartState, new LexerState[]{this.nmCharState});
        map.put(this.nmCharState, new LexerState[]{this.nmCharState, this.lparenState});
        map.put(this.plusState, new LexerState[]{this.leadingDigitsState, this.decimalMarkState});
        map.put(this.leadingDigitsState, new LexerState[]{this.leadingDigitsState, this.decimalMarkState, this.unitsState});
        map.put(this.dotState, new LexerState[]{this.trailingDigitsState});
        map.put(this.decimalMarkState, new LexerState[]{this.trailingDigitsState});
        map.put(this.trailingDigitsState, new LexerState[]{this.trailingDigitsState, this.unitsState});
        map.put(this.unitsState, new LexerState[]{this.unitsState});
        return map;
    }

    CSSLexer() {
    }

    public void setReader(Reader reader) {
        this.reader = reader;
        this.lastc = -1;
        this.offset = 0;
        this.pos = 0;
        this.line = 1;
        this.currentState = this.initState;
        this.token = null;
        try {
            this.ch = readChar();
        } catch (IOException e2) {
            this.token = Token.EOF_TOKEN;
        }
    }

    private Token scanImportant() throws IOException {
        Recognizer[] important_sym = {this.f11861I, this.f11865M, this.f11868P, this.f11867O, this.f11870R, this.f11872T, this.f11853A, this.f11866N, this.f11872T};
        int current = 0;
        this.text.append((char) this.ch);
        this.ch = readChar();
        while (true) {
            switch (this.ch) {
                case -1:
                    this.token = Token.EOF_TOKEN;
                    return this.token;
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    this.ch = readChar();
                    break;
                case 47:
                    this.ch = readChar();
                    if (this.ch == 42) {
                        skipComment();
                        break;
                    } else {
                        if (this.ch != 47) {
                            this.text.append('/').append((char) this.ch);
                            int temp = this.offset;
                            this.offset = this.pos;
                            return new Token(0, this.text.toString(), this.line, temp);
                        }
                        skipEOL();
                        break;
                    }
                default:
                    boolean accepted = true;
                    while (accepted && current < important_sym.length) {
                        int i2 = current;
                        current++;
                        accepted = important_sym[i2].recognize(this.ch);
                        this.text.append((char) this.ch);
                        this.ch = readChar();
                    }
                    if (accepted) {
                        int temp2 = this.offset;
                        this.offset = this.pos - 1;
                        return new Token(39, "!important", this.line, temp2);
                    }
                    while (this.ch != 59 && this.ch != 125 && this.ch != -1) {
                        this.ch = readChar();
                    }
                    if (this.ch != -1) {
                        int temp3 = this.offset;
                        this.offset = this.pos - 1;
                        return new Token(1, this.text.toString(), this.line, temp3);
                    }
                    return Token.EOF_TOKEN;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int consumeUrl() throws IOException {
        this.text.delete(0, this.text.length());
        while (this.WS_CHARS.recognize(this.ch)) {
            this.ch = readChar();
        }
        if (this.ch == -1) {
            return -1;
        }
        if (this.ch == 39 || this.ch == 34) {
            int endQuote = this.ch;
            this.ch = readChar();
            while (this.ch != endQuote && this.ch != -1 && !this.NL_CHARS.recognize(this.ch)) {
                if (this.ch == 92) {
                    this.ch = readChar();
                    if (this.NL_CHARS.recognize(this.ch)) {
                        while (this.NL_CHARS.recognize(this.ch)) {
                            this.ch = readChar();
                        }
                    } else if (this.ch != -1) {
                        this.text.append((char) this.ch);
                        this.ch = readChar();
                    }
                } else {
                    this.text.append((char) this.ch);
                    this.ch = readChar();
                }
            }
            if (this.ch == endQuote) {
                this.ch = readChar();
                while (this.WS_CHARS.recognize(this.ch)) {
                    this.ch = readChar();
                }
                if (this.ch == 41) {
                    this.ch = readChar();
                    return 43;
                }
                if (this.ch == -1) {
                    return 43;
                }
            }
        } else {
            this.text.append((char) this.ch);
            this.ch = readChar();
            while (true) {
                if (this.WS_CHARS.recognize(this.ch)) {
                    this.ch = readChar();
                } else {
                    if (this.ch == 41) {
                        this.ch = readChar();
                        return 43;
                    }
                    if (this.ch == -1) {
                        return 43;
                    }
                    if (this.ch == 92) {
                        this.ch = readChar();
                        if (this.NL_CHARS.recognize(this.ch)) {
                            while (this.NL_CHARS.recognize(this.ch)) {
                                this.ch = readChar();
                            }
                        } else if (this.ch != -1) {
                            this.text.append((char) this.ch);
                            this.ch = readChar();
                        }
                    } else {
                        if (this.ch == 39 || this.ch == 34 || this.ch == 40) {
                            break;
                        }
                        this.text.append((char) this.ch);
                        this.ch = readChar();
                    }
                }
            }
        }
        while (true) {
            int lastCh = this.ch;
            if (this.ch == -1) {
                return -1;
            }
            if (this.ch == 41 && lastCh != 92) {
                this.ch = readChar();
                return 0;
            }
            int i2 = this.ch;
            this.ch = readChar();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/CSSLexer$UnitsState.class */
    private class UnitsState extends LexerState {
        private final Recognizer[][] units;
        private int unitsMask;
        private int index;

        /* JADX WARN: Type inference failed for: r1v3, types: [com.sun.javafx.css.parser.Recognizer[], com.sun.javafx.css.parser.Recognizer[][]] */
        UnitsState() {
            super(-1, "UnitsState", null, new Recognizer[0]);
            this.units = new Recognizer[]{new Recognizer[]{CSSLexer.this.f11855C, CSSLexer.this.f11865M}, new Recognizer[]{CSSLexer.this.f11856D, CSSLexer.this.f11857E, CSSLexer.this.f11859G}, new Recognizer[]{CSSLexer.this.f11857E, CSSLexer.this.f11865M}, new Recognizer[]{CSSLexer.this.f11857E, CSSLexer.this.f11876X}, new Recognizer[]{CSSLexer.this.f11859G, CSSLexer.this.f11870R, CSSLexer.this.f11853A, CSSLexer.this.f11856D}, new Recognizer[]{CSSLexer.this.f11861I, CSSLexer.this.f11866N}, new Recognizer[]{CSSLexer.this.f11865M, CSSLexer.this.f11865M}, new Recognizer[]{CSSLexer.this.f11865M, CSSLexer.this.f11871S}, new Recognizer[]{CSSLexer.this.f11868P, CSSLexer.this.f11855C}, new Recognizer[]{CSSLexer.this.f11868P, CSSLexer.this.f11872T}, new Recognizer[]{CSSLexer.this.f11868P, CSSLexer.this.f11876X}, new Recognizer[]{CSSLexer.this.f11870R, CSSLexer.this.f11853A, CSSLexer.this.f11856D}, new Recognizer[]{CSSLexer.this.f11871S}, new Recognizer[]{CSSLexer.this.f11872T, CSSLexer.this.f11873U, CSSLexer.this.f11870R, CSSLexer.this.f11866N}, new Recognizer[]{c2 -> {
                return c2 == 37;
            }}};
            this.unitsMask = Short.MAX_VALUE;
            this.index = -1;
        }

        @Override // com.sun.javafx.css.parser.LexerState
        public int getType() {
            int type;
            switch (this.unitsMask) {
                case 1:
                    type = 14;
                    break;
                case 2:
                    type = 23;
                    break;
                case 4:
                    type = 15;
                    break;
                case 8:
                    type = 16;
                    break;
                case 16:
                    type = 24;
                    break;
                case 32:
                    type = 17;
                    break;
                case 64:
                    type = 18;
                    break;
                case 128:
                    type = 46;
                    break;
                case 256:
                    type = 19;
                    break;
                case 512:
                    type = 20;
                    break;
                case 1024:
                    type = 21;
                    break;
                case 2048:
                    type = 25;
                    break;
                case 4096:
                    type = 45;
                    break;
                case 8192:
                    type = 26;
                    break;
                case 16384:
                    type = 22;
                    break;
                default:
                    type = 0;
                    break;
            }
            this.unitsMask = Short.MAX_VALUE;
            this.index = -1;
            return type;
        }

        @Override // com.sun.javafx.css.parser.LexerState
        public boolean accepts(int c2) {
            if (!CSSLexer.this.ALPHA.recognize(c2) && c2 != 37) {
                return false;
            }
            if (this.unitsMask == 0) {
                return true;
            }
            this.index++;
            for (int n2 = 0; n2 < this.units.length; n2++) {
                int u2 = 1 << n2;
                if ((this.unitsMask & u2) != 0 && (this.index >= this.units[n2].length || !this.units[n2][this.index].recognize(c2))) {
                    this.unitsMask &= u2 ^ (-1);
                }
            }
            return true;
        }
    }

    private void skipComment() throws IOException {
        while (this.ch != -1) {
            if (this.ch == 42) {
                this.ch = readChar();
                if (this.ch == 47) {
                    this.offset = this.pos;
                    this.ch = readChar();
                    return;
                }
            } else {
                this.ch = readChar();
            }
        }
    }

    private void skipEOL() throws IOException {
        int lastc = this.ch;
        while (this.ch != -1) {
            this.ch = readChar();
            if (this.ch == 10) {
                return;
            }
            if (lastc == 13 && this.ch != 10) {
                return;
            }
        }
    }

    private int readChar() throws IOException {
        int c2 = this.reader.read();
        if (this.lastc == 10 || (this.lastc == 13 && c2 != 10)) {
            this.pos = 1;
            this.offset = 0;
            this.line++;
        } else {
            this.pos++;
        }
        this.lastc = c2;
        return c2;
    }

    public Token nextToken() {
        Token tok;
        if (this.token != null) {
            tok = this.token;
            if (this.token.getType() != -1) {
                this.token = null;
            }
        } else {
            do {
                tok = getToken();
                if (tok == null) {
                    break;
                }
            } while (Token.SKIP_TOKEN.equals(tok));
        }
        this.text.delete(0, this.text.length());
        this.currentState = this.initState;
        return tok;
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x009e, code lost:
    
        r0 = r8.text.toString();
        r0 = new com.sun.javafx.css.parser.Token(r12, r0, r8.line, r8.offset);
        r8.offset = r8.pos - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00c8, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x04d8, code lost:
    
        if (r8.token != null) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x04db, code lost:
    
        r8.token = new com.sun.javafx.css.parser.Token(0, null, r8.line, r8.offset);
        r8.offset = r8.pos;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0503, code lost:
    
        if (r8.token.getType() != (-1)) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x050a, code lost:
    
        return r8.token;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0510, code lost:
    
        if (r8.ch == (-1)) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0517, code lost:
    
        if (r8.charNotConsumed != false) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x051a, code lost:
    
        r8.ch = readChar();
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0522, code lost:
    
        r0 = r8.token;
        r8.token = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x052f, code lost:
    
        return r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01cd A[Catch: IOException -> 0x0530, TryCatch #0 {IOException -> 0x0530, blocks: (B:2:0x0000, B:4:0x000c, B:8:0x0025, B:15:0x003b, B:18:0x0050, B:21:0x005a, B:22:0x0077, B:24:0x007e, B:28:0x0090, B:32:0x00c9, B:33:0x00cd, B:34:0x0180, B:36:0x018c, B:37:0x019f, B:39:0x01ac, B:42:0x01c5, B:44:0x01cd, B:86:0x04d4, B:88:0x04db, B:93:0x050b, B:95:0x0513, B:97:0x051a, B:98:0x0522, B:89:0x04fb, B:91:0x0506, B:45:0x01f4, B:46:0x021a, B:48:0x022b, B:51:0x023a, B:53:0x0246, B:55:0x024f, B:58:0x025e, B:60:0x026a, B:61:0x0291, B:62:0x02b3, B:63:0x02d5, B:64:0x02f7, B:65:0x0319, B:66:0x033b, B:67:0x035d, B:68:0x037f, B:69:0x03a1, B:70:0x03c3, B:71:0x03e5, B:72:0x040d, B:74:0x0435, B:75:0x044f, B:77:0x045e, B:79:0x0465, B:81:0x046b, B:82:0x0485, B:84:0x048e, B:85:0x04b0, B:30:0x009e), top: B:103:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01f4 A[Catch: IOException -> 0x0530, TryCatch #0 {IOException -> 0x0530, blocks: (B:2:0x0000, B:4:0x000c, B:8:0x0025, B:15:0x003b, B:18:0x0050, B:21:0x005a, B:22:0x0077, B:24:0x007e, B:28:0x0090, B:32:0x00c9, B:33:0x00cd, B:34:0x0180, B:36:0x018c, B:37:0x019f, B:39:0x01ac, B:42:0x01c5, B:44:0x01cd, B:86:0x04d4, B:88:0x04db, B:93:0x050b, B:95:0x0513, B:97:0x051a, B:98:0x0522, B:89:0x04fb, B:91:0x0506, B:45:0x01f4, B:46:0x021a, B:48:0x022b, B:51:0x023a, B:53:0x0246, B:55:0x024f, B:58:0x025e, B:60:0x026a, B:61:0x0291, B:62:0x02b3, B:63:0x02d5, B:64:0x02f7, B:65:0x0319, B:66:0x033b, B:67:0x035d, B:68:0x037f, B:69:0x03a1, B:70:0x03c3, B:71:0x03e5, B:72:0x040d, B:74:0x0435, B:75:0x044f, B:77:0x045e, B:79:0x0465, B:81:0x046b, B:82:0x0485, B:84:0x048e, B:85:0x04b0, B:30:0x009e), top: B:103:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.sun.javafx.css.parser.Token getToken() {
        /*
            Method dump skipped, instructions count: 1341
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.css.parser.CSSLexer.getToken():com.sun.javafx.css.parser.Token");
    }
}
