package javax.swing;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.swing.AbstractButton;
import javax.swing.plaf.ButtonUI;

/* loaded from: rt.jar:javax/swing/JToggleButton.class */
public class JToggleButton extends AbstractButton implements Accessible {
    private static final String uiClassID = "ToggleButtonUI";

    public JToggleButton() {
        this(null, null, false);
    }

    public JToggleButton(Icon icon) {
        this(null, icon, false);
    }

    public JToggleButton(Icon icon, boolean z2) {
        this(null, icon, z2);
    }

    public JToggleButton(String str) {
        this(str, null, false);
    }

    public JToggleButton(String str, boolean z2) {
        this(str, null, z2);
    }

    public JToggleButton(Action action) {
        this();
        setAction(action);
    }

    public JToggleButton(String str, Icon icon) {
        this(str, icon, false);
    }

    public JToggleButton(String str, Icon icon, boolean z2) {
        setModel(new ToggleButtonModel());
        this.model.setSelected(z2);
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

    @Override // javax.swing.AbstractButton
    boolean shouldUpdateSelectedStateFromAction() {
        return true;
    }

    /* loaded from: rt.jar:javax/swing/JToggleButton$ToggleButtonModel.class */
    public static class ToggleButtonModel extends DefaultButtonModel {
        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public boolean isSelected() {
            return (this.stateMask & 2) != 0;
        }

        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public void setSelected(boolean z2) {
            ButtonGroup group = getGroup();
            if (group != null) {
                group.setSelected(this, z2);
                z2 = group.isSelected(this);
            }
            if (isSelected() == z2) {
                return;
            }
            if (z2) {
                this.stateMask |= 2;
            } else {
                this.stateMask &= -3;
            }
            fireStateChanged();
            fireItemStateChanged(new ItemEvent(this, 701, this, isSelected() ? 1 : 2));
        }

        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public void setPressed(boolean z2) {
            if (isPressed() == z2 || !isEnabled()) {
                return;
            }
            if (!z2 && isArmed()) {
                setSelected(!isSelected());
            }
            if (z2) {
                this.stateMask |= 4;
            } else {
                this.stateMask &= -5;
            }
            fireStateChanged();
            if (!isPressed() && isArmed()) {
                int modifiers = 0;
                AWTEvent currentEvent = EventQueue.getCurrentEvent();
                if (currentEvent instanceof InputEvent) {
                    modifiers = ((InputEvent) currentEvent).getModifiers();
                } else if (currentEvent instanceof ActionEvent) {
                    modifiers = ((ActionEvent) currentEvent).getModifiers();
                }
                fireActionPerformed(new ActionEvent(this, 1001, getActionCommand(), EventQueue.getMostRecentEventTime(), modifiers));
            }
        }
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
        return super.paramString();
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJToggleButton();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JToggleButton$AccessibleJToggleButton.class */
    protected class AccessibleJToggleButton extends AbstractButton.AccessibleAbstractButton implements ItemListener {
        public AccessibleJToggleButton() {
            super();
            JToggleButton.this.addItemListener(this);
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            JToggleButton jToggleButton = (JToggleButton) itemEvent.getSource();
            if (JToggleButton.this.accessibleContext != null) {
                if (jToggleButton.isSelected()) {
                    JToggleButton.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.CHECKED);
                } else {
                    JToggleButton.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.CHECKED, null);
                }
            }
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TOGGLE_BUTTON;
        }
    }
}
