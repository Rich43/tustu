package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/EditPrinButtonListener.class */
class EditPrinButtonListener extends MouseAdapter implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog td;
    private boolean editPolicyEntry;

    EditPrinButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, boolean z2) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.td = toolDialog;
        this.editPolicyEntry = z2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((TaggedList) this.td.getComponent(6)).getSelectedIndex() < 0) {
            this.tw.displayErrorDialog(this.td, new Exception(PolicyTool.getMessage("No.principal.selected")));
        } else {
            this.td.displayPrincipalDialog(this.editPolicyEntry, true);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            actionPerformed(null);
        }
    }
}
