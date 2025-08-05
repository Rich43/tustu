package java.util;

import java.lang.reflect.Array;

/* loaded from: rt.jar:java/util/AbstractCollection.class */
public abstract class AbstractCollection<E> implements Collection<E> {
    private static final int MAX_ARRAY_SIZE = 2147483639;

    @Override // java.util.Collection, java.lang.Iterable, java.util.List
    public abstract Iterator<E> iterator();

    @Override // java.util.Collection, java.util.Set
    public abstract int size();

    protected AbstractCollection() {
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        Iterator<E> it = iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] objArr = new Object[size()];
        Iterator<E> it = iterator();
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (!it.hasNext()) {
                return Arrays.copyOf(objArr, i2);
            }
            objArr[i2] = it.next();
        }
        return it.hasNext() ? finishToArray(objArr, it) : objArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        int size = size();
        T[] tArr2 = (T[]) (tArr.length >= size ? tArr : (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size));
        Iterator<E> it = iterator();
        for (int i2 = 0; i2 < tArr2.length; i2++) {
            if (!it.hasNext()) {
                if (tArr == tArr2) {
                    tArr2[i2] = 0;
                } else {
                    if (tArr.length < i2) {
                        return (T[]) Arrays.copyOf(tArr2, i2);
                    }
                    System.arraycopy(tArr2, 0, tArr, 0, i2);
                    if (tArr.length > i2) {
                        tArr[i2] = null;
                    }
                }
                return tArr;
            }
            tArr2[i2] = it.next();
        }
        return it.hasNext() ? (T[]) finishToArray(tArr2, it) : tArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v18, types: [java.lang.Object[]] */
    private static <T> T[] finishToArray(T[] tArr, Iterator<?> it) {
        int length = tArr.length;
        while (it.hasNext()) {
            int length2 = tArr.length;
            if (length == length2) {
                int iHugeCapacity = length2 + (length2 >> 1) + 1;
                if (iHugeCapacity - MAX_ARRAY_SIZE > 0) {
                    iHugeCapacity = hugeCapacity(length2 + 1);
                }
                tArr = Arrays.copyOf(tArr, iHugeCapacity);
            }
            int i2 = length;
            length++;
            tArr[i2] = it.next();
        }
        return length == tArr.length ? tArr : (T[]) Arrays.copyOf(tArr, length);
    }

    private static int hugeCapacity(int i2) {
        if (i2 < 0) {
            throw new OutOfMemoryError("Required array size too large");
        }
        if (i2 > MAX_ARRAY_SIZE) {
            return Integer.MAX_VALUE;
        }
        return MAX_ARRAY_SIZE;
    }

    @Override // java.util.Collection, java.util.List
    public boolean add(E e2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        Iterator<E> it = iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        boolean z2 = false;
        Iterator<? extends E> it = collection.iterator();
        while (it.hasNext()) {
            if (add(it.next())) {
                z2 = true;
            }
        }
        return z2;
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        boolean z2 = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                z2 = true;
            }
        }
        return z2;
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        boolean z2 = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                z2 = true;
            }
        }
        return z2;
    }

    @Override // java.util.Collection, java.util.List
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (true) {
            E next = it.next();
            sb.append(next == this ? "(this Collection)" : next);
            if (!it.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }
}
