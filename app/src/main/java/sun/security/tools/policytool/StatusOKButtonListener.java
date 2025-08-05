package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/StatusOKButtonListener.class */
class StatusOKButtonListener implements ActionListener {
    private ToolDialog sd;

    StatusOKButtonListener(ToolDialog toolDialog) {
        this.sd = toolDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.sd.setVisible(false);
        this.sd.dispose();
    }
}
