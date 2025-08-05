package javax.swing;

import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/* loaded from: rt.jar:javax/swing/AbstractListModel.class */
public abstract class AbstractListModel<E> implements ListModel<E>, Serializable {
    protected EventListenerList listenerList = new EventListenerList();

    @Override // javax.swing.ListModel
    public void addListDataListener(ListDataListener listDataListener) {
        this.listenerList.add(ListDataListener.class, listDataListener);
    }

    @Override // javax.swing.ListModel
    public void removeListDataListener(ListDataListener listDataListener) {
        this.listenerList.remove(ListDataListener.class, listDataListener);
    }

    public ListDataListener[] getListDataListeners() {
        return (ListDataListener[]) this.listenerList.getListeners(ListDataListener.class);
    }

    protected void fireContentsChanged(Object obj, int i2, int i3) {
        Object[] listenerList = this.listenerList.getListenerList();
        ListDataEvent listDataEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ListDataListener.class) {
                if (listDataEvent == null) {
                    listDataEvent = new ListDataEvent(obj, 0, i2, i3);
                }
                ((ListDataListener) listenerList[length + 1]).contentsChanged(listDataEvent);
            }
        }
    }

    protected void fireIntervalAdded(Object obj, int i2, int i3) {
        Object[] listenerList = this.listenerList.getListenerList();
        ListDataEvent listDataEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ListDataListener.class) {
                if (listDataEvent == null) {
                    listDataEvent = new ListDataEvent(obj, 1, i2, i3);
                }
                ((ListDataListener) listenerList[length + 1]).intervalAdded(listDataEvent);
            }
        }
    }

    protected void fireIntervalRemoved(Object obj, int i2, int i3) {
        Object[] listenerList = this.listenerList.getListenerList();
        ListDataEvent listDataEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ListDataListener.class) {
                if (listDataEvent == null) {
                    listDataEvent = new ListDataEvent(obj, 2, i2, i3);
                }
                ((ListDataListener) listenerList[length + 1]).intervalRemoved(listDataEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }
}
