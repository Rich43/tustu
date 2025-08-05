package sun.security.tools.policytool;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/PermissionNameMenuListener.class */
class PermissionNameMenuListener implements ItemListener {
    private ToolDialog td;

    PermissionNameMenuListener(ToolDialog toolDialog) {
        this.td = toolDialog;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 2) {
            return;
        }
        ((JComboBox) this.td.getComponent(3)).getAccessibleContext().setAccessibleName(PolicyTool.splitToWords((String) itemEvent.getItem()));
        if (((String) itemEvent.getItem()).indexOf(ToolDialog.PERM_NAME) != -1) {
            return;
        }
        ((JTextField) this.td.getComponent(4)).setText((String) itemEvent.getItem());
    }
}
