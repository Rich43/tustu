package javax.swing;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/TimerQueue.class */
class TimerQueue implements Runnable {
    private static final Object sharedInstanceKey;
    private static final Object expiredTimersKey;
    private volatile boolean running;
    private static final Object classLock;
    private static final long NANO_ORIGIN;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final DelayQueue<DelayedTimer> queue = new DelayQueue<>();
    private final Lock runningLock = new ReentrantLock();

    static {
        $assertionsDisabled = !TimerQueue.class.desiredAssertionStatus();
        sharedInstanceKey = new StringBuffer("TimerQueue.sharedInstanceKey");
        expiredTimersKey = new StringBuffer("TimerQueue.expiredTimersKey");
        classLock = new Object();
        NANO_ORIGIN = System.nanoTime();
    }

    public TimerQueue() {
        startIfNeeded();
    }

    public static TimerQueue sharedInstance() {
        TimerQueue timerQueue;
        synchronized (classLock) {
            TimerQueue timerQueue2 = (TimerQueue) SwingUtilities.appContextGet(sharedInstanceKey);
            if (timerQueue2 == null) {
                timerQueue2 = new TimerQueue();
                SwingUtilities.appContextPut(sharedInstanceKey, timerQueue2);
            }
            timerQueue = timerQueue2;
        }
        return timerQueue;
    }

    void startIfNeeded() {
        if (!this.running) {
            this.runningLock.lock();
            if (this.running) {
                return;
            }
            try {
                final ThreadGroup threadGroup = AppContext.getAppContext().getThreadGroup();
                AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javax.swing.TimerQueue.1
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() {
                        Thread thread = new Thread(threadGroup, TimerQueue.this, "TimerQueue");
                        thread.setDaemon(true);
                        thread.setPriority(5);
                        thread.start();
                        return null;
                    }
                });
                this.running = true;
            } finally {
                this.runningLock.unlock();
            }
        }
    }

    void addTimer(Timer timer, long j2) {
        timer.getLock().lock();
        try {
            if (!containsTimer(timer)) {
                addTimer(new DelayedTimer(timer, TimeUnit.MILLISECONDS.toNanos(j2) + now()));
            }
        } finally {
            timer.getLock().unlock();
        }
    }

    private void addTimer(DelayedTimer delayedTimer) {
        if (!$assertionsDisabled && (delayedTimer == null || containsTimer(delayedTimer.getTimer()))) {
            throw new AssertionError();
        }
        Timer timer = delayedTimer.getTimer();
        timer.getLock().lock();
        try {
            timer.delayedTimer = delayedTimer;
            this.queue.add((DelayQueue<DelayedTimer>) delayedTimer);
        } finally {
            timer.getLock().unlock();
        }
    }

    void removeTimer(Timer timer) {
        timer.getLock().lock();
        try {
            if (timer.delayedTimer != null) {
                this.queue.remove(timer.delayedTimer);
                timer.delayedTimer = null;
            }
        } finally {
            timer.getLock().unlock();
        }
    }

    boolean containsTimer(Timer timer) {
        timer.getLock().lock();
        try {
            return timer.delayedTimer != null;
        } finally {
            timer.getLock().unlock();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        this.runningLock.lock();
        while (this.running) {
            try {
                try {
                    try {
                        DelayedTimer delayedTimer = (DelayedTimer) this.queue.take2();
                        Timer timer = delayedTimer.getTimer();
                        timer.getLock().lock();
                        try {
                            DelayedTimer delayedTimer2 = timer.delayedTimer;
                            if (delayedTimer2 == delayedTimer) {
                                timer.post();
                                timer.delayedTimer = null;
                                if (timer.isRepeats()) {
                                    delayedTimer2.setTime(now() + TimeUnit.MILLISECONDS.toNanos(timer.getDelay()));
                                    addTimer(delayedTimer2);
                                }
                            }
                            timer.getLock().newCondition().awaitNanos(1L);
                            timer.getLock().unlock();
                        } catch (SecurityException e2) {
                            timer.getLock().unlock();
                        } catch (Throwable th) {
                            timer.getLock().unlock();
                            throw th;
                        }
                    } catch (InterruptedException e3) {
                        if (AppContext.getAppContext().isDisposed()) {
                            break;
                        }
                    }
                } catch (ThreadDeath e4) {
                    Iterator<E> it = this.queue.iterator();
                    while (it.hasNext()) {
                        ((DelayedTimer) it.next()).getTimer().cancelEvent();
                    }
                    throw e4;
                }
            } finally {
                this.running = false;
                this.runningLock.unlock();
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimerQueue (");
        boolean z2 = true;
        Iterator<E> it = this.queue.iterator();
        while (it.hasNext()) {
            DelayedTimer delayedTimer = (DelayedTimer) it.next();
            if (!z2) {
                sb.append(", ");
            }
            sb.append(delayedTimer.getTimer().toString());
            z2 = false;
        }
        sb.append(")");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long now() {
        return System.nanoTime() - NANO_ORIGIN;
    }

    /* loaded from: rt.jar:javax/swing/TimerQueue$DelayedTimer.class */
    static class DelayedTimer implements Delayed {
        private static final AtomicLong sequencer = new AtomicLong(0);
        private final long sequenceNumber = sequencer.getAndIncrement();
        private volatile long time;
        private final Timer timer;

        DelayedTimer(Timer timer, long j2) {
            this.timer = timer;
            this.time = j2;
        }

        @Override // java.util.concurrent.Delayed
        public final long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.time - TimerQueue.now(), TimeUnit.NANOSECONDS);
        }

        final void setTime(long j2) {
            this.time = j2;
        }

        final Timer getTimer() {
            return this.timer;
        }

        @Override // java.lang.Comparable
        public int compareTo(Delayed delayed) {
            if (delayed == this) {
                return 0;
            }
            if (delayed instanceof DelayedTimer) {
                DelayedTimer delayedTimer = (DelayedTimer) delayed;
                long j2 = this.time - delayedTimer.time;
                if (j2 < 0) {
                    return -1;
                }
                if (j2 <= 0 && this.sequenceNumber < delayedTimer.sequenceNumber) {
                    return -1;
                }
                return 1;
            }
            long delay = getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS);
            if (delay == 0) {
                return 0;
            }
            return delay < 0 ? -1 : 1;
        }
    }
}
