package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.MakeImmutable;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/FreezableList.class */
public class FreezableList extends AbstractList {
    private List delegate;
    private boolean immutable;

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FreezableList)) {
            return false;
        }
        FreezableList freezableList = (FreezableList) obj;
        return this.delegate.equals(freezableList.delegate) && this.immutable == freezableList.immutable;
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        return this.delegate.hashCode();
    }

    public FreezableList(List list, boolean z2) {
        this.delegate = null;
        this.immutable = false;
        this.delegate = list;
        this.immutable = z2;
    }

    public FreezableList(List list) {
        this(list, false);
    }

    public void makeImmutable() {
        this.immutable = true;
    }

    public boolean isImmutable() {
        return this.immutable;
    }

    public void makeElementsImmutable() {
        Iterator it = iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof MakeImmutable) {
                ((MakeImmutable) next).makeImmutable();
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.delegate.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int i2) {
        return this.delegate.get(i2);
    }

    @Override // java.util.AbstractList, java.util.List
    public Object set(int i2, Object obj) {
        if (this.immutable) {
            throw new UnsupportedOperationException();
        }
        return this.delegate.set(i2, obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i2, Object obj) {
        if (this.immutable) {
            throw new UnsupportedOperationException();
        }
        this.delegate.add(i2, obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public Object remove(int i2) {
        if (this.immutable) {
            throw new UnsupportedOperationException();
        }
        return this.delegate.remove(i2);
    }

    @Override // java.util.AbstractList, java.util.List
    public List subList(int i2, int i3) {
        return new FreezableList(this.delegate.subList(i2, i3), this.immutable);
    }
}
