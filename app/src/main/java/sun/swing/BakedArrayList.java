package sun.swing;

import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:sun/swing/BakedArrayList.class */
public class BakedArrayList extends ArrayList {
    private int _hashCode;

    public BakedArrayList(int i2) {
        super(i2);
    }

    public BakedArrayList(List list) {
        this(list.size());
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            add(list.get(i2));
        }
        cacheHashCode();
    }

    public void cacheHashCode() {
        this._hashCode = 1;
        for (int size = size() - 1; size >= 0; size--) {
            this._hashCode = (31 * this._hashCode) + get(size).hashCode();
        }
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        return this._hashCode;
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        BakedArrayList bakedArrayList = (BakedArrayList) obj;
        int size = size();
        if (bakedArrayList.size() != size) {
            return false;
        }
        do {
            int i2 = size;
            size--;
            if (i2 <= 0) {
                return true;
            }
        } while (get(size).equals(bakedArrayList.get(size)));
        return false;
    }
}
