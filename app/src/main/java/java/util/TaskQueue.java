package java.util;

/* compiled from: Timer.java */
/* loaded from: rt.jar:java/util/TaskQueue.class */
class TaskQueue {
    private TimerTask[] queue = new TimerTask[128];
    private int size = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    TaskQueue() {
    }

    static {
        $assertionsDisabled = !TaskQueue.class.desiredAssertionStatus();
    }

    int size() {
        return this.size;
    }

    void add(TimerTask timerTask) {
        if (this.size + 1 == this.queue.length) {
            this.queue = (TimerTask[]) Arrays.copyOf(this.queue, 2 * this.queue.length);
        }
        TimerTask[] timerTaskArr = this.queue;
        int i2 = this.size + 1;
        this.size = i2;
        timerTaskArr[i2] = timerTask;
        fixUp(this.size);
    }

    TimerTask getMin() {
        return this.queue[1];
    }

    TimerTask get(int i2) {
        return this.queue[i2];
    }

    void removeMin() {
        this.queue[1] = this.queue[this.size];
        TimerTask[] timerTaskArr = this.queue;
        int i2 = this.size;
        this.size = i2 - 1;
        timerTaskArr[i2] = null;
        fixDown(1);
    }

    void quickRemove(int i2) {
        if (!$assertionsDisabled && i2 > this.size) {
            throw new AssertionError();
        }
        this.queue[i2] = this.queue[this.size];
        TimerTask[] timerTaskArr = this.queue;
        int i3 = this.size;
        this.size = i3 - 1;
        timerTaskArr[i3] = null;
    }

    void rescheduleMin(long j2) {
        this.queue[1].nextExecutionTime = j2;
        fixDown(1);
    }

    boolean isEmpty() {
        return this.size == 0;
    }

    void clear() {
        for (int i2 = 1; i2 <= this.size; i2++) {
            this.queue[i2] = null;
        }
        this.size = 0;
    }

    private void fixUp(int i2) {
        while (i2 > 1) {
            int i3 = i2 >> 1;
            if (this.queue[i3].nextExecutionTime > this.queue[i2].nextExecutionTime) {
                TimerTask timerTask = this.queue[i3];
                this.queue[i3] = this.queue[i2];
                this.queue[i2] = timerTask;
                i2 = i3;
            } else {
                return;
            }
        }
    }

    private void fixDown(int i2) {
        while (true) {
            int i3 = i2 << 1;
            int i4 = i3;
            if (i3 <= this.size && i4 > 0) {
                if (i4 < this.size && this.queue[i4].nextExecutionTime > this.queue[i4 + 1].nextExecutionTime) {
                    i4++;
                }
                if (this.queue[i2].nextExecutionTime > this.queue[i4].nextExecutionTime) {
                    TimerTask timerTask = this.queue[i4];
                    this.queue[i4] = this.queue[i2];
                    this.queue[i2] = timerTask;
                    i2 = i4;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void heapify() {
        for (int i2 = this.size / 2; i2 >= 1; i2--) {
            fixDown(i2);
        }
    }
}
