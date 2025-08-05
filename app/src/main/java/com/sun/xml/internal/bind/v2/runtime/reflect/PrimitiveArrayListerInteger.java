package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerInteger.class */
final class PrimitiveArrayListerInteger<BeanT> extends Lister<BeanT, int[], Integer, IntegerArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(IntegerArrayPack integerArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(integerArrayPack, (IntegerArrayPack) obj, (Accessor<IntegerArrayPack, int[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ IntegerArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerInteger<BeanT>) obj, (Accessor<PrimitiveArrayListerInteger<BeanT>, int[]>) accessor);
    }

    private PrimitiveArrayListerInteger() {
    }

    static void register() {
        Lister.primitiveArrayListers.put(Integer.TYPE, new PrimitiveArrayListerInteger());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ListIterator<Integer> iterator(final int[] objects, XMLSerializer context) {
        return new ListIterator<Integer>() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerInteger.1
            int idx = 0;

            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public Integer next() {
                int[] iArr = objects;
                int i2 = this.idx;
                this.idx = i2 + 1;
                return Integer.valueOf(iArr[i2]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public IntegerArrayPack startPacking(BeanT current, Accessor<BeanT, int[]> acc) {
        return new IntegerArrayPack();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void addToPack(IntegerArrayPack objects, Integer o2) {
        objects.add(o2);
    }

    /* renamed from: endPacking, reason: avoid collision after fix types in other method */
    public void endPacking2(IntegerArrayPack pack, BeanT bean, Accessor<BeanT, int[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o2, Accessor<BeanT, int[]> acc) throws AccessorException {
        acc.set(o2, new int[0]);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerInteger$IntegerArrayPack.class */
    static final class IntegerArrayPack {
        int[] buf = new int[16];
        int size;

        IntegerArrayPack() {
        }

        void add(Integer b2) {
            if (this.buf.length == this.size) {
                int[] nb = new int[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b2 != null) {
                int[] iArr = this.buf;
                int i2 = this.size;
                this.size = i2 + 1;
                iArr[i2] = b2.intValue();
            }
        }

        int[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            int[] r2 = new int[this.size];
            System.arraycopy(this.buf, 0, r2, 0, this.size);
            return r2;
        }
    }
}
