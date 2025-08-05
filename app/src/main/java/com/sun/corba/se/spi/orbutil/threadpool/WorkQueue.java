package com.sun.corba.se.spi.orbutil.threadpool;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/threadpool/WorkQueue.class */
public interface WorkQueue {
    void addWork(Work work);

    String getName();

    long totalWorkItemsAdded();

    int workItemsInQueue();

    long averageTimeInQueue();

    void setThreadPool(ThreadPool threadPool);

    ThreadPool getThreadPool();
}
