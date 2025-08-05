package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.icepdf.core.util.PdfOps;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/Collections.class */
public class Collections {
    private static final int BINARYSEARCH_THRESHOLD = 5000;
    private static final int REVERSE_THRESHOLD = 18;
    private static final int SHUFFLE_THRESHOLD = 5;
    private static final int FILL_THRESHOLD = 25;
    private static final int ROTATE_THRESHOLD = 100;
    private static final int COPY_THRESHOLD = 10;
    private static final int REPLACEALL_THRESHOLD = 11;
    private static final int INDEXOFSUBLIST_THRESHOLD = 35;

    /* renamed from: r, reason: collision with root package name */
    private static Random f12528r;
    public static final Set EMPTY_SET = new EmptySet();
    public static final List EMPTY_LIST = new EmptyList();
    public static final Map EMPTY_MAP = new EmptyMap();

    private Collections() {
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        list.sort(null);
    }

    public static <T> void sort(List<T> list, Comparator<? super T> comparator) {
        list.sort(comparator);
    }

    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T t2) {
        if ((list instanceof RandomAccess) || list.size() < 5000) {
            return indexedBinarySearch(list, t2);
        }
        return iteratorBinarySearch(list, t2);
    }

    private static <T> int indexedBinarySearch(List<? extends Comparable<? super T>> list, T t2) {
        int i2 = 0;
        int size = list.size() - 1;
        while (i2 <= size) {
            int i3 = (i2 + size) >>> 1;
            int iCompareTo = list.get(i3).compareTo(t2);
            if (iCompareTo < 0) {
                i2 = i3 + 1;
            } else if (iCompareTo > 0) {
                size = i3 - 1;
            } else {
                return i3;
            }
        }
        return -(i2 + 1);
    }

    private static <T> int iteratorBinarySearch(List<? extends Comparable<? super T>> list, T t2) {
        int i2 = 0;
        int size = list.size() - 1;
        ListIterator<? extends Comparable<? super T>> listIterator = list.listIterator();
        while (i2 <= size) {
            int i3 = (i2 + size) >>> 1;
            int iCompareTo = ((Comparable) get(listIterator, i3)).compareTo(t2);
            if (iCompareTo < 0) {
                i2 = i3 + 1;
            } else if (iCompareTo > 0) {
                size = i3 - 1;
            } else {
                return i3;
            }
        }
        return -(i2 + 1);
    }

    private static <T> T get(ListIterator<? extends T> listIterator, int i2) {
        T tPrevious;
        int i3;
        int iNextIndex = listIterator.nextIndex();
        if (iNextIndex <= i2) {
            do {
                tPrevious = listIterator.next();
                i3 = iNextIndex;
                iNextIndex++;
            } while (i3 < i2);
        } else {
            do {
                tPrevious = listIterator.previous();
                iNextIndex--;
            } while (iNextIndex > i2);
        }
        return tPrevious;
    }

    public static <T> int binarySearch(List<? extends T> list, T t2, Comparator<? super T> comparator) {
        if (comparator == null) {
            return binarySearch(list, t2);
        }
        if ((list instanceof RandomAccess) || list.size() < 5000) {
            return indexedBinarySearch(list, t2, comparator);
        }
        return iteratorBinarySearch(list, t2, comparator);
    }

    private static <T> int indexedBinarySearch(List<? extends T> list, T t2, Comparator<? super T> comparator) {
        int i2 = 0;
        int size = list.size() - 1;
        while (i2 <= size) {
            int i3 = (i2 + size) >>> 1;
            int iCompare = comparator.compare(list.get(i3), t2);
            if (iCompare < 0) {
                i2 = i3 + 1;
            } else if (iCompare > 0) {
                size = i3 - 1;
            } else {
                return i3;
            }
        }
        return -(i2 + 1);
    }

    private static <T> int iteratorBinarySearch(List<? extends T> list, T t2, Comparator<? super T> comparator) {
        int i2 = 0;
        int size = list.size() - 1;
        ListIterator<? extends T> listIterator = list.listIterator();
        while (i2 <= size) {
            int i3 = (i2 + size) >>> 1;
            int iCompare = comparator.compare((Object) get(listIterator, i3), t2);
            if (iCompare < 0) {
                i2 = i3 + 1;
            } else if (iCompare > 0) {
                size = i3 - 1;
            } else {
                return i3;
            }
        }
        return -(i2 + 1);
    }

    public static void reverse(List<?> list) {
        int size = list.size();
        if (size < 18 || (list instanceof RandomAccess)) {
            int i2 = 0;
            int i3 = size >> 1;
            int i4 = size - 1;
            while (i2 < i3) {
                swap(list, i2, i4);
                i2++;
                i4--;
            }
            return;
        }
        ListIterator<?> listIterator = list.listIterator();
        ListIterator<?> listIterator2 = list.listIterator(size);
        int size2 = list.size() >> 1;
        for (int i5 = 0; i5 < size2; i5++) {
            Object next = listIterator.next();
            listIterator.set(listIterator2.previous());
            listIterator2.set(next);
        }
    }

    public static void shuffle(List<?> list) {
        Random random = f12528r;
        if (random == null) {
            Random random2 = new Random();
            random = random2;
            f12528r = random2;
        }
        shuffle(list, random);
    }

    public static void shuffle(List<?> list, Random random) {
        if (list.size() < 5 || (list instanceof RandomAccess)) {
            for (int i2 = r0; i2 > 1; i2--) {
                swap(list, i2 - 1, random.nextInt(i2));
            }
            return;
        }
        Object[] array = list.toArray();
        for (int i3 = r0; i3 > 1; i3--) {
            swap(array, i3 - 1, random.nextInt(i3));
        }
        ListIterator<?> listIterator = list.listIterator();
        for (Object obj : array) {
            listIterator.next();
            listIterator.set(obj);
        }
    }

    public static void swap(List<?> list, int i2, int i3) {
        list.set(i2, list.set(i3, list.get(i2)));
    }

    private static void swap(Object[] objArr, int i2, int i3) {
        Object obj = objArr[i2];
        objArr[i2] = objArr[i3];
        objArr[i3] = obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> void fill(List<? super T> list, T t2) {
        int size = list.size();
        if (size < 25 || (list instanceof RandomAccess)) {
            for (int i2 = 0; i2 < size; i2++) {
                list.set(i2, t2);
            }
            return;
        }
        ListIterator<? super T> listIterator = list.listIterator();
        for (int i3 = 0; i3 < size; i3++) {
            listIterator.next();
            listIterator.set(t2);
        }
    }

    public static <T> void copy(List<? super T> list, List<? extends T> list2) {
        int size = list2.size();
        if (size > list.size()) {
            throw new IndexOutOfBoundsException("Source does not fit in dest");
        }
        if (size < 10 || ((list2 instanceof RandomAccess) && (list instanceof RandomAccess))) {
            for (int i2 = 0; i2 < size; i2++) {
                list.set(i2, list2.get(i2));
            }
            return;
        }
        ListIterator<? super T> listIterator = list.listIterator();
        ListIterator<? extends T> listIterator2 = list2.listIterator();
        for (int i3 = 0; i3 < size; i3++) {
            listIterator.next();
            listIterator.set(listIterator2.next());
        }
    }

    public static <T extends Comparable<? super T>> T min(Collection<? extends T> collection) {
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (next2.compareTo(next) < 0) {
                next = next2;
            }
        }
        return next;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Object] */
    public static <T> T min(Collection<? extends T> collection, Comparator<? super T> comparator) {
        if (comparator == null) {
            return (T) min(collection);
        }
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (comparator.compare(next2, (Object) next) < 0) {
                next = next2;
            }
        }
        return next;
    }

    public static <T extends Comparable<? super T>> T max(Collection<? extends T> collection) {
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (next2.compareTo(next) > 0) {
                next = next2;
            }
        }
        return next;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Object] */
    public static <T> T max(Collection<? extends T> collection, Comparator<? super T> comparator) {
        if (comparator == null) {
            return (T) max(collection);
        }
        Iterator<? extends T> it = collection.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (comparator.compare(next2, (Object) next) > 0) {
                next = next2;
            }
        }
        return next;
    }

    public static void rotate(List<?> list, int i2) {
        if ((list instanceof RandomAccess) || list.size() < 100) {
            rotate1(list, i2);
        } else {
            rotate2(list, i2);
        }
    }

    private static <T> void rotate1(List<T> list, int i2) {
        int size = list.size();
        if (size == 0) {
            return;
        }
        int i3 = i2 % size;
        if (i3 < 0) {
            i3 += size;
        }
        if (i3 == 0) {
            return;
        }
        int i4 = 0;
        int i5 = 0;
        while (i5 != size) {
            T t2 = list.get(i4);
            int i6 = i4;
            do {
                i6 += i3;
                if (i6 >= size) {
                    i6 -= size;
                }
                t2 = list.set(i6, t2);
                i5++;
            } while (i6 != i4);
            i4++;
        }
    }

    private static void rotate2(List<?> list, int i2) {
        int size = list.size();
        if (size == 0) {
            return;
        }
        int i3 = (-i2) % size;
        if (i3 < 0) {
            i3 += size;
        }
        if (i3 == 0) {
            return;
        }
        reverse(list.subList(0, i3));
        reverse(list.subList(i3, size));
        reverse(list);
    }

    public static <T> boolean replaceAll(List<T> list, T t2, T t3) {
        boolean z2 = false;
        int size = list.size();
        if (size < 11 || (list instanceof RandomAccess)) {
            if (t2 == null) {
                for (int i2 = 0; i2 < size; i2++) {
                    if (list.get(i2) == null) {
                        list.set(i2, t3);
                        z2 = true;
                    }
                }
            } else {
                for (int i3 = 0; i3 < size; i3++) {
                    if (t2.equals(list.get(i3))) {
                        list.set(i3, t3);
                        z2 = true;
                    }
                }
            }
        } else {
            ListIterator<T> listIterator = list.listIterator();
            if (t2 == null) {
                for (int i4 = 0; i4 < size; i4++) {
                    if (listIterator.next() == null) {
                        listIterator.set(t3);
                        z2 = true;
                    }
                }
            } else {
                for (int i5 = 0; i5 < size; i5++) {
                    if (t2.equals(listIterator.next())) {
                        listIterator.set(t3);
                        z2 = true;
                    }
                }
            }
        }
        return z2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0063, code lost:
    
        r9 = r9 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int indexOfSubList(java.util.List<?> r4, java.util.List<?> r5) {
        /*
            r0 = r4
            int r0 = r0.size()
            r6 = r0
            r0 = r5
            int r0 = r0.size()
            r7 = r0
            r0 = r6
            r1 = r7
            int r0 = r0 - r1
            r8 = r0
            r0 = r6
            r1 = 35
            if (r0 < r1) goto L27
            r0 = r4
            boolean r0 = r0 instanceof java.util.RandomAccess
            if (r0 == 0) goto L6c
            r0 = r5
            boolean r0 = r0 instanceof java.util.RandomAccess
            if (r0 == 0) goto L6c
        L27:
            r0 = 0
            r9 = r0
        L2a:
            r0 = r9
            r1 = r8
            if (r0 > r1) goto L69
            r0 = 0
            r10 = r0
            r0 = r9
            r11 = r0
        L38:
            r0 = r10
            r1 = r7
            if (r0 >= r1) goto L60
            r0 = r5
            r1 = r10
            java.lang.Object r0 = r0.get(r1)
            r1 = r4
            r2 = r11
            java.lang.Object r1 = r1.get(r2)
            boolean r0 = eq(r0, r1)
            if (r0 != 0) goto L57
            goto L63
        L57:
            int r10 = r10 + 1
            int r11 = r11 + 1
            goto L38
        L60:
            r0 = r9
            return r0
        L63:
            int r9 = r9 + 1
            goto L2a
        L69:
            goto Lcd
        L6c:
            r0 = r4
            java.util.ListIterator r0 = r0.listIterator()
            r9 = r0
            r0 = 0
            r10 = r0
        L77:
            r0 = r10
            r1 = r8
            if (r0 > r1) goto Lcd
            r0 = r5
            java.util.ListIterator r0 = r0.listIterator()
            r11 = r0
            r0 = 0
            r12 = r0
        L89:
            r0 = r12
            r1 = r7
            if (r0 >= r1) goto Lc4
            r0 = r11
            java.lang.Object r0 = r0.next()
            r1 = r9
            java.lang.Object r1 = r1.next()
            boolean r0 = eq(r0, r1)
            if (r0 != 0) goto Lbe
            r0 = 0
            r13 = r0
        La6:
            r0 = r13
            r1 = r12
            if (r0 >= r1) goto Lbb
            r0 = r9
            java.lang.Object r0 = r0.previous()
            int r13 = r13 + 1
            goto La6
        Lbb:
            goto Lc7
        Lbe:
            int r12 = r12 + 1
            goto L89
        Lc4:
            r0 = r10
            return r0
        Lc7:
            int r10 = r10 + 1
            goto L77
        Lcd:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Collections.indexOfSubList(java.util.List, java.util.List):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x005b, code lost:
    
        r9 = r9 - 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int lastIndexOfSubList(java.util.List<?> r4, java.util.List<?> r5) {
        /*
            r0 = r4
            int r0 = r0.size()
            r6 = r0
            r0 = r5
            int r0 = r0.size()
            r7 = r0
            r0 = r6
            r1 = r7
            int r0 = r0 - r1
            r8 = r0
            r0 = r6
            r1 = 35
            if (r0 < r1) goto L20
            r0 = r4
            boolean r0 = r0 instanceof java.util.RandomAccess
            if (r0 == 0) goto L64
        L20:
            r0 = r8
            r9 = r0
        L24:
            r0 = r9
            if (r0 < 0) goto L61
            r0 = 0
            r10 = r0
            r0 = r9
            r11 = r0
        L30:
            r0 = r10
            r1 = r7
            if (r0 >= r1) goto L58
            r0 = r5
            r1 = r10
            java.lang.Object r0 = r0.get(r1)
            r1 = r4
            r2 = r11
            java.lang.Object r1 = r1.get(r2)
            boolean r0 = eq(r0, r1)
            if (r0 != 0) goto L4f
            goto L5b
        L4f:
            int r10 = r10 + 1
            int r11 = r11 + 1
            goto L30
        L58:
            r0 = r9
            return r0
        L5b:
            int r9 = r9 + (-1)
            goto L24
        L61:
            goto Ld4
        L64:
            r0 = r8
            if (r0 >= 0) goto L6b
            r0 = -1
            return r0
        L6b:
            r0 = r4
            r1 = r8
            java.util.ListIterator r0 = r0.listIterator(r1)
            r9 = r0
            r0 = r8
            r10 = r0
        L79:
            r0 = r10
            if (r0 < 0) goto Ld4
            r0 = r5
            java.util.ListIterator r0 = r0.listIterator()
            r11 = r0
            r0 = 0
            r12 = r0
        L89:
            r0 = r12
            r1 = r7
            if (r0 >= r1) goto Lcb
            r0 = r11
            java.lang.Object r0 = r0.next()
            r1 = r9
            java.lang.Object r1 = r1.next()
            boolean r0 = eq(r0, r1)
            if (r0 != 0) goto Lc5
            r0 = r10
            if (r0 == 0) goto Lce
            r0 = 0
            r13 = r0
        Lab:
            r0 = r13
            r1 = r12
            r2 = 1
            int r1 = r1 + r2
            if (r0 > r1) goto Lc2
            r0 = r9
            java.lang.Object r0 = r0.previous()
            int r13 = r13 + 1
            goto Lab
        Lc2:
            goto Lce
        Lc5:
            int r12 = r12 + 1
            goto L89
        Lcb:
            r0 = r10
            return r0
        Lce:
            int r10 = r10 + (-1)
            goto L79
        Ld4:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Collections.lastIndexOfSubList(java.util.List, java.util.List):int");
    }

    public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> collection) {
        return new UnmodifiableCollection(collection);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableCollection.class */
    static class UnmodifiableCollection<E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 1820017752578914078L;

        /* renamed from: c, reason: collision with root package name */
        final Collection<? extends E> f12542c;

        UnmodifiableCollection(Collection<? extends E> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            this.f12542c = collection;
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            return this.f12542c.size();
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return this.f12542c.isEmpty();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12542c.contains(obj);
        }

        @Override // java.util.Collection, java.util.List
        public Object[] toArray() {
            return this.f12542c.toArray();
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.f12542c.toArray(tArr);
        }

        public String toString() {
            return this.f12542c.toString();
        }

        @Override // java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return new Iterator<E>() { // from class: java.util.Collections.UnmodifiableCollection.1

                /* renamed from: i, reason: collision with root package name */
                private final Iterator<? extends E> f12543i;

                {
                    this.f12543i = UnmodifiableCollection.this.f12542c.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.f12543i.hasNext();
                }

                @Override // java.util.Iterator
                public E next() {
                    return this.f12543i.next();
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super E> consumer) {
                    this.f12543i.forEachRemaining(consumer);
                }
            };
        }

        @Override // java.util.Collection, java.util.List
        public boolean add(E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return this.f12542c.containsAll(collection);
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, java.util.List
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            this.f12542c.forEach(consumer);
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return this.f12542c.spliterator();
        }

        @Override // java.util.Collection
        public Stream<E> stream() {
            return this.f12542c.stream();
        }

        @Override // java.util.Collection
        public Stream<E> parallelStream() {
            return this.f12542c.parallelStream();
        }
    }

    public static <T> Set<T> unmodifiableSet(Set<? extends T> set) {
        return new UnmodifiableSet(set);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableSet.class */
    static class UnmodifiableSet<E> extends UnmodifiableCollection<E> implements Set<E>, Serializable {
        private static final long serialVersionUID = -9215047833775013803L;

        UnmodifiableSet(Set<? extends E> set) {
            super(set);
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return obj == this || this.f12542c.equals(obj);
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            return this.f12542c.hashCode();
        }
    }

    public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> sortedSet) {
        return new UnmodifiableSortedSet(sortedSet);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableSortedSet.class */
    static class UnmodifiableSortedSet<E> extends UnmodifiableSet<E> implements SortedSet<E>, Serializable {
        private static final long serialVersionUID = -4929149591599911165L;
        private final SortedSet<E> ss;

        UnmodifiableSortedSet(SortedSet<E> sortedSet) {
            super(sortedSet);
            this.ss = sortedSet;
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            return this.ss.comparator();
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public SortedSet<E> subSet(E e2, E e3) {
            return new UnmodifiableSortedSet(this.ss.subSet(e2, e3));
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public SortedSet<E> headSet(E e2) {
            return new UnmodifiableSortedSet(this.ss.headSet(e2));
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public SortedSet<E> tailSet(E e2) {
            return new UnmodifiableSortedSet(this.ss.tailSet(e2));
        }

        @Override // java.util.SortedSet
        public E first() {
            return this.ss.first();
        }

        @Override // java.util.SortedSet
        public E last() {
            return this.ss.last();
        }
    }

    public static <T> NavigableSet<T> unmodifiableNavigableSet(NavigableSet<T> navigableSet) {
        return new UnmodifiableNavigableSet(navigableSet);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableNavigableSet.class */
    static class UnmodifiableNavigableSet<E> extends UnmodifiableSortedSet<E> implements NavigableSet<E>, Serializable {
        private static final long serialVersionUID = -6027448201786391929L;
        private static final NavigableSet<?> EMPTY_NAVIGABLE_SET = new EmptyNavigableSet();
        private final NavigableSet<E> ns;

        /* loaded from: rt.jar:java/util/Collections$UnmodifiableNavigableSet$EmptyNavigableSet.class */
        private static class EmptyNavigableSet<E> extends UnmodifiableNavigableSet<E> implements Serializable {
            private static final long serialVersionUID = -6291252904449939134L;

            public EmptyNavigableSet() {
                super(new TreeSet());
            }

            private Object readResolve() {
                return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
            }
        }

        UnmodifiableNavigableSet(NavigableSet<E> navigableSet) {
            super(navigableSet);
            this.ns = navigableSet;
        }

        @Override // java.util.NavigableSet
        public E lower(E e2) {
            return this.ns.lower(e2);
        }

        @Override // java.util.NavigableSet
        public E floor(E e2) {
            return this.ns.floor(e2);
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e2) {
            return this.ns.ceiling(e2);
        }

        @Override // java.util.NavigableSet
        public E higher(E e2) {
            return this.ns.higher(e2);
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            return new UnmodifiableNavigableSet(this.ns.descendingSet());
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3) {
            return new UnmodifiableNavigableSet(this.ns.subSet(e2, z2, e3, z3));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e2, boolean z2) {
            return new UnmodifiableNavigableSet(this.ns.headSet(e2, z2));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2, boolean z2) {
            return new UnmodifiableNavigableSet(this.ns.tailSet(e2, z2));
        }
    }

    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return list instanceof RandomAccess ? new UnmodifiableRandomAccessList(list) : new UnmodifiableList(list);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableList.class */
    static class UnmodifiableList<E> extends UnmodifiableCollection<E> implements List<E> {
        private static final long serialVersionUID = -283967356065247728L;
        final List<? extends E> list;

        UnmodifiableList(List<? extends E> list) {
            super(list);
            this.list = list;
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return obj == this || this.list.equals(obj);
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            return this.list.hashCode();
        }

        @Override // java.util.List
        public E get(int i2) {
            return this.list.get(i2);
        }

        @Override // java.util.List
        public E set(int i2, E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void add(int i2, E e2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public E remove(int i2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public int indexOf(Object obj) {
            return this.list.indexOf(obj);
        }

        @Override // java.util.List
        public int lastIndexOf(Object obj) {
            return this.list.lastIndexOf(obj);
        }

        @Override // java.util.List
        public boolean addAll(int i2, Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<E> unaryOperator) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List, com.sun.javafx.collections.SortableList
        public void sort(Comparator<? super E> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        @Override // java.util.List
        public ListIterator<E> listIterator(final int i2) {
            return new ListIterator<E>() { // from class: java.util.Collections.UnmodifiableList.1

                /* renamed from: i, reason: collision with root package name */
                private final ListIterator<? extends E> f12544i;

                {
                    this.f12544i = UnmodifiableList.this.list.listIterator(i2);
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return this.f12544i.hasNext();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public E next() {
                    return this.f12544i.next();
                }

                @Override // java.util.ListIterator
                public boolean hasPrevious() {
                    return this.f12544i.hasPrevious();
                }

                @Override // java.util.ListIterator
                public E previous() {
                    return this.f12544i.previous();
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return this.f12544i.nextIndex();
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return this.f12544i.previousIndex();
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
                    this.f12544i.forEachRemaining(consumer);
                }
            };
        }

        @Override // java.util.List
        public List<E> subList(int i2, int i3) {
            return new UnmodifiableList(this.list.subList(i2, i3));
        }

        private Object readResolve() {
            return this.list instanceof RandomAccess ? new UnmodifiableRandomAccessList(this.list) : this;
        }
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableRandomAccessList.class */
    static class UnmodifiableRandomAccessList<E> extends UnmodifiableList<E> implements RandomAccess {
        private static final long serialVersionUID = -2542308836966382001L;

        UnmodifiableRandomAccessList(List<? extends E> list) {
            super(list);
        }

        @Override // java.util.Collections.UnmodifiableList, java.util.List
        public List<E> subList(int i2, int i3) {
            return new UnmodifiableRandomAccessList(this.list.subList(i2, i3));
        }

        private Object writeReplace() {
            return new UnmodifiableList(this.list);
        }
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> map) {
        return new UnmodifiableMap(map);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableMap.class */
    private static class UnmodifiableMap<K, V> implements Map<K, V>, Serializable {
        private static final long serialVersionUID = -1034234728574286014L;

        /* renamed from: m, reason: collision with root package name */
        private final Map<? extends K, ? extends V> f12545m;
        private transient Set<K> keySet;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Collection<V> values;

        UnmodifiableMap(Map<? extends K, ? extends V> map) {
            if (map == null) {
                throw new NullPointerException();
            }
            this.f12545m = map;
        }

        @Override // java.util.Map
        public int size() {
            return this.f12545m.size();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.f12545m.isEmpty();
        }

        @Override // java.util.Map
        public boolean containsKey(Object obj) {
            return this.f12545m.containsKey(obj);
        }

        @Override // java.util.Map
        public boolean containsValue(Object obj) {
            return this.f12545m.containsValue(obj);
        }

        @Override // java.util.Map
        public V get(Object obj) {
            return this.f12545m.get(obj);
        }

        @Override // java.util.Map
        public V put(K k2, V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends V> map) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public Set<K> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.unmodifiableSet(this.f12545m.keySet());
            }
            return this.keySet;
        }

        @Override // java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = new UnmodifiableEntrySet(this.f12545m.entrySet());
            }
            return this.entrySet;
        }

        @Override // java.util.Map
        public Collection<V> values() {
            if (this.values == null) {
                this.values = Collections.unmodifiableCollection(this.f12545m.values());
            }
            return this.values;
        }

        @Override // java.util.Map
        public boolean equals(Object obj) {
            return obj == this || this.f12545m.equals(obj);
        }

        @Override // java.util.Map
        public int hashCode() {
            return this.f12545m.hashCode();
        }

        public String toString() {
            return this.f12545m.toString();
        }

        @Override // java.util.Map
        public V getOrDefault(Object obj, V v2) {
            return this.f12545m.getOrDefault(obj, v2);
        }

        @Override // java.util.Map
        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            this.f12545m.forEach(biConsumer);
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V putIfAbsent(K k2, V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean remove(Object obj, Object obj2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean replace(K k2, V v2, V v3) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V replace(K k2, V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        /* loaded from: rt.jar:java/util/Collections$UnmodifiableMap$UnmodifiableEntrySet.class */
        static class UnmodifiableEntrySet<K, V> extends UnmodifiableSet<Map.Entry<K, V>> {
            private static final long serialVersionUID = 7854390611657943733L;

            UnmodifiableEntrySet(Set<? extends Map.Entry<? extends K, ? extends V>> set) {
                super(set);
            }

            static <K, V> Consumer<Map.Entry<K, V>> entryConsumer(Consumer<? super Map.Entry<K, V>> consumer) {
                return entry -> {
                    consumer.accept(new UnmodifiableEntry(entry));
                };
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.lang.Iterable
            public void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
                Objects.requireNonNull(consumer);
                this.f12542c.forEach(entryConsumer(consumer));
            }

            /* loaded from: rt.jar:java/util/Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntrySetSpliterator.class */
            static final class UnmodifiableEntrySetSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {

                /* renamed from: s, reason: collision with root package name */
                final Spliterator<Map.Entry<K, V>> f12548s;

                UnmodifiableEntrySetSpliterator(Spliterator<Map.Entry<K, V>> spliterator) {
                    this.f12548s = spliterator;
                }

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
                    Objects.requireNonNull(consumer);
                    return this.f12548s.tryAdvance(UnmodifiableEntrySet.entryConsumer(consumer));
                }

                @Override // java.util.Spliterator
                public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
                    Objects.requireNonNull(consumer);
                    this.f12548s.forEachRemaining(UnmodifiableEntrySet.entryConsumer(consumer));
                }

                @Override // java.util.Spliterator
                public Spliterator<Map.Entry<K, V>> trySplit() {
                    Spliterator<Map.Entry<K, V>> spliteratorTrySplit = this.f12548s.trySplit();
                    if (spliteratorTrySplit == null) {
                        return null;
                    }
                    return new UnmodifiableEntrySetSpliterator(spliteratorTrySplit);
                }

                @Override // java.util.Spliterator
                public long estimateSize() {
                    return this.f12548s.estimateSize();
                }

                @Override // java.util.Spliterator
                public long getExactSizeIfKnown() {
                    return this.f12548s.getExactSizeIfKnown();
                }

                @Override // java.util.Spliterator
                public int characteristics() {
                    return this.f12548s.characteristics();
                }

                @Override // java.util.Spliterator
                public boolean hasCharacteristics(int i2) {
                    return this.f12548s.hasCharacteristics(i2);
                }

                @Override // java.util.Spliterator
                public Comparator<? super Map.Entry<K, V>> getComparator() {
                    return this.f12548s.getComparator();
                }
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection, java.lang.Iterable
            public Spliterator<Map.Entry<K, V>> spliterator() {
                return new UnmodifiableEntrySetSpliterator(this.f12542c.spliterator());
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection
            public Stream<Map.Entry<K, V>> stream() {
                return StreamSupport.stream(spliterator(), false);
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection
            public Stream<Map.Entry<K, V>> parallelStream() {
                return StreamSupport.stream(spliterator(), true);
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection, java.lang.Iterable, java.util.List
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() { // from class: java.util.Collections.UnmodifiableMap.UnmodifiableEntrySet.1

                    /* renamed from: i, reason: collision with root package name */
                    private final Iterator<? extends Map.Entry<? extends K, ? extends V>> f12546i;

                    {
                        this.f12546i = UnmodifiableEntrySet.this.f12542c.iterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.f12546i.hasNext();
                    }

                    @Override // java.util.Iterator
                    public Map.Entry<K, V> next() {
                        return new UnmodifiableEntry(this.f12546i.next());
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection, java.util.List
            public Object[] toArray() {
                Object[] array = this.f12542c.toArray();
                for (int i2 = 0; i2 < array.length; i2++) {
                    array[i2] = new UnmodifiableEntry((Map.Entry) array[i2]);
                }
                return array;
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection
            public <T> T[] toArray(T[] tArr) {
                Object[] array = this.f12542c.toArray(tArr.length == 0 ? tArr : Arrays.copyOf(tArr, 0));
                for (int i2 = 0; i2 < array.length; i2++) {
                    array[i2] = new UnmodifiableEntry((Map.Entry) array[i2]);
                }
                if (array.length > tArr.length) {
                    return (T[]) array;
                }
                System.arraycopy(array, 0, tArr, 0, array.length);
                if (tArr.length > array.length) {
                    tArr[array.length] = null;
                }
                return tArr;
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                return this.f12542c.contains(new UnmodifiableEntry((Map.Entry) obj));
            }

            @Override // java.util.Collections.UnmodifiableCollection, java.util.Collection
            public boolean containsAll(Collection<?> collection) {
                Iterator<?> it = collection.iterator();
                while (it.hasNext()) {
                    if (!contains(it.next())) {
                        return false;
                    }
                }
                return true;
            }

            @Override // java.util.Collections.UnmodifiableSet, java.util.Collection, java.util.List
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof Set)) {
                    return false;
                }
                Set set = (Set) obj;
                if (set.size() != this.f12542c.size()) {
                    return false;
                }
                return containsAll(set);
            }

            /* loaded from: rt.jar:java/util/Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry.class */
            private static class UnmodifiableEntry<K, V> implements Map.Entry<K, V> {

                /* renamed from: e, reason: collision with root package name */
                private Map.Entry<? extends K, ? extends V> f12547e;

                UnmodifiableEntry(Map.Entry<? extends K, ? extends V> entry) {
                    this.f12547e = (Map.Entry) Objects.requireNonNull(entry);
                }

                @Override // java.util.Map.Entry
                public K getKey() {
                    return this.f12547e.getKey();
                }

                @Override // java.util.Map.Entry
                public V getValue() {
                    return this.f12547e.getValue();
                }

                @Override // java.util.Map.Entry
                public V setValue(V v2) {
                    throw new UnsupportedOperationException();
                }

                @Override // java.util.Map.Entry
                public int hashCode() {
                    return this.f12547e.hashCode();
                }

                @Override // java.util.Map.Entry
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (!(obj instanceof Map.Entry)) {
                        return false;
                    }
                    Map.Entry entry = (Map.Entry) obj;
                    return Collections.eq(this.f12547e.getKey(), entry.getKey()) && Collections.eq(this.f12547e.getValue(), entry.getValue());
                }

                public String toString() {
                    return this.f12547e.toString();
                }
            }
        }
    }

    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> sortedMap) {
        return new UnmodifiableSortedMap(sortedMap);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableSortedMap.class */
    static class UnmodifiableSortedMap<K, V> extends UnmodifiableMap<K, V> implements SortedMap<K, V>, Serializable {
        private static final long serialVersionUID = -8806743815996713206L;
        private final SortedMap<K, ? extends V> sm;

        UnmodifiableSortedMap(SortedMap<K, ? extends V> sortedMap) {
            super(sortedMap);
            this.sm = sortedMap;
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            return this.sm.comparator();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> subMap(K k2, K k3) {
            return new UnmodifiableSortedMap(this.sm.subMap(k2, k3));
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> headMap(K k2) {
            return new UnmodifiableSortedMap(this.sm.headMap(k2));
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> tailMap(K k2) {
            return new UnmodifiableSortedMap(this.sm.tailMap(k2));
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            return this.sm.firstKey();
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            return this.sm.lastKey();
        }
    }

    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> navigableMap) {
        return new UnmodifiableNavigableMap(navigableMap);
    }

    /* loaded from: rt.jar:java/util/Collections$UnmodifiableNavigableMap.class */
    static class UnmodifiableNavigableMap<K, V> extends UnmodifiableSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        private static final long serialVersionUID = -4858195264774772197L;
        private static final EmptyNavigableMap<?, ?> EMPTY_NAVIGABLE_MAP = new EmptyNavigableMap<>();
        private final NavigableMap<K, ? extends V> nm;

        /* loaded from: rt.jar:java/util/Collections$UnmodifiableNavigableMap$EmptyNavigableMap.class */
        private static class EmptyNavigableMap<K, V> extends UnmodifiableNavigableMap<K, V> implements Serializable {
            private static final long serialVersionUID = -2239321462712562324L;

            EmptyNavigableMap() {
                super(new TreeMap());
            }

            @Override // java.util.Collections.UnmodifiableNavigableMap, java.util.NavigableMap
            public NavigableSet<K> navigableKeySet() {
                return Collections.emptyNavigableSet();
            }

            private Object readResolve() {
                return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
            }
        }

        UnmodifiableNavigableMap(NavigableMap<K, ? extends V> navigableMap) {
            super(navigableMap);
            this.nm = navigableMap;
        }

        @Override // java.util.NavigableMap
        public K lowerKey(K k2) {
            return this.nm.lowerKey(k2);
        }

        @Override // java.util.NavigableMap
        public K floorKey(K k2) {
            return this.nm.floorKey(k2);
        }

        @Override // java.util.NavigableMap
        public K ceilingKey(K k2) {
            return this.nm.ceilingKey(k2);
        }

        @Override // java.util.NavigableMap
        public K higherKey(K k2) {
            return this.nm.higherKey(k2);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lowerEntry(K k2) {
            Map.Entry<K, ? extends V> entryLowerEntry = this.nm.lowerEntry(k2);
            if (null != entryLowerEntry) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(entryLowerEntry);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> floorEntry(K k2) {
            Map.Entry<K, ? extends V> entryFloorEntry = this.nm.floorEntry(k2);
            if (null != entryFloorEntry) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(entryFloorEntry);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> ceilingEntry(K k2) {
            Map.Entry<K, ? extends V> entryCeilingEntry = this.nm.ceilingEntry(k2);
            if (null != entryCeilingEntry) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(entryCeilingEntry);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> higherEntry(K k2) {
            Map.Entry<K, ? extends V> entryHigherEntry = this.nm.higherEntry(k2);
            if (null != entryHigherEntry) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(entryHigherEntry);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> firstEntry() {
            Map.Entry<K, ? extends V> entryFirstEntry = this.nm.firstEntry();
            if (null != entryFirstEntry) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(entryFirstEntry);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lastEntry() {
            Map.Entry<K, ? extends V> entryLastEntry = this.nm.lastEntry();
            if (null != entryLastEntry) {
                return new UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry(entryLastEntry);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            return Collections.unmodifiableNavigableMap(this.nm.descendingMap());
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return Collections.unmodifiableNavigableSet(this.nm.navigableKeySet());
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return Collections.unmodifiableNavigableSet(this.nm.descendingKeySet());
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
            return Collections.unmodifiableNavigableMap(this.nm.subMap(k2, z2, k3, z3));
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(K k2, boolean z2) {
            return Collections.unmodifiableNavigableMap(this.nm.headMap(k2, z2));
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(K k2, boolean z2) {
            return Collections.unmodifiableNavigableMap(this.nm.tailMap(k2, z2));
        }
    }

    public static <T> Collection<T> synchronizedCollection(Collection<T> collection) {
        return new SynchronizedCollection(collection);
    }

    static <T> Collection<T> synchronizedCollection(Collection<T> collection, Object obj) {
        return new SynchronizedCollection(collection, obj);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedCollection.class */
    static class SynchronizedCollection<E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 3053995032091335093L;

        /* renamed from: c, reason: collision with root package name */
        final Collection<E> f12540c;
        final Object mutex;

        SynchronizedCollection(Collection<E> collection) {
            this.f12540c = (Collection) Objects.requireNonNull(collection);
            this.mutex = this;
        }

        SynchronizedCollection(Collection<E> collection, Object obj) {
            this.f12540c = (Collection) Objects.requireNonNull(collection);
            this.mutex = Objects.requireNonNull(obj);
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.f12540c.size();
            }
            return size;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = this.f12540c.isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            boolean zContains;
            synchronized (this.mutex) {
                zContains = this.f12540c.contains(obj);
            }
            return zContains;
        }

        @Override // java.util.Collection, java.util.List
        public Object[] toArray() {
            Object[] array;
            synchronized (this.mutex) {
                array = this.f12540c.toArray();
            }
            return array;
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            T[] tArr2;
            synchronized (this.mutex) {
                tArr2 = (T[]) this.f12540c.toArray(tArr);
            }
            return tArr2;
        }

        @Override // java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return this.f12540c.iterator();
        }

        @Override // java.util.Collection, java.util.List
        public boolean add(E e2) {
            boolean zAdd;
            synchronized (this.mutex) {
                zAdd = this.f12540c.add(e2);
            }
            return zAdd;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            boolean zRemove;
            synchronized (this.mutex) {
                zRemove = this.f12540c.remove(obj);
            }
            return zRemove;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            boolean zContainsAll;
            synchronized (this.mutex) {
                zContainsAll = this.f12540c.containsAll(collection);
            }
            return zContainsAll;
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = this.f12540c.addAll(collection);
            }
            return zAddAll;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            boolean zRemoveAll;
            synchronized (this.mutex) {
                zRemoveAll = this.f12540c.removeAll(collection);
            }
            return zRemoveAll;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            boolean zRetainAll;
            synchronized (this.mutex) {
                zRetainAll = this.f12540c.retainAll(collection);
            }
            return zRetainAll;
        }

        @Override // java.util.Collection, java.util.List
        public void clear() {
            synchronized (this.mutex) {
                this.f12540c.clear();
            }
        }

        public String toString() {
            String string;
            synchronized (this.mutex) {
                string = this.f12540c.toString();
            }
            return string;
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            synchronized (this.mutex) {
                this.f12540c.forEach(consumer);
            }
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            boolean zRemoveIf;
            synchronized (this.mutex) {
                zRemoveIf = this.f12540c.removeIf(predicate);
            }
            return zRemoveIf;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return this.f12540c.spliterator();
        }

        @Override // java.util.Collection
        public Stream<E> stream() {
            return this.f12540c.stream();
        }

        @Override // java.util.Collection
        public Stream<E> parallelStream() {
            return this.f12540c.parallelStream();
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
    }

    public static <T> Set<T> synchronizedSet(Set<T> set) {
        return new SynchronizedSet(set);
    }

    static <T> Set<T> synchronizedSet(Set<T> set, Object obj) {
        return new SynchronizedSet(set, obj);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedSet.class */
    static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
        private static final long serialVersionUID = 487447009682186044L;

        SynchronizedSet(Set<E> set) {
            super(set);
        }

        SynchronizedSet(Set<E> set, Object obj) {
            super(set, obj);
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            boolean zEquals;
            if (this == obj) {
                return true;
            }
            synchronized (this.mutex) {
                zEquals = this.f12540c.equals(obj);
            }
            return zEquals;
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = this.f12540c.hashCode();
            }
            return iHashCode;
        }
    }

    public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> sortedSet) {
        return new SynchronizedSortedSet(sortedSet);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedSortedSet.class */
    static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> {
        private static final long serialVersionUID = 8695801310862127406L;
        private final SortedSet<E> ss;

        SynchronizedSortedSet(SortedSet<E> sortedSet) {
            super(sortedSet);
            this.ss = sortedSet;
        }

        SynchronizedSortedSet(SortedSet<E> sortedSet, Object obj) {
            super(sortedSet, obj);
            this.ss = sortedSet;
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            Comparator<? super E> comparator;
            synchronized (this.mutex) {
                comparator = this.ss.comparator();
            }
            return comparator;
        }

        public SortedSet<E> subSet(E e2, E e3) {
            SynchronizedSortedSet synchronizedSortedSet;
            synchronized (this.mutex) {
                synchronizedSortedSet = new SynchronizedSortedSet(this.ss.subSet(e2, e3), this.mutex);
            }
            return synchronizedSortedSet;
        }

        public SortedSet<E> headSet(E e2) {
            SynchronizedSortedSet synchronizedSortedSet;
            synchronized (this.mutex) {
                synchronizedSortedSet = new SynchronizedSortedSet(this.ss.headSet(e2), this.mutex);
            }
            return synchronizedSortedSet;
        }

        public SortedSet<E> tailSet(E e2) {
            SynchronizedSortedSet synchronizedSortedSet;
            synchronized (this.mutex) {
                synchronizedSortedSet = new SynchronizedSortedSet(this.ss.tailSet(e2), this.mutex);
            }
            return synchronizedSortedSet;
        }

        @Override // java.util.SortedSet
        public E first() {
            E eFirst;
            synchronized (this.mutex) {
                eFirst = this.ss.first();
            }
            return eFirst;
        }

        @Override // java.util.SortedSet
        public E last() {
            E eLast;
            synchronized (this.mutex) {
                eLast = this.ss.last();
            }
            return eLast;
        }
    }

    public static <T> NavigableSet<T> synchronizedNavigableSet(NavigableSet<T> navigableSet) {
        return new SynchronizedNavigableSet(navigableSet);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedNavigableSet.class */
    static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
        private static final long serialVersionUID = -5505529816273629798L;
        private final NavigableSet<E> ns;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collections.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((SynchronizedNavigableSet<E>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collections.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((SynchronizedNavigableSet<E>) obj);
        }

        SynchronizedNavigableSet(NavigableSet<E> navigableSet) {
            super(navigableSet);
            this.ns = navigableSet;
        }

        SynchronizedNavigableSet(NavigableSet<E> navigableSet, Object obj) {
            super(navigableSet, obj);
            this.ns = navigableSet;
        }

        @Override // java.util.NavigableSet
        public E lower(E e2) {
            E eLower;
            synchronized (this.mutex) {
                eLower = this.ns.lower(e2);
            }
            return eLower;
        }

        @Override // java.util.NavigableSet
        public E floor(E e2) {
            E eFloor;
            synchronized (this.mutex) {
                eFloor = this.ns.floor(e2);
            }
            return eFloor;
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e2) {
            E eCeiling;
            synchronized (this.mutex) {
                eCeiling = this.ns.ceiling(e2);
            }
            return eCeiling;
        }

        @Override // java.util.NavigableSet
        public E higher(E e2) {
            E eHigher;
            synchronized (this.mutex) {
                eHigher = this.ns.higher(e2);
            }
            return eHigher;
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            E ePollFirst;
            synchronized (this.mutex) {
                ePollFirst = this.ns.pollFirst();
            }
            return ePollFirst;
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            E ePollLast;
            synchronized (this.mutex) {
                ePollLast = this.ns.pollLast();
            }
            return ePollLast;
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.ns.descendingSet(), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            Iterator<E> it;
            synchronized (this.mutex) {
                it = descendingSet().iterator();
            }
            return it;
        }

        @Override // java.util.Collections.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, E e3) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.ns.subSet(e2, true, e3, false), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.Collections.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<E> headSet(E e2) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.ns.headSet(e2, false), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.Collections.SynchronizedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.ns.tailSet(e2, true), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.ns.subSet(e2, z2, e3, z3), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e2, boolean z2) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.ns.headSet(e2, z2), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2, boolean z2) {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.ns.tailSet(e2, z2), this.mutex);
            }
            return synchronizedNavigableSet;
        }
    }

    public static <T> List<T> synchronizedList(List<T> list) {
        return list instanceof RandomAccess ? new SynchronizedRandomAccessList(list) : new SynchronizedList(list);
    }

    static <T> List<T> synchronizedList(List<T> list, Object obj) {
        return list instanceof RandomAccess ? new SynchronizedRandomAccessList(list, obj) : new SynchronizedList(list, obj);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedList.class */
    static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = -7754090372962971524L;
        final List<E> list;

        SynchronizedList(List<E> list) {
            super(list);
            this.list = list;
        }

        SynchronizedList(List<E> list, Object obj) {
            super(list, obj);
            this.list = list;
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            boolean zEquals;
            if (this == obj) {
                return true;
            }
            synchronized (this.mutex) {
                zEquals = this.list.equals(obj);
            }
            return zEquals;
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = this.list.hashCode();
            }
            return iHashCode;
        }

        @Override // java.util.List
        public E get(int i2) {
            E e2;
            synchronized (this.mutex) {
                e2 = this.list.get(i2);
            }
            return e2;
        }

        @Override // java.util.List
        public E set(int i2, E e2) {
            E e3;
            synchronized (this.mutex) {
                e3 = this.list.set(i2, e2);
            }
            return e3;
        }

        @Override // java.util.List
        public void add(int i2, E e2) {
            synchronized (this.mutex) {
                this.list.add(i2, e2);
            }
        }

        @Override // java.util.List
        public E remove(int i2) {
            E eRemove;
            synchronized (this.mutex) {
                eRemove = this.list.remove(i2);
            }
            return eRemove;
        }

        @Override // java.util.List
        public int indexOf(Object obj) {
            int iIndexOf;
            synchronized (this.mutex) {
                iIndexOf = this.list.indexOf(obj);
            }
            return iIndexOf;
        }

        @Override // java.util.List
        public int lastIndexOf(Object obj) {
            int iLastIndexOf;
            synchronized (this.mutex) {
                iLastIndexOf = this.list.lastIndexOf(obj);
            }
            return iLastIndexOf;
        }

        @Override // java.util.List
        public boolean addAll(int i2, Collection<? extends E> collection) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = this.list.addAll(i2, collection);
            }
            return zAddAll;
        }

        @Override // java.util.List
        public ListIterator<E> listIterator() {
            return this.list.listIterator();
        }

        @Override // java.util.List
        public ListIterator<E> listIterator(int i2) {
            return this.list.listIterator(i2);
        }

        @Override // java.util.List
        public List<E> subList(int i2, int i3) {
            SynchronizedList synchronizedList;
            synchronized (this.mutex) {
                synchronizedList = new SynchronizedList(this.list.subList(i2, i3), this.mutex);
            }
            return synchronizedList;
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<E> unaryOperator) {
            synchronized (this.mutex) {
                this.list.replaceAll(unaryOperator);
            }
        }

        @Override // java.util.List, com.sun.javafx.collections.SortableList
        public void sort(Comparator<? super E> comparator) {
            synchronized (this.mutex) {
                this.list.sort(comparator);
            }
        }

        private Object readResolve() {
            return this.list instanceof RandomAccess ? new SynchronizedRandomAccessList(this.list) : this;
        }
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedRandomAccessList.class */
    static class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {
        private static final long serialVersionUID = 1530674583602358482L;

        SynchronizedRandomAccessList(List<E> list) {
            super(list);
        }

        SynchronizedRandomAccessList(List<E> list, Object obj) {
            super(list, obj);
        }

        @Override // java.util.Collections.SynchronizedList, java.util.List
        public List<E> subList(int i2, int i3) {
            SynchronizedRandomAccessList synchronizedRandomAccessList;
            synchronized (this.mutex) {
                synchronizedRandomAccessList = new SynchronizedRandomAccessList(this.list.subList(i2, i3), this.mutex);
            }
            return synchronizedRandomAccessList;
        }

        private Object writeReplace() {
            return new SynchronizedList(this.list);
        }
    }

    public static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
        return new SynchronizedMap(map);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedMap.class */
    private static class SynchronizedMap<K, V> implements Map<K, V>, Serializable {
        private static final long serialVersionUID = 1978198479659022715L;

        /* renamed from: m, reason: collision with root package name */
        private final Map<K, V> f12541m;
        final Object mutex;
        private transient Set<K> keySet;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Collection<V> values;

        SynchronizedMap(Map<K, V> map) {
            this.f12541m = (Map) Objects.requireNonNull(map);
            this.mutex = this;
        }

        SynchronizedMap(Map<K, V> map, Object obj) {
            this.f12541m = map;
            this.mutex = obj;
        }

        @Override // java.util.Map
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = this.f12541m.size();
            }
            return size;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = this.f12541m.isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.Map
        public boolean containsKey(Object obj) {
            boolean zContainsKey;
            synchronized (this.mutex) {
                zContainsKey = this.f12541m.containsKey(obj);
            }
            return zContainsKey;
        }

        @Override // java.util.Map
        public boolean containsValue(Object obj) {
            boolean zContainsValue;
            synchronized (this.mutex) {
                zContainsValue = this.f12541m.containsValue(obj);
            }
            return zContainsValue;
        }

        @Override // java.util.Map
        public V get(Object obj) {
            V v2;
            synchronized (this.mutex) {
                v2 = this.f12541m.get(obj);
            }
            return v2;
        }

        @Override // java.util.Map
        public V put(K k2, V v2) {
            V vPut;
            synchronized (this.mutex) {
                vPut = this.f12541m.put(k2, v2);
            }
            return vPut;
        }

        @Override // java.util.Map
        public V remove(Object obj) {
            V vRemove;
            synchronized (this.mutex) {
                vRemove = this.f12541m.remove(obj);
            }
            return vRemove;
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends V> map) {
            synchronized (this.mutex) {
                this.f12541m.putAll(map);
            }
        }

        @Override // java.util.Map
        public void clear() {
            synchronized (this.mutex) {
                this.f12541m.clear();
            }
        }

        @Override // java.util.Map
        public Set<K> keySet() {
            Set<K> set;
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = new SynchronizedSet(this.f12541m.keySet(), this.mutex);
                }
                set = this.keySet;
            }
            return set;
        }

        @Override // java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set;
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = new SynchronizedSet(this.f12541m.entrySet(), this.mutex);
                }
                set = this.entrySet;
            }
            return set;
        }

        @Override // java.util.Map
        public Collection<V> values() {
            Collection<V> collection;
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = new SynchronizedCollection(this.f12541m.values(), this.mutex);
                }
                collection = this.values;
            }
            return collection;
        }

        @Override // java.util.Map
        public boolean equals(Object obj) {
            boolean zEquals;
            if (this == obj) {
                return true;
            }
            synchronized (this.mutex) {
                zEquals = this.f12541m.equals(obj);
            }
            return zEquals;
        }

        @Override // java.util.Map
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = this.f12541m.hashCode();
            }
            return iHashCode;
        }

        public String toString() {
            String string;
            synchronized (this.mutex) {
                string = this.f12541m.toString();
            }
            return string;
        }

        @Override // java.util.Map
        public V getOrDefault(Object obj, V v2) {
            V orDefault;
            synchronized (this.mutex) {
                orDefault = this.f12541m.getOrDefault(obj, v2);
            }
            return orDefault;
        }

        @Override // java.util.Map
        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            synchronized (this.mutex) {
                this.f12541m.forEach(biConsumer);
            }
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            synchronized (this.mutex) {
                this.f12541m.replaceAll(biFunction);
            }
        }

        @Override // java.util.Map
        public V putIfAbsent(K k2, V v2) {
            V vPutIfAbsent;
            synchronized (this.mutex) {
                vPutIfAbsent = this.f12541m.putIfAbsent(k2, v2);
            }
            return vPutIfAbsent;
        }

        @Override // java.util.Map
        public boolean remove(Object obj, Object obj2) {
            boolean zRemove;
            synchronized (this.mutex) {
                zRemove = this.f12541m.remove(obj, obj2);
            }
            return zRemove;
        }

        @Override // java.util.Map
        public boolean replace(K k2, V v2, V v3) {
            boolean zReplace;
            synchronized (this.mutex) {
                zReplace = this.f12541m.replace(k2, v2, v3);
            }
            return zReplace;
        }

        @Override // java.util.Map
        public V replace(K k2, V v2) {
            V vReplace;
            synchronized (this.mutex) {
                vReplace = this.f12541m.replace(k2, v2);
            }
            return vReplace;
        }

        @Override // java.util.Map
        public V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
            V vComputeIfAbsent;
            synchronized (this.mutex) {
                vComputeIfAbsent = this.f12541m.computeIfAbsent(k2, function);
            }
            return vComputeIfAbsent;
        }

        @Override // java.util.Map
        public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            V vComputeIfPresent;
            synchronized (this.mutex) {
                vComputeIfPresent = this.f12541m.computeIfPresent(k2, biFunction);
            }
            return vComputeIfPresent;
        }

        @Override // java.util.Map
        public V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            V vCompute;
            synchronized (this.mutex) {
                vCompute = this.f12541m.compute(k2, biFunction);
            }
            return vCompute;
        }

        @Override // java.util.Map
        public V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            V vMerge;
            synchronized (this.mutex) {
                vMerge = this.f12541m.merge(k2, v2, biFunction);
            }
            return vMerge;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
    }

    public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> sortedMap) {
        return new SynchronizedSortedMap(sortedMap);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedSortedMap.class */
    static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
        private static final long serialVersionUID = -8798146769416483793L;
        private final SortedMap<K, V> sm;

        SynchronizedSortedMap(SortedMap<K, V> sortedMap) {
            super(sortedMap);
            this.sm = sortedMap;
        }

        SynchronizedSortedMap(SortedMap<K, V> sortedMap, Object obj) {
            super(sortedMap, obj);
            this.sm = sortedMap;
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.mutex) {
                comparator = this.sm.comparator();
            }
            return comparator;
        }

        public SortedMap<K, V> subMap(K k2, K k3) {
            SynchronizedSortedMap synchronizedSortedMap;
            synchronized (this.mutex) {
                synchronizedSortedMap = new SynchronizedSortedMap(this.sm.subMap(k2, k3), this.mutex);
            }
            return synchronizedSortedMap;
        }

        public SortedMap<K, V> headMap(K k2) {
            SynchronizedSortedMap synchronizedSortedMap;
            synchronized (this.mutex) {
                synchronizedSortedMap = new SynchronizedSortedMap(this.sm.headMap(k2), this.mutex);
            }
            return synchronizedSortedMap;
        }

        public SortedMap<K, V> tailMap(K k2) {
            SynchronizedSortedMap synchronizedSortedMap;
            synchronized (this.mutex) {
                synchronizedSortedMap = new SynchronizedSortedMap(this.sm.tailMap(k2), this.mutex);
            }
            return synchronizedSortedMap;
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            K kFirstKey;
            synchronized (this.mutex) {
                kFirstKey = this.sm.firstKey();
            }
            return kFirstKey;
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            K kLastKey;
            synchronized (this.mutex) {
                kLastKey = this.sm.lastKey();
            }
            return kLastKey;
        }
    }

    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
        return new SynchronizedNavigableMap(navigableMap);
    }

    /* loaded from: rt.jar:java/util/Collections$SynchronizedNavigableMap.class */
    static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {
        private static final long serialVersionUID = 699392247599746807L;
        private final NavigableMap<K, V> nm;

        SynchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
            super(navigableMap);
            this.nm = navigableMap;
        }

        SynchronizedNavigableMap(NavigableMap<K, V> navigableMap, Object obj) {
            super(navigableMap, obj);
            this.nm = navigableMap;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lowerEntry(K k2) {
            Map.Entry<K, V> entryLowerEntry;
            synchronized (this.mutex) {
                entryLowerEntry = this.nm.lowerEntry(k2);
            }
            return entryLowerEntry;
        }

        @Override // java.util.NavigableMap
        public K lowerKey(K k2) {
            K kLowerKey;
            synchronized (this.mutex) {
                kLowerKey = this.nm.lowerKey(k2);
            }
            return kLowerKey;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> floorEntry(K k2) {
            Map.Entry<K, V> entryFloorEntry;
            synchronized (this.mutex) {
                entryFloorEntry = this.nm.floorEntry(k2);
            }
            return entryFloorEntry;
        }

        @Override // java.util.NavigableMap
        public K floorKey(K k2) {
            K kFloorKey;
            synchronized (this.mutex) {
                kFloorKey = this.nm.floorKey(k2);
            }
            return kFloorKey;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> ceilingEntry(K k2) {
            Map.Entry<K, V> entryCeilingEntry;
            synchronized (this.mutex) {
                entryCeilingEntry = this.nm.ceilingEntry(k2);
            }
            return entryCeilingEntry;
        }

        @Override // java.util.NavigableMap
        public K ceilingKey(K k2) {
            K kCeilingKey;
            synchronized (this.mutex) {
                kCeilingKey = this.nm.ceilingKey(k2);
            }
            return kCeilingKey;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> higherEntry(K k2) {
            Map.Entry<K, V> entryHigherEntry;
            synchronized (this.mutex) {
                entryHigherEntry = this.nm.higherEntry(k2);
            }
            return entryHigherEntry;
        }

        @Override // java.util.NavigableMap
        public K higherKey(K k2) {
            K kHigherKey;
            synchronized (this.mutex) {
                kHigherKey = this.nm.higherKey(k2);
            }
            return kHigherKey;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> firstEntry() {
            Map.Entry<K, V> entryFirstEntry;
            synchronized (this.mutex) {
                entryFirstEntry = this.nm.firstEntry();
            }
            return entryFirstEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lastEntry() {
            Map.Entry<K, V> entryLastEntry;
            synchronized (this.mutex) {
                entryLastEntry = this.nm.lastEntry();
            }
            return entryLastEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollFirstEntry() {
            Map.Entry<K, V> entryPollFirstEntry;
            synchronized (this.mutex) {
                entryPollFirstEntry = this.nm.pollFirstEntry();
            }
            return entryPollFirstEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollLastEntry() {
            Map.Entry<K, V> entryPollLastEntry;
            synchronized (this.mutex) {
                entryPollLastEntry = this.nm.pollLastEntry();
            }
            return entryPollLastEntry;
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.nm.descendingMap(), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        @Override // java.util.Collections.SynchronizedMap, java.util.Map
        public NavigableSet<K> keySet() {
            return navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.nm.navigableKeySet(), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            SynchronizedNavigableSet synchronizedNavigableSet;
            synchronized (this.mutex) {
                synchronizedNavigableSet = new SynchronizedNavigableSet(this.nm.descendingKeySet(), this.mutex);
            }
            return synchronizedNavigableSet;
        }

        @Override // java.util.Collections.SynchronizedSortedMap, java.util.SortedMap
        public SortedMap<K, V> subMap(K k2, K k3) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.nm.subMap(k2, true, k3, false), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        @Override // java.util.Collections.SynchronizedSortedMap, java.util.SortedMap
        public SortedMap<K, V> headMap(K k2) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.nm.headMap(k2, false), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        @Override // java.util.Collections.SynchronizedSortedMap, java.util.SortedMap
        public SortedMap<K, V> tailMap(K k2) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.nm.tailMap(k2, true), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.nm.subMap(k2, z2, k3, z3), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(K k2, boolean z2) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.nm.headMap(k2, z2), this.mutex);
            }
            return synchronizedNavigableMap;
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(K k2, boolean z2) {
            SynchronizedNavigableMap synchronizedNavigableMap;
            synchronized (this.mutex) {
                synchronizedNavigableMap = new SynchronizedNavigableMap(this.nm.tailMap(k2, z2), this.mutex);
            }
            return synchronizedNavigableMap;
        }
    }

    public static <E> Collection<E> checkedCollection(Collection<E> collection, Class<E> cls) {
        return new CheckedCollection(collection, cls);
    }

    static <T> T[] zeroLengthArray(Class<T> cls) {
        return (T[]) ((Object[]) Array.newInstance((Class<?>) cls, 0));
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedCollection.class */
    static class CheckedCollection<E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 1578914078182001775L;

        /* renamed from: c, reason: collision with root package name */
        final Collection<E> f12531c;
        final Class<E> type;
        private E[] zeroLengthElementArray;

        /* JADX WARN: Multi-variable type inference failed */
        E typeCheck(Object obj) {
            if (obj != 0 && !this.type.isInstance(obj)) {
                throw new ClassCastException(badElementMsg(obj));
            }
            return obj;
        }

        private String badElementMsg(Object obj) {
            return "Attempt to insert " + ((Object) obj.getClass()) + " element into collection with element type " + ((Object) this.type);
        }

        CheckedCollection(Collection<E> collection, Class<E> cls) {
            this.f12531c = (Collection) Objects.requireNonNull(collection, PdfOps.c_TOKEN);
            this.type = (Class) Objects.requireNonNull(cls, "type");
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            return this.f12531c.size();
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return this.f12531c.isEmpty();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12531c.contains(obj);
        }

        @Override // java.util.Collection, java.util.List
        public Object[] toArray() {
            return this.f12531c.toArray();
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.f12531c.toArray(tArr);
        }

        public String toString() {
            return this.f12531c.toString();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.f12531c.remove(obj);
        }

        @Override // java.util.Collection, java.util.List
        public void clear() {
            this.f12531c.clear();
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return this.f12531c.containsAll(collection);
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            return this.f12531c.removeAll(collection);
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            return this.f12531c.retainAll(collection);
        }

        @Override // java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            final Iterator<E> it = this.f12531c.iterator();
            return new Iterator<E>() { // from class: java.util.Collections.CheckedCollection.1
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

        @Override // java.util.Collection, java.util.List
        public boolean add(E e2) {
            return this.f12531c.add(typeCheck(e2));
        }

        private E[] zeroLengthElementArray() {
            if (this.zeroLengthElementArray != null) {
                return this.zeroLengthElementArray;
            }
            E[] eArr = (E[]) Collections.zeroLengthArray(this.type);
            this.zeroLengthElementArray = eArr;
            return eArr;
        }

        Collection<E> checkedCopyOf(Collection<? extends E> collection) {
            Object[] array;
            try {
                E[] eArrZeroLengthElementArray = zeroLengthElementArray();
                array = collection.toArray(eArrZeroLengthElementArray);
                if (array.getClass() != eArrZeroLengthElementArray.getClass()) {
                    array = Arrays.copyOf(array, array.length, eArrZeroLengthElementArray.getClass());
                }
            } catch (ArrayStoreException e2) {
                array = (Object[]) collection.toArray().clone();
                for (Object obj : array) {
                    typeCheck(obj);
                }
            }
            return Arrays.asList(array);
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            return this.f12531c.addAll(checkedCopyOf(collection));
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            this.f12531c.forEach(consumer);
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            return this.f12531c.removeIf(predicate);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return this.f12531c.spliterator();
        }

        @Override // java.util.Collection
        public Stream<E> stream() {
            return this.f12531c.stream();
        }

        @Override // java.util.Collection
        public Stream<E> parallelStream() {
            return this.f12531c.parallelStream();
        }
    }

    public static <E> Queue<E> checkedQueue(Queue<E> queue, Class<E> cls) {
        return new CheckedQueue(queue, cls);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedQueue.class */
    static class CheckedQueue<E> extends CheckedCollection<E> implements Queue<E>, Serializable {
        private static final long serialVersionUID = 1433151992604707767L;
        final Queue<E> queue;

        CheckedQueue(Queue<E> queue, Class<E> cls) {
            super(queue, cls);
            this.queue = queue;
        }

        @Override // java.util.Queue
        public E element() {
            return this.queue.element();
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return obj == this || this.f12531c.equals(obj);
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            return this.f12531c.hashCode();
        }

        @Override // java.util.Queue
        public E peek() {
            return this.queue.peek();
        }

        @Override // java.util.Queue
        public E poll() {
            return this.queue.poll();
        }

        @Override // java.util.Queue
        public E remove() {
            return this.queue.remove();
        }

        @Override // java.util.Queue
        public boolean offer(E e2) {
            return this.queue.offer(typeCheck(e2));
        }
    }

    public static <E> Set<E> checkedSet(Set<E> set, Class<E> cls) {
        return new CheckedSet(set, cls);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedSet.class */
    static class CheckedSet<E> extends CheckedCollection<E> implements Set<E>, Serializable {
        private static final long serialVersionUID = 4694047833775013803L;

        CheckedSet(Set<E> set, Class<E> cls) {
            super(set, cls);
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return obj == this || this.f12531c.equals(obj);
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            return this.f12531c.hashCode();
        }
    }

    public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> sortedSet, Class<E> cls) {
        return new CheckedSortedSet(sortedSet, cls);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedSortedSet.class */
    static class CheckedSortedSet<E> extends CheckedSet<E> implements SortedSet<E>, Serializable {
        private static final long serialVersionUID = 1599911165492914959L;
        private final SortedSet<E> ss;

        CheckedSortedSet(SortedSet<E> sortedSet, Class<E> cls) {
            super(sortedSet, cls);
            this.ss = sortedSet;
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            return this.ss.comparator();
        }

        @Override // java.util.SortedSet
        public E first() {
            return this.ss.first();
        }

        @Override // java.util.SortedSet
        public E last() {
            return this.ss.last();
        }

        public SortedSet<E> subSet(E e2, E e3) {
            return Collections.checkedSortedSet(this.ss.subSet(e2, e3), this.type);
        }

        public SortedSet<E> headSet(E e2) {
            return Collections.checkedSortedSet(this.ss.headSet(e2), this.type);
        }

        public SortedSet<E> tailSet(E e2) {
            return Collections.checkedSortedSet(this.ss.tailSet(e2), this.type);
        }
    }

    public static <E> NavigableSet<E> checkedNavigableSet(NavigableSet<E> navigableSet, Class<E> cls) {
        return new CheckedNavigableSet(navigableSet, cls);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedNavigableSet.class */
    static class CheckedNavigableSet<E> extends CheckedSortedSet<E> implements NavigableSet<E>, Serializable {
        private static final long serialVersionUID = -5429120189805438922L;
        private final NavigableSet<E> ns;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collections.CheckedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
            return tailSet((CheckedNavigableSet<E>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collections.CheckedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
            return headSet((CheckedNavigableSet<E>) obj);
        }

        CheckedNavigableSet(NavigableSet<E> navigableSet, Class<E> cls) {
            super(navigableSet, cls);
            this.ns = navigableSet;
        }

        @Override // java.util.NavigableSet
        public E lower(E e2) {
            return this.ns.lower(e2);
        }

        @Override // java.util.NavigableSet
        public E floor(E e2) {
            return this.ns.floor(e2);
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e2) {
            return this.ns.ceiling(e2);
        }

        @Override // java.util.NavigableSet
        public E higher(E e2) {
            return this.ns.higher(e2);
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            return this.ns.pollFirst();
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            return this.ns.pollLast();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            return Collections.checkedNavigableSet(this.ns.descendingSet(), this.type);
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return Collections.checkedNavigableSet(this.ns.descendingSet(), this.type).iterator();
        }

        @Override // java.util.Collections.CheckedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, E e3) {
            return Collections.checkedNavigableSet(this.ns.subSet(e2, true, e3, false), this.type);
        }

        @Override // java.util.Collections.CheckedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<E> headSet(E e2) {
            return Collections.checkedNavigableSet(this.ns.headSet(e2, false), this.type);
        }

        @Override // java.util.Collections.CheckedSortedSet, java.util.SortedSet, java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2) {
            return Collections.checkedNavigableSet(this.ns.tailSet(e2, true), this.type);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3) {
            return Collections.checkedNavigableSet(this.ns.subSet(e2, z2, e3, z3), this.type);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e2, boolean z2) {
            return Collections.checkedNavigableSet(this.ns.headSet(e2, z2), this.type);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2, boolean z2) {
            return Collections.checkedNavigableSet(this.ns.tailSet(e2, z2), this.type);
        }
    }

    public static <E> List<E> checkedList(List<E> list, Class<E> cls) {
        return list instanceof RandomAccess ? new CheckedRandomAccessList(list, cls) : new CheckedList(list, cls);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedList.class */
    static class CheckedList<E> extends CheckedCollection<E> implements List<E> {
        private static final long serialVersionUID = 65247728283967356L;
        final List<E> list;

        CheckedList(List<E> list, Class<E> cls) {
            super(list, cls);
            this.list = list;
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return obj == this || this.list.equals(obj);
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            return this.list.hashCode();
        }

        @Override // java.util.List
        public E get(int i2) {
            return this.list.get(i2);
        }

        @Override // java.util.List
        public E remove(int i2) {
            return this.list.remove(i2);
        }

        @Override // java.util.List
        public int indexOf(Object obj) {
            return this.list.indexOf(obj);
        }

        @Override // java.util.List
        public int lastIndexOf(Object obj) {
            return this.list.lastIndexOf(obj);
        }

        @Override // java.util.List
        public E set(int i2, E e2) {
            return this.list.set(i2, typeCheck(e2));
        }

        @Override // java.util.List
        public void add(int i2, E e2) {
            this.list.add(i2, typeCheck(e2));
        }

        @Override // java.util.List
        public boolean addAll(int i2, Collection<? extends E> collection) {
            return this.list.addAll(i2, checkedCopyOf(collection));
        }

        @Override // java.util.List
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        @Override // java.util.List
        public ListIterator<E> listIterator(int i2) {
            final ListIterator<E> listIterator = this.list.listIterator(i2);
            return new ListIterator<E>() { // from class: java.util.Collections.CheckedList.1
                @Override // java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return listIterator.hasNext();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public E next() {
                    return (E) listIterator.next();
                }

                @Override // java.util.ListIterator
                public boolean hasPrevious() {
                    return listIterator.hasPrevious();
                }

                @Override // java.util.ListIterator
                public E previous() {
                    return (E) listIterator.previous();
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return listIterator.nextIndex();
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return listIterator.previousIndex();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public void remove() {
                    listIterator.remove();
                }

                @Override // java.util.ListIterator
                public void set(E e2) {
                    listIterator.set(CheckedList.this.typeCheck(e2));
                }

                @Override // java.util.ListIterator
                public void add(E e2) {
                    listIterator.add(CheckedList.this.typeCheck(e2));
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super E> consumer) {
                    listIterator.forEachRemaining(consumer);
                }
            };
        }

        @Override // java.util.List
        public List<E> subList(int i2, int i3) {
            return new CheckedList(this.list.subList(i2, i3), this.type);
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<E> unaryOperator) {
            Objects.requireNonNull(unaryOperator);
            this.list.replaceAll(obj -> {
                return typeCheck(unaryOperator.apply(obj));
            });
        }

        @Override // java.util.List, com.sun.javafx.collections.SortableList
        public void sort(Comparator<? super E> comparator) {
            this.list.sort(comparator);
        }
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedRandomAccessList.class */
    static class CheckedRandomAccessList<E> extends CheckedList<E> implements RandomAccess {
        private static final long serialVersionUID = 1638200125423088369L;

        CheckedRandomAccessList(List<E> list, Class<E> cls) {
            super(list, cls);
        }

        @Override // java.util.Collections.CheckedList, java.util.List
        public List<E> subList(int i2, int i3) {
            return new CheckedRandomAccessList(this.list.subList(i2, i3), this.type);
        }
    }

    public static <K, V> Map<K, V> checkedMap(Map<K, V> map, Class<K> cls, Class<V> cls2) {
        return new CheckedMap(map, cls, cls2);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedMap.class */
    private static class CheckedMap<K, V> implements Map<K, V>, Serializable {
        private static final long serialVersionUID = 5742860141034234728L;

        /* renamed from: m, reason: collision with root package name */
        private final Map<K, V> f12532m;
        final Class<K> keyType;
        final Class<V> valueType;
        private transient Set<Map.Entry<K, V>> entrySet;

        private void typeCheck(Object obj, Object obj2) {
            if (obj != null && !this.keyType.isInstance(obj)) {
                throw new ClassCastException(badKeyMsg(obj));
            }
            if (obj2 != null && !this.valueType.isInstance(obj2)) {
                throw new ClassCastException(badValueMsg(obj2));
            }
        }

        private BiFunction<? super K, ? super V, ? extends V> typeCheck(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            Objects.requireNonNull(biFunction);
            return (obj, obj2) -> {
                Object objApply = biFunction.apply(obj, obj2);
                typeCheck(obj, objApply);
                return objApply;
            };
        }

        private String badKeyMsg(Object obj) {
            return "Attempt to insert " + ((Object) obj.getClass()) + " key into map with key type " + ((Object) this.keyType);
        }

        private String badValueMsg(Object obj) {
            return "Attempt to insert " + ((Object) obj.getClass()) + " value into map with value type " + ((Object) this.valueType);
        }

        CheckedMap(Map<K, V> map, Class<K> cls, Class<V> cls2) {
            this.f12532m = (Map) Objects.requireNonNull(map);
            this.keyType = (Class) Objects.requireNonNull(cls);
            this.valueType = (Class) Objects.requireNonNull(cls2);
        }

        @Override // java.util.Map
        public int size() {
            return this.f12532m.size();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.f12532m.isEmpty();
        }

        @Override // java.util.Map
        public boolean containsKey(Object obj) {
            return this.f12532m.containsKey(obj);
        }

        @Override // java.util.Map
        public boolean containsValue(Object obj) {
            return this.f12532m.containsValue(obj);
        }

        @Override // java.util.Map
        public V get(Object obj) {
            return this.f12532m.get(obj);
        }

        @Override // java.util.Map
        public V remove(Object obj) {
            return this.f12532m.remove(obj);
        }

        @Override // java.util.Map
        public void clear() {
            this.f12532m.clear();
        }

        @Override // java.util.Map
        public Set<K> keySet() {
            return this.f12532m.keySet();
        }

        @Override // java.util.Map
        public Collection<V> values() {
            return this.f12532m.values();
        }

        @Override // java.util.Map
        public boolean equals(Object obj) {
            return obj == this || this.f12532m.equals(obj);
        }

        @Override // java.util.Map
        public int hashCode() {
            return this.f12532m.hashCode();
        }

        public String toString() {
            return this.f12532m.toString();
        }

        @Override // java.util.Map
        public V put(K k2, V v2) {
            typeCheck(k2, v2);
            return this.f12532m.put(k2, v2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends V> map) {
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
                this.f12532m.put(entry2.getKey(), entry2.getValue());
            }
        }

        @Override // java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = new CheckedEntrySet(this.f12532m.entrySet(), this.valueType);
            }
            return this.entrySet;
        }

        @Override // java.util.Map
        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            this.f12532m.forEach(biConsumer);
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            this.f12532m.replaceAll(typeCheck(biFunction));
        }

        @Override // java.util.Map
        public V putIfAbsent(K k2, V v2) {
            typeCheck(k2, v2);
            return this.f12532m.putIfAbsent(k2, v2);
        }

        @Override // java.util.Map
        public boolean remove(Object obj, Object obj2) {
            return this.f12532m.remove(obj, obj2);
        }

        @Override // java.util.Map
        public boolean replace(K k2, V v2, V v3) {
            typeCheck(k2, v3);
            return this.f12532m.replace(k2, v2, v3);
        }

        @Override // java.util.Map
        public V replace(K k2, V v2) {
            typeCheck(k2, v2);
            return this.f12532m.replace(k2, v2);
        }

        @Override // java.util.Map
        public V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
            Objects.requireNonNull(function);
            return this.f12532m.computeIfAbsent(k2, obj -> {
                Object objApply = function.apply(obj);
                typeCheck(obj, objApply);
                return objApply;
            });
        }

        @Override // java.util.Map
        public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            return this.f12532m.computeIfPresent(k2, typeCheck(biFunction));
        }

        @Override // java.util.Map
        public V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            return this.f12532m.compute(k2, typeCheck(biFunction));
        }

        @Override // java.util.Map
        public V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            Objects.requireNonNull(biFunction);
            return this.f12532m.merge(k2, v2, (obj, obj2) -> {
                Object objApply = biFunction.apply(obj, obj2);
                typeCheck(null, objApply);
                return objApply;
            });
        }

        /* loaded from: rt.jar:java/util/Collections$CheckedMap$CheckedEntrySet.class */
        static class CheckedEntrySet<K, V> implements Set<Map.Entry<K, V>> {

            /* renamed from: s, reason: collision with root package name */
            private final Set<Map.Entry<K, V>> f12533s;
            private final Class<V> valueType;

            CheckedEntrySet(Set<Map.Entry<K, V>> set, Class<V> cls) {
                this.f12533s = set;
                this.valueType = cls;
            }

            @Override // java.util.Set
            public int size() {
                return this.f12533s.size();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean isEmpty() {
                return this.f12533s.isEmpty();
            }

            public String toString() {
                return this.f12533s.toString();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public int hashCode() {
                return this.f12533s.hashCode();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public void clear() {
                this.f12533s.clear();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public boolean add(Map.Entry<K, V> entry) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
            public Iterator<Map.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> it = this.f12533s.iterator();
                final Class<V> cls = this.valueType;
                return new Iterator<Map.Entry<K, V>>() { // from class: java.util.Collections.CheckedMap.CheckedEntrySet.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        it.remove();
                    }

                    @Override // java.util.Iterator
                    public Map.Entry<K, V> next() {
                        return CheckedEntrySet.checkedEntry((Map.Entry) it.next(), cls);
                    }
                };
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public Object[] toArray() {
                Object[] array = this.f12533s.toArray();
                Object[] objArr = CheckedEntry.class.isInstance(array.getClass().getComponentType()) ? array : new Object[array.length];
                for (int i2 = 0; i2 < array.length; i2++) {
                    objArr[i2] = checkedEntry((Map.Entry) array[i2], this.valueType);
                }
                return objArr;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Set, java.util.Collection
            public <T> T[] toArray(T[] tArr) {
                T[] tArr2 = (T[]) this.f12533s.toArray(tArr.length == 0 ? tArr : Arrays.copyOf(tArr, 0));
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
            public boolean contains(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                return this.f12533s.contains(entry instanceof CheckedEntry ? entry : checkedEntry(entry, this.valueType));
            }

            @Override // java.util.Set, java.util.Collection
            public boolean containsAll(Collection<?> collection) {
                Iterator<?> it = collection.iterator();
                while (it.hasNext()) {
                    if (!contains(it.next())) {
                        return false;
                    }
                }
                return true;
            }

            @Override // java.util.Set
            public boolean remove(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                return this.f12533s.remove(new AbstractMap.SimpleImmutableEntry((Map.Entry) obj));
            }

            @Override // java.util.Set, java.util.Collection
            public boolean removeAll(Collection<?> collection) {
                return batchRemove(collection, false);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean retainAll(Collection<?> collection) {
                return batchRemove(collection, true);
            }

            private boolean batchRemove(Collection<?> collection, boolean z2) {
                Objects.requireNonNull(collection);
                boolean z3 = false;
                Iterator<Map.Entry<K, V>> it = iterator();
                while (it.hasNext()) {
                    if (collection.contains(it.next()) != z2) {
                        it.remove();
                        z3 = true;
                    }
                }
                return z3;
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof Set)) {
                    return false;
                }
                Set set = (Set) obj;
                return set.size() == this.f12533s.size() && containsAll(set);
            }

            static <K, V, T> CheckedEntry<K, V, T> checkedEntry(Map.Entry<K, V> entry, Class<T> cls) {
                return new CheckedEntry<>(entry, cls);
            }

            /* loaded from: rt.jar:java/util/Collections$CheckedMap$CheckedEntrySet$CheckedEntry.class */
            private static class CheckedEntry<K, V, T> implements Map.Entry<K, V> {

                /* renamed from: e, reason: collision with root package name */
                private final Map.Entry<K, V> f12534e;
                private final Class<T> valueType;

                CheckedEntry(Map.Entry<K, V> entry, Class<T> cls) {
                    this.f12534e = (Map.Entry) Objects.requireNonNull(entry);
                    this.valueType = (Class) Objects.requireNonNull(cls);
                }

                @Override // java.util.Map.Entry
                public K getKey() {
                    return this.f12534e.getKey();
                }

                @Override // java.util.Map.Entry
                public V getValue() {
                    return this.f12534e.getValue();
                }

                @Override // java.util.Map.Entry
                public int hashCode() {
                    return this.f12534e.hashCode();
                }

                public String toString() {
                    return this.f12534e.toString();
                }

                @Override // java.util.Map.Entry
                public V setValue(V v2) {
                    if (v2 != null && !this.valueType.isInstance(v2)) {
                        throw new ClassCastException(badValueMsg(v2));
                    }
                    return this.f12534e.setValue(v2);
                }

                private String badValueMsg(Object obj) {
                    return "Attempt to insert " + ((Object) obj.getClass()) + " value into map with value type " + ((Object) this.valueType);
                }

                @Override // java.util.Map.Entry
                public boolean equals(Object obj) {
                    if (obj == this) {
                        return true;
                    }
                    if (!(obj instanceof Map.Entry)) {
                        return false;
                    }
                    return this.f12534e.equals(new AbstractMap.SimpleImmutableEntry((Map.Entry) obj));
                }
            }
        }
    }

    public static <K, V> SortedMap<K, V> checkedSortedMap(SortedMap<K, V> sortedMap, Class<K> cls, Class<V> cls2) {
        return new CheckedSortedMap(sortedMap, cls, cls2);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedSortedMap.class */
    static class CheckedSortedMap<K, V> extends CheckedMap<K, V> implements SortedMap<K, V>, Serializable {
        private static final long serialVersionUID = 1599671320688067438L;
        private final SortedMap<K, V> sm;

        CheckedSortedMap(SortedMap<K, V> sortedMap, Class<K> cls, Class<V> cls2) {
            super(sortedMap, cls, cls2);
            this.sm = sortedMap;
        }

        public Comparator<? super K> comparator() {
            return this.sm.comparator();
        }

        public K firstKey() {
            return this.sm.firstKey();
        }

        public K lastKey() {
            return this.sm.lastKey();
        }

        public SortedMap<K, V> subMap(K k2, K k3) {
            return Collections.checkedSortedMap(this.sm.subMap(k2, k3), this.keyType, this.valueType);
        }

        public SortedMap<K, V> headMap(K k2) {
            return Collections.checkedSortedMap(this.sm.headMap(k2), this.keyType, this.valueType);
        }

        public SortedMap<K, V> tailMap(K k2) {
            return Collections.checkedSortedMap(this.sm.tailMap(k2), this.keyType, this.valueType);
        }
    }

    public static <K, V> NavigableMap<K, V> checkedNavigableMap(NavigableMap<K, V> navigableMap, Class<K> cls, Class<V> cls2) {
        return new CheckedNavigableMap(navigableMap, cls, cls2);
    }

    /* loaded from: rt.jar:java/util/Collections$CheckedNavigableMap.class */
    static class CheckedNavigableMap<K, V> extends CheckedSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        private static final long serialVersionUID = -4852462692372534096L;
        private final NavigableMap<K, V> nm;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
            return tailMap((CheckedNavigableMap<K, V>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
            return headMap((CheckedNavigableMap<K, V>) obj);
        }

        CheckedNavigableMap(NavigableMap<K, V> navigableMap, Class<K> cls, Class<V> cls2) {
            super(navigableMap, cls, cls2);
            this.nm = navigableMap;
        }

        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public Comparator<? super K> comparator() {
            return this.nm.comparator();
        }

        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public K firstKey() {
            return this.nm.firstKey();
        }

        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public K lastKey() {
            return this.nm.lastKey();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lowerEntry(K k2) {
            Map.Entry<K, V> entryLowerEntry = this.nm.lowerEntry(k2);
            if (null != entryLowerEntry) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(entryLowerEntry, this.valueType);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public K lowerKey(K k2) {
            return this.nm.lowerKey(k2);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> floorEntry(K k2) {
            Map.Entry<K, V> entryFloorEntry = this.nm.floorEntry(k2);
            if (null != entryFloorEntry) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(entryFloorEntry, this.valueType);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public K floorKey(K k2) {
            return this.nm.floorKey(k2);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> ceilingEntry(K k2) {
            Map.Entry<K, V> entryCeilingEntry = this.nm.ceilingEntry(k2);
            if (null != entryCeilingEntry) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(entryCeilingEntry, this.valueType);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public K ceilingKey(K k2) {
            return this.nm.ceilingKey(k2);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> higherEntry(K k2) {
            Map.Entry<K, V> entryHigherEntry = this.nm.higherEntry(k2);
            if (null != entryHigherEntry) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(entryHigherEntry, this.valueType);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public K higherKey(K k2) {
            return this.nm.higherKey(k2);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> firstEntry() {
            Map.Entry<K, V> entryFirstEntry = this.nm.firstEntry();
            if (null != entryFirstEntry) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(entryFirstEntry, this.valueType);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> lastEntry() {
            Map.Entry<K, V> entryLastEntry = this.nm.lastEntry();
            if (null != entryLastEntry) {
                return new CheckedMap.CheckedEntrySet.CheckedEntry(entryLastEntry, this.valueType);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollFirstEntry() {
            Map.Entry<K, V> entryPollFirstEntry = this.nm.pollFirstEntry();
            if (null == entryPollFirstEntry) {
                return null;
            }
            return new CheckedMap.CheckedEntrySet.CheckedEntry(entryPollFirstEntry, this.valueType);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, V> pollLastEntry() {
            Map.Entry<K, V> entryPollLastEntry = this.nm.pollLastEntry();
            if (null == entryPollLastEntry) {
                return null;
            }
            return new CheckedMap.CheckedEntrySet.CheckedEntry(entryPollLastEntry, this.valueType);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            return Collections.checkedNavigableMap(this.nm.descendingMap(), this.keyType, this.valueType);
        }

        @Override // java.util.Collections.CheckedMap, java.util.Map
        public NavigableSet<K> keySet() {
            return navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return Collections.checkedNavigableSet(this.nm.navigableKeySet(), this.keyType);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return Collections.checkedNavigableSet(this.nm.descendingKeySet(), this.keyType);
        }

        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public NavigableMap<K, V> subMap(K k2, K k3) {
            return Collections.checkedNavigableMap(this.nm.subMap(k2, true, k3, false), this.keyType, this.valueType);
        }

        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public NavigableMap<K, V> headMap(K k2) {
            return Collections.checkedNavigableMap(this.nm.headMap(k2, false), this.keyType, this.valueType);
        }

        @Override // java.util.Collections.CheckedSortedMap, java.util.SortedMap
        public NavigableMap<K, V> tailMap(K k2) {
            return Collections.checkedNavigableMap(this.nm.tailMap(k2, true), this.keyType, this.valueType);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
            return Collections.checkedNavigableMap(this.nm.subMap(k2, z2, k3, z3), this.keyType, this.valueType);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(K k2, boolean z2) {
            return Collections.checkedNavigableMap(this.nm.headMap(k2, z2), this.keyType, this.valueType);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(K k2, boolean z2) {
            return Collections.checkedNavigableMap(this.nm.tailMap(k2, z2), this.keyType, this.valueType);
        }
    }

    public static <T> Iterator<T> emptyIterator() {
        return EmptyIterator.EMPTY_ITERATOR;
    }

    /* loaded from: rt.jar:java/util/Collections$EmptyIterator.class */
    private static class EmptyIterator<E> implements Iterator<E> {
        static final EmptyIterator<Object> EMPTY_ITERATOR = new EmptyIterator<>();

        private EmptyIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public E next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new IllegalStateException();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
        }
    }

    public static <T> ListIterator<T> emptyListIterator() {
        return EmptyListIterator.EMPTY_ITERATOR;
    }

    /* loaded from: rt.jar:java/util/Collections$EmptyListIterator.class */
    private static class EmptyListIterator<E> extends EmptyIterator<E> implements ListIterator<E> {
        static final EmptyListIterator<Object> EMPTY_ITERATOR = new EmptyListIterator<>();

        private EmptyListIterator() {
            super();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return false;
        }

        @Override // java.util.ListIterator
        public E previous() {
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
        public void set(E e2) {
            throw new IllegalStateException();
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            throw new UnsupportedOperationException();
        }
    }

    public static <T> Enumeration<T> emptyEnumeration() {
        return EmptyEnumeration.EMPTY_ENUMERATION;
    }

    /* loaded from: rt.jar:java/util/Collections$EmptyEnumeration.class */
    private static class EmptyEnumeration<E> implements Enumeration<E> {
        static final EmptyEnumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration<>();

        private EmptyEnumeration() {
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return false;
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public E nextElement2() {
            throw new NoSuchElementException();
        }
    }

    public static final <T> Set<T> emptySet() {
        return EMPTY_SET;
    }

    /* loaded from: rt.jar:java/util/Collections$EmptySet.class */
    private static class EmptySet<E> extends AbstractSet<E> implements Serializable {
        private static final long serialVersionUID = 1582296315990362920L;

        private EmptySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return Collections.emptyIterator();
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
        public boolean containsAll(Collection<?> collection) {
            return collection.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return new Object[0];
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            Objects.requireNonNull(predicate);
            return false;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        private Object readResolve() {
            return Collections.EMPTY_SET;
        }
    }

    public static <E> SortedSet<E> emptySortedSet() {
        return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
    }

    public static <E> NavigableSet<E> emptyNavigableSet() {
        return UnmodifiableNavigableSet.EMPTY_NAVIGABLE_SET;
    }

    public static final <T> List<T> emptyList() {
        return EMPTY_LIST;
    }

    /* loaded from: rt.jar:java/util/Collections$EmptyList.class */
    private static class EmptyList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 8842843931221139166L;

        private EmptyList() {
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<E> listIterator() {
            return Collections.emptyListIterator();
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
        public boolean containsAll(Collection<?> collection) {
            return collection.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return new Object[0];
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i2) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return (obj instanceof List) && ((List) obj).isEmpty();
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            return 1;
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            Objects.requireNonNull(predicate);
            return false;
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<E> unaryOperator) {
            Objects.requireNonNull(unaryOperator);
        }

        @Override // java.util.List, com.sun.javafx.collections.SortableList
        public void sort(Comparator<? super E> comparator) {
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        private Object readResolve() {
            return Collections.EMPTY_LIST;
        }
    }

    public static final <K, V> Map<K, V> emptyMap() {
        return EMPTY_MAP;
    }

    public static final <K, V> SortedMap<K, V> emptySortedMap() {
        return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
    }

    public static final <K, V> NavigableMap<K, V> emptyNavigableMap() {
        return UnmodifiableNavigableMap.EMPTY_NAVIGABLE_MAP;
    }

    /* loaded from: rt.jar:java/util/Collections$EmptyMap.class */
    private static class EmptyMap<K, V> extends AbstractMap<K, V> implements Serializable {
        private static final long serialVersionUID = 6428348081105594320L;

        private EmptyMap() {
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
        public boolean containsKey(Object obj) {
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object obj) {
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return Collections.emptySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            return Collections.emptySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean equals(Object obj) {
            return (obj instanceof Map) && ((Map) obj).isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int hashCode() {
            return 0;
        }

        @Override // java.util.Map
        public V getOrDefault(Object obj, V v2) {
            return v2;
        }

        @Override // java.util.Map
        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            Objects.requireNonNull(biConsumer);
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            Objects.requireNonNull(biFunction);
        }

        @Override // java.util.Map
        public V putIfAbsent(K k2, V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean remove(Object obj, Object obj2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean replace(K k2, V v2, V v3) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V replace(K k2, V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        private Object readResolve() {
            return Collections.EMPTY_MAP;
        }
    }

    public static <T> Set<T> singleton(T t2) {
        return new SingletonSet(t2);
    }

    static <E> Iterator<E> singletonIterator(final E e2) {
        return new Iterator<E>() { // from class: java.util.Collections.1
            private boolean hasNext = true;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.hasNext;
            }

            @Override // java.util.Iterator
            public E next() {
                if (this.hasNext) {
                    this.hasNext = false;
                    return (E) e2;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Iterator
            public void forEachRemaining(Consumer<? super E> consumer) {
                Objects.requireNonNull(consumer);
                if (this.hasNext) {
                    consumer.accept((Object) e2);
                    this.hasNext = false;
                }
            }
        };
    }

    static <T> Spliterator<T> singletonSpliterator(final T t2) {
        return new Spliterator<T>() { // from class: java.util.Collections.2
            long est = 1;

            @Override // java.util.Spliterator
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                if (this.est > 0) {
                    this.est--;
                    consumer.accept((Object) t2);
                    return true;
                }
                return false;
            }

            @Override // java.util.Spliterator
            public void forEachRemaining(Consumer<? super T> consumer) {
                tryAdvance(consumer);
            }

            @Override // java.util.Spliterator
            public long estimateSize() {
                return this.est;
            }

            @Override // java.util.Spliterator
            public int characteristics() {
                return (t2 != null ? 256 : 0) | 64 | 16384 | 1024 | 1 | 16;
            }
        };
    }

    /* loaded from: rt.jar:java/util/Collections$SingletonSet.class */
    private static class SingletonSet<E> extends AbstractSet<E> implements Serializable {
        private static final long serialVersionUID = 3193687207550431679L;
        private final E element;

        SingletonSet(E e2) {
            this.element = e2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return Collections.singletonIterator(this.element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return Collections.eq(obj, this.element);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            consumer.accept(this.element);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return Collections.singletonSpliterator(this.element);
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            throw new UnsupportedOperationException();
        }
    }

    public static <T> List<T> singletonList(T t2) {
        return new SingletonList(t2);
    }

    /* loaded from: rt.jar:java/util/Collections$SingletonList.class */
    private static class SingletonList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 3093736618740652951L;
        private final E element;

        SingletonList(E e2) {
            this.element = e2;
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return Collections.singletonIterator(this.element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return 1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return Collections.eq(obj, this.element);
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i2) {
            if (i2 != 0) {
                throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: 1");
            }
            return this.element;
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            consumer.accept(this.element);
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<E> unaryOperator) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List, com.sun.javafx.collections.SortableList
        public void sort(Comparator<? super E> comparator) {
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return Collections.singletonSpliterator(this.element);
        }
    }

    public static <K, V> Map<K, V> singletonMap(K k2, V v2) {
        return new SingletonMap(k2, v2);
    }

    /* loaded from: rt.jar:java/util/Collections$SingletonMap.class */
    private static class SingletonMap<K, V> extends AbstractMap<K, V> implements Serializable {
        private static final long serialVersionUID = -6979724477215052911L;

        /* renamed from: k, reason: collision with root package name */
        private final K f12538k;

        /* renamed from: v, reason: collision with root package name */
        private final V f12539v;
        private transient Set<K> keySet;
        private transient Set<Map.Entry<K, V>> entrySet;
        private transient Collection<V> values;

        SingletonMap(K k2, V v2) {
            this.f12538k = k2;
            this.f12539v = v2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return 1;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return Collections.eq(obj, this.f12538k);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object obj) {
            return Collections.eq(obj, this.f12539v);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            if (Collections.eq(obj, this.f12538k)) {
                return this.f12539v;
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            if (this.keySet == null) {
                this.keySet = Collections.singleton(this.f12538k);
            }
            return this.keySet;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            if (this.entrySet == null) {
                this.entrySet = Collections.singleton(new AbstractMap.SimpleImmutableEntry(this.f12538k, this.f12539v));
            }
            return this.entrySet;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            if (this.values == null) {
                this.values = Collections.singleton(this.f12539v);
            }
            return this.values;
        }

        @Override // java.util.Map
        public V getOrDefault(Object obj, V v2) {
            return Collections.eq(obj, this.f12538k) ? this.f12539v : v2;
        }

        @Override // java.util.Map
        public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
            biConsumer.accept(this.f12538k, this.f12539v);
        }

        @Override // java.util.Map
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V putIfAbsent(K k2, V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean remove(Object obj, Object obj2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public boolean replace(K k2, V v2, V v3) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V replace(K k2, V v2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
            throw new UnsupportedOperationException();
        }
    }

    public static <T> List<T> nCopies(int i2, T t2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("List length = " + i2);
        }
        return new CopiesList(i2, t2);
    }

    /* loaded from: rt.jar:java/util/Collections$CopiesList.class */
    private static class CopiesList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 2739099268398711800L;

        /* renamed from: n, reason: collision with root package name */
        final int f12535n;
        final E element;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Collections.class.desiredAssertionStatus();
        }

        CopiesList(int i2, E e2) {
            if (!$assertionsDisabled && i2 < 0) {
                throw new AssertionError();
            }
            this.f12535n = i2;
            this.element = e2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12535n;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12535n != 0 && Collections.eq(obj, this.element);
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object obj) {
            return contains(obj) ? 0 : -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public int lastIndexOf(Object obj) {
            if (contains(obj)) {
                return this.f12535n - 1;
            }
            return -1;
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i2) {
            if (i2 < 0 || i2 >= this.f12535n) {
                throw new IndexOutOfBoundsException("Index: " + i2 + ", Size: " + this.f12535n);
            }
            return this.element;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            Object[] objArr = new Object[this.f12535n];
            if (this.element != null) {
                Arrays.fill(objArr, 0, this.f12535n, this.element);
            }
            return objArr;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v14, types: [java.lang.Object[]] */
        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            int i2 = this.f12535n;
            if (tArr.length < i2) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i2);
                if (this.element != null) {
                    Arrays.fill(tArr, 0, i2, this.element);
                }
            } else {
                Arrays.fill(tArr, 0, i2, this.element);
                if (tArr.length > i2) {
                    tArr[i2] = null;
                }
            }
            return tArr;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<E> subList(int i2, int i3) {
            if (i2 < 0) {
                throw new IndexOutOfBoundsException("fromIndex = " + i2);
            }
            if (i3 > this.f12535n) {
                throw new IndexOutOfBoundsException("toIndex = " + i3);
            }
            if (i2 > i3) {
                throw new IllegalArgumentException("fromIndex(" + i2 + ") > toIndex(" + i3 + ")");
            }
            return new CopiesList(i3 - i2, this.element);
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            if (this.f12535n == 0) {
                return 1;
            }
            int i2 = 31;
            int i3 = 1;
            for (int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(this.f12535n) + 1; iNumberOfLeadingZeros < 32; iNumberOfLeadingZeros++) {
                i3 *= i2 + 1;
                i2 *= i2;
                if ((this.f12535n << iNumberOfLeadingZeros) < 0) {
                    i2 *= 31;
                    i3 = (i3 * 31) + 1;
                }
            }
            return i2 + (i3 * (this.element == null ? 0 : this.element.hashCode()));
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof CopiesList) {
                CopiesList copiesList = (CopiesList) obj;
                return this.f12535n == copiesList.f12535n && (this.f12535n == 0 || Collections.eq(this.element, copiesList.element));
            }
            if (!(obj instanceof List)) {
                return false;
            }
            int i2 = this.f12535n;
            E e2 = this.element;
            Iterator<E> it = ((List) obj).iterator();
            if (e2 == null) {
                while (it.hasNext()) {
                    int i3 = i2;
                    i2--;
                    if (i3 <= 0) {
                        break;
                    }
                    if (it.next() != null) {
                        return false;
                    }
                }
            } else {
                while (it.hasNext()) {
                    int i4 = i2;
                    i2--;
                    if (i4 <= 0) {
                        break;
                    }
                    if (!e2.equals(it.next())) {
                        return false;
                    }
                }
            }
            return i2 == 0 && !it.hasNext();
        }

        @Override // java.util.Collection
        public Stream<E> stream() {
            return IntStream.range(0, this.f12535n).mapToObj(i2 -> {
                return this.element;
            });
        }

        @Override // java.util.Collection
        public Stream<E> parallelStream() {
            return IntStream.range(0, this.f12535n).parallel().mapToObj(i2 -> {
                return this.element;
            });
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return stream().spliterator2();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Object[].class, this.f12535n);
        }
    }

    public static <T> Comparator<T> reverseOrder() {
        return ReverseComparator.REVERSE_ORDER;
    }

    /* loaded from: rt.jar:java/util/Collections$ReverseComparator.class */
    private static class ReverseComparator implements Comparator<Comparable<Object>>, Serializable {
        private static final long serialVersionUID = 7207038068494060240L;
        static final ReverseComparator REVERSE_ORDER = new ReverseComparator();

        private ReverseComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Comparable<Object> comparable, Comparable<Object> comparable2) {
            return comparable2.compareTo(comparable);
        }

        private Object readResolve() {
            return Collections.reverseOrder();
        }

        @Override // java.util.Comparator
        public Comparator<Comparable<Object>> reversed() {
            return Comparator.naturalOrder();
        }
    }

    public static <T> Comparator<T> reverseOrder(Comparator<T> comparator) {
        if (comparator == null) {
            return reverseOrder();
        }
        if (comparator instanceof ReverseComparator2) {
            return ((ReverseComparator2) comparator).cmp;
        }
        return new ReverseComparator2(comparator);
    }

    /* loaded from: rt.jar:java/util/Collections$ReverseComparator2.class */
    private static class ReverseComparator2<T> implements Comparator<T>, Serializable {
        private static final long serialVersionUID = 4374092139857L;
        final Comparator<T> cmp;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Collections.class.desiredAssertionStatus();
        }

        ReverseComparator2(Comparator<T> comparator) {
            if (!$assertionsDisabled && comparator == null) {
                throw new AssertionError();
            }
            this.cmp = comparator;
        }

        @Override // java.util.Comparator
        public int compare(T t2, T t3) {
            return this.cmp.compare(t3, t2);
        }

        @Override // java.util.Comparator
        public boolean equals(Object obj) {
            return obj == this || ((obj instanceof ReverseComparator2) && this.cmp.equals(((ReverseComparator2) obj).cmp));
        }

        public int hashCode() {
            return this.cmp.hashCode() ^ Integer.MIN_VALUE;
        }

        @Override // java.util.Comparator
        public Comparator<T> reversed() {
            return this.cmp;
        }
    }

    public static <T> Enumeration<T> enumeration(final Collection<T> collection) {
        return new Enumeration<T>() { // from class: java.util.Collections.3

            /* renamed from: i, reason: collision with root package name */
            private final Iterator<T> f12529i;

            {
                this.f12529i = collection.iterator();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.f12529i.hasNext();
            }

            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public T nextElement2() {
                return this.f12529i.next();
            }
        };
    }

    public static <T> ArrayList<T> list(Enumeration<T> enumeration) {
        ArrayList<T> arrayList = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            arrayList.add(enumeration.nextElement2());
        }
        return arrayList;
    }

    static boolean eq(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    public static int frequency(Collection<?> collection, Object obj) {
        int i2 = 0;
        if (obj == null) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (it.next() == null) {
                    i2++;
                }
            }
        } else {
            Iterator<?> it2 = collection.iterator();
            while (it2.hasNext()) {
                if (obj.equals(it2.next())) {
                    i2++;
                }
            }
        }
        return i2;
    }

    public static boolean disjoint(Collection<?> collection, Collection<?> collection2) {
        Collection<?> collection3 = collection2;
        Collection<?> collection4 = collection;
        if (collection instanceof Set) {
            collection4 = collection2;
            collection3 = collection;
        } else if (!(collection2 instanceof Set)) {
            int size = collection.size();
            int size2 = collection2.size();
            if (size == 0 || size2 == 0) {
                return true;
            }
            if (size > size2) {
                collection4 = collection2;
                collection3 = collection;
            }
        }
        Iterator<?> it = collection4.iterator();
        while (it.hasNext()) {
            if (collection3.contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public static <T> boolean addAll(Collection<? super T> collection, T... tArr) {
        boolean zAdd = false;
        for (T t2 : tArr) {
            zAdd |= collection.add((Object) t2);
        }
        return zAdd;
    }

    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return new SetFromMap(map);
    }

    /* loaded from: rt.jar:java/util/Collections$SetFromMap.class */
    private static class SetFromMap<E> extends AbstractSet<E> implements Set<E>, Serializable {

        /* renamed from: m, reason: collision with root package name */
        private final Map<E, Boolean> f12536m;

        /* renamed from: s, reason: collision with root package name */
        private transient Set<E> f12537s;
        private static final long serialVersionUID = 2454657854757543876L;

        SetFromMap(Map<E, Boolean> map) {
            if (!map.isEmpty()) {
                throw new IllegalArgumentException("Map is non-empty");
            }
            this.f12536m = map;
            this.f12537s = map.keySet();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12536m.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12536m.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12536m.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12536m.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.f12536m.remove(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(E e2) {
            return this.f12536m.put(e2, Boolean.TRUE) == null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return this.f12537s.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return this.f12537s.toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.f12537s.toArray(tArr);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return this.f12537s.toString();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.List
        public int hashCode() {
            return this.f12537s.hashCode();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return obj == this || this.f12537s.equals(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return this.f12537s.containsAll(collection);
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            return this.f12537s.removeAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            return this.f12537s.retainAll(collection);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            this.f12537s.forEach(consumer);
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            return this.f12537s.removeIf(predicate);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return this.f12537s.spliterator();
        }

        @Override // java.util.Collection
        public Stream<E> stream() {
            return this.f12537s.stream();
        }

        @Override // java.util.Collection
        public Stream<E> parallelStream() {
            return this.f12537s.parallelStream();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.f12537s = this.f12536m.keySet();
        }
    }

    public static <T> Queue<T> asLifoQueue(Deque<T> deque) {
        return new AsLIFOQueue(deque);
    }

    /* loaded from: rt.jar:java/util/Collections$AsLIFOQueue.class */
    static class AsLIFOQueue<E> extends AbstractQueue<E> implements Queue<E>, Serializable {
        private static final long serialVersionUID = 1802017725587941708L;

        /* renamed from: q, reason: collision with root package name */
        private final Deque<E> f12530q;

        AsLIFOQueue(Deque<E> deque) {
            this.f12530q = deque;
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(E e2) {
            this.f12530q.addFirst(e2);
            return true;
        }

        @Override // java.util.Queue
        public boolean offer(E e2) {
            return this.f12530q.offerFirst(e2);
        }

        @Override // java.util.Queue
        public E poll() {
            return this.f12530q.pollFirst();
        }

        @Override // java.util.AbstractQueue, java.util.Queue
        public E remove() {
            return this.f12530q.removeFirst();
        }

        @Override // java.util.Queue
        public E peek() {
            return this.f12530q.peekFirst();
        }

        @Override // java.util.AbstractQueue, java.util.Queue
        public E element() {
            return this.f12530q.getFirst();
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12530q.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12530q.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12530q.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12530q.contains(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.f12530q.remove(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return this.f12530q.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return this.f12530q.toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.f12530q.toArray(tArr);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return this.f12530q.toString();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return this.f12530q.containsAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            return this.f12530q.removeAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            return this.f12530q.retainAll(collection);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            this.f12530q.forEach(consumer);
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super E> predicate) {
            return this.f12530q.removeIf(predicate);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return this.f12530q.spliterator();
        }

        @Override // java.util.Collection
        public Stream<E> stream() {
            return this.f12530q.stream();
        }

        @Override // java.util.Collection
        public Stream<E> parallelStream() {
            return this.f12530q.parallelStream();
        }
    }
}
