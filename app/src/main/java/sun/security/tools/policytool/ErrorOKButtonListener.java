package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ErrorOKButtonListener.class */
class ErrorOKButtonListener implements ActionListener {
    private ToolDialog ed;

    ErrorOKButtonListener(ToolDialog toolDialog) {
        this.ed = toolDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.ed.setVisible(false);
        this.ed.dispose();
    }
}
