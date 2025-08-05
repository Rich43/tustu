package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import sun.security.provider.PolicyParser;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/NewPolicyPermOKButtonListener.class */
class NewPolicyPermOKButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog listDialog;
    private ToolDialog infoDialog;
    private boolean edit;

    NewPolicyPermOKButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, ToolDialog toolDialog2, boolean z2) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.listDialog = toolDialog;
        this.infoDialog = toolDialog2;
        this.edit = z2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            PolicyParser.PermissionEntry permFromDialog = this.infoDialog.getPermFromDialog();
            try {
                this.tool.verifyPermission(permFromDialog.permission, permFromDialog.name, permFromDialog.action);
            } catch (ClassNotFoundException e2) {
                MessageFormat messageFormat = new MessageFormat(PolicyTool.getMessage("Warning.Class.not.found.class"));
                Object[] objArr = {permFromDialog.permission};
                this.tool.warnings.addElement(messageFormat.format(objArr));
                this.tw.displayStatusDialog(this.infoDialog, messageFormat.format(objArr));
            }
            TaggedList taggedList = (TaggedList) this.listDialog.getComponent(8);
            String strPermissionEntryToUserFriendlyString = ToolDialog.PermissionEntryToUserFriendlyString(permFromDialog);
            if (this.edit) {
                taggedList.replaceTaggedItem(strPermissionEntryToUserFriendlyString, permFromDialog, taggedList.getSelectedIndex());
            } else {
                taggedList.addTaggedItem(strPermissionEntryToUserFriendlyString, permFromDialog);
            }
            this.infoDialog.dispose();
        } catch (InvocationTargetException e3) {
            this.tw.displayErrorDialog(this.infoDialog, e3.getTargetException());
        } catch (Exception e4) {
            this.tw.displayErrorDialog(this.infoDialog, e4);
        }
    }
}
