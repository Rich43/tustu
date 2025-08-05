package com.sun.javafx.css.parser;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/Token.class */
final class Token {
    static final int EOF = -1;
    static final int INVALID = 0;
    static final int SKIP = 1;
    static final Token EOF_TOKEN = new Token(-1, "EOF");
    static final Token INVALID_TOKEN = new Token(0, "INVALID");
    static final Token SKIP_TOKEN = new Token(1, "SKIP");
    private final String text;
    private int offset;
    private int line;
    private final int type;

    Token(int type, String text, int line, int offset) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.offset = offset;
    }

    Token(int type, String text) {
        this(type, text, -1, -1);
    }

    Token(int type) {
        this(type, null);
    }

    private Token() {
        this(0, "INVALID");
    }

    String getText() {
        return this.text;
    }

    int getType() {
        return this.type;
    }

    int getLine() {
        return this.line;
    }

    void setLine(int line) {
        this.line = line;
    }

    int getOffset() {
        return this.offset;
    }

    void setOffset(int offset) {
        this.offset = offset;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('[').append(this.line).append(',').append(this.offset).append(']').append(',').append(this.text).append(",<").append(this.type).append('>');
        return buf.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Token other = (Token) obj;
        if (this.type != other.type) {
            return false;
        }
        if (this.text == null) {
            if (other.text != null) {
                return false;
            }
            return true;
        }
        if (!this.text.equals(other.text)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = (67 * 7) + this.type;
        return (67 * hash) + (this.text != null ? this.text.hashCode() : 0);
    }
}
