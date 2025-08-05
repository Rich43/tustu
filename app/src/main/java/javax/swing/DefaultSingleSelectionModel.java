package javax.swing;

import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/* loaded from: rt.jar:javax/swing/DefaultSingleSelectionModel.class */
public class DefaultSingleSelectionModel implements SingleSelectionModel, Serializable {
    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();
    private int index = -1;

    @Override // javax.swing.SingleSelectionModel
    public int getSelectedIndex() {
        return this.index;
    }

    @Override // javax.swing.SingleSelectionModel
    public void setSelectedIndex(int i2) {
        if (this.index != i2) {
            this.index = i2;
            fireStateChanged();
        }
    }

    @Override // javax.swing.SingleSelectionModel
    public void clearSelection() {
        setSelectedIndex(-1);
    }

    @Override // javax.swing.SingleSelectionModel
    public boolean isSelected() {
        boolean z2 = false;
        if (getSelectedIndex() != -1) {
            z2 = true;
        }
        return z2;
    }

    @Override // javax.swing.SingleSelectionModel
    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    @Override // javax.swing.SingleSelectionModel
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

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }
}
