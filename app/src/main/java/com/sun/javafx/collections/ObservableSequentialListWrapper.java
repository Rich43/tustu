package com.sun.javafx.collections;

import com.sun.javafx.collections.NonIterableChange;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableSequentialListWrapper.class */
public final class ObservableSequentialListWrapper<E> extends ModifiableObservableListBase<E> implements ObservableList<E>, SortableList<E> {
    private final List<E> backingList;
    private final ElementObserver elementObserver;
    private SortHelper helper;

    public ObservableSequentialListWrapper(List<E> list) {
        this.backingList = list;
        this.elementObserver = null;
    }

    public ObservableSequentialListWrapper(List<E> list, Callback<E, Observable[]> extractor) {
        this.backingList = list;
        this.elementObserver = new ElementObserver(extractor, new Callback<E, InvalidationListener>() { // from class: com.sun.javafx.collections.ObservableSequentialListWrapper.1
            @Override // javafx.util.Callback
            public /* bridge */ /* synthetic */ InvalidationListener call(Object obj) {
                return call((AnonymousClass1) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.util.Callback
            public InvalidationListener call(final E e2) {
                return new InvalidationListener() { // from class: com.sun.javafx.collections.ObservableSequentialListWrapper.1.1
                    @Override // javafx.beans.InvalidationListener
                    public void invalidated(Observable observable) {
                        ObservableSequentialListWrapper.this.beginChange();
                        int i2 = 0;
                        Iterator<E> it = ObservableSequentialListWrapper.this.backingList.iterator();
                        while (it.hasNext()) {
                            if (it.next() == e2) {
                                ObservableSequentialListWrapper.this.nextUpdate(i2);
                            }
                            i2++;
                        }
                        ObservableSequentialListWrapper.this.endChange();
                    }
                };
            }
        }, this);
        for (E e2 : this.backingList) {
            this.elementObserver.attachListener(e2);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o2) {
        return this.backingList.contains(o2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> c2) {
        return this.backingList.containsAll(c2);
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object o2) {
        return this.backingList.indexOf(o2);
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object o2) {
        return this.backingList.lastIndexOf(o2);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(final int index) {
        return new ListIterator<E>() { // from class: com.sun.javafx.collections.ObservableSequentialListWrapper.2
            private final ListIterator<E> backingIt;
            private E lastReturned;

            {
                this.backingIt = ObservableSequentialListWrapper.this.backingList.listIterator(index);
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public boolean hasNext() {
                return this.backingIt.hasNext();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public E next() {
                E next = this.backingIt.next();
                this.lastReturned = next;
                return next;
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return this.backingIt.hasPrevious();
            }

            @Override // java.util.ListIterator
            public E previous() {
                E ePrevious = this.backingIt.previous();
                this.lastReturned = ePrevious;
                return ePrevious;
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.backingIt.nextIndex();
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.backingIt.previousIndex();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public void remove() {
                ObservableSequentialListWrapper.this.beginChange();
                int idx = previousIndex();
                this.backingIt.remove();
                ObservableSequentialListWrapper.this.nextRemove(idx, (int) this.lastReturned);
                ObservableSequentialListWrapper.this.endChange();
            }

            @Override // java.util.ListIterator
            public void set(E e2) {
                ObservableSequentialListWrapper.this.beginChange();
                int idx = previousIndex();
                this.backingIt.set(e2);
                ObservableSequentialListWrapper.this.nextSet(idx, this.lastReturned);
                ObservableSequentialListWrapper.this.endChange();
            }

            @Override // java.util.ListIterator
            public void add(E e2) {
                ObservableSequentialListWrapper.this.beginChange();
                int idx = nextIndex();
                this.backingIt.add(e2);
                ObservableSequentialListWrapper.this.nextAdd(idx, idx + 1);
                ObservableSequentialListWrapper.this.endChange();
            }
        };
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override // javafx.collections.ModifiableObservableListBase, java.util.AbstractList, java.util.List
    public E get(int index) {
        try {
            return this.backingList.listIterator(index).next();
        } catch (NoSuchElementException e2) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    @Override // javafx.collections.ModifiableObservableListBase, java.util.AbstractList, java.util.List
    public boolean addAll(int index, Collection<? extends E> c2) {
        try {
            beginChange();
            boolean modified = false;
            ListIterator<E> e1 = listIterator(index);
            Iterator<? extends E> e2 = c2.iterator();
            while (e2.hasNext()) {
                e1.add(e2.next());
                modified = true;
            }
            endChange();
            return modified;
        } catch (NoSuchElementException e3) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    @Override // javafx.collections.ModifiableObservableListBase, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.backingList.size();
    }

    @Override // javafx.collections.ModifiableObservableListBase
    protected void doAdd(int index, E element) {
        try {
            this.backingList.listIterator(index).add(element);
        } catch (NoSuchElementException e2) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    @Override // javafx.collections.ModifiableObservableListBase
    protected E doSet(int index, E element) {
        try {
            ListIterator<E> e2 = this.backingList.listIterator(index);
            E oldVal = e2.next();
            e2.set(element);
            return oldVal;
        } catch (NoSuchElementException e3) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    @Override // javafx.collections.ModifiableObservableListBase
    protected E doRemove(int index) {
        try {
            ListIterator<E> e2 = this.backingList.listIterator(index);
            E outCast = e2.next();
            e2.remove();
            return outCast;
        } catch (NoSuchElementException e3) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
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
