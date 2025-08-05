package java.lang;

/* loaded from: rt.jar:java/lang/Object.class */
public class Object {
    private static native void registerNatives();

    public final native Class<?> getClass();

    public native int hashCode();

    protected native Object clone() throws CloneNotSupportedException;

    public final native void notify();

    public final native void notifyAll();

    public final native void wait(long j2) throws InterruptedException;

    static {
        registerNatives();
    }

    public boolean equals(Object obj) {
        return this == obj;
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public final void wait(long j2, int i2) throws InterruptedException {
        if (j2 < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (i2 < 0 || i2 > 999999) {
            throw new IllegalArgumentException("nanosecond timeout value out of range");
        }
        if (i2 > 0) {
            j2++;
        }
        wait(j2);
    }

    public final void wait() throws InterruptedException {
        wait(0L);
    }

    protected void finalize() throws Throwable {
    }
}
