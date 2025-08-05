package javafx.collections;

import com.sun.javafx.collections.ListListenerHelper;
import com.sun.javafx.collections.MapAdapterChange;
import com.sun.javafx.collections.MapListenerHelper;
import com.sun.javafx.collections.ObservableFloatArrayImpl;
import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.collections.ObservableSequentialListWrapper;
import com.sun.javafx.collections.ObservableSetWrapper;
import com.sun.javafx.collections.SetAdapterChange;
import com.sun.javafx.collections.SetListenerHelper;
import com.sun.javafx.collections.SortableList;
import com.sun.javafx.collections.SourceAdapterChange;
import com.sun.javafx.collections.UnmodifiableObservableMap;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.MapChangeListener;
import javafx.collections.SetChangeListener;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/collections/FXCollections.class */
public class FXCollections {
    private static ObservableMap EMPTY_OBSERVABLE_MAP = new EmptyObservableMap();
    private static ObservableList EMPTY_OBSERVABLE_LIST = new EmptyObservableList();
    private static ObservableSet EMPTY_OBSERVABLE_SET = new EmptyObservableSet();

    /* renamed from: r, reason: collision with root package name */
    private static Random f12625r;

    private FXCollections() {
    }

    public static <E> ObservableList<E> observableList(List<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return list instanceof RandomAccess ? new ObservableListWrapper(list) : new ObservableSequentialListWrapper(list);
    }

    public static <E> ObservableList<E> observableList(List<E> list, Callback<E, Observable[]> extractor) {
        if (list == null || extractor == null) {
            throw new NullPointerException();
        }
        return list instanceof RandomAccess ? new ObservableListWrapper(list, extractor) : new ObservableSequentialListWrapper(list, extractor);
    }

    public static <K, V> ObservableMap<K, V> observableMap(Map<K, V> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        return new ObservableMapWrapper(map);
    }

    public static <E> ObservableSet<E> observableSet(Set<E> set) {
        if (set == null) {
            throw new NullPointerException();
        }
        return new ObservableSetWrapper(set);
    }

    public static <E> ObservableSet<E> observableSet(E... elements) {
        if (elements == null) {
            throw new NullPointerException();
        }
        Set<E> set = new HashSet<>(elements.length);
        Collections.addAll(set, elements);
        return new ObservableSetWrapper(set);
    }

    public static <K, V> ObservableMap<K, V> unmodifiableObservableMap(ObservableMap<K, V> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableMap(map);
    }

    public static <K, V> ObservableMap<K, V> checkedObservableMap(ObservableMap<K, V> map, Class<K> keyType, Class<V> valueType) {
        if (map == null || keyType == null || valueType == null) {
            throw new NullPointerException();
        }
        return new CheckedObservableMap(map, keyType, valueType);
    }

    public static <K, V> ObservableMap<K, V> synchronizedObservableMap(ObservableMap<K, V> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        return new SynchronizedObservableMap(map);
    }

    public static <K, V> ObservableMap<K, V> emptyObservableMap() {
        return EMPTY_OBSERVABLE_MAP;
    }

    public static ObservableIntegerArray observableIntegerArray() {
        return new ObservableIntegerArrayImpl();
    }

    public static ObservableIntegerArray observableIntegerArray(int... values) {
        return new ObservableIntegerArrayImpl(values);
    }

    public static ObservableIntegerArray observableIntegerArray(ObservableIntegerArray array) {
        return new ObservableIntegerArrayImpl(array);
    }

    public static ObservableFloatArray observableFloatArray() {
        return new ObservableFloatArrayImpl();
    }

    public static ObservableFloatArray observableFloatArray(float... values) {
        return new ObservableFloatArrayImpl(values);
    }

    public static ObservableFloatArray observableFloatArray(ObservableFloatArray array) {
        return new ObservableFloatArrayImpl(array);
    }

    public static <E> ObservableList<E> observableArrayList() {
        return observableList(new ArrayList());
    }

    public static <E> ObservableList<E> observableArrayList(Callback<E, Observable[]> extractor) {
        return observableList(new ArrayList(), extractor);
    }

    public static <E> ObservableList<E> observableArrayList(E... items) {
        ObservableList<E> list = observableArrayList();
        list.addAll(items);
        return list;
    }

    public static <E> ObservableList<E> observableArrayList(Collection<? extends E> col) {
        ObservableList<E> list = observableArrayList();
        list.addAll(col);
        return list;
    }

    public static <K, V> ObservableMap<K, V> observableHashMap() {
        return observableMap(new HashMap());
    }

    public static <E> ObservableList<E> concat(ObservableList<E>... lists) {
        if (lists.length == 0) {
            return observableArrayList();
        }
        if (lists.length == 1) {
            return observableArrayList(lists[0]);
        }
        ArrayList<E> backingList = new ArrayList<>();
        for (ObservableList<E> s2 : lists) {
            backingList.addAll(s2);
        }
        return observableList(backingList);
    }

    public static <E> ObservableList<E> unmodifiableObservableList(ObservableList<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableListImpl(list);
    }

    public static <E> ObservableList<E> checkedObservableList(ObservableList<E> list, Class<E> type) {
        if (list == null) {
            throw new NullPointerException();
        }
        return new CheckedObservableList(list, type);
    }

    public static <E> ObservableList<E> synchronizedObservableList(ObservableList<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return new SynchronizedObservableList(list);
    }

    public static <E> ObservableList<E> emptyObservableList() {
        return EMPTY_OBSERVABLE_LIST;
    }

    public static <E> ObservableList<E> singletonObservableList(E e2) {
        return new SingletonObservableList(e2);
    }

    public static <E> ObservableSet<E> unmodifiableObservableSet(ObservableSet<E> set) {
        if (set == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableSet(set);
    }

    public static <E> ObservableSet<E> checkedObservableSet(ObservableSet<E> set, Class<E> type) {
        if (set == null) {
            throw new NullPointerException();
        }
        return new CheckedObservableSet(set, type);
    }

    public static <E> ObservableSet<E> synchronizedObservableSet(ObservableSet<E> set) {
        if (set == null) {
            throw new NullPointerException();
        }
        return new SynchronizedObservableSet(set);
    }

    public static <E> ObservableSet<E> emptyObservableSet() {
        return EMPTY_OBSERVABLE_SET;
    }

    public static <T> void copy(ObservableList<? super T> dest, List<? extends T> src) {
        int srcSize = src.size();
        if (srcSize > dest.size()) {
            throw new IndexOutOfBoundsException("Source does not fit in dest");
        }
        Object[] array = dest.toArray();
        System.arraycopy(src.toArray(), 0, array, 0, srcSize);
        dest.setAll(array);
    }

    public static <T> void fill(ObservableList<? super T> list, T obj) {
        Object[] objArr = new Object[list.size()];
        Arrays.fill(objArr, obj);
        list.setAll(objArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> boolean replaceAll(ObservableList<T> observableList, T oldVal, T newVal) {
        Object[] array = observableList.toArray();
        boolean modified = false;
        for (int i2 = 0; i2 < array.length; i2++) {
            if (array[i2].equals(oldVal)) {
                array[i2] = newVal;
                modified = true;
            }
        }
        if (modified) {
            observableList.setAll((T[]) array);
        }
        return modified;
    }

    public static void reverse(ObservableList list) {
        Object[] newContent = list.toArray();
        for (int i2 = 0; i2 < newContent.length / 2; i2++) {
            Object tmp = newContent[i2];
            newContent[i2] = newContent[(newContent.length - i2) - 1];
            newContent[(newContent.length - i2) - 1] = tmp;
        }
        list.setAll(newContent);
    }

    public static void rotate(ObservableList list, int distance) {
        Object[] newContent = list.toArray();
        int size = list.size();
        int distance2 = distance % size;
        if (distance2 < 0) {
            distance2 += size;
        }
        if (distance2 == 0) {
            return;
        }
        int cycleStart = 0;
        int nMoved = 0;
        while (nMoved != size) {
            Object displaced = newContent[cycleStart];
            int i2 = cycleStart;
            do {
                i2 += distance2;
                if (i2 >= size) {
                    i2 -= size;
                }
                Object tmp = newContent[i2];
                newContent[i2] = displaced;
                displaced = tmp;
                nMoved++;
            } while (i2 != cycleStart);
            cycleStart++;
        }
        list.setAll(newContent);
    }

    public static void shuffle(ObservableList<?> list) {
        if (f12625r == null) {
            f12625r = new Random();
        }
        shuffle(list, f12625r);
    }

    public static void shuffle(ObservableList list, Random rnd) {
        Object[] newContent = list.toArray();
        for (int i2 = list.size(); i2 > 1; i2--) {
            swap(newContent, i2 - 1, rnd.nextInt(i2));
        }
        list.setAll(newContent);
    }

    private static void swap(Object[] arr, int i2, int j2) {
        Object tmp = arr[i2];
        arr[i2] = arr[j2];
        arr[j2] = tmp;
    }

    public static <T extends Comparable<? super T>> void sort(ObservableList<T> list) {
        if (list instanceof SortableList) {
            ((SortableList) list).sort();
            return;
        }
        List<T> newContent = new ArrayList<>(list);
        Collections.sort(newContent);
        list.setAll(newContent);
    }

    public static <T> void sort(ObservableList<T> list, Comparator<? super T> c2) {
        if (list instanceof SortableList) {
            ((SortableList) list).sort(c2);
            return;
        }
        List<T> newContent = new ArrayList<>(list);
        Collections.sort(newContent, c2);
        list.setAll(newContent);
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$EmptyObservableList.class */
    private static class EmptyObservableList<E> extends AbstractList<E> implements ObservableList<E> {
        private static final ListIterator iterator = new ListIterator() { // from class: javafx.collections.FXCollections.EmptyObservableList.1
            @Override // java.util.ListIterator, java.util.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override // java.util.ListIterator, java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return false;
            }

            @Override // java.util.ListIterator
            public Object previous() {
                throw new NoSuchElementException();
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return 0;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return -1;
            }

            @Override // java.util.ListIterator
            public void set(Object e2) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.ListIterator
            public void add(Object e2) {
                throw new UnsupportedOperationException();
            }
        };

        @Override // javafx.beans.Observable
        public final void addListener(InvalidationListener listener) {
        }

        @Override // javafx.beans.Observable
        public final void removeListener(InvalidationListener listener) {
        }

        @Override // javafx.collections.ObservableList
        public void addListener(ListChangeListener<? super E> o2) {
        }

        @Override // javafx.collections.ObservableList
        public void removeListener(ListChangeListener<? super E> o2) {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            return false;
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return iterator;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            return c2.isEmpty();
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int index) {
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object o2) {
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object o2) {
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<E> listIterator() {
            return iterator;
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<E> listIterator(int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            return iterator;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<E> subList(int fromIndex, int toIndex) {
            if (fromIndex != 0 || toIndex != 0) {
                throw new IndexOutOfBoundsException();
            }
            return this;
        }

        @Override // javafx.collections.ObservableList
        public boolean addAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(Collection<? extends E> col) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean removeAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean retainAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SingletonObservableList.class */
    private static class SingletonObservableList<E> extends AbstractList<E> implements ObservableList<E> {
        private final E element;

        public SingletonObservableList(E element) {
            if (element == null) {
                throw new NullPointerException();
            }
            this.element = element;
        }

        @Override // javafx.collections.ObservableList
        public boolean addAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(Collection<? extends E> col) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean removeAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean retainAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
        }

        @Override // javafx.collections.ObservableList
        public void addListener(ListChangeListener<? super E> o2) {
        }

        @Override // javafx.collections.ObservableList
        public void removeListener(ListChangeListener<? super E> o2) {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            return this.element.equals(o2);
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            return this.element;
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$UnmodifiableObservableListImpl.class */
    private static class UnmodifiableObservableListImpl<T> extends ObservableListBase<T> implements ObservableList<T> {
        private final ObservableList<T> backingList;
        private final ListChangeListener<T> listener = c2 -> {
            fireChange(new SourceAdapterChange(this, c2));
        };

        public UnmodifiableObservableListImpl(ObservableList<T> backingList) {
            this.backingList = backingList;
            this.backingList.addListener(new WeakListChangeListener(this.listener));
        }

        @Override // java.util.AbstractList, java.util.List
        public T get(int index) {
            return this.backingList.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.backingList.size();
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean addAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean setAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean setAll(Collection<? extends T> col) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean removeAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean retainAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SynchronizedList.class */
    private static class SynchronizedList<T> implements List<T> {
        final Object mutex;
        private final List<T> backingList;

        SynchronizedList(List<T> list, Object mutex) {
            this.backingList = list;
            this.mutex = mutex;
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.backingList.size();
            }
            return size;
        }

        @Override // java.util.List, java.util.Collection
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = this.backingList.isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            boolean zContains;
            synchronized (this.mutex) {
                zContains = this.backingList.contains(o2);
            }
            return zContains;
        }

        @Override // java.util.List
        public Iterator<T> iterator() {
            return this.backingList.iterator();
        }

        @Override // java.util.List
        public Object[] toArray() {
            Object[] array;
            synchronized (this.mutex) {
                array = this.backingList.toArray();
            }
            return array;
        }

        @Override // java.util.List, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            T[] tArr2;
            synchronized (this.mutex) {
                tArr2 = (T[]) this.backingList.toArray(tArr);
            }
            return tArr2;
        }

        @Override // java.util.List
        public boolean add(T e2) {
            boolean zAdd;
            synchronized (this.mutex) {
                zAdd = this.backingList.add(e2);
            }
            return zAdd;
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            boolean zRemove;
            synchronized (this.mutex) {
                zRemove = this.backingList.remove(o2);
            }
            return zRemove;
        }

        @Override // java.util.List, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            boolean zContainsAll;
            synchronized (this.mutex) {
                zContainsAll = this.backingList.containsAll(c2);
            }
            return zContainsAll;
        }

        @Override // java.util.List, java.util.Collection
        public boolean addAll(Collection<? extends T> c2) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = this.backingList.addAll(c2);
            }
            return zAddAll;
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends T> c2) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = this.backingList.addAll(index, c2);
            }
            return zAddAll;
        }

        @Override // java.util.List, java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            boolean zRemoveAll;
            synchronized (this.mutex) {
                zRemoveAll = this.backingList.removeAll(c2);
            }
            return zRemoveAll;
        }

        @Override // java.util.List, java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            boolean zRetainAll;
            synchronized (this.mutex) {
                zRetainAll = this.backingList.retainAll(c2);
            }
            return zRetainAll;
        }

        @Override // java.util.List
        public void clear() {
            synchronized (this.mutex) {
                this.backingList.clear();
            }
        }

        @Override // java.util.List
        public T get(int index) {
            T t2;
            synchronized (this.mutex) {
                t2 = this.backingList.get(index);
            }
            return t2;
        }

        @Override // java.util.List
        public T set(int index, T element) {
            T t2;
            synchronized (this.mutex) {
                t2 = this.backingList.set(index, element);
            }
            return t2;
        }

        @Override // java.util.List
        public void add(int index, T element) {
            synchronized (this.mutex) {
                this.backingList.add(index, element);
            }
        }

        @Override // java.util.List
        public T remove(int index) {
            T tRemove;
            synchronized (this.mutex) {
                tRemove = this.backingList.remove(index);
            }
            return tRemove;
        }

        @Override // java.util.List
        public int indexOf(Object o2) {
            int iIndexOf;
            synchronized (this.mutex) {
                iIndexOf = this.backingList.indexOf(o2);
            }
            return iIndexOf;
        }

        @Override // java.util.List
        public int lastIndexOf(Object o2) {
            int iLastIndexOf;
            synchronized (this.mutex) {
                iLastIndexOf = this.backingList.lastIndexOf(o2);
            }
            return iLastIndexOf;
        }

        @Override // java.util.List
        public ListIterator<T> listIterator() {
            return this.backingList.listIterator();
        }

        @Override // java.util.List
        public ListIterator<T> listIterator(int index) {
            ListIterator<T> listIterator;
            synchronized (this.mutex) {
                listIterator = this.backingList.listIterator(index);
            }
            return listIterator;
        }

        @Override // java.util.List
        public List<T> subList(int fromIndex, int toIndex) {
            SynchronizedList synchronizedList;
            synchronized (this.mutex) {
                synchronizedList = new SynchronizedList(this.backingList.subList(fromIndex, toIndex), this.mutex);
            }
            return synchronizedList;
        }

        public String toString() {
            String string;
            synchronized (this.mutex) {
                string = this.backingList.toString();
            }
            return string;
        }

        @Override // java.util.List
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = this.backingList.hashCode();
            }
            return iHashCode;
        }

        @Override // java.util.List
        public boolean equals(Object o2) {
            boolean zEquals;
            synchronized (this.mutex) {
                zEquals = this.backingList.equals(o2);
            }
            return zEquals;
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SynchronizedObservableList.class */
    private static class SynchronizedObservableList<T> extends SynchronizedList<T> implements ObservableList<T> {
        private ListListenerHelper helper;
        private final ObservableList<T> backingList;
        private final ListChangeListener<T> listener;

        SynchronizedObservableList(ObservableList<T> seq, Object mutex) {
            super(seq, mutex);
            this.backingList = seq;
            this.listener = c2 -> {
                ListListenerHelper.fireValueChangedEvent(this.helper, new SourceAdapterChange(this, c2));
            };
            this.backingList.addListener(new WeakListChangeListener(this.listener));
        }

        SynchronizedObservableList(ObservableList<T> seq) {
            this(seq, new Object());
        }

        @Override // javafx.collections.ObservableList
        public boolean addAll(T... elements) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = this.backingList.addAll(elements);
            }
            return zAddAll;
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(T... elements) {
            boolean all;
            synchronized (this.mutex) {
                all = this.backingList.setAll(elements);
            }
            return all;
        }

        @Override // javafx.collections.ObservableList
        public boolean removeAll(T... elements) {
            boolean zRemoveAll;
            synchronized (this.mutex) {
                zRemoveAll = this.backingList.removeAll(elements);
            }
            return zRemoveAll;
        }

        @Override // javafx.collections.ObservableList
        public boolean retainAll(T... elements) {
            boolean zRetainAll;
            synchronized (this.mutex) {
                zRetainAll = this.backingList.retainAll(elements);
            }
            return zRetainAll;
        }

        @Override // javafx.collections.ObservableList
        public void remove(int from, int to) {
            synchronized (this.mutex) {
                this.backingList.remove(from, to);
            }
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(Collection<? extends T> col) {
            boolean all;
            synchronized (this.mutex) {
                all = this.backingList.setAll(col);
            }
            return all;
        }

        @Override // javafx.beans.Observable
        public final void addListener(InvalidationListener listener) {
            synchronized (this.mutex) {
                this.helper = ListListenerHelper.addListener(this.helper, listener);
            }
        }

        @Override // javafx.beans.Observable
        public final void removeListener(InvalidationListener listener) {
            synchronized (this.mutex) {
                this.helper = ListListenerHelper.removeListener(this.helper, listener);
            }
        }

        @Override // javafx.collections.ObservableList
        public void addListener(ListChangeListener<? super T> listener) {
            synchronized (this.mutex) {
                this.helper = ListListenerHelper.addListener(this.helper, listener);
            }
        }

        @Override // javafx.collections.ObservableList
        public void removeListener(ListChangeListener<? super T> listener) {
            synchronized (this.mutex) {
                this.helper = ListListenerHelper.removeListener(this.helper, listener);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$CheckedObservableList.class */
    private static class CheckedObservableList<T> extends ObservableListBase<T> implements ObservableList<T> {
        private final ObservableList<T> list;
        private final Class<T> type;
        private final ListChangeListener<T> listener;

        CheckedObservableList(ObservableList<T> list, Class<T> type) {
            if (list == null || type == null) {
                throw new NullPointerException();
            }
            this.list = list;
            this.type = type;
            this.listener = c2 -> {
                fireChange(new SourceAdapterChange(this, c2));
            };
            list.addListener(new WeakListChangeListener(this.listener));
        }

        void typeCheck(Object o2) {
            if (o2 != null && !this.type.isInstance(o2)) {
                throw new ClassCastException("Attempt to insert " + ((Object) o2.getClass()) + " element into collection with element type " + ((Object) this.type));
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.list.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            return this.list.contains(o2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return this.list.toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.list.toArray(tArr);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return this.list.toString();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            return this.list.remove(o2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> coll) {
            return this.list.containsAll(coll);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> coll) {
            return this.list.removeAll(coll);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> coll) {
            return this.list.retainAll(coll);
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean removeAll(T... elements) {
            return this.list.removeAll(elements);
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean retainAll(T... elements) {
            return this.list.retainAll(elements);
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public void remove(int from, int to) {
            this.list.remove(from, to);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.list.clear();
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(Object o2) {
            return o2 == this || this.list.equals(o2);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            return this.list.hashCode();
        }

        @Override // java.util.AbstractList, java.util.List
        public T get(int index) {
            return this.list.get(index);
        }

        @Override // java.util.AbstractList, java.util.List
        public T remove(int index) {
            return this.list.remove(index);
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object o2) {
            return this.list.indexOf(o2);
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object o2) {
            return this.list.lastIndexOf(o2);
        }

        @Override // java.util.AbstractList, java.util.List
        public T set(int index, T element) {
            typeCheck(element);
            return this.list.set(index, element);
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int index, T element) {
            typeCheck(element);
            this.list.add(index, element);
        }

        @Override // java.util.AbstractList, java.util.List
        public boolean addAll(int index, Collection<? extends T> c2) {
            try {
                return this.list.addAll(index, Arrays.asList(c2.toArray((Object[]) Array.newInstance((Class<?>) this.type, 0))));
            } catch (ArrayStoreException e2) {
                throw new ClassCastException();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends T> coll) {
            try {
                return this.list.addAll(Arrays.asList(coll.toArray((Object[]) Array.newInstance((Class<?>) this.type, 0))));
            } catch (ArrayStoreException e2) {
                throw new ClassCastException();
            }
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator() {
            return listIterator(0);
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator(final int index) {
            return new ListIterator<T>() { // from class: javafx.collections.FXCollections.CheckedObservableList.1

                /* renamed from: i, reason: collision with root package name */
                ListIterator<T> f12626i;

                {
                    this.f12626i = (ListIterator<T>) CheckedObservableList.this.list.listIterator(index);
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return this.f12626i.hasNext();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public T next() {
                    return this.f12626i.next();
                }

                @Override // java.util.ListIterator
                public boolean hasPrevious() {
                    return this.f12626i.hasPrevious();
                }

                @Override // java.util.ListIterator
                public T previous() {
                    return this.f12626i.previous();
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return this.f12626i.nextIndex();
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return this.f12626i.previousIndex();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public void remove() {
                    this.f12626i.remove();
                }

                @Override // java.util.ListIterator
                public void set(T e2) {
                    CheckedObservableList.this.typeCheck(e2);
                    this.f12626i.set(e2);
                }

                @Override // java.util.ListIterator
                public void add(T e2) {
                    CheckedObservableList.this.typeCheck(e2);
                    this.f12626i.add(e2);
                }
            };
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<T> iterator() {
            return new Iterator<T>() { // from class: javafx.collections.FXCollections.CheckedObservableList.2
                private final Iterator<T> it;

                {
                    this.it = (Iterator<T>) CheckedObservableList.this.list.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.it.hasNext();
                }

                @Override // java.util.Iterator
                public T next() {
                    return this.it.next();
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.it.remove();
                }
            };
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(T e2) {
            typeCheck(e2);
            return this.list.add(e2);
        }

        @Override // java.util.AbstractList, java.util.List
        public List<T> subList(int fromIndex, int toIndex) {
            return Collections.checkedList(this.list.subList(fromIndex, toIndex), this.type);
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean addAll(T... tArr) {
            try {
                Object[] objArr = (Object[]) Array.newInstance((Class<?>) this.type, tArr.length);
                System.arraycopy(tArr, 0, objArr, 0, tArr.length);
                return this.list.addAll((T[]) objArr);
            } catch (ArrayStoreException e2) {
                throw new ClassCastException();
            }
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean setAll(T... tArr) {
            try {
                Object[] objArr = (Object[]) Array.newInstance((Class<?>) this.type, tArr.length);
                System.arraycopy(tArr, 0, objArr, 0, tArr.length);
                return this.list.setAll((T[]) objArr);
            } catch (ArrayStoreException e2) {
                throw new ClassCastException();
            }
        }

        @Override // javafx.collections.ObservableListBase, javafx.collections.ObservableList
        public boolean setAll(Collection<? extends T> col) {
            try {
                return this.list.setAll(Arrays.asList(col.toArray((Object[]) Array.newInstance((Class<?>) this.type, 0))));
            } catch (ArrayStoreException e2) {
                throw new ClassCastException();
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$EmptyObservableSet.class */
    private static class EmptyObservableSet<E> extends AbstractSet<E> implements ObservableSet<E> {
        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
        }

        @Override // javafx.collections.ObservableSet
        public void addListener(SetChangeListener<? super E> listener) {
        }

        @Override // javafx.collections.ObservableSet
        public void removeListener(SetChangeListener<? super E> listener) {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            return c2.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return new Object[0];
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <E> E[] toArray(E[] a2) {
            if (a2.length > 0) {
                a2[0] = null;
            }
            return a2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return new Iterator() { // from class: javafx.collections.FXCollections.EmptyObservableSet.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return false;
                }

                @Override // java.util.Iterator
                public Object next() {
                    throw new NoSuchElementException();
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$UnmodifiableObservableSet.class */
    private static class UnmodifiableObservableSet<E> extends AbstractSet<E> implements ObservableSet<E> {
        private final ObservableSet<E> backingSet;
        private SetListenerHelper<E> listenerHelper;
        private SetChangeListener<E> listener = null;

        public UnmodifiableObservableSet(ObservableSet<E> backingSet) {
            this.backingSet = backingSet;
        }

        private void initListener() {
            if (this.listener == null) {
                this.listener = c2 -> {
                    callObservers(new SetAdapterChange(this, c2));
                };
                this.backingSet.addListener(new WeakSetChangeListener(this.listener));
            }
        }

        private void callObservers(SetChangeListener.Change<? extends E> change) {
            SetListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return new Iterator<E>() { // from class: javafx.collections.FXCollections.UnmodifiableObservableSet.1

                /* renamed from: i, reason: collision with root package name */
                private final Iterator<? extends E> f12629i;

                {
                    this.f12629i = UnmodifiableObservableSet.this.backingSet.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.f12629i.hasNext();
                }

                @Override // java.util.Iterator
                public E next() {
                    return this.f12629i.next();
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.backingSet.size();
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            initListener();
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableSet
        public void addListener(SetChangeListener<? super E> listener) {
            initListener();
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableSet
        public void removeListener(SetChangeListener<? super E> listener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, listener);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> c2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SynchronizedSet.class */
    private static class SynchronizedSet<E> implements Set<E> {
        final Object mutex;
        private final Set<E> backingSet;

        SynchronizedSet(Set<E> set, Object mutex) {
            this.backingSet = set;
            this.mutex = mutex;
        }

        SynchronizedSet(Set<E> set) {
            this(set, new Object());
        }

        @Override // java.util.Set
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.backingSet.size();
            }
            return size;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = this.backingSet.isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.Set
        public boolean contains(Object o2) {
            boolean zContains;
            synchronized (this.mutex) {
                zContains = this.backingSet.contains(o2);
            }
            return zContains;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return this.backingSet.iterator();
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public Object[] toArray() {
            Object[] array;
            synchronized (this.mutex) {
                array = this.backingSet.toArray();
            }
            return array;
        }

        @Override // java.util.Set, java.util.Collection
        public <E> E[] toArray(E[] eArr) {
            E[] eArr2;
            synchronized (this.mutex) {
                eArr2 = (E[]) this.backingSet.toArray(eArr);
            }
            return eArr2;
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public boolean add(E e2) {
            boolean zAdd;
            synchronized (this.mutex) {
                zAdd = this.backingSet.add(e2);
            }
            return zAdd;
        }

        @Override // java.util.Set
        public boolean remove(Object o2) {
            boolean zRemove;
            synchronized (this.mutex) {
                zRemove = this.backingSet.remove(o2);
            }
            return zRemove;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            boolean zContainsAll;
            synchronized (this.mutex) {
                zContainsAll = this.backingSet.containsAll(c2);
            }
            return zContainsAll;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends E> c2) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = this.backingSet.addAll(c2);
            }
            return zAddAll;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            boolean zRetainAll;
            synchronized (this.mutex) {
                zRetainAll = this.backingSet.retainAll(c2);
            }
            return zRetainAll;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            boolean zRemoveAll;
            synchronized (this.mutex) {
                zRemoveAll = this.backingSet.removeAll(c2);
            }
            return zRemoveAll;
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public void clear() {
            synchronized (this.mutex) {
                this.backingSet.clear();
            }
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public boolean equals(Object o2) {
            boolean zEquals;
            if (o2 == this) {
                return true;
            }
            synchronized (this.mutex) {
                zEquals = this.backingSet.equals(o2);
            }
            return zEquals;
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = this.backingSet.hashCode();
            }
            return iHashCode;
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SynchronizedObservableSet.class */
    private static class SynchronizedObservableSet<E> extends SynchronizedSet<E> implements ObservableSet<E> {
        private final ObservableSet<E> backingSet;
        private SetListenerHelper listenerHelper;
        private final SetChangeListener<E> listener;

        SynchronizedObservableSet(ObservableSet<E> set, Object mutex) {
            super(set, mutex);
            this.backingSet = set;
            this.listener = c2 -> {
                SetListenerHelper.fireValueChangedEvent(this.listenerHelper, new SetAdapterChange(this, c2));
            };
            this.backingSet.addListener(new WeakSetChangeListener(this.listener));
        }

        SynchronizedObservableSet(ObservableSet<E> set) {
            this(set, new Object());
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            synchronized (this.mutex) {
                this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, listener);
            }
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            synchronized (this.mutex) {
                this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, listener);
            }
        }

        @Override // javafx.collections.ObservableSet
        public void addListener(SetChangeListener<? super E> listener) {
            synchronized (this.mutex) {
                this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, listener);
            }
        }

        @Override // javafx.collections.ObservableSet
        public void removeListener(SetChangeListener<? super E> listener) {
            synchronized (this.mutex) {
                this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, listener);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$CheckedObservableSet.class */
    private static class CheckedObservableSet<E> extends AbstractSet<E> implements ObservableSet<E> {
        private final ObservableSet<E> backingSet;
        private final Class<E> type;
        private SetListenerHelper listenerHelper;
        private final SetChangeListener<E> listener;

        CheckedObservableSet(ObservableSet<E> set, Class<E> type) {
            if (set == null || type == null) {
                throw new NullPointerException();
            }
            this.backingSet = set;
            this.type = type;
            this.listener = c2 -> {
                callObservers(new SetAdapterChange(this, c2));
            };
            this.backingSet.addListener(new WeakSetChangeListener(this.listener));
        }

        private void callObservers(SetChangeListener.Change<? extends E> c2) {
            SetListenerHelper.fireValueChangedEvent(this.listenerHelper, c2);
        }

        void typeCheck(Object o2) {
            if (o2 != null && !this.type.isInstance(o2)) {
                throw new ClassCastException("Attempt to insert " + ((Object) o2.getClass()) + " element into collection with element type " + ((Object) this.type));
            }
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableSet
        public void addListener(SetChangeListener<? super E> listener) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableSet
        public void removeListener(SetChangeListener<? super E> listener) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, listener);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.backingSet.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.backingSet.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            return this.backingSet.contains(o2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return this.backingSet.toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.backingSet.toArray(tArr);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(E e2) {
            typeCheck(e2);
            return this.backingSet.add(e2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            return this.backingSet.remove(o2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            return this.backingSet.containsAll(c2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> c2) {
            try {
                return this.backingSet.addAll(Arrays.asList(c2.toArray((Object[]) Array.newInstance((Class<?>) this.type, 0))));
            } catch (ArrayStoreException e2) {
                throw new ClassCastException();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            return this.backingSet.retainAll(c2);
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            return this.backingSet.removeAll(c2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.backingSet.clear();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.List
        public boolean equals(Object o2) {
            return o2 == this || this.backingSet.equals(o2);
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.List
        public int hashCode() {
            return this.backingSet.hashCode();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            final Iterator<E> it = this.backingSet.iterator();
            return new Iterator<E>() { // from class: javafx.collections.FXCollections.CheckedObservableSet.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override // java.util.Iterator
                public E next() {
                    return (E) it.next();
                }

                @Override // java.util.Iterator
                public void remove() {
                    it.remove();
                }
            };
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$EmptyObservableMap.class */
    private static class EmptyObservableMap<K, V> extends AbstractMap<K, V> implements ObservableMap<K, V> {
        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
        }

        @Override // javafx.collections.ObservableMap
        public void addListener(MapChangeListener<? super K, ? super V> listener) {
        }

        @Override // javafx.collections.ObservableMap
        public void removeListener(MapChangeListener<? super K, ? super V> listener) {
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return 0;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return true;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object value) {
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object key) {
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return FXCollections.emptyObservableSet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            return FXCollections.emptyObservableSet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            return FXCollections.emptyObservableSet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean equals(Object o2) {
            return (o2 instanceof Map) && ((Map) o2).isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int hashCode() {
            return 0;
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$CheckedObservableMap.class */
    private static class CheckedObservableMap<K, V> extends AbstractMap<K, V> implements ObservableMap<K, V> {
        private final ObservableMap<K, V> backingMap;
        private final Class<K> keyType;
        private final Class<V> valueType;
        private MapListenerHelper listenerHelper;
        private transient Set<Map.Entry<K, V>> entrySet = null;
        private final MapChangeListener<K, V> listener = c2 -> {
            callObservers(new MapAdapterChange(this, c2));
        };

        CheckedObservableMap(ObservableMap<K, V> map, Class<K> keyType, Class<V> valueType) {
            this.backingMap = map;
            this.keyType = keyType;
            this.valueType = valueType;
            this.backingMap.addListener(new WeakMapChangeListener(this.listener));
        }

        private void callObservers(MapChangeListener.Change<? extends K, ? extends V> c2) {
            MapListenerHelper.fireValueChangedEvent(this.listenerHelper, c2);
        }

        void typeCheck(Object key, Object value) {
            if (key != null && !this.keyType.isInstance(key)) {
                throw new ClassCastException("Attempt to insert " + ((Object) key.getClass()) + " key into map with key type " + ((Object) this.keyType));
            }
            if (value != null && !this.valueType.isInstance(value)) {
                throw new ClassCastException("Attempt to insert " + ((Object) value.getClass()) + " value into map with value type " + ((Object) this.valueType));
            }
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableMap
        public void addListener(MapChangeListener<? super K, ? super V> listener) {
            this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableMap
        public void removeListener(MapChangeListener<? super K, ? super V> listener) {
            this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, listener);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.backingMap.size();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return this.backingMap.isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            return this.backingMap.containsKey(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object value) {
            return this.backingMap.containsValue(value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object key) {
            return this.backingMap.get(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K key, V value) {
            typeCheck(key, value);
            return this.backingMap.put(key, value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(Object key) {
            return this.backingMap.remove(key);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractMap, java.util.Map
        public void putAll(Map map) {
            Object[] array = map.entrySet().toArray();
            ArrayList<Map.Entry> arrayList = new ArrayList(array.length);
            for (Object obj : array) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object value = entry.getValue();
                typeCheck(key, value);
                arrayList.add(new AbstractMap.SimpleImmutableEntry(key, value));
            }
            for (Map.Entry entry2 : arrayList) {
                this.backingMap.put(entry2.getKey(), entry2.getValue());
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            this.backingMap.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return this.backingMap.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            return this.backingMap.values();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set entrySet() {
            if (this.entrySet == null) {
                this.entrySet = new CheckedEntrySet(this.backingMap.entrySet(), this.valueType);
            }
            return this.entrySet;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean equals(Object o2) {
            return o2 == this || this.backingMap.equals(o2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int hashCode() {
            return this.backingMap.hashCode();
        }

        /* loaded from: jfxrt.jar:javafx/collections/FXCollections$CheckedObservableMap$CheckedEntrySet.class */
        static class CheckedEntrySet<K, V> implements Set<Map.Entry<K, V>> {

            /* renamed from: s, reason: collision with root package name */
            private final Set<Map.Entry<K, V>> f12627s;
            private final Class<V> valueType;

            CheckedEntrySet(Set<Map.Entry<K, V>> s2, Class<V> valueType) {
                this.f12627s = s2;
                this.valueType = valueType;
            }

            @Override // java.util.Set
            public int size() {
                return this.f12627s.size();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean isEmpty() {
                return this.f12627s.isEmpty();
            }

            public String toString() {
                return this.f12627s.toString();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public int hashCode() {
                return this.f12627s.hashCode();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public void clear() {
                this.f12627s.clear();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public boolean add(Map.Entry<K, V> e2) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean addAll(Collection<? extends Map.Entry<K, V>> coll) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
            public Iterator<Map.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> i2 = this.f12627s.iterator();
                final Class<V> valueType = this.valueType;
                return new Iterator<Map.Entry<K, V>>() { // from class: javafx.collections.FXCollections.CheckedObservableMap.CheckedEntrySet.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return i2.hasNext();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        i2.remove();
                    }

                    @Override // java.util.Iterator
                    public Map.Entry<K, V> next() {
                        return CheckedEntrySet.checkedEntry((Map.Entry) i2.next(), valueType);
                    }
                };
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public Object[] toArray() {
                Object[] source = this.f12627s.toArray();
                Object[] dest = CheckedEntry.class.isInstance(source.getClass().getComponentType()) ? source : new Object[source.length];
                for (int i2 = 0; i2 < source.length; i2++) {
                    dest[i2] = checkedEntry((Map.Entry) source[i2], this.valueType);
                }
                return dest;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Set, java.util.Collection
            public <T> T[] toArray(T[] tArr) {
                T[] tArr2 = (T[]) this.f12627s.toArray(tArr.length == 0 ? tArr : Arrays.copyOf(tArr, 0));
                for (int i2 = 0; i2 < tArr2.length; i2++) {
                    tArr2[i2] = checkedEntry((Map.Entry) tArr2[i2], this.valueType);
                }
                if (tArr2.length > tArr.length) {
                    return tArr2;
                }
                System.arraycopy(tArr2, 0, tArr, 0, tArr2.length);
                if (tArr.length > tArr2.length) {
                    tArr[tArr2.length] = null;
                }
                return tArr;
            }

            @Override // java.util.Set
            public boolean contains(Object o2) {
                if (!(o2 instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<?, ?> e2 = (Map.Entry) o2;
                return this.f12627s.contains(e2 instanceof CheckedEntry ? e2 : checkedEntry(e2, this.valueType));
            }

            @Override // java.util.Set, java.util.Collection
            public boolean containsAll(Collection<?> c2) {
                for (Object o2 : c2) {
                    if (!contains(o2)) {
                        return false;
                    }
                }
                return true;
            }

            @Override // java.util.Set
            public boolean remove(Object o2) {
                if (!(o2 instanceof Map.Entry)) {
                    return false;
                }
                return this.f12627s.remove(new AbstractMap.SimpleImmutableEntry((Map.Entry) o2));
            }

            @Override // java.util.Set, java.util.Collection
            public boolean removeAll(Collection<?> c2) {
                return batchRemove(c2, false);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean retainAll(Collection<?> c2) {
                return batchRemove(c2, true);
            }

            private boolean batchRemove(Collection<?> c2, boolean complement) {
                boolean modified = false;
                Iterator<Map.Entry<K, V>> it = iterator();
                while (it.hasNext()) {
                    if (c2.contains(it.next()) != complement) {
                        it.remove();
                        modified = true;
                    }
                }
                return modified;
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public boolean equals(Object o2) {
                if (o2 == this) {
                    return true;
                }
                if (!(o2 instanceof Set)) {
                    return false;
                }
                Set<?> that = (Set) o2;
                return that.size() == this.f12627s.size() && containsAll(that);
            }

            static <K, V, T> CheckedEntry<K, V, T> checkedEntry(Map.Entry<K, V> e2, Class<T> valueType) {
                return new CheckedEntry<>(e2, valueType);
            }

            /* loaded from: jfxrt.jar:javafx/collections/FXCollections$CheckedObservableMap$CheckedEntrySet$CheckedEntry.class */
            private static class CheckedEntry<K, V, T> implements Map.Entry<K, V> {

                /* renamed from: e, reason: collision with root package name */
                private final Map.Entry<K, V> f12628e;
                private final Class<T> valueType;

                CheckedEntry(Map.Entry<K, V> e2, Class<T> valueType) {
                    this.f12628e = e2;
                    this.valueType = valueType;
                }

                @Override // java.util.Map.Entry
                public K getKey() {
                    return this.f12628e.getKey();
                }

                @Override // java.util.Map.Entry
                public V getValue() {
                    return this.f12628e.getValue();
                }

                @Override // java.util.Map.Entry
                public int hashCode() {
                    return this.f12628e.hashCode();
                }

                public String toString() {
                    return this.f12628e.toString();
                }

                @Override // java.util.Map.Entry
                public V setValue(V value) {
                    if (value != null && !this.valueType.isInstance(value)) {
                        throw new ClassCastException(badValueMsg(value));
                    }
                    return this.f12628e.setValue(value);
                }

                private String badValueMsg(Object value) {
                    return "Attempt to insert " + ((Object) value.getClass()) + " value into map with value type " + ((Object) this.valueType);
                }

                @Override // java.util.Map.Entry
                public boolean equals(Object o2) {
                    if (o2 == this) {
                        return true;
                    }
                    if (!(o2 instanceof Map.Entry)) {
                        return false;
                    }
                    return this.f12628e.equals(new AbstractMap.SimpleImmutableEntry((Map.Entry) o2));
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SynchronizedMap.class */
    private static class SynchronizedMap<K, V> implements Map<K, V> {
        final Object mutex;
        private final Map<K, V> backingMap;
        private transient Set<K> keySet;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Collection<V> values;

        SynchronizedMap(Map<K, V> map, Object mutex) {
            this.keySet = null;
            this.entrySet = null;
            this.values = null;
            this.backingMap = map;
            this.mutex = mutex;
        }

        SynchronizedMap(Map<K, V> map) {
            this(map, new Object());
        }

        @Override // java.util.Map
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.backingMap.size();
            }
            return size;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = this.backingMap.isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.Map
        public boolean containsKey(Object key) {
            boolean zContainsKey;
            synchronized (this.mutex) {
                zContainsKey = this.backingMap.containsKey(key);
            }
            return zContainsKey;
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            boolean zContainsValue;
            synchronized (this.mutex) {
                zContainsValue = this.backingMap.containsValue(value);
            }
            return zContainsValue;
        }

        @Override // java.util.Map
        public V get(Object key) {
            V v2;
            synchronized (this.mutex) {
                v2 = this.backingMap.get(key);
            }
            return v2;
        }

        @Override // java.util.Map
        public V put(K key, V value) {
            V vPut;
            synchronized (this.mutex) {
                vPut = this.backingMap.put(key, value);
            }
            return vPut;
        }

        @Override // java.util.Map
        public V remove(Object key) {
            V vRemove;
            synchronized (this.mutex) {
                vRemove = this.backingMap.remove(key);
            }
            return vRemove;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends V> m2) {
            synchronized (this.mutex) {
                this.backingMap.putAll(m2);
            }
        }

        @Override // java.util.Map
        public void clear() {
            synchronized (this.mutex) {
                this.backingMap.clear();
            }
        }

        @Override // java.util.Map
        public Set<K> keySet() {
            Set<K> set;
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = new SynchronizedSet(this.backingMap.keySet(), this.mutex);
                }
                set = this.keySet;
            }
            return set;
        }

        @Override // java.util.Map
        public Collection<V> values() {
            Collection<V> collection;
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = new SynchronizedCollection(this.backingMap.values(), this.mutex);
                }
                collection = this.values;
            }
            return collection;
        }

        @Override // java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set;
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = new SynchronizedSet(this.backingMap.entrySet(), this.mutex);
                }
                set = this.entrySet;
            }
            return set;
        }

        @Override // java.util.Map
        public boolean equals(Object o2) {
            boolean zEquals;
            if (o2 == this) {
                return true;
            }
            synchronized (this.mutex) {
                zEquals = this.backingMap.equals(o2);
            }
            return zEquals;
        }

        @Override // java.util.Map
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = this.backingMap.hashCode();
            }
            return iHashCode;
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SynchronizedCollection.class */
    private static class SynchronizedCollection<E> implements Collection<E> {
        private final Collection<E> backingCollection;
        final Object mutex;

        SynchronizedCollection(Collection<E> c2, Object mutex) {
            this.backingCollection = c2;
            this.mutex = mutex;
        }

        SynchronizedCollection(Collection<E> c2) {
            this(c2, new Object());
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.backingCollection.size();
            }
            return size;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = this.backingCollection.isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            boolean zContains;
            synchronized (this.mutex) {
                zContains = this.backingCollection.contains(o2);
            }
            return zContains;
        }

        @Override // java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return this.backingCollection.iterator();
        }

        @Override // java.util.Collection, java.util.List
        public Object[] toArray() {
            Object[] array;
            synchronized (this.mutex) {
                array = this.backingCollection.toArray();
            }
            return array;
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            T[] tArr2;
            synchronized (this.mutex) {
                tArr2 = (T[]) this.backingCollection.toArray(tArr);
            }
            return tArr2;
        }

        @Override // java.util.Collection, java.util.List
        public boolean add(E e2) {
            boolean zAdd;
            synchronized (this.mutex) {
                zAdd = this.backingCollection.add(e2);
            }
            return zAdd;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            boolean zRemove;
            synchronized (this.mutex) {
                zRemove = this.backingCollection.remove(o2);
            }
            return zRemove;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            boolean zContainsAll;
            synchronized (this.mutex) {
                zContainsAll = this.backingCollection.containsAll(c2);
            }
            return zContainsAll;
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends E> c2) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = this.backingCollection.addAll(c2);
            }
            return zAddAll;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            boolean zRemoveAll;
            synchronized (this.mutex) {
                zRemoveAll = this.backingCollection.removeAll(c2);
            }
            return zRemoveAll;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            boolean zRetainAll;
            synchronized (this.mutex) {
                zRetainAll = this.backingCollection.retainAll(c2);
            }
            return zRetainAll;
        }

        @Override // java.util.Collection, java.util.List
        public void clear() {
            synchronized (this.mutex) {
                this.backingCollection.clear();
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/FXCollections$SynchronizedObservableMap.class */
    private static class SynchronizedObservableMap<K, V> extends SynchronizedMap<K, V> implements ObservableMap<K, V> {
        private final ObservableMap<K, V> backingMap;
        private MapListenerHelper listenerHelper;
        private final MapChangeListener<K, V> listener;

        SynchronizedObservableMap(ObservableMap<K, V> map, Object mutex) {
            super(map, mutex);
            this.backingMap = map;
            this.listener = c2 -> {
                MapListenerHelper.fireValueChangedEvent(this.listenerHelper, new MapAdapterChange(this, c2));
            };
            this.backingMap.addListener(new WeakMapChangeListener(this.listener));
        }

        SynchronizedObservableMap(ObservableMap<K, V> map) {
            this(map, new Object());
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            synchronized (this.mutex) {
                this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, listener);
            }
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            synchronized (this.mutex) {
                this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, listener);
            }
        }

        @Override // javafx.collections.ObservableMap
        public void addListener(MapChangeListener<? super K, ? super V> listener) {
            synchronized (this.mutex) {
                this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, listener);
            }
        }

        @Override // javafx.collections.ObservableMap
        public void removeListener(MapChangeListener<? super K, ? super V> listener) {
            synchronized (this.mutex) {
                this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, listener);
            }
        }
    }
}
