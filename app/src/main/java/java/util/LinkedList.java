package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.function.Consumer;

/* loaded from: rt.jar:java/util/LinkedList.class */
public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, Serializable {
    transient int size;
    transient Node<E> first;
    transient Node<E> last;
    private static final long serialVersionUID = 876323262645176354L;

    public LinkedList() {
        this.size = 0;
    }

    public LinkedList(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    private void linkFirst(E e2) {
        Node<E> node = this.first;
        Node<E> node2 = new Node<>(null, e2, node);
        this.first = node2;
        if (node == null) {
            this.last = node2;
        } else {
            node.prev = node2;
        }
        this.size++;
        this.modCount++;
    }

    void linkLast(E e2) {
        Node<E> node = this.last;
        Node<E> node2 = new Node<>(node, e2, null);
        this.last = node2;
        if (node == null) {
            this.first = node2;
        } else {
            node.next = node2;
        }
        this.size++;
        this.modCount++;
    }

    void linkBefore(E e2, Node<E> node) {
        Node<E> node2 = node.prev;
        Node<E> node3 = new Node<>(node2, e2, node);
        node.prev = node3;
        if (node2 == null) {
            this.first = node3;
        } else {
            node2.next = node3;
        }
        this.size++;
        this.modCount++;
    }

    private E unlinkFirst(Node<E> node) {
        E e2 = node.item;
        Node<E> node2 = node.next;
        node.item = null;
        node.next = null;
        this.first = node2;
        if (node2 == null) {
            this.last = null;
        } else {
            node2.prev = null;
        }
        this.size--;
        this.modCount++;
        return e2;
    }

    private E unlinkLast(Node<E> node) {
        E e2 = node.item;
        Node<E> node2 = node.prev;
        node.item = null;
        node.prev = null;
        this.last = node2;
        if (node2 == null) {
            this.first = null;
        } else {
            node2.next = null;
        }
        this.size--;
        this.modCount++;
        return e2;
    }

    E unlink(Node<E> node) {
        E e2 = node.item;
        Node<E> node2 = node.next;
        Node<E> node3 = node.prev;
        if (node3 == null) {
            this.first = node2;
        } else {
            node3.next = node2;
            node.prev = null;
        }
        if (node2 == null) {
            this.last = node3;
        } else {
            node2.prev = node3;
            node.next = null;
        }
        node.item = null;
        this.size--;
        this.modCount++;
        return e2;
    }

    @Override // java.util.Deque
    public E getFirst() {
        Node<E> node = this.first;
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.item;
    }

    @Override // java.util.Deque
    public E getLast() {
        Node<E> node = this.last;
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.item;
    }

    @Override // java.util.Deque
    public E removeFirst() {
        Node<E> node = this.first;
        if (node == null) {
            throw new NoSuchElementException();
        }
        return unlinkFirst(node);
    }

    @Override // java.util.Deque
    public E removeLast() {
        Node<E> node = this.last;
        if (node == null) {
            throw new NoSuchElementException();
        }
        return unlinkLast(node);
    }

    @Override // java.util.Deque
    public void addFirst(E e2) {
        linkFirst(e2);
    }

    @Override // java.util.Deque
    public void addLast(E e2) {
        linkLast(e2);
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
        linkLast(e2);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        if (obj == null) {
            Node<E> node = this.first;
            while (true) {
                Node<E> node2 = node;
                if (node2 != null) {
                    if (node2.item != null) {
                        node = node2.next;
                    } else {
                        unlink(node2);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            Node<E> node3 = this.first;
            while (true) {
                Node<E> node4 = node3;
                if (node4 != null) {
                    if (!obj.equals(node4.item)) {
                        node3 = node4.next;
                    } else {
                        unlink(node4);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        return addAll(this.size, collection);
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) {
        Node<E> node;
        Node<E> node2;
        checkPositionIndex(i2);
        Object[] array = collection.toArray();
        int length = array.length;
        if (length == 0) {
            return false;
        }
        if (i2 == this.size) {
            node = null;
            node2 = this.last;
        } else {
            node = node(i2);
            node2 = node.prev;
        }
        for (Object obj : array) {
            Node<E> node3 = new Node<>(node2, obj, null);
            if (node2 == null) {
                this.first = node3;
            } else {
                node2.next = node3;
            }
            node2 = node3;
        }
        if (node == null) {
            this.last = node2;
        } else {
            node2.next = node;
            node.prev = node2;
        }
        this.size += length;
        this.modCount++;
        return true;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        Node<E> node = this.first;
        while (true) {
            Node<E> node2 = node;
            if (node2 != null) {
                Node<E> node3 = node2.next;
                node2.item = null;
                node2.next = null;
                node2.prev = null;
                node = node3;
            } else {
                this.last = null;
                this.first = null;
                this.size = 0;
                this.modCount++;
                return;
            }
        }
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public E get(int i2) {
        checkElementIndex(i2);
        return node(i2).item;
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public E set(int i2, E e2) {
        checkElementIndex(i2);
        Node<E> node = node(i2);
        E e3 = node.item;
        node.item = e2;
        return e3;
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public void add(int i2, E e2) {
        checkPositionIndex(i2);
        if (i2 == this.size) {
            linkLast(e2);
        } else {
            linkBefore(e2, node(i2));
        }
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public E remove(int i2) {
        checkElementIndex(i2);
        return unlink(node(i2));
    }

    private boolean isElementIndex(int i2) {
        return i2 >= 0 && i2 < this.size;
    }

    private boolean isPositionIndex(int i2) {
        return i2 >= 0 && i2 <= this.size;
    }

    private String outOfBoundsMsg(int i2) {
        return "Index: " + i2 + ", Size: " + this.size;
    }

    private void checkElementIndex(int i2) {
        if (!isElementIndex(i2)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
        }
    }

    private void checkPositionIndex(int i2) {
        if (!isPositionIndex(i2)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
        }
    }

    Node<E> node(int i2) {
        if (i2 < (this.size >> 1)) {
            Node<E> node = this.first;
            for (int i3 = 0; i3 < i2; i3++) {
                node = node.next;
            }
            return node;
        }
        Node<E> node2 = this.last;
        for (int i4 = this.size - 1; i4 > i2; i4--) {
            node2 = node2.prev;
        }
        return node2;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object obj) {
        int i2 = 0;
        if (obj == null) {
            Node<E> node = this.first;
            while (true) {
                Node<E> node2 = node;
                if (node2 != null) {
                    if (node2.item == null) {
                        return i2;
                    }
                    i2++;
                    node = node2.next;
                } else {
                    return -1;
                }
            }
        } else {
            Node<E> node3 = this.first;
            while (true) {
                Node<E> node4 = node3;
                if (node4 != null) {
                    if (obj.equals(node4.item)) {
                        return i2;
                    }
                    i2++;
                    node3 = node4.next;
                } else {
                    return -1;
                }
            }
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object obj) {
        int i2 = this.size;
        if (obj == null) {
            Node<E> node = this.last;
            while (true) {
                Node<E> node2 = node;
                if (node2 != null) {
                    i2--;
                    if (node2.item != null) {
                        node = node2.prev;
                    } else {
                        return i2;
                    }
                } else {
                    return -1;
                }
            }
        } else {
            Node<E> node3 = this.last;
            while (true) {
                Node<E> node4 = node3;
                if (node4 != null) {
                    i2--;
                    if (!obj.equals(node4.item)) {
                        node3 = node4.prev;
                    } else {
                        return i2;
                    }
                } else {
                    return -1;
                }
            }
        }
    }

    @Override // java.util.Deque, java.util.Queue
    public E peek() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return node.item;
    }

    @Override // java.util.Deque, java.util.Queue
    public E element() {
        return getFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E poll() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return unlinkFirst(node);
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
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return node.item;
    }

    @Override // java.util.Deque
    public E peekLast() {
        Node<E> node = this.last;
        if (node == null) {
            return null;
        }
        return node.item;
    }

    @Override // java.util.Deque
    public E pollFirst() {
        Node<E> node = this.first;
        if (node == null) {
            return null;
        }
        return unlinkFirst(node);
    }

    @Override // java.util.Deque
    public E pollLast() {
        Node<E> node = this.last;
        if (node == null) {
            return null;
        }
        return unlinkLast(node);
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
        if (obj == null) {
            Node<E> node = this.last;
            while (true) {
                Node<E> node2 = node;
                if (node2 != null) {
                    if (node2.item != null) {
                        node = node2.prev;
                    } else {
                        unlink(node2);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            Node<E> node3 = this.last;
            while (true) {
                Node<E> node4 = node3;
                if (node4 != null) {
                    if (!obj.equals(node4.item)) {
                        node3 = node4.prev;
                    } else {
                        unlink(node4);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int i2) {
        checkPositionIndex(i2);
        return new ListItr(i2);
    }

    /* loaded from: rt.jar:java/util/LinkedList$ListItr.class */
    private class ListItr implements ListIterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;
        private int expectedModCount;

        ListItr(int i2) {
            this.expectedModCount = LinkedList.this.modCount;
            this.next = i2 == LinkedList.this.size ? null : LinkedList.this.node(i2);
            this.nextIndex = i2;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.nextIndex < LinkedList.this.size;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.next;
            this.next = this.next.next;
            this.nextIndex++;
            return this.lastReturned.item;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.nextIndex > 0;
        }

        @Override // java.util.ListIterator
        public E previous() {
            checkForComodification();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Node<E> node = this.next == null ? LinkedList.this.last : this.next.prev;
            this.next = node;
            this.lastReturned = node;
            this.nextIndex--;
            return this.lastReturned.item;
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
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            Node<E> node = this.lastReturned.next;
            LinkedList.this.unlink(this.lastReturned);
            if (this.next == this.lastReturned) {
                this.next = node;
            } else {
                this.nextIndex--;
            }
            this.lastReturned = null;
            this.expectedModCount++;
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            checkForComodification();
            this.lastReturned.item = e2;
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            checkForComodification();
            this.lastReturned = null;
            if (this.next == null) {
                LinkedList.this.linkLast(e2);
            } else {
                LinkedList.this.linkBefore(e2, this.next);
            }
            this.nextIndex++;
            this.expectedModCount++;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            while (LinkedList.this.modCount == this.expectedModCount && this.nextIndex < LinkedList.this.size) {
                consumer.accept(this.next.item);
                this.lastReturned = this.next;
                this.next = this.next.next;
                this.nextIndex++;
            }
            checkForComodification();
        }

        final void checkForComodification() {
            if (LinkedList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/LinkedList$Node.class */
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> node, E e2, Node<E> node2) {
            this.item = e2;
            this.next = node2;
            this.prev = node;
        }
    }

    @Override // java.util.Deque
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    /* loaded from: rt.jar:java/util/LinkedList$DescendingIterator.class */
    private class DescendingIterator implements Iterator<E> {
        private final LinkedList<E>.ListItr itr;

        private DescendingIterator() {
            this.itr = new ListItr(LinkedList.this.size());
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

    private LinkedList<E> superClone() {
        try {
            return (LinkedList) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public Object clone() {
        LinkedList<E> linkedListSuperClone = superClone();
        linkedListSuperClone.last = null;
        linkedListSuperClone.first = null;
        linkedListSuperClone.size = 0;
        linkedListSuperClone.modCount = 0;
        Node<E> node = this.first;
        while (true) {
            Node<E> node2 = node;
            if (node2 != null) {
                linkedListSuperClone.add(node2.item);
                node = node2.next;
            } else {
                return linkedListSuperClone;
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] objArr = new Object[this.size];
        int i2 = 0;
        Node<E> node = this.first;
        while (true) {
            Node<E> node2 = node;
            if (node2 != null) {
                int i3 = i2;
                i2++;
                objArr[i3] = node2.item;
                node = node2.next;
            } else {
                return objArr;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v20, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r0v3 */
    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.size) {
            tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.size);
        }
        int i2 = 0;
        ?? r0 = tArr;
        Node<E> node = this.first;
        while (true) {
            Node<E> node2 = node;
            if (node2 == null) {
                break;
            }
            int i3 = i2;
            i2++;
            r0[i3] = node2.item;
            node = node2.next;
        }
        if (tArr.length > this.size) {
            tArr[this.size] = null;
        }
        return tArr;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        Node<E> node = this.first;
        while (true) {
            Node<E> node2 = node;
            if (node2 != null) {
                objectOutputStream.writeObject(node2.item);
                node = node2.next;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        for (int i3 = 0; i3 < i2; i3++) {
            linkLast(objectInputStream.readObject());
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new LLSpliterator(this, -1, 0);
    }

    /* loaded from: rt.jar:java/util/LinkedList$LLSpliterator.class */
    static final class LLSpliterator<E> implements Spliterator<E> {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        final LinkedList<E> list;
        Node<E> current;
        int est;
        int expectedModCount;
        int batch;

        LLSpliterator(LinkedList<E> linkedList, int i2, int i3) {
            this.list = linkedList;
            this.est = i2;
            this.expectedModCount = i3;
        }

        final int getEst() {
            int i2 = this.est;
            int i3 = i2;
            if (i2 < 0) {
                LinkedList<E> linkedList = this.list;
                if (linkedList == null) {
                    this.est = 0;
                    i3 = 0;
                } else {
                    this.expectedModCount = linkedList.modCount;
                    this.current = linkedList.first;
                    int i4 = linkedList.size;
                    this.est = i4;
                    i3 = i4;
                }
            }
            return i3;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getEst();
        }

        @Override // java.util.Spliterator
        public Spliterator<E> trySplit() {
            int est = getEst();
            if (est <= 1) {
                return null;
            }
            Node<E> node = this.current;
            Node<E> node2 = node;
            if (node != null) {
                int i2 = this.batch + 1024;
                if (i2 > est) {
                    i2 = est;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                Object[] objArr = new Object[i2];
                int i3 = 0;
                do {
                    int i4 = i3;
                    i3++;
                    objArr[i4] = node2.item;
                    Node<E> node3 = node2.next;
                    node2 = node3;
                    if (node3 == null) {
                        break;
                    }
                } while (i3 < i2);
                this.current = node2;
                this.batch = i3;
                this.est = est - i3;
                return Spliterators.spliterator(objArr, 0, i3, 16);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int est = getEst();
            int i2 = est;
            if (est > 0) {
                Node<E> node = this.current;
                Node<E> node2 = node;
                if (node != null) {
                    this.current = null;
                    this.est = 0;
                    do {
                        E e2 = node2.item;
                        node2 = node2.next;
                        consumer.accept(e2);
                        if (node2 == null) {
                            break;
                        } else {
                            i2--;
                        }
                    } while (i2 > 0);
                }
            }
            if (this.list.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            Node<E> node;
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (getEst() > 0 && (node = this.current) != null) {
                this.est--;
                E e2 = node.item;
                this.current = node.next;
                consumer.accept(e2);
                if (this.list.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16464;
        }
    }
}
