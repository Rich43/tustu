package sun.print;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

/* loaded from: rt.jar:sun/print/OpenBook.class */
class OpenBook implements Pageable {
    private PageFormat mFormat;
    private Printable mPainter;

    OpenBook(PageFormat pageFormat, Printable printable) {
        this.mFormat = pageFormat;
        this.mPainter = printable;
    }

    @Override // java.awt.print.Pageable
    public int getNumberOfPages() {
        return -1;
    }

    @Override // java.awt.print.Pageable
    public PageFormat getPageFormat(int i2) {
        return this.mFormat;
    }

    @Override // java.awt.print.Pageable
    public Printable getPrintable(int i2) throws IndexOutOfBoundsException {
        return this.mPainter;
    }
}
