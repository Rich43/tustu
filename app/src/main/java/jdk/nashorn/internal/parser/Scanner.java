package jdk.nashorn.internal.parser;

/* loaded from: nashorn.jar:jdk/nashorn/internal/parser/Scanner.class */
public class Scanner {
    protected final char[] content;
    protected int position;
    protected final int limit;
    protected int line;
    protected char ch0;
    protected char ch1;
    protected char ch2;
    protected char ch3;

    protected Scanner(char[] content, int line, int start, int length) {
        this.content = content;
        this.position = start;
        this.limit = start + length;
        this.line = line;
        reset(this.position);
    }

    protected Scanner(String content) {
        this(content.toCharArray(), 0, 0, content.length());
    }

    Scanner(Scanner scanner, State state) {
        this.content = scanner.content;
        this.position = state.position;
        this.limit = state.limit;
        this.line = state.line;
        reset(this.position);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/parser/Scanner$State.class */
    static class State {
        public final int position;
        public int limit;
        public final int line;

        State(int position, int limit, int line) {
            this.position = position;
            this.limit = limit;
            this.line = line;
        }

        void setLimit(int limit) {
            this.limit = limit;
        }

        boolean isEmpty() {
            return this.position == this.limit;
        }
    }

    State saveState() {
        return new State(this.position, this.limit, this.line);
    }

    void restoreState(State state) {
        this.position = state.position;
        this.line = state.line;
        reset(this.position);
    }

    protected final boolean atEOF() {
        return this.position == this.limit;
    }

    protected final char charAt(int i2) {
        if (i2 < this.limit) {
            return this.content[i2];
        }
        return (char) 0;
    }

    protected final void reset(int i2) {
        this.ch0 = charAt(i2);
        this.ch1 = charAt(i2 + 1);
        this.ch2 = charAt(i2 + 2);
        this.ch3 = charAt(i2 + 3);
        this.position = i2 < this.limit ? i2 : this.limit;
    }

    protected final void skip(int n2) {
        if (n2 == 1 && !atEOF()) {
            this.ch0 = this.ch1;
            this.ch1 = this.ch2;
            this.ch2 = this.ch3;
            this.ch3 = charAt(this.position + 4);
            this.position++;
            return;
        }
        if (n2 != 0) {
            reset(this.position + n2);
        }
    }
}
