package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ConfirmRemovePolicyEntryOKButtonListener.class */
class ConfirmRemovePolicyEntryOKButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog us;

    ConfirmRemovePolicyEntryOKButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.us = toolDialog;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.tool.removeEntry(this.tool.getEntry()[((JList) this.tw.getComponent(3)).getSelectedIndex()]);
        DefaultListModel defaultListModel = new DefaultListModel();
        JList jList = new JList(defaultListModel);
        jList.setVisibleRowCount(15);
        jList.setSelectionMode(0);
        jList.addMouseListener(new PolicyListListener(this.tool, this.tw));
        PolicyEntry[] entry = this.tool.getEntry();
        if (entry != null) {
            for (PolicyEntry policyEntry : entry) {
                defaultListModel.addElement(policyEntry.headerToString());
            }
        }
        this.tw.replacePolicyList(jList);
        this.us.setVisible(false);
        this.us.dispose();
    }
}
