package sun.security.tools.policytool;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.JTextField;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/UserSaveYesButtonListener.class */
class UserSaveYesButtonListener implements ActionListener {
    private ToolDialog us;
    private PolicyTool tool;
    private ToolWindow tw;
    private int select;

    UserSaveYesButtonListener(ToolDialog toolDialog, PolicyTool policyTool, ToolWindow toolWindow, int i2) {
        this.us = toolDialog;
        this.tool = policyTool;
        this.tw = toolWindow;
        this.select = i2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.us.setVisible(false);
        this.us.dispose();
        try {
            String text = ((JTextField) this.tw.getComponent(1)).getText();
            if (text == null || text.equals("")) {
                this.us.displaySaveAsDialog(this.select);
            } else {
                this.tool.savePolicy(text);
                this.tw.displayStatusDialog(null, new MessageFormat(PolicyTool.getMessage("Policy.successfully.written.to.filename")).format(new Object[]{text}));
                this.us.userSaveContinue(this.tool, this.tw, this.us, this.select);
            }
        } catch (Exception e2) {
            this.tw.displayErrorDialog((Window) null, e2);
        }
    }
}
