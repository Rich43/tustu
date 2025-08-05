package com.sun.corba.se.spi.orbutil.threadpool;

import java.io.Closeable;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/threadpool/ThreadPoolManager.class */
public interface ThreadPoolManager extends Closeable {
    ThreadPool getThreadPool(String str) throws NoSuchThreadPoolException;

    ThreadPool getThreadPool(int i2) throws NoSuchThreadPoolException;

    int getThreadPoolNumericId(String str);

    String getThreadPoolStringId(int i2);

    ThreadPool getDefaultThreadPool();

    ThreadPoolChooser getThreadPoolChooser(String str);

    ThreadPoolChooser getThreadPoolChooser(int i2);

    void setThreadPoolChooser(String str, ThreadPoolChooser threadPoolChooser);

    int getThreadPoolChooserNumericId(String str);
}
