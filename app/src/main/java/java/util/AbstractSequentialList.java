package java.util;

/* loaded from: rt.jar:java/util/AbstractSequentialList.class */
public abstract class AbstractSequentialList<E> extends AbstractList<E> {
    @Override // java.util.AbstractList, java.util.List
    public abstract ListIterator<E> listIterator(int i2);

    protected AbstractSequentialList() {
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i2) {
        try {
            return listIterator(i2).next();
        } catch (NoSuchElementException e2) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i2, E e2) {
        try {
            ListIterator<E> listIterator = listIterator(i2);
            E next = listIterator.next();
            listIterator.set(e2);
            return next;
        } catch (NoSuchElementException e3) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i2, E e2) {
        try {
            listIterator(i2).add(e2);
        } catch (NoSuchElementException e3) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int i2) {
        try {
            ListIterator<E> listIterator = listIterator(i2);
            E next = listIterator.next();
            listIterator.remove();
            return next;
        } catch (NoSuchElementException e2) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) {
        try {
            boolean z2 = false;
            ListIterator<E> listIterator = listIterator(i2);
            Iterator<? extends E> it = collection.iterator();
            while (it.hasNext()) {
                listIterator.add(it.next());
                z2 = true;
            }
            return z2;
        } catch (NoSuchElementException e2) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return listIterator();
    }
}
