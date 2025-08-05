package javax.swing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

/* loaded from: rt.jar:javax/swing/JRadioButtonMenuItem.class */
public class JRadioButtonMenuItem extends JMenuItem implements Accessible {
    private static final String uiClassID = "RadioButtonMenuItemUI";

    public JRadioButtonMenuItem() {
        this(null, null, false);
    }

    public JRadioButtonMenuItem(Icon icon) {
        this(null, icon, false);
    }

    public JRadioButtonMenuItem(String str) {
        this(str, null, false);
    }

    public JRadioButtonMenuItem(Action action) {
        this();
        setAction(action);
    }

    public JRadioButtonMenuItem(String str, Icon icon) {
        this(str, icon, false);
    }

    public JRadioButtonMenuItem(String str, boolean z2) {
        this(str);
        setSelected(z2);
    }

    public JRadioButtonMenuItem(Icon icon, boolean z2) {
        this(null, icon, z2);
    }

    public JRadioButtonMenuItem(String str, Icon icon, boolean z2) {
        super(str, icon);
        setModel(new JToggleButton.ToggleButtonModel());
        setSelected(z2);
        setFocusable(false);
    }

    @Override // javax.swing.JMenuItem, javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
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
            this.accessibleContext = new AccessibleJRadioButtonMenuItem();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JRadioButtonMenuItem$AccessibleJRadioButtonMenuItem.class */
    protected class AccessibleJRadioButtonMenuItem extends JMenuItem.AccessibleJMenuItem {
        protected AccessibleJRadioButtonMenuItem() {
            super();
        }

        @Override // javax.swing.JMenuItem.AccessibleJMenuItem, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.RADIO_BUTTON;
        }
    }
}
