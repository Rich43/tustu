package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/UserSaveNoButtonListener.class */
class UserSaveNoButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog us;
    private int select;

    UserSaveNoButtonListener(ToolDialog toolDialog, PolicyTool policyTool, ToolWindow toolWindow, int i2) {
        this.us = toolDialog;
        this.tool = policyTool;
        this.tw = toolWindow;
        this.select = i2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.us.setVisible(false);
        this.us.dispose();
        this.us.userSaveContinue(this.tool, this.tw, this.us, this.select);
    }
}
