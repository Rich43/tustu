package java.awt.print;

/* loaded from: rt.jar:java/awt/print/Pageable.class */
public interface Pageable {
    public static final int UNKNOWN_NUMBER_OF_PAGES = -1;

    int getNumberOfPages();

    PageFormat getPageFormat(int i2) throws IndexOutOfBoundsException;

    Printable getPrintable(int i2) throws IndexOutOfBoundsException;
}
