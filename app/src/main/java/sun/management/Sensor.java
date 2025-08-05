package sun.management;

import java.lang.management.MemoryUsage;

/* loaded from: rt.jar:sun/management/Sensor.class */
public abstract class Sensor {
    private String name;
    private long count = 0;
    private boolean on = false;
    private Object lock = new Object();

    abstract void triggerAction();

    abstract void triggerAction(MemoryUsage memoryUsage);

    abstract void clearAction();

    public Sensor(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public long getCount() {
        long j2;
        synchronized (this.lock) {
            j2 = this.count;
        }
        return j2;
    }

    public boolean isOn() {
        boolean z2;
        synchronized (this.lock) {
            z2 = this.on;
        }
        return z2;
    }

    public void trigger() {
        synchronized (this.lock) {
            this.on = true;
            this.count++;
        }
        triggerAction();
    }

    public void trigger(int i2) {
        synchronized (this.lock) {
            this.on = true;
            this.count += i2;
        }
        triggerAction();
    }

    public void trigger(int i2, MemoryUsage memoryUsage) {
        synchronized (this.lock) {
            this.on = true;
            this.count += i2;
        }
        triggerAction(memoryUsage);
    }

    public void clear() {
        synchronized (this.lock) {
            this.on = false;
        }
        clearAction();
    }

    public void clear(int i2) {
        synchronized (this.lock) {
            this.on = false;
            this.count += i2;
        }
        clearAction();
    }

    public String toString() {
        return "Sensor - " + getName() + (isOn() ? " on " : " off ") + " count = " + getCount();
    }
}
