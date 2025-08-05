package java.util;

import java.lang.Enum;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:java/util/RegularEnumSet.class */
class RegularEnumSet<E extends Enum<E>> extends EnumSet<E> {
    private static final long serialVersionUID = 3411599620347842686L;
    private long elements;

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$002(java.util.RegularEnumSet r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.elements = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.RegularEnumSet.access$002(java.util.RegularEnumSet, long):long");
    }

    RegularEnumSet(Class<E> cls, Enum<?>[] enumArr) {
        super(cls, enumArr);
        this.elements = 0L;
    }

    @Override // java.util.EnumSet
    void addRange(E e2, E e3) {
        this.elements = ((-1) >>> ((e2.ordinal() - e3.ordinal()) - 1)) << e2.ordinal();
    }

    @Override // java.util.EnumSet
    void addAll() {
        if (this.universe.length != 0) {
            this.elements = (-1) >>> (-this.universe.length);
        }
    }

    @Override // java.util.EnumSet
    void complement() {
        if (this.universe.length != 0) {
            this.elements ^= -1;
            this.elements &= (-1) >>> (-this.universe.length);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new EnumSetIterator();
    }

    /* loaded from: rt.jar:java/util/RegularEnumSet$EnumSetIterator.class */
    private class EnumSetIterator<E extends Enum<E>> implements Iterator<E> {
        long unseen;
        long lastReturned = 0;

        EnumSetIterator() {
            this.unseen = RegularEnumSet.this.elements;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.unseen != 0;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.unseen == 0) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.unseen & (-this.unseen);
            this.unseen -= this.lastReturned;
            return (E) RegularEnumSet.this.universe[Long.numberOfTrailingZeros(this.lastReturned)];
        }

        /* JADX WARN: Failed to check method for inline after forced processjava.util.RegularEnumSet.access$002(java.util.RegularEnumSet, long):long */
        @Override // java.util.Iterator
        public void remove() {
            if (this.lastReturned != 0) {
                RegularEnumSet.access$002(RegularEnumSet.this, RegularEnumSet.this.elements & (this.lastReturned ^ (-1)));
                this.lastReturned = 0L;
                return;
            }
            throw new IllegalStateException();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return Long.bitCount(this.elements);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.elements == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        return (cls == this.elementType || cls.getSuperclass() == this.elementType) && (this.elements & (1 << ((Enum) obj).ordinal())) != 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        typeCheck(e2);
        long j2 = this.elements;
        this.elements |= 1 << e2.ordinal();
        return this.elements != j2;
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
        long j2 = this.elements;
        this.elements &= (1 << ((Enum) obj).ordinal()) ^ (-1);
        return this.elements != j2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.containsAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        if (regularEnumSet.elementType != this.elementType) {
            return regularEnumSet.isEmpty();
        }
        return (regularEnumSet.elements & (this.elements ^ (-1))) == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.addAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        if (regularEnumSet.elementType != this.elementType) {
            if (regularEnumSet.isEmpty()) {
                return false;
            }
            throw new ClassCastException(((Object) regularEnumSet.elementType) + " != " + ((Object) this.elementType));
        }
        long j2 = this.elements;
        this.elements |= regularEnumSet.elements;
        return this.elements != j2;
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.removeAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        if (regularEnumSet.elementType != this.elementType) {
            return false;
        }
        long j2 = this.elements;
        this.elements &= regularEnumSet.elements ^ (-1);
        return this.elements != j2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        if (!(collection instanceof RegularEnumSet)) {
            return super.retainAll(collection);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) collection;
        if (regularEnumSet.elementType != this.elementType) {
            boolean z2 = this.elements != 0;
            this.elements = 0L;
            return z2;
        }
        long j2 = this.elements;
        this.elements &= regularEnumSet.elements;
        return this.elements != j2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.elements = 0L;
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (!(obj instanceof RegularEnumSet)) {
            return super.equals(obj);
        }
        RegularEnumSet regularEnumSet = (RegularEnumSet) obj;
        return regularEnumSet.elementType != this.elementType ? this.elements == 0 && regularEnumSet.elements == 0 : regularEnumSet.elements == this.elements;
    }
}
