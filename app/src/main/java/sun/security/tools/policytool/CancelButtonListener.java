package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/CancelButtonListener.class */
class CancelButtonListener implements ActionListener {
    private ToolDialog td;

    CancelButtonListener(ToolDialog toolDialog) {
        this.td = toolDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.td.setVisible(false);
        this.td.dispose();
    }
}
