package sun.security.tools.policytool;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PermissionActionsMenuListener.class */
class PermissionActionsMenuListener implements ItemListener {
    private ToolDialog td;

    PermissionActionsMenuListener(ToolDialog toolDialog) {
        this.td = toolDialog;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 2) {
            return;
        }
        ((JComboBox) this.td.getComponent(5)).getAccessibleContext().setAccessibleName((String) itemEvent.getItem());
        if (((String) itemEvent.getItem()).indexOf(ToolDialog.PERM_ACTIONS) != -1) {
            return;
        }
        JTextField jTextField = (JTextField) this.td.getComponent(6);
        if (jTextField.getText() == null || jTextField.getText().equals("")) {
            jTextField.setText((String) itemEvent.getItem());
        } else if (jTextField.getText().indexOf((String) itemEvent.getItem()) == -1) {
            jTextField.setText(jTextField.getText() + ", " + ((String) itemEvent.getItem()));
        }
    }
}
