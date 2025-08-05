package javax.swing.event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.EventListener;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/swing/event/EventListenerList.class */
public class EventListenerList implements Serializable {
    private static final Object[] NULL_ARRAY = new Object[0];
    protected transient Object[] listenerList = NULL_ARRAY;

    public Object[] getListenerList() {
        return this.listenerList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        Object[] objArr = this.listenerList;
        T[] tArr = (T[]) ((EventListener[]) Array.newInstance((Class<?>) cls, getListenerCount(objArr, cls)));
        int i2 = 0;
        for (int length = objArr.length - 2; length >= 0; length -= 2) {
            if (objArr[length] == cls) {
                int i3 = i2;
                i2++;
                tArr[i3] = (EventListener) objArr[length + 1];
            }
        }
        return tArr;
    }

    public int getListenerCount() {
        return this.listenerList.length / 2;
    }

    public int getListenerCount(Class<?> cls) {
        return getListenerCount(this.listenerList, cls);
    }

    private int getListenerCount(Object[] objArr, Class cls) {
        int i2 = 0;
        for (int i3 = 0; i3 < objArr.length; i3 += 2) {
            if (cls == ((Class) objArr[i3])) {
                i2++;
            }
        }
        return i2;
    }

    public synchronized <T extends EventListener> void add(Class<T> cls, T t2) {
        if (t2 == null) {
            return;
        }
        if (!cls.isInstance(t2)) {
            throw new IllegalArgumentException("Listener " + ((Object) t2) + " is not of type " + ((Object) cls));
        }
        if (this.listenerList == NULL_ARRAY) {
            this.listenerList = new Object[]{cls, t2};
            return;
        }
        int length = this.listenerList.length;
        Object[] objArr = new Object[length + 2];
        System.arraycopy(this.listenerList, 0, objArr, 0, length);
        objArr[length] = cls;
        objArr[length + 1] = t2;
        this.listenerList = objArr;
    }

    public synchronized <T extends EventListener> void remove(Class<T> cls, T t2) {
        if (t2 == null) {
            return;
        }
        if (!cls.isInstance(t2)) {
            throw new IllegalArgumentException("Listener " + ((Object) t2) + " is not of type " + ((Object) cls));
        }
        int i2 = -1;
        int length = this.listenerList.length - 2;
        while (true) {
            if (length >= 0) {
                if (this.listenerList[length] != cls || !this.listenerList[length + 1].equals(t2)) {
                    length -= 2;
                } else {
                    i2 = length;
                    break;
                }
            } else {
                break;
            }
        }
        if (i2 != -1) {
            Object[] objArr = new Object[this.listenerList.length - 2];
            System.arraycopy(this.listenerList, 0, objArr, 0, i2);
            if (i2 < objArr.length) {
                System.arraycopy(this.listenerList, i2 + 2, objArr, i2, objArr.length - i2);
            }
            this.listenerList = objArr.length == 0 ? NULL_ARRAY : objArr;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Object[] objArr = this.listenerList;
        objectOutputStream.defaultWriteObject();
        for (int i2 = 0; i2 < objArr.length; i2 += 2) {
            Class cls = (Class) objArr[i2];
            EventListener eventListener = (EventListener) objArr[i2 + 1];
            if (eventListener != null && (eventListener instanceof Serializable)) {
                objectOutputStream.writeObject(cls.getName());
                objectOutputStream.writeObject(eventListener);
            }
        }
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.listenerList = NULL_ARRAY;
        objectInputStream.defaultReadObject();
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                EventListener eventListener = (EventListener) objectInputStream.readObject();
                String str = (String) object;
                ReflectUtil.checkPackageAccess(str);
                add(Class.forName(str, true, contextClassLoader), eventListener);
            } else {
                return;
            }
        }
    }

    public String toString() {
        Object[] objArr = this.listenerList;
        String str = "EventListenerList: " + (objArr.length / 2) + " listeners: ";
        for (int i2 = 0; i2 <= objArr.length - 2; i2 += 2) {
            str = (str + " type " + ((Class) objArr[i2]).getName()) + " listener " + objArr[i2 + 1];
        }
        return str;
    }
}
