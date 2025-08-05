package com.sun.corba.se.spi.orbutil.threadpool;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/threadpool/Work.class */
public interface Work {
    void doWork();

    void setEnqueueTime(long j2);

    long getEnqueueTime();

    String getName();
}
