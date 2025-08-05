package java.util;

/* loaded from: rt.jar:java/util/AbstractList.class */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    protected transient int modCount = 0;

    public abstract E get(int i2);

    protected AbstractList() {
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        add(size(), e2);
        return true;
    }

    public E set(int i2, E e2) {
        throw new UnsupportedOperationException();
    }

    public void add(int i2, E e2) {
        throw new UnsupportedOperationException();
    }

    public E remove(int i2) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object obj) {
        ListIterator<E> listIterator = listIterator();
        if (obj == null) {
            while (listIterator.hasNext()) {
                if (listIterator.next() == null) {
                    return listIterator.previousIndex();
                }
            }
            return -1;
        }
        while (listIterator.hasNext()) {
            if (obj.equals(listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        ListIterator<E> listIterator = listIterator(size());
        if (obj == null) {
            while (listIterator.hasPrevious()) {
                if (listIterator.previous() == null) {
                    return listIterator.nextIndex();
                }
            }
            return -1;
        }
        while (listIterator.hasPrevious()) {
            if (obj.equals(listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        removeRange(0, size());
    }

    public boolean addAll(int i2, Collection<? extends E> collection) {
        rangeCheckForAdd(i2);
        boolean z2 = false;
        Iterator<? extends E> it = collection.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            add(i3, it.next());
            z2 = true;
        }
        return z2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(int i2) {
        rangeCheckForAdd(i2);
        return new ListItr(i2);
    }

    /* loaded from: rt.jar:java/util/AbstractList$Itr.class */
    private class Itr implements Iterator<E> {
        int cursor;
        int lastRet;
        int expectedModCount;

        private Itr() {
            this.cursor = 0;
            this.lastRet = -1;
            this.expectedModCount = AbstractList.this.modCount;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor != AbstractList.this.size();
        }

        @Override // java.util.Iterator
        public E next() {
            checkForComodification();
            try {
                int i2 = this.cursor;
                E e2 = (E) AbstractList.this.get(i2);
                this.lastRet = i2;
                this.cursor = i2 + 1;
                return e2;
            } catch (IndexOutOfBoundsException e3) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                AbstractList.this.remove(this.lastRet);
                if (this.lastRet < this.cursor) {
                    this.cursor--;
                }
                this.lastRet = -1;
                this.expectedModCount = AbstractList.this.modCount;
            } catch (IndexOutOfBoundsException e2) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (AbstractList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/AbstractList$ListItr.class */
    private class ListItr extends AbstractList<E>.Itr implements ListIterator<E> {
        ListItr(int i2) {
            super();
            this.cursor = i2;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        @Override // java.util.ListIterator
        public E previous() {
            checkForComodification();
            try {
                int i2 = this.cursor - 1;
                E e2 = (E) AbstractList.this.get(i2);
                this.cursor = i2;
                this.lastRet = i2;
                return e2;
            } catch (IndexOutOfBoundsException e3) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.cursor;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.cursor - 1;
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                AbstractList.this.set(this.lastRet, e2);
                this.expectedModCount = AbstractList.this.modCount;
            } catch (IndexOutOfBoundsException e3) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            checkForComodification();
            try {
                int i2 = this.cursor;
                AbstractList.this.add(i2, e2);
                this.lastRet = -1;
                this.cursor = i2 + 1;
                this.expectedModCount = AbstractList.this.modCount;
            } catch (IndexOutOfBoundsException e3) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public List<E> subList(int i2, int i3) {
        return this instanceof RandomAccess ? new RandomAccessSubList(this, i2, i3) : new SubList(this, i2, i3);
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        ListIterator<E> listIterator = listIterator();
        ListIterator<E> listIterator2 = ((List) obj).listIterator();
        while (listIterator.hasNext() && listIterator2.hasNext()) {
            E next = listIterator.next();
            E next2 = listIterator2.next();
            if (next == null) {
                if (next2 != null) {
                    return false;
                }
            } else if (!next.equals(next2)) {
                return false;
            }
        }
        return (listIterator.hasNext() || listIterator2.hasNext()) ? false : true;
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        int iHashCode = 1;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E next = it.next();
            iHashCode = (31 * iHashCode) + (next == null ? 0 : next.hashCode());
        }
        return iHashCode;
    }

    protected void removeRange(int i2, int i3) {
        ListIterator<E> listIterator = listIterator(i2);
        int i4 = i3 - i2;
        for (int i5 = 0; i5 < i4; i5++) {
            listIterator.next();
            listIterator.remove();
        }
    }

    private void rangeCheckForAdd(int i2) {
        if (i2 < 0 || i2 > size()) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
        }
    }

    private String outOfBoundsMsg(int i2) {
        return "Index: " + i2 + ", Size: " + size();
    }
}
