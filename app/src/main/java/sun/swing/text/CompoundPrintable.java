package sun.swing.text;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* loaded from: rt.jar:sun/swing/text/CompoundPrintable.class */
class CompoundPrintable implements CountingPrintable {
    private final Queue<CountingPrintable> printables;
    private int offset = 0;

    public CompoundPrintable(List<CountingPrintable> list) {
        this.printables = new LinkedList(list);
    }

    @Override // java.awt.print.Printable
    public int print(Graphics graphics, PageFormat pageFormat, int i2) throws PrinterException {
        int iPrint = 1;
        while (this.printables.peek() != null) {
            iPrint = this.printables.peek().print(graphics, pageFormat, i2 - this.offset);
            if (iPrint == 0) {
                break;
            }
            this.offset += this.printables.poll().getNumberOfPages();
        }
        return iPrint;
    }

    @Override // sun.swing.text.CountingPrintable
    public int getNumberOfPages() {
        return this.offset;
    }
}
