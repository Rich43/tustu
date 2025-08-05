package sun.awt.windows;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

/* loaded from: rt.jar:sun/awt/windows/WPageDialog.class */
final class WPageDialog extends WPrintDialog {
    PageFormat page;
    Printable painter;

    private static native void initIDs();

    static {
        initIDs();
    }

    WPageDialog(Frame frame, PrinterJob printerJob, PageFormat pageFormat, Printable printable) {
        super(frame, printerJob);
        this.page = pageFormat;
        this.painter = printable;
    }

    WPageDialog(Dialog dialog, PrinterJob printerJob, PageFormat pageFormat, Printable printable) {
        super(dialog, printerJob);
        this.page = pageFormat;
        this.painter = printable;
    }

    @Override // sun.awt.windows.WPrintDialog, java.awt.Dialog, java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            Container parent = getParent();
            if (parent != null && parent.getPeer() == null) {
                parent.addNotify();
            }
            if (getPeer() == null) {
                setPeer(((WToolkit) Toolkit.getDefaultToolkit()).createWPageDialog(this));
            }
            super.addNotify();
        }
    }
}
