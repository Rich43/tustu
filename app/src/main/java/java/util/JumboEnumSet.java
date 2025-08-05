package java.util;

import java.lang.Enum;

/* loaded from: rt.jar:java/util/JumboEnumSet.class */
class JumboEnumSet<E extends Enum<E>> extends EnumSet<E> {
    private static final long serialVersionUID = 334349849919042784L;
    private long[] elements;
    private int size;

    static /* synthetic */ int access$110(JumboEnumSet jumboEnumSet) {
        int i2 = jumboEnumSet.size;
        jumboEnumSet.size = i2 - 1;
        return i2;
    }

    JumboEnumSet(Class<E> cls, Enum<?>[] enumArr) {
        super(cls, enumArr);
        this.size = 0;
        this.elements = new long[(enumArr.length + 63) >>> 6];
    }

    @Override // java.util.EnumSet
    void addRange(E e2, E e3) {
        int iOrdinal = e2.ordinal() >>> 6;
        int iOrdinal2 = e3.ordinal() >>> 6;
        if (iOrdinal == iOrdinal2) {
            this.elements[iOrdinal] = ((-1) >>> ((e2.ordinal() - e3.ordinal()) - 1)) << e2.ordinal();
        } else {
            this.elements[iOrdinal] = (-1) << e2.ordinal();
            for (int i2 = iOrdinal + 1; i2 < iOrdinal2; i2++) {
                this.elements[i2] = -1;
            }
            this.elements[iOrdinal2] = (-1) >>> (63 - e3.ordinal());
        }
        this.size = (e3.ordinal() - e2.ordinal()) + 1;
    }

    @Override // java.util.EnumSet
    void addAll() {
        for (int i2 = 0; i2 < this.elements.length; i2++) {
            this.elements[i2] = -1;
        }
        long[] jArr = this.elements;
        int length = this.elements.length - 1;
        jArr[length] = jArr[length] >>> (-this.universe.length);
        this.size = this.universe.length;
    }

    @Override // java.util.EnumSet
    void complement() {
        for (int i2 = 0; i2 < this.elements.length; i2++) {
            this.elements[i2] = this.elements[i2] ^ (-1);
        }
        long[] jArr = this.elements;
        int length = this.elements.length - 1;
        jArr[length] = jArr[length] & ((-1) >>> (-this.universe.length));
        this.size = this.universe.length - this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new EnumSetIterator();
    }

    /* loaded from: rt.jar:java/util/JumboEnumSet$EnumSetIterator.class */
    private class EnumSetIterator<E extends Enum<E>> implements Iterator<E> {
        long unseen;
        int unseenIndex = 0;
        long lastReturned = 0;
        int lastReturnedIndex = 0;

        EnumSetIterator() {
            this.unseen = JumboEnumSet.this.elements[0];
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            while (this.unseen == 0 && this.unseenIndex < JumboEnumSet.this.elements.length - 1) {
                long[] jArr = JumboEnumSet.this.elements;
                int i2 = this.unseenIndex + 1;
                this.unseenIndex = i2;
                this.unseen = jArr[i2];
            }
            return this.unseen != 0;
        }

        @Override // java.util.Iterator
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.unseen & (-this.unseen);
            this.lastReturnedIndex = this.unseenIndex;
            this.unseen -= this.lastReturned;
            return (E) JumboEnumSet.this.universe[(this.lastReturnedIndex << 6) + Long.numberOfTrailingZeros(this.lastReturned)];
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastReturned != 0) {
                long j2 = JumboEnumSet.this.elements[this.lastReturnedIndex];
                long[] jArr = JumboEnumSet.this.elements;
                int i2 = this.lastReturnedIndex;
                jArr[i2] = jArr[i2] & (this.lastReturned ^ (-1));
                if (j2 != JumboEnumSet.this.elements[this.lastReturnedIndex]) {
                    JumboEnumSet.access$110(JumboEnumSet.this);
                }
                this.lastReturned = 0L;
                return;
            }
            throw new IllegalStateException();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            return false;
        }
        int iOrdinal = ((Enum) obj).ordinal();
        return (this.elements[iOrdinal >>> 6] & (1 << iOrdinal)) != 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        typeCheck(e2);
        int iOrdinal = e2.ordinal();
        int i2 = iOrdinal >>> 6;
        long j2 = this.elements[i2];
        long[] jArr = this.elements;
        jArr[i2] = jArr[i2] | (1 << iOrdinal);
        boolean z2 = this.elements[i2] != j2;
        if (z2) {
            this.size++;
        }
        return z2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            return false;
        }
        int iOrdinal = ((Enum) obj).ordinal();
        int i2 = iOrdinal >>> 6;
        long j2 = this.elements[i2];
        long[] jArr = this.elements;
        jArr[i2] = jArr[i2] & ((1 << iOrdinal) ^ (-1));
        boolean z2 = this.elements[i2] != j2;
        if (z2) {
            this.size--;
        }
        return z2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        if (!(collection instanceof JumboEnumSet)) {
            return super.containsAll(collection);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) collection;
        if (jumboEnumSet.elementType != this.elementType) {
            return jumboEnumSet.isEmpty();
        }
        for (int i2 = 0; i2 < this.elements.length; i2++) {
            if ((jumboEnumSet.elements[i2] & (this.elements[i2] ^ (-1))) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        if (!(collection instanceof JumboEnumSet)) {
            return super.addAll(collection);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) collection;
        if (jumboEnumSet.elementType != this.elementType) {
            if (jumboEnumSet.isEmpty()) {
                return false;
            }
            throw new ClassCastException(((Object) jumboEnumSet.elementType) + " != " + ((Object) this.elementType));
        }
        for (int i2 = 0; i2 < this.elements.length; i2++) {
            long[] jArr = this.elements;
            int i3 = i2;
            jArr[i3] = jArr[i3] | jumboEnumSet.elements[i2];
        }
        return recalculateSize();
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        if (!(collection instanceof JumboEnumSet)) {
            return super.removeAll(collection);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) collection;
        if (jumboEnumSet.elementType != this.elementType) {
            return false;
        }
        for (int i2 = 0; i2 < this.elements.length; i2++) {
            long[] jArr = this.elements;
            int i3 = i2;
            jArr[i3] = jArr[i3] & (jumboEnumSet.elements[i2] ^ (-1));
        }
        return recalculateSize();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        if (!(collection instanceof JumboEnumSet)) {
            return super.retainAll(collection);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) collection;
        if (jumboEnumSet.elementType != this.elementType) {
            boolean z2 = this.size != 0;
            clear();
            return z2;
        }
        for (int i2 = 0; i2 < this.elements.length; i2++) {
            long[] jArr = this.elements;
            int i3 = i2;
            jArr[i3] = jArr[i3] & jumboEnumSet.elements[i2];
        }
        return recalculateSize();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        Arrays.fill(this.elements, 0L);
        this.size = 0;
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (!(obj instanceof JumboEnumSet)) {
            return super.equals(obj);
        }
        JumboEnumSet jumboEnumSet = (JumboEnumSet) obj;
        if (jumboEnumSet.elementType != this.elementType) {
            return this.size == 0 && jumboEnumSet.size == 0;
        }
        return Arrays.equals(jumboEnumSet.elements, this.elements);
    }

    private boolean recalculateSize() {
        int i2 = this.size;
        this.size = 0;
        for (long j2 : this.elements) {
            this.size += Long.bitCount(j2);
        }
        return this.size != i2;
    }

    @Override // java.util.EnumSet
    /* renamed from: clone */
    public EnumSet<E> mo3394clone() {
        JumboEnumSet jumboEnumSet = (JumboEnumSet) super.mo3394clone();
        jumboEnumSet.elements = (long[]) jumboEnumSet.elements.clone();
        return jumboEnumSet;
    }
}
