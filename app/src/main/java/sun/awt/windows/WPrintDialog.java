package sun.awt.windows;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.peer.ComponentPeer;
import java.awt.print.PrinterJob;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:sun/awt/windows/WPrintDialog.class */
class WPrintDialog extends Dialog {
    protected PrintJob job;
    protected PrinterJob pjob;
    private boolean retval;

    private static native void initIDs();

    static {
        initIDs();
    }

    WPrintDialog(Frame frame, PrinterJob printerJob) {
        super(frame, true);
        this.retval = false;
        this.pjob = printerJob;
        setLayout(null);
    }

    WPrintDialog(Dialog dialog, PrinterJob printerJob) {
        super(dialog, "", true);
        this.retval = false;
        this.pjob = printerJob;
        setLayout(null);
    }

    final void setPeer(ComponentPeer componentPeer) {
        AWTAccessor.getComponentAccessor().setPeer(this, componentPeer);
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            Container parent = getParent();
            if (parent != null && parent.getPeer() == null) {
                parent.addNotify();
            }
            if (getPeer() == null) {
                setPeer(((WToolkit) Toolkit.getDefaultToolkit()).createWPrintDialog(this));
            }
            super.addNotify();
        }
    }

    final void setRetVal(boolean z2) {
        this.retval = z2;
    }

    final boolean getRetVal() {
        return this.retval;
    }
}
