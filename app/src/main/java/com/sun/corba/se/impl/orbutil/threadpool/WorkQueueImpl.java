package com.sun.corba.se.impl.orbutil.threadpool;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
import com.sun.corba.se.spi.monitoring.MonitoredObject;
import com.sun.corba.se.spi.monitoring.MonitoringConstants;
import com.sun.corba.se.spi.monitoring.MonitoringFactories;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
import java.util.LinkedList;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/threadpool/WorkQueueImpl.class */
public class WorkQueueImpl implements WorkQueue {
    private ThreadPool workerThreadPool;
    private LinkedList theWorkQueue;
    private long workItemsAdded;
    private long workItemsDequeued;
    private long totalTimeInQueue;
    private String name;
    private MonitoredObject workqueueMonitoredObject;

    public WorkQueueImpl() {
        this.theWorkQueue = new LinkedList();
        this.workItemsAdded = 0L;
        this.workItemsDequeued = 1L;
        this.totalTimeInQueue = 0L;
        this.name = ORBConstants.WORKQUEUE_DEFAULT_NAME;
        initializeMonitoring();
    }

    public WorkQueueImpl(ThreadPool threadPool) {
        this(threadPool, ORBConstants.WORKQUEUE_DEFAULT_NAME);
    }

    public WorkQueueImpl(ThreadPool threadPool, String str) {
        this.theWorkQueue = new LinkedList();
        this.workItemsAdded = 0L;
        this.workItemsDequeued = 1L;
        this.totalTimeInQueue = 0L;
        this.workerThreadPool = threadPool;
        this.name = str;
        initializeMonitoring();
    }

    private void initializeMonitoring() {
        this.workqueueMonitoredObject = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(this.name, MonitoringConstants.WORKQUEUE_MONITORING_DESCRIPTION);
        this.workqueueMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.WORKQUEUE_TOTAL_WORK_ITEMS_ADDED, MonitoringConstants.WORKQUEUE_TOTAL_WORK_ITEMS_ADDED_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.WorkQueueImpl.1
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(WorkQueueImpl.this.totalWorkItemsAdded());
            }
        });
        this.workqueueMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.WORKQUEUE_WORK_ITEMS_IN_QUEUE, MonitoringConstants.WORKQUEUE_WORK_ITEMS_IN_QUEUE_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.WorkQueueImpl.2
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(WorkQueueImpl.this.workItemsInQueue());
            }
        });
        this.workqueueMonitoredObject.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.WORKQUEUE_AVERAGE_TIME_IN_QUEUE, MonitoringConstants.WORKQUEUE_AVERAGE_TIME_IN_QUEUE_DESCRIPTION) { // from class: com.sun.corba.se.impl.orbutil.threadpool.WorkQueueImpl.3
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(WorkQueueImpl.this.averageTimeInQueue());
            }
        });
    }

    MonitoredObject getMonitoredObject() {
        return this.workqueueMonitoredObject;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
    public synchronized void addWork(Work work) {
        this.workItemsAdded++;
        work.setEnqueueTime(System.currentTimeMillis());
        this.theWorkQueue.addLast(work);
        ((ThreadPoolImpl) this.workerThreadPool).notifyForAvailableWork(this);
    }

    synchronized Work requestWork(long j2) throws TimeoutException, InterruptedException {
        ((ThreadPoolImpl) this.workerThreadPool).incrementNumberOfAvailableThreads();
        if (this.theWorkQueue.size() != 0) {
            Work work = (Work) this.theWorkQueue.removeFirst();
            this.totalTimeInQueue += System.currentTimeMillis() - work.getEnqueueTime();
            this.workItemsDequeued++;
            ((ThreadPoolImpl) this.workerThreadPool).decrementNumberOfAvailableThreads();
            return work;
        }
        try {
            long jCurrentTimeMillis = j2;
            long jCurrentTimeMillis2 = System.currentTimeMillis() + j2;
            do {
                wait(jCurrentTimeMillis);
                if (this.theWorkQueue.size() != 0) {
                    Work work2 = (Work) this.theWorkQueue.removeFirst();
                    this.totalTimeInQueue += System.currentTimeMillis() - work2.getEnqueueTime();
                    this.workItemsDequeued++;
                    ((ThreadPoolImpl) this.workerThreadPool).decrementNumberOfAvailableThreads();
                    return work2;
                }
                jCurrentTimeMillis = jCurrentTimeMillis2 - System.currentTimeMillis();
            } while (jCurrentTimeMillis > 0);
            ((ThreadPoolImpl) this.workerThreadPool).decrementNumberOfAvailableThreads();
            throw new TimeoutException();
        } catch (InterruptedException e2) {
            ((ThreadPoolImpl) this.workerThreadPool).decrementNumberOfAvailableThreads();
            throw e2;
        }
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
    public void setThreadPool(ThreadPool threadPool) {
        this.workerThreadPool = threadPool;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
    public ThreadPool getThreadPool() {
        return this.workerThreadPool;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
    public long totalWorkItemsAdded() {
        return this.workItemsAdded;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
    public int workItemsInQueue() {
        return this.theWorkQueue.size();
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
    public synchronized long averageTimeInQueue() {
        return this.totalTimeInQueue / this.workItemsDequeued;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
    public String getName() {
        return this.name;
    }
}
