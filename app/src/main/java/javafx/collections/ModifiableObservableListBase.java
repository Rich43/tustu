package javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* loaded from: jfxrt.jar:javafx/collections/ModifiableObservableListBase.class */
public abstract class ModifiableObservableListBase<E> extends ObservableListBase<E> {
    @Override // java.util.AbstractList, java.util.List
    public abstract E get(int i2);

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public abstract int size();

    protected abstract void doAdd(int i2, E e2);

    protected abstract E doSet(int i2, E e2);

    protected abstract E doRemove(int i2);

    @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
    public boolean setAll(Collection<? extends E> col) {
        beginChange();
        try {
            clear();
            addAll(col);
            return true;
        } finally {
            endChange();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> c2) {
        beginChange();
        try {
            boolean res = super.addAll(c2);
            endChange();
            return res;
        } catch (Throwable th) {
            endChange();
            throw th;
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int index, Collection<? extends E> c2) {
        beginChange();
        try {
            boolean res = super.addAll(index, c2);
            endChange();
            return res;
        } catch (Throwable th) {
            endChange();
            throw th;
        }
    }

    @Override // java.util.AbstractList
    protected void removeRange(int fromIndex, int toIndex) {
        beginChange();
        try {
            super.removeRange(fromIndex, toIndex);
        } finally {
            endChange();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> c2) {
        beginChange();
        try {
            boolean res = super.removeAll(c2);
            endChange();
            return res;
        } catch (Throwable th) {
            endChange();
            throw th;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> c2) {
        beginChange();
        try {
            boolean res = super.retainAll(c2);
            endChange();
            return res;
        } catch (Throwable th) {
            endChange();
            throw th;
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, E element) {
        doAdd(index, element);
        beginChange();
        nextAdd(index, index + 1);
        this.modCount++;
        endChange();
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int index, E element) {
        E old = doSet(index, element);
        beginChange();
        nextSet(index, old);
        endChange();
        return old;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o2) {
        int i2 = indexOf(o2);
        if (i2 != -1) {
            remove(i2);
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int index) {
        E old = doRemove(index);
        beginChange();
        nextRemove(index, (int) old);
        this.modCount++;
        endChange();
        return old;
    }

    @Override // java.util.AbstractList, java.util.List
    public List<E> subList(int fromIndex, int toIndex) {
        return new SubObservableList(super.subList(fromIndex, toIndex));
    }

    /* loaded from: jfxrt.jar:javafx/collections/ModifiableObservableListBase$SubObservableList.class */
    private class SubObservableList implements List<E> {
        private List<E> sublist;

        public SubObservableList(List<E> sublist) {
            this.sublist = sublist;
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public int size() {
            return this.sublist.size();
        }

        @Override // java.util.List, java.util.Collection
        public boolean isEmpty() {
            return this.sublist.isEmpty();
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            return this.sublist.contains(o2);
        }

        @Override // java.util.List
        public Iterator<E> iterator() {
            return this.sublist.iterator();
        }

        @Override // java.util.List
        public Object[] toArray() {
            return this.sublist.toArray();
        }

        @Override // java.util.List, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.sublist.toArray(tArr);
        }

        @Override // java.util.List
        public boolean add(E e2) {
            return this.sublist.add(e2);
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            return this.sublist.remove(o2);
        }

        @Override // java.util.List, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            return this.sublist.containsAll(c2);
        }

        @Override // java.util.List, java.util.Collection
        public boolean addAll(Collection<? extends E> c2) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean res = this.sublist.addAll(c2);
                ModifiableObservableListBase.this.endChange();
                return res;
            } catch (Throwable th) {
                ModifiableObservableListBase.this.endChange();
                throw th;
            }
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends E> c2) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean res = this.sublist.addAll(index, c2);
                ModifiableObservableListBase.this.endChange();
                return res;
            } catch (Throwable th) {
                ModifiableObservableListBase.this.endChange();
                throw th;
            }
        }

        @Override // java.util.List, java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean res = this.sublist.removeAll(c2);
                ModifiableObservableListBase.this.endChange();
                return res;
            } catch (Throwable th) {
                ModifiableObservableListBase.this.endChange();
                throw th;
            }
        }

        @Override // java.util.List, java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            ModifiableObservableListBase.this.beginChange();
            try {
                boolean res = this.sublist.retainAll(c2);
                ModifiableObservableListBase.this.endChange();
                return res;
            } catch (Throwable th) {
                ModifiableObservableListBase.this.endChange();
                throw th;
            }
        }

        @Override // java.util.List
        public void clear() {
            ModifiableObservableListBase.this.beginChange();
            try {
                this.sublist.clear();
            } finally {
                ModifiableObservableListBase.this.endChange();
            }
        }

        @Override // java.util.List
        public E get(int index) {
            return this.sublist.get(index);
        }

        @Override // java.util.List
        public E set(int index, E element) {
            return this.sublist.set(index, element);
        }

        @Override // java.util.List
        public void add(int index, E element) {
            this.sublist.add(index, element);
        }

        @Override // java.util.List
        public E remove(int index) {
            return this.sublist.remove(index);
        }

        @Override // java.util.List
        public int indexOf(Object o2) {
            return this.sublist.indexOf(o2);
        }

        @Override // java.util.List
        public int lastIndexOf(Object o2) {
            return this.sublist.lastIndexOf(o2);
        }

        @Override // java.util.List
        public ListIterator<E> listIterator() {
            return this.sublist.listIterator();
        }

        @Override // java.util.List
        public ListIterator<E> listIterator(int index) {
            return this.sublist.listIterator(index);
        }

        @Override // java.util.List
        public List<E> subList(int fromIndex, int toIndex) {
            return new SubObservableList(this.sublist.subList(fromIndex, toIndex));
        }

        @Override // java.util.List
        public boolean equals(Object obj) {
            return this.sublist.equals(obj);
        }

        @Override // java.util.List
        public int hashCode() {
            return this.sublist.hashCode();
        }

        public String toString() {
            return this.sublist.toString();
        }
    }
}
