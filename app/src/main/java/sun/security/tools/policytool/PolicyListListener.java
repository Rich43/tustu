package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PolicyListListener.class */
class PolicyListListener extends MouseAdapter implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;

    PolicyListListener(PolicyTool policyTool, ToolWindow toolWindow) {
        this.tool = policyTool;
        this.tw = toolWindow;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        new ToolDialog(PolicyTool.getMessage("Policy.Entry"), this.tool, this.tw, true).displayPolicyEntryDialog(true);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            actionPerformed(null);
        }
    }
}
