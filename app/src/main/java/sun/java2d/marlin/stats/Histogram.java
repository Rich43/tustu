package sun.java2d.marlin.stats;

/* loaded from: rt.jar:sun/java2d/marlin/stats/Histogram.class */
public final class Histogram extends StatLong {
    static final int BUCKET = 2;
    static final int MAX = 20;
    static final int LAST = 19;
    static final int[] STEPS = new int[20];
    private final StatLong[] stats;

    static {
        STEPS[0] = 0;
        STEPS[1] = 1;
        for (int i2 = 2; i2 < 20; i2++) {
            STEPS[i2] = STEPS[i2 - 1] * 2;
        }
    }

    static int bucket(int i2) {
        for (int i3 = 1; i3 < 20; i3++) {
            if (i2 < STEPS[i3]) {
                return i3 - 1;
            }
        }
        return 19;
    }

    public Histogram(String str) {
        super(str);
        this.stats = new StatLong[20];
        for (int i2 = 0; i2 < 20; i2++) {
            StatLong[] statLongArr = this.stats;
            int i3 = i2;
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(STEPS[i2]);
            objArr[1] = i2 + 1 < 20 ? Integer.valueOf(STEPS[i2 + 1]) : "~";
            statLongArr[i3] = new StatLong(String.format("%5s .. %5s", objArr));
        }
    }

    @Override // sun.java2d.marlin.stats.StatLong
    public void reset() {
        super.reset();
        for (int i2 = 0; i2 < 20; i2++) {
            this.stats[i2].reset();
        }
    }

    @Override // sun.java2d.marlin.stats.StatLong
    public void add(int i2) {
        super.add(i2);
        this.stats[bucket(i2)].add(i2);
    }

    @Override // sun.java2d.marlin.stats.StatLong
    public void add(long j2) {
        add((int) j2);
    }

    @Override // sun.java2d.marlin.stats.StatLong
    public String toString() {
        StringBuilder sb = new StringBuilder(2048);
        super.toString(sb).append(" { ");
        for (int i2 = 0; i2 < 20; i2++) {
            if (this.stats[i2].count != 0) {
                sb.append("\n        ").append(this.stats[i2].toString());
            }
        }
        return sb.append(" }").toString();
    }
}
