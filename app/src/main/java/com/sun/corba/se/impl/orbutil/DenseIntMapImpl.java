package com.sun.corba.se.impl.orbutil;

import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/DenseIntMapImpl.class */
public class DenseIntMapImpl {
    private ArrayList list = new ArrayList();

    private void checkKey(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Key must be >= 0.");
        }
    }

    public Object get(int i2) {
        checkKey(i2);
        Object obj = null;
        if (i2 < this.list.size()) {
            obj = this.list.get(i2);
        }
        return obj;
    }

    public void set(int i2, Object obj) {
        checkKey(i2);
        extend(i2);
        this.list.set(i2, obj);
    }

    private void extend(int i2) {
        if (i2 >= this.list.size()) {
            this.list.ensureCapacity(i2 + 1);
            int size = this.list.size();
            while (true) {
                int i3 = size;
                size++;
                if (i3 <= i2) {
                    this.list.add(null);
                } else {
                    return;
                }
            }
        }
    }
}
