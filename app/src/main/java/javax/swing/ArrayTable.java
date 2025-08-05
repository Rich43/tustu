package javax.swing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/* loaded from: rt.jar:javax/swing/ArrayTable.class */
class ArrayTable implements Cloneable {
    private Object table = null;
    private static final int ARRAY_BOUNDARY = 8;

    ArrayTable() {
    }

    static void writeArrayTable(ObjectOutputStream objectOutputStream, ArrayTable arrayTable) throws IOException {
        Object[] keys;
        if (arrayTable == null || (keys = arrayTable.getKeys(null)) == null) {
            objectOutputStream.writeInt(0);
            return;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < keys.length; i3++) {
            Object obj = keys[i3];
            if (((obj instanceof Serializable) && (arrayTable.get(obj) instanceof Serializable)) || ((obj instanceof ClientPropertyKey) && ((ClientPropertyKey) obj).getReportValueNotSerializable())) {
                i2++;
            } else {
                keys[i3] = null;
            }
        }
        objectOutputStream.writeInt(i2);
        if (i2 > 0) {
            for (Object obj2 : keys) {
                if (obj2 != null) {
                    objectOutputStream.writeObject(obj2);
                    objectOutputStream.writeObject(arrayTable.get(obj2));
                    i2--;
                    if (i2 == 0) {
                        return;
                    }
                }
            }
        }
    }

    public void put(Object obj, Object obj2) {
        if (this.table == null) {
            this.table = new Object[]{obj, obj2};
            return;
        }
        int size = size();
        if (size < 8) {
            if (containsKey(obj)) {
                Object[] objArr = (Object[]) this.table;
                for (int i2 = 0; i2 < objArr.length - 1; i2 += 2) {
                    if (objArr[i2].equals(obj)) {
                        objArr[i2 + 1] = obj2;
                        return;
                    }
                }
                return;
            }
            Object[] objArr2 = (Object[]) this.table;
            int length = objArr2.length;
            Object[] objArr3 = new Object[length + 2];
            System.arraycopy(objArr2, 0, objArr3, 0, length);
            objArr3[length] = obj;
            objArr3[length + 1] = obj2;
            this.table = objArr3;
            return;
        }
        if (size == 8 && isArray()) {
            grow();
        }
        ((Hashtable) this.table).put(obj, obj2);
    }

    public Object get(Object obj) {
        Object obj2 = null;
        if (this.table != null) {
            if (isArray()) {
                Object[] objArr = (Object[]) this.table;
                int i2 = 0;
                while (true) {
                    if (i2 >= objArr.length - 1) {
                        break;
                    }
                    if (!objArr[i2].equals(obj)) {
                        i2 += 2;
                    } else {
                        obj2 = objArr[i2 + 1];
                        break;
                    }
                }
            } else {
                obj2 = ((Hashtable) this.table).get(obj);
            }
        }
        return obj2;
    }

    public int size() {
        int size;
        if (this.table == null) {
            return 0;
        }
        if (isArray()) {
            size = ((Object[]) this.table).length / 2;
        } else {
            size = ((Hashtable) this.table).size();
        }
        return size;
    }

    public boolean containsKey(Object obj) {
        boolean zContainsKey = false;
        if (this.table != null) {
            if (isArray()) {
                Object[] objArr = (Object[]) this.table;
                int i2 = 0;
                while (true) {
                    if (i2 >= objArr.length - 1) {
                        break;
                    }
                    if (!objArr[i2].equals(obj)) {
                        i2 += 2;
                    } else {
                        zContainsKey = true;
                        break;
                    }
                }
            } else {
                zContainsKey = ((Hashtable) this.table).containsKey(obj);
            }
        }
        return zContainsKey;
    }

    public Object remove(Object obj) {
        Object objRemove = null;
        if (obj == null) {
            return null;
        }
        if (this.table != null) {
            if (isArray()) {
                int i2 = -1;
                Object[] objArr = (Object[]) this.table;
                int length = objArr.length - 2;
                while (true) {
                    if (length < 0) {
                        break;
                    }
                    if (!objArr[length].equals(obj)) {
                        length -= 2;
                    } else {
                        i2 = length;
                        objRemove = objArr[length + 1];
                        break;
                    }
                }
                if (i2 != -1) {
                    Object[] objArr2 = new Object[objArr.length - 2];
                    System.arraycopy(objArr, 0, objArr2, 0, i2);
                    if (i2 < objArr2.length) {
                        System.arraycopy(objArr, i2 + 2, objArr2, i2, objArr2.length - i2);
                    }
                    this.table = objArr2.length == 0 ? null : objArr2;
                }
            } else {
                objRemove = ((Hashtable) this.table).remove(obj);
            }
            if (size() == 7 && !isArray()) {
                shrink();
            }
        }
        return objRemove;
    }

    public void clear() {
        this.table = null;
    }

    public Object clone() {
        ArrayTable arrayTable = new ArrayTable();
        if (isArray()) {
            Object[] objArr = (Object[]) this.table;
            for (int i2 = 0; i2 < objArr.length - 1; i2 += 2) {
                arrayTable.put(objArr[i2], objArr[i2 + 1]);
            }
        } else {
            Hashtable hashtable = (Hashtable) this.table;
            Enumeration enumerationKeys = hashtable.keys();
            while (enumerationKeys.hasMoreElements()) {
                Object objNextElement = enumerationKeys.nextElement2();
                arrayTable.put(objNextElement, hashtable.get(objNextElement));
            }
        }
        return arrayTable;
    }

    public Object[] getKeys(Object[] objArr) {
        if (this.table == null) {
            return null;
        }
        if (isArray()) {
            Object[] objArr2 = (Object[]) this.table;
            if (objArr == null) {
                objArr = new Object[objArr2.length / 2];
            }
            int i2 = 0;
            int i3 = 0;
            while (i2 < objArr2.length - 1) {
                objArr[i3] = objArr2[i2];
                i2 += 2;
                i3++;
            }
        } else {
            Hashtable hashtable = (Hashtable) this.table;
            Enumeration enumerationKeys = hashtable.keys();
            int size = hashtable.size();
            if (objArr == null) {
                objArr = new Object[size];
            }
            while (size > 0) {
                size--;
                objArr[size] = enumerationKeys.nextElement2();
            }
        }
        return objArr;
    }

    private boolean isArray() {
        return this.table instanceof Object[];
    }

    private void grow() {
        Object[] objArr = (Object[]) this.table;
        Hashtable hashtable = new Hashtable(objArr.length / 2);
        for (int i2 = 0; i2 < objArr.length; i2 += 2) {
            hashtable.put(objArr[i2], objArr[i2 + 1]);
        }
        this.table = hashtable;
    }

    private void shrink() {
        Hashtable hashtable = (Hashtable) this.table;
        Object[] objArr = new Object[hashtable.size() * 2];
        Enumeration enumerationKeys = hashtable.keys();
        int i2 = 0;
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement = enumerationKeys.nextElement2();
            objArr[i2] = objNextElement;
            objArr[i2 + 1] = hashtable.get(objNextElement);
            i2 += 2;
        }
        this.table = objArr;
    }
}
