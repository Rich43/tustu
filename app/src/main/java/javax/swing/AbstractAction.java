package javax.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import javax.swing.event.SwingPropertyChangeSupport;
import jdk.jfr.Enabled;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:javax/swing/AbstractAction.class */
public abstract class AbstractAction implements Action, Cloneable, Serializable {
    private static Boolean RECONFIGURE_ON_NULL;
    protected boolean enabled;
    private transient ArrayTable arrayTable;
    protected SwingPropertyChangeSupport changeSupport;

    static boolean shouldReconfigure(PropertyChangeEvent propertyChangeEvent) {
        boolean zBooleanValue;
        if (propertyChangeEvent.getPropertyName() == null) {
            synchronized (AbstractAction.class) {
                if (RECONFIGURE_ON_NULL == null) {
                    RECONFIGURE_ON_NULL = Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction("swing.actions.reconfigureOnNull", "false")));
                }
                zBooleanValue = RECONFIGURE_ON_NULL.booleanValue();
            }
            return zBooleanValue;
        }
        return false;
    }

    static void setEnabledFromAction(JComponent jComponent, Action action) {
        jComponent.setEnabled(action != null ? action.isEnabled() : true);
    }

    static void setToolTipTextFromAction(JComponent jComponent, Action action) {
        jComponent.setToolTipText(action != null ? (String) action.getValue(Action.SHORT_DESCRIPTION) : null);
    }

    static boolean hasSelectedKey(Action action) {
        return (action == null || action.getValue(Action.SELECTED_KEY) == null) ? false : true;
    }

    static boolean isSelected(Action action) {
        return Boolean.TRUE.equals(action.getValue(Action.SELECTED_KEY));
    }

    public AbstractAction() {
        this.enabled = true;
    }

    public AbstractAction(String str) {
        this.enabled = true;
        putValue("Name", str);
    }

    public AbstractAction(String str, Icon icon) {
        this(str);
        putValue(Action.SMALL_ICON, icon);
    }

    @Override // javax.swing.Action
    public Object getValue(String str) {
        if (str == Enabled.NAME) {
            return Boolean.valueOf(this.enabled);
        }
        if (this.arrayTable == null) {
            return null;
        }
        return this.arrayTable.get(str);
    }

    @Override // javax.swing.Action
    public void putValue(String str, Object obj) {
        Object objValueOf = null;
        if (str == Enabled.NAME) {
            if (obj == null || !(obj instanceof Boolean)) {
                obj = false;
            }
            objValueOf = Boolean.valueOf(this.enabled);
            this.enabled = ((Boolean) obj).booleanValue();
        } else {
            if (this.arrayTable == null) {
                this.arrayTable = new ArrayTable();
            }
            if (this.arrayTable.containsKey(str)) {
                objValueOf = this.arrayTable.get(str);
            }
            if (obj == null) {
                this.arrayTable.remove(str);
            } else {
                this.arrayTable.put(str, obj);
            }
        }
        firePropertyChange(str, objValueOf, obj);
    }

    @Override // javax.swing.Action
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override // javax.swing.Action
    public void setEnabled(boolean z2) {
        boolean z3 = this.enabled;
        if (z3 != z2) {
            this.enabled = z2;
            firePropertyChange(Enabled.NAME, Boolean.valueOf(z3), Boolean.valueOf(z2));
        }
    }

    public Object[] getKeys() {
        if (this.arrayTable == null) {
            return null;
        }
        Object[] objArr = new Object[this.arrayTable.size()];
        this.arrayTable.getKeys(objArr);
        return objArr;
    }

    protected void firePropertyChange(String str, Object obj, Object obj2) {
        if (this.changeSupport != null) {
            if (obj != null && obj2 != null && obj.equals(obj2)) {
                return;
            }
            this.changeSupport.firePropertyChange(str, obj, obj2);
        }
    }

    @Override // javax.swing.Action
    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            this.changeSupport = new SwingPropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    @Override // javax.swing.Action
    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            return;
        }
        this.changeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners();
    }

    protected Object clone() throws CloneNotSupportedException {
        AbstractAction abstractAction = (AbstractAction) super.clone();
        synchronized (this) {
            if (this.arrayTable != null) {
                abstractAction.arrayTable = (ArrayTable) this.arrayTable.clone();
            }
        }
        return abstractAction;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        ArrayTable.writeArrayTable(objectOutputStream, this.arrayTable);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        for (int i2 = objectInputStream.readInt() - 1; i2 >= 0; i2--) {
            putValue((String) objectInputStream.readObject(), objectInputStream.readObject());
        }
    }
}
