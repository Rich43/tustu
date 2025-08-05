package javax.swing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JToggleButton;
import javax.swing.plaf.ButtonUI;

/* loaded from: rt.jar:javax/swing/JCheckBox.class */
public class JCheckBox extends JToggleButton implements Accessible {
    public static final String BORDER_PAINTED_FLAT_CHANGED_PROPERTY = "borderPaintedFlat";
    private boolean flat;
    private static final String uiClassID = "CheckBoxUI";

    public JCheckBox() {
        this(null, null, false);
    }

    public JCheckBox(Icon icon) {
        this(null, icon, false);
    }

    public JCheckBox(Icon icon, boolean z2) {
        this(null, icon, z2);
    }

    public JCheckBox(String str) {
        this(str, null, false);
    }

    public JCheckBox(Action action) {
        this();
        setAction(action);
    }

    public JCheckBox(String str, boolean z2) {
        this(str, null, z2);
    }

    public JCheckBox(String str, Icon icon) {
        this(str, icon, false);
    }

    public JCheckBox(String str, Icon icon, boolean z2) {
        super(str, icon, z2);
        this.flat = false;
        setUIProperty(AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, Boolean.FALSE);
        setHorizontalAlignment(10);
    }

    public void setBorderPaintedFlat(boolean z2) {
        boolean z3 = this.flat;
        this.flat = z2;
        firePropertyChange(BORDER_PAINTED_FLAT_CHANGED_PROPERTY, z3, this.flat);
        if (z2 != z3) {
            revalidate();
            repaint();
        }
    }

    public boolean isBorderPaintedFlat() {
        return this.flat;
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

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (getUIClassID().equals(uiClassID)) {
            updateUI();
        }
    }

    @Override // javax.swing.JToggleButton, javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }

    @Override // javax.swing.JToggleButton, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJCheckBox();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JCheckBox$AccessibleJCheckBox.class */
    protected class AccessibleJCheckBox extends JToggleButton.AccessibleJToggleButton {
        protected AccessibleJCheckBox() {
            super();
        }

        @Override // javax.swing.JToggleButton.AccessibleJToggleButton, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CHECK_BOX;
        }
    }
}
