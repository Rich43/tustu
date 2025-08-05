package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerLong.class */
final class PrimitiveArrayListerLong<BeanT> extends Lister<BeanT, long[], Long, LongArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(LongArrayPack longArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(longArrayPack, (LongArrayPack) obj, (Accessor<LongArrayPack, long[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ LongArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerLong<BeanT>) obj, (Accessor<PrimitiveArrayListerLong<BeanT>, long[]>) accessor);
    }

    private PrimitiveArrayListerLong() {
    }

    static void register() {
        Lister.primitiveArrayListers.put(Long.TYPE, new PrimitiveArrayListerLong());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ListIterator<Long> iterator(final long[] objects, XMLSerializer context) {
        return new ListIterator<Long>() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerLong.1
            int idx = 0;

            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public Long next() {
                long[] jArr = objects;
                int i2 = this.idx;
                this.idx = i2 + 1;
                return Long.valueOf(jArr[i2]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public LongArrayPack startPacking(BeanT current, Accessor<BeanT, long[]> acc) {
        return new LongArrayPack();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void addToPack(LongArrayPack objects, Long o2) {
        objects.add(o2);
    }

    /* renamed from: endPacking, reason: avoid collision after fix types in other method */
    public void endPacking2(LongArrayPack pack, BeanT bean, Accessor<BeanT, long[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o2, Accessor<BeanT, long[]> acc) throws AccessorException {
        acc.set(o2, new long[0]);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerLong$LongArrayPack.class */
    static final class LongArrayPack {
        long[] buf = new long[16];
        int size;

        LongArrayPack() {
        }

        void add(Long b2) {
            if (this.buf.length == this.size) {
                long[] nb = new long[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b2 != null) {
                long[] jArr = this.buf;
                int i2 = this.size;
                this.size = i2 + 1;
                jArr[i2] = b2.longValue();
            }
        }

        long[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            long[] r2 = new long[this.size];
            System.arraycopy(this.buf, 0, r2, 0, this.size);
            return r2;
        }
    }
}
