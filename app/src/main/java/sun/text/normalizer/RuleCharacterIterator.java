package sun.text.normalizer;

import java.text.ParsePosition;

/* loaded from: rt.jar:sun/text/normalizer/RuleCharacterIterator.class */
public class RuleCharacterIterator {
    private String text;
    private ParsePosition pos;
    private SymbolTable sym;
    private char[] buf;
    private int bufPos;
    private boolean isEscaped;
    public static final int DONE = -1;
    public static final int PARSE_VARIABLES = 1;
    public static final int PARSE_ESCAPES = 2;
    public static final int SKIP_WHITESPACE = 4;

    public RuleCharacterIterator(String str, SymbolTable symbolTable, ParsePosition parsePosition) {
        if (str == null || parsePosition.getIndex() > str.length()) {
            throw new IllegalArgumentException();
        }
        this.text = str;
        this.sym = symbolTable;
        this.pos = parsePosition;
        this.buf = null;
    }

    public boolean atEnd() {
        return this.buf == null && this.pos.getIndex() == this.text.length();
    }

    public int next(int i2) {
        int i_current;
        this.isEscaped = false;
        while (true) {
            i_current = _current();
            _advance(UTF16.getCharCount(i_current));
            if (i_current == 36 && this.buf == null && (i2 & 1) != 0 && this.sym != null) {
                String reference = this.sym.parseReference(this.text, this.pos, this.text.length());
                if (reference == null) {
                    break;
                }
                this.bufPos = 0;
                this.buf = this.sym.lookup(reference);
                if (this.buf == null) {
                    throw new IllegalArgumentException("Undefined variable: " + reference);
                }
                if (this.buf.length == 0) {
                    this.buf = null;
                }
            } else if ((i2 & 4) == 0 || !UCharacterProperty.isRuleWhiteSpace(i_current)) {
                break;
            }
        }
        if (i_current == 92 && (i2 & 2) != 0) {
            int[] iArr = {0};
            i_current = Utility.unescapeAt(lookahead(), iArr);
            jumpahead(iArr[0]);
            this.isEscaped = true;
            if (i_current < 0) {
                throw new IllegalArgumentException("Invalid escape");
            }
        }
        return i_current;
    }

    public boolean isEscaped() {
        return this.isEscaped;
    }

    public boolean inVariable() {
        return this.buf != null;
    }

    public Object getPos(Object obj) {
        if (obj == null) {
            return new Object[]{this.buf, new int[]{this.pos.getIndex(), this.bufPos}};
        }
        Object[] objArr = (Object[]) obj;
        objArr[0] = this.buf;
        int[] iArr = (int[]) objArr[1];
        iArr[0] = this.pos.getIndex();
        iArr[1] = this.bufPos;
        return obj;
    }

    public void setPos(Object obj) {
        Object[] objArr = (Object[]) obj;
        this.buf = (char[]) objArr[0];
        int[] iArr = (int[]) objArr[1];
        this.pos.setIndex(iArr[0]);
        this.bufPos = iArr[1];
    }

    public void skipIgnored(int i2) {
        if ((i2 & 4) == 0) {
            return;
        }
        while (true) {
            int i_current = _current();
            if (UCharacterProperty.isRuleWhiteSpace(i_current)) {
                _advance(UTF16.getCharCount(i_current));
            } else {
                return;
            }
        }
    }

    public String lookahead() {
        if (this.buf != null) {
            return new String(this.buf, this.bufPos, this.buf.length - this.bufPos);
        }
        return this.text.substring(this.pos.getIndex());
    }

    public void jumpahead(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        if (this.buf != null) {
            this.bufPos += i2;
            if (this.bufPos > this.buf.length) {
                throw new IllegalArgumentException();
            }
            if (this.bufPos == this.buf.length) {
                this.buf = null;
                return;
            }
            return;
        }
        int index = this.pos.getIndex() + i2;
        this.pos.setIndex(index);
        if (index > this.text.length()) {
            throw new IllegalArgumentException();
        }
    }

    private int _current() {
        if (this.buf != null) {
            return UTF16.charAt(this.buf, 0, this.buf.length, this.bufPos);
        }
        int index = this.pos.getIndex();
        if (index < this.text.length()) {
            return UTF16.charAt(this.text, index);
        }
        return -1;
    }

    private void _advance(int i2) {
        if (this.buf != null) {
            this.bufPos += i2;
            if (this.bufPos == this.buf.length) {
                this.buf = null;
                return;
            }
            return;
        }
        this.pos.setIndex(this.pos.getIndex() + i2);
        if (this.pos.getIndex() > this.text.length()) {
            this.pos.setIndex(this.text.length());
        }
    }
}
