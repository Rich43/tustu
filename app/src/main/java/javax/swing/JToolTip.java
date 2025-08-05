package javax.swing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.plaf.ToolTipUI;

/* loaded from: rt.jar:javax/swing/JToolTip.class */
public class JToolTip extends JComponent implements Accessible {
    private static final String uiClassID = "ToolTipUI";
    String tipText;
    JComponent component;

    public JToolTip() {
        setOpaque(true);
        updateUI();
    }

    public ToolTipUI getUI() {
        return (ToolTipUI) this.ui;
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ToolTipUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public void setTipText(String str) {
        String str2 = this.tipText;
        this.tipText = str;
        firePropertyChange("tiptext", str2, str);
        if (!Objects.equals(str2, str)) {
            revalidate();
            repaint();
        }
    }

    public String getTipText() {
        return this.tipText;
    }

    public void setComponent(JComponent jComponent) {
        JComponent jComponent2 = this.component;
        this.component = jComponent;
        firePropertyChange("component", jComponent2, jComponent);
    }

    public JComponent getComponent() {
        return this.component;
    }

    @Override // javax.swing.JComponent
    boolean alwaysOnTop() {
        return true;
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

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",tipText=" + (this.tipText != null ? this.tipText : "");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJToolTip();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JToolTip$AccessibleJToolTip.class */
    protected class AccessibleJToolTip extends JComponent.AccessibleJComponent {
        protected AccessibleJToolTip() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            String tipText = this.accessibleDescription;
            if (tipText == null) {
                tipText = (String) JToolTip.this.getClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY);
            }
            if (tipText == null) {
                tipText = JToolTip.this.getTipText();
            }
            return tipText;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TOOL_TIP;
        }
    }
}
