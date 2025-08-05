package com.sun.corba.se.spi.orbutil.threadpool;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/threadpool/ThreadPoolChooser.class */
public interface ThreadPoolChooser {
    ThreadPool getThreadPool();

    ThreadPool getThreadPool(int i2);

    String[] getThreadPoolIds();
}
