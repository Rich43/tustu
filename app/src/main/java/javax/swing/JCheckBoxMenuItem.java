package javax.swing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

/* loaded from: rt.jar:javax/swing/JCheckBoxMenuItem.class */
public class JCheckBoxMenuItem extends JMenuItem implements SwingConstants, Accessible {
    private static final String uiClassID = "CheckBoxMenuItemUI";

    public JCheckBoxMenuItem() {
        this(null, null, false);
    }

    public JCheckBoxMenuItem(Icon icon) {
        this(null, icon, false);
    }

    public JCheckBoxMenuItem(String str) {
        this(str, null, false);
    }

    public JCheckBoxMenuItem(Action action) {
        this();
        setAction(action);
    }

    public JCheckBoxMenuItem(String str, Icon icon) {
        this(str, icon, false);
    }

    public JCheckBoxMenuItem(String str, boolean z2) {
        this(str, null, z2);
    }

    public JCheckBoxMenuItem(String str, Icon icon, boolean z2) {
        super(str, icon);
        setModel(new JToggleButton.ToggleButtonModel());
        setSelected(z2);
        setFocusable(false);
    }

    @Override // javax.swing.JMenuItem, javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public boolean getState() {
        return isSelected();
    }

    public synchronized void setState(boolean z2) {
        setSelected(z2);
    }

    @Override // javax.swing.AbstractButton, java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        if (!isSelected()) {
            return null;
        }
        return new Object[]{getText()};
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JMenuItem, javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }

    @Override // javax.swing.AbstractButton
    boolean shouldUpdateSelectedStateFromAction() {
        return true;
    }

    @Override // javax.swing.JMenuItem, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJCheckBoxMenuItem();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JCheckBoxMenuItem$AccessibleJCheckBoxMenuItem.class */
    protected class AccessibleJCheckBoxMenuItem extends JMenuItem.AccessibleJMenuItem {
        protected AccessibleJCheckBoxMenuItem() {
            super();
        }

        @Override // javax.swing.JMenuItem.AccessibleJMenuItem, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CHECK_BOX;
        }
    }
}
