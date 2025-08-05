package java.util.concurrent;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/CopyOnWriteArrayList.class */
public class CopyOnWriteArrayList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 8673264195747942595L;
    final transient ReentrantLock lock = new ReentrantLock();
    private volatile transient Object[] array;
    private static final Unsafe UNSAFE;
    private static final long lockOffset;

    final Object[] getArray() {
        return this.array;
    }

    final void setArray(Object[] objArr) {
        this.array = objArr;
    }

    public CopyOnWriteArrayList() {
        setArray(new Object[0]);
    }

    public CopyOnWriteArrayList(Collection<? extends E> collection) {
        Object[] array;
        if (collection.getClass() == CopyOnWriteArrayList.class) {
            array = ((CopyOnWriteArrayList) collection).getArray();
        } else {
            array = collection.toArray();
            if (collection.getClass() != ArrayList.class) {
                array = Arrays.copyOf(array, array.length, Object[].class);
            }
        }
        setArray(array);
    }

    public CopyOnWriteArrayList(E[] eArr) {
        setArray(Arrays.copyOf(eArr, eArr.length, Object[].class));
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public int size() {
        return getArray().length;
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return size() == 0;
    }

    private static boolean eq(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    private static int indexOf(Object obj, Object[] objArr, int i2, int i3) {
        if (obj == null) {
            for (int i4 = i2; i4 < i3; i4++) {
                if (objArr[i4] == null) {
                    return i4;
                }
            }
            return -1;
        }
        for (int i5 = i2; i5 < i3; i5++) {
            if (obj.equals(objArr[i5])) {
                return i5;
            }
        }
        return -1;
    }

    private static int lastIndexOf(Object obj, Object[] objArr, int i2) {
        if (obj == null) {
            for (int i3 = i2; i3 >= 0; i3--) {
                if (objArr[i3] == null) {
                    return i3;
                }
            }
            return -1;
        }
        for (int i4 = i2; i4 >= 0; i4--) {
            if (obj.equals(objArr[i4])) {
                return i4;
            }
        }
        return -1;
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        Object[] array = getArray();
        return indexOf(obj, array, 0, array.length) >= 0;
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        Object[] array = getArray();
        return indexOf(obj, array, 0, array.length);
    }

    public int indexOf(E e2, int i2) {
        Object[] array = getArray();
        return indexOf(e2, array, i2, array.length);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        Object[] array = getArray();
        return lastIndexOf(obj, array, array.length - 1);
    }

    public int lastIndexOf(E e2, int i2) {
        return lastIndexOf(e2, getArray(), i2);
    }

    public Object clone() {
        try {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) super.clone();
            copyOnWriteArrayList.resetLock();
            return copyOnWriteArrayList;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    @Override // java.util.List
    public Object[] toArray() {
        Object[] array = getArray();
        return Arrays.copyOf(array, array.length);
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        Object[] array = getArray();
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

    private E get(Object[] objArr, int i2) {
        return (E) objArr[i2];
    }

    @Override // java.util.List
    public E get(int i2) {
        return get(getArray(), i2);
    }

    @Override // java.util.List
    public E set(int i2, E e2) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            E e3 = get(array, i2);
            if (e3 != e2) {
                Object[] objArrCopyOf = Arrays.copyOf(array, array.length);
                objArrCopyOf[i2] = e2;
                setArray(objArrCopyOf);
            } else {
                setArray(array);
            }
            return e3;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.List
    public boolean add(E e2) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            Object[] objArrCopyOf = Arrays.copyOf(array, length + 1);
            objArrCopyOf[length] = e2;
            setArray(objArrCopyOf);
            reentrantLock.unlock();
            return true;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List
    public void add(int i2, E e2) {
        Object[] objArrCopyOf;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            if (i2 > length || i2 < 0) {
                throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: " + length);
            }
            int i3 = length - i2;
            if (i3 == 0) {
                objArrCopyOf = Arrays.copyOf(array, length + 1);
            } else {
                objArrCopyOf = new Object[length + 1];
                System.arraycopy(array, 0, objArrCopyOf, 0, i2);
                System.arraycopy(array, i2, objArrCopyOf, i2 + 1, i3);
            }
            objArrCopyOf[i2] = e2;
            setArray(objArrCopyOf);
            reentrantLock.unlock();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List
    public E remove(int i2) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            E e2 = get(array, i2);
            int i3 = (length - i2) - 1;
            if (i3 == 0) {
                setArray(Arrays.copyOf(array, length - 1));
            } else {
                Object[] objArr = new Object[length - 1];
                System.arraycopy(array, 0, objArr, 0, i2);
                System.arraycopy(array, i2 + 1, objArr, i2, i3);
                setArray(objArr);
            }
            return e2;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        Object[] array = getArray();
        int iIndexOf = indexOf(obj, array, 0, array.length);
        if (iIndexOf < 0) {
            return false;
        }
        return remove(obj, array, iIndexOf);
    }

    private boolean remove(Object obj, Object[] objArr, int i2) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            if (objArr != array) {
                int iMin = Math.min(i2, length);
                int i3 = 0;
                while (true) {
                    if (i3 < iMin) {
                        if (array[i3] != objArr[i3] && eq(obj, array[i3])) {
                            i2 = i3;
                            break;
                        }
                        i3++;
                    } else {
                        if (i2 >= length) {
                            return false;
                        }
                        if (array[i2] != obj) {
                            i2 = indexOf(obj, array, i2, length);
                            if (i2 < 0) {
                                reentrantLock.unlock();
                                return false;
                            }
                        }
                    }
                }
            }
            Object[] objArr2 = new Object[length - 1];
            System.arraycopy(array, 0, objArr2, 0, i2);
            System.arraycopy(array, i2 + 1, objArr2, i2, (length - i2) - 1);
            setArray(objArr2);
            reentrantLock.unlock();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    void removeRange(int i2, int i3) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            if (i2 < 0 || i3 > length || i3 < i2) {
                throw new IndexOutOfBoundsException();
            }
            int i4 = length - (i3 - i2);
            int i5 = length - i3;
            if (i5 == 0) {
                setArray(Arrays.copyOf(array, i4));
            } else {
                Object[] objArr = new Object[i4];
                System.arraycopy(array, 0, objArr, 0, i2);
                System.arraycopy(array, i3, objArr, i2, i5);
                setArray(objArr);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean addIfAbsent(E e2) {
        Object[] array = getArray();
        if (indexOf(e2, array, 0, array.length) >= 0) {
            return false;
        }
        return addIfAbsent(e2, array);
    }

    private boolean addIfAbsent(E e2, Object[] objArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            if (objArr != array) {
                int iMin = Math.min(objArr.length, length);
                for (int i2 = 0; i2 < iMin; i2++) {
                    if (array[i2] != objArr[i2] && eq(e2, array[i2])) {
                        return false;
                    }
                }
                if (indexOf(e2, array, iMin, length) >= 0) {
                    reentrantLock.unlock();
                    return false;
                }
            }
            Object[] objArrCopyOf = Arrays.copyOf(array, length + 1);
            objArrCopyOf[length] = e2;
            setArray(objArrCopyOf);
            reentrantLock.unlock();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        Object[] array = getArray();
        int length = array.length;
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (indexOf(it.next(), array, 0, length) < 0) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            if (length != 0) {
                int i2 = 0;
                Object[] objArr = new Object[length];
                for (Object obj : array) {
                    if (!collection.contains(obj)) {
                        int i3 = i2;
                        i2++;
                        objArr[i3] = obj;
                    }
                }
                if (i2 != length) {
                    setArray(Arrays.copyOf(objArr, i2));
                    reentrantLock.unlock();
                    return true;
                }
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            if (length != 0) {
                int i2 = 0;
                Object[] objArr = new Object[length];
                for (Object obj : array) {
                    if (collection.contains(obj)) {
                        int i3 = i2;
                        i2++;
                        objArr[i3] = obj;
                    }
                }
                if (i2 != length) {
                    setArray(Arrays.copyOf(objArr, i2));
                    reentrantLock.unlock();
                    return true;
                }
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int addAllAbsent(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        if (collection.getClass() != ArrayList.class) {
            array = (Object[]) array.clone();
        }
        if (array.length == 0) {
            return 0;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            int i2 = 0;
            for (Object obj : array) {
                if (indexOf(obj, array2, 0, length) < 0 && indexOf(obj, array, 0, i2) < 0) {
                    int i3 = i2;
                    i2++;
                    array[i3] = obj;
                }
            }
            if (i2 > 0) {
                Object[] objArrCopyOf = Arrays.copyOf(array2, length + i2);
                System.arraycopy(array, 0, objArrCopyOf, length, i2);
                setArray(objArrCopyOf);
            }
            return i2;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.List
    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            setArray(new Object[0]);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        Object[] array = collection.getClass() == CopyOnWriteArrayList.class ? ((CopyOnWriteArrayList) collection).getArray() : collection.toArray();
        if (array.length == 0) {
            return false;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            if (length == 0 && (collection.getClass() == CopyOnWriteArrayList.class || collection.getClass() == ArrayList.class)) {
                setArray(array);
            } else {
                Object[] objArrCopyOf = Arrays.copyOf(array2, length + array.length);
                System.arraycopy(array, 0, objArrCopyOf, length, array.length);
                setArray(objArrCopyOf);
            }
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) {
        Object[] objArrCopyOf;
        Object[] array = collection.toArray();
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            if (i2 > length || i2 < 0) {
                throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: " + length);
            }
            if (array.length == 0) {
                return false;
            }
            int i3 = length - i2;
            if (i3 == 0) {
                objArrCopyOf = Arrays.copyOf(array2, length + array.length);
            } else {
                objArrCopyOf = new Object[length + array.length];
                System.arraycopy(array2, 0, objArrCopyOf, 0, i2);
                System.arraycopy(array2, i2, objArrCopyOf, i2 + array.length, i3);
            }
            System.arraycopy(array, 0, objArrCopyOf, i2, array.length);
            setArray(objArrCopyOf);
            reentrantLock.unlock();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super E> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        for (Object obj : getArray()) {
            consumer.accept(obj);
        }
    }

    @Override // java.util.Collection
    public boolean removeIf(Predicate<? super E> predicate) {
        if (predicate == null) {
            throw new NullPointerException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            if (length != 0) {
                int i2 = 0;
                Object[] objArr = new Object[length];
                for (Object obj : array) {
                    if (!predicate.test(obj)) {
                        int i3 = i2;
                        i2++;
                        objArr[i3] = obj;
                    }
                }
                if (i2 != length) {
                    setArray(Arrays.copyOf(objArr, i2));
                    reentrantLock.unlock();
                    return true;
                }
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.List
    public void replaceAll(UnaryOperator<E> unaryOperator) {
        if (unaryOperator == 0) {
            throw new NullPointerException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            Object[] objArrCopyOf = Arrays.copyOf(array, length);
            for (int i2 = 0; i2 < length; i2++) {
                objArrCopyOf[i2] = unaryOperator.apply(array[i2]);
            }
            setArray(objArrCopyOf);
            reentrantLock.unlock();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List, com.sun.javafx.collections.SortableList
    public void sort(Comparator<? super E> comparator) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array = getArray();
            Object[] objArrCopyOf = Arrays.copyOf(array, array.length);
            Arrays.sort(objArrCopyOf, comparator);
            setArray(objArrCopyOf);
            reentrantLock.unlock();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Object[] array = getArray();
        objectOutputStream.writeInt(array.length);
        for (Object obj : array) {
            objectOutputStream.writeObject(obj);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        resetLock();
        int i2 = objectInputStream.readInt();
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Object[].class, i2);
        Object[] objArr = new Object[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            objArr[i3] = objectInputStream.readObject();
        }
        setArray(objArr);
    }

    public String toString() {
        return Arrays.toString(getArray());
    }

    @Override // java.util.List
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        Iterator<E> it = ((List) obj).iterator();
        for (Object obj2 : getArray()) {
            if (!it.hasNext() || !eq(obj2, it.next())) {
                return false;
            }
        }
        if (it.hasNext()) {
            return false;
        }
        return true;
    }

    @Override // java.util.List
    public int hashCode() {
        int iHashCode = 1;
        Object[] array = getArray();
        int length = array.length;
        for (int i2 = 0; i2 < length; i2++) {
            Object obj = array[i2];
            iHashCode = (31 * iHashCode) + (obj == null ? 0 : obj.hashCode());
        }
        return iHashCode;
    }

    @Override // java.util.List
    public Iterator<E> iterator() {
        return new COWIterator(getArray(), 0);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return new COWIterator(getArray(), 0);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int i2) {
        Object[] array = getArray();
        int length = array.length;
        if (i2 < 0 || i2 > length) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
        return new COWIterator(array, i2);
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(getArray(), EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    /* loaded from: rt.jar:java/util/concurrent/CopyOnWriteArrayList$COWIterator.class */
    static final class COWIterator<E> implements ListIterator<E> {
        private final Object[] snapshot;
        private int cursor;

        private COWIterator(Object[] objArr, int i2) {
            this.cursor = i2;
            this.snapshot = objArr;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.cursor < this.snapshot.length;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.cursor > 0;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Object[] objArr = this.snapshot;
            int i2 = this.cursor;
            this.cursor = i2 + 1;
            return (E) objArr[i2];
        }

        @Override // java.util.ListIterator
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Object[] objArr = this.snapshot;
            int i2 = this.cursor - 1;
            this.cursor = i2;
            return (E) objArr[i2];
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.cursor;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.cursor - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            Object[] objArr = this.snapshot;
            int length = objArr.length;
            for (int i2 = this.cursor; i2 < length; i2++) {
                consumer.accept(objArr[i2]);
            }
            this.cursor = length;
        }
    }

    @Override // java.util.List
    public List<E> subList(int i2, int i3) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int length = getArray().length;
            if (i2 < 0 || i3 > length || i2 > i3) {
                throw new IndexOutOfBoundsException();
            }
            COWSubList cOWSubList = new COWSubList(this, i2, i3);
            reentrantLock.unlock();
            return cOWSubList;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/CopyOnWriteArrayList$COWSubList.class */
    private static class COWSubList<E> extends AbstractList<E> implements RandomAccess {

        /* renamed from: l, reason: collision with root package name */
        private final CopyOnWriteArrayList<E> f12578l;
        private final int offset;
        private int size;
        private Object[] expectedArray;

        COWSubList(CopyOnWriteArrayList<E> copyOnWriteArrayList, int i2, int i3) {
            this.f12578l = copyOnWriteArrayList;
            this.expectedArray = this.f12578l.getArray();
            this.offset = i2;
            this.size = i3 - i2;
        }

        private void checkForComodification() {
            if (this.f12578l.getArray() != this.expectedArray) {
                throw new ConcurrentModificationException();
            }
        }

        private void rangeCheck(int i2) {
            if (i2 < 0 || i2 >= this.size) {
                throw new IndexOutOfBoundsException("Index: " + i2 + ",Size: " + this.size);
            }
        }

        @Override // java.util.AbstractList, java.util.List
        public E set(int i2, E e2) {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                rangeCheck(i2);
                checkForComodification();
                E e3 = this.f12578l.set(i2 + this.offset, e2);
                this.expectedArray = this.f12578l.getArray();
                reentrantLock.unlock();
                return e3;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i2) {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                rangeCheck(i2);
                checkForComodification();
                E e2 = this.f12578l.get(i2 + this.offset);
                reentrantLock.unlock();
                return e2;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                checkForComodification();
                return this.size;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int i2, E e2) {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                checkForComodification();
                if (i2 < 0 || i2 > this.size) {
                    throw new IndexOutOfBoundsException();
                }
                this.f12578l.add(i2 + this.offset, e2);
                this.expectedArray = this.f12578l.getArray();
                this.size++;
                reentrantLock.unlock();
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                checkForComodification();
                this.f12578l.removeRange(this.offset, this.offset + this.size);
                this.expectedArray = this.f12578l.getArray();
                this.size = 0;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractList, java.util.List
        public E remove(int i2) {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                rangeCheck(i2);
                checkForComodification();
                E eRemove = this.f12578l.remove(i2 + this.offset);
                this.expectedArray = this.f12578l.getArray();
                this.size--;
                reentrantLock.unlock();
                return eRemove;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int iIndexOf = indexOf(obj);
            if (iIndexOf == -1) {
                return false;
            }
            remove(iIndexOf);
            return true;
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                checkForComodification();
                return new COWSubListIterator(this.f12578l, 0, this.offset, this.size);
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<E> listIterator(int i2) {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                checkForComodification();
                if (i2 < 0 || i2 > this.size) {
                    throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: " + this.size);
                }
                COWSubListIterator cOWSubListIterator = new COWSubListIterator(this.f12578l, i2, this.offset, this.size);
                reentrantLock.unlock();
                return cOWSubListIterator;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.util.AbstractList, java.util.List
        public List<E> subList(int i2, int i3) {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                checkForComodification();
                if (i2 < 0 || i3 > this.size || i2 > i3) {
                    throw new IndexOutOfBoundsException();
                }
                COWSubList cOWSubList = new COWSubList(this.f12578l, i2 + this.offset, i3 + this.offset);
                reentrantLock.unlock();
                return cOWSubList;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int i2 = this.offset;
            int i3 = this.offset + this.size;
            Object[] objArr = this.expectedArray;
            if (this.f12578l.getArray() != objArr) {
                throw new ConcurrentModificationException();
            }
            if (i2 < 0 || i3 > objArr.length) {
                throw new IndexOutOfBoundsException();
            }
            for (int i4 = i2; i4 < i3; i4++) {
                consumer.accept(objArr[i4]);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.List
        public void replaceAll(UnaryOperator<E> unaryOperator) {
            if (unaryOperator == 0) {
                throw new NullPointerException();
            }
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                int i2 = this.offset;
                int i3 = this.offset + this.size;
                Object[] objArr = this.expectedArray;
                if (this.f12578l.getArray() != objArr) {
                    throw new ConcurrentModificationException();
                }
                int length = objArr.length;
                if (i2 < 0 || i3 > length) {
                    throw new IndexOutOfBoundsException();
                }
                Object[] objArrCopyOf = Arrays.copyOf(objArr, length);
                for (int i4 = i2; i4 < i3; i4++) {
                    objArrCopyOf[i4] = unaryOperator.apply(objArr[i4]);
                }
                CopyOnWriteArrayList<E> copyOnWriteArrayList = this.f12578l;
                this.expectedArray = objArrCopyOf;
                copyOnWriteArrayList.setArray(objArrCopyOf);
                reentrantLock.unlock();
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.util.List, com.sun.javafx.collections.SortableList
        public void sort(Comparator<? super E> comparator) {
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                int i2 = this.offset;
                int i3 = this.offset + this.size;
                Object[] objArr = this.expectedArray;
                if (this.f12578l.getArray() != objArr) {
                    throw new ConcurrentModificationException();
                }
                int length = objArr.length;
                if (i2 < 0 || i3 > length) {
                    throw new IndexOutOfBoundsException();
                }
                Object[] objArrCopyOf = Arrays.copyOf(objArr, length);
                Arrays.sort(objArrCopyOf, i2, i3, comparator);
                CopyOnWriteArrayList<E> copyOnWriteArrayList = this.f12578l;
                this.expectedArray = objArrCopyOf;
                copyOnWriteArrayList.setArray(objArrCopyOf);
                reentrantLock.unlock();
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            boolean z2 = false;
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                int i2 = this.size;
                if (i2 > 0) {
                    int i3 = this.offset;
                    int i4 = this.offset + i2;
                    Object[] objArr = this.expectedArray;
                    if (this.f12578l.getArray() != objArr) {
                        throw new ConcurrentModificationException();
                    }
                    int length = objArr.length;
                    if (i3 < 0 || i4 > length) {
                        throw new IndexOutOfBoundsException();
                    }
                    int i5 = 0;
                    Object[] objArr2 = new Object[i2];
                    for (int i6 = i3; i6 < i4; i6++) {
                        Object obj = objArr[i6];
                        if (!collection.contains(obj)) {
                            int i7 = i5;
                            i5++;
                            objArr2[i7] = obj;
                        }
                    }
                    if (i5 != i2) {
                        Object[] objArr3 = new Object[(length - i2) + i5];
                        System.arraycopy(objArr, 0, objArr3, 0, i3);
                        System.arraycopy(objArr2, 0, objArr3, i3, i5);
                        System.arraycopy(objArr, i4, objArr3, i3 + i5, length - i4);
                        this.size = i5;
                        z2 = true;
                        CopyOnWriteArrayList<E> copyOnWriteArrayList = this.f12578l;
                        this.expectedArray = objArr3;
                        copyOnWriteArrayList.setArray(objArr3);
                    }
                }
                return z2;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            boolean z2 = false;
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                int i2 = this.size;
                if (i2 > 0) {
                    int i3 = this.offset;
                    int i4 = this.offset + i2;
                    Object[] objArr = this.expectedArray;
                    if (this.f12578l.getArray() != objArr) {
                        throw new ConcurrentModificationException();
                    }
                    int length = objArr.length;
                    if (i3 < 0 || i4 > length) {
                        throw new IndexOutOfBoundsException();
                    }
                    int i5 = 0;
                    Object[] objArr2 = new Object[i2];
                    for (int i6 = i3; i6 < i4; i6++) {
                        Object obj = objArr[i6];
                        if (collection.contains(obj)) {
                            int i7 = i5;
                            i5++;
                            objArr2[i7] = obj;
                        }
                    }
                    if (i5 != i2) {
                        Object[] objArr3 = new Object[(length - i2) + i5];
                        System.arraycopy(objArr, 0, objArr3, 0, i3);
                        System.arraycopy(objArr2, 0, objArr3, i3, i5);
                        System.arraycopy(objArr, i4, objArr3, i3 + i5, length - i4);
                        this.size = i5;
                        z2 = true;
                        CopyOnWriteArrayList<E> copyOnWriteArrayList = this.f12578l;
                        this.expectedArray = objArr3;
                        copyOnWriteArrayList.setArray(objArr3);
                    }
                }
                return z2;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            if (predicate == null) {
                throw new NullPointerException();
            }
            boolean z2 = false;
            ReentrantLock reentrantLock = this.f12578l.lock;
            reentrantLock.lock();
            try {
                int i2 = this.size;
                if (i2 > 0) {
                    int i3 = this.offset;
                    int i4 = this.offset + i2;
                    Object[] objArr = this.expectedArray;
                    if (this.f12578l.getArray() != objArr) {
                        throw new ConcurrentModificationException();
                    }
                    int length = objArr.length;
                    if (i3 < 0 || i4 > length) {
                        throw new IndexOutOfBoundsException();
                    }
                    int i5 = 0;
                    Object[] objArr2 = new Object[i2];
                    for (int i6 = i3; i6 < i4; i6++) {
                        Object obj = objArr[i6];
                        if (!predicate.test(obj)) {
                            int i7 = i5;
                            i5++;
                            objArr2[i7] = obj;
                        }
                    }
                    if (i5 != i2) {
                        Object[] objArr3 = new Object[(length - i2) + i5];
                        System.arraycopy(objArr, 0, objArr3, 0, i3);
                        System.arraycopy(objArr2, 0, objArr3, i3, i5);
                        System.arraycopy(objArr, i4, objArr3, i3 + i5, length - i4);
                        this.size = i5;
                        z2 = true;
                        CopyOnWriteArrayList<E> copyOnWriteArrayList = this.f12578l;
                        this.expectedArray = objArr3;
                        copyOnWriteArrayList.setArray(objArr3);
                    }
                }
                return z2;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            int i2 = this.offset;
            int i3 = this.offset + this.size;
            Object[] objArr = this.expectedArray;
            if (this.f12578l.getArray() != objArr) {
                throw new ConcurrentModificationException();
            }
            if (i2 < 0 || i3 > objArr.length) {
                throw new IndexOutOfBoundsException();
            }
            return Spliterators.spliterator(objArr, i2, i3, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/CopyOnWriteArrayList$COWSubListIterator.class */
    private static class COWSubListIterator<E> implements ListIterator<E> {
        private final ListIterator<E> it;
        private final int offset;
        private final int size;

        COWSubListIterator(List<E> list, int i2, int i3, int i4) {
            this.offset = i3;
            this.size = i4;
            this.it = list.listIterator(i2 + i3);
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return nextIndex() < this.size;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            if (hasNext()) {
                return this.it.next();
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
                return this.it.previous();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.it.nextIndex() - this.offset;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.it.previousIndex() - this.offset;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int i2 = this.size;
            ListIterator<E> listIterator = this.it;
            while (nextIndex() < i2) {
                consumer.accept(listIterator.next());
            }
        }
    }

    private void resetLock() {
        UNSAFE.putObjectVolatile(this, lockOffset, new ReentrantLock());
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            lockOffset = UNSAFE.objectFieldOffset(CopyOnWriteArrayList.class.getDeclaredField("lock"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
