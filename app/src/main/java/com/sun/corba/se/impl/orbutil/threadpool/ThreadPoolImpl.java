package com.sun.corba.se.impl.orbutil.threadpool;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
import com.sun.corba.se.spi.monitoring.MonitoredObject;
import com.sun.corba.se.spi.monitoring.MonitoringConstants;
import com.sun.corba.se.spi.monitoring.MonitoringFactories;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
import java.io.Closeable;
import java.io.IOException;
import java.lang.Thread;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/threadpool/ThreadPoolImpl.class */
public class ThreadPoolImpl implements ThreadPool {
    private static AtomicInteger threadCounter = new AtomicInteger(0);
    private static final ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_TRANSPORT);
    private WorkQueue workQueue;
    private int availableWorkerThreads;
    private int currentThreadCount;
    private int minWorkerThreads;
    private int maxWorkerThreads;
    private long inactivityTimeout;
    private boolean boundedThreadPool;
    private AtomicLong processedCount;
    private AtomicLong totalTimeTaken;
    private String name;
    private MonitoredObject threadpoolMonitoredObject;
    private ThreadGroup threadGroup;
    Object workersLock;
    List<WorkerThread> workers;

    public ThreadPoolImpl(ThreadGroup threadGroup, String str) {
        this.availableWorkerThreads = 0;
        this.currentThreadCount = 0;
        this.minWorkerThreads = 0;
        this.maxWorkerThreads = 0;
        this.boundedThreadPool = false;
        this.processedCount = new AtomicLong(1L);
        this.totalTimeTaken = new AtomicLong(0L);
        this.workersLock = new Object();
        this.workers = new ArrayList();
        this.inactivityTimeout = 120000L;
        this.maxWorkerThreads = Integer.MAX_VALUE;
        this.workQueue = new WorkQueueImpl(this);
        this.threadGroup = threadGroup;
        this.name = str;
        initializeMonitoring();
    }

    public ThreadPoolImpl(String str) {
        this(Thread.currentThread().getThreadGroup(), str);
    }

    public ThreadPoolImpl(int i2, int i3, long j2, String str) {
        this.availableWorkerThreads = 0;
        this.currentThreadCount = 0;
        this.minWorkerThreads = 0;
        this.maxWorkerThreads = 0;
        this.boundedThreadPool = false;
        this.processedCount = new AtomicLong(1L);
        this.totalTimeTaken = new AtomicLong(0L);
        this.workersLock = new Object();
        this.workers = new ArrayList();
        this.minWorkerThreads = i2;
        this.maxWorkerThreads = i3;
        this.inactivityTimeout = j2;
        this.boundedThreadPool = true;
        this.workQueue = new WorkQueueImpl(this);
        this.name = str;
        for (int i4 = 0; i4 < this.minWorkerThreads; i4++) {
            createWorkerThread();
        }
        initializeMonitoring();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        ArrayList<WorkerThread> arrayList;
        synchronized (this.workersLock) {
            arrayList = new ArrayList(this.workers);
        }
        for (WorkerThread workerThread : arrayList) {
            workerThread.close();
            while (workerThread.getState() != Thread.State.TERMINATED) {
                try {
                    workerThread.join();
                } catch (InterruptedException e2) {
                    wrapper.interruptedJoinCallWhileClosingThreadPool(e2, workerThread, this);
                }
            }
        }
        this.threadGroup = null;
    }

    private void initializeMonitoring() {
        MonitoredObject rootMonitoredObject = MonitoringFactories.getMonitoringManagerFactory().createMonitoringManager("orb", null).getRootMonitoredObject();
        MonitoredObject child = rootMonitoredObject.getChild(MonitoringConstants.THREADPOOL_MONITORING_ROOT);
        if (child == null) {
            child = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(MonitoringConstants.THREADPOOL_MONITORING_ROOT, MonitoringConstants.THREADPOOL_MONITORING_ROOT_DESCRIPTION);
            rootMonitoredObject.addChild(child);
        }
        this.threadpoolMonitoredObject = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(this.name, MonitoringConstants.THREADPOOL_MONITORING_DESCRIPTION);
        child.addChild(this.threadpoolMonitoredObject);
        this.threadpoolMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.THREADPOOL_CURRENT_NUMBER_OF_THREADS, MonitoringConstants.THREADPOOL_CURRENT_NUMBER_OF_THREADS_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl.1
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(ThreadPoolImpl.this.currentNumberOfThreads());
            }
        });
        this.threadpoolMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.THREADPOOL_NUMBER_OF_AVAILABLE_THREADS, MonitoringConstants.THREADPOOL_CURRENT_NUMBER_OF_THREADS_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl.2
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(ThreadPoolImpl.this.numberOfAvailableThreads());
            }
        });
        this.threadpoolMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.THREADPOOL_NUMBER_OF_BUSY_THREADS, MonitoringConstants.THREADPOOL_NUMBER_OF_BUSY_THREADS_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl.3
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(ThreadPoolImpl.this.numberOfBusyThreads());
            }
        });
        this.threadpoolMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.THREADPOOL_AVERAGE_WORK_COMPLETION_TIME, MonitoringConstants.THREADPOOL_AVERAGE_WORK_COMPLETION_TIME_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl.4
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(ThreadPoolImpl.this.averageWorkCompletionTime());
            }
        });
        this.threadpoolMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.THREADPOOL_CURRENT_PROCESSED_COUNT, MonitoringConstants.THREADPOOL_CURRENT_PROCESSED_COUNT_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl.5
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(ThreadPoolImpl.this.currentProcessedCount());
            }
        });
        this.threadpoolMonitoredObject.addChild(((WorkQueueImpl) this.workQueue).getMonitoredObject());
    }

    MonitoredObject getMonitoredObject() {
        return this.threadpoolMonitoredObject;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public WorkQueue getAnyWorkQueue() {
        return this.workQueue;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public WorkQueue getWorkQueue(int i2) throws NoSuchWorkQueueException {
        if (i2 != 0) {
            throw new NoSuchWorkQueueException();
        }
        return this.workQueue;
    }

    void notifyForAvailableWork(WorkQueue workQueue) {
        synchronized (workQueue) {
            if (this.availableWorkerThreads < workQueue.workItemsInQueue()) {
                createWorkerThread();
            } else {
                workQueue.notify();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Thread createWorkerThreadHelper(String str) {
        WorkerThread workerThread = new WorkerThread(this.threadGroup, str);
        synchronized (this.workersLock) {
            this.workers.add(workerThread);
        }
        workerThread.setDaemon(true);
        wrapper.workerThreadCreated(workerThread, workerThread.getContextClassLoader());
        workerThread.start();
        return null;
    }

    /* JADX WARN: Finally extract failed */
    void createWorkerThread() {
        final String name = getName();
        synchronized (this.workQueue) {
            try {
                try {
                    if (System.getSecurityManager() == null) {
                        createWorkerThreadHelper(name);
                    } else {
                        AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl.6
                            @Override // java.security.PrivilegedAction
                            /* renamed from: run */
                            public Object run2() {
                                return ThreadPoolImpl.this.createWorkerThreadHelper(name);
                            }
                        });
                    }
                    incrementCurrentNumberOfThreads();
                } catch (Throwable th) {
                    decrementCurrentNumberOfThreads();
                    wrapper.workerThreadCreationFailure(th);
                    incrementCurrentNumberOfThreads();
                }
            } catch (Throwable th2) {
                incrementCurrentNumberOfThreads();
                throw th2;
            }
        }
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public int minimumNumberOfThreads() {
        return this.minWorkerThreads;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public int maximumNumberOfThreads() {
        return this.maxWorkerThreads;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public long idleTimeoutForThreads() {
        return this.inactivityTimeout;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public int currentNumberOfThreads() {
        int i2;
        synchronized (this.workQueue) {
            i2 = this.currentThreadCount;
        }
        return i2;
    }

    void decrementCurrentNumberOfThreads() {
        synchronized (this.workQueue) {
            this.currentThreadCount--;
        }
    }

    void incrementCurrentNumberOfThreads() {
        synchronized (this.workQueue) {
            this.currentThreadCount++;
        }
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public int numberOfAvailableThreads() {
        int i2;
        synchronized (this.workQueue) {
            i2 = this.availableWorkerThreads;
        }
        return i2;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public int numberOfBusyThreads() {
        int i2;
        synchronized (this.workQueue) {
            i2 = this.currentThreadCount - this.availableWorkerThreads;
        }
        return i2;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public long averageWorkCompletionTime() {
        long j2;
        synchronized (this.workQueue) {
            j2 = this.totalTimeTaken.get() / this.processedCount.get();
        }
        return j2;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public long currentProcessedCount() {
        long j2;
        synchronized (this.workQueue) {
            j2 = this.processedCount.get();
        }
        return j2;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public String getName() {
        return this.name;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
    public int numberOfWorkQueues() {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int getUniqueThreadId() {
        return threadCounter.incrementAndGet();
    }

    void decrementNumberOfAvailableThreads() {
        synchronized (this.workQueue) {
            this.availableWorkerThreads--;
        }
    }

    void incrementNumberOfAvailableThreads() {
        synchronized (this.workQueue) {
            this.availableWorkerThreads++;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/threadpool/ThreadPoolImpl$WorkerThread.class */
    private class WorkerThread extends Thread implements Closeable {
        private Work currentWork;
        private int threadId;
        private volatile boolean closeCalled;
        private String threadPoolName;
        private StringBuffer workerThreadName;

        WorkerThread(ThreadGroup threadGroup, String str) {
            super(threadGroup, "Idle");
            this.threadId = 0;
            this.closeCalled = false;
            this.workerThreadName = new StringBuffer();
            this.threadId = ThreadPoolImpl.getUniqueThreadId();
            this.threadPoolName = str;
            setName(composeWorkerThreadName(str, "Idle"));
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() {
            this.closeCalled = true;
            interrupt();
        }

        private void resetClassLoader() {
        }

        private void performWork() {
            long jCurrentTimeMillis = System.currentTimeMillis();
            try {
                this.currentWork.doWork();
            } catch (Throwable th) {
                ThreadPoolImpl.wrapper.workerThreadDoWorkThrowable(this, th);
            }
            ThreadPoolImpl.this.totalTimeTaken.addAndGet(System.currentTimeMillis() - jCurrentTimeMillis);
            ThreadPoolImpl.this.processedCount.incrementAndGet();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.closeCalled) {
                try {
                    try {
                        try {
                            this.currentWork = ((WorkQueueImpl) ThreadPoolImpl.this.workQueue).requestWork(ThreadPoolImpl.this.inactivityTimeout);
                            if (this.currentWork != null) {
                                performWork();
                                this.currentWork = null;
                                resetClassLoader();
                            }
                        } catch (InterruptedException e2) {
                            ThreadPoolImpl.wrapper.workQueueThreadInterrupted(e2, getName(), Boolean.valueOf(this.closeCalled));
                        } catch (Throwable th) {
                            ThreadPoolImpl.wrapper.workerThreadThrowableFromRequestWork(this, th, ThreadPoolImpl.this.workQueue.getName());
                        }
                    } catch (Throwable th2) {
                        ThreadPoolImpl.wrapper.workerThreadCaughtUnexpectedThrowable(this, th2);
                        synchronized (ThreadPoolImpl.this.workersLock) {
                            ThreadPoolImpl.this.workers.remove(this);
                            return;
                        }
                    }
                } catch (Throwable th3) {
                    synchronized (ThreadPoolImpl.this.workersLock) {
                        ThreadPoolImpl.this.workers.remove(this);
                        throw th3;
                    }
                }
            }
            synchronized (ThreadPoolImpl.this.workersLock) {
                ThreadPoolImpl.this.workers.remove(this);
            }
        }

        private String composeWorkerThreadName(String str, String str2) {
            this.workerThreadName.setLength(0);
            this.workerThreadName.append("p: ").append(str);
            this.workerThreadName.append("; w: ").append(str2);
            return this.workerThreadName.toString();
        }
    }
}
