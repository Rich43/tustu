package com.sun.javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.WeakListChangeListener;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ElementObservableListDecorator.class */
public final class ElementObservableListDecorator<E> extends ObservableListBase<E> implements ObservableList<E> {
    private final ObservableList<E> decoratedList;
    private final ListChangeListener<E> listener;
    private ElementObserver<E> observer;

    public ElementObservableListDecorator(ObservableList<E> observableList, Callback<E, Observable[]> callback) {
        this.observer = new ElementObserver<>(callback, new Callback<E, InvalidationListener>() { // from class: com.sun.javafx.collections.ElementObservableListDecorator.1
            @Override // javafx.util.Callback
            public /* bridge */ /* synthetic */ InvalidationListener call(Object obj) {
                return call((AnonymousClass1) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.util.Callback
            public InvalidationListener call(final E e2) {
                return new InvalidationListener() { // from class: com.sun.javafx.collections.ElementObservableListDecorator.1.1
                    @Override // javafx.beans.InvalidationListener
                    public void invalidated(Observable observable) {
                        ElementObservableListDecorator.this.beginChange();
                        int i2 = 0;
                        if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                            int size = ElementObservableListDecorator.this.size();
                            while (i2 < size) {
                                if (ElementObservableListDecorator.this.get(i2) == e2) {
                                    ElementObservableListDecorator.this.nextUpdate(i2);
                                }
                                i2++;
                            }
                        } else {
                            Iterator<E> it = ElementObservableListDecorator.this.iterator();
                            while (it.hasNext()) {
                                if (it.next() == e2) {
                                    ElementObservableListDecorator.this.nextUpdate(i2);
                                }
                                i2++;
                            }
                        }
                        ElementObservableListDecorator.this.endChange();
                    }
                };
            }
        }, this);
        this.decoratedList = observableList;
        int size = this.decoratedList.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.observer.attachListener(this.decoratedList.get(i2));
        }
        this.listener = new ListChangeListener<E>() { // from class: com.sun.javafx.collections.ElementObservableListDecorator.2
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends E> c2) {
                while (c2.next()) {
                    if (c2.wasAdded() || c2.wasRemoved()) {
                        int removedSize = c2.getRemovedSize();
                        List<? extends E> removed = c2.getRemoved();
                        for (int i3 = 0; i3 < removedSize; i3++) {
                            ElementObservableListDecorator.this.observer.detachListener(removed.get(i3));
                        }
                        if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                            int to = c2.getTo();
                            for (int i4 = c2.getFrom(); i4 < to; i4++) {
                                ElementObservableListDecorator.this.observer.attachListener(ElementObservableListDecorator.this.decoratedList.get(i4));
                            }
                        } else {
                            for (E e2 : c2.getAddedSubList()) {
                                ElementObservableListDecorator.this.observer.attachListener(e2);
                            }
                        }
                    }
                }
                c2.reset();
                ElementObservableListDecorator.this.fireChange(c2);
            }
        };
        this.decoratedList.addListener(new WeakListChangeListener(this.listener));
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.decoratedList.toArray(tArr);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return this.decoratedList.toArray();
    }

    @Override // java.util.AbstractList, java.util.List
    public List<E> subList(int fromIndex, int toIndex) {
        return this.decoratedList.subList(fromIndex, toIndex);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.decoratedList.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int index, E element) {
        return this.decoratedList.set(index, element);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> c2) {
        return this.decoratedList.retainAll(c2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> c2) {
        return this.decoratedList.removeAll(c2);
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int index) {
        return this.decoratedList.remove(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o2) {
        return this.decoratedList.remove(o2);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int index) {
        return this.decoratedList.listIterator(index);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator() {
        return this.decoratedList.listIterator();
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object o2) {
        return this.decoratedList.lastIndexOf(o2);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return this.decoratedList.iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.decoratedList.isEmpty();
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object o2) {
        return this.decoratedList.indexOf(o2);
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        return this.decoratedList.get(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> c2) {
        return this.decoratedList.containsAll(c2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o2) {
        return this.decoratedList.contains(o2);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.decoratedList.clear();
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int index, Collection<? extends E> c2) {
        return this.decoratedList.addAll(index, c2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> c2) {
        return this.decoratedList.addAll(c2);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, E element) {
        this.decoratedList.add(index, element);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return this.decoratedList.add(e2);
    }

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public boolean setAll(Collection<? extends E> col) {
        return this.decoratedList.setAll(col);
    }

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public boolean setAll(E... elements) {
        return this.decoratedList.setAll(elements);
    }

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public boolean retainAll(E... elements) {
        return this.decoratedList.retainAll(elements);
    }

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public boolean removeAll(E... elements) {
        return this.decoratedList.removeAll(elements);
    }

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public void remove(int from, int to) {
        this.decoratedList.remove(from, to);
    }

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public boolean addAll(E... elements) {
        return this.decoratedList.addAll(elements);
    }
}
