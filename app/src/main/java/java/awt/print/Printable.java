package java.awt.print;

import java.awt.Graphics;

/* loaded from: rt.jar:java/awt/print/Printable.class */
public interface Printable {
    public static final int PAGE_EXISTS = 0;
    public static final int NO_SUCH_PAGE = 1;

    int print(Graphics graphics, PageFormat pageFormat, int i2) throws PrinterException;
}
