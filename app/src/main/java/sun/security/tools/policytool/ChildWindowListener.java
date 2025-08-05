package sun.security.tools.policytool;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ChildWindowListener.class */
class ChildWindowListener implements WindowListener {
    private ToolDialog td;

    ChildWindowListener(ToolDialog toolDialog) {
        this.td = toolDialog;
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        this.td.setVisible(false);
        this.td.dispose();
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent windowEvent) {
    }
}
