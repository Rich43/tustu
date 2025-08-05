package javax.swing.text.html.parser;

import java.io.PrintWriter;

/* compiled from: TagStack.java */
/* loaded from: rt.jar:javax/swing/text/html/parser/NPrintWriter.class */
class NPrintWriter extends PrintWriter {
    private int numLines;
    private int numPrinted;

    public NPrintWriter(int i2) {
        super(System.out);
        this.numLines = 5;
        this.numPrinted = 0;
        this.numLines = i2;
    }

    @Override // java.io.PrintWriter
    public void println(char[] cArr) {
        if (this.numPrinted >= this.numLines) {
            return;
        }
        for (int i2 = 0; i2 < cArr.length; i2++) {
            if (cArr[i2] == '\n') {
                this.numPrinted++;
            }
            if (this.numPrinted == this.numLines) {
                System.arraycopy(cArr, 0, null, 0, i2);
            }
        }
        if (0 != 0) {
            super.print((char[]) null);
        }
        if (this.numPrinted == this.numLines) {
            return;
        }
        super.println(cArr);
        this.numPrinted++;
    }
}
