package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/SmallSet.class */
class SmallSet<E> extends AbstractSet<E> implements Iterator<E> {
    E e1;
    E e2;

    static final <T> Set<T> emptySet() {
        return new SmallSet(null, null);
    }

    SmallSet(E e2, E e3) {
        this.e1 = e2;
        this.e2 = e3;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new SmallSet(this.e1, this.e2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        if (this.e1 == null) {
            return 0;
        }
        return this.e2 == null ? 1 : 2;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.e1 != null;
    }

    @Override // java.util.Iterator
    public E next() {
        if (this.e1 == null) {
            throw new NoSuchElementException();
        }
        E e2 = this.e1;
        this.e1 = this.e2;
        this.e2 = null;
        return e2;
    }

    @Override // java.util.Iterator
    public void remove() {
    }

    Set<E> union(SmallSet<E> smallSet) {
        if ((smallSet.e1 == this.e1 && smallSet.e2 == this.e2) || (smallSet.e1 == this.e2 && smallSet.e2 == this.e1)) {
            return this;
        }
        if (smallSet.e1 == null) {
            return this;
        }
        if (this.e1 == null) {
            return smallSet;
        }
        if (smallSet.e2 == null) {
            if (this.e2 == null) {
                return new SmallSet(this.e1, smallSet.e1);
            }
            if (smallSet.e1 == this.e1 || smallSet.e1 == this.e2) {
                return this;
            }
        }
        if (this.e2 == null && (this.e1 == smallSet.e1 || this.e1 == smallSet.e2)) {
            return smallSet;
        }
        HashSet hashSet = new HashSet(4);
        hashSet.add(this.e1);
        if (this.e2 != null) {
            hashSet.add(this.e2);
        }
        hashSet.add(smallSet.e1);
        if (smallSet.e2 != null) {
            hashSet.add(smallSet.e2);
        }
        return hashSet;
    }
}
