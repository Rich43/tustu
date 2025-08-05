package sun.java2d.marlin.stats;

/* loaded from: rt.jar:sun/java2d/marlin/stats/Monitor.class */
public final class Monitor extends StatLong {
    private static final long INVALID = -1;
    private long start;

    public Monitor(String str) {
        super(str);
        this.start = -1L;
    }

    public void start() {
        this.start = System.nanoTime();
    }

    public void stop() {
        long jNanoTime = System.nanoTime() - this.start;
        if (this.start != -1 && jNanoTime > 0) {
            add(jNanoTime);
        }
        this.start = -1L;
    }
}
