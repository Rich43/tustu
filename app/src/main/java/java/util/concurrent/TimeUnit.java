package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/TimeUnit.class */
public enum TimeUnit {
    NANOSECONDS { // from class: java.util.concurrent.TimeUnit.1
        @Override // java.util.concurrent.TimeUnit
        public long toNanos(long j2) {
            return j2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMicros(long j2) {
            return j2 / 1000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMillis(long j2) {
            return j2 / TimeUnit.C2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toSeconds(long j2) {
            return j2 / 1000000000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMinutes(long j2) {
            return j2 / TimeUnit.C4;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toHours(long j2) {
            return j2 / TimeUnit.C5;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toDays(long j2) {
            return j2 / TimeUnit.C6;
        }

        @Override // java.util.concurrent.TimeUnit
        public long convert(long j2, TimeUnit timeUnit) {
            return timeUnit.toNanos(j2);
        }

        @Override // java.util.concurrent.TimeUnit
        int excessNanos(long j2, long j3) {
            return (int) (j2 - (j3 * TimeUnit.C2));
        }
    },
    MICROSECONDS { // from class: java.util.concurrent.TimeUnit.2
        @Override // java.util.concurrent.TimeUnit
        public long toNanos(long j2) {
            return x(j2, 1000L, 9223372036854775L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMicros(long j2) {
            return j2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMillis(long j2) {
            return j2 / 1000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toSeconds(long j2) {
            return j2 / TimeUnit.C2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMinutes(long j2) {
            return j2 / 60000000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toHours(long j2) {
            return j2 / 3600000000L;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toDays(long j2) {
            return j2 / 86400000000L;
        }

        @Override // java.util.concurrent.TimeUnit
        public long convert(long j2, TimeUnit timeUnit) {
            return timeUnit.toMicros(j2);
        }

        @Override // java.util.concurrent.TimeUnit
        int excessNanos(long j2, long j3) {
            return (int) ((j2 * 1000) - (j3 * TimeUnit.C2));
        }
    },
    MILLISECONDS { // from class: java.util.concurrent.TimeUnit.3
        @Override // java.util.concurrent.TimeUnit
        public long toNanos(long j2) {
            return x(j2, TimeUnit.C2, 9223372036854L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMicros(long j2) {
            return x(j2, 1000L, 9223372036854775L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMillis(long j2) {
            return j2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toSeconds(long j2) {
            return j2 / 1000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMinutes(long j2) {
            return j2 / 60000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toHours(long j2) {
            return j2 / 3600000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toDays(long j2) {
            return j2 / 86400000;
        }

        @Override // java.util.concurrent.TimeUnit
        public long convert(long j2, TimeUnit timeUnit) {
            return timeUnit.toMillis(j2);
        }

        @Override // java.util.concurrent.TimeUnit
        int excessNanos(long j2, long j3) {
            return 0;
        }
    },
    SECONDS { // from class: java.util.concurrent.TimeUnit.4
        @Override // java.util.concurrent.TimeUnit
        public long toNanos(long j2) {
            return x(j2, 1000000000L, 9223372036L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMicros(long j2) {
            return x(j2, TimeUnit.C2, 9223372036854L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMillis(long j2) {
            return x(j2, 1000L, 9223372036854775L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toSeconds(long j2) {
            return j2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMinutes(long j2) {
            return j2 / 60;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toHours(long j2) {
            return j2 / 3600;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toDays(long j2) {
            return j2 / 86400;
        }

        @Override // java.util.concurrent.TimeUnit
        public long convert(long j2, TimeUnit timeUnit) {
            return timeUnit.toSeconds(j2);
        }

        @Override // java.util.concurrent.TimeUnit
        int excessNanos(long j2, long j3) {
            return 0;
        }
    },
    MINUTES { // from class: java.util.concurrent.TimeUnit.5
        @Override // java.util.concurrent.TimeUnit
        public long toNanos(long j2) {
            return x(j2, TimeUnit.C4, 153722867L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMicros(long j2) {
            return x(j2, 60000000L, 153722867280L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMillis(long j2) {
            return x(j2, 60000L, 153722867280912L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toSeconds(long j2) {
            return x(j2, 60L, 153722867280912930L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMinutes(long j2) {
            return j2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toHours(long j2) {
            return j2 / 60;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toDays(long j2) {
            return j2 / 1440;
        }

        @Override // java.util.concurrent.TimeUnit
        public long convert(long j2, TimeUnit timeUnit) {
            return timeUnit.toMinutes(j2);
        }

        @Override // java.util.concurrent.TimeUnit
        int excessNanos(long j2, long j3) {
            return 0;
        }
    },
    HOURS { // from class: java.util.concurrent.TimeUnit.6
        @Override // java.util.concurrent.TimeUnit
        public long toNanos(long j2) {
            return x(j2, TimeUnit.C5, 2562047L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMicros(long j2) {
            return x(j2, 3600000000L, 2562047788L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMillis(long j2) {
            return x(j2, 3600000L, 2562047788015L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toSeconds(long j2) {
            return x(j2, 3600L, 2562047788015215L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMinutes(long j2) {
            return x(j2, 60L, 153722867280912930L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toHours(long j2) {
            return j2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long toDays(long j2) {
            return j2 / 24;
        }

        @Override // java.util.concurrent.TimeUnit
        public long convert(long j2, TimeUnit timeUnit) {
            return timeUnit.toHours(j2);
        }

        @Override // java.util.concurrent.TimeUnit
        int excessNanos(long j2, long j3) {
            return 0;
        }
    },
    DAYS { // from class: java.util.concurrent.TimeUnit.7
        @Override // java.util.concurrent.TimeUnit
        public long toNanos(long j2) {
            return x(j2, TimeUnit.C6, 106751L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMicros(long j2) {
            return x(j2, 86400000000L, 106751991L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMillis(long j2) {
            return x(j2, 86400000L, 106751991167L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toSeconds(long j2) {
            return x(j2, 86400L, 106751991167300L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toMinutes(long j2) {
            return x(j2, 1440L, 6405119470038038L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toHours(long j2) {
            return x(j2, 24L, 384307168202282325L);
        }

        @Override // java.util.concurrent.TimeUnit
        public long toDays(long j2) {
            return j2;
        }

        @Override // java.util.concurrent.TimeUnit
        public long convert(long j2, TimeUnit timeUnit) {
            return timeUnit.toDays(j2);
        }

        @Override // java.util.concurrent.TimeUnit
        int excessNanos(long j2, long j3) {
            return 0;
        }
    };

    static final long C0 = 1;
    static final long C1 = 1000;
    static final long C2 = 1000000;
    static final long C3 = 1000000000;
    static final long C4 = 60000000000L;
    static final long C5 = 3600000000000L;
    static final long C6 = 86400000000000L;
    static final long MAX = Long.MAX_VALUE;

    abstract int excessNanos(long j2, long j3);

    static long x(long j2, long j3, long j4) {
        if (j2 > j4) {
            return Long.MAX_VALUE;
        }
        if (j2 < (-j4)) {
            return Long.MIN_VALUE;
        }
        return j2 * j3;
    }

    public long convert(long j2, TimeUnit timeUnit) {
        throw new AbstractMethodError();
    }

    public long toNanos(long j2) {
        throw new AbstractMethodError();
    }

    public long toMicros(long j2) {
        throw new AbstractMethodError();
    }

    public long toMillis(long j2) {
        throw new AbstractMethodError();
    }

    public long toSeconds(long j2) {
        throw new AbstractMethodError();
    }

    public long toMinutes(long j2) {
        throw new AbstractMethodError();
    }

    public long toHours(long j2) {
        throw new AbstractMethodError();
    }

    public long toDays(long j2) {
        throw new AbstractMethodError();
    }

    public void timedWait(Object obj, long j2) throws InterruptedException {
        if (j2 > 0) {
            long millis = toMillis(j2);
            obj.wait(millis, excessNanos(j2, millis));
        }
    }

    public void timedJoin(Thread thread, long j2) throws InterruptedException {
        if (j2 > 0) {
            long millis = toMillis(j2);
            thread.join(millis, excessNanos(j2, millis));
        }
    }

    public void sleep(long j2) throws InterruptedException {
        if (j2 > 0) {
            long millis = toMillis(j2);
            Thread.sleep(millis, excessNanos(j2, millis));
        }
    }
}
