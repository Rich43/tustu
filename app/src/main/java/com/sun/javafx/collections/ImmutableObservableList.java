package com.sun.javafx.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ImmutableObservableList.class */
public class ImmutableObservableList<E> extends AbstractList<E> implements ObservableList<E> {
    private final E[] elements;

    public ImmutableObservableList(E... eArr) {
        this.elements = (eArr == null || eArr.length == 0) ? null : (E[]) Arrays.copyOf(eArr, eArr.length);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
    }

    @Override // javafx.collections.ObservableList
    public void addListener(ListChangeListener<? super E> listener) {
    }

    @Override // javafx.collections.ObservableList
    public void removeListener(ListChangeListener<? super E> listener) {
    }

    @Override // javafx.collections.ObservableList
    public boolean addAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(Collection<? extends E> col) {
        throw new UnsupportedOperationException();
    }

    @Override // javafx.collections.ObservableList
    public boolean removeAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override // javafx.collections.ObservableList
    public boolean retainAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override // javafx.collections.ObservableList
    public void remove(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return this.elements[index];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        if (this.elements == null) {
            return 0;
        }
        return this.elements.length;
    }
}
