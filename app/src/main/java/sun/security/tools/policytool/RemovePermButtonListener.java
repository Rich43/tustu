package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/RemovePermButtonListener.class */
class RemovePermButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog td;
    private boolean edit;

    RemovePermButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, boolean z2) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.td = toolDialog;
        this.edit = z2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        TaggedList taggedList = (TaggedList) this.td.getComponent(8);
        int selectedIndex = taggedList.getSelectedIndex();
        if (selectedIndex < 0) {
            this.tw.displayErrorDialog(this.td, new Exception(PolicyTool.getMessage("No.permission.selected")));
        } else {
            taggedList.removeTaggedItem(selectedIndex);
        }
    }
}
