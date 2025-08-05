package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import sun.security.provider.PolicyParser;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/NewPolicyPrinOKButtonListener.class */
class NewPolicyPrinOKButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog listDialog;
    private ToolDialog infoDialog;
    private boolean edit;

    NewPolicyPrinOKButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, ToolDialog toolDialog2, boolean z2) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.listDialog = toolDialog;
        this.infoDialog = toolDialog2;
        this.edit = z2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            PolicyParser.PrincipalEntry prinFromDialog = this.infoDialog.getPrinFromDialog();
            if (prinFromDialog != null) {
                try {
                    this.tool.verifyPrincipal(prinFromDialog.getPrincipalClass(), prinFromDialog.getPrincipalName());
                } catch (ClassNotFoundException e2) {
                    MessageFormat messageFormat = new MessageFormat(PolicyTool.getMessage("Warning.Class.not.found.class"));
                    Object[] objArr = {prinFromDialog.getPrincipalClass()};
                    this.tool.warnings.addElement(messageFormat.format(objArr));
                    this.tw.displayStatusDialog(this.infoDialog, messageFormat.format(objArr));
                }
                TaggedList taggedList = (TaggedList) this.listDialog.getComponent(6);
                String strPrincipalEntryToUserFriendlyString = ToolDialog.PrincipalEntryToUserFriendlyString(prinFromDialog);
                if (this.edit) {
                    taggedList.replaceTaggedItem(strPrincipalEntryToUserFriendlyString, prinFromDialog, taggedList.getSelectedIndex());
                } else {
                    taggedList.addTaggedItem(strPrincipalEntryToUserFriendlyString, prinFromDialog);
                }
            }
            this.infoDialog.dispose();
        } catch (Exception e3) {
            this.tw.displayErrorDialog(this.infoDialog, e3);
        }
    }
}
