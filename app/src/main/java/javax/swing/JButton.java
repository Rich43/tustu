package javax.swing;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.AbstractButton;
import javax.swing.plaf.ButtonUI;

/* loaded from: rt.jar:javax/swing/JButton.class */
public class JButton extends AbstractButton implements Accessible {
    private static final String uiClassID = "ButtonUI";

    public JButton() {
        this(null, null);
    }

    public JButton(Icon icon) {
        this(null, icon);
    }

    @ConstructorProperties({"text"})
    public JButton(String str) {
        this(str, null);
    }

    public JButton(Action action) {
        this();
        setAction(action);
    }

    public JButton(String str, Icon icon) {
        setModel(new DefaultButtonModel());
        init(str, icon);
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent
    public void updateUI() {
        setUI((ButtonUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public boolean isDefaultButton() {
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        return rootPane != null && rootPane.getDefaultButton() == this;
    }

    public boolean isDefaultCapable() {
        return this.defaultCapable;
    }

    public void setDefaultCapable(boolean z2) {
        boolean z3 = this.defaultCapable;
        this.defaultCapable = z2;
        firePropertyChange("defaultCapable", z3, z2);
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        if (rootPane != null && rootPane.getDefaultButton() == this) {
            rootPane.setDefaultButton(null);
        }
        super.removeNotify();
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

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",defaultCapable=" + (this.defaultCapable ? "true" : "false");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJButton();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JButton$AccessibleJButton.class */
    protected class AccessibleJButton extends AbstractButton.AccessibleAbstractButton {
        protected AccessibleJButton() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PUSH_BUTTON;
        }
    }
}
