package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/HeaderTokenizer.class */
public class HeaderTokenizer {
    private String string;
    private boolean skipComments;
    private String delimiters;
    private int currentPos;
    private int maxPos;
    private int nextPos;
    private int peekPos;
    public static final String RFC822 = "()<>@,;:\\\"\t .[]";
    public static final String MIME = "()<>@,;:\\\"\t []/?=";
    private static final Token EOFToken = new Token(-4, null);

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/HeaderTokenizer$Token.class */
    public static class Token {
        private int type;
        private String value;
        public static final int ATOM = -1;
        public static final int QUOTEDSTRING = -2;
        public static final int COMMENT = -3;
        public static final int EOF = -4;

        public Token(int type, String value) {
            this.type = type;
            this.value = value;
        }

        public int getType() {
            return this.type;
        }

        public String getValue() {
            return this.value;
        }
    }

    public HeaderTokenizer(String header, String delimiters, boolean skipComments) {
        this.string = header == null ? "" : header;
        this.skipComments = skipComments;
        this.delimiters = delimiters;
        this.peekPos = 0;
        this.nextPos = 0;
        this.currentPos = 0;
        this.maxPos = this.string.length();
    }

    public HeaderTokenizer(String header, String delimiters) {
        this(header, delimiters, true);
    }

    public HeaderTokenizer(String header) {
        this(header, RFC822);
    }

    public Token next() throws ParseException {
        this.currentPos = this.nextPos;
        Token tk = getNext();
        int i2 = this.currentPos;
        this.peekPos = i2;
        this.nextPos = i2;
        return tk;
    }

    public Token peek() throws ParseException {
        this.currentPos = this.peekPos;
        Token tk = getNext();
        this.peekPos = this.currentPos;
        return tk;
    }

    public String getRemainder() {
        return this.string.substring(this.nextPos);
    }

    private Token getNext() throws ParseException {
        char c2;
        String s2;
        String s3;
        if (this.currentPos >= this.maxPos) {
            return EOFToken;
        }
        if (skipWhiteSpace() == -4) {
            return EOFToken;
        }
        boolean filter = false;
        char cCharAt = this.string.charAt(this.currentPos);
        while (true) {
            char c3 = cCharAt;
            if (c3 == '(') {
                int start = this.currentPos + 1;
                this.currentPos = start;
                int nesting = 1;
                while (nesting > 0 && this.currentPos < this.maxPos) {
                    char c4 = this.string.charAt(this.currentPos);
                    if (c4 == '\\') {
                        this.currentPos++;
                        filter = true;
                    } else if (c4 == '\r') {
                        filter = true;
                    } else if (c4 == '(') {
                        nesting++;
                    } else if (c4 == ')') {
                        nesting--;
                    }
                    this.currentPos++;
                }
                if (nesting != 0) {
                    throw new ParseException("Unbalanced comments");
                }
                if (!this.skipComments) {
                    if (filter) {
                        s3 = filterToken(this.string, start, this.currentPos - 1);
                    } else {
                        s3 = this.string.substring(start, this.currentPos - 1);
                    }
                    return new Token(-3, s3);
                }
                if (skipWhiteSpace() == -4) {
                    return EOFToken;
                }
                cCharAt = this.string.charAt(this.currentPos);
            } else {
                if (c3 == '\"') {
                    int start2 = this.currentPos + 1;
                    this.currentPos = start2;
                    while (this.currentPos < this.maxPos) {
                        char c5 = this.string.charAt(this.currentPos);
                        if (c5 == '\\') {
                            this.currentPos++;
                            filter = true;
                        } else if (c5 == '\r') {
                            filter = true;
                        } else if (c5 == '\"') {
                            this.currentPos++;
                            if (filter) {
                                s2 = filterToken(this.string, start2, this.currentPos - 1);
                            } else {
                                s2 = this.string.substring(start2, this.currentPos - 1);
                            }
                            return new Token(-2, s2);
                        }
                        this.currentPos++;
                    }
                    throw new ParseException("Unbalanced quoted string");
                }
                if (c3 < ' ' || c3 >= 127 || this.delimiters.indexOf(c3) >= 0) {
                    this.currentPos++;
                    char[] ch = {c3};
                    return new Token(c3, new String(ch));
                }
                int start3 = this.currentPos;
                while (this.currentPos < this.maxPos && (c2 = this.string.charAt(this.currentPos)) >= ' ' && c2 < 127 && c2 != '(' && c2 != ' ' && c2 != '\"' && this.delimiters.indexOf(c2) < 0) {
                    this.currentPos++;
                }
                return new Token(-1, this.string.substring(start3, this.currentPos));
            }
        }
    }

    private int skipWhiteSpace() {
        while (this.currentPos < this.maxPos) {
            char c2 = this.string.charAt(this.currentPos);
            if (c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n') {
                this.currentPos++;
            } else {
                return this.currentPos;
            }
        }
        return -4;
    }

    private static String filterToken(String s2, int start, int end) {
        StringBuffer sb = new StringBuffer();
        boolean gotEscape = false;
        boolean gotCR = false;
        for (int i2 = start; i2 < end; i2++) {
            char c2 = s2.charAt(i2);
            if (c2 == '\n' && gotCR) {
                gotCR = false;
            } else {
                gotCR = false;
                if (!gotEscape) {
                    if (c2 == '\\') {
                        gotEscape = true;
                    } else if (c2 == '\r') {
                        gotCR = true;
                    } else {
                        sb.append(c2);
                    }
                } else {
                    sb.append(c2);
                    gotEscape = false;
                }
            }
        }
        return sb.toString();
    }
}
