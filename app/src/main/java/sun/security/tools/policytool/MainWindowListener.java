package sun.security.tools.policytool;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/MainWindowListener.class */
class MainWindowListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;

    MainWindowListener(PolicyTool policyTool, ToolWindow toolWindow) {
        this.tool = policyTool;
        this.tw = toolWindow;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.ADD_POLICY_ENTRY) == 0) {
            new ToolDialog(PolicyTool.getMessage("Policy.Entry"), this.tool, this.tw, true).displayPolicyEntryDialog(false);
            return;
        }
        if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.REMOVE_POLICY_ENTRY) == 0) {
            if (((JList) this.tw.getComponent(3)).getSelectedIndex() < 0) {
                this.tw.displayErrorDialog((Window) null, new Exception(PolicyTool.getMessage("No.Policy.Entry.selected")));
                return;
            } else {
                new ToolDialog(PolicyTool.getMessage(ToolWindow.REMOVE_POLICY_ENTRY), this.tool, this.tw, true).displayConfirmRemovePolicyEntry();
                return;
            }
        }
        if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.EDIT_POLICY_ENTRY) != 0) {
            if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.EDIT_KEYSTORE) == 0) {
                new ToolDialog(PolicyTool.getMessage("KeyStore"), this.tool, this.tw, true).keyStoreDialog(0);
            }
        } else if (((JList) this.tw.getComponent(3)).getSelectedIndex() < 0) {
            this.tw.displayErrorDialog((Window) null, new Exception(PolicyTool.getMessage("No.Policy.Entry.selected")));
        } else {
            new ToolDialog(PolicyTool.getMessage("Policy.Entry"), this.tool, this.tw, true).displayPolicyEntryDialog(true);
        }
    }
}
