package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerShort.class */
final class PrimitiveArrayListerShort<BeanT> extends Lister<BeanT, short[], Short, ShortArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(ShortArrayPack shortArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(shortArrayPack, (ShortArrayPack) obj, (Accessor<ShortArrayPack, short[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ ShortArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerShort<BeanT>) obj, (Accessor<PrimitiveArrayListerShort<BeanT>, short[]>) accessor);
    }

    private PrimitiveArrayListerShort() {
    }

    static void register() {
        Lister.primitiveArrayListers.put(Short.TYPE, new PrimitiveArrayListerShort());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ListIterator<Short> iterator(final short[] objects, XMLSerializer context) {
        return new ListIterator<Short>() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerShort.1
            int idx = 0;

            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public Short next() {
                short[] sArr = objects;
                int i2 = this.idx;
                this.idx = i2 + 1;
                return Short.valueOf(sArr[i2]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ShortArrayPack startPacking(BeanT current, Accessor<BeanT, short[]> acc) {
        return new ShortArrayPack();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void addToPack(ShortArrayPack objects, Short o2) {
        objects.add(o2);
    }

    /* renamed from: endPacking, reason: avoid collision after fix types in other method */
    public void endPacking2(ShortArrayPack pack, BeanT bean, Accessor<BeanT, short[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o2, Accessor<BeanT, short[]> acc) throws AccessorException {
        acc.set(o2, new short[0]);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerShort$ShortArrayPack.class */
    static final class ShortArrayPack {
        short[] buf = new short[16];
        int size;

        ShortArrayPack() {
        }

        void add(Short b2) {
            if (this.buf.length == this.size) {
                short[] nb = new short[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b2 != null) {
                short[] sArr = this.buf;
                int i2 = this.size;
                this.size = i2 + 1;
                sArr[i2] = b2.shortValue();
            }
        }

        short[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            short[] r2 = new short[this.size];
            System.arraycopy(this.buf, 0, r2, 0, this.size);
            return r2;
        }
    }
}
