package com.sun.javafx.css.parser;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/LexerState.class */
class LexerState {
    private final int type;
    private final String name;
    private final Recognizer[] recognizers;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LexerState.class.desiredAssertionStatus();
    }

    public boolean accepts(int c2) {
        int nRecognizers = this.recognizers != null ? this.recognizers.length : 0;
        for (int n2 = 0; n2 < nRecognizers; n2++) {
            if (this.recognizers[n2].recognize(c2)) {
                return true;
            }
        }
        return false;
    }

    public int getType() {
        return this.type;
    }

    public LexerState(int type, String name, Recognizer recognizer, Recognizer... others) {
        if (!$assertionsDisabled && name == null) {
            throw new AssertionError();
        }
        this.type = type;
        this.name = name;
        if (recognizer != null) {
            int nRecognizers = 1 + (others != null ? others.length : 0);
            this.recognizers = new Recognizer[nRecognizers];
            this.recognizers[0] = recognizer;
            for (int n2 = 1; n2 < this.recognizers.length; n2++) {
                this.recognizers[n2] = others[n2 - 1];
            }
            return;
        }
        this.recognizers = null;
    }

    public LexerState(String name, Recognizer recognizer, Recognizer... others) {
        this(0, name, recognizer, others);
    }

    private LexerState() {
        this(0, "invalid", null, new Recognizer[0]);
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LexerState) {
            return this.name.equals(((LexerState) other).name);
        }
        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
