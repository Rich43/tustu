package java.util.concurrent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;
import sun.misc.Unsafe;
import sun.misc.VM;

/* loaded from: rt.jar:java/util/concurrent/ThreadLocalRandom.class */
public class ThreadLocalRandom extends Random {
    private static final long GAMMA = -7046029254386353131L;
    private static final int PROBE_INCREMENT = -1640531527;
    private static final long SEEDER_INCREMENT = -4942790177534073029L;
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16d;
    private static final float FLOAT_UNIT = 5.9604645E-8f;
    boolean initialized = true;
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";
    private static final long serialVersionUID = -5851777807851030925L;
    private static final Unsafe UNSAFE;
    private static final long SEED;
    private static final long PROBE;
    private static final long SECONDARY;
    private static final AtomicInteger probeGenerator = new AtomicInteger();
    private static final AtomicLong seeder = new AtomicLong(initialSeed());
    private static final ThreadLocal<Double> nextLocalGaussian = new ThreadLocal<>();
    static final ThreadLocalRandom instance = new ThreadLocalRandom();
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("rnd", Long.TYPE), new ObjectStreamField("initialized", Boolean.TYPE)};

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            SEED = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomSeed"));
            PROBE = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomProbe"));
            SECONDARY = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomSecondarySeed"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    private static long initialSeed() {
        if (Boolean.parseBoolean(VM.getSavedProperty("java.util.secureRandomSeed"))) {
            byte[] seed = SecureRandom.getSeed(8);
            long j2 = seed[0] & 255;
            for (int i2 = 1; i2 < 8; i2++) {
                j2 = (j2 << 8) | (seed[i2] & 255);
            }
            return j2;
        }
        return mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
    }

    private static long mix64(long j2) {
        long j3 = (j2 ^ (j2 >>> 33)) * (-49064778989728563L);
        long j4 = (j3 ^ (j3 >>> 33)) * (-4265267296055464877L);
        return j4 ^ (j4 >>> 33);
    }

    private static int mix32(long j2) {
        long j3 = (j2 ^ (j2 >>> 33)) * (-49064778989728563L);
        return (int) (((j3 ^ (j3 >>> 33)) * (-4265267296055464877L)) >>> 32);
    }

    private ThreadLocalRandom() {
    }

    static final void localInit() {
        int iAddAndGet = probeGenerator.addAndGet(PROBE_INCREMENT);
        int i2 = iAddAndGet == 0 ? 1 : iAddAndGet;
        long jMix64 = mix64(seeder.getAndAdd(SEEDER_INCREMENT));
        Thread threadCurrentThread = Thread.currentThread();
        UNSAFE.putLong(threadCurrentThread, SEED, jMix64);
        UNSAFE.putInt(threadCurrentThread, PROBE, i2);
    }

    public static ThreadLocalRandom current() {
        if (UNSAFE.getInt(Thread.currentThread(), PROBE) == 0) {
            localInit();
        }
        return instance;
    }

    @Override // java.util.Random
    public void setSeed(long j2) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [long, sun.misc.Unsafe] */
    final long nextSeed() {
        ?? r0 = UNSAFE;
        Thread threadCurrentThread = Thread.currentThread();
        r0.putLong(threadCurrentThread, SEED, UNSAFE.getLong(threadCurrentThread, SEED) + GAMMA);
        return r0;
    }

    @Override // java.util.Random
    protected int next(int i2) {
        return (int) (mix64(nextSeed()) >>> (64 - i2));
    }

    final long internalNextLong(long j2, long j3) {
        long j4;
        long jMix64 = mix64(nextSeed());
        if (j2 < j3) {
            long j5 = j3 - j2;
            long j6 = j5 - 1;
            if ((j5 & j6) != 0) {
                if (j5 <= 0) {
                    while (true) {
                        if (jMix64 >= j2 && jMix64 < j3) {
                            break;
                        }
                        jMix64 = mix64(nextSeed());
                    }
                } else {
                    long jMix642 = jMix64;
                    while (true) {
                        long j7 = jMix642 >>> 1;
                        j4 = j7 + j6;
                        if (j4 - (j7 % j5) >= 0) {
                            break;
                        }
                        jMix642 = mix64(nextSeed());
                    }
                    jMix64 = j4 + j2;
                }
            } else {
                jMix64 = (jMix64 & j6) + j2;
            }
        }
        return jMix64;
    }

    final int internalNextInt(int i2, int i3) {
        int i4;
        int iMix32 = mix32(nextSeed());
        if (i2 < i3) {
            int i5 = i3 - i2;
            int i6 = i5 - 1;
            if ((i5 & i6) == 0) {
                iMix32 = (iMix32 & i6) + i2;
            } else if (i5 <= 0) {
                while (true) {
                    if (iMix32 >= i2 && iMix32 < i3) {
                        break;
                    }
                    iMix32 = mix32(nextSeed());
                }
            } else {
                int iMix322 = iMix32;
                while (true) {
                    int i7 = iMix322 >>> 1;
                    i4 = i7 % i5;
                    if ((i7 + i6) - i4 >= 0) {
                        break;
                    }
                    iMix322 = mix32(nextSeed());
                }
                iMix32 = i4 + i2;
            }
        }
        return iMix32;
    }

    final double internalNextDouble(double d2, double d3) {
        double dNextLong = (nextLong() >>> 11) * DOUBLE_UNIT;
        if (d2 < d3) {
            dNextLong = (dNextLong * (d3 - d2)) + d2;
            if (dNextLong >= d3) {
                dNextLong = Double.longBitsToDouble(Double.doubleToLongBits(d3) - 1);
            }
        }
        return dNextLong;
    }

    @Override // java.util.Random
    public int nextInt() {
        return mix32(nextSeed());
    }

    @Override // java.util.Random
    public int nextInt(int i2) {
        int i3;
        if (i2 <= 0) {
            throw new IllegalArgumentException(BadBound);
        }
        int iMix32 = mix32(nextSeed());
        int i4 = i2 - 1;
        if ((i2 & i4) == 0) {
            i3 = iMix32 & i4;
        } else {
            int iMix322 = iMix32;
            while (true) {
                int i5 = iMix322 >>> 1;
                int i6 = i5 % i2;
                i3 = i6;
                if ((i5 + i4) - i6 >= 0) {
                    break;
                }
                iMix322 = mix32(nextSeed());
            }
        }
        return i3;
    }

    public int nextInt(int i2, int i3) {
        if (i2 >= i3) {
            throw new IllegalArgumentException(BadRange);
        }
        return internalNextInt(i2, i3);
    }

    @Override // java.util.Random
    public long nextLong() {
        return mix64(nextSeed());
    }

    public long nextLong(long j2) {
        long j3;
        if (j2 <= 0) {
            throw new IllegalArgumentException(BadBound);
        }
        long jMix64 = mix64(nextSeed());
        long j4 = j2 - 1;
        if ((j2 & j4) == 0) {
            j3 = jMix64 & j4;
        } else {
            long jMix642 = jMix64;
            while (true) {
                long j5 = jMix642 >>> 1;
                long j6 = j5 + j4;
                j3 = j6;
                if (j6 - (j5 % j2) >= 0) {
                    break;
                }
                jMix642 = mix64(nextSeed());
            }
        }
        return j3;
    }

    public long nextLong(long j2, long j3) {
        if (j2 >= j3) {
            throw new IllegalArgumentException(BadRange);
        }
        return internalNextLong(j2, j3);
    }

    @Override // java.util.Random
    public double nextDouble() {
        return (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT;
    }

    public double nextDouble(double d2) {
        if (d2 <= 0.0d) {
            throw new IllegalArgumentException(BadBound);
        }
        double dMix64 = (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT * d2;
        return dMix64 < d2 ? dMix64 : Double.longBitsToDouble(Double.doubleToLongBits(d2) - 1);
    }

    public double nextDouble(double d2, double d3) {
        if (d2 >= d3) {
            throw new IllegalArgumentException(BadRange);
        }
        return internalNextDouble(d2, d3);
    }

    @Override // java.util.Random
    public boolean nextBoolean() {
        return mix32(nextSeed()) < 0;
    }

    @Override // java.util.Random
    public float nextFloat() {
        return (mix32(nextSeed()) >>> 8) * FLOAT_UNIT;
    }

    @Override // java.util.Random
    public double nextGaussian() {
        Double d2 = nextLocalGaussian.get();
        if (d2 != null) {
            nextLocalGaussian.set(null);
            return d2.doubleValue();
        }
        while (true) {
            double dNextDouble = (2.0d * nextDouble()) - 1.0d;
            double dNextDouble2 = (2.0d * nextDouble()) - 1.0d;
            double d3 = (dNextDouble * dNextDouble) + (dNextDouble2 * dNextDouble2);
            if (d3 < 1.0d && d3 != 0.0d) {
                double dSqrt = StrictMath.sqrt(((-2.0d) * StrictMath.log(d3)) / d3);
                nextLocalGaussian.set(new Double(dNextDouble2 * dSqrt));
                return dNextDouble * dSqrt;
            }
        }
    }

    @Override // java.util.Random
    public IntStream ints(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, j2, Integer.MAX_VALUE, 0), false);
    }

    @Override // java.util.Random
    public IntStream ints() {
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, Long.MAX_VALUE, Integer.MAX_VALUE, 0), false);
    }

    @Override // java.util.Random
    public IntStream ints(long j2, int i2, int i3) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (i2 >= i3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, j2, i2, i3), false);
    }

    @Override // java.util.Random
    public IntStream ints(int i2, int i3) {
        if (i2 >= i3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.intStream(new RandomIntsSpliterator(0L, Long.MAX_VALUE, i2, i3), false);
    }

    @Override // java.util.Random
    public LongStream longs(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, j2, Long.MAX_VALUE, 0L), false);
    }

    @Override // java.util.Random
    public LongStream longs() {
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, Long.MAX_VALUE, Long.MAX_VALUE, 0L), false);
    }

    @Override // java.util.Random
    public LongStream longs(long j2, long j3, long j4) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (j3 >= j4) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, j2, j3, j4), false);
    }

    @Override // java.util.Random
    public LongStream longs(long j2, long j3) {
        if (j2 >= j3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.longStream(new RandomLongsSpliterator(0L, Long.MAX_VALUE, j2, j3), false);
    }

    @Override // java.util.Random
    public DoubleStream doubles(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, j2, Double.MAX_VALUE, 0.0d), false);
    }

    @Override // java.util.Random
    public DoubleStream doubles() {
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, Long.MAX_VALUE, Double.MAX_VALUE, 0.0d), false);
    }

    @Override // java.util.Random
    public DoubleStream doubles(long j2, double d2, double d3) {
        if (j2 < 0) {
            throw new IllegalArgumentException(BadSize);
        }
        if (d2 >= d3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, j2, d2, d3), false);
    }

    @Override // java.util.Random
    public DoubleStream doubles(double d2, double d3) {
        if (d2 >= d3) {
            throw new IllegalArgumentException(BadRange);
        }
        return StreamSupport.doubleStream(new RandomDoublesSpliterator(0L, Long.MAX_VALUE, d2, d3), false);
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadLocalRandom$RandomIntsSpliterator.class */
    static final class RandomIntsSpliterator implements Spliterator.OfInt {
        long index;
        final long fence;
        final int origin;
        final int bound;

        RandomIntsSpliterator(long j2, long j3, int i2, int i3) {
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
            this.index = j3;
            return new RandomIntsSpliterator(j2, j3, this.origin, this.bound);
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
                intConsumer.accept(ThreadLocalRandom.current().internalNextInt(this.origin, this.bound));
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
                int i2 = this.origin;
                int i3 = this.bound;
                ThreadLocalRandom threadLocalRandomCurrent = ThreadLocalRandom.current();
                do {
                    intConsumer.accept(threadLocalRandomCurrent.internalNextInt(i2, i3));
                    j2 = j3 + 1;
                    j3 = j2;
                } while (j2 < j4);
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadLocalRandom$RandomLongsSpliterator.class */
    static final class RandomLongsSpliterator implements Spliterator.OfLong {
        long index;
        final long fence;
        final long origin;
        final long bound;

        RandomLongsSpliterator(long j2, long j3, long j4, long j5) {
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
            this.index = j3;
            return new RandomLongsSpliterator(j2, j3, this.origin, this.bound);
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
                longConsumer.accept(ThreadLocalRandom.current().internalNextLong(this.origin, this.bound));
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
                long j5 = this.origin;
                long j6 = this.bound;
                ThreadLocalRandom threadLocalRandomCurrent = ThreadLocalRandom.current();
                do {
                    longConsumer.accept(threadLocalRandomCurrent.internalNextLong(j5, j6));
                    j2 = j3 + 1;
                    j3 = j6;
                } while (j2 < j4);
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadLocalRandom$RandomDoublesSpliterator.class */
    static final class RandomDoublesSpliterator implements Spliterator.OfDouble {
        long index;
        final long fence;
        final double origin;
        final double bound;

        RandomDoublesSpliterator(long j2, long j3, double d2, double d3) {
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
            this.index = j3;
            return new RandomDoublesSpliterator(j2, j3, this.origin, this.bound);
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
                doubleConsumer.accept(ThreadLocalRandom.current().internalNextDouble(this.origin, this.bound));
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
                double d2 = this.origin;
                double d3 = this.bound;
                ThreadLocalRandom threadLocalRandomCurrent = ThreadLocalRandom.current();
                do {
                    doubleConsumer.accept(threadLocalRandomCurrent.internalNextDouble(d2, d3));
                    j2 = j3 + 1;
                    j3 = d3;
                } while (j2 < j4);
            }
        }
    }

    static final int getProbe() {
        return UNSAFE.getInt(Thread.currentThread(), PROBE);
    }

    static final int advanceProbe(int i2) {
        int i3 = i2 ^ (i2 << 13);
        int i4 = i3 ^ (i3 >>> 17);
        int i5 = i4 ^ (i4 << 5);
        UNSAFE.putInt(Thread.currentThread(), PROBE, i5);
        return i5;
    }

    static final int nextSecondarySeed() {
        int i2;
        Thread threadCurrentThread = Thread.currentThread();
        int i3 = UNSAFE.getInt(threadCurrentThread, SECONDARY);
        if (i3 != 0) {
            int i4 = i3 ^ (i3 << 13);
            int i5 = i4 ^ (i4 >>> 17);
            i2 = i5 ^ (i5 << 5);
        } else {
            localInit();
            int i6 = (int) UNSAFE.getLong(threadCurrentThread, SEED);
            i2 = i6;
            if (i6 == 0) {
                i2 = 1;
            }
        }
        UNSAFE.putInt(threadCurrentThread, SECONDARY, i2);
        return i2;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("rnd", UNSAFE.getLong(Thread.currentThread(), SEED));
        putFieldPutFields.put("initialized", true);
        objectOutputStream.writeFields();
    }

    private Object readResolve() {
        return current();
    }
}
