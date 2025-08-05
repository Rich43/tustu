package sun.security.tools.policytool;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ToolWindowListener.class */
class ToolWindowListener implements WindowListener {
    private PolicyTool tool;
    private ToolWindow tw;

    ToolWindowListener(PolicyTool policyTool, ToolWindow toolWindow) {
        this.tool = policyTool;
        this.tw = toolWindow;
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        new ToolDialog(PolicyTool.getMessage("Save.Changes"), this.tool, this.tw, true).displayUserSave(1);
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent windowEvent) {
        System.exit(0);
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
