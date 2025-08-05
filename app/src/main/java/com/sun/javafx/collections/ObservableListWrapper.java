package com.sun.javafx.collections;

import com.sun.javafx.collections.NonIterableChange;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableListWrapper.class */
public class ObservableListWrapper<E> extends ModifiableObservableListBase<E> implements ObservableList<E>, SortableList<E>, RandomAccess {
    private final List<E> backingList;
    private final ElementObserver elementObserver;
    private SortHelper helper;

    public ObservableListWrapper(List<E> list) {
        this.backingList = list;
        this.elementObserver = null;
    }

    public ObservableListWrapper(List<E> list, Callback<E, Observable[]> extractor) {
        this.backingList = list;
        this.elementObserver = new ElementObserver(extractor, new Callback<E, InvalidationListener>() { // from class: com.sun.javafx.collections.ObservableListWrapper.1
            @Override // javafx.util.Callback
            public /* bridge */ /* synthetic */ InvalidationListener call(Object obj) {
                return call((AnonymousClass1) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.util.Callback
            public InvalidationListener call(final E e2) {
                return new InvalidationListener() { // from class: com.sun.javafx.collections.ObservableListWrapper.1.1
                    @Override // javafx.beans.InvalidationListener
                    public void invalidated(Observable observable) {
                        ObservableListWrapper.this.beginChange();
                        int size = ObservableListWrapper.this.size();
                        for (int i2 = 0; i2 < size; i2++) {
                            if (ObservableListWrapper.this.get(i2) == e2) {
                                ObservableListWrapper.this.nextUpdate(i2);
                            }
                        }
                        ObservableListWrapper.this.endChange();
                    }
                };
            }
        }, this);
        int sz = this.backingList.size();
        for (int i2 = 0; i2 < sz; i2++) {
            this.elementObserver.attachListener(this.backingList.get(i2));
        }
    }

    @Override // javafx.collections.ModifiableObservableListBase, java.util.AbstractList, java.util.List
    public E get(int index) {
        return this.backingList.get(index);
    }

    @Override // javafx.collections.ModifiableObservableListBase, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.backingList.size();
    }

    @Override // javafx.collections.ModifiableObservableListBase
    protected void doAdd(int index, E element) {
        if (this.elementObserver != null) {
            this.elementObserver.attachListener(element);
        }
        this.backingList.add(index, element);
    }

    @Override // javafx.collections.ModifiableObservableListBase
    protected E doSet(int index, E element) {
        E removed = this.backingList.set(index, element);
        if (this.elementObserver != null) {
            this.elementObserver.detachListener(removed);
            this.elementObserver.attachListener(element);
        }
        return removed;
    }

    @Override // javafx.collections.ModifiableObservableListBase
    protected E doRemove(int index) {
        E removed = this.backingList.remove(index);
        if (this.elementObserver != null) {
            this.elementObserver.detachListener(removed);
        }
        return removed;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object o2) {
        return this.backingList.indexOf(o2);
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object o2) {
        return this.backingList.lastIndexOf(o2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o2) {
        return this.backingList.contains(o2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> c2) {
        return this.backingList.containsAll(c2);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        if (this.elementObserver != null) {
            int sz = size();
            for (int i2 = 0; i2 < sz; i2++) {
                this.elementObserver.detachListener(get(i2));
            }
        }
        if (hasListeners()) {
            beginChange();
            nextRemove(0, (List) this);
        }
        this.backingList.clear();
        this.modCount++;
        if (hasListeners()) {
            endChange();
        }
    }

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public void remove(int fromIndex, int toIndex) {
        beginChange();
        for (int i2 = fromIndex; i2 < toIndex; i2++) {
            remove(fromIndex);
        }
        endChange();
    }

    @Override // javafx.collections.ModifiableObservableListBase, java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> c2) {
        beginChange();
        BitSet bs2 = new BitSet(c2.size());
        for (int i2 = 0; i2 < size(); i2++) {
            if (c2.contains(get(i2))) {
                bs2.set(i2);
            }
        }
        if (!bs2.isEmpty()) {
            int cur = size();
            while (true) {
                int iPreviousSetBit = bs2.previousSetBit(cur - 1);
                cur = iPreviousSetBit;
                if (iPreviousSetBit < 0) {
                    break;
                }
                remove(cur);
            }
        }
        endChange();
        return !bs2.isEmpty();
    }

    @Override // javafx.collections.ModifiableObservableListBase, java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> c2) {
        beginChange();
        BitSet bs2 = new BitSet(c2.size());
        for (int i2 = 0; i2 < size(); i2++) {
            if (!c2.contains(get(i2))) {
                bs2.set(i2);
            }
        }
        if (!bs2.isEmpty()) {
            int cur = size();
            while (true) {
                int iPreviousSetBit = bs2.previousSetBit(cur - 1);
                cur = iPreviousSetBit;
                if (iPreviousSetBit < 0) {
                    break;
                }
                remove(cur);
            }
        }
        endChange();
        return !bs2.isEmpty();
    }

    @Override // com.sun.javafx.collections.SortableList
    public void sort() {
        if (this.backingList.isEmpty()) {
            return;
        }
        int[] perm = getSortHelper().sort(this.backingList);
        fireChange(new NonIterableChange.SimplePermutationChange(0, size(), perm, this));
    }

    @Override // java.util.List, com.sun.javafx.collections.SortableList
    public void sort(Comparator<? super E> comparator) {
        if (this.backingList.isEmpty()) {
            return;
        }
        int[] perm = getSortHelper().sort(this.backingList, comparator);
        fireChange(new NonIterableChange.SimplePermutationChange(0, size(), perm, this));
    }

    private SortHelper getSortHelper() {
        if (this.helper == null) {
            this.helper = new SortHelper();
        }
        return this.helper;
    }
}
