package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/UserSaveCancelButtonListener.class */
class UserSaveCancelButtonListener implements ActionListener {
    private ToolDialog us;

    UserSaveCancelButtonListener(ToolDialog toolDialog) {
        this.us = toolDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.us.setVisible(false);
        this.us.dispose();
    }
}
