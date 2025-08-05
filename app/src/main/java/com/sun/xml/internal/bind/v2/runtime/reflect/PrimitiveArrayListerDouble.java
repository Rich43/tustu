package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerDouble.class */
final class PrimitiveArrayListerDouble<BeanT> extends Lister<BeanT, double[], Double, DoubleArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(DoubleArrayPack doubleArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(doubleArrayPack, (DoubleArrayPack) obj, (Accessor<DoubleArrayPack, double[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ DoubleArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerDouble<BeanT>) obj, (Accessor<PrimitiveArrayListerDouble<BeanT>, double[]>) accessor);
    }

    private PrimitiveArrayListerDouble() {
    }

    static void register() {
        Lister.primitiveArrayListers.put(Double.TYPE, new PrimitiveArrayListerDouble());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ListIterator<Double> iterator(final double[] objects, XMLSerializer context) {
        return new ListIterator<Double>() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerDouble.1
            int idx = 0;

            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public Double next() {
                double[] dArr = objects;
                int i2 = this.idx;
                this.idx = i2 + 1;
                return Double.valueOf(dArr[i2]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public DoubleArrayPack startPacking(BeanT current, Accessor<BeanT, double[]> acc) {
        return new DoubleArrayPack();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void addToPack(DoubleArrayPack objects, Double o2) {
        objects.add(o2);
    }

    /* renamed from: endPacking, reason: avoid collision after fix types in other method */
    public void endPacking2(DoubleArrayPack pack, BeanT bean, Accessor<BeanT, double[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o2, Accessor<BeanT, double[]> acc) throws AccessorException {
        acc.set(o2, new double[0]);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerDouble$DoubleArrayPack.class */
    static final class DoubleArrayPack {
        double[] buf = new double[16];
        int size;

        DoubleArrayPack() {
        }

        void add(Double b2) {
            if (this.buf.length == this.size) {
                double[] nb = new double[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b2 != null) {
                double[] dArr = this.buf;
                int i2 = this.size;
                this.size = i2 + 1;
                dArr[i2] = b2.doubleValue();
            }
        }

        double[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            double[] r2 = new double[this.size];
            System.arraycopy(this.buf, 0, r2, 0, this.size);
            return r2;
        }
    }
}
