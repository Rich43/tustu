package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/EditPermButtonListener.class */
class EditPermButtonListener extends MouseAdapter implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog td;
    private boolean editPolicyEntry;

    EditPermButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, boolean z2) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.td = toolDialog;
        this.editPolicyEntry = z2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JList) this.td.getComponent(8)).getSelectedIndex() < 0) {
            this.tw.displayErrorDialog(this.td, new Exception(PolicyTool.getMessage("No.permission.selected")));
        } else {
            this.td.displayPermissionDialog(this.editPolicyEntry, true);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            actionPerformed(null);
        }
    }
}
