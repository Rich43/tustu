package java.beans;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/* loaded from: rt.jar:java/beans/PropertyChangeSupport.class */
public class PropertyChangeSupport implements Serializable {
    private PropertyChangeListenerMap map = new PropertyChangeListenerMap();
    private Object source;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField(Constants.ELEMNAME_CHILDREN_STRING, Hashtable.class), new ObjectStreamField("source", Object.class), new ObjectStreamField("propertyChangeSupportSerializedDataVersion", Integer.TYPE)};
    static final long serialVersionUID = 6401253773779951803L;

    public PropertyChangeSupport(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        this.source = obj;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener == null) {
            return;
        }
        if (propertyChangeListener instanceof PropertyChangeListenerProxy) {
            PropertyChangeListenerProxy propertyChangeListenerProxy = (PropertyChangeListenerProxy) propertyChangeListener;
            addPropertyChangeListener(propertyChangeListenerProxy.getPropertyName(), propertyChangeListenerProxy.getListener());
        } else {
            this.map.add(null, propertyChangeListener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener == null) {
            return;
        }
        if (propertyChangeListener instanceof PropertyChangeListenerProxy) {
            PropertyChangeListenerProxy propertyChangeListenerProxy = (PropertyChangeListenerProxy) propertyChangeListener;
            removePropertyChangeListener(propertyChangeListenerProxy.getPropertyName(), propertyChangeListenerProxy.getListener());
        } else {
            this.map.remove(null, propertyChangeListener);
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.map.getListeners();
    }

    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        PropertyChangeListener propertyChangeListenerExtract;
        if (propertyChangeListener != null && str != null && (propertyChangeListenerExtract = this.map.extract(propertyChangeListener)) != null) {
            this.map.add(str, propertyChangeListenerExtract);
        }
    }

    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        PropertyChangeListener propertyChangeListenerExtract;
        if (propertyChangeListener != null && str != null && (propertyChangeListenerExtract = this.map.extract(propertyChangeListener)) != null) {
            this.map.remove(str, propertyChangeListenerExtract);
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String str) {
        return this.map.getListeners(str);
    }

    public void firePropertyChange(String str, Object obj, Object obj2) {
        if (obj == null || obj2 == null || !obj.equals(obj2)) {
            firePropertyChange(new PropertyChangeEvent(this.source, str, obj, obj2));
        }
    }

    public void firePropertyChange(String str, int i2, int i3) {
        if (i2 != i3) {
            firePropertyChange(str, Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    public void firePropertyChange(String str, boolean z2, boolean z3) {
        if (z2 != z3) {
            firePropertyChange(str, Boolean.valueOf(z2), Boolean.valueOf(z3));
        }
    }

    public void firePropertyChange(PropertyChangeEvent propertyChangeEvent) {
        Object oldValue = propertyChangeEvent.getOldValue();
        Object newValue = propertyChangeEvent.getNewValue();
        if (oldValue == null || newValue == null || !oldValue.equals(newValue)) {
            String propertyName = propertyChangeEvent.getPropertyName();
            PropertyChangeListener[] propertyChangeListenerArr = this.map.get(null);
            PropertyChangeListener[] propertyChangeListenerArr2 = propertyName != null ? this.map.get(propertyName) : null;
            fire(propertyChangeListenerArr, propertyChangeEvent);
            fire(propertyChangeListenerArr2, propertyChangeEvent);
        }
    }

    private static void fire(PropertyChangeListener[] propertyChangeListenerArr, PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeListenerArr != null) {
            for (PropertyChangeListener propertyChangeListener : propertyChangeListenerArr) {
                propertyChangeListener.propertyChange(propertyChangeEvent);
            }
        }
    }

    public void fireIndexedPropertyChange(String str, int i2, Object obj, Object obj2) {
        if (obj == null || obj2 == null || !obj.equals(obj2)) {
            firePropertyChange(new IndexedPropertyChangeEvent(this.source, str, obj, obj2, i2));
        }
    }

    public void fireIndexedPropertyChange(String str, int i2, int i3, int i4) {
        if (i3 != i4) {
            fireIndexedPropertyChange(str, i2, Integer.valueOf(i3), Integer.valueOf(i4));
        }
    }

    public void fireIndexedPropertyChange(String str, int i2, boolean z2, boolean z3) {
        if (z2 != z3) {
            fireIndexedPropertyChange(str, i2, Boolean.valueOf(z2), Boolean.valueOf(z3));
        }
    }

    public boolean hasListeners(String str) {
        return this.map.hasListeners(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable = null;
        PropertyChangeListener[] value = null;
        synchronized (this.map) {
            for (Map.Entry<String, PropertyChangeListener[]> entry : this.map.getEntries()) {
                String key = entry.getKey();
                if (key == null) {
                    value = entry.getValue();
                } else {
                    if (hashtable == null) {
                        hashtable = new Hashtable();
                    }
                    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this.source);
                    propertyChangeSupport.map.set(null, entry.getValue());
                    hashtable.put(key, propertyChangeSupport);
                }
            }
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put(Constants.ELEMNAME_CHILDREN_STRING, hashtable);
        putFieldPutFields.put("source", this.source);
        putFieldPutFields.put("propertyChangeSupportSerializedDataVersion", 2);
        objectOutputStream.writeFields();
        if (value != null) {
            for (PropertyChangeListener propertyChangeListener : value) {
                if (propertyChangeListener instanceof Serializable) {
                    objectOutputStream.writeObject(propertyChangeListener);
                }
            }
        }
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.map = new PropertyChangeListenerMap();
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Hashtable hashtable = (Hashtable) fields.get(Constants.ELEMNAME_CHILDREN_STRING, (Object) null);
        this.source = fields.get("source", (Object) null);
        fields.get("propertyChangeSupportSerializedDataVersion", 2);
        while (true) {
            Object object = objectInputStream.readObject();
            if (null == object) {
                break;
            } else {
                this.map.add(null, (PropertyChangeListener) object);
            }
        }
        if (hashtable != null) {
            for (Map.Entry entry : hashtable.entrySet()) {
                for (PropertyChangeListener propertyChangeListener : ((PropertyChangeSupport) entry.getValue()).getPropertyChangeListeners()) {
                    this.map.add((String) entry.getKey(), propertyChangeListener);
                }
            }
        }
    }

    /* loaded from: rt.jar:java/beans/PropertyChangeSupport$PropertyChangeListenerMap.class */
    private static final class PropertyChangeListenerMap extends ChangeListenerMap<PropertyChangeListener> {
        private static final PropertyChangeListener[] EMPTY = new PropertyChangeListener[0];

        private PropertyChangeListenerMap() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.beans.ChangeListenerMap
        public PropertyChangeListener[] newArray(int i2) {
            return 0 < i2 ? new PropertyChangeListener[i2] : EMPTY;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.beans.ChangeListenerMap
        public PropertyChangeListener newProxy(String str, PropertyChangeListener propertyChangeListener) {
            return new PropertyChangeListenerProxy(str, propertyChangeListener);
        }

        @Override // java.beans.ChangeListenerMap
        public final PropertyChangeListener extract(PropertyChangeListener propertyChangeListener) {
            while (propertyChangeListener instanceof PropertyChangeListenerProxy) {
                propertyChangeListener = ((PropertyChangeListenerProxy) propertyChangeListener).getListener();
            }
            return propertyChangeListener;
        }
    }
}
