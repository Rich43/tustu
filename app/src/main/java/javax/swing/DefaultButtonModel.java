package javax.swing;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/* loaded from: rt.jar:javax/swing/DefaultButtonModel.class */
public class DefaultButtonModel implements ButtonModel, Serializable {
    protected int stateMask;
    protected String actionCommand = null;
    protected ButtonGroup group = null;
    protected int mnemonic = 0;
    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();
    private boolean menuItem = false;
    public static final int ARMED = 1;
    public static final int SELECTED = 2;
    public static final int PRESSED = 4;
    public static final int ENABLED = 8;
    public static final int ROLLOVER = 16;

    public DefaultButtonModel() {
        this.stateMask = 0;
        this.stateMask = 0;
        setEnabled(true);
    }

    @Override // javax.swing.ButtonModel
    public void setActionCommand(String str) {
        this.actionCommand = str;
    }

    @Override // javax.swing.ButtonModel
    public String getActionCommand() {
        return this.actionCommand;
    }

    @Override // javax.swing.ButtonModel
    public boolean isArmed() {
        return (this.stateMask & 1) != 0;
    }

    @Override // javax.swing.ButtonModel
    public boolean isSelected() {
        return (this.stateMask & 2) != 0;
    }

    @Override // javax.swing.ButtonModel
    public boolean isEnabled() {
        return (this.stateMask & 8) != 0;
    }

    @Override // javax.swing.ButtonModel
    public boolean isPressed() {
        return (this.stateMask & 4) != 0;
    }

    @Override // javax.swing.ButtonModel
    public boolean isRollover() {
        return (this.stateMask & 16) != 0;
    }

    @Override // javax.swing.ButtonModel
    public void setArmed(boolean z2) {
        if (isMenuItem() && UIManager.getBoolean("MenuItem.disabledAreNavigable")) {
            if (isArmed() == z2) {
                return;
            }
        } else if (isArmed() == z2 || !isEnabled()) {
            return;
        }
        if (z2) {
            this.stateMask |= 1;
        } else {
            this.stateMask &= -2;
        }
        fireStateChanged();
    }

    @Override // javax.swing.ButtonModel
    public void setEnabled(boolean z2) {
        if (isEnabled() == z2) {
            return;
        }
        if (z2) {
            this.stateMask |= 8;
        } else {
            this.stateMask &= -9;
            this.stateMask &= -2;
            this.stateMask &= -5;
        }
        fireStateChanged();
    }

    @Override // javax.swing.ButtonModel
    public void setSelected(boolean z2) {
        if (isSelected() == z2) {
            return;
        }
        if (z2) {
            this.stateMask |= 2;
        } else {
            this.stateMask &= -3;
        }
        fireItemStateChanged(new ItemEvent(this, 701, this, z2 ? 1 : 2));
        fireStateChanged();
    }

    @Override // javax.swing.ButtonModel
    public void setPressed(boolean z2) {
        if (isPressed() == z2 || !isEnabled()) {
            return;
        }
        if (z2) {
            this.stateMask |= 4;
        } else {
            this.stateMask &= -5;
        }
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
        fireStateChanged();
    }

    @Override // javax.swing.ButtonModel
    public void setRollover(boolean z2) {
        if (isRollover() == z2 || !isEnabled()) {
            return;
        }
        if (z2) {
            this.stateMask |= 16;
        } else {
            this.stateMask &= -17;
        }
        fireStateChanged();
    }

    @Override // javax.swing.ButtonModel
    public void setMnemonic(int i2) {
        this.mnemonic = i2;
        fireStateChanged();
    }

    @Override // javax.swing.ButtonModel
    public int getMnemonic() {
        return this.mnemonic;
    }

    @Override // javax.swing.ButtonModel
    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    @Override // javax.swing.ButtonModel
    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    @Override // javax.swing.ButtonModel
    public void addActionListener(ActionListener actionListener) {
        this.listenerList.add(ActionListener.class, actionListener);
    }

    @Override // javax.swing.ButtonModel
    public void removeActionListener(ActionListener actionListener) {
        this.listenerList.remove(ActionListener.class, actionListener);
    }

    public ActionListener[] getActionListeners() {
        return (ActionListener[]) this.listenerList.getListeners(ActionListener.class);
    }

    protected void fireActionPerformed(ActionEvent actionEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ActionListener.class) {
                ((ActionListener) listenerList[length + 1]).actionPerformed(actionEvent);
            }
        }
    }

    @Override // javax.swing.ButtonModel, java.awt.ItemSelectable
    public void addItemListener(ItemListener itemListener) {
        this.listenerList.add(ItemListener.class, itemListener);
    }

    @Override // javax.swing.ButtonModel, java.awt.ItemSelectable
    public void removeItemListener(ItemListener itemListener) {
        this.listenerList.remove(ItemListener.class, itemListener);
    }

    public ItemListener[] getItemListeners() {
        return (ItemListener[]) this.listenerList.getListeners(ItemListener.class);
    }

    protected void fireItemStateChanged(ItemEvent itemEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ItemListener.class) {
                ((ItemListener) listenerList[length + 1]).itemStateChanged(itemEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    @Override // java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        return null;
    }

    @Override // javax.swing.ButtonModel
    public void setGroup(ButtonGroup buttonGroup) {
        this.group = buttonGroup;
    }

    public ButtonGroup getGroup() {
        return this.group;
    }

    boolean isMenuItem() {
        return this.menuItem;
    }

    void setMenuItem(boolean z2) {
        this.menuItem = z2;
    }
}
