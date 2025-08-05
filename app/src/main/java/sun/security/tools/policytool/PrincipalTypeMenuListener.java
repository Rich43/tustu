package sun.security.tools.policytool;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PrincipalTypeMenuListener.class */
class PrincipalTypeMenuListener implements ItemListener {
    private ToolDialog td;

    PrincipalTypeMenuListener(ToolDialog toolDialog) {
        this.td = toolDialog;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 2) {
            return;
        }
        JComboBox jComboBox = (JComboBox) this.td.getComponent(1);
        JTextField jTextField = (JTextField) this.td.getComponent(2);
        JTextField jTextField2 = (JTextField) this.td.getComponent(4);
        jComboBox.getAccessibleContext().setAccessibleName(PolicyTool.splitToWords((String) itemEvent.getItem()));
        if (((String) itemEvent.getItem()).equals(ToolDialog.PRIN_TYPE)) {
            if (jTextField.getText() != null && jTextField.getText().length() > 0) {
                jComboBox.setSelectedItem(ToolDialog.getPrin(jTextField.getText(), true).CLASS);
                return;
            }
            return;
        }
        if (jTextField.getText().indexOf((String) itemEvent.getItem()) == -1) {
            jTextField2.setText("");
        }
        Prin prin = ToolDialog.getPrin((String) itemEvent.getItem(), false);
        if (prin != null) {
            jTextField.setText(prin.FULL_CLASS);
        }
    }
}
