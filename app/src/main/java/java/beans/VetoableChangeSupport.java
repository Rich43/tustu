package java.beans;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/* loaded from: rt.jar:java/beans/VetoableChangeSupport.class */
public class VetoableChangeSupport implements Serializable {
    private VetoableChangeListenerMap map = new VetoableChangeListenerMap();
    private Object source;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField(Constants.ELEMNAME_CHILDREN_STRING, Hashtable.class), new ObjectStreamField("source", Object.class), new ObjectStreamField("vetoableChangeSupportSerializedDataVersion", Integer.TYPE)};
    static final long serialVersionUID = -5090210921595982017L;

    public VetoableChangeSupport(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        this.source = obj;
    }

    public void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (vetoableChangeListener == null) {
            return;
        }
        if (vetoableChangeListener instanceof VetoableChangeListenerProxy) {
            VetoableChangeListenerProxy vetoableChangeListenerProxy = (VetoableChangeListenerProxy) vetoableChangeListener;
            addVetoableChangeListener(vetoableChangeListenerProxy.getPropertyName(), vetoableChangeListenerProxy.getListener());
        } else {
            this.map.add(null, vetoableChangeListener);
        }
    }

    public void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (vetoableChangeListener == null) {
            return;
        }
        if (vetoableChangeListener instanceof VetoableChangeListenerProxy) {
            VetoableChangeListenerProxy vetoableChangeListenerProxy = (VetoableChangeListenerProxy) vetoableChangeListener;
            removeVetoableChangeListener(vetoableChangeListenerProxy.getPropertyName(), vetoableChangeListenerProxy.getListener());
        } else {
            this.map.remove(null, vetoableChangeListener);
        }
    }

    public VetoableChangeListener[] getVetoableChangeListeners() {
        return this.map.getListeners();
    }

    public void addVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        VetoableChangeListener vetoableChangeListenerExtract;
        if (vetoableChangeListener != null && str != null && (vetoableChangeListenerExtract = this.map.extract(vetoableChangeListener)) != null) {
            this.map.add(str, vetoableChangeListenerExtract);
        }
    }

    public void removeVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        VetoableChangeListener vetoableChangeListenerExtract;
        if (vetoableChangeListener != null && str != null && (vetoableChangeListenerExtract = this.map.extract(vetoableChangeListener)) != null) {
            this.map.remove(str, vetoableChangeListenerExtract);
        }
    }

    public VetoableChangeListener[] getVetoableChangeListeners(String str) {
        return this.map.getListeners(str);
    }

    public void fireVetoableChange(String str, Object obj, Object obj2) throws PropertyVetoException {
        if (obj == null || obj2 == null || !obj.equals(obj2)) {
            fireVetoableChange(new PropertyChangeEvent(this.source, str, obj, obj2));
        }
    }

    public void fireVetoableChange(String str, int i2, int i3) throws PropertyVetoException {
        if (i2 != i3) {
            fireVetoableChange(str, Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    public void fireVetoableChange(String str, boolean z2, boolean z3) throws PropertyVetoException {
        if (z2 != z3) {
            fireVetoableChange(str, Boolean.valueOf(z2), Boolean.valueOf(z3));
        }
    }

    public void fireVetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        VetoableChangeListener[] vetoableChangeListenerArr;
        Object oldValue = propertyChangeEvent.getOldValue();
        Object newValue = propertyChangeEvent.getNewValue();
        if (oldValue == null || newValue == null || !oldValue.equals(newValue)) {
            String propertyName = propertyChangeEvent.getPropertyName();
            VetoableChangeListener[] vetoableChangeListenerArr2 = this.map.get(null);
            VetoableChangeListener[] vetoableChangeListenerArr3 = propertyName != null ? this.map.get(propertyName) : null;
            if (vetoableChangeListenerArr2 == null) {
                vetoableChangeListenerArr = vetoableChangeListenerArr3;
            } else if (vetoableChangeListenerArr3 == null) {
                vetoableChangeListenerArr = vetoableChangeListenerArr2;
            } else {
                vetoableChangeListenerArr = new VetoableChangeListener[vetoableChangeListenerArr2.length + vetoableChangeListenerArr3.length];
                System.arraycopy(vetoableChangeListenerArr2, 0, vetoableChangeListenerArr, 0, vetoableChangeListenerArr2.length);
                System.arraycopy(vetoableChangeListenerArr3, 0, vetoableChangeListenerArr, vetoableChangeListenerArr2.length, vetoableChangeListenerArr3.length);
            }
            if (vetoableChangeListenerArr != null) {
                for (int i2 = 0; i2 < vetoableChangeListenerArr.length; i2++) {
                    try {
                        vetoableChangeListenerArr[i2].vetoableChange(propertyChangeEvent);
                    } catch (PropertyVetoException e2) {
                        PropertyChangeEvent propertyChangeEvent2 = new PropertyChangeEvent(this.source, propertyName, newValue, oldValue);
                        for (int i3 = 0; i3 < i2; i3++) {
                            try {
                                vetoableChangeListenerArr[i3].vetoableChange(propertyChangeEvent2);
                            } catch (PropertyVetoException e3) {
                            }
                        }
                        throw e2;
                    }
                }
            }
        }
    }

    public boolean hasListeners(String str) {
        return this.map.hasListeners(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable = null;
        VetoableChangeListener[] value = null;
        synchronized (this.map) {
            for (Map.Entry<String, VetoableChangeListener[]> entry : this.map.getEntries()) {
                String key = entry.getKey();
                if (key == null) {
                    value = entry.getValue();
                } else {
                    if (hashtable == null) {
                        hashtable = new Hashtable();
                    }
                    VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this.source);
                    vetoableChangeSupport.map.set(null, entry.getValue());
                    hashtable.put(key, vetoableChangeSupport);
                }
            }
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put(Constants.ELEMNAME_CHILDREN_STRING, hashtable);
        putFieldPutFields.put("source", this.source);
        putFieldPutFields.put("vetoableChangeSupportSerializedDataVersion", 2);
        objectOutputStream.writeFields();
        if (value != null) {
            for (VetoableChangeListener vetoableChangeListener : value) {
                if (vetoableChangeListener instanceof Serializable) {
                    objectOutputStream.writeObject(vetoableChangeListener);
                }
            }
        }
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.map = new VetoableChangeListenerMap();
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Hashtable hashtable = (Hashtable) fields.get(Constants.ELEMNAME_CHILDREN_STRING, (Object) null);
        this.source = fields.get("source", (Object) null);
        fields.get("vetoableChangeSupportSerializedDataVersion", 2);
        while (true) {
            Object object = objectInputStream.readObject();
            if (null == object) {
                break;
            } else {
                this.map.add(null, (VetoableChangeListener) object);
            }
        }
        if (hashtable != null) {
            for (Map.Entry entry : hashtable.entrySet()) {
                for (VetoableChangeListener vetoableChangeListener : ((VetoableChangeSupport) entry.getValue()).getVetoableChangeListeners()) {
                    this.map.add((String) entry.getKey(), vetoableChangeListener);
                }
            }
        }
    }

    /* loaded from: rt.jar:java/beans/VetoableChangeSupport$VetoableChangeListenerMap.class */
    private static final class VetoableChangeListenerMap extends ChangeListenerMap<VetoableChangeListener> {
        private static final VetoableChangeListener[] EMPTY = new VetoableChangeListener[0];

        private VetoableChangeListenerMap() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.beans.ChangeListenerMap
        public VetoableChangeListener[] newArray(int i2) {
            return 0 < i2 ? new VetoableChangeListener[i2] : EMPTY;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.beans.ChangeListenerMap
        public VetoableChangeListener newProxy(String str, VetoableChangeListener vetoableChangeListener) {
            return new VetoableChangeListenerProxy(str, vetoableChangeListener);
        }

        @Override // java.beans.ChangeListenerMap
        public final VetoableChangeListener extract(VetoableChangeListener vetoableChangeListener) {
            while (vetoableChangeListener instanceof VetoableChangeListenerProxy) {
                vetoableChangeListener = ((VetoableChangeListenerProxy) vetoableChangeListener).getListener();
            }
            return vetoableChangeListener;
        }
    }
}
