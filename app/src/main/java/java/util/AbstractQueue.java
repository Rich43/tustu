package java.util;

/* loaded from: rt.jar:java/util/AbstractQueue.class */
public abstract class AbstractQueue<E> extends AbstractCollection<E> implements Queue<E> {
    protected AbstractQueue() {
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        if (offer(e2)) {
            return true;
        }
        throw new IllegalStateException("Queue full");
    }

    @Override // java.util.Queue
    public E remove() {
        E ePoll = poll();
        if (ePoll != null) {
            return ePoll;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Queue
    public E element() {
        E ePeek = peek();
        if (ePeek != null) {
            return ePeek;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        while (poll() != null) {
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        boolean z2 = false;
        Iterator<? extends E> it = collection.iterator();
        while (it.hasNext()) {
            if (add(it.next())) {
                z2 = true;
            }
        }
        return z2;
    }
}
