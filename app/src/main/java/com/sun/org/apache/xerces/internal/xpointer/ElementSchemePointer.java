package com.sun.org.apache.xerces.internal.xpointer;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer.class */
final class ElementSchemePointer implements XPointerPart {
    private String fSchemeName;
    private String fSchemeData;
    private String fShortHandPointerName;
    private int[] fChildSequence;
    private int[] fCurrentChildSequence;
    private ShortHandPointer fShortHandPointer;
    protected XMLErrorReporter fErrorReporter;
    protected XMLErrorHandler fErrorHandler;
    private SymbolTable fSymbolTable;
    private boolean fIsResolveElement = false;
    private boolean fIsElementFound = false;
    private boolean fWasOnlyEmptyElementFound = false;
    boolean fIsShortHand = false;
    int fFoundDepth = 0;
    private int fCurrentChildPosition = 1;
    private int fCurrentChildDepth = 0;
    private boolean fIsFragmentResolved = false;

    public ElementSchemePointer() {
    }

    public ElementSchemePointer(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public ElementSchemePointer(SymbolTable symbolTable, XMLErrorReporter errorReporter) {
        this.fSymbolTable = symbolTable;
        this.fErrorReporter = errorReporter;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public void parseXPointer(String xpointer) throws XNIException {
        init();
        Tokens tokens = new Tokens(this.fSymbolTable);
        Scanner scanner = new Scanner(this.fSymbolTable) { // from class: com.sun.org.apache.xerces.internal.xpointer.ElementSchemePointer.1
            @Override // com.sun.org.apache.xerces.internal.xpointer.ElementSchemePointer.Scanner
            protected void addToken(Tokens tokens2, int token) throws XNIException {
                if (token == 1 || token == 0) {
                    super.addToken(tokens2, token);
                } else {
                    ElementSchemePointer.this.reportError("InvalidElementSchemeToken", new Object[]{tokens2.getTokenString(token)});
                }
            }
        };
        int length = xpointer.length();
        boolean success = scanner.scanExpr(this.fSymbolTable, tokens, xpointer, 0, length);
        if (!success) {
            reportError("InvalidElementSchemeXPointer", new Object[]{xpointer});
        }
        int[] tmpChildSequence = new int[(tokens.getTokenCount() / 2) + 1];
        int i2 = 0;
        while (tokens.hasMore()) {
            int token = tokens.nextToken();
            switch (token) {
                case 0:
                    int token2 = tokens.nextToken();
                    this.fShortHandPointerName = tokens.getTokenString(token2);
                    this.fShortHandPointer = new ShortHandPointer(this.fSymbolTable);
                    this.fShortHandPointer.setSchemeName(this.fShortHandPointerName);
                    break;
                case 1:
                    tmpChildSequence[i2] = tokens.nextToken();
                    i2++;
                    break;
                default:
                    reportError("InvalidElementSchemeXPointer", new Object[]{xpointer});
                    break;
            }
        }
        this.fChildSequence = new int[i2];
        this.fCurrentChildSequence = new int[i2];
        System.arraycopy(tmpChildSequence, 0, this.fChildSequence, 0, i2);
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public String getSchemeName() {
        return this.fSchemeName;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public String getSchemeData() {
        return this.fSchemeData;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public void setSchemeName(String schemeName) {
        this.fSchemeName = schemeName;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public void setSchemeData(String schemeData) {
        this.fSchemeData = schemeData;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public boolean resolveXPointer(QName element, XMLAttributes attributes, Augmentations augs, int event) throws XNIException {
        boolean isShortHandPointerResolved = false;
        if (this.fShortHandPointerName != null) {
            isShortHandPointerResolved = this.fShortHandPointer.resolveXPointer(element, attributes, augs, event);
            if (isShortHandPointerResolved) {
                this.fIsResolveElement = true;
                this.fIsShortHand = true;
            } else {
                this.fIsResolveElement = false;
            }
        } else {
            this.fIsResolveElement = true;
        }
        if (this.fChildSequence.length > 0) {
            this.fIsFragmentResolved = matchChildSequence(element, event);
        } else if (isShortHandPointerResolved && this.fChildSequence.length <= 0) {
            this.fIsFragmentResolved = isShortHandPointerResolved;
        } else {
            this.fIsFragmentResolved = false;
        }
        return this.fIsFragmentResolved;
    }

    protected boolean matchChildSequence(QName element, int event) throws XNIException {
        if (this.fCurrentChildDepth >= this.fCurrentChildSequence.length) {
            int[] tmpCurrentChildSequence = new int[this.fCurrentChildSequence.length];
            System.arraycopy(this.fCurrentChildSequence, 0, tmpCurrentChildSequence, 0, this.fCurrentChildSequence.length);
            this.fCurrentChildSequence = new int[this.fCurrentChildDepth * 2];
            System.arraycopy(tmpCurrentChildSequence, 0, this.fCurrentChildSequence, 0, tmpCurrentChildSequence.length);
        }
        if (this.fIsResolveElement) {
            this.fWasOnlyEmptyElementFound = false;
            if (event == 0) {
                this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition;
                this.fCurrentChildDepth++;
                this.fCurrentChildPosition = 1;
                if (this.fCurrentChildDepth <= this.fFoundDepth || this.fFoundDepth == 0) {
                    if (checkMatch()) {
                        this.fIsElementFound = true;
                        this.fFoundDepth = this.fCurrentChildDepth;
                    } else {
                        this.fIsElementFound = false;
                        this.fFoundDepth = 0;
                    }
                }
            } else if (event == 1) {
                if (this.fCurrentChildDepth == this.fFoundDepth) {
                    this.fIsElementFound = true;
                } else if ((this.fCurrentChildDepth < this.fFoundDepth && this.fFoundDepth != 0) || (this.fCurrentChildDepth > this.fFoundDepth && this.fFoundDepth == 0)) {
                    this.fIsElementFound = false;
                }
                this.fCurrentChildSequence[this.fCurrentChildDepth] = 0;
                this.fCurrentChildDepth--;
                this.fCurrentChildPosition = this.fCurrentChildSequence[this.fCurrentChildDepth] + 1;
            } else if (event == 2) {
                this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition;
                this.fCurrentChildPosition++;
                if (checkMatch()) {
                    if (!this.fIsElementFound) {
                        this.fWasOnlyEmptyElementFound = true;
                    } else {
                        this.fWasOnlyEmptyElementFound = false;
                    }
                    this.fIsElementFound = true;
                } else {
                    this.fIsElementFound = false;
                    this.fWasOnlyEmptyElementFound = false;
                }
            }
        }
        return this.fIsElementFound;
    }

    protected boolean checkMatch() {
        if (!this.fIsShortHand) {
            if (this.fChildSequence.length <= this.fCurrentChildDepth + 1) {
                for (int i2 = 0; i2 < this.fChildSequence.length; i2++) {
                    if (this.fChildSequence[i2] != this.fCurrentChildSequence[i2]) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        if (this.fChildSequence.length <= this.fCurrentChildDepth + 1) {
            for (int i3 = 0; i3 < this.fChildSequence.length; i3++) {
                if (this.fCurrentChildSequence.length < i3 + 2 || this.fChildSequence[i3] != this.fCurrentChildSequence[i3 + 1]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public boolean isFragmentResolved() throws XNIException {
        return this.fIsFragmentResolved;
    }

    @Override // com.sun.org.apache.xerces.internal.xpointer.XPointerPart
    public boolean isChildFragmentResolved() {
        if (!this.fIsShortHand || this.fShortHandPointer == null || this.fChildSequence.length > 0) {
            return this.fWasOnlyEmptyElementFound ? !this.fWasOnlyEmptyElementFound : this.fIsFragmentResolved && this.fCurrentChildDepth >= this.fFoundDepth;
        }
        return this.fShortHandPointer.isChildFragmentResolved();
    }

    protected void reportError(String key, Object[] arguments) throws XNIException {
        throw new XNIException(this.fErrorReporter.getMessageFormatter(XPointerMessageFormatter.XPOINTER_DOMAIN).formatMessage(this.fErrorReporter.getLocale(), key, arguments));
    }

    protected void initErrorReporter() {
        if (this.fErrorReporter == null) {
            this.fErrorReporter = new XMLErrorReporter();
        }
        if (this.fErrorHandler == null) {
            this.fErrorHandler = new XPointerErrorHandler();
        }
        this.fErrorReporter.putMessageFormatter(XPointerMessageFormatter.XPOINTER_DOMAIN, new XPointerMessageFormatter());
    }

    protected void init() {
        this.fSchemeName = null;
        this.fSchemeData = null;
        this.fShortHandPointerName = null;
        this.fIsResolveElement = false;
        this.fIsElementFound = false;
        this.fWasOnlyEmptyElementFound = false;
        this.fFoundDepth = 0;
        this.fCurrentChildPosition = 1;
        this.fCurrentChildDepth = 0;
        this.fIsFragmentResolved = false;
        this.fShortHandPointer = null;
        initErrorReporter();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Tokens.class */
    private final class Tokens {
        private static final int XPTRTOKEN_ELEM_NCNAME = 0;
        private static final int XPTRTOKEN_ELEM_CHILD = 1;
        private final String[] fgTokenNames;
        private static final int INITIAL_TOKEN_COUNT = 256;
        private int[] fTokens;
        private int fTokenCount;
        private int fCurrentTokenIndex;
        private SymbolTable fSymbolTable;
        private HashMap<Integer, String> fTokenNames;

        private Tokens(SymbolTable symbolTable) {
            this.fgTokenNames = new String[]{"XPTRTOKEN_ELEM_NCNAME", "XPTRTOKEN_ELEM_CHILD"};
            this.fTokens = new int[256];
            this.fTokenCount = 0;
            this.fTokenNames = new HashMap<>();
            this.fSymbolTable = symbolTable;
            this.fTokenNames.put(new Integer(0), "XPTRTOKEN_ELEM_NCNAME");
            this.fTokenNames.put(new Integer(1), "XPTRTOKEN_ELEM_CHILD");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getTokenString(int token) {
            return this.fTokenNames.get(new Integer(token));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToken(String tokenStr) {
            String str = this.fTokenNames.get(tokenStr);
            Integer tokenInt = str == null ? null : Integer.valueOf(Integer.parseInt(str));
            if (tokenInt == null) {
                tokenInt = new Integer(this.fTokenNames.size());
                this.fTokenNames.put(tokenInt, tokenStr);
            }
            addToken(tokenInt.intValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToken(int token) {
            try {
                this.fTokens[this.fTokenCount] = token;
            } catch (ArrayIndexOutOfBoundsException e2) {
                int[] oldList = this.fTokens;
                this.fTokens = new int[this.fTokenCount << 1];
                System.arraycopy(oldList, 0, this.fTokens, 0, this.fTokenCount);
                this.fTokens[this.fTokenCount] = token;
            }
            this.fTokenCount++;
        }

        private void rewind() {
            this.fCurrentTokenIndex = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasMore() {
            return this.fCurrentTokenIndex < this.fTokenCount;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int nextToken() throws XNIException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", null);
            }
            int[] iArr = this.fTokens;
            int i2 = this.fCurrentTokenIndex;
            this.fCurrentTokenIndex = i2 + 1;
            return iArr[i2];
        }

        private int peekToken() throws XNIException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", null);
            }
            return this.fTokens[this.fCurrentTokenIndex];
        }

        private String nextTokenAsString() throws XNIException {
            String s2 = getTokenString(nextToken());
            if (s2 == null) {
                ElementSchemePointer.this.reportError("XPointerElementSchemeProcessingError", null);
            }
            return s2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getTokenCount() {
            return this.fTokenCount;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xpointer/ElementSchemePointer$Scanner.class */
    private class Scanner {
        private static final byte CHARTYPE_INVALID = 0;
        private static final byte CHARTYPE_OTHER = 1;
        private static final byte CHARTYPE_MINUS = 2;
        private static final byte CHARTYPE_PERIOD = 3;
        private static final byte CHARTYPE_SLASH = 4;
        private static final byte CHARTYPE_DIGIT = 5;
        private static final byte CHARTYPE_LETTER = 6;
        private static final byte CHARTYPE_UNDERSCORE = 7;
        private static final byte CHARTYPE_NONASCII = 8;
        private final byte[] fASCIICharMap;
        private SymbolTable fSymbolTable;

        private Scanner(SymbolTable symbolTable) {
            this.fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 7, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 1};
            this.fSymbolTable = symbolTable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00cd  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x00b1 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean scanExpr(com.sun.org.apache.xerces.internal.util.SymbolTable r10, com.sun.org.apache.xerces.internal.xpointer.ElementSchemePointer.Tokens r11, java.lang.String r12, int r13, int r14) throws com.sun.org.apache.xerces.internal.xni.XNIException {
            /*
                Method dump skipped, instructions count: 307
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.xpointer.ElementSchemePointer.Scanner.scanExpr(com.sun.org.apache.xerces.internal.util.SymbolTable, com.sun.org.apache.xerces.internal.xpointer.ElementSchemePointer$Tokens, java.lang.String, int, int):boolean");
        }

        private int scanNCName(String data, int endOffset, int currentOffset) {
            int ch = data.charAt(currentOffset);
            if (ch >= 128) {
                if (!XMLChar.isNameStart(ch)) {
                    return currentOffset;
                }
            } else {
                byte chartype = this.fASCIICharMap[ch];
                if (chartype != 6 && chartype != 7) {
                    return currentOffset;
                }
            }
            while (true) {
                currentOffset++;
                if (currentOffset < endOffset) {
                    int ch2 = data.charAt(currentOffset);
                    if (ch2 >= 128) {
                        if (!XMLChar.isName(ch2)) {
                            break;
                        }
                    } else {
                        byte chartype2 = this.fASCIICharMap[ch2];
                        if (chartype2 != 6 && chartype2 != 5 && chartype2 != 3 && chartype2 != 2 && chartype2 != 7) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            return currentOffset;
        }

        protected void addToken(Tokens tokens, int token) throws XNIException {
            tokens.addToken(token);
        }
    }
}
