package java.util;

/* compiled from: AbstractList.java */
/* loaded from: rt.jar:java/util/SubList.class */
class SubList<E> extends AbstractList<E> {

    /* renamed from: l, reason: collision with root package name */
    private final AbstractList<E> f12563l;
    private final int offset;
    private int size;

    static /* synthetic */ int access$210(SubList subList) {
        int i2 = subList.size;
        subList.size = i2 - 1;
        return i2;
    }

    static /* synthetic */ int access$208(SubList subList) {
        int i2 = subList.size;
        subList.size = i2 + 1;
        return i2;
    }

    SubList(AbstractList<E> abstractList, int i2, int i3) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + i2);
        }
        if (i3 > abstractList.size()) {
            throw new IndexOutOfBoundsException("toIndex = " + i3);
        }
        if (i2 > i3) {
            throw new IllegalArgumentException("fromIndex(" + i2 + ") > toIndex(" + i3 + ")");
        }
        this.f12563l = abstractList;
        this.offset = i2;
        this.size = i3 - i2;
        this.modCount = this.f12563l.modCount;
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i2, E e2) {
        rangeCheck(i2);
        checkForComodification();
        return this.f12563l.set(i2 + this.offset, e2);
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i2) {
        rangeCheck(i2);
        checkForComodification();
        return this.f12563l.get(i2 + this.offset);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        checkForComodification();
        return this.size;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i2, E e2) {
        rangeCheckForAdd(i2);
        checkForComodification();
        this.f12563l.add(i2 + this.offset, e2);
        this.modCount = this.f12563l.modCount;
        this.size++;
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int i2) {
        rangeCheck(i2);
        checkForComodification();
        E eRemove = this.f12563l.remove(i2 + this.offset);
        this.modCount = this.f12563l.modCount;
        this.size--;
        return eRemove;
    }

    @Override // java.util.AbstractList
    protected void removeRange(int i2, int i3) {
        checkForComodification();
        this.f12563l.removeRange(i2 + this.offset, i3 + this.offset);
        this.modCount = this.f12563l.modCount;
        this.size -= i3 - i2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        return addAll(this.size, collection);
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) {
        rangeCheckForAdd(i2);
        int size = collection.size();
        if (size == 0) {
            return false;
        }
        checkForComodification();
        this.f12563l.addAll(this.offset + i2, collection);
        this.modCount = this.f12563l.modCount;
        this.size += size;
        return true;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(final int i2) {
        checkForComodification();
        rangeCheckForAdd(i2);
        return new ListIterator<E>() { // from class: java.util.SubList.1

            /* renamed from: i, reason: collision with root package name */
            private final ListIterator<E> f12564i;

            {
                this.f12564i = SubList.this.f12563l.listIterator(i2 + SubList.this.offset);
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public boolean hasNext() {
                return nextIndex() < SubList.this.size;
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public E next() {
                if (hasNext()) {
                    return this.f12564i.next();
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return previousIndex() >= 0;
            }

            @Override // java.util.ListIterator
            public E previous() {
                if (hasPrevious()) {
                    return this.f12564i.previous();
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.f12564i.nextIndex() - SubList.this.offset;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.f12564i.previousIndex() - SubList.this.offset;
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public void remove() {
                this.f12564i.remove();
                SubList.this.modCount = SubList.this.f12563l.modCount;
                SubList.access$210(SubList.this);
            }

            @Override // java.util.ListIterator
            public void set(E e2) {
                this.f12564i.set(e2);
            }

            @Override // java.util.ListIterator
            public void add(E e2) {
                this.f12564i.add(e2);
                SubList.this.modCount = SubList.this.f12563l.modCount;
                SubList.access$208(SubList.this);
            }
        };
    }

    @Override // java.util.AbstractList, java.util.List
    public List<E> subList(int i2, int i3) {
        return new SubList(this, i2, i3);
    }

    private void rangeCheck(int i2) {
        if (i2 < 0 || i2 >= this.size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
        }
    }

    private void rangeCheckForAdd(int i2) {
        if (i2 < 0 || i2 > this.size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
        }
    }

    private String outOfBoundsMsg(int i2) {
        return "Index: " + i2 + ", Size: " + this.size;
    }

    private void checkForComodification() {
        if (this.modCount != this.f12563l.modCount) {
            throw new ConcurrentModificationException();
        }
    }
}
