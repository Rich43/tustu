package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/Random.class */
public class Random implements Serializable {
    static final long serialVersionUID = 3905348978240129619L;
    private final AtomicLong seed;
    private static final long multiplier = 25214903917L;
    private static final long addend = 11;
    private static final long mask = 281474976710655L;
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16d;
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";
    private double nextNextGaussian;
    private boolean haveNextNextGaussian;
    private static final long seedOffset;
    private static final AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("seed", Long.TYPE), new ObjectStreamField("nextNextGaussian", Double.TYPE), new ObjectStreamField("haveNextNextGaussian", Boolean.TYPE)};
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    public Random() {
        this(seedUniquifier() ^ System.nanoTime());
    }

    private static long seedUniquifier() {
        long j2;
        long j3;
        do {
            j2 = seedUniquifier.get();
            j3 = j2 * 181783497276652981L;
        } while (!seedUniquifier.compareAndSet(j2, j3));
        return j3;
    }

    static {
        try {
            seedOffset = unsafe.objectFieldOffset(Random.class.getDeclaredField("seed"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    public Random(long j2) {
        this.haveNextNextGaussian = false;
        if (getClass() == Random.class) {
            this.seed = new AtomicLong(initialScramble(j2));
        } else {
            this.seed = new AtomicLong();
            setSeed(j2);
        }
    }

    private static long initialScramble(long j2) {
        return (j2 ^ multiplier) & mask;
    }

    public synchronized void setSeed(long j2) {
        this.seed.set(initialScramble(j2));
        this.haveNextNextGaussian = false;
    }

    protected int next(int i2) {
        long j2;
        long j3;
        AtomicLong atomicLong = this.seed;
        do {
            j2 = atomicLong.get();
            j3 = ((j2 * multiplier) + 11) & mask;
        } while (!atomicLong.compareAndSet(j2, j3));
        return (int) (j3 >>> (48 - i2));
    }

    public void nextBytes(byte[] bArr) {
        int i2 = 0;
        int length = bArr.length;
        while (i2 < length) {
            int iNextInt = nextInt();
            int iMin = Math.min(length - i2, 4);
            while (true) {
                int i3 = iMin;
                iMin--;
                if (i3 > 0) {
                    int i4 = i2;
                    i2++;
                    bArr[i4] = (byte) iNextInt;
                    iNextInt >>= 8;
                }
            }
        }
    }

    final long internalNextLong(long j2, long j3) {
        long j4;
        long jNextLong = nextLong();
        if (j2 < j3) {
            long j5 = j3 - j2;
            long j6 = j5 - 1;
            if ((j5 & j6) != 0) {
                if (j5 <= 0) {
                    while (true) {
                        if (jNextLong >= j2 && jNextLong < j3) {
                            break;
                        }
                        jNextLong = nextLong();
                    }
                } else {
                    long jNextLong2 = jNextLong;
                    while (true) {
                        long j7 = jNextLong2 >>> 1;
                        j4 = j7 + j6;
                        if (j4 - (j7 % j5) >= 0) {
                            break;
                        }
                        jNextLong2 = nextLong();
                    }
                    jNextLong = j4 + j2;
                }
            } else {
                jNextLong = (jNextLong & j6) + j2;
            }
        }
        return jNextLong;
    }

    final int internalNextInt(int i2, int i3) {
        if (i2 < i3) {
            int i4 = i3 - i2;
            if (i4 > 0) {
                return nextInt(i4) + i2;
            }
            while (true) {
                int iNextInt = nextInt();
                if (iNextInt >= i2 && iNextInt < i3) {
                    return iNextInt;
                }
            }
        } else {
            return nextInt();
        }
    }

    final double internalNextDouble(double d2, double d3) {
        double dNextDouble = nextDouble();
        if (d2 < d3) {
            dNextDouble = (dNextDouble * (d3 - d2)) + d2;
            if (dNextDouble >= d3) {
                dNextDouble = Double.longBitsToDouble(Double.doubleToLongBits(d3) - 1);
            }
        }
        return dNextDouble;
    }

    public int nextInt() {
        return next(32);
    }

    public int nextInt(int i2) {
        int i3;
        if (i2 <= 0) {
            throw new IllegalArgumentException(BadBound);
        }
        int next = next(31);
        int i4 = i2 - 1;
        if ((i2 & i4) == 0) {
            i3 = (int) ((i2 * next) >> 31);
        } else {
            int next2 = next;
            while (true) {
                int i5 = next2;
                int i6 = i5 % i2;
                i3 = i6;
                if ((i5 - i6) + i4 >= 0) {
                    break;
                }
                next2 = next(31);
            }
        }
        return i3;
    }

    public long nextLong() {
        return (next(32) << 32) + next(32);
    }

    public boolean nextBoolean() {
        return next(1) != 0;
    }

    public float nextFloat() {
        return next(24) / 1.6777216E7f;
    }

    public double nextDouble() {
        return ((next(26) << 27) + next(27)) * DOUBLE_UNIT;
    }

    public synchronized double nextGaussian() {
        if (this.haveNextNextGaussian) {
            this.haveNextNextGaussian = false;
            return this.nextNextGaussian;
        }
        while (true) {
            double dNextDouble = (2.0d * nextDouble()) - 1.0d;
            double dNextDouble2 = (2.0d * nextDouble()) - 1.0d;
            double d2 = (dNextDouble * dNextDouble) + (dNextDouble2 * dNextDouble2);
            if (d2 < 1.0d && d2 != 0.0d) {
                double dSqrt = StrictMath.sqrt(((-2.0d) * StrictMath.log(d2)) / d2);
                this.nextNextGaussian = dNextDouble2 * dSqrt;
                this.haveNextNextGaussian = true;
                return dNextDouble * dSqrt;
            }
        }
    }

    public IntStream ints(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(this, 0L, j2, Integer.MAX_VALUE, 0), false);
    }

    public IntStream ints() {
        return StreamSupport.intStream(new RandomIntsSpliterator(this, 0L, Long.MAX_VALUE, Integer.MAX_VALUE, 0), false);
    }

    public IntStream ints(long j2, int i2, int i3) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (i2 >= i3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(this, 0L, j2, i2, i3), false);
    }

    public IntStream ints(int i2, int i3) {
        if (i2 >= i3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(this, 0L, Long.MAX_VALUE, i2, i3), false);
    }

    public LongStream longs(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(this, 0L, j2, Long.MAX_VALUE, 0L), false);
    }

    public LongStream longs() {
        return StreamSupport.longStream(new RandomLongsSpliterator(this, 0L, Long.MAX_VALUE, Long.MAX_VALUE, 0L), false);
    }

    public LongStream longs(long j2, long j3, long j4) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (j3 >= j4) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(this, 0L, j2, j3, j4), false);
    }

    public LongStream longs(long j2, long j3) {
        if (j2 >= j3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(this, 0L, Long.MAX_VALUE, j2, j3), false);
    }

    public DoubleStream doubles(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0L, j2, Double.MAX_VALUE, 0.0d), false);
    }

    public DoubleStream doubles() {
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0L, Long.MAX_VALUE, Double.MAX_VALUE, 0.0d), false);
    }

    public DoubleStream doubles(long j2, double d2, double d3) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (d2 >= d3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0L, j2, d2, d3), false);
    }

    public DoubleStream doubles(double d2, double d3) {
        if (d2 >= d3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(this, 0L, Long.MAX_VALUE, d2, d3), false);
    }

    /* loaded from: rt.jar:java/util/Random$RandomIntsSpliterator.class */
    static final class RandomIntsSpliterator implements Spliterator.OfInt {
        final Random rng;
        long index;
        final long fence;
        final int origin;
        final int bound;

        RandomIntsSpliterator(Random random, long j2, long j3, int i2, int i3) {
            this.rng = random;
            this.index = j2;
            this.fence = j3;
            this.origin = i2;
            this.bound = i3;
        }

        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public RandomIntsSpliterator trySplit() {
            long j2 = this.index;
            long j3 = (j2 + this.fence) >>> 1;
            if (j3 <= j2) {
                return null;
            }
            Random random = this.rng;
            this.index = j3;
            return new RandomIntsSpliterator(random, j2, j3, this.origin, this.bound);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fence - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17728;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(IntConsumer intConsumer) {
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            long j2 = this.index;
            if (j2 < this.fence) {
                intConsumer.accept(this.rng.internalNextInt(this.origin, this.bound));
                this.index = j2 + 1;
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(IntConsumer intConsumer) {
            long j2;
            if (intConsumer == null) {
                throw new NullPointerException();
            }
            long j3 = this.index;
            long j4 = this.fence;
            if (j3 < j4) {
                this.index = j4;
                Random random = this.rng;
                int i2 = this.origin;
                int i3 = this.bound;
                do {
                    intConsumer.accept(random.internalNextInt(i2, i3));
                    j2 = j3 + 1;
                    j3 = j2;
                } while (j2 < j4);
            }
        }
    }

    /* loaded from: rt.jar:java/util/Random$RandomLongsSpliterator.class */
    static final class RandomLongsSpliterator implements Spliterator.OfLong {
        final Random rng;
        long index;
        final long fence;
        final long origin;
        final long bound;

        RandomLongsSpliterator(Random random, long j2, long j3, long j4, long j5) {
            this.rng = random;
            this.index = j2;
            this.fence = j3;
            this.origin = j4;
            this.bound = j5;
        }

        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public RandomLongsSpliterator trySplit() {
            long j2 = this.index;
            long j3 = (j2 + this.fence) >>> 1;
            if (j3 <= j2) {
                return null;
            }
            Random random = this.rng;
            this.index = j3;
            return new RandomLongsSpliterator(random, j2, j3, this.origin, this.bound);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fence - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17728;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(LongConsumer longConsumer) {
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            long j2 = this.index;
            if (j2 < this.fence) {
                longConsumer.accept(this.rng.internalNextLong(this.origin, this.bound));
                this.index = j2 + 1;
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(LongConsumer longConsumer) {
            long j2;
            if (longConsumer == null) {
                throw new NullPointerException();
            }
            long j3 = this.index;
            long j4 = this.fence;
            if (j3 < j4) {
                this.index = j4;
                Random random = this.rng;
                long j5 = this.origin;
                long j6 = this.bound;
                do {
                    longConsumer.accept(random.internalNextLong(j5, j6));
                    j2 = j3 + 1;
                    j3 = j6;
                } while (j2 < j4);
            }
        }
    }

    /* loaded from: rt.jar:java/util/Random$RandomDoublesSpliterator.class */
    static final class RandomDoublesSpliterator implements Spliterator.OfDouble {
        final Random rng;
        long index;
        final long fence;
        final double origin;
        final double bound;

        RandomDoublesSpliterator(Random random, long j2, long j3, double d2, double d3) {
            this.rng = random;
            this.index = j2;
            this.fence = j3;
            this.origin = d2;
            this.bound = d3;
        }

        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public RandomDoublesSpliterator trySplit() {
            long j2 = this.index;
            long j3 = (j2 + this.fence) >>> 1;
            if (j3 <= j2) {
                return null;
            }
            Random random = this.rng;
            this.index = j3;
            return new RandomDoublesSpliterator(random, j2, j3, this.origin, this.bound);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fence - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17728;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            long j2 = this.index;
            if (j2 < this.fence) {
                doubleConsumer.accept(this.rng.internalNextDouble(this.origin, this.bound));
                this.index = j2 + 1;
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            long j2;
            if (doubleConsumer == null) {
                throw new NullPointerException();
            }
            long j3 = this.index;
            long j4 = this.fence;
            if (j3 < j4) {
                this.index = j4;
                Random random = this.rng;
                double d2 = this.origin;
                double d3 = this.bound;
                do {
                    doubleConsumer.accept(random.internalNextDouble(d2, d3));
                    j2 = j3 + 1;
                    j3 = d3;
                } while (j2 < j4);
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        long j2 = fields.get("seed", -1L);
        if (j2 < 0) {
            throw new StreamCorruptedException("Random: invalid seed");
        }
        resetSeed(j2);
        this.nextNextGaussian = fields.get("nextNextGaussian", 0.0d);
        this.haveNextNextGaussian = fields.get("haveNextNextGaussian", false);
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("seed", this.seed.get());
        putFieldPutFields.put("nextNextGaussian", this.nextNextGaussian);
        putFieldPutFields.put("haveNextNextGaussian", this.haveNextNextGaussian);
        objectOutputStream.writeFields();
    }

    private void resetSeed(long j2) {
        unsafe.putObjectVolatile(this, seedOffset, new AtomicLong(j2));
    }
}
