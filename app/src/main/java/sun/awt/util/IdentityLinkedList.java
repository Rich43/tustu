package sun.awt.util;

import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:sun/awt/util/IdentityLinkedList.class */
public class IdentityLinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E> {
    private transient Entry<E> header;
    private transient int size;

    public IdentityLinkedList() {
        this.header = new Entry<>(null, null, null);
        this.size = 0;
        Entry<E> entry = this.header;
        Entry<E> entry2 = this.header;
        Entry<E> entry3 = this.header;
        entry2.previous = entry3;
        entry.next = entry3;
    }

    public IdentityLinkedList(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    @Override // java.util.Deque
    public E getFirst() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.header.next.element;
    }

    @Override // java.util.Deque
    public E getLast() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.header.previous.element;
    }

    @Override // java.util.Deque
    public E removeFirst() {
        return remove((Entry) this.header.next);
    }

    @Override // java.util.Deque
    public E removeLast() {
        return remove((Entry) this.header.previous);
    }

    @Override // java.util.Deque
    public void addFirst(E e2) {
        addBefore(e2, this.header.next);
    }

    @Override // java.util.Deque
    public void addLast(E e2) {
        addBefore(e2, this.header);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        addBefore(e2, this.header);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        Entry<E> entry = this.header.next;
        while (true) {
            Entry<E> entry2 = entry;
            if (entry2 != this.header) {
                if (obj != entry2.element) {
                    entry = entry2.next;
                } else {
                    remove((Entry) entry2);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        return addAll(this.size, collection);
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) {
        if (i2 < 0 || i2 > this.size) {
            throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: " + this.size);
        }
        Object[] array = collection.toArray();
        int length = array.length;
        if (length == 0) {
            return false;
        }
        this.modCount++;
        Entry<E> entry = i2 == this.size ? this.header : entry(i2);
        Entry<E> entry2 = entry.previous;
        for (Object obj : array) {
            Entry<E> entry3 = new Entry<>(obj, entry, entry2);
            entry2.next = entry3;
            entry2 = entry3;
        }
        entry.previous = entry2;
        this.size += length;
        return true;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        Entry<E> entry = this.header.next;
        while (true) {
            Entry<E> entry2 = entry;
            if (entry2 != this.header) {
                Entry<E> entry3 = entry2.next;
                entry2.previous = null;
                entry2.next = null;
                entry2.element = null;
                entry = entry3;
            } else {
                Entry<E> entry4 = this.header;
                Entry<E> entry5 = this.header;
                Entry<E> entry6 = this.header;
                entry5.previous = entry6;
                entry4.next = entry6;
                this.size = 0;
                this.modCount++;
                return;
            }
        }
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public E get(int i2) {
        return entry(i2).element;
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public E set(int i2, E e2) {
        Entry<E> entry = entry(i2);
        E e3 = entry.element;
        entry.element = e2;
        return e3;
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public void add(int i2, E e2) {
        addBefore(e2, i2 == this.size ? this.header : entry(i2));
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public E remove(int i2) {
        return remove((Entry) entry(i2));
    }

    private Entry<E> entry(int i2) {
        if (i2 < 0 || i2 >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: " + this.size);
        }
        Entry<E> entry = this.header;
        if (i2 < (this.size >> 1)) {
            for (int i3 = 0; i3 <= i2; i3++) {
                entry = entry.next;
            }
        } else {
            for (int i4 = this.size; i4 > i2; i4--) {
                entry = entry.previous;
            }
        }
        return entry;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object obj) {
        int i2 = 0;
        Entry<E> entry = this.header.next;
        while (true) {
            Entry<E> entry2 = entry;
            if (entry2 != this.header) {
                if (obj == entry2.element) {
                    return i2;
                }
                i2++;
                entry = entry2.next;
            } else {
                return -1;
            }
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object obj) {
        int i2 = this.size;
        Entry<E> entry = this.header.previous;
        while (true) {
            Entry<E> entry2 = entry;
            if (entry2 != this.header) {
                i2--;
                if (obj != entry2.element) {
                    entry = entry2.previous;
                } else {
                    return i2;
                }
            } else {
                return -1;
            }
        }
    }

    @Override // java.util.Deque, java.util.Queue
    public E peek() {
        if (this.size == 0) {
            return null;
        }
        return getFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E element() {
        return getFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E poll() {
        if (this.size == 0) {
            return null;
        }
        return removeFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E remove() {
        return removeFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public boolean offer(E e2) {
        return add(e2);
    }

    @Override // java.util.Deque
    public boolean offerFirst(E e2) {
        addFirst(e2);
        return true;
    }

    @Override // java.util.Deque
    public boolean offerLast(E e2) {
        addLast(e2);
        return true;
    }

    @Override // java.util.Deque
    public E peekFirst() {
        if (this.size == 0) {
            return null;
        }
        return getFirst();
    }

    @Override // java.util.Deque
    public E peekLast() {
        if (this.size == 0) {
            return null;
        }
        return getLast();
    }

    @Override // java.util.Deque
    public E pollFirst() {
        if (this.size == 0) {
            return null;
        }
        return removeFirst();
    }

    @Override // java.util.Deque
    public E pollLast() {
        if (this.size == 0) {
            return null;
        }
        return removeLast();
    }

    @Override // java.util.Deque
    public void push(E e2) {
        addFirst(e2);
    }

    @Override // java.util.Deque
    public E pop() {
        return removeFirst();
    }

    @Override // java.util.Deque
    public boolean removeFirstOccurrence(Object obj) {
        return remove(obj);
    }

    @Override // java.util.Deque
    public boolean removeLastOccurrence(Object obj) {
        Entry<E> entry = this.header.previous;
        while (true) {
            Entry<E> entry2 = entry;
            if (entry2 != this.header) {
                if (obj != entry2.element) {
                    entry = entry2.previous;
                } else {
                    remove((Entry) entry2);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int i2) {
        return new ListItr(i2);
    }

    /* loaded from: rt.jar:sun/awt/util/IdentityLinkedList$ListItr.class */
    private class ListItr implements ListIterator<E> {
        private Entry<E> lastReturned;
        private Entry<E> next;
        private int nextIndex;
        private int expectedModCount;

        ListItr(int i2) {
            this.lastReturned = IdentityLinkedList.this.header;
            this.expectedModCount = IdentityLinkedList.this.modCount;
            if (i2 < 0 || i2 > IdentityLinkedList.this.size) {
                throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: " + IdentityLinkedList.this.size);
            }
            if (i2 < (IdentityLinkedList.this.size >> 1)) {
                this.next = IdentityLinkedList.this.header.next;
                this.nextIndex = 0;
                while (this.nextIndex < i2) {
                    this.next = this.next.next;
                    this.nextIndex++;
                }
                return;
            }
            this.next = IdentityLinkedList.this.header;
            this.nextIndex = IdentityLinkedList.this.size;
            while (this.nextIndex > i2) {
                this.next = this.next.previous;
                this.nextIndex--;
            }
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.nextIndex != IdentityLinkedList.this.size;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            checkForComodification();
            if (this.nextIndex == IdentityLinkedList.this.size) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.next;
            this.next = this.next.next;
            this.nextIndex++;
            return this.lastReturned.element;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.nextIndex != 0;
        }

        @Override // java.util.ListIterator
        public E previous() {
            if (this.nextIndex == 0) {
                throw new NoSuchElementException();
            }
            Entry<E> entry = this.next.previous;
            this.next = entry;
            this.lastReturned = entry;
            this.nextIndex--;
            checkForComodification();
            return this.lastReturned.element;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.nextIndex;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.nextIndex - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            checkForComodification();
            Entry<E> entry = this.lastReturned.next;
            try {
                IdentityLinkedList.this.remove((Entry<Object>) this.lastReturned);
                if (this.next == this.lastReturned) {
                    this.next = entry;
                } else {
                    this.nextIndex--;
                }
                this.lastReturned = IdentityLinkedList.this.header;
                this.expectedModCount++;
            } catch (NoSuchElementException e2) {
                throw new IllegalStateException();
            }
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            if (this.lastReturned == IdentityLinkedList.this.header) {
                throw new IllegalStateException();
            }
            checkForComodification();
            this.lastReturned.element = e2;
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            checkForComodification();
            this.lastReturned = IdentityLinkedList.this.header;
            IdentityLinkedList.this.addBefore(e2, this.next);
            this.nextIndex++;
            this.expectedModCount++;
        }

        final void checkForComodification() {
            if (IdentityLinkedList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* loaded from: rt.jar:sun/awt/util/IdentityLinkedList$Entry.class */
    private static class Entry<E> {
        E element;
        Entry<E> next;
        Entry<E> previous;

        Entry(E e2, Entry<E> entry, Entry<E> entry2) {
            this.element = e2;
            this.next = entry;
            this.previous = entry2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Entry<E> addBefore(E e2, Entry<E> entry) {
        Entry<E> entry2 = new Entry<>(e2, entry, entry.previous);
        entry2.previous.next = entry2;
        entry2.next.previous = entry2;
        this.size++;
        this.modCount++;
        return entry2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public E remove(Entry<E> entry) {
        if (entry == this.header) {
            throw new NoSuchElementException();
        }
        E e2 = entry.element;
        entry.previous.next = entry.next;
        entry.next.previous = entry.previous;
        entry.previous = null;
        entry.next = null;
        entry.element = null;
        this.size--;
        this.modCount++;
        return e2;
    }

    @Override // java.util.Deque
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    /* loaded from: rt.jar:sun/awt/util/IdentityLinkedList$DescendingIterator.class */
    private class DescendingIterator implements Iterator {
        final IdentityLinkedList<E>.ListItr itr;

        private DescendingIterator() {
            this.itr = new ListItr(IdentityLinkedList.this.size());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.itr.hasPrevious();
        }

        @Override // java.util.Iterator
        public E next() {
            return this.itr.previous();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.itr.remove();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] objArr = new Object[this.size];
        int i2 = 0;
        Entry<E> entry = this.header.next;
        while (true) {
            Entry<E> entry2 = entry;
            if (entry2 != this.header) {
                int i3 = i2;
                i2++;
                objArr[i3] = entry2.element;
                entry = entry2.next;
            } else {
                return objArr;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v21, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r0v3 */
    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.size) {
            tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.size);
        }
        int i2 = 0;
        ?? r0 = tArr;
        Entry<E> entry = this.header.next;
        while (true) {
            Entry<E> entry2 = entry;
            if (entry2 == this.header) {
                break;
            }
            int i3 = i2;
            i2++;
            r0[i3] = entry2.element;
            entry = entry2.next;
        }
        if (tArr.length > this.size) {
            tArr[this.size] = null;
        }
        return tArr;
    }
}
