package sun.awt;

import java.lang.reflect.Array;
import java.util.EventListener;

/* loaded from: rt.jar:sun/awt/EventListenerAggregate.class */
public class EventListenerAggregate {
    private EventListener[] listenerList;

    public EventListenerAggregate(Class<? extends EventListener> cls) {
        if (cls == null) {
            throw new NullPointerException("listener class is null");
        }
        this.listenerList = (EventListener[]) Array.newInstance(cls, 0);
    }

    private Class<?> getListenerClass() {
        return this.listenerList.getClass().getComponentType();
    }

    public synchronized void add(EventListener eventListener) {
        Class<?> listenerClass = getListenerClass();
        if (!listenerClass.isInstance(eventListener)) {
            throw new ClassCastException("listener " + ((Object) eventListener) + " is not an instance of listener class " + ((Object) listenerClass));
        }
        EventListener[] eventListenerArr = (EventListener[]) Array.newInstance(listenerClass, this.listenerList.length + 1);
        System.arraycopy(this.listenerList, 0, eventListenerArr, 0, this.listenerList.length);
        eventListenerArr[this.listenerList.length] = eventListener;
        this.listenerList = eventListenerArr;
    }

    public synchronized boolean remove(EventListener eventListener) {
        Class<?> listenerClass = getListenerClass();
        if (!listenerClass.isInstance(eventListener)) {
            throw new ClassCastException("listener " + ((Object) eventListener) + " is not an instance of listener class " + ((Object) listenerClass));
        }
        for (int i2 = 0; i2 < this.listenerList.length; i2++) {
            if (this.listenerList[i2].equals(eventListener)) {
                EventListener[] eventListenerArr = (EventListener[]) Array.newInstance(listenerClass, this.listenerList.length - 1);
                System.arraycopy(this.listenerList, 0, eventListenerArr, 0, i2);
                System.arraycopy(this.listenerList, i2 + 1, eventListenerArr, i2, (this.listenerList.length - i2) - 1);
                this.listenerList = eventListenerArr;
                return true;
            }
        }
        return false;
    }

    public synchronized EventListener[] getListenersInternal() {
        return this.listenerList;
    }

    public synchronized EventListener[] getListenersCopy() {
        return this.listenerList.length == 0 ? this.listenerList : (EventListener[]) this.listenerList.clone();
    }

    public synchronized int size() {
        return this.listenerList.length;
    }

    public synchronized boolean isEmpty() {
        return this.listenerList.length == 0;
    }
}
