package com.sun.javafx.scene.control;

import com.sun.javafx.collections.ListListenerHelper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/ReadOnlyUnbackedObservableList.class */
public abstract class ReadOnlyUnbackedObservableList<E> implements ObservableList<E> {
    private ListListenerHelper<E> listenerHelper;

    @Override // java.util.List
    public abstract E get(int i2);

    @Override // java.util.List, java.util.Collection, java.util.Set
    public abstract int size();

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, listener);
    }

    @Override // javafx.collections.ObservableList
    public void addListener(ListChangeListener<? super E> obs) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, obs);
    }

    @Override // javafx.collections.ObservableList
    public void removeListener(ListChangeListener<? super E> obs) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, obs);
    }

    public void callObservers(ListChangeListener.Change<E> c2) {
        ListListenerHelper.fireValueChangedEvent(this.listenerHelper, c2);
    }

    @Override // java.util.List
    public int indexOf(Object o2) {
        if (o2 == null) {
            return -1;
        }
        for (int i2 = 0; i2 < size(); i2++) {
            Object obj = get(i2);
            if (o2.equals(obj)) {
                return i2;
            }
        }
        return -1;
    }

    @Override // java.util.List
    public int lastIndexOf(Object o2) {
        if (o2 == null) {
            return -1;
        }
        for (int i2 = size() - 1; i2 >= 0; i2--) {
            Object obj = get(i2);
            if (o2.equals(obj)) {
                return i2;
            }
        }
        return -1;
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean contains(Object o2) {
        return indexOf(o2) != -1;
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> c2) {
        for (Object o2 : c2) {
            if (!contains(o2)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return new SelectionListIterator(this);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int index) {
        return new SelectionListIterator(this, index);
    }

    @Override // java.util.List
    public Iterator<E> iterator() {
        return new SelectionListIterator(this);
    }

    @Override // java.util.List
    public List<E> subList(final int fromIndex, final int toIndex) {
        if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        return new ReadOnlyUnbackedObservableList<E>() { // from class: com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList.1
            @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
            public E get(int i2) {
                return (E) this.get(i2 + fromIndex);
            }

            @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
            public int size() {
                return toIndex - fromIndex;
            }
        };
    }

    @Override // java.util.List
    public Object[] toArray() {
        Object[] arr = new Object[size()];
        for (int i2 = 0; i2 < size(); i2++) {
            arr[i2] = get(i2);
        }
        return arr;
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        Object[] array = toArray();
        int length = array.length;
        if (tArr.length < length) {
            return (T[]) Arrays.copyOf(array, length, tArr.getClass());
        }
        System.arraycopy(array, 0, tArr, 0, length);
        if (tArr.length > length) {
            tArr[length] = null;
        }
        return tArr;
    }

    public String toString() {
        Iterator<E> i2 = iterator();
        if (!i2.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (true) {
            E e2 = i2.next();
            sb.append(e2 == this ? "(this Collection)" : e2);
            if (!i2.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(", ");
        }
    }

    @Override // java.util.List
    public boolean add(E e2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> c2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends E> c2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // javafx.collections.ObservableList
    public boolean addAll(E... elements) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(Collection<? extends E> col) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(E... elements) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List
    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List
    public E remove(int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean remove(Object o2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> c2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> c2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // javafx.collections.ObservableList
    public void remove(int from, int to) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // javafx.collections.ObservableList
    public boolean removeAll(E... elements) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // javafx.collections.ObservableList
    public boolean retainAll(E... elements) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/ReadOnlyUnbackedObservableList$SelectionListIterator.class */
    private static class SelectionListIterator<E> implements ListIterator<E> {
        private int pos;
        private final ReadOnlyUnbackedObservableList<E> list;

        public SelectionListIterator(ReadOnlyUnbackedObservableList<E> list) {
            this(list, 0);
        }

        public SelectionListIterator(ReadOnlyUnbackedObservableList<E> list, int pos) {
            this.list = list;
            this.pos = pos;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.pos < this.list.size();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            ReadOnlyUnbackedObservableList<E> readOnlyUnbackedObservableList = this.list;
            int i2 = this.pos;
            this.pos = i2 + 1;
            return readOnlyUnbackedObservableList.get(i2);
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.pos > 0;
        }

        @Override // java.util.ListIterator
        public E previous() {
            ReadOnlyUnbackedObservableList<E> readOnlyUnbackedObservableList = this.list;
            int i2 = this.pos;
            this.pos = i2 - 1;
            return readOnlyUnbackedObservableList.get(i2);
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.pos + 1;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.pos - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
}
