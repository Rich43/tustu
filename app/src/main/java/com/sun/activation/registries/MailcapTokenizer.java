package com.sun.activation.registries;

/* loaded from: rt.jar:com/sun/activation/registries/MailcapTokenizer.class */
public class MailcapTokenizer {
    public static final int UNKNOWN_TOKEN = 0;
    public static final int START_TOKEN = 1;
    public static final int STRING_TOKEN = 2;
    public static final int EOI_TOKEN = 5;
    public static final int SLASH_TOKEN = 47;
    public static final int SEMICOLON_TOKEN = 59;
    public static final int EQUALS_TOKEN = 61;
    private String data;
    private int dataLength;
    private int dataIndex = 0;
    private int currentToken = 1;
    private String currentTokenValue = "";
    private boolean isAutoquoting = false;
    private char autoquoteChar = ';';

    public MailcapTokenizer(String inputString) {
        this.data = inputString;
        this.dataLength = inputString.length();
    }

    public void setIsAutoquoting(boolean value) {
        this.isAutoquoting = value;
    }

    public int getCurrentToken() {
        return this.currentToken;
    }

    public static String nameForToken(int token) {
        String name = "really unknown";
        switch (token) {
            case 0:
                name = "unknown";
                break;
            case 1:
                name = "start";
                break;
            case 2:
                name = "string";
                break;
            case 5:
                name = "EOI";
                break;
            case 47:
                name = "'/'";
                break;
            case 59:
                name = "';'";
                break;
            case 61:
                name = "'='";
                break;
        }
        return name;
    }

    public String getCurrentTokenValue() {
        return this.currentTokenValue;
    }

    public int nextToken() {
        if (this.dataIndex < this.dataLength) {
            while (this.dataIndex < this.dataLength && isWhiteSpaceChar(this.data.charAt(this.dataIndex))) {
                this.dataIndex++;
            }
            if (this.dataIndex < this.dataLength) {
                char c2 = this.data.charAt(this.dataIndex);
                if (this.isAutoquoting) {
                    if (c2 == ';' || c2 == '=') {
                        this.currentToken = c2;
                        this.currentTokenValue = new Character(c2).toString();
                        this.dataIndex++;
                    } else {
                        processAutoquoteToken();
                    }
                } else if (isStringTokenChar(c2)) {
                    processStringToken();
                } else if (c2 == '/' || c2 == ';' || c2 == '=') {
                    this.currentToken = c2;
                    this.currentTokenValue = new Character(c2).toString();
                    this.dataIndex++;
                } else {
                    this.currentToken = 0;
                    this.currentTokenValue = new Character(c2).toString();
                    this.dataIndex++;
                }
            } else {
                this.currentToken = 5;
                this.currentTokenValue = null;
            }
        } else {
            this.currentToken = 5;
            this.currentTokenValue = null;
        }
        return this.currentToken;
    }

    private void processStringToken() {
        int initialIndex = this.dataIndex;
        while (this.dataIndex < this.dataLength && isStringTokenChar(this.data.charAt(this.dataIndex))) {
            this.dataIndex++;
        }
        this.currentToken = 2;
        this.currentTokenValue = this.data.substring(initialIndex, this.dataIndex);
    }

    private void processAutoquoteToken() {
        int initialIndex = this.dataIndex;
        boolean foundTerminator = false;
        while (this.dataIndex < this.dataLength && !foundTerminator) {
            char c2 = this.data.charAt(this.dataIndex);
            if (c2 != this.autoquoteChar) {
                this.dataIndex++;
            } else {
                foundTerminator = true;
            }
        }
        this.currentToken = 2;
        this.currentTokenValue = fixEscapeSequences(this.data.substring(initialIndex, this.dataIndex));
    }

    private static boolean isSpecialChar(char c2) {
        boolean lAnswer = false;
        switch (c2) {
            case '\"':
            case '(':
            case ')':
            case ',':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '[':
            case '\\':
            case ']':
                lAnswer = true;
                break;
        }
        return lAnswer;
    }

    private static boolean isControlChar(char c2) {
        return Character.isISOControl(c2);
    }

    private static boolean isWhiteSpaceChar(char c2) {
        return Character.isWhitespace(c2);
    }

    private static boolean isStringTokenChar(char c2) {
        return (isSpecialChar(c2) || isControlChar(c2) || isWhiteSpaceChar(c2)) ? false : true;
    }

    private static String fixEscapeSequences(String inputString) {
        int inputLength = inputString.length();
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(inputLength);
        int i2 = 0;
        while (i2 < inputLength) {
            char currentChar = inputString.charAt(i2);
            if (currentChar != '\\') {
                buffer.append(currentChar);
            } else if (i2 < inputLength - 1) {
                char nextChar = inputString.charAt(i2 + 1);
                buffer.append(nextChar);
                i2++;
            } else {
                buffer.append(currentChar);
            }
            i2++;
        }
        return buffer.toString();
    }
}
