package L;

import java.util.List;

/* loaded from: TunerStudioMS.jar:L/H.class */
public class H extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    List f1539a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1540b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1541c;

    /* renamed from: d, reason: collision with root package name */
    double f1542d = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    long f1543e = 0;

    protected H(List list) {
        this.f1541c = null;
        this.f1539a = list;
        this.f1540b = (ax.ab) list.get(0);
        if (list.size() > 1) {
            this.f1541c = (ax.ab) list.get(1);
        }
    }

    public double a(ax.S s2) {
        double dB = this.f1540b.b(s2);
        if (this.f1541c != null && c(s2) - this.f1543e > this.f1541c.b(s2)) {
            this.f1542d = Double.NaN;
        }
        if (Double.isNaN(this.f1542d) || dB > this.f1542d) {
            this.f1542d = dB;
            this.f1543e = c(s2);
        }
        return this.f1542d;
    }

    private long c(ax.S s2) {
        return (long) C0157n.a().f1677b.b(s2);
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("maxValue( ");
        for (int i2 = 0; i2 < this.f1539a.size(); i2++) {
            sb.append(((ax.ab) this.f1539a.get(i2)).toString());
            if (i2 + 1 < this.f1539a.size()) {
                sb.append(", ");
            }
        }
        sb.append(" )");
        return sb.toString();
    }
}
