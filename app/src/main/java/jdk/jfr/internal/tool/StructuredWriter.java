package jdk.jfr.internal.tool;

import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: jfr.jar:jdk/jfr/internal/tool/StructuredWriter.class */
public abstract class StructuredWriter {
    private static final String LINE_SEPARATOR = String.format("%n", new Object[0]);
    private final PrintWriter out;
    private int column;
    private final StringBuilder builder = new StringBuilder(4000);
    private char[] indentionArray = new char[0];
    private int indent = 0;
    private boolean first = true;

    StructuredWriter(PrintWriter printWriter) {
        this.out = printWriter;
    }

    protected final int getColumn() {
        return this.column;
    }

    public final void flush(boolean z2) {
        if (z2) {
            this.out.print(this.builder.toString());
            this.builder.setLength(0);
        } else if (this.first || this.builder.length() > 100000) {
            this.out.print(this.builder.toString());
            this.builder.setLength(0);
            this.first = false;
        }
    }

    public final void printIndent() {
        this.builder.append(this.indentionArray, 0, this.indent);
        this.column += this.indent;
    }

    public final void println() {
        this.builder.append(LINE_SEPARATOR);
        this.column = 0;
    }

    public final void print(String... strArr) {
        for (String str : strArr) {
            print(str);
        }
    }

    public final void printAsString(Object obj) {
        print(String.valueOf(obj));
    }

    public final void print(String str) {
        this.builder.append(str);
        this.column += str.length();
    }

    public final void print(char c2) {
        this.builder.append(c2);
        this.column++;
    }

    public final void print(int i2) {
        print(String.valueOf(i2));
    }

    public final void indent() {
        this.indent += 2;
        updateIndent();
    }

    public final void retract() {
        this.indent -= 2;
        updateIndent();
    }

    public final void println(String str) {
        print(str);
        println();
    }

    private void updateIndent() {
        if (this.indent > this.indentionArray.length) {
            this.indentionArray = new char[this.indent];
            for (int i2 = 0; i2 < this.indentionArray.length; i2++) {
                this.indentionArray[i2] = ' ';
            }
        }
    }
}
