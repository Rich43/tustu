package sun.java2d.marlin.stats;

/* loaded from: rt.jar:sun/java2d/marlin/stats/StatLong.class */
public class StatLong {
    public final String name;
    public long count = 0;
    public long sum = 0;
    public long min = 2147483647L;
    public long max = -2147483648L;

    public StatLong(String str) {
        this.name = str;
    }

    public void reset() {
        this.count = 0L;
        this.sum = 0L;
        this.min = 2147483647L;
        this.max = -2147483648L;
    }

    public void add(int i2) {
        this.count++;
        this.sum += i2;
        if (i2 < this.min) {
            this.min = i2;
        }
        if (i2 > this.max) {
            this.max = i2;
        }
    }

    public void add(long j2) {
        this.count++;
        this.sum += j2;
        if (j2 < this.min) {
            this.min = j2;
        }
        if (j2 > this.max) {
            this.max = j2;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        toString(sb);
        return sb.toString();
    }

    public final StringBuilder toString(StringBuilder sb) {
        sb.append(this.name).append('[').append(this.count);
        sb.append("] sum: ").append(this.sum).append(" avg: ");
        sb.append(trimTo3Digits(this.sum / this.count));
        sb.append(" [").append(this.min).append(" | ").append(this.max).append("]");
        return sb;
    }

    public static double trimTo3Digits(double d2) {
        return ((long) (1000.0d * d2)) / 1000.0d;
    }
}
