package com.sun.xml.internal.bind.v2.util;

import java.util.AbstractList;
import java.util.Arrays;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/util/CollisionCheckStack.class */
public final class CollisionCheckStack<E> extends AbstractList<E> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private int size = 0;
    private boolean latestPushResult = false;
    private boolean useIdentity = true;
    private final int[] initialHash = new int[17];
    private Object[] data = new Object[16];
    private int[] next = new int[16];

    static {
        $assertionsDisabled = !CollisionCheckStack.class.desiredAssertionStatus();
    }

    public void setUseIdentity(boolean useIdentity) {
        this.useIdentity = useIdentity;
    }

    public boolean getUseIdentity() {
        return this.useIdentity;
    }

    public boolean getLatestPushResult() {
        return this.latestPushResult;
    }

    public boolean push(E o2) {
        if (this.data.length == this.size) {
            expandCapacity();
        }
        this.data[this.size] = o2;
        int hash = hash(o2);
        boolean r2 = findDuplicate(o2, hash);
        this.next[this.size] = this.initialHash[hash];
        this.initialHash[hash] = this.size + 1;
        this.size++;
        this.latestPushResult = r2;
        return this.latestPushResult;
    }

    public void pushNocheck(E o2) {
        if (this.data.length == this.size) {
            expandCapacity();
        }
        this.data[this.size] = o2;
        this.next[this.size] = -1;
        this.size++;
    }

    public boolean findDuplicate(E o2) {
        int hash = hash(o2);
        return findDuplicate(o2, hash);
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i2) {
        return (E) this.data[i2];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    private int hash(Object o2) {
        return ((this.useIdentity ? System.identityHashCode(o2) : o2.hashCode()) & Integer.MAX_VALUE) % this.initialHash.length;
    }

    public E pop() {
        this.size--;
        E e2 = (E) this.data[this.size];
        this.data[this.size] = null;
        int i2 = this.next[this.size];
        if (i2 >= 0) {
            int iHash = hash(e2);
            if (!$assertionsDisabled && this.initialHash[iHash] != this.size + 1) {
                throw new AssertionError();
            }
            this.initialHash[iHash] = i2;
        }
        return e2;
    }

    public E peek() {
        return (E) this.data[this.size - 1];
    }

    private boolean findDuplicate(E o2, int hash) {
        int i2 = this.initialHash[hash];
        while (true) {
            int p2 = i2;
            if (p2 != 0) {
                int p3 = p2 - 1;
                Object existing = this.data[p3];
                if (this.useIdentity) {
                    if (existing == o2) {
                        return true;
                    }
                } else if (o2.equals(existing)) {
                    return true;
                }
                i2 = this.next[p3];
            } else {
                return false;
            }
        }
    }

    private void expandCapacity() {
        int oldSize = this.data.length;
        int newSize = oldSize * 2;
        Object[] d2 = new Object[newSize];
        int[] n2 = new int[newSize];
        System.arraycopy(this.data, 0, d2, 0, oldSize);
        System.arraycopy(this.next, 0, n2, 0, oldSize);
        this.data = d2;
        this.next = n2;
    }

    public void reset() {
        if (this.size > 0) {
            this.size = 0;
            Arrays.fill(this.initialHash, 0);
        }
    }

    public String getCycleString() {
        Object x2;
        StringBuilder sb = new StringBuilder();
        int i2 = size() - 1;
        Object obj = get(i2);
        sb.append(obj);
        do {
            sb.append(" -> ");
            i2--;
            x2 = get(i2);
            sb.append(x2);
        } while (obj != x2);
        return sb.toString();
    }
}
