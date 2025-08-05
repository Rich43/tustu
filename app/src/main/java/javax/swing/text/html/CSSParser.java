package javax.swing.text.html;

import java.io.IOException;
import java.io.Reader;

/* loaded from: rt.jar:javax/swing/text/html/CSSParser.class */
class CSSParser {
    private static final int IDENTIFIER = 1;
    private static final int BRACKET_OPEN = 2;
    private static final int BRACKET_CLOSE = 3;
    private static final int BRACE_OPEN = 4;
    private static final int BRACE_CLOSE = 5;
    private static final int PAREN_OPEN = 6;
    private static final int PAREN_CLOSE = 7;
    private static final int END = -1;
    private static final char[] charMapping = {0, 0, '[', ']', '{', '}', '(', ')', 0};
    private boolean didPushChar;
    private int pushedChar;
    private int stackCount;
    private Reader reader;
    private boolean encounteredRuleSet;
    private CSSParserCallback callback;
    private int tokenBufferLength;
    private boolean readWS;
    private int[] unitStack = new int[2];
    private char[] tokenBuffer = new char[80];
    private StringBuffer unitBuffer = new StringBuffer();

    /* loaded from: rt.jar:javax/swing/text/html/CSSParser$CSSParserCallback.class */
    interface CSSParserCallback {
        void handleImport(String str);

        void handleSelector(String str);

        void startRule();

        void handleProperty(String str);

        void handleValue(String str);

        void endRule();
    }

    CSSParser() {
    }

    void parse(Reader reader, CSSParserCallback cSSParserCallback, boolean z2) throws IOException {
        this.callback = cSSParserCallback;
        this.tokenBufferLength = 0;
        this.stackCount = 0;
        this.reader = reader;
        this.encounteredRuleSet = false;
        try {
            if (z2) {
                parseDeclarationBlock();
            } else {
                while (getNextStatement()) {
                }
            }
        } finally {
        }
    }

    private boolean getNextStatement() throws IOException {
        this.unitBuffer.setLength(0);
        int iNextToken = nextToken((char) 0);
        switch (iNextToken) {
            case -1:
                return false;
            case 0:
            default:
                return true;
            case 1:
                if (this.tokenBufferLength > 0) {
                    if (this.tokenBuffer[0] == '@') {
                        parseAtRule();
                        return true;
                    }
                    this.encounteredRuleSet = true;
                    parseRuleSet();
                    return true;
                }
                return true;
            case 2:
            case 4:
            case 6:
                parseTillClosed(iNextToken);
                return true;
            case 3:
            case 5:
            case 7:
                throw new RuntimeException("Unexpected top level block close");
        }
    }

    private void parseAtRule() throws IOException {
        boolean z2 = false;
        boolean z3 = this.tokenBufferLength == 7 && this.tokenBuffer[0] == '@' && this.tokenBuffer[1] == 'i' && this.tokenBuffer[2] == 'm' && this.tokenBuffer[3] == 'p' && this.tokenBuffer[4] == 'o' && this.tokenBuffer[5] == 'r' && this.tokenBuffer[6] == 't';
        this.unitBuffer.setLength(0);
        while (!z2) {
            int iNextToken = nextToken(';');
            switch (iNextToken) {
                case -1:
                    z2 = true;
                    break;
                case 1:
                    if (this.tokenBufferLength > 0 && this.tokenBuffer[this.tokenBufferLength - 1] == ';') {
                        this.tokenBufferLength--;
                        z2 = true;
                    }
                    if (this.tokenBufferLength > 0) {
                        if (this.unitBuffer.length() > 0 && this.readWS) {
                            this.unitBuffer.append(' ');
                        }
                        this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength);
                        break;
                    } else {
                        break;
                    }
                    break;
                case 2:
                case 6:
                    this.unitBuffer.append(charMapping[iNextToken]);
                    parseTillClosed(iNextToken);
                    break;
                case 3:
                case 5:
                case 7:
                    throw new RuntimeException("Unexpected close in @ rule");
                case 4:
                    if (this.unitBuffer.length() > 0 && this.readWS) {
                        this.unitBuffer.append(' ');
                    }
                    this.unitBuffer.append(charMapping[iNextToken]);
                    parseTillClosed(iNextToken);
                    z2 = true;
                    int ws = readWS();
                    if (ws != -1 && ws != 59) {
                        pushChar(ws);
                        break;
                    } else {
                        break;
                    }
                    break;
            }
        }
        if (z3 && !this.encounteredRuleSet) {
            this.callback.handleImport(this.unitBuffer.toString());
        }
    }

    private void parseRuleSet() throws IOException {
        if (parseSelectors()) {
            this.callback.startRule();
            parseDeclarationBlock();
            this.callback.endRule();
        }
    }

    private boolean parseSelectors() throws IOException {
        if (this.tokenBufferLength > 0) {
            this.callback.handleSelector(new String(this.tokenBuffer, 0, this.tokenBufferLength));
        }
        this.unitBuffer.setLength(0);
        while (true) {
            int iNextToken = nextToken((char) 0);
            if (iNextToken == 1) {
                if (this.tokenBufferLength > 0) {
                    this.callback.handleSelector(new String(this.tokenBuffer, 0, this.tokenBufferLength));
                }
            } else {
                switch (iNextToken) {
                    case -1:
                        return false;
                    case 2:
                    case 6:
                        parseTillClosed(iNextToken);
                        this.unitBuffer.setLength(0);
                        break;
                    case 3:
                    case 5:
                    case 7:
                        throw new RuntimeException("Unexpected block close in selector");
                    case 4:
                        return true;
                }
            }
        }
    }

    private void parseDeclarationBlock() throws IOException {
        while (true) {
            switch (parseDeclaration()) {
                case -1:
                case 5:
                    return;
                case 3:
                case 7:
                    throw new RuntimeException("Unexpected close in declaration block");
            }
        }
    }

    private int parseDeclaration() throws IOException {
        int identifiers = parseIdentifiers(':', false);
        if (identifiers != 1) {
            return identifiers;
        }
        for (int length = this.unitBuffer.length() - 1; length >= 0; length--) {
            this.unitBuffer.setCharAt(length, Character.toLowerCase(this.unitBuffer.charAt(length)));
        }
        this.callback.handleProperty(this.unitBuffer.toString());
        int identifiers2 = parseIdentifiers(';', true);
        this.callback.handleValue(this.unitBuffer.toString());
        return identifiers2;
    }

    private int parseIdentifiers(char c2, boolean z2) throws IOException {
        this.unitBuffer.setLength(0);
        while (true) {
            int iNextToken = nextToken(c2);
            switch (iNextToken) {
                case -1:
                case 3:
                case 5:
                case 7:
                    return iNextToken;
                case 1:
                    if (this.tokenBufferLength > 0) {
                        if (this.tokenBuffer[this.tokenBufferLength - 1] == c2) {
                            int i2 = this.tokenBufferLength - 1;
                            this.tokenBufferLength = i2;
                            if (i2 > 0) {
                                if (this.readWS && this.unitBuffer.length() > 0) {
                                    this.unitBuffer.append(' ');
                                }
                                this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength);
                                return 1;
                            }
                            return 1;
                        }
                        if (this.readWS && this.unitBuffer.length() > 0) {
                            this.unitBuffer.append(' ');
                        }
                        this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength);
                        break;
                    } else {
                        continue;
                    }
                case 2:
                case 4:
                case 6:
                    int length = this.unitBuffer.length();
                    if (z2) {
                        this.unitBuffer.append(charMapping[iNextToken]);
                    }
                    parseTillClosed(iNextToken);
                    if (!z2) {
                        this.unitBuffer.setLength(length);
                        break;
                    } else {
                        break;
                    }
            }
        }
    }

    private void parseTillClosed(int i2) throws IOException {
        boolean z2 = false;
        startBlock(i2);
        while (!z2) {
            int iNextToken = nextToken((char) 0);
            switch (iNextToken) {
                case -1:
                    throw new RuntimeException("Unclosed block");
                case 1:
                    if (this.unitBuffer.length() > 0 && this.readWS) {
                        this.unitBuffer.append(' ');
                    }
                    if (this.tokenBufferLength <= 0) {
                        break;
                    } else {
                        this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength);
                        break;
                    }
                    break;
                case 2:
                case 4:
                case 6:
                    if (this.unitBuffer.length() > 0 && this.readWS) {
                        this.unitBuffer.append(' ');
                    }
                    this.unitBuffer.append(charMapping[iNextToken]);
                    startBlock(iNextToken);
                    break;
                case 3:
                case 5:
                case 7:
                    if (this.unitBuffer.length() > 0 && this.readWS) {
                        this.unitBuffer.append(' ');
                    }
                    this.unitBuffer.append(charMapping[iNextToken]);
                    endBlock(iNextToken);
                    if (!inBlock()) {
                        z2 = true;
                        break;
                    } else {
                        break;
                    }
                    break;
            }
        }
    }

    private int nextToken(char c2) throws IOException {
        this.readWS = false;
        int ws = readWS();
        switch (ws) {
            case -1:
                break;
            case 34:
                readTill('\"');
                if (this.tokenBufferLength > 0) {
                    this.tokenBufferLength--;
                    break;
                }
                break;
            case 39:
                readTill('\'');
                if (this.tokenBufferLength > 0) {
                    this.tokenBufferLength--;
                    break;
                }
                break;
            case 40:
                break;
            case 41:
                break;
            case 91:
                break;
            case 93:
                break;
            case 123:
                break;
            case 125:
                break;
            default:
                pushChar(ws);
                getIdentifier(c2);
                break;
        }
        return 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean getIdentifier(char c2) throws IOException {
        boolean z2;
        boolean z3 = false;
        boolean z4 = false;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        this.tokenBufferLength = 0;
        while (!z4) {
            int i5 = readChar();
            switch (i5) {
                case -1:
                    z4 = true;
                    z2 = false;
                    break;
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 33:
                case 35:
                case 36:
                case 37:
                case 38:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 94:
                case 95:
                case 96:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 124:
                default:
                    z2 = false;
                    break;
                case 9:
                case 10:
                case 13:
                case 32:
                case 34:
                case 39:
                case 40:
                case 41:
                case 91:
                case 93:
                case 123:
                case 125:
                    z2 = 3;
                    break;
                case 47:
                    z2 = 4;
                    break;
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    z2 = 2;
                    i4 = i5 - 48;
                    break;
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                    z2 = 2;
                    i4 = (i5 - 65) + 10;
                    break;
                case 92:
                    z2 = true;
                    break;
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                    z2 = 2;
                    i4 = (i5 - 97) + 10;
                    break;
            }
            if (z3) {
                if (z2 == 2) {
                    i3 = (i3 * 16) + i4;
                    i2++;
                    if (i2 == 4) {
                        z3 = false;
                        append((char) i3);
                    }
                } else {
                    z3 = false;
                    if (i2 > 0) {
                        append((char) i3);
                        pushChar(i5);
                    } else if (!z4) {
                        append((char) i5);
                    }
                }
            } else if (!z4) {
                if (z2) {
                    z3 = true;
                    i2 = 0;
                    i3 = 0;
                } else if (z2 == 3) {
                    z4 = true;
                    pushChar(i5);
                } else if (z2 == 4) {
                    int i6 = readChar();
                    if (i6 == 42) {
                        z4 = true;
                        readComment();
                        this.readWS = true;
                    } else {
                        append('/');
                        if (i6 == -1) {
                            z4 = true;
                        } else {
                            pushChar(i6);
                        }
                    }
                } else {
                    append((char) i5);
                    if (i5 == c2) {
                        z4 = true;
                    }
                }
            }
        }
        return this.tokenBufferLength > 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readTill(char c2) throws IOException {
        boolean z2;
        boolean z3 = false;
        int i2 = 0;
        int i3 = 0;
        boolean z4 = false;
        int i4 = 0;
        this.tokenBufferLength = 0;
        while (!z4) {
            int i5 = readChar();
            switch (i5) {
                case -1:
                    throw new RuntimeException("Unclosed " + c2);
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 93:
                case 94:
                case 95:
                case 96:
                default:
                    z2 = false;
                    break;
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    z2 = 2;
                    i4 = i5 - 48;
                    break;
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                    z2 = 2;
                    i4 = (i5 - 65) + 10;
                    break;
                case 92:
                    z2 = true;
                    break;
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                    z2 = 2;
                    i4 = (i5 - 97) + 10;
                    break;
            }
            if (z3) {
                if (z2 == 2) {
                    i3 = (i3 * 16) + i4;
                    i2++;
                    if (i2 == 4) {
                        z3 = false;
                        append((char) i3);
                    }
                } else if (i2 > 0) {
                    append((char) i3);
                    if (z2) {
                        z3 = true;
                        i2 = 0;
                        i3 = 0;
                    } else {
                        if (i5 == c2) {
                            z4 = true;
                        }
                        append((char) i5);
                        z3 = false;
                    }
                } else {
                    append((char) i5);
                    z3 = false;
                }
            } else if (z2) {
                z3 = true;
                i2 = 0;
                i3 = 0;
            } else {
                if (i5 == c2) {
                    z4 = true;
                }
                append((char) i5);
            }
        }
    }

    private void append(char c2) {
        if (this.tokenBufferLength == this.tokenBuffer.length) {
            char[] cArr = new char[this.tokenBuffer.length * 2];
            System.arraycopy(this.tokenBuffer, 0, cArr, 0, this.tokenBuffer.length);
            this.tokenBuffer = cArr;
        }
        char[] cArr2 = this.tokenBuffer;
        int i2 = this.tokenBufferLength;
        this.tokenBufferLength = i2 + 1;
        cArr2[i2] = c2;
    }

    private void readComment() throws IOException {
        while (true) {
            switch (readChar()) {
                case -1:
                    throw new RuntimeException("Unclosed comment");
                case 42:
                    int i2 = readChar();
                    if (i2 == 47) {
                        return;
                    }
                    if (i2 == -1) {
                        throw new RuntimeException("Unclosed comment");
                    }
                    pushChar(i2);
                    break;
            }
        }
    }

    private void startBlock(int i2) {
        if (this.stackCount == this.unitStack.length) {
            int[] iArr = new int[this.stackCount * 2];
            System.arraycopy(this.unitStack, 0, iArr, 0, this.stackCount);
            this.unitStack = iArr;
        }
        int[] iArr2 = this.unitStack;
        int i3 = this.stackCount;
        this.stackCount = i3 + 1;
        iArr2[i3] = i2;
    }

    private void endBlock(int i2) {
        int i3;
        switch (i2) {
            case 3:
                i3 = 2;
                break;
            case 4:
            case 6:
            default:
                i3 = -1;
                break;
            case 5:
                i3 = 4;
                break;
            case 7:
                i3 = 6;
                break;
        }
        if (this.stackCount > 0 && this.unitStack[this.stackCount - 1] == i3) {
            this.stackCount--;
            return;
        }
        throw new RuntimeException("Unmatched block");
    }

    private boolean inBlock() {
        return this.stackCount > 0;
    }

    private int readWS() throws IOException {
        int i2;
        while (true) {
            i2 = readChar();
            if (i2 == -1 || !Character.isWhitespace((char) i2)) {
                break;
            }
            this.readWS = true;
        }
        return i2;
    }

    private int readChar() throws IOException {
        if (this.didPushChar) {
            this.didPushChar = false;
            return this.pushedChar;
        }
        return this.reader.read();
    }

    private void pushChar(int i2) {
        if (this.didPushChar) {
            throw new RuntimeException("Can not handle look ahead of more than one character");
        }
        this.didPushChar = true;
        this.pushedChar = i2;
    }
}
