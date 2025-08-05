package sun.security.tools.policytool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import sun.security.provider.PolicyParser;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/AddEntryDoneButtonListener.class */
class AddEntryDoneButtonListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;
    private ToolDialog td;
    private boolean edit;

    AddEntryDoneButtonListener(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, boolean z2) {
        this.tool = policyTool;
        this.tw = toolWindow;
        this.td = toolDialog;
        this.edit = z2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            PolicyEntry policyEntryFromDialog = this.td.getPolicyEntryFromDialog();
            PolicyParser.GrantEntry grantEntry = policyEntryFromDialog.getGrantEntry();
            if (grantEntry.signedBy != null) {
                String[] signers = this.tool.parseSigners(grantEntry.signedBy);
                for (int i2 = 0; i2 < signers.length; i2++) {
                    if (this.tool.getPublicKeyAlias(signers[i2]) == null) {
                        MessageFormat messageFormat = new MessageFormat(PolicyTool.getMessage("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured."));
                        Object[] objArr = {signers[i2]};
                        this.tool.warnings.addElement(messageFormat.format(objArr));
                        this.tw.displayStatusDialog(this.td, messageFormat.format(objArr));
                    }
                }
            }
            JList jList = (JList) this.tw.getComponent(3);
            if (this.edit) {
                int selectedIndex = jList.getSelectedIndex();
                this.tool.addEntry(policyEntryFromDialog, selectedIndex);
                String strHeaderToString = policyEntryFromDialog.headerToString();
                if (PolicyTool.collator.compare(strHeaderToString, jList.getModel().getElementAt(selectedIndex)) != 0) {
                    this.tool.modified = true;
                }
                ((DefaultListModel) jList.getModel()).set(selectedIndex, strHeaderToString);
            } else {
                this.tool.addEntry(policyEntryFromDialog, -1);
                ((DefaultListModel) jList.getModel()).addElement(policyEntryFromDialog.headerToString());
                this.tool.modified = true;
            }
            this.td.setVisible(false);
            this.td.dispose();
        } catch (Exception e2) {
            this.tw.displayErrorDialog(this.td, e2);
        }
    }
}
