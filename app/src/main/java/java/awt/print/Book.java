package java.awt.print;

import java.util.Vector;

/* loaded from: rt.jar:java/awt/print/Book.class */
public class Book implements Pageable {
    private Vector mPages = new Vector();

    @Override // java.awt.print.Pageable
    public int getNumberOfPages() {
        return this.mPages.size();
    }

    @Override // java.awt.print.Pageable
    public PageFormat getPageFormat(int i2) throws IndexOutOfBoundsException {
        return getPage(i2).getPageFormat();
    }

    @Override // java.awt.print.Pageable
    public Printable getPrintable(int i2) throws IndexOutOfBoundsException {
        return getPage(i2).getPrintable();
    }

    public void setPage(int i2, Printable printable, PageFormat pageFormat) throws IndexOutOfBoundsException {
        if (printable == null) {
            throw new NullPointerException("painter is null");
        }
        if (pageFormat == null) {
            throw new NullPointerException("page is null");
        }
        this.mPages.setElementAt(new BookPage(printable, pageFormat), i2);
    }

    public void append(Printable printable, PageFormat pageFormat) {
        this.mPages.addElement(new BookPage(printable, pageFormat));
    }

    public void append(Printable printable, PageFormat pageFormat, int i2) {
        BookPage bookPage = new BookPage(printable, pageFormat);
        int size = this.mPages.size();
        int i3 = size + i2;
        this.mPages.setSize(i3);
        for (int i4 = size; i4 < i3; i4++) {
            this.mPages.setElementAt(bookPage, i4);
        }
    }

    private BookPage getPage(int i2) throws ArrayIndexOutOfBoundsException {
        return (BookPage) this.mPages.elementAt(i2);
    }

    /* loaded from: rt.jar:java/awt/print/Book$BookPage.class */
    private class BookPage {
        private PageFormat mFormat;
        private Printable mPainter;

        BookPage(Printable printable, PageFormat pageFormat) {
            if (printable == null || pageFormat == null) {
                throw new NullPointerException();
            }
            this.mFormat = pageFormat;
            this.mPainter = printable;
        }

        Printable getPrintable() {
            return this.mPainter;
        }

        PageFormat getPageFormat() {
            return this.mFormat;
        }
    }
}
