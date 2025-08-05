package java.util;

import java.util.concurrent.CountedCompleter;

/* loaded from: rt.jar:java/util/ArraysParallelSortHelpers.class */
class ArraysParallelSortHelpers {
    ArraysParallelSortHelpers() {
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$EmptyCompleter.class */
    static final class EmptyCompleter extends CountedCompleter<Void> {
        static final long serialVersionUID = 2446542900576103244L;

        EmptyCompleter(CountedCompleter<?> countedCompleter) {
            super(countedCompleter);
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$Relay.class */
    static final class Relay extends CountedCompleter<Void> {
        static final long serialVersionUID = 2446542900576103244L;
        final CountedCompleter<?> task;

        Relay(CountedCompleter<?> countedCompleter) {
            super(null, 1);
            this.task = countedCompleter;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void onCompletion(CountedCompleter<?> countedCompleter) {
            this.task.compute();
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJObject.class */
    static final class FJObject {
        FJObject() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJObject$Sorter.class */
        static final class Sorter<T> extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final T[] f12522a;

            /* renamed from: w, reason: collision with root package name */
            final T[] f12523w;
            final int base;
            final int size;
            final int wbase;
            final int gran;
            Comparator<? super T> comparator;

            Sorter(CountedCompleter<?> countedCompleter, T[] tArr, T[] tArr2, int i2, int i3, int i4, int i5, Comparator<? super T> comparator) {
                super(countedCompleter);
                this.f12522a = tArr;
                this.f12523w = tArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
                this.comparator = comparator;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                Comparator<? super T> comparator = this.comparator;
                T[] tArr = this.f12522a;
                T[] tArr2 = this.f12523w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, tArr2, tArr, i4, i6, i4 + i6, i3 - i6, i2, i5, comparator));
                    Relay relay2 = new Relay(new Merger(relay, tArr, tArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5, comparator));
                    new Sorter(relay2, tArr, tArr2, i2 + i8, i3 - i8, i4 + i8, i5, comparator).fork();
                    new Sorter(relay2, tArr, tArr2, i2 + i6, i7, i4 + i6, i5, comparator).fork();
                    Relay relay3 = new Relay(new Merger(relay, tArr, tArr2, i2, i7, i2 + i7, i6 - i7, i4, i5, comparator));
                    new Sorter(relay3, tArr, tArr2, i2 + i7, i6 - i7, i4 + i7, i5, comparator).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                TimSort.sort(tArr, i2, i2 + i3, comparator, tArr2, i4, i3);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJObject$Merger.class */
        static final class Merger<T> extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final T[] f12520a;

            /* renamed from: w, reason: collision with root package name */
            final T[] f12521w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;
            Comparator<? super T> comparator;

            Merger(CountedCompleter<?> countedCompleter, T[] tArr, T[] tArr2, int i2, int i3, int i4, int i5, int i6, int i7, Comparator<? super T> comparator) {
                super(countedCompleter);
                this.f12520a = tArr;
                this.f12521w = tArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
                this.comparator = comparator;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                Object obj;
                Comparator<? super T> comparator = this.comparator;
                T[] tArr = this.f12520a;
                T[] tArr2 = this.f12521w;
                int i4 = this.lbase;
                int i5 = this.lsize;
                int i6 = this.rbase;
                int i7 = this.rsize;
                int i8 = this.wbase;
                int i9 = this.gran;
                if (tArr == null || tArr2 == null || i4 < 0 || i6 < 0 || i8 < 0 || comparator == null) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i5 >= i7) {
                        if (i5 <= i9) {
                            break;
                        }
                        i3 = i7;
                        int i10 = i5 >>> 1;
                        i2 = i10;
                        Object obj2 = (Object) tArr[i10 + i4];
                        int i11 = 0;
                        while (i11 < i3) {
                            int i12 = (i11 + i3) >>> 1;
                            if (comparator.compare(obj2, (Object) tArr[i12 + i6]) <= 0) {
                                i3 = i12;
                            } else {
                                i11 = i12 + 1;
                            }
                        }
                        Merger merger = new Merger(this, tArr, tArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9, comparator);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i7 <= i9) {
                            break;
                        }
                        i2 = i5;
                        int i13 = i7 >>> 1;
                        i3 = i13;
                        Object obj3 = (Object) tArr[i13 + i6];
                        int i14 = 0;
                        while (i14 < i2) {
                            int i15 = (i14 + i2) >>> 1;
                            if (comparator.compare(obj3, (Object) tArr[i15 + i4]) <= 0) {
                                i2 = i15;
                            } else {
                                i14 = i15 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, tArr, tArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9, comparator);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i16 = i4 + i5;
                int i17 = i6 + i7;
                while (i4 < i16 && i6 < i17) {
                    Object obj4 = (Object) tArr[i4];
                    Object obj5 = (Object) tArr[i6];
                    if (comparator.compare(obj4, obj5) <= 0) {
                        i4++;
                        obj = obj4;
                    } else {
                        i6++;
                        obj = obj5;
                    }
                    int i18 = i8;
                    i8++;
                    tArr2[i18] = obj;
                }
                if (i6 < i17) {
                    System.arraycopy(tArr, i6, tArr2, i8, i17 - i6);
                } else if (i4 < i16) {
                    System.arraycopy(tArr, i4, tArr2, i8, i16 - i4);
                }
                tryComplete();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJByte.class */
    static final class FJByte {
        FJByte() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJByte$Sorter.class */
        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final byte[] f12498a;

            /* renamed from: w, reason: collision with root package name */
            final byte[] f12499w;
            final int base;
            final int size;
            final int wbase;
            final int gran;

            Sorter(CountedCompleter<?> countedCompleter, byte[] bArr, byte[] bArr2, int i2, int i3, int i4, int i5) {
                super(countedCompleter);
                this.f12498a = bArr;
                this.f12499w = bArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                byte[] bArr = this.f12498a;
                byte[] bArr2 = this.f12499w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, bArr2, bArr, i4, i6, i4 + i6, i3 - i6, i2, i5));
                    Relay relay2 = new Relay(new Merger(relay, bArr, bArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5));
                    new Sorter(relay2, bArr, bArr2, i2 + i8, i3 - i8, i4 + i8, i5).fork();
                    new Sorter(relay2, bArr, bArr2, i2 + i6, i7, i4 + i6, i5).fork();
                    Relay relay3 = new Relay(new Merger(relay, bArr, bArr2, i2, i7, i2 + i7, i6 - i7, i4, i5));
                    new Sorter(relay3, bArr, bArr2, i2 + i7, i6 - i7, i4 + i7, i5).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                DualPivotQuicksort.sort(bArr, i2, (i2 + i3) - 1);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJByte$Merger.class */
        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final byte[] f12496a;

            /* renamed from: w, reason: collision with root package name */
            final byte[] f12497w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;

            Merger(CountedCompleter<?> countedCompleter, byte[] bArr, byte[] bArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
                super(countedCompleter);
                this.f12496a = bArr;
                this.f12497w = bArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                byte b2;
                byte[] bArr = this.f12496a;
                byte[] bArr2 = this.f12497w;
                int i4 = this.lbase;
                int i5 = this.lsize;
                int i6 = this.rbase;
                int i7 = this.rsize;
                int i8 = this.wbase;
                int i9 = this.gran;
                if (bArr == null || bArr2 == null || i4 < 0 || i6 < 0 || i8 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i5 >= i7) {
                        if (i5 <= i9) {
                            break;
                        }
                        i3 = i7;
                        int i10 = i5 >>> 1;
                        i2 = i10;
                        byte b3 = bArr[i10 + i4];
                        int i11 = 0;
                        while (i11 < i3) {
                            int i12 = (i11 + i3) >>> 1;
                            if (b3 <= bArr[i12 + i6]) {
                                i3 = i12;
                            } else {
                                i11 = i12 + 1;
                            }
                        }
                        Merger merger = new Merger(this, bArr, bArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i7 <= i9) {
                            break;
                        }
                        i2 = i5;
                        int i13 = i7 >>> 1;
                        i3 = i13;
                        byte b4 = bArr[i13 + i6];
                        int i14 = 0;
                        while (i14 < i2) {
                            int i15 = (i14 + i2) >>> 1;
                            if (b4 <= bArr[i15 + i4]) {
                                i2 = i15;
                            } else {
                                i14 = i15 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, bArr, bArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i16 = i4 + i5;
                int i17 = i6 + i7;
                while (i4 < i16 && i6 < i17) {
                    byte b5 = bArr[i4];
                    byte b6 = bArr[i6];
                    if (b5 <= b6) {
                        i4++;
                        b2 = b5;
                    } else {
                        i6++;
                        b2 = b6;
                    }
                    int i18 = i8;
                    i8++;
                    bArr2[i18] = b2;
                }
                if (i6 < i17) {
                    System.arraycopy(bArr, i6, bArr2, i8, i17 - i6);
                } else if (i4 < i16) {
                    System.arraycopy(bArr, i4, bArr2, i8, i16 - i4);
                }
                tryComplete();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJChar.class */
    static final class FJChar {
        FJChar() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJChar$Sorter.class */
        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final char[] f12502a;

            /* renamed from: w, reason: collision with root package name */
            final char[] f12503w;
            final int base;
            final int size;
            final int wbase;
            final int gran;

            Sorter(CountedCompleter<?> countedCompleter, char[] cArr, char[] cArr2, int i2, int i3, int i4, int i5) {
                super(countedCompleter);
                this.f12502a = cArr;
                this.f12503w = cArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                char[] cArr = this.f12502a;
                char[] cArr2 = this.f12503w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, cArr2, cArr, i4, i6, i4 + i6, i3 - i6, i2, i5));
                    Relay relay2 = new Relay(new Merger(relay, cArr, cArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5));
                    new Sorter(relay2, cArr, cArr2, i2 + i8, i3 - i8, i4 + i8, i5).fork();
                    new Sorter(relay2, cArr, cArr2, i2 + i6, i7, i4 + i6, i5).fork();
                    Relay relay3 = new Relay(new Merger(relay, cArr, cArr2, i2, i7, i2 + i7, i6 - i7, i4, i5));
                    new Sorter(relay3, cArr, cArr2, i2 + i7, i6 - i7, i4 + i7, i5).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                DualPivotQuicksort.sort(cArr, i2, (i2 + i3) - 1, cArr2, i4, i3);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJChar$Merger.class */
        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final char[] f12500a;

            /* renamed from: w, reason: collision with root package name */
            final char[] f12501w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;

            Merger(CountedCompleter<?> countedCompleter, char[] cArr, char[] cArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
                super(countedCompleter);
                this.f12500a = cArr;
                this.f12501w = cArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                char c2;
                char[] cArr = this.f12500a;
                char[] cArr2 = this.f12501w;
                int i4 = this.lbase;
                int i5 = this.lsize;
                int i6 = this.rbase;
                int i7 = this.rsize;
                int i8 = this.wbase;
                int i9 = this.gran;
                if (cArr == null || cArr2 == null || i4 < 0 || i6 < 0 || i8 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i5 >= i7) {
                        if (i5 <= i9) {
                            break;
                        }
                        i3 = i7;
                        int i10 = i5 >>> 1;
                        i2 = i10;
                        char c3 = cArr[i10 + i4];
                        int i11 = 0;
                        while (i11 < i3) {
                            int i12 = (i11 + i3) >>> 1;
                            if (c3 <= cArr[i12 + i6]) {
                                i3 = i12;
                            } else {
                                i11 = i12 + 1;
                            }
                        }
                        Merger merger = new Merger(this, cArr, cArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i7 <= i9) {
                            break;
                        }
                        i2 = i5;
                        int i13 = i7 >>> 1;
                        i3 = i13;
                        char c4 = cArr[i13 + i6];
                        int i14 = 0;
                        while (i14 < i2) {
                            int i15 = (i14 + i2) >>> 1;
                            if (c4 <= cArr[i15 + i4]) {
                                i2 = i15;
                            } else {
                                i14 = i15 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, cArr, cArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i16 = i4 + i5;
                int i17 = i6 + i7;
                while (i4 < i16 && i6 < i17) {
                    char c5 = cArr[i4];
                    char c6 = cArr[i6];
                    if (c5 <= c6) {
                        i4++;
                        c2 = c5;
                    } else {
                        i6++;
                        c2 = c6;
                    }
                    int i18 = i8;
                    i8++;
                    cArr2[i18] = c2;
                }
                if (i6 < i17) {
                    System.arraycopy(cArr, i6, cArr2, i8, i17 - i6);
                } else if (i4 < i16) {
                    System.arraycopy(cArr, i4, cArr2, i8, i16 - i4);
                }
                tryComplete();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJShort.class */
    static final class FJShort {
        FJShort() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJShort$Sorter.class */
        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final short[] f12526a;

            /* renamed from: w, reason: collision with root package name */
            final short[] f12527w;
            final int base;
            final int size;
            final int wbase;
            final int gran;

            Sorter(CountedCompleter<?> countedCompleter, short[] sArr, short[] sArr2, int i2, int i3, int i4, int i5) {
                super(countedCompleter);
                this.f12526a = sArr;
                this.f12527w = sArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                short[] sArr = this.f12526a;
                short[] sArr2 = this.f12527w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, sArr2, sArr, i4, i6, i4 + i6, i3 - i6, i2, i5));
                    Relay relay2 = new Relay(new Merger(relay, sArr, sArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5));
                    new Sorter(relay2, sArr, sArr2, i2 + i8, i3 - i8, i4 + i8, i5).fork();
                    new Sorter(relay2, sArr, sArr2, i2 + i6, i7, i4 + i6, i5).fork();
                    Relay relay3 = new Relay(new Merger(relay, sArr, sArr2, i2, i7, i2 + i7, i6 - i7, i4, i5));
                    new Sorter(relay3, sArr, sArr2, i2 + i7, i6 - i7, i4 + i7, i5).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                DualPivotQuicksort.sort(sArr, i2, (i2 + i3) - 1, sArr2, i4, i3);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJShort$Merger.class */
        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final short[] f12524a;

            /* renamed from: w, reason: collision with root package name */
            final short[] f12525w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;

            Merger(CountedCompleter<?> countedCompleter, short[] sArr, short[] sArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
                super(countedCompleter);
                this.f12524a = sArr;
                this.f12525w = sArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                short s2;
                short[] sArr = this.f12524a;
                short[] sArr2 = this.f12525w;
                int i4 = this.lbase;
                int i5 = this.lsize;
                int i6 = this.rbase;
                int i7 = this.rsize;
                int i8 = this.wbase;
                int i9 = this.gran;
                if (sArr == null || sArr2 == null || i4 < 0 || i6 < 0 || i8 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i5 >= i7) {
                        if (i5 <= i9) {
                            break;
                        }
                        i3 = i7;
                        int i10 = i5 >>> 1;
                        i2 = i10;
                        short s3 = sArr[i10 + i4];
                        int i11 = 0;
                        while (i11 < i3) {
                            int i12 = (i11 + i3) >>> 1;
                            if (s3 <= sArr[i12 + i6]) {
                                i3 = i12;
                            } else {
                                i11 = i12 + 1;
                            }
                        }
                        Merger merger = new Merger(this, sArr, sArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i7 <= i9) {
                            break;
                        }
                        i2 = i5;
                        int i13 = i7 >>> 1;
                        i3 = i13;
                        short s4 = sArr[i13 + i6];
                        int i14 = 0;
                        while (i14 < i2) {
                            int i15 = (i14 + i2) >>> 1;
                            if (s4 <= sArr[i15 + i4]) {
                                i2 = i15;
                            } else {
                                i14 = i15 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, sArr, sArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i16 = i4 + i5;
                int i17 = i6 + i7;
                while (i4 < i16 && i6 < i17) {
                    short s5 = sArr[i4];
                    short s6 = sArr[i6];
                    if (s5 <= s6) {
                        i4++;
                        s2 = s5;
                    } else {
                        i6++;
                        s2 = s6;
                    }
                    int i18 = i8;
                    i8++;
                    sArr2[i18] = s2;
                }
                if (i6 < i17) {
                    System.arraycopy(sArr, i6, sArr2, i8, i17 - i6);
                } else if (i4 < i16) {
                    System.arraycopy(sArr, i4, sArr2, i8, i16 - i4);
                }
                tryComplete();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJInt.class */
    static final class FJInt {
        FJInt() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJInt$Sorter.class */
        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final int[] f12514a;

            /* renamed from: w, reason: collision with root package name */
            final int[] f12515w;
            final int base;
            final int size;
            final int wbase;
            final int gran;

            Sorter(CountedCompleter<?> countedCompleter, int[] iArr, int[] iArr2, int i2, int i3, int i4, int i5) {
                super(countedCompleter);
                this.f12514a = iArr;
                this.f12515w = iArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                int[] iArr = this.f12514a;
                int[] iArr2 = this.f12515w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, iArr2, iArr, i4, i6, i4 + i6, i3 - i6, i2, i5));
                    Relay relay2 = new Relay(new Merger(relay, iArr, iArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5));
                    new Sorter(relay2, iArr, iArr2, i2 + i8, i3 - i8, i4 + i8, i5).fork();
                    new Sorter(relay2, iArr, iArr2, i2 + i6, i7, i4 + i6, i5).fork();
                    Relay relay3 = new Relay(new Merger(relay, iArr, iArr2, i2, i7, i2 + i7, i6 - i7, i4, i5));
                    new Sorter(relay3, iArr, iArr2, i2 + i7, i6 - i7, i4 + i7, i5).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                DualPivotQuicksort.sort(iArr, i2, (i2 + i3) - 1, iArr2, i4, i3);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJInt$Merger.class */
        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final int[] f12512a;

            /* renamed from: w, reason: collision with root package name */
            final int[] f12513w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;

            Merger(CountedCompleter<?> countedCompleter, int[] iArr, int[] iArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
                super(countedCompleter);
                this.f12512a = iArr;
                this.f12513w = iArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                int i4;
                int[] iArr = this.f12512a;
                int[] iArr2 = this.f12513w;
                int i5 = this.lbase;
                int i6 = this.lsize;
                int i7 = this.rbase;
                int i8 = this.rsize;
                int i9 = this.wbase;
                int i10 = this.gran;
                if (iArr == null || iArr2 == null || i5 < 0 || i7 < 0 || i9 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i6 >= i8) {
                        if (i6 <= i10) {
                            break;
                        }
                        i3 = i8;
                        int i11 = i6 >>> 1;
                        i2 = i11;
                        int i12 = iArr[i11 + i5];
                        int i13 = 0;
                        while (i13 < i3) {
                            int i14 = (i13 + i3) >>> 1;
                            if (i12 <= iArr[i14 + i7]) {
                                i3 = i14;
                            } else {
                                i13 = i14 + 1;
                            }
                        }
                        Merger merger = new Merger(this, iArr, iArr2, i5 + i2, i6 - i2, i7 + i3, i8 - i3, i9 + i2 + i3, i10);
                        i8 = i3;
                        i6 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i8 <= i10) {
                            break;
                        }
                        i2 = i6;
                        int i15 = i8 >>> 1;
                        i3 = i15;
                        int i16 = iArr[i15 + i7];
                        int i17 = 0;
                        while (i17 < i2) {
                            int i18 = (i17 + i2) >>> 1;
                            if (i16 <= iArr[i18 + i5]) {
                                i2 = i18;
                            } else {
                                i17 = i18 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, iArr, iArr2, i5 + i2, i6 - i2, i7 + i3, i8 - i3, i9 + i2 + i3, i10);
                        i8 = i3;
                        i6 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i19 = i5 + i6;
                int i20 = i7 + i8;
                while (i5 < i19 && i7 < i20) {
                    int i21 = iArr[i5];
                    int i22 = iArr[i7];
                    if (i21 <= i22) {
                        i5++;
                        i4 = i21;
                    } else {
                        i7++;
                        i4 = i22;
                    }
                    int i23 = i9;
                    i9++;
                    iArr2[i23] = i4;
                }
                if (i7 < i20) {
                    System.arraycopy(iArr, i7, iArr2, i9, i20 - i7);
                } else if (i5 < i19) {
                    System.arraycopy(iArr, i5, iArr2, i9, i19 - i5);
                }
                tryComplete();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJLong.class */
    static final class FJLong {
        FJLong() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJLong$Sorter.class */
        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final long[] f12518a;

            /* renamed from: w, reason: collision with root package name */
            final long[] f12519w;
            final int base;
            final int size;
            final int wbase;
            final int gran;

            Sorter(CountedCompleter<?> countedCompleter, long[] jArr, long[] jArr2, int i2, int i3, int i4, int i5) {
                super(countedCompleter);
                this.f12518a = jArr;
                this.f12519w = jArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                long[] jArr = this.f12518a;
                long[] jArr2 = this.f12519w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, jArr2, jArr, i4, i6, i4 + i6, i3 - i6, i2, i5));
                    Relay relay2 = new Relay(new Merger(relay, jArr, jArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5));
                    new Sorter(relay2, jArr, jArr2, i2 + i8, i3 - i8, i4 + i8, i5).fork();
                    new Sorter(relay2, jArr, jArr2, i2 + i6, i7, i4 + i6, i5).fork();
                    Relay relay3 = new Relay(new Merger(relay, jArr, jArr2, i2, i7, i2 + i7, i6 - i7, i4, i5));
                    new Sorter(relay3, jArr, jArr2, i2 + i7, i6 - i7, i4 + i7, i5).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                DualPivotQuicksort.sort(jArr, i2, (i2 + i3) - 1, jArr2, i4, i3);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJLong$Merger.class */
        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final long[] f12516a;

            /* renamed from: w, reason: collision with root package name */
            final long[] f12517w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;

            Merger(CountedCompleter<?> countedCompleter, long[] jArr, long[] jArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
                super(countedCompleter);
                this.f12516a = jArr;
                this.f12517w = jArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
            }

            /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Object, long[]] */
            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                long j2;
                long[] jArr = this.f12516a;
                ?? r0 = this.f12517w;
                int i4 = this.lbase;
                int i5 = this.lsize;
                int i6 = this.rbase;
                int i7 = this.rsize;
                int i8 = this.wbase;
                int i9 = this.gran;
                if (jArr == null || r0 == 0 || i4 < 0 || i6 < 0 || i8 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i5 >= i7) {
                        if (i5 <= i9) {
                            break;
                        }
                        i3 = i7;
                        int i10 = i5 >>> 1;
                        i2 = i10;
                        long j3 = jArr[i10 + i4];
                        int i11 = 0;
                        while (i11 < i3) {
                            int i12 = (i11 + i3) >>> 1;
                            if (j3 <= jArr[i12 + i6]) {
                                i3 = i12;
                            } else {
                                i11 = i12 + 1;
                            }
                        }
                        Merger merger = new Merger(this, jArr, r0, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i7 <= i9) {
                            break;
                        }
                        i2 = i5;
                        int i13 = i7 >>> 1;
                        i3 = i13;
                        long j4 = jArr[i13 + i6];
                        int i14 = 0;
                        while (i14 < i2) {
                            int i15 = (i14 + i2) >>> 1;
                            if (j4 <= jArr[i15 + i4]) {
                                i2 = i15;
                            } else {
                                i14 = i15 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, jArr, r0, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i16 = i4 + i5;
                int i17 = i6 + i7;
                while (i4 < i16 && i6 < i17) {
                    if (jArr[i4] <= jArr[i6]) {
                        i4++;
                        j2 = r0;
                    } else {
                        i6++;
                        j2 = r0;
                    }
                    int i18 = i8;
                    i8++;
                    r0[i18] = j2;
                }
                if (i6 < i17) {
                    System.arraycopy(jArr, i6, r0, i8, i17 - i6);
                } else if (i4 < i16) {
                    System.arraycopy(jArr, i4, r0, i8, i16 - i4);
                }
                tryComplete();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJFloat.class */
    static final class FJFloat {
        FJFloat() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJFloat$Sorter.class */
        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final float[] f12510a;

            /* renamed from: w, reason: collision with root package name */
            final float[] f12511w;
            final int base;
            final int size;
            final int wbase;
            final int gran;

            Sorter(CountedCompleter<?> countedCompleter, float[] fArr, float[] fArr2, int i2, int i3, int i4, int i5) {
                super(countedCompleter);
                this.f12510a = fArr;
                this.f12511w = fArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                float[] fArr = this.f12510a;
                float[] fArr2 = this.f12511w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, fArr2, fArr, i4, i6, i4 + i6, i3 - i6, i2, i5));
                    Relay relay2 = new Relay(new Merger(relay, fArr, fArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5));
                    new Sorter(relay2, fArr, fArr2, i2 + i8, i3 - i8, i4 + i8, i5).fork();
                    new Sorter(relay2, fArr, fArr2, i2 + i6, i7, i4 + i6, i5).fork();
                    Relay relay3 = new Relay(new Merger(relay, fArr, fArr2, i2, i7, i2 + i7, i6 - i7, i4, i5));
                    new Sorter(relay3, fArr, fArr2, i2 + i7, i6 - i7, i4 + i7, i5).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                DualPivotQuicksort.sort(fArr, i2, (i2 + i3) - 1, fArr2, i4, i3);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJFloat$Merger.class */
        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final float[] f12508a;

            /* renamed from: w, reason: collision with root package name */
            final float[] f12509w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;

            Merger(CountedCompleter<?> countedCompleter, float[] fArr, float[] fArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
                super(countedCompleter);
                this.f12508a = fArr;
                this.f12509w = fArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                float f2;
                float[] fArr = this.f12508a;
                float[] fArr2 = this.f12509w;
                int i4 = this.lbase;
                int i5 = this.lsize;
                int i6 = this.rbase;
                int i7 = this.rsize;
                int i8 = this.wbase;
                int i9 = this.gran;
                if (fArr == null || fArr2 == null || i4 < 0 || i6 < 0 || i8 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i5 >= i7) {
                        if (i5 <= i9) {
                            break;
                        }
                        i3 = i7;
                        int i10 = i5 >>> 1;
                        i2 = i10;
                        float f3 = fArr[i10 + i4];
                        int i11 = 0;
                        while (i11 < i3) {
                            int i12 = (i11 + i3) >>> 1;
                            if (f3 <= fArr[i12 + i6]) {
                                i3 = i12;
                            } else {
                                i11 = i12 + 1;
                            }
                        }
                        Merger merger = new Merger(this, fArr, fArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i7 <= i9) {
                            break;
                        }
                        i2 = i5;
                        int i13 = i7 >>> 1;
                        i3 = i13;
                        float f4 = fArr[i13 + i6];
                        int i14 = 0;
                        while (i14 < i2) {
                            int i15 = (i14 + i2) >>> 1;
                            if (f4 <= fArr[i15 + i4]) {
                                i2 = i15;
                            } else {
                                i14 = i15 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, fArr, fArr2, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i16 = i4 + i5;
                int i17 = i6 + i7;
                while (i4 < i16 && i6 < i17) {
                    float f5 = fArr[i4];
                    float f6 = fArr[i6];
                    if (f5 <= f6) {
                        i4++;
                        f2 = f5;
                    } else {
                        i6++;
                        f2 = f6;
                    }
                    int i18 = i8;
                    i8++;
                    fArr2[i18] = f2;
                }
                if (i6 < i17) {
                    System.arraycopy(fArr, i6, fArr2, i8, i17 - i6);
                } else if (i4 < i16) {
                    System.arraycopy(fArr, i4, fArr2, i8, i16 - i4);
                }
                tryComplete();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJDouble.class */
    static final class FJDouble {
        FJDouble() {
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJDouble$Sorter.class */
        static final class Sorter extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final double[] f12506a;

            /* renamed from: w, reason: collision with root package name */
            final double[] f12507w;
            final int base;
            final int size;
            final int wbase;
            final int gran;

            Sorter(CountedCompleter<?> countedCompleter, double[] dArr, double[] dArr2, int i2, int i3, int i4, int i5) {
                super(countedCompleter);
                this.f12506a = dArr;
                this.f12507w = dArr2;
                this.base = i2;
                this.size = i3;
                this.wbase = i4;
                this.gran = i5;
            }

            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                CountedCompleter emptyCompleter = this;
                double[] dArr = this.f12506a;
                double[] dArr2 = this.f12507w;
                int i2 = this.base;
                int i3 = this.size;
                int i4 = this.wbase;
                int i5 = this.gran;
                while (i3 > i5) {
                    int i6 = i3 >>> 1;
                    int i7 = i6 >>> 1;
                    int i8 = i6 + i7;
                    Relay relay = new Relay(new Merger(emptyCompleter, dArr2, dArr, i4, i6, i4 + i6, i3 - i6, i2, i5));
                    Relay relay2 = new Relay(new Merger(relay, dArr, dArr2, i2 + i6, i7, i2 + i8, i3 - i8, i4 + i6, i5));
                    new Sorter(relay2, dArr, dArr2, i2 + i8, i3 - i8, i4 + i8, i5).fork();
                    new Sorter(relay2, dArr, dArr2, i2 + i6, i7, i4 + i6, i5).fork();
                    Relay relay3 = new Relay(new Merger(relay, dArr, dArr2, i2, i7, i2 + i7, i6 - i7, i4, i5));
                    new Sorter(relay3, dArr, dArr2, i2 + i7, i6 - i7, i4 + i7, i5).fork();
                    emptyCompleter = new EmptyCompleter(relay3);
                    i3 = i7;
                }
                DualPivotQuicksort.sort(dArr, i2, (i2 + i3) - 1, dArr2, i4, i3);
                emptyCompleter.tryComplete();
            }
        }

        /* loaded from: rt.jar:java/util/ArraysParallelSortHelpers$FJDouble$Merger.class */
        static final class Merger extends CountedCompleter<Void> {
            static final long serialVersionUID = 2446542900576103244L;

            /* renamed from: a, reason: collision with root package name */
            final double[] f12504a;

            /* renamed from: w, reason: collision with root package name */
            final double[] f12505w;
            final int lbase;
            final int lsize;
            final int rbase;
            final int rsize;
            final int wbase;
            final int gran;

            Merger(CountedCompleter<?> countedCompleter, double[] dArr, double[] dArr2, int i2, int i3, int i4, int i5, int i6, int i7) {
                super(countedCompleter);
                this.f12504a = dArr;
                this.f12505w = dArr2;
                this.lbase = i2;
                this.lsize = i3;
                this.rbase = i4;
                this.rsize = i5;
                this.wbase = i6;
                this.gran = i7;
            }

            /* JADX WARN: Type inference failed for: r0v3, types: [double[], java.lang.Object] */
            @Override // java.util.concurrent.CountedCompleter
            public final void compute() {
                int i2;
                int i3;
                long j2;
                double[] dArr = this.f12504a;
                ?? r0 = this.f12505w;
                int i4 = this.lbase;
                int i5 = this.lsize;
                int i6 = this.rbase;
                int i7 = this.rsize;
                int i8 = this.wbase;
                int i9 = this.gran;
                if (dArr == null || r0 == 0 || i4 < 0 || i6 < 0 || i8 < 0) {
                    throw new IllegalStateException();
                }
                while (true) {
                    if (i5 >= i7) {
                        if (i5 <= i9) {
                            break;
                        }
                        i3 = i7;
                        int i10 = i5 >>> 1;
                        i2 = i10;
                        double d2 = dArr[i10 + i4];
                        int i11 = 0;
                        while (i11 < i3) {
                            int i12 = (i11 + i3) >>> 1;
                            if (d2 <= dArr[i12 + i6]) {
                                i3 = i12;
                            } else {
                                i11 = i12 + 1;
                            }
                        }
                        Merger merger = new Merger(this, dArr, r0, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger.fork();
                    } else {
                        if (i7 <= i9) {
                            break;
                        }
                        i2 = i5;
                        int i13 = i7 >>> 1;
                        i3 = i13;
                        double d3 = dArr[i13 + i6];
                        int i14 = 0;
                        while (i14 < i2) {
                            int i15 = (i14 + i2) >>> 1;
                            if (d3 <= dArr[i15 + i4]) {
                                i2 = i15;
                            } else {
                                i14 = i15 + 1;
                            }
                        }
                        Merger merger2 = new Merger(this, dArr, r0, i4 + i2, i5 - i2, i6 + i3, i7 - i3, i8 + i2 + i3, i9);
                        i7 = i3;
                        i5 = i2;
                        addToPendingCount(1);
                        merger2.fork();
                    }
                }
                int i16 = i4 + i5;
                int i17 = i6 + i7;
                while (i4 < i16 && i6 < i17) {
                    if (dArr[i4] <= dArr[i6]) {
                        i4++;
                        j2 = r0;
                    } else {
                        i6++;
                        j2 = r0;
                    }
                    int i18 = i8;
                    i8++;
                    r0[i18] = j2;
                }
                if (i6 < i17) {
                    System.arraycopy(dArr, i6, r0, i8, i17 - i6);
                } else if (i4 < i16) {
                    System.arraycopy(dArr, i4, r0, i8, i16 - i4);
                }
                tryComplete();
            }
        }
    }
}
