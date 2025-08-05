package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/AddPrinButtonListener.class */
class AddPrinButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog td;
    private boolean editPolicyEntry;

    AddPrinButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, boolean z2) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.td = toolDialog;
        this.editPolicyEntry = z2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.td.displayPrincipalDialog(this.editPolicyEntry, false);
    }
}
