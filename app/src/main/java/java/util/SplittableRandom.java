package java.util;

import java.security.AccessController;
import java.security.SecureRandom;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/util/SplittableRandom.class */
public final class SplittableRandom {
    private static final long GOLDEN_GAMMA = -7046029254386353131L;
    private static final double DOUBLE_UNIT = 1.1102230246251565E-16d;
    private long seed;
    private final long gamma;
    private static final AtomicLong defaultGen = new AtomicLong(initialSeed());
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";

    /*  JADX ERROR: Failed to decode insn: 0x000A: MOVE_MULTI
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
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    private long nextSeed() {
        /*
            r6 = this;
            r0 = r6
            r1 = r0
            long r1 = r1.seed
            r2 = r6
            long r2 = r2.gamma
            long r1 = r1 + r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.seed = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.SplittableRandom.nextSeed():long");
    }

    private SplittableRandom(long j2, long j3) {
        this.seed = j2;
        this.gamma = j3;
    }

    private static long mix64(long j2) {
        long j3 = (j2 ^ (j2 >>> 30)) * (-4658895280553007687L);
        long j4 = (j3 ^ (j3 >>> 27)) * (-7723592293110705685L);
        return j4 ^ (j4 >>> 31);
    }

    private static int mix32(long j2) {
        long j3 = (j2 ^ (j2 >>> 33)) * 7109453100751455733L;
        return (int) (((j3 ^ (j3 >>> 28)) * (-3808689974395783757L)) >>> 32);
    }

    private static long mixGamma(long j2) {
        long j3 = (j2 ^ (j2 >>> 33)) * (-49064778989728563L);
        long j4 = (j3 ^ (j3 >>> 33)) * (-4265267296055464877L);
        long j5 = (j4 ^ (j4 >>> 33)) | 1;
        return Long.bitCount(j5 ^ (j5 >>> 1)) < 24 ? j5 ^ (-6148914691236517206L) : j5;
    }

    private static long initialSeed() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("java.util.secureRandomSeed"));
        if (str != null && str.equalsIgnoreCase("true")) {
            byte[] seed = SecureRandom.getSeed(8);
            long j2 = seed[0] & 255;
            for (int i2 = 1; i2 < 8; i2++) {
                j2 = (j2 << 8) | (seed[i2] & 255);
            }
            return j2;
        }
        return mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
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

    public SplittableRandom(long j2) {
        this(j2, GOLDEN_GAMMA);
    }

    public SplittableRandom() {
        long andAdd = defaultGen.getAndAdd(4354685564936845354L);
        this.seed = mix64(andAdd);
        this.gamma = mixGamma(andAdd + GOLDEN_GAMMA);
    }

    public SplittableRandom split() {
        return new SplittableRandom(nextLong(), mixGamma(nextSeed()));
    }

    public int nextInt() {
        return mix32(nextSeed());
    }

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

    public boolean nextBoolean() {
        return mix32(nextSeed()) < 0;
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

    /* loaded from: rt.jar:java/util/SplittableRandom$RandomIntsSpliterator.class */
    static final class RandomIntsSpliterator implements Spliterator.OfInt {
        final SplittableRandom rng;
        long index;
        final long fence;
        final int origin;
        final int bound;

        RandomIntsSpliterator(SplittableRandom splittableRandom, long j2, long j3, int i2, int i3) {
            this.rng = splittableRandom;
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
            SplittableRandom splittableRandomSplit = this.rng.split();
            this.index = j3;
            return new RandomIntsSpliterator(splittableRandomSplit, j2, j3, this.origin, this.bound);
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
                SplittableRandom splittableRandom = this.rng;
                int i2 = this.origin;
                int i3 = this.bound;
                do {
                    intConsumer.accept(splittableRandom.internalNextInt(i2, i3));
                    j2 = j3 + 1;
                    j3 = j2;
                } while (j2 < j4);
            }
        }
    }

    /* loaded from: rt.jar:java/util/SplittableRandom$RandomLongsSpliterator.class */
    static final class RandomLongsSpliterator implements Spliterator.OfLong {
        final SplittableRandom rng;
        long index;
        final long fence;
        final long origin;
        final long bound;

        RandomLongsSpliterator(SplittableRandom splittableRandom, long j2, long j3, long j4, long j5) {
            this.rng = splittableRandom;
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
            SplittableRandom splittableRandomSplit = this.rng.split();
            this.index = j3;
            return new RandomLongsSpliterator(splittableRandomSplit, j2, j3, this.origin, this.bound);
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
                SplittableRandom splittableRandom = this.rng;
                long j5 = this.origin;
                long j6 = this.bound;
                do {
                    longConsumer.accept(splittableRandom.internalNextLong(j5, j6));
                    j2 = j3 + 1;
                    j3 = j6;
                } while (j2 < j4);
            }
        }
    }

    /* loaded from: rt.jar:java/util/SplittableRandom$RandomDoublesSpliterator.class */
    static final class RandomDoublesSpliterator implements Spliterator.OfDouble {
        final SplittableRandom rng;
        long index;
        final long fence;
        final double origin;
        final double bound;

        RandomDoublesSpliterator(SplittableRandom splittableRandom, long j2, long j3, double d2, double d3) {
            this.rng = splittableRandom;
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
            SplittableRandom splittableRandomSplit = this.rng.split();
            this.index = j3;
            return new RandomDoublesSpliterator(splittableRandomSplit, j2, j3, this.origin, this.bound);
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
                SplittableRandom splittableRandom = this.rng;
                double d2 = this.origin;
                double d3 = this.bound;
                do {
                    doubleConsumer.accept(splittableRandom.internalNextDouble(d2, d3));
                    j2 = j3 + 1;
                    j3 = d3;
                } while (j2 < j4);
            }
        }
    }
}
