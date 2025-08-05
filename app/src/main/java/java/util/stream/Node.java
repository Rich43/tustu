package java.util.stream;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.stream.Sink;

/* loaded from: rt.jar:java/util/stream/Node.class */
interface Node<T> {

    /* loaded from: rt.jar:java/util/stream/Node$Builder.class */
    public interface Builder<T> extends Sink<T> {

        /* loaded from: rt.jar:java/util/stream/Node$Builder$OfDouble.class */
        public interface OfDouble extends Builder<Double>, Sink.OfDouble {
            @Override // java.util.stream.Node.Builder
            /* renamed from: build */
            Node<Double> build2();
        }

        /* loaded from: rt.jar:java/util/stream/Node$Builder$OfInt.class */
        public interface OfInt extends Builder<Integer>, Sink.OfInt {
            @Override // java.util.stream.Node.Builder
            /* renamed from: build */
            Node<Integer> build2();
        }

        /* loaded from: rt.jar:java/util/stream/Node$Builder$OfLong.class */
        public interface OfLong extends Builder<Long>, Sink.OfLong {
            @Override // java.util.stream.Node.Builder
            /* renamed from: build */
            Node<Long> build2();
        }

        /* renamed from: build */
        Node<T> build2();
    }

    Spliterator<T> spliterator();

    void forEach(Consumer<? super T> consumer);

    T[] asArray(IntFunction<T[]> intFunction);

    void copyInto(T[] tArr, int i2);

    long count();

    default int getChildCount() {
        return 0;
    }

    default Node<T> getChild(int i2) {
        throw new IndexOutOfBoundsException();
    }

    default Node<T> truncate(long j2, long j3, IntFunction<T[]> intFunction) {
        if (j2 == 0 && j3 == count()) {
            return this;
        }
        Spliterator<T> spliterator = spliterator();
        long j4 = j3 - j2;
        Builder builder = Nodes.builder(j4, intFunction);
        builder.begin(j4);
        for (int i2 = 0; i2 < j2 && spliterator.tryAdvance(obj -> {
        }); i2++) {
        }
        for (int i3 = 0; i3 < j4 && spliterator.tryAdvance(builder); i3++) {
        }
        builder.end();
        return builder.build2();
    }

    default StreamShape getShape() {
        return StreamShape.REFERENCE;
    }

    /* loaded from: rt.jar:java/util/stream/Node$OfPrimitive.class */
    public interface OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_NODE extends OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends Node<T> {
        @Override // java.util.stream.Node
        T_SPLITR spliterator();

        void forEach(T_CONS t_cons);

        @Override // java.util.stream.Node
        T_NODE truncate(long j2, long j3, IntFunction<T[]> intFunction);

        T_ARR asPrimitiveArray();

        T_ARR newArray(int i2);

        void copyInto(T_ARR t_arr, int i2);

        @Override // java.util.stream.Node
        default T_NODE getChild(int i2) {
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.stream.Node
        default T[] asArray(IntFunction<T[]> intFunction) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfPrimitive.asArray");
            }
            if (count() >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            T[] tArrApply = intFunction.apply((int) count());
            copyInto((Object[]) tArrApply, 0);
            return tArrApply;
        }
    }

    /* loaded from: rt.jar:java/util/stream/Node$OfInt.class */
    public interface OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, OfInt> {
        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        /* bridge */ /* synthetic */ default OfPrimitive truncate(long j2, long j3, IntFunction intFunction) {
            return truncate(j2, j3, (IntFunction<Integer[]>) intFunction);
        }

        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        /* bridge */ /* synthetic */ default Node truncate(long j2, long j3, IntFunction intFunction) {
            return truncate(j2, j3, (IntFunction<Integer[]>) intFunction);
        }

        @Override // java.util.stream.Node
        default void forEach(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                forEach((OfInt) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
            }
            spliterator().forEachRemaining(consumer);
        }

        @Override // java.util.stream.Node
        default void copyInto(Integer[] numArr, int i2) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            }
            int[] iArrAsPrimitiveArray = asPrimitiveArray();
            for (int i3 = 0; i3 < iArrAsPrimitiveArray.length; i3++) {
                numArr[i2 + i3] = Integer.valueOf(iArrAsPrimitiveArray[i3]);
            }
        }

        /* JADX WARN: Type inference failed for: r0v20, types: [java.util.stream.Node$OfInt] */
        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        default OfInt truncate(long j2, long j3, IntFunction<Integer[]> intFunction) {
            if (j2 == 0 && j3 == count()) {
                return this;
            }
            long j4 = j3 - j2;
            Spliterator.OfInt ofIntSpliterator = spliterator();
            Builder.OfInt ofIntIntBuilder = Nodes.intBuilder(j4);
            ofIntIntBuilder.begin(j4);
            for (int i2 = 0; i2 < j2 && ofIntSpliterator.tryAdvance(i3 -> {
            }); i2++) {
            }
            for (int i4 = 0; i4 < j4 && ofIntSpliterator.tryAdvance((IntConsumer) ofIntIntBuilder); i4++) {
            }
            ofIntIntBuilder.end();
            return ofIntIntBuilder.build2();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.Node.OfPrimitive
        default int[] newArray(int i2) {
            return new int[i2];
        }

        @Override // java.util.stream.Node
        default StreamShape getShape() {
            return StreamShape.INT_VALUE;
        }
    }

    /* loaded from: rt.jar:java/util/stream/Node$OfLong.class */
    public interface OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, OfLong> {
        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        /* bridge */ /* synthetic */ default OfPrimitive truncate(long j2, long j3, IntFunction intFunction) {
            return truncate(j2, j3, (IntFunction<Long[]>) intFunction);
        }

        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        /* bridge */ /* synthetic */ default Node truncate(long j2, long j3, IntFunction intFunction) {
            return truncate(j2, j3, (IntFunction<Long[]>) intFunction);
        }

        @Override // java.util.stream.Node
        default void forEach(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                forEach((OfLong) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            }
            spliterator().forEachRemaining(consumer);
        }

        @Override // java.util.stream.Node
        default void copyInto(Long[] lArr, int i2) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            }
            long[] jArrAsPrimitiveArray = asPrimitiveArray();
            for (int i3 = 0; i3 < jArrAsPrimitiveArray.length; i3++) {
                lArr[i2 + i3] = Long.valueOf(jArrAsPrimitiveArray[i3]);
            }
        }

        /* JADX WARN: Type inference failed for: r0v20, types: [java.util.stream.Node$OfLong] */
        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        default OfLong truncate(long j2, long j3, IntFunction<Long[]> intFunction) {
            if (j2 == 0 && j3 == count()) {
                return this;
            }
            long j4 = j3 - j2;
            Spliterator.OfLong ofLongSpliterator = spliterator();
            Builder.OfLong ofLongLongBuilder = Nodes.longBuilder(j4);
            ofLongLongBuilder.begin(j4);
            for (int i2 = 0; i2 < j2 && ofLongSpliterator.tryAdvance(j5 -> {
            }); i2++) {
            }
            for (int i3 = 0; i3 < j4 && ofLongSpliterator.tryAdvance((LongConsumer) ofLongLongBuilder); i3++) {
            }
            ofLongLongBuilder.end();
            return ofLongLongBuilder.build2();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.Node.OfPrimitive
        default long[] newArray(int i2) {
            return new long[i2];
        }

        @Override // java.util.stream.Node
        default StreamShape getShape() {
            return StreamShape.LONG_VALUE;
        }
    }

    /* loaded from: rt.jar:java/util/stream/Node$OfDouble.class */
    public interface OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, OfDouble> {
        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        /* bridge */ /* synthetic */ default OfPrimitive truncate(long j2, long j3, IntFunction intFunction) {
            return truncate(j2, j3, (IntFunction<Double[]>) intFunction);
        }

        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        /* bridge */ /* synthetic */ default Node truncate(long j2, long j3, IntFunction intFunction) {
            return truncate(j2, j3, (IntFunction<Double[]>) intFunction);
        }

        @Override // java.util.stream.Node
        default void forEach(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                forEach((OfDouble) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            }
            spliterator().forEachRemaining(consumer);
        }

        @Override // java.util.stream.Node
        default void copyInto(Double[] dArr, int i2) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            }
            double[] dArrAsPrimitiveArray = asPrimitiveArray();
            for (int i3 = 0; i3 < dArrAsPrimitiveArray.length; i3++) {
                dArr[i2 + i3] = Double.valueOf(dArrAsPrimitiveArray[i3]);
            }
        }

        /* JADX WARN: Type inference failed for: r0v20, types: [java.util.stream.Node$OfDouble] */
        @Override // java.util.stream.Node.OfPrimitive, java.util.stream.Node
        default OfDouble truncate(long j2, long j3, IntFunction<Double[]> intFunction) {
            if (j2 == 0 && j3 == count()) {
                return this;
            }
            long j4 = j3 - j2;
            Spliterator.OfDouble ofDoubleSpliterator = spliterator();
            Builder.OfDouble ofDoubleDoubleBuilder = Nodes.doubleBuilder(j4);
            ofDoubleDoubleBuilder.begin(j4);
            for (int i2 = 0; i2 < j2 && ofDoubleSpliterator.tryAdvance(d2 -> {
            }); i2++) {
            }
            for (int i3 = 0; i3 < j4 && ofDoubleSpliterator.tryAdvance((DoubleConsumer) ofDoubleDoubleBuilder); i3++) {
            }
            ofDoubleDoubleBuilder.end();
            return ofDoubleDoubleBuilder.build2();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.Node.OfPrimitive
        default double[] newArray(int i2) {
            return new double[i2];
        }

        @Override // java.util.stream.Node
        default StreamShape getShape() {
            return StreamShape.DOUBLE_VALUE;
        }
    }
}
