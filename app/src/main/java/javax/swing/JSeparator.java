package javax.swing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SeparatorUI;

/* loaded from: rt.jar:javax/swing/JSeparator.class */
public class JSeparator extends JComponent implements SwingConstants, Accessible {
    private static final String uiClassID = "SeparatorUI";
    private int orientation;

    public JSeparator() {
        this(0);
    }

    public JSeparator(int i2) {
        this.orientation = 0;
        checkOrientation(i2);
        this.orientation = i2;
        setFocusable(false);
        updateUI();
    }

    public SeparatorUI getUI() {
        return (SeparatorUI) this.ui;
    }

    public void setUI(SeparatorUI separatorUI) {
        super.setUI((ComponentUI) separatorUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((SeparatorUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
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

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i2) {
        if (this.orientation == i2) {
            return;
        }
        int i3 = this.orientation;
        checkOrientation(i2);
        this.orientation = i2;
        firePropertyChange("orientation", i3, i2);
        revalidate();
        repaint();
    }

    private void checkOrientation(int i2) {
        switch (i2) {
            case 0:
            case 1:
                return;
            default:
                throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",orientation=" + (this.orientation == 0 ? "HORIZONTAL" : "VERTICAL");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJSeparator();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JSeparator$AccessibleJSeparator.class */
    protected class AccessibleJSeparator extends JComponent.AccessibleJComponent {
        protected AccessibleJSeparator() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SEPARATOR;
        }
    }
}
