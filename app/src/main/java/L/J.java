package L;

import java.util.List;

/* loaded from: TunerStudioMS.jar:L/J.class */
public class J extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    List f1545a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1546b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1547c;

    /* renamed from: d, reason: collision with root package name */
    double f1548d = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    long f1549e = 0;

    protected J(List list) {
        this.f1547c = null;
        this.f1545a = list;
        this.f1546b = (ax.ab) list.get(0);
        if (list.size() > 1) {
            this.f1547c = (ax.ab) list.get(1);
        }
    }

    public double a(ax.S s2) {
        double dB = this.f1546b.b(s2);
        if (this.f1547c != null && c(s2) - this.f1549e > this.f1547c.b(s2)) {
            this.f1548d = Double.NaN;
        }
        if (Double.isNaN(this.f1548d) || dB < this.f1548d) {
            this.f1548d = dB;
            this.f1549e = c(s2);
        }
        return this.f1548d;
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
        sb.append("minValue( ");
        for (int i2 = 0; i2 < this.f1545a.size(); i2++) {
            sb.append(((ax.ab) this.f1545a.get(i2)).toString());
            if (i2 + 1 < this.f1545a.size()) {
                sb.append(", ");
            }
        }
        sb.append(" )");
        return sb.toString();
    }
}
