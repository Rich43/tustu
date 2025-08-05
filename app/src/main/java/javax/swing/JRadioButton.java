package javax.swing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JToggleButton;
import javax.swing.plaf.ButtonUI;

/* loaded from: rt.jar:javax/swing/JRadioButton.class */
public class JRadioButton extends JToggleButton implements Accessible {
    private static final String uiClassID = "RadioButtonUI";

    public JRadioButton() {
        this(null, null, false);
    }

    public JRadioButton(Icon icon) {
        this(null, icon, false);
    }

    public JRadioButton(Action action) {
        this();
        setAction(action);
    }

    public JRadioButton(Icon icon, boolean z2) {
        this(null, icon, z2);
    }

    public JRadioButton(String str) {
        this(str, null, false);
    }

    public JRadioButton(String str, boolean z2) {
        this(str, null, z2);
    }

    public JRadioButton(String str, Icon icon) {
        this(str, icon, false);
    }

    public JRadioButton(String str, Icon icon, boolean z2) {
        super(str, icon, z2);
        setBorderPainted(false);
        setHorizontalAlignment(10);
    }

    @Override // javax.swing.JToggleButton, javax.swing.AbstractButton, javax.swing.JComponent
    public void updateUI() {
        setUI((ButtonUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JToggleButton, javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // javax.swing.AbstractButton
    void setIconFromAction(Action action) {
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

    @Override // javax.swing.JToggleButton, javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }

    @Override // javax.swing.JToggleButton, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJRadioButton();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JRadioButton$AccessibleJRadioButton.class */
    protected class AccessibleJRadioButton extends JToggleButton.AccessibleJToggleButton {
        protected AccessibleJRadioButton() {
            super();
        }

        @Override // javax.swing.JToggleButton.AccessibleJToggleButton, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.RADIO_BUTTON;
        }
    }
}
