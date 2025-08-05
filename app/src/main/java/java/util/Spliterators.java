package java.util;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/* loaded from: rt.jar:java/util/Spliterators.class */
public final class Spliterators {
    private static final Spliterator<Object> EMPTY_SPLITERATOR = new EmptySpliterator.OfRef();
    private static final Spliterator.OfInt EMPTY_INT_SPLITERATOR = new EmptySpliterator.OfInt();
    private static final Spliterator.OfLong EMPTY_LONG_SPLITERATOR = new EmptySpliterator.OfLong();
    private static final Spliterator.OfDouble EMPTY_DOUBLE_SPLITERATOR = new EmptySpliterator.OfDouble();

    private Spliterators() {
    }

    public static <T> Spliterator<T> emptySpliterator() {
        return (Spliterator<T>) EMPTY_SPLITERATOR;
    }

    public static Spliterator.OfInt emptyIntSpliterator() {
        return EMPTY_INT_SPLITERATOR;
    }

    public static Spliterator.OfLong emptyLongSpliterator() {
        return EMPTY_LONG_SPLITERATOR;
    }

    public static Spliterator.OfDouble emptyDoubleSpliterator() {
        return EMPTY_DOUBLE_SPLITERATOR;
    }

    public static <T> Spliterator<T> spliterator(Object[] objArr, int i2) {
        return new ArraySpliterator((Object[]) Objects.requireNonNull(objArr), i2);
    }

    public static <T> Spliterator<T> spliterator(Object[] objArr, int i2, int i3, int i4) {
        checkFromToBounds(((Object[]) Objects.requireNonNull(objArr)).length, i2, i3);
        return new ArraySpliterator(objArr, i2, i3, i4);
    }

    public static Spliterator.OfInt spliterator(int[] iArr, int i2) {
        return new IntArraySpliterator((int[]) Objects.requireNonNull(iArr), i2);
    }

    public static Spliterator.OfInt spliterator(int[] iArr, int i2, int i3, int i4) {
        checkFromToBounds(((int[]) Objects.requireNonNull(iArr)).length, i2, i3);
        return new IntArraySpliterator(iArr, i2, i3, i4);
    }

    public static Spliterator.OfLong spliterator(long[] jArr, int i2) {
        return new LongArraySpliterator((long[]) Objects.requireNonNull(jArr), i2);
    }

    public static Spliterator.OfLong spliterator(long[] jArr, int i2, int i3, int i4) {
        checkFromToBounds(((long[]) Objects.requireNonNull(jArr)).length, i2, i3);
        return new LongArraySpliterator(jArr, i2, i3, i4);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr, int i2) {
        return new DoubleArraySpliterator((double[]) Objects.requireNonNull(dArr), i2);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr, int i2, int i3, int i4) {
        checkFromToBounds(((double[]) Objects.requireNonNull(dArr)).length, i2, i3);
        return new DoubleArraySpliterator(dArr, i2, i3, i4);
    }

    private static void checkFromToBounds(int i2, int i3, int i4) {
        if (i3 > i4) {
            throw new ArrayIndexOutOfBoundsException("origin(" + i3 + ") > fence(" + i4 + ")");
        }
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException(i3);
        }
        if (i4 > i2) {
            throw new ArrayIndexOutOfBoundsException(i4);
        }
    }

    public static <T> Spliterator<T> spliterator(Collection<? extends T> collection, int i2) {
        return new IteratorSpliterator((Collection) Objects.requireNonNull(collection), i2);
    }

    public static <T> Spliterator<T> spliterator(Iterator<? extends T> it, long j2, int i2) {
        return new IteratorSpliterator((Iterator) Objects.requireNonNull(it), j2, i2);
    }

    public static <T> Spliterator<T> spliteratorUnknownSize(Iterator<? extends T> it, int i2) {
        return new IteratorSpliterator((Iterator) Objects.requireNonNull(it), i2);
    }

    public static Spliterator.OfInt spliterator(PrimitiveIterator.OfInt ofInt, long j2, int i2) {
        return new IntIteratorSpliterator((PrimitiveIterator.OfInt) Objects.requireNonNull(ofInt), j2, i2);
    }

    public static Spliterator.OfInt spliteratorUnknownSize(PrimitiveIterator.OfInt ofInt, int i2) {
        return new IntIteratorSpliterator((PrimitiveIterator.OfInt) Objects.requireNonNull(ofInt), i2);
    }

    public static Spliterator.OfLong spliterator(PrimitiveIterator.OfLong ofLong, long j2, int i2) {
        return new LongIteratorSpliterator((PrimitiveIterator.OfLong) Objects.requireNonNull(ofLong), j2, i2);
    }

    public static Spliterator.OfLong spliteratorUnknownSize(PrimitiveIterator.OfLong ofLong, int i2) {
        return new LongIteratorSpliterator((PrimitiveIterator.OfLong) Objects.requireNonNull(ofLong), i2);
    }

    public static Spliterator.OfDouble spliterator(PrimitiveIterator.OfDouble ofDouble, long j2, int i2) {
        return new DoubleIteratorSpliterator((PrimitiveIterator.OfDouble) Objects.requireNonNull(ofDouble), j2, i2);
    }

    public static Spliterator.OfDouble spliteratorUnknownSize(PrimitiveIterator.OfDouble ofDouble, int i2) {
        return new DoubleIteratorSpliterator((PrimitiveIterator.OfDouble) Objects.requireNonNull(ofDouble), i2);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: java.util.Spliterators$1Adapter, reason: invalid class name */
    /* loaded from: rt.jar:java/util/Spliterators$1Adapter.class */
    class C1Adapter<T> implements Iterator<T>, Consumer<T> {
        boolean valueReady = false;
        T nextElement;
        final /* synthetic */ Spliterator val$spliterator;

        C1Adapter(Spliterator spliterator) {
            this.val$spliterator = spliterator;
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            this.valueReady = true;
            this.nextElement = t2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (!this.valueReady) {
                this.val$spliterator.tryAdvance(this);
            }
            return this.valueReady;
        }

        @Override // java.util.Iterator
        public T next() {
            if (!this.valueReady && !hasNext()) {
                throw new NoSuchElementException();
            }
            this.valueReady = false;
            return this.nextElement;
        }
    }

    public static <T> Iterator<T> iterator(Spliterator<? extends T> spliterator) {
        Objects.requireNonNull(spliterator);
        return new C1Adapter(spliterator);
    }

    /* renamed from: java.util.Spliterators$2Adapter, reason: invalid class name */
    /* loaded from: rt.jar:java/util/Spliterators$2Adapter.class */
    class C2Adapter implements PrimitiveIterator.OfInt, IntConsumer {
        boolean valueReady = false;
        int nextElement;
        final /* synthetic */ Spliterator.OfInt val$spliterator;

        C2Adapter(Spliterator.OfInt ofInt) {
            this.val$spliterator = ofInt;
        }

        @Override // java.util.function.IntConsumer
        public void accept(int i2) {
            this.valueReady = true;
            this.nextElement = i2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (!this.valueReady) {
                this.val$spliterator.tryAdvance((IntConsumer) this);
            }
            return this.valueReady;
        }

        @Override // java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!this.valueReady && !hasNext()) {
                throw new NoSuchElementException();
            }
            this.valueReady = false;
            return this.nextElement;
        }
    }

    public static PrimitiveIterator.OfInt iterator(Spliterator.OfInt ofInt) {
        Objects.requireNonNull(ofInt);
        return new C2Adapter(ofInt);
    }

    /* renamed from: java.util.Spliterators$3Adapter, reason: invalid class name */
    /* loaded from: rt.jar:java/util/Spliterators$3Adapter.class */
    class C3Adapter implements PrimitiveIterator.OfLong, LongConsumer {
        boolean valueReady = false;
        long nextElement;
        final /* synthetic */ Spliterator.OfLong val$spliterator;

        C3Adapter(Spliterator.OfLong ofLong) {
            this.val$spliterator = ofLong;
        }

        @Override // java.util.function.LongConsumer
        public void accept(long j2) {
            this.valueReady = true;
            this.nextElement = j2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (!this.valueReady) {
                this.val$spliterator.tryAdvance((LongConsumer) this);
            }
            return this.valueReady;
        }

        @Override // java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!this.valueReady && !hasNext()) {
                throw new NoSuchElementException();
            }
            this.valueReady = false;
            return this.nextElement;
        }
    }

    public static PrimitiveIterator.OfLong iterator(Spliterator.OfLong ofLong) {
        Objects.requireNonNull(ofLong);
        return new C3Adapter(ofLong);
    }

    /* renamed from: java.util.Spliterators$4Adapter, reason: invalid class name */
    /* loaded from: rt.jar:java/util/Spliterators$4Adapter.class */
    class C4Adapter implements PrimitiveIterator.OfDouble, DoubleConsumer {
        boolean valueReady = false;
        double nextElement;
        final /* synthetic */ Spliterator.OfDouble val$spliterator;

        C4Adapter(Spliterator.OfDouble ofDouble) {
            this.val$spliterator = ofDouble;
        }

        @Override // java.util.function.DoubleConsumer
        public void accept(double d2) {
            this.valueReady = true;
            this.nextElement = d2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (!this.valueReady) {
                this.val$spliterator.tryAdvance((DoubleConsumer) this);
            }
            return this.valueReady;
        }

        @Override // java.util.PrimitiveIterator.OfDouble
        public double nextDouble() {
            if (!this.valueReady && !hasNext()) {
                throw new NoSuchElementException();
            }
            this.valueReady = false;
            return this.nextElement;
        }
    }

    public static PrimitiveIterator.OfDouble iterator(Spliterator.OfDouble ofDouble) {
        Objects.requireNonNull(ofDouble);
        return new C4Adapter(ofDouble);
    }

    /* loaded from: rt.jar:java/util/Spliterators$EmptySpliterator.class */
    private static abstract class EmptySpliterator<T, S extends Spliterator<T>, C> {
        EmptySpliterator() {
        }

        public S trySplit() {
            return null;
        }

        public boolean tryAdvance(C c2) {
            Objects.requireNonNull(c2);
            return false;
        }

        public void forEachRemaining(C c2) {
            Objects.requireNonNull(c2);
        }

        public long estimateSize() {
            return 0L;
        }

        public int characteristics() {
            return 16448;
        }

        /* loaded from: rt.jar:java/util/Spliterators$EmptySpliterator$OfRef.class */
        private static final class OfRef<T> extends EmptySpliterator<T, Spliterator<T>, Consumer<? super T>> implements Spliterator<T> {
            @Override // java.util.Spliterator
            public /* bridge */ /* synthetic */ void forEachRemaining(Consumer consumer) {
                super.forEachRemaining((OfRef<T>) consumer);
            }

            @Override // java.util.Spliterator
            public /* bridge */ /* synthetic */ boolean tryAdvance(Consumer consumer) {
                return super.tryAdvance((OfRef<T>) consumer);
            }

            OfRef() {
            }
        }

        /* loaded from: rt.jar:java/util/Spliterators$EmptySpliterator$OfInt.class */
        private static final class OfInt extends EmptySpliterator<Integer, Spliterator.OfInt, IntConsumer> implements Spliterator.OfInt {
            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining((OfInt) intConsumer);
            }

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance((OfInt) intConsumer);
            }

            @Override // java.util.Spliterators.EmptySpliterator, java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            @Override // java.util.Spliterators.EmptySpliterator, java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfInt() {
            }
        }

        /* loaded from: rt.jar:java/util/Spliterators$EmptySpliterator$OfLong.class */
        private static final class OfLong extends EmptySpliterator<Long, Spliterator.OfLong, LongConsumer> implements Spliterator.OfLong {
            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining((OfLong) longConsumer);
            }

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance((OfLong) longConsumer);
            }

            @Override // java.util.Spliterators.EmptySpliterator, java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            @Override // java.util.Spliterators.EmptySpliterator, java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfLong() {
            }
        }

        /* loaded from: rt.jar:java/util/Spliterators$EmptySpliterator$OfDouble.class */
        private static final class OfDouble extends EmptySpliterator<Double, Spliterator.OfDouble, DoubleConsumer> implements Spliterator.OfDouble {
            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining((OfDouble) doubleConsumer);
            }

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance((OfDouble) doubleConsumer);
            }

            @Override // java.util.Spliterators.EmptySpliterator, java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            @Override // java.util.Spliterators.EmptySpliterator, java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfDouble() {
            }
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$ArraySpliterator.class */
    static final class ArraySpliterator<T> implements Spliterator<T> {
        private final Object[] array;
        private int index;
        private final int fence;
        private final int characteristics;

        public ArraySpliterator(Object[] objArr, int i2) {
            this(objArr, 0, objArr.length, i2);
        }

        public ArraySpliterator(Object[] objArr, int i2, int i3, int i4) {
            this.array = objArr;
            this.index = i2;
            this.fence = i3;
            this.characteristics = i4 | 64 | 16384;
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            int i2 = this.index;
            int i3 = (i2 + this.fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            Object[] objArr = this.array;
            this.index = i3;
            return new ArraySpliterator(objArr, i2, i3, this.characteristics);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Object[] objArr = this.array;
            int length = objArr.length;
            int i2 = this.fence;
            if (length >= i2) {
                int i3 = this.index;
                int i4 = i3;
                if (i3 >= 0) {
                    this.index = i2;
                    if (i4 < i2) {
                        do {
                            consumer.accept(objArr[i4]);
                            i4++;
                        } while (i4 < i2);
                    }
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                Object[] objArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                consumer.accept(objArr[i2]);
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fence - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$IntArraySpliterator.class */
    static final class IntArraySpliterator implements Spliterator.OfInt {
        private final int[] array;
        private int index;
        private final int fence;
        private final int characteristics;

        public IntArraySpliterator(int[] iArr, int i2) {
            this(iArr, 0, iArr.length, i2);
        }

        public IntArraySpliterator(int[] iArr, int i2, int i3, int i4) {
            this.array = iArr;
            this.index = i2;
            this.fence = i3;
            this.characteristics = i4 | 64 | 16384;
        }

        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfInt trySplit() {
            int i2 = this.index;
            int i3 = (i2 + this.fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            int[] iArr = this.array;
            this.index = i3;
            return new IntArraySpliterator(iArr, i2, i3, this.characteristics);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            int[] iArr = this.array;
            int length = iArr.length;
            int i2 = this.fence;
            if (length >= i2) {
                int i3 = this.index;
                int i4 = i3;
                if (i3 >= 0) {
                    this.index = i2;
                    if (i4 < i2) {
                        do {
                            intConsumer.accept(iArr[i4]);
                            i4++;
                        } while (i4 < i2);
                    }
                }
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                int[] iArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                intConsumer.accept(iArr[i2]);
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fence - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Integer> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$LongArraySpliterator.class */
    static final class LongArraySpliterator implements Spliterator.OfLong {
        private final long[] array;
        private int index;
        private final int fence;
        private final int characteristics;

        public LongArraySpliterator(long[] jArr, int i2) {
            this(jArr, 0, jArr.length, i2);
        }

        public LongArraySpliterator(long[] jArr, int i2, int i3, int i4) {
            this.array = jArr;
            this.index = i2;
            this.fence = i3;
            this.characteristics = i4 | 64 | 16384;
        }

        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfLong trySplit() {
            int i2 = this.index;
            int i3 = (i2 + this.fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            long[] jArr = this.array;
            this.index = i3;
            return new LongArraySpliterator(jArr, i2, i3, this.characteristics);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            long[] jArr = this.array;
            int length = jArr.length;
            int i2 = this.fence;
            if (length >= i2) {
                int i3 = this.index;
                int i4 = i3;
                if (i3 >= 0) {
                    this.index = i2;
                    if (i4 < i2) {
                        do {
                            longConsumer.accept(jArr[i4]);
                            i4++;
                        } while (i4 < i2);
                    }
                }
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                long[] jArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                longConsumer.accept(jArr[i2]);
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fence - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Long> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$DoubleArraySpliterator.class */
    static final class DoubleArraySpliterator implements Spliterator.OfDouble {
        private final double[] array;
        private int index;
        private final int fence;
        private final int characteristics;

        public DoubleArraySpliterator(double[] dArr, int i2) {
            this(dArr, 0, dArr.length, i2);
        }

        public DoubleArraySpliterator(double[] dArr, int i2, int i3, int i4) {
            this.array = dArr;
            this.index = i2;
            this.fence = i3;
            this.characteristics = i4 | 64 | 16384;
        }

        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfDouble trySplit() {
            int i2 = this.index;
            int i3 = (i2 + this.fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            double[] dArr = this.array;
            this.index = i3;
            return new DoubleArraySpliterator(dArr, i2, i3, this.characteristics);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            double[] dArr = this.array;
            int length = dArr.length;
            int i2 = this.fence;
            if (length >= i2) {
                int i3 = this.index;
                int i4 = i3;
                if (i3 >= 0) {
                    this.index = i2;
                    if (i4 < i2) {
                        do {
                            doubleConsumer.accept(dArr[i4]);
                            i4++;
                        } while (i4 < i2);
                    }
                }
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            if (this.index >= 0 && this.index < this.fence) {
                double[] dArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                doubleConsumer.accept(dArr[i2]);
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fence - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Double> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$AbstractSpliterator.class */
    public static abstract class AbstractSpliterator<T> implements Spliterator<T> {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractSpliterator(long j2, int i2) {
            this.est = j2;
            this.characteristics = (i2 & 64) != 0 ? i2 | 16384 : i2;
        }

        /* loaded from: rt.jar:java/util/Spliterators$AbstractSpliterator$HoldingConsumer.class */
        static final class HoldingConsumer<T> implements Consumer<T> {
            Object value;

            HoldingConsumer() {
            }

            @Override // java.util.function.Consumer
            public void accept(T t2) {
                this.value = t2;
            }
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            HoldingConsumer holdingConsumer = new HoldingConsumer();
            long j2 = this.est;
            if (j2 > 1 && tryAdvance(holdingConsumer)) {
                int i2 = this.batch + 1024;
                if (i2 > j2) {
                    i2 = (int) j2;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                Object[] objArr = new Object[i2];
                int i3 = 0;
                do {
                    objArr[i3] = holdingConsumer.value;
                    i3++;
                    if (i3 >= i2) {
                        break;
                    }
                } while (tryAdvance(holdingConsumer));
                this.batch = i3;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= i3;
                }
                return new ArraySpliterator(objArr, 0, i3, characteristics());
            }
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$AbstractIntSpliterator.class */
    public static abstract class AbstractIntSpliterator implements Spliterator.OfInt {
        static final int MAX_BATCH = 33554432;
        static final int BATCH_UNIT = 1024;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractIntSpliterator(long j2, int i2) {
            this.est = j2;
            this.characteristics = (i2 & 64) != 0 ? i2 | 16384 : i2;
        }

        /* loaded from: rt.jar:java/util/Spliterators$AbstractIntSpliterator$HoldingIntConsumer.class */
        static final class HoldingIntConsumer implements IntConsumer {
            int value;

            HoldingIntConsumer() {
            }

            @Override // java.util.function.IntConsumer
            public void accept(int i2) {
                this.value = i2;
            }
        }

        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfInt trySplit() {
            HoldingIntConsumer holdingIntConsumer = new HoldingIntConsumer();
            long j2 = this.est;
            if (j2 > 1 && tryAdvance((IntConsumer) holdingIntConsumer)) {
                int i2 = this.batch + 1024;
                if (i2 > j2) {
                    i2 = (int) j2;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                int[] iArr = new int[i2];
                int i3 = 0;
                do {
                    iArr[i3] = holdingIntConsumer.value;
                    i3++;
                    if (i3 >= i2) {
                        break;
                    }
                } while (tryAdvance((IntConsumer) holdingIntConsumer));
                this.batch = i3;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= i3;
                }
                return new IntArraySpliterator(iArr, 0, i3, characteristics());
            }
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$AbstractLongSpliterator.class */
    public static abstract class AbstractLongSpliterator implements Spliterator.OfLong {
        static final int MAX_BATCH = 33554432;
        static final int BATCH_UNIT = 1024;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractLongSpliterator(long j2, int i2) {
            this.est = j2;
            this.characteristics = (i2 & 64) != 0 ? i2 | 16384 : i2;
        }

        /* loaded from: rt.jar:java/util/Spliterators$AbstractLongSpliterator$HoldingLongConsumer.class */
        static final class HoldingLongConsumer implements LongConsumer {
            long value;

            HoldingLongConsumer() {
            }

            @Override // java.util.function.LongConsumer
            public void accept(long j2) {
                this.value = j2;
            }
        }

        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfLong trySplit() {
            HoldingLongConsumer holdingLongConsumer = new HoldingLongConsumer();
            long j2 = this.est;
            if (j2 > 1 && tryAdvance((LongConsumer) holdingLongConsumer)) {
                int i2 = this.batch + 1024;
                if (i2 > j2) {
                    i2 = (int) j2;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                long[] jArr = new long[i2];
                int i3 = 0;
                do {
                    jArr[i3] = holdingLongConsumer.value;
                    i3++;
                    if (i3 >= i2) {
                        break;
                    }
                } while (tryAdvance((LongConsumer) holdingLongConsumer));
                this.batch = i3;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= i3;
                }
                return new LongArraySpliterator(jArr, 0, i3, characteristics());
            }
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$AbstractDoubleSpliterator.class */
    public static abstract class AbstractDoubleSpliterator implements Spliterator.OfDouble {
        static final int MAX_BATCH = 33554432;
        static final int BATCH_UNIT = 1024;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractDoubleSpliterator(long j2, int i2) {
            this.est = j2;
            this.characteristics = (i2 & 64) != 0 ? i2 | 16384 : i2;
        }

        /* loaded from: rt.jar:java/util/Spliterators$AbstractDoubleSpliterator$HoldingDoubleConsumer.class */
        static final class HoldingDoubleConsumer implements DoubleConsumer {
            double value;

            HoldingDoubleConsumer() {
            }

            @Override // java.util.function.DoubleConsumer
            public void accept(double d2) {
                this.value = d2;
            }
        }

        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfDouble trySplit() {
            HoldingDoubleConsumer holdingDoubleConsumer = new HoldingDoubleConsumer();
            long j2 = this.est;
            if (j2 > 1 && tryAdvance((DoubleConsumer) holdingDoubleConsumer)) {
                int i2 = this.batch + 1024;
                if (i2 > j2) {
                    i2 = (int) j2;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                double[] dArr = new double[i2];
                int i3 = 0;
                do {
                    dArr[i3] = holdingDoubleConsumer.value;
                    i3++;
                    if (i3 >= i2) {
                        break;
                    }
                } while (tryAdvance((DoubleConsumer) holdingDoubleConsumer));
                this.batch = i3;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= i3;
                }
                return new DoubleArraySpliterator(dArr, 0, i3, characteristics());
            }
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$IteratorSpliterator.class */
    static class IteratorSpliterator<T> implements Spliterator<T> {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private final Collection<? extends T> collection;
        private Iterator<? extends T> it;
        private final int characteristics;
        private long est;
        private int batch;

        /*  JADX ERROR: Failed to decode insn: 0x0023: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[7]
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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        @Override // java.util.Spliterator
        public java.util.Spliterator<T> trySplit() {
            /*
                r7 = this;
                r0 = r7
                java.util.Iterator<? extends T> r0 = r0.it
                r1 = r0
                r8 = r1
                if (r0 != 0) goto L2b
                r0 = r7
                r1 = r7
                java.util.Collection<? extends T> r1 = r1.collection
                java.util.Iterator r1 = r1.iterator()
                r2 = r1; r1 = r0; r0 = r2; 
                r1.it = r2
                r8 = r0
                r0 = r7
                r1 = r7
                java.util.Collection<? extends T> r1 = r1.collection
                int r1 = r1.size()
                long r1 = (long) r1
                // decode failed: arraycopy: source index -1 out of bounds for object array[7]
                r0.est = r1
                r9 = r-1
                goto L30
                r0 = r7
                long r0 = r0.est
                r9 = r0
                r-1 = r9
                r0 = 1
                int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
                if (r-1 <= 0) goto Lb6
                r-1 = r8
                r-1.hasNext()
                if (r-1 == 0) goto Lb6
                r-1 = r7
                int r-1 = r-1.batch
                r0 = 1024(0x400, float:1.435E-42)
                int r-1 = r-1 + r0
                r11 = r-1
                r-1 = r11
                long r-1 = (long) r-1
                r0 = r9
                int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
                if (r-1 <= 0) goto L55
                r-1 = r9
                int r-1 = (int) r-1
                r11 = r-1
                r-1 = r11
                r0 = 33554432(0x2000000, float:9.403955E-38)
                if (r-1 <= r0) goto L60
                r-1 = 33554432(0x2000000, float:9.403955E-38)
                r11 = r-1
                r-1 = r11
                java.lang.Object[] r-1 = new java.lang.Object[r-1]
                r12 = r-1
                r-1 = 0
                r13 = r-1
                r0 = r12
                r1 = r13
                r2 = r8
                java.lang.Object r2 = r2.next()
                r0[r1] = r2
                int r13 = r13 + 1
                r0 = r13
                r1 = r11
                if (r0 >= r1) goto L88
                r0 = r8
                boolean r0 = r0.hasNext()
                if (r0 != 0) goto L6a
                r0 = r7
                r1 = r13
                r0.batch = r1
                r0 = r7
                long r0 = r0.est
                r1 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 == 0) goto La5
                r0 = r7
                r1 = r0
                long r1 = r1.est
                r2 = r13
                long r2 = (long) r2
                long r1 = r1 - r2
                r0.est = r1
                java.util.Spliterators$ArraySpliterator r0 = new java.util.Spliterators$ArraySpliterator
                r1 = r0
                r2 = r12
                r3 = 0
                r4 = r13
                r5 = r7
                int r5 = r5.characteristics
                r1.<init>(r2, r3, r4, r5)
                return r0
                r-1 = 0
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Spliterators.IteratorSpliterator.trySplit():java.util.Spliterator");
        }

        /*  JADX ERROR: Failed to decode insn: 0x001F: MOVE_MULTI
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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        @Override // java.util.Spliterator
        public long estimateSize() {
            /*
                r6 = this;
                r0 = r6
                java.util.Iterator<? extends T> r0 = r0.it
                if (r0 != 0) goto L24
                r0 = r6
                r1 = r6
                java.util.Collection<? extends T> r1 = r1.collection
                java.util.Iterator r1 = r1.iterator()
                r0.it = r1
                r0 = r6
                r1 = r6
                java.util.Collection<? extends T> r1 = r1.collection
                int r1 = r1.size()
                long r1 = (long) r1
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.est = r1
                return r-1
                r0 = r6
                long r0 = r0.est
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Spliterators.IteratorSpliterator.estimateSize():long");
        }

        public IteratorSpliterator(Collection<? extends T> collection, int i2) {
            this.collection = collection;
            this.it = null;
            this.characteristics = (i2 & 4096) == 0 ? i2 | 64 | 16384 : i2;
        }

        public IteratorSpliterator(Iterator<? extends T> it, long j2, int i2) {
            this.collection = null;
            this.it = it;
            this.est = j2;
            this.characteristics = (i2 & 4096) == 0 ? i2 | 64 | 16384 : i2;
        }

        public IteratorSpliterator(Iterator<? extends T> it, int i2) {
            this.collection = null;
            this.it = it;
            this.est = Long.MAX_VALUE;
            this.characteristics = i2 & (-16449);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Iterator<? extends T> it = this.it;
            Iterator<? extends T> it2 = it;
            if (it == null) {
                Iterator<? extends T> it3 = this.collection.iterator();
                this.it = it3;
                it2 = it3;
                this.est = this.collection.size();
            }
            it2.forEachRemaining(consumer);
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.it == null) {
                this.it = this.collection.iterator();
                this.est = this.collection.size();
            }
            if (this.it.hasNext()) {
                consumer.accept(this.it.next());
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$IntIteratorSpliterator.class */
    static final class IntIteratorSpliterator implements Spliterator.OfInt {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private PrimitiveIterator.OfInt it;
        private final int characteristics;
        private long est;
        private int batch;

        public IntIteratorSpliterator(PrimitiveIterator.OfInt ofInt, long j2, int i2) {
            this.it = ofInt;
            this.est = j2;
            this.characteristics = (i2 & 4096) == 0 ? i2 | 64 | 16384 : i2;
        }

        public IntIteratorSpliterator(PrimitiveIterator.OfInt ofInt, int i2) {
            this.it = ofInt;
            this.est = Long.MAX_VALUE;
            this.characteristics = i2 & (-16449);
        }

        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfInt trySplit() {
            PrimitiveIterator.OfInt ofInt = this.it;
            long j2 = this.est;
            if (j2 > 1 && ofInt.hasNext()) {
                int i2 = this.batch + 1024;
                if (i2 > j2) {
                    i2 = (int) j2;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                int[] iArr = new int[i2];
                int i3 = 0;
                do {
                    iArr[i3] = ofInt.nextInt();
                    i3++;
                    if (i3 >= i2) {
                        break;
                    }
                } while (ofInt.hasNext());
                this.batch = i3;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= i3;
                }
                return new IntArraySpliterator(iArr, 0, i3, this.characteristics);
            }
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            this.it.forEachRemaining(intConsumer);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            if (this.it.hasNext()) {
                intConsumer.accept(this.it.nextInt());
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Integer> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$LongIteratorSpliterator.class */
    static final class LongIteratorSpliterator implements Spliterator.OfLong {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private PrimitiveIterator.OfLong it;
        private final int characteristics;
        private long est;
        private int batch;

        public LongIteratorSpliterator(PrimitiveIterator.OfLong ofLong, long j2, int i2) {
            this.it = ofLong;
            this.est = j2;
            this.characteristics = (i2 & 4096) == 0 ? i2 | 64 | 16384 : i2;
        }

        public LongIteratorSpliterator(PrimitiveIterator.OfLong ofLong, int i2) {
            this.it = ofLong;
            this.est = Long.MAX_VALUE;
            this.characteristics = i2 & (-16449);
        }

        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfLong trySplit() {
            PrimitiveIterator.OfLong ofLong = this.it;
            long j2 = this.est;
            if (j2 > 1 && ofLong.hasNext()) {
                int i2 = this.batch + 1024;
                if (i2 > j2) {
                    i2 = (int) j2;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                long[] jArr = new long[i2];
                int i3 = 0;
                do {
                    jArr[i3] = ofLong.nextLong();
                    i3++;
                    if (i3 >= i2) {
                        break;
                    }
                } while (ofLong.hasNext());
                this.batch = i3;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= i3;
                }
                return new LongArraySpliterator(jArr, 0, i3, this.characteristics);
            }
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            this.it.forEachRemaining(longConsumer);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            if (this.it.hasNext()) {
                longConsumer.accept(this.it.nextLong());
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Long> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/Spliterators$DoubleIteratorSpliterator.class */
    static final class DoubleIteratorSpliterator implements Spliterator.OfDouble {
        static final int BATCH_UNIT = 1024;
        static final int MAX_BATCH = 33554432;
        private PrimitiveIterator.OfDouble it;
        private final int characteristics;
        private long est;
        private int batch;

        public DoubleIteratorSpliterator(PrimitiveIterator.OfDouble ofDouble, long j2, int i2) {
            this.it = ofDouble;
            this.est = j2;
            this.characteristics = (i2 & 4096) == 0 ? i2 | 64 | 16384 : i2;
        }

        public DoubleIteratorSpliterator(PrimitiveIterator.OfDouble ofDouble, int i2) {
            this.it = ofDouble;
            this.est = Long.MAX_VALUE;
            this.characteristics = i2 & (-16449);
        }

        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfDouble trySplit() {
            PrimitiveIterator.OfDouble ofDouble = this.it;
            long j2 = this.est;
            if (j2 > 1 && ofDouble.hasNext()) {
                int i2 = this.batch + 1024;
                if (i2 > j2) {
                    i2 = (int) j2;
                }
                if (i2 > 33554432) {
                    i2 = 33554432;
                }
                double[] dArr = new double[i2];
                int i3 = 0;
                do {
                    dArr[i3] = ofDouble.nextDouble();
                    i3++;
                    if (i3 >= i2) {
                        break;
                    }
                } while (ofDouble.hasNext());
                this.batch = i3;
                if (this.est != Long.MAX_VALUE) {
                    this.est -= i3;
                }
                return new DoubleArraySpliterator(dArr, 0, i3, this.characteristics);
            }
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            this.it.forEachRemaining(doubleConsumer);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            if (this.it.hasNext()) {
                doubleConsumer.accept(this.it.nextDouble());
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Double> getComparator() {
            if (hasCharacteristics(4)) {
                return null;
            }
            throw new IllegalStateException();
        }
    }
}
