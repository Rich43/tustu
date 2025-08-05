package com.sun.javafx.css;

import com.sun.javafx.collections.SetListenerHelper;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/css/BitSet.class */
abstract class BitSet<T> implements ObservableSet<T> {
    private static final long[] EMPTY_SET = new long[0];
    private long[] bits = EMPTY_SET;
    private SetListenerHelper<T> listenerHelper;

    protected abstract T getT(int i2);

    protected abstract int getIndex(T t2);

    protected abstract T cast(Object obj);

    protected BitSet() {
    }

    @Override // java.util.Set
    public int size() {
        int size = 0;
        if (this.bits.length > 0) {
            for (int n2 = 0; n2 < this.bits.length; n2++) {
                long mask = this.bits[n2];
                if (mask != 0) {
                    size += Long.bitCount(mask);
                }
            }
        }
        return size;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        if (this.bits.length > 0) {
            for (int n2 = 0; n2 < this.bits.length; n2++) {
                long mask = this.bits[n2];
                if (mask != 0) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<T> iterator() {
        return new Iterator<T>() { // from class: com.sun.javafx.css.BitSet.1
            int next = -1;
            int element = 0;
            int index = -1;

            @Override // java.util.Iterator
            public boolean hasNext() {
                boolean found;
                if (BitSet.this.bits == null || BitSet.this.bits.length == 0) {
                    return false;
                }
                do {
                    int i2 = this.next + 1;
                    this.next = i2;
                    if (i2 >= 64) {
                        int i3 = this.element + 1;
                        this.element = i3;
                        if (i3 < BitSet.this.bits.length) {
                            this.next = 0;
                        } else {
                            return false;
                        }
                    }
                    long bit = 1 << this.next;
                    found = (bit & BitSet.this.bits[this.element]) == bit;
                } while (!found);
                if (found) {
                    this.index = (64 * this.element) + this.next;
                }
                return found;
            }

            @Override // java.util.Iterator
            public T next() {
                try {
                    return (T) BitSet.this.getT(this.index);
                } catch (IndexOutOfBoundsException e2) {
                    throw new NoSuchElementException("[" + this.element + "][" + this.next + "]");
                }
            }

            @Override // java.util.Iterator
            public void remove() {
                try {
                    BitSet.this.remove(BitSet.this.getT(this.index));
                } catch (IndexOutOfBoundsException e2) {
                    throw new NoSuchElementException("[" + this.element + "][" + this.next + "]");
                }
            }
        };
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public boolean add(T t2) {
        if (t2 == null) {
            return false;
        }
        int element = getIndex(t2) / 64;
        long bit = 1 << (getIndex(t2) % 64);
        if (element >= this.bits.length) {
            long[] temp = new long[element + 1];
            System.arraycopy(this.bits, 0, temp, 0, this.bits.length);
            this.bits = temp;
        }
        long temp2 = this.bits[element];
        this.bits[element] = temp2 | bit;
        boolean modified = this.bits[element] != temp2;
        if (modified && SetListenerHelper.hasListeners(this.listenerHelper)) {
            notifyObservers(t2, false);
        }
        return modified;
    }

    @Override // java.util.Set
    public boolean remove(Object o2) {
        if (o2 == null) {
            return false;
        }
        T t2 = cast(o2);
        int element = getIndex(t2) / 64;
        long bit = 1 << (getIndex(t2) % 64);
        if (element >= this.bits.length) {
            return false;
        }
        long temp = this.bits[element];
        this.bits[element] = temp & (bit ^ (-1));
        boolean modified = this.bits[element] != temp;
        if (modified) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                notifyObservers(t2, true);
            }
            boolean isEmpty = true;
            for (int n2 = 0; n2 < this.bits.length && isEmpty; n2++) {
                isEmpty &= this.bits[n2] == 0;
            }
            if (isEmpty) {
                this.bits = EMPTY_SET;
            }
        }
        return modified;
    }

    @Override // java.util.Set
    public boolean contains(Object o2) {
        if (o2 == null) {
            return false;
        }
        T t2 = cast(o2);
        int element = getIndex(t2) / 64;
        long bit = 1 << (getIndex(t2) % 64);
        return element < this.bits.length && (this.bits[element] & bit) == bit;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> c2) {
        if (c2 == null || getClass() != c2.getClass()) {
            return false;
        }
        BitSet other = (BitSet) c2;
        if (this.bits.length == 0 && other.bits.length == 0) {
            return true;
        }
        if (this.bits.length < other.bits.length) {
            return false;
        }
        int max = other.bits.length;
        for (int n2 = 0; n2 < max; n2++) {
            if ((this.bits[n2] & other.bits[n2]) != other.bits[n2]) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends T> c2) {
        long bitsAdded;
        boolean z2;
        if (c2 == null || getClass() != c2.getClass()) {
            return false;
        }
        boolean modified = false;
        BitSet other = (BitSet) c2;
        long[] maskOne = this.bits;
        long[] maskTwo = other.bits;
        int a2 = maskOne.length;
        int b2 = maskTwo.length;
        int max = a2 < b2 ? b2 : a2;
        long[] union = max > 0 ? new long[max] : EMPTY_SET;
        for (int n2 = 0; n2 < max; n2++) {
            if (n2 < maskOne.length && n2 < maskTwo.length) {
                union[n2] = maskOne[n2] | maskTwo[n2];
                z2 = modified | (union[n2] != maskOne[n2]);
            } else if (n2 < maskOne.length) {
                union[n2] = maskOne[n2];
                z2 = modified | false;
            } else {
                union[n2] = maskTwo[n2];
                z2 = true;
            }
            modified = z2;
        }
        if (modified) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                for (int n3 = 0; n3 < max; n3++) {
                    if (n3 < maskOne.length && n3 < maskTwo.length) {
                        bitsAdded = (maskOne[n3] ^ (-1)) & maskTwo[n3];
                    } else if (n3 >= maskOne.length) {
                        bitsAdded = maskTwo[n3];
                    }
                    for (int bit = 0; bit < 64; bit++) {
                        long m2 = 1 << bit;
                        if ((m2 & bitsAdded) == m2) {
                            T t2 = getT((n3 * 64) + bit);
                            notifyObservers(t2, false);
                        }
                    }
                }
            }
            this.bits = union;
        }
        return modified;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> c2) {
        long bitsRemoved;
        if (c2 == null || getClass() != c2.getClass()) {
            clear();
            return true;
        }
        BitSet other = (BitSet) c2;
        long[] maskOne = this.bits;
        long[] maskTwo = other.bits;
        int a2 = maskOne.length;
        int b2 = maskTwo.length;
        int max = a2 < b2 ? a2 : b2;
        long[] intersection = max > 0 ? new long[max] : EMPTY_SET;
        boolean modified = false | (maskOne.length > max);
        boolean isEmpty = true;
        for (int n2 = 0; n2 < max; n2++) {
            intersection[n2] = maskOne[n2] & maskTwo[n2];
            modified |= intersection[n2] != maskOne[n2];
            isEmpty &= intersection[n2] == 0;
        }
        if (modified) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                for (int n3 = 0; n3 < maskOne.length; n3++) {
                    if (n3 < maskTwo.length) {
                        bitsRemoved = maskOne[n3] & (maskTwo[n3] ^ (-1));
                    } else {
                        bitsRemoved = maskOne[n3];
                    }
                    for (int bit = 0; bit < 64; bit++) {
                        long m2 = 1 << bit;
                        if ((m2 & bitsRemoved) == m2) {
                            T t2 = getT((n3 * 64) + bit);
                            notifyObservers(t2, true);
                        }
                    }
                }
            }
            this.bits = !isEmpty ? intersection : EMPTY_SET;
        }
        return modified;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> c2) {
        if (c2 == null || getClass() != c2.getClass()) {
            return false;
        }
        boolean modified = false;
        BitSet other = (BitSet) c2;
        long[] maskOne = this.bits;
        long[] maskTwo = other.bits;
        int a2 = maskOne.length;
        int b2 = maskTwo.length;
        int max = a2 < b2 ? a2 : b2;
        long[] difference = max > 0 ? new long[max] : EMPTY_SET;
        boolean isEmpty = true;
        for (int n2 = 0; n2 < max; n2++) {
            difference[n2] = maskOne[n2] & (maskTwo[n2] ^ (-1));
            modified |= difference[n2] != maskOne[n2];
            isEmpty &= difference[n2] == 0;
        }
        if (modified) {
            if (SetListenerHelper.hasListeners(this.listenerHelper)) {
                for (int n3 = 0; n3 < max; n3++) {
                    long bitsRemoved = maskOne[n3] & maskTwo[n3];
                    for (int bit = 0; bit < 64; bit++) {
                        long m2 = 1 << bit;
                        if ((m2 & bitsRemoved) == m2) {
                            T t2 = getT((n3 * 64) + bit);
                            notifyObservers(t2, true);
                        }
                    }
                }
            }
            this.bits = !isEmpty ? difference : EMPTY_SET;
        }
        return modified;
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public void clear() {
        for (int n2 = 0; n2 < this.bits.length; n2++) {
            long bitsRemoved = this.bits[n2];
            for (int b2 = 0; b2 < 64; b2++) {
                long m2 = 1 << b2;
                if ((m2 & bitsRemoved) == m2) {
                    T t2 = getT((n2 * 64) + b2);
                    notifyObservers(t2, true);
                }
            }
        }
        this.bits = EMPTY_SET;
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public int hashCode() {
        int hash = 7;
        if (this.bits.length > 0) {
            for (int n2 = 0; n2 < this.bits.length; n2++) {
                long mask = this.bits[n2];
                hash = (71 * hash) + ((int) (mask ^ (mask >>> 32)));
            }
        }
        return hash;
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BitSet other = (BitSet) obj;
        int a2 = this.bits != null ? this.bits.length : 0;
        int b2 = other.bits != null ? other.bits.length : 0;
        if (a2 != b2) {
            return false;
        }
        for (int m2 = 0; m2 < a2; m2++) {
            long m0 = this.bits[m2];
            long m1 = other.bits[m2];
            if (m0 != m1) {
                return false;
            }
        }
        return true;
    }

    protected long[] getBits() {
        return this.bits;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/BitSet$Change.class */
    private class Change extends SetChangeListener.Change<T> {
        private static final boolean ELEMENT_ADDED = false;
        private static final boolean ELEMENT_REMOVED = true;
        private final T element;
        private final boolean removed;

        public Change(T element, boolean removed) {
            super(FXCollections.unmodifiableObservableSet(BitSet.this));
            this.element = element;
            this.removed = removed;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasAdded() {
            return !this.removed;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasRemoved() {
            return this.removed;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public T getElementAdded() {
            if (this.removed) {
                return null;
            }
            return this.element;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public T getElementRemoved() {
            if (this.removed) {
                return this.element;
            }
            return null;
        }
    }

    @Override // javafx.collections.ObservableSet
    public void addListener(SetChangeListener<? super T> setChangeListener) {
        if (setChangeListener != null) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, setChangeListener);
        }
    }

    @Override // javafx.collections.ObservableSet
    public void removeListener(SetChangeListener<? super T> setChangeListener) {
        if (setChangeListener != null) {
            SetListenerHelper.removeListener(this.listenerHelper, setChangeListener);
        }
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener invalidationListener) {
        if (invalidationListener != null) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, invalidationListener);
        }
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener invalidationListener) {
        if (invalidationListener != null) {
            SetListenerHelper.removeListener(this.listenerHelper, invalidationListener);
        }
    }

    private void notifyObservers(T element, boolean removed) {
        if (element != null && SetListenerHelper.hasListeners(this.listenerHelper)) {
            BitSet<T>.Change change = new Change(element, removed);
            SetListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
        }
    }
}
