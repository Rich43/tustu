package javax.swing;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PanelUI;

/* loaded from: rt.jar:javax/swing/JPanel.class */
public class JPanel extends JComponent implements Accessible {
    private static final String uiClassID = "PanelUI";

    public JPanel(LayoutManager layoutManager, boolean z2) {
        setLayout(layoutManager);
        setDoubleBuffered(z2);
        setUIProperty("opaque", Boolean.TRUE);
        updateUI();
    }

    public JPanel(LayoutManager layoutManager) {
        this(layoutManager, true);
    }

    public JPanel(boolean z2) {
        this(new FlowLayout(), z2);
    }

    public JPanel() {
        this(true);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((PanelUI) UIManager.getUI(this));
    }

    public PanelUI getUI() {
        return (PanelUI) this.ui;
    }

    public void setUI(PanelUI panelUI) {
        super.setUI((ComponentUI) panelUI);
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

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJPanel();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JPanel$AccessibleJPanel.class */
    protected class AccessibleJPanel extends JComponent.AccessibleJComponent {
        protected AccessibleJPanel() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PANEL;
        }
    }
}
