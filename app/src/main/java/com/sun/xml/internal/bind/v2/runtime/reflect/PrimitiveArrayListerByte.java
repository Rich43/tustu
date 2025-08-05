package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerByte.class */
final class PrimitiveArrayListerByte<BeanT> extends Lister<BeanT, byte[], Byte, ByteArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(ByteArrayPack byteArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(byteArrayPack, (ByteArrayPack) obj, (Accessor<ByteArrayPack, byte[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ ByteArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerByte<BeanT>) obj, (Accessor<PrimitiveArrayListerByte<BeanT>, byte[]>) accessor);
    }

    private PrimitiveArrayListerByte() {
    }

    static void register() {
        Lister.primitiveArrayListers.put(Byte.TYPE, new PrimitiveArrayListerByte());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ListIterator<Byte> iterator(final byte[] objects, XMLSerializer context) {
        return new ListIterator<Byte>() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerByte.1
            int idx = 0;

            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public Byte next() {
                byte[] bArr = objects;
                int i2 = this.idx;
                this.idx = i2 + 1;
                return Byte.valueOf(bArr[i2]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ByteArrayPack startPacking(BeanT current, Accessor<BeanT, byte[]> acc) {
        return new ByteArrayPack();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void addToPack(ByteArrayPack objects, Byte o2) {
        objects.add(o2);
    }

    /* renamed from: endPacking, reason: avoid collision after fix types in other method */
    public void endPacking2(ByteArrayPack pack, BeanT bean, Accessor<BeanT, byte[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o2, Accessor<BeanT, byte[]> acc) throws AccessorException {
        acc.set(o2, new byte[0]);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerByte$ByteArrayPack.class */
    static final class ByteArrayPack {
        byte[] buf = new byte[16];
        int size;

        ByteArrayPack() {
        }

        void add(Byte b2) {
            if (this.buf.length == this.size) {
                byte[] nb = new byte[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b2 != null) {
                byte[] bArr = this.buf;
                int i2 = this.size;
                this.size = i2 + 1;
                bArr[i2] = b2.byteValue();
            }
        }

        byte[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            byte[] r2 = new byte[this.size];
            System.arraycopy(this.buf, 0, r2, 0, this.size);
            return r2;
        }
    }
}
