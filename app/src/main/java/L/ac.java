package L;

import java.util.List;

/* loaded from: TunerStudioMS.jar:L/ac.class */
public class ac extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1636a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1637b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1638c;

    /* renamed from: d, reason: collision with root package name */
    double f1639d = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    boolean f1640e = false;

    /* renamed from: f, reason: collision with root package name */
    List f1641f;

    protected ac(List list) {
        this.f1638c = null;
        this.f1636a = (ax.ab) list.get(0);
        this.f1637b = (ax.ab) list.get(1);
        if (list.size() > 2) {
            this.f1638c = (ax.ab) list.get(2);
        }
        this.f1641f = list;
    }

    public synchronized double a(ax.S s2) {
        if (!this.f1640e && this.f1638c != null && Double.isNaN(this.f1639d)) {
            this.f1639d = this.f1638c.b(s2);
        }
        if (this.f1636a.b(s2) != 0.0d) {
            this.f1639d = this.f1637b.b(s2);
        }
        return this.f1639d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("updateValueWhen( ");
        for (int i2 = 0; i2 < this.f1641f.size(); i2++) {
            sb.append(((ax.ab) this.f1641f.get(i2)).toString());
            if (i2 + 1 < this.f1641f.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
