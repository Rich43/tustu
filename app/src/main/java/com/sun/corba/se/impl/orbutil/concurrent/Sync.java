package com.sun.corba.se.impl.orbutil.concurrent;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/concurrent/Sync.class */
public interface Sync {
    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60000;
    public static final long ONE_HOUR = 3600000;
    public static final long ONE_DAY = 86400000;
    public static final long ONE_WEEK = 604800000;
    public static final long ONE_YEAR = 31556952000L;
    public static final long ONE_CENTURY = 3155695200000L;

    void acquire() throws InterruptedException;

    boolean attempt(long j2) throws InterruptedException;

    void release();
}
