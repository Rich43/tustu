package java.util;

import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: rt.jar:java/util/Timer.class */
public class Timer {
    private final TaskQueue queue;
    private final TimerThread thread;
    private final Object threadReaper;
    private static final AtomicInteger nextSerialNumber = new AtomicInteger(0);

    private static int serialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    public Timer() {
        this("Timer-" + serialNumber());
    }

    public Timer(boolean z2) {
        this("Timer-" + serialNumber(), z2);
    }

    public Timer(String str) {
        this.queue = new TaskQueue();
        this.thread = new TimerThread(this.queue);
        this.threadReaper = new Object() { // from class: java.util.Timer.1
            protected void finalize() throws Throwable {
                synchronized (Timer.this.queue) {
                    Timer.this.thread.newTasksMayBeScheduled = false;
                    Timer.this.queue.notify();
                }
            }
        };
        this.thread.setName(str);
        this.thread.start();
    }

    public Timer(String str, boolean z2) {
        this.queue = new TaskQueue();
        this.thread = new TimerThread(this.queue);
        this.threadReaper = new Object() { // from class: java.util.Timer.1
            protected void finalize() throws Throwable {
                synchronized (Timer.this.queue) {
                    Timer.this.thread.newTasksMayBeScheduled = false;
                    Timer.this.queue.notify();
                }
            }
        };
        this.thread.setName(str);
        this.thread.setDaemon(z2);
        this.thread.start();
    }

    public void schedule(TimerTask timerTask, long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative delay.");
        }
        sched(timerTask, System.currentTimeMillis() + j2, 0L);
    }

    public void schedule(TimerTask timerTask, Date date) {
        sched(timerTask, date.getTime(), 0L);
    }

    public void schedule(TimerTask timerTask, long j2, long j3) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative delay.");
        }
        if (j3 <= 0) {
            throw new IllegalArgumentException("Non-positive period.");
        }
        sched(timerTask, System.currentTimeMillis() + j2, -j3);
    }

    public void schedule(TimerTask timerTask, Date date, long j2) {
        if (j2 <= 0) {
            throw new IllegalArgumentException("Non-positive period.");
        }
        sched(timerTask, date.getTime(), -j2);
    }

    public void scheduleAtFixedRate(TimerTask timerTask, long j2, long j3) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative delay.");
        }
        if (j3 <= 0) {
            throw new IllegalArgumentException("Non-positive period.");
        }
        sched(timerTask, System.currentTimeMillis() + j2, j3);
    }

    public void scheduleAtFixedRate(TimerTask timerTask, Date date, long j2) {
        if (j2 <= 0) {
            throw new IllegalArgumentException("Non-positive period.");
        }
        sched(timerTask, date.getTime(), j2);
    }

    private void sched(TimerTask timerTask, long j2, long j3) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Illegal execution time.");
        }
        if (Math.abs(j3) > 4611686018427387903L) {
            j3 >>= 1;
        }
        synchronized (this.queue) {
            if (!this.thread.newTasksMayBeScheduled) {
                throw new IllegalStateException("Timer already cancelled.");
            }
            synchronized (timerTask.lock) {
                if (timerTask.state != 0) {
                    throw new IllegalStateException("Task already scheduled or cancelled");
                }
                timerTask.nextExecutionTime = j2;
                timerTask.period = j3;
                timerTask.state = 1;
            }
            this.queue.add(timerTask);
            if (this.queue.getMin() == timerTask) {
                this.queue.notify();
            }
        }
    }

    public void cancel() {
        synchronized (this.queue) {
            this.thread.newTasksMayBeScheduled = false;
            this.queue.clear();
            this.queue.notify();
        }
    }

    public int purge() {
        int i2 = 0;
        synchronized (this.queue) {
            for (int size = this.queue.size(); size > 0; size--) {
                if (this.queue.get(size).state == 3) {
                    this.queue.quickRemove(size);
                    i2++;
                }
            }
            if (i2 != 0) {
                this.queue.heapify();
            }
        }
        return i2;
    }
}
