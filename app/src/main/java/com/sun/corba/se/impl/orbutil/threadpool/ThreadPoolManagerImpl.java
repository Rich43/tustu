package com.sun.corba.se.impl.orbutil.threadpool;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolChooser;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/threadpool/ThreadPoolManagerImpl.class */
public class ThreadPoolManagerImpl implements ThreadPoolManager {
    private static final ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_TRANSPORT);
    private static AtomicInteger tgCount = new AtomicInteger();
    private ThreadGroup threadGroup = getThreadGroup();
    private ThreadPool threadPool = new ThreadPoolImpl(this.threadGroup, ORBConstants.THREADPOOL_DEFAULT_NAME);

    private ThreadGroup getThreadGroup() {
        ThreadGroup threadGroup;
        try {
            threadGroup = (ThreadGroup) AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() { // from class: com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolManagerImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ThreadGroup run2() {
                    ThreadGroup threadGroup2 = Thread.currentThread().getThreadGroup();
                    ThreadGroup parent = threadGroup2;
                    while (parent != null) {
                        try {
                            threadGroup2 = parent;
                            parent = threadGroup2.getParent();
                        } catch (SecurityException e2) {
                        }
                    }
                    return new ThreadGroup(threadGroup2, "ORB ThreadGroup " + ThreadPoolManagerImpl.tgCount.getAndIncrement());
                }
            });
        } catch (SecurityException e2) {
            threadGroup = Thread.currentThread().getThreadGroup();
        }
        return threadGroup;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.threadPool.close();
        } catch (IOException e2) {
            wrapper.threadPoolCloseError();
        }
        try {
            boolean zIsDestroyed = this.threadGroup.isDestroyed();
            int iActiveCount = this.threadGroup.activeCount();
            int iActiveGroupCount = this.threadGroup.activeGroupCount();
            if (zIsDestroyed) {
                wrapper.threadGroupIsDestroyed(this.threadGroup);
            } else {
                if (iActiveCount > 0) {
                    wrapper.threadGroupHasActiveThreadsInClose(this.threadGroup, Integer.valueOf(iActiveCount));
                }
                if (iActiveGroupCount > 0) {
                    wrapper.threadGroupHasSubGroupsInClose(this.threadGroup, Integer.valueOf(iActiveGroupCount));
                }
                this.threadGroup.destroy();
            }
        } catch (IllegalThreadStateException e3) {
            wrapper.threadGroupDestroyFailed(e3, this.threadGroup);
        }
        this.threadGroup = null;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public ThreadPool getThreadPool(String str) throws NoSuchThreadPoolException {
        return this.threadPool;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public ThreadPool getThreadPool(int i2) throws NoSuchThreadPoolException {
        return this.threadPool;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public int getThreadPoolNumericId(String str) {
        return 0;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public String getThreadPoolStringId(int i2) {
        return "";
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public ThreadPool getDefaultThreadPool() {
        return this.threadPool;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public ThreadPoolChooser getThreadPoolChooser(String str) {
        return null;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public ThreadPoolChooser getThreadPoolChooser(int i2) {
        return null;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public void setThreadPoolChooser(String str, ThreadPoolChooser threadPoolChooser) {
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
    public int getThreadPoolChooserNumericId(String str) {
        return 0;
    }
}
