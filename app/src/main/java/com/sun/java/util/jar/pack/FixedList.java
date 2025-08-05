package com.sun.java.util.jar.pack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/FixedList.class */
final class FixedList<E> implements List<E> {
    private final ArrayList<E> flist;

    protected FixedList(int i2) {
        this.flist = new ArrayList<>(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            this.flist.add(null);
        }
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public int size() {
        return this.flist.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.flist.isEmpty();
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.flist.contains(obj);
    }

    @Override // java.util.List
    public Iterator<E> iterator() {
        return this.flist.iterator();
    }

    @Override // java.util.List
    public Object[] toArray() {
        return this.flist.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.flist.toArray(tArr);
    }

    @Override // java.util.List
    public boolean add(E e2) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean remove(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return this.flist.containsAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> collection) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> collection) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List
    public void clear() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List
    public E get(int i2) {
        return this.flist.get(i2);
    }

    @Override // java.util.List
    public E set(int i2, E e2) {
        return this.flist.set(i2, e2);
    }

    @Override // java.util.List
    public void add(int i2, E e2) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List
    public E remove(int i2) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        return this.flist.indexOf(obj);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        return this.flist.lastIndexOf(obj);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return this.flist.listIterator();
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int i2) {
        return this.flist.listIterator(i2);
    }

    @Override // java.util.List
    public List<E> subList(int i2, int i3) {
        return this.flist.subList(i2, i3);
    }

    public String toString() {
        return "FixedList{plist=" + ((Object) this.flist) + '}';
    }
}
