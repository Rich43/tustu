package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.JTextField;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ChangeKeyStoreOKButtonListener.class */
class ChangeKeyStoreOKButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog td;

    ChangeKeyStoreOKButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.td = toolDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strTrim = ((JTextField) this.td.getComponent(1)).getText().trim();
        String strTrim2 = ((JTextField) this.td.getComponent(3)).getText().trim();
        String strTrim3 = ((JTextField) this.td.getComponent(5)).getText().trim();
        String strTrim4 = ((JTextField) this.td.getComponent(7)).getText().trim();
        try {
            this.tool.openKeyStore(strTrim.length() == 0 ? null : strTrim, strTrim2.length() == 0 ? null : strTrim2, strTrim3.length() == 0 ? null : strTrim3, strTrim4.length() == 0 ? null : strTrim4);
            this.tool.modified = true;
            this.td.dispose();
        } catch (Exception e2) {
            this.tw.displayErrorDialog(this.td, new MessageFormat(PolicyTool.getMessage("Unable.to.open.KeyStore.ex.toString.")).format(new Object[]{e2.toString()}));
        }
    }
}
