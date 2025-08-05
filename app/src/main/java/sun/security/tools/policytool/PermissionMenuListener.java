package sun.security.tools.policytool;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PermissionMenuListener.class */
class PermissionMenuListener implements ItemListener {
    private ToolDialog td;

    PermissionMenuListener(ToolDialog toolDialog) {
        this.td = toolDialog;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        Perm perm;
        if (itemEvent.getStateChange() == 2) {
            return;
        }
        JComboBox jComboBox = (JComboBox) this.td.getComponent(1);
        JComboBox jComboBox2 = (JComboBox) this.td.getComponent(3);
        JComboBox jComboBox3 = (JComboBox) this.td.getComponent(5);
        JTextField jTextField = (JTextField) this.td.getComponent(4);
        JTextField jTextField2 = (JTextField) this.td.getComponent(6);
        JTextField jTextField3 = (JTextField) this.td.getComponent(2);
        JTextField jTextField4 = (JTextField) this.td.getComponent(8);
        jComboBox.getAccessibleContext().setAccessibleName(PolicyTool.splitToWords((String) itemEvent.getItem()));
        if (PolicyTool.collator.compare((String) itemEvent.getItem(), ToolDialog.PERM) == 0) {
            if (jTextField3.getText() != null && jTextField3.getText().length() > 0 && (perm = ToolDialog.getPerm(jTextField3.getText(), true)) != null) {
                jComboBox.setSelectedItem(perm.CLASS);
                return;
            }
            return;
        }
        if (jTextField3.getText().indexOf((String) itemEvent.getItem()) == -1) {
            jTextField.setText("");
            jTextField2.setText("");
            jTextField4.setText("");
        }
        Perm perm2 = ToolDialog.getPerm((String) itemEvent.getItem(), false);
        if (perm2 == null) {
            jTextField3.setText("");
        } else {
            jTextField3.setText(perm2.FULL_CLASS);
        }
        this.td.setPermissionNames(perm2, jComboBox2, jTextField);
        this.td.setPermissionActions(perm2, jComboBox3, jTextField2);
    }
}
