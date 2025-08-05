package com.sun.corba.se.spi.orbutil.threadpool;

import java.io.Closeable;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/threadpool/ThreadPool.class */
public interface ThreadPool extends Closeable {
    WorkQueue getAnyWorkQueue();

    WorkQueue getWorkQueue(int i2) throws NoSuchWorkQueueException;

    int numberOfWorkQueues();

    int minimumNumberOfThreads();

    int maximumNumberOfThreads();

    long idleTimeoutForThreads();

    int currentNumberOfThreads();

    int numberOfAvailableThreads();

    int numberOfBusyThreads();

    long currentProcessedCount();

    long averageWorkCompletionTime();

    String getName();
}
