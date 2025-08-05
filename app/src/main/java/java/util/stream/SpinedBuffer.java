package java.util.stream;

import A.a;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;

/* loaded from: rt.jar:java/util/stream/SpinedBuffer.class */
class SpinedBuffer<E> extends AbstractSpinedBuffer implements Consumer<E>, Iterable<E> {
    protected E[] curChunk;
    protected E[][] spine;
    private static final int SPLITERATOR_CHARACTERISTICS = 16464;

    SpinedBuffer(int i2) {
        super(i2);
        this.curChunk = (E[]) new Object[1 << this.initialChunkPower];
    }

    SpinedBuffer() {
        this.curChunk = (E[]) new Object[1 << this.initialChunkPower];
    }

    protected long capacity() {
        return this.spineIndex == 0 ? this.curChunk.length : this.priorElementCount[this.spineIndex] + this.spine[this.spineIndex].length;
    }

    private void inflateSpine() {
        if (this.spine == null) {
            this.spine = (E[][]) ((Object[][]) new Object[8]);
            this.priorElementCount = new long[8];
            this.spine[0] = this.curChunk;
        }
    }

    protected final void ensureCapacity(long j2) {
        long jCapacity = capacity();
        if (j2 > jCapacity) {
            inflateSpine();
            int i2 = this.spineIndex + 1;
            while (j2 > jCapacity) {
                if (i2 >= this.spine.length) {
                    int length = this.spine.length * 2;
                    this.spine = (E[][]) ((Object[][]) Arrays.copyOf(this.spine, length));
                    this.priorElementCount = Arrays.copyOf(this.priorElementCount, length);
                }
                int iChunkSize = chunkSize(i2);
                ((E[][]) this.spine)[i2] = new Object[iChunkSize];
                this.priorElementCount[i2] = this.priorElementCount[i2 - 1] + this.spine[i2 - 1].length;
                jCapacity += iChunkSize;
                i2++;
            }
        }
    }

    protected void increaseCapacity() {
        ensureCapacity(capacity() + 1);
    }

    public E get(long j2) {
        if (this.spineIndex == 0) {
            if (j2 < this.elementIndex) {
                return this.curChunk[(int) j2];
            }
            throw new IndexOutOfBoundsException(Long.toString(j2));
        }
        if (j2 >= count()) {
            throw new IndexOutOfBoundsException(Long.toString(j2));
        }
        for (int i2 = 0; i2 <= this.spineIndex; i2++) {
            if (j2 < this.priorElementCount[i2] + this.spine[i2].length) {
                return this.spine[i2][(int) (j2 - this.priorElementCount[i2])];
            }
        }
        throw new IndexOutOfBoundsException(Long.toString(j2));
    }

    public void copyInto(E[] eArr, int i2) {
        long jCount = i2 + count();
        if (jCount > eArr.length || jCount < i2) {
            throw new IndexOutOfBoundsException("does not fit");
        }
        if (this.spineIndex == 0) {
            System.arraycopy(this.curChunk, 0, eArr, i2, this.elementIndex);
            return;
        }
        for (int i3 = 0; i3 < this.spineIndex; i3++) {
            System.arraycopy(this.spine[i3], 0, eArr, i2, this.spine[i3].length);
            i2 += this.spine[i3].length;
        }
        if (this.elementIndex > 0) {
            System.arraycopy(this.curChunk, 0, eArr, i2, this.elementIndex);
        }
    }

    public E[] asArray(IntFunction<E[]> intFunction) {
        long jCount = count();
        if (jCount >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        E[] eArrApply = intFunction.apply((int) jCount);
        copyInto(eArrApply, 0);
        return eArrApply;
    }

    @Override // java.util.stream.AbstractSpinedBuffer
    public void clear() {
        if (this.spine != null) {
            this.curChunk = this.spine[0];
            for (int i2 = 0; i2 < this.curChunk.length; i2++) {
                this.curChunk[i2] = null;
            }
            this.spine = (E[][]) ((Object[][]) null);
            this.priorElementCount = null;
        } else {
            for (int i3 = 0; i3 < this.elementIndex; i3++) {
                this.curChunk[i3] = null;
            }
        }
        this.elementIndex = 0;
        this.spineIndex = 0;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return Spliterators.iterator(spliterator());
    }

    public void forEach(Consumer<? super E> consumer) {
        for (int i2 = 0; i2 < this.spineIndex; i2++) {
            for (a aVar : this.spine[i2]) {
                consumer.accept(aVar);
            }
        }
        for (int i3 = 0; i3 < this.elementIndex; i3++) {
            consumer.accept(this.curChunk[i3]);
        }
    }

    public void accept(E e2) {
        if (this.elementIndex == this.curChunk.length) {
            inflateSpine();
            if (this.spineIndex + 1 >= this.spine.length || this.spine[this.spineIndex + 1] == null) {
                increaseCapacity();
            }
            this.elementIndex = 0;
            this.spineIndex++;
            this.curChunk = this.spine[this.spineIndex];
        }
        E[] eArr = this.curChunk;
        int i2 = this.elementIndex;
        this.elementIndex = i2 + 1;
        eArr[i2] = e2;
    }

    public String toString() {
        ArrayList arrayList = new ArrayList();
        arrayList.getClass();
        forEach(arrayList::add);
        return "SpinedBuffer:" + arrayList.toString();
    }

    /* renamed from: java.util.stream.SpinedBuffer$1Splitr, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/SpinedBuffer$1Splitr.class */
    class C1Splitr implements Spliterator<E> {
        int splSpineIndex;
        final int lastSpineIndex;
        int splElementIndex;
        final int lastSpineElementFence;
        E[] splChunk;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SpinedBuffer.class.desiredAssertionStatus();
        }

        C1Splitr(int i2, int i3, int i4, int i5) {
            this.splSpineIndex = i2;
            this.lastSpineIndex = i3;
            this.splElementIndex = i4;
            this.lastSpineElementFence = i5;
            if (!$assertionsDisabled && SpinedBuffer.this.spine == null && (i2 != 0 || i3 != 0)) {
                throw new AssertionError();
            }
            this.splChunk = SpinedBuffer.this.spine == null ? SpinedBuffer.this.curChunk : SpinedBuffer.this.spine[i2];
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.splSpineIndex == this.lastSpineIndex ? this.lastSpineElementFence - this.splElementIndex : ((SpinedBuffer.this.priorElementCount[this.lastSpineIndex] + this.lastSpineElementFence) - SpinedBuffer.this.priorElementCount[this.splSpineIndex]) - this.splElementIndex;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return SpinedBuffer.SPLITERATOR_CHARACTERISTICS;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                E[] eArr = this.splChunk;
                int i2 = this.splElementIndex;
                this.splElementIndex = i2 + 1;
                consumer.accept(eArr[i2]);
                if (this.splElementIndex == this.splChunk.length) {
                    this.splElementIndex = 0;
                    this.splSpineIndex++;
                    if (SpinedBuffer.this.spine != null && this.splSpineIndex <= this.lastSpineIndex) {
                        this.splChunk = SpinedBuffer.this.spine[this.splSpineIndex];
                        return true;
                    }
                    return true;
                }
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                int i2 = this.splElementIndex;
                for (int i3 = this.splSpineIndex; i3 < this.lastSpineIndex; i3++) {
                    a[] aVarArr = SpinedBuffer.this.spine[i3];
                    while (i2 < aVarArr.length) {
                        consumer.accept(aVarArr[i2]);
                        i2++;
                    }
                    i2 = 0;
                }
                E[] eArr = this.splSpineIndex == this.lastSpineIndex ? this.splChunk : SpinedBuffer.this.spine[this.lastSpineIndex];
                int i4 = this.lastSpineElementFence;
                while (i2 < i4) {
                    consumer.accept((Object) eArr[i2]);
                    i2++;
                }
                this.splSpineIndex = this.lastSpineIndex;
                this.splElementIndex = this.lastSpineElementFence;
            }
        }

        @Override // java.util.Spliterator
        public Spliterator<E> trySplit() {
            int i2;
            if (this.splSpineIndex < this.lastSpineIndex) {
                C1Splitr c1Splitr = new C1Splitr(this.splSpineIndex, this.lastSpineIndex - 1, this.splElementIndex, SpinedBuffer.this.spine[this.lastSpineIndex - 1].length);
                this.splSpineIndex = this.lastSpineIndex;
                this.splElementIndex = 0;
                this.splChunk = SpinedBuffer.this.spine[this.splSpineIndex];
                return c1Splitr;
            }
            if (this.splSpineIndex != this.lastSpineIndex || (i2 = (this.lastSpineElementFence - this.splElementIndex) / 2) == 0) {
                return null;
            }
            Spliterator<E> spliterator = Arrays.spliterator(this.splChunk, this.splElementIndex, this.splElementIndex + i2);
            this.splElementIndex += i2;
            return spliterator;
        }
    }

    public Spliterator<E> spliterator() {
        return new C1Splitr(0, this.spineIndex, 0, this.elementIndex);
    }

    /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfPrimitive.class */
    static abstract class OfPrimitive<E, T_ARR, T_CONS> extends AbstractSpinedBuffer implements Iterable<E> {
        T_ARR curChunk;
        T_ARR[] spine;

        public abstract Iterator<E> iterator();

        public abstract void forEach(Consumer<? super E> consumer);

        protected abstract T_ARR[] newArrayArray(int i2);

        public abstract T_ARR newArray(int i2);

        protected abstract int arrayLength(T_ARR t_arr);

        protected abstract void arrayForEach(T_ARR t_arr, int i2, int i3, T_CONS t_cons);

        OfPrimitive(int i2) {
            super(i2);
            this.curChunk = newArray(1 << this.initialChunkPower);
        }

        OfPrimitive() {
            this.curChunk = newArray(1 << this.initialChunkPower);
        }

        protected long capacity() {
            if (this.spineIndex == 0) {
                return arrayLength(this.curChunk);
            }
            return this.priorElementCount[this.spineIndex] + arrayLength(this.spine[this.spineIndex]);
        }

        private void inflateSpine() {
            if (this.spine == null) {
                this.spine = newArrayArray(8);
                this.priorElementCount = new long[8];
                this.spine[0] = this.curChunk;
            }
        }

        protected final void ensureCapacity(long j2) {
            long jCapacity = capacity();
            if (j2 > jCapacity) {
                inflateSpine();
                int i2 = this.spineIndex + 1;
                while (j2 > jCapacity) {
                    if (i2 >= this.spine.length) {
                        int length = this.spine.length * 2;
                        this.spine = (T_ARR[]) Arrays.copyOf(this.spine, length);
                        this.priorElementCount = Arrays.copyOf(this.priorElementCount, length);
                    }
                    int iChunkSize = chunkSize(i2);
                    this.spine[i2] = newArray(iChunkSize);
                    this.priorElementCount[i2] = this.priorElementCount[i2 - 1] + arrayLength(this.spine[i2 - 1]);
                    jCapacity += iChunkSize;
                    i2++;
                }
            }
        }

        protected void increaseCapacity() {
            ensureCapacity(capacity() + 1);
        }

        protected int chunkFor(long j2) {
            if (this.spineIndex == 0) {
                if (j2 < this.elementIndex) {
                    return 0;
                }
                throw new IndexOutOfBoundsException(Long.toString(j2));
            }
            if (j2 >= count()) {
                throw new IndexOutOfBoundsException(Long.toString(j2));
            }
            for (int i2 = 0; i2 <= this.spineIndex; i2++) {
                if (j2 < this.priorElementCount[i2] + arrayLength(this.spine[i2])) {
                    return i2;
                }
            }
            throw new IndexOutOfBoundsException(Long.toString(j2));
        }

        public void copyInto(T_ARR t_arr, int i2) {
            long jCount = i2 + count();
            if (jCount > arrayLength(t_arr) || jCount < i2) {
                throw new IndexOutOfBoundsException("does not fit");
            }
            if (this.spineIndex == 0) {
                System.arraycopy(this.curChunk, 0, t_arr, i2, this.elementIndex);
                return;
            }
            for (int i3 = 0; i3 < this.spineIndex; i3++) {
                System.arraycopy(this.spine[i3], 0, t_arr, i2, arrayLength(this.spine[i3]));
                i2 += arrayLength(this.spine[i3]);
            }
            if (this.elementIndex > 0) {
                System.arraycopy(this.curChunk, 0, t_arr, i2, this.elementIndex);
            }
        }

        public T_ARR asPrimitiveArray() {
            long jCount = count();
            if (jCount >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            T_ARR t_arrNewArray = newArray((int) jCount);
            copyInto(t_arrNewArray, 0);
            return t_arrNewArray;
        }

        protected void preAccept() {
            if (this.elementIndex == arrayLength(this.curChunk)) {
                inflateSpine();
                if (this.spineIndex + 1 >= this.spine.length || this.spine[this.spineIndex + 1] == null) {
                    increaseCapacity();
                }
                this.elementIndex = 0;
                this.spineIndex++;
                this.curChunk = this.spine[this.spineIndex];
            }
        }

        @Override // java.util.stream.AbstractSpinedBuffer
        public void clear() {
            if (this.spine != null) {
                this.curChunk = this.spine[0];
                this.spine = null;
                this.priorElementCount = null;
            }
            this.elementIndex = 0;
            this.spineIndex = 0;
        }

        public void forEach(T_CONS t_cons) {
            for (int i2 = 0; i2 < this.spineIndex; i2++) {
                arrayForEach(this.spine[i2], 0, arrayLength(this.spine[i2]), t_cons);
            }
            arrayForEach(this.curChunk, 0, this.elementIndex, t_cons);
        }

        /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfPrimitive$BaseSpliterator.class */
        abstract class BaseSpliterator<T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>> implements Spliterator.OfPrimitive<E, T_CONS, T_SPLITR> {
            int splSpineIndex;
            final int lastSpineIndex;
            int splElementIndex;
            final int lastSpineElementFence;
            T_ARR splChunk;
            static final /* synthetic */ boolean $assertionsDisabled;

            abstract T_SPLITR newSpliterator(int i2, int i3, int i4, int i5);

            abstract void arrayForOne(T_ARR t_arr, int i2, T_CONS t_cons);

            abstract T_SPLITR arraySpliterator(T_ARR t_arr, int i2, int i3);

            static {
                $assertionsDisabled = !SpinedBuffer.class.desiredAssertionStatus();
            }

            BaseSpliterator(int i2, int i3, int i4, int i5) {
                this.splSpineIndex = i2;
                this.lastSpineIndex = i3;
                this.splElementIndex = i4;
                this.lastSpineElementFence = i5;
                if (!$assertionsDisabled && OfPrimitive.this.spine == null && (i2 != 0 || i3 != 0)) {
                    throw new AssertionError();
                }
                this.splChunk = OfPrimitive.this.spine == null ? OfPrimitive.this.curChunk : OfPrimitive.this.spine[i2];
            }

            @Override // java.util.Spliterator
            public long estimateSize() {
                return this.splSpineIndex == this.lastSpineIndex ? this.lastSpineElementFence - this.splElementIndex : ((OfPrimitive.this.priorElementCount[this.lastSpineIndex] + this.lastSpineElementFence) - OfPrimitive.this.priorElementCount[this.splSpineIndex]) - this.splElementIndex;
            }

            @Override // java.util.Spliterator
            public int characteristics() {
                return SpinedBuffer.SPLITERATOR_CHARACTERISTICS;
            }

            @Override // java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                    T_ARR t_arr = this.splChunk;
                    int i2 = this.splElementIndex;
                    this.splElementIndex = i2 + 1;
                    arrayForOne(t_arr, i2, t_cons);
                    if (this.splElementIndex == OfPrimitive.this.arrayLength(this.splChunk)) {
                        this.splElementIndex = 0;
                        this.splSpineIndex++;
                        if (OfPrimitive.this.spine != null && this.splSpineIndex <= this.lastSpineIndex) {
                            this.splChunk = OfPrimitive.this.spine[this.splSpineIndex];
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
                return false;
            }

            @Override // java.util.Spliterator.OfPrimitive
            public void forEachRemaining(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                if (this.splSpineIndex < this.lastSpineIndex || (this.splSpineIndex == this.lastSpineIndex && this.splElementIndex < this.lastSpineElementFence)) {
                    int i2 = this.splElementIndex;
                    for (int i3 = this.splSpineIndex; i3 < this.lastSpineIndex; i3++) {
                        T_ARR t_arr = OfPrimitive.this.spine[i3];
                        OfPrimitive.this.arrayForEach(t_arr, i2, OfPrimitive.this.arrayLength(t_arr), t_cons);
                        i2 = 0;
                    }
                    OfPrimitive.this.arrayForEach(this.splSpineIndex == this.lastSpineIndex ? this.splChunk : OfPrimitive.this.spine[this.lastSpineIndex], i2, this.lastSpineElementFence, t_cons);
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = this.lastSpineElementFence;
                }
            }

            @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public T_SPLITR trySplit() {
                int i2;
                if (this.splSpineIndex < this.lastSpineIndex) {
                    T_SPLITR t_splitr = (T_SPLITR) newSpliterator(this.splSpineIndex, this.lastSpineIndex - 1, this.splElementIndex, OfPrimitive.this.arrayLength(OfPrimitive.this.spine[this.lastSpineIndex - 1]));
                    this.splSpineIndex = this.lastSpineIndex;
                    this.splElementIndex = 0;
                    this.splChunk = OfPrimitive.this.spine[this.splSpineIndex];
                    return t_splitr;
                }
                if (this.splSpineIndex != this.lastSpineIndex || (i2 = (this.lastSpineElementFence - this.splElementIndex) / 2) == 0) {
                    return null;
                }
                T_SPLITR t_splitr2 = (T_SPLITR) arraySpliterator(this.splChunk, this.splElementIndex, i2);
                this.splElementIndex += i2;
                return t_splitr2;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfInt.class */
    static class OfInt extends OfPrimitive<Integer, int[], IntConsumer> implements IntConsumer {
        OfInt() {
        }

        OfInt(int i2) {
            super(i2);
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.lang.Iterable
        public void forEach(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                forEach((OfInt) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling SpinedBuffer.OfInt.forEach(Consumer)");
            }
            spliterator().forEachRemaining(consumer);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Type inference failed for: r0v1, types: [int[], int[][]] */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public int[][] newArrayArray(int i2) {
            return new int[i2];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public int[] newArray(int i2) {
            return new int[i2];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public int arrayLength(int[] iArr) {
            return iArr.length;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public void arrayForEach(int[] iArr, int i2, int i3, IntConsumer intConsumer) {
            for (int i4 = i2; i4 < i3; i4++) {
                intConsumer.accept(iArr[i4]);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void accept(int i2) {
            preAccept();
            int[] iArr = (int[]) this.curChunk;
            int i3 = this.elementIndex;
            this.elementIndex = i3 + 1;
            iArr[i3] = i2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int get(long j2) {
            int iChunkFor = chunkFor(j2);
            if (this.spineIndex == 0 && iChunkFor == 0) {
                return ((int[]) this.curChunk)[(int) j2];
            }
            return ((int[][]) this.spine)[iChunkFor][(int) (j2 - this.priorElementCount[iChunkFor])];
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.lang.Iterable, java.util.List
        public PrimitiveIterator.OfInt iterator() {
            return Spliterators.iterator(spliterator());
        }

        /* renamed from: java.util.stream.SpinedBuffer$OfInt$1Splitr, reason: invalid class name */
        /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfInt$1Splitr.class */
        class C1Splitr extends OfPrimitive<Integer, int[], IntConsumer>.BaseSpliterator<Spliterator.OfInt> implements Spliterator.OfInt {
            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining((C1Splitr) intConsumer);
            }

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance((C1Splitr) intConsumer);
            }

            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            C1Splitr(int i2, int i3, int i4, int i5) {
                super(i2, i3, i4, i5);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public C1Splitr newSpliterator(int i2, int i3, int i4, int i5) {
                return OfInt.this.new C1Splitr(i2, i3, i4, i5);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public void arrayForOne(int[] iArr, int i2, IntConsumer intConsumer) {
                intConsumer.accept(iArr[i2]);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public Spliterator.OfInt arraySpliterator(int[] iArr, int i2, int i3) {
                return Arrays.spliterator(iArr, i2, i2 + i3);
            }
        }

        @Override // java.lang.Iterable
        public Spliterator.OfInt spliterator() {
            return new C1Splitr(0, this.spineIndex, 0, this.elementIndex);
        }

        public String toString() {
            int[] iArrAsPrimitiveArray = asPrimitiveArray();
            if (iArrAsPrimitiveArray.length < 200) {
                return String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(iArrAsPrimitiveArray.length), Integer.valueOf(this.spineIndex), Arrays.toString(iArrAsPrimitiveArray));
            }
            return String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(iArrAsPrimitiveArray.length), Integer.valueOf(this.spineIndex), Arrays.toString(Arrays.copyOf(iArrAsPrimitiveArray, 200)));
        }
    }

    /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfLong.class */
    static class OfLong extends OfPrimitive<Long, long[], LongConsumer> implements LongConsumer {
        OfLong() {
        }

        OfLong(int i2) {
            super(i2);
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.lang.Iterable
        public void forEach(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                forEach((OfLong) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling SpinedBuffer.OfLong.forEach(Consumer)");
            }
            spliterator().forEachRemaining(consumer);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Type inference failed for: r0v1, types: [long[], long[][]] */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public long[][] newArrayArray(int i2) {
            return new long[i2];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public long[] newArray(int i2) {
            return new long[i2];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public int arrayLength(long[] jArr) {
            return jArr.length;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public void arrayForEach(long[] jArr, int i2, int i3, LongConsumer longConsumer) {
            for (int i4 = i2; i4 < i3; i4++) {
                longConsumer.accept(jArr[i4]);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void accept(long j2) {
            preAccept();
            long[] jArr = (long[]) this.curChunk;
            int i2 = this.elementIndex;
            this.elementIndex = i2 + 1;
            jArr[i2] = j2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public long get(long j2) {
            int iChunkFor = chunkFor(j2);
            if (this.spineIndex == 0 && iChunkFor == 0) {
                return ((long[]) this.curChunk)[(int) j2];
            }
            return ((long[][]) this.spine)[iChunkFor][(int) (j2 - this.priorElementCount[iChunkFor])];
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.lang.Iterable, java.util.List
        public PrimitiveIterator.OfLong iterator() {
            return Spliterators.iterator(spliterator());
        }

        /* renamed from: java.util.stream.SpinedBuffer$OfLong$1Splitr, reason: invalid class name */
        /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfLong$1Splitr.class */
        class C1Splitr extends OfPrimitive<Long, long[], LongConsumer>.BaseSpliterator<Spliterator.OfLong> implements Spliterator.OfLong {
            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining((C1Splitr) longConsumer);
            }

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance((C1Splitr) longConsumer);
            }

            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            C1Splitr(int i2, int i3, int i4, int i5) {
                super(i2, i3, i4, i5);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public C1Splitr newSpliterator(int i2, int i3, int i4, int i5) {
                return OfLong.this.new C1Splitr(i2, i3, i4, i5);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public void arrayForOne(long[] jArr, int i2, LongConsumer longConsumer) {
                longConsumer.accept(jArr[i2]);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public Spliterator.OfLong arraySpliterator(long[] jArr, int i2, int i3) {
                return Arrays.spliterator(jArr, i2, i2 + i3);
            }
        }

        @Override // java.lang.Iterable
        public Spliterator.OfLong spliterator() {
            return new C1Splitr(0, this.spineIndex, 0, this.elementIndex);
        }

        public String toString() {
            long[] jArrAsPrimitiveArray = asPrimitiveArray();
            if (jArrAsPrimitiveArray.length < 200) {
                return String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(jArrAsPrimitiveArray.length), Integer.valueOf(this.spineIndex), Arrays.toString(jArrAsPrimitiveArray));
            }
            return String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(jArrAsPrimitiveArray.length), Integer.valueOf(this.spineIndex), Arrays.toString(Arrays.copyOf(jArrAsPrimitiveArray, 200)));
        }
    }

    /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfDouble.class */
    static class OfDouble extends OfPrimitive<Double, double[], DoubleConsumer> implements DoubleConsumer {
        OfDouble() {
        }

        OfDouble(int i2) {
            super(i2);
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.lang.Iterable
        public void forEach(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                forEach((OfDouble) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling SpinedBuffer.OfDouble.forEach(Consumer)");
            }
            spliterator().forEachRemaining(consumer);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Type inference failed for: r0v1, types: [double[], double[][]] */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public double[][] newArrayArray(int i2) {
            return new double[i2];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public double[] newArray(int i2) {
            return new double[i2];
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public int arrayLength(double[] dArr) {
            return dArr.length;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.SpinedBuffer.OfPrimitive
        public void arrayForEach(double[] dArr, int i2, int i3, DoubleConsumer doubleConsumer) {
            for (int i4 = i2; i4 < i3; i4++) {
                doubleConsumer.accept(dArr[i4]);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void accept(double d2) {
            preAccept();
            double[] dArr = (double[]) this.curChunk;
            int i2 = this.elementIndex;
            this.elementIndex = i2 + 1;
            dArr[i2] = d2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public double get(long j2) {
            int iChunkFor = chunkFor(j2);
            if (this.spineIndex == 0 && iChunkFor == 0) {
                return ((double[]) this.curChunk)[(int) j2];
            }
            return ((double[][]) this.spine)[iChunkFor][(int) (j2 - this.priorElementCount[iChunkFor])];
        }

        @Override // java.util.stream.SpinedBuffer.OfPrimitive, java.lang.Iterable, java.util.List
        public PrimitiveIterator.OfDouble iterator() {
            return Spliterators.iterator(spliterator());
        }

        /* renamed from: java.util.stream.SpinedBuffer$OfDouble$1Splitr, reason: invalid class name */
        /* loaded from: rt.jar:java/util/stream/SpinedBuffer$OfDouble$1Splitr.class */
        class C1Splitr extends OfPrimitive<Double, double[], DoubleConsumer>.BaseSpliterator<Spliterator.OfDouble> implements Spliterator.OfDouble {
            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining((C1Splitr) doubleConsumer);
            }

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance((C1Splitr) doubleConsumer);
            }

            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            C1Splitr(int i2, int i3, int i4, int i5) {
                super(i2, i3, i4, i5);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public C1Splitr newSpliterator(int i2, int i3, int i4, int i5) {
                return OfDouble.this.new C1Splitr(i2, i3, i4, i5);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public void arrayForOne(double[] dArr, int i2, DoubleConsumer doubleConsumer) {
                doubleConsumer.accept(dArr[i2]);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.SpinedBuffer.OfPrimitive.BaseSpliterator
            public Spliterator.OfDouble arraySpliterator(double[] dArr, int i2, int i3) {
                return Arrays.spliterator(dArr, i2, i2 + i3);
            }
        }

        @Override // java.lang.Iterable
        public Spliterator.OfDouble spliterator() {
            return new C1Splitr(0, this.spineIndex, 0, this.elementIndex);
        }

        public String toString() {
            double[] dArrAsPrimitiveArray = asPrimitiveArray();
            if (dArrAsPrimitiveArray.length < 200) {
                return String.format("%s[length=%d, chunks=%d]%s", getClass().getSimpleName(), Integer.valueOf(dArrAsPrimitiveArray.length), Integer.valueOf(this.spineIndex), Arrays.toString(dArrAsPrimitiveArray));
            }
            return String.format("%s[length=%d, chunks=%d]%s...", getClass().getSimpleName(), Integer.valueOf(dArrAsPrimitiveArray.length), Integer.valueOf(this.spineIndex), Arrays.toString(Arrays.copyOf(dArrAsPrimitiveArray, 200)));
        }
    }
}
