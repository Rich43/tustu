package javax.swing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: rt.jar:javax/swing/InputMap.class */
public class InputMap implements Serializable {
    private transient ArrayTable arrayTable;
    private InputMap parent;

    public void setParent(InputMap inputMap) {
        this.parent = inputMap;
    }

    public InputMap getParent() {
        return this.parent;
    }

    public void put(KeyStroke keyStroke, Object obj) {
        if (keyStroke == null) {
            return;
        }
        if (obj == null) {
            remove(keyStroke);
            return;
        }
        if (this.arrayTable == null) {
            this.arrayTable = new ArrayTable();
        }
        this.arrayTable.put(keyStroke, obj);
    }

    public Object get(KeyStroke keyStroke) {
        InputMap parent;
        if (this.arrayTable == null) {
            InputMap parent2 = getParent();
            if (parent2 != null) {
                return parent2.get(keyStroke);
            }
            return null;
        }
        Object obj = this.arrayTable.get(keyStroke);
        if (obj == null && (parent = getParent()) != null) {
            return parent.get(keyStroke);
        }
        return obj;
    }

    public void remove(KeyStroke keyStroke) {
        if (this.arrayTable != null) {
            this.arrayTable.remove(keyStroke);
        }
    }

    public void clear() {
        if (this.arrayTable != null) {
            this.arrayTable.clear();
        }
    }

    public KeyStroke[] keys() {
        if (this.arrayTable == null) {
            return null;
        }
        KeyStroke[] keyStrokeArr = new KeyStroke[this.arrayTable.size()];
        this.arrayTable.getKeys(keyStrokeArr);
        return keyStrokeArr;
    }

    public int size() {
        if (this.arrayTable == null) {
            return 0;
        }
        return this.arrayTable.size();
    }

    public KeyStroke[] allKeys() {
        int size = size();
        InputMap parent = getParent();
        if (size == 0) {
            if (parent != null) {
                return parent.allKeys();
            }
            return keys();
        }
        if (parent == null) {
            return keys();
        }
        KeyStroke[] keyStrokeArrKeys = keys();
        KeyStroke[] keyStrokeArrAllKeys = parent.allKeys();
        if (keyStrokeArrAllKeys == null) {
            return keyStrokeArrKeys;
        }
        if (keyStrokeArrKeys == null) {
            return keyStrokeArrAllKeys;
        }
        HashMap map = new HashMap();
        for (int length = keyStrokeArrKeys.length - 1; length >= 0; length--) {
            map.put(keyStrokeArrKeys[length], keyStrokeArrKeys[length]);
        }
        for (int length2 = keyStrokeArrAllKeys.length - 1; length2 >= 0; length2--) {
            map.put(keyStrokeArrAllKeys[length2], keyStrokeArrAllKeys[length2]);
        }
        return (KeyStroke[]) map.keySet().toArray(new KeyStroke[map.size()]);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        ArrayTable.writeArrayTable(objectOutputStream, this.arrayTable);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        for (int i2 = objectInputStream.readInt() - 1; i2 >= 0; i2--) {
            put((KeyStroke) objectInputStream.readObject(), objectInputStream.readObject());
        }
    }
}
