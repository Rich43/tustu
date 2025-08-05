package javax.swing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: rt.jar:javax/swing/ActionMap.class */
public class ActionMap implements Serializable {
    private transient ArrayTable arrayTable;
    private ActionMap parent;

    public void setParent(ActionMap actionMap) {
        this.parent = actionMap;
    }

    public ActionMap getParent() {
        return this.parent;
    }

    public void put(Object obj, Action action) {
        if (obj == null) {
            return;
        }
        if (action == null) {
            remove(obj);
            return;
        }
        if (this.arrayTable == null) {
            this.arrayTable = new ArrayTable();
        }
        this.arrayTable.put(obj, action);
    }

    public Action get(Object obj) {
        ActionMap parent;
        Action action = this.arrayTable == null ? null : (Action) this.arrayTable.get(obj);
        if (action == null && (parent = getParent()) != null) {
            return parent.get(obj);
        }
        return action;
    }

    public void remove(Object obj) {
        if (this.arrayTable != null) {
            this.arrayTable.remove(obj);
        }
    }

    public void clear() {
        if (this.arrayTable != null) {
            this.arrayTable.clear();
        }
    }

    public Object[] keys() {
        if (this.arrayTable == null) {
            return null;
        }
        return this.arrayTable.getKeys(null);
    }

    public int size() {
        if (this.arrayTable == null) {
            return 0;
        }
        return this.arrayTable.size();
    }

    public Object[] allKeys() {
        int size = size();
        ActionMap parent = getParent();
        if (size == 0) {
            if (parent != null) {
                return parent.allKeys();
            }
            return keys();
        }
        if (parent == null) {
            return keys();
        }
        Object[] objArrKeys = keys();
        Object[] objArrAllKeys = parent.allKeys();
        if (objArrAllKeys == null) {
            return objArrKeys;
        }
        if (objArrKeys == null) {
            return objArrAllKeys;
        }
        HashMap map = new HashMap();
        for (int length = objArrKeys.length - 1; length >= 0; length--) {
            map.put(objArrKeys[length], objArrKeys[length]);
        }
        for (int length2 = objArrAllKeys.length - 1; length2 >= 0; length2--) {
            map.put(objArrAllKeys[length2], objArrAllKeys[length2]);
        }
        return map.keySet().toArray();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        ArrayTable.writeArrayTable(objectOutputStream, this.arrayTable);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        for (int i2 = objectInputStream.readInt() - 1; i2 >= 0; i2--) {
            put(objectInputStream.readObject(), (Action) objectInputStream.readObject());
        }
    }
}
