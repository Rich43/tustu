package L;

import java.util.List;

/* loaded from: TunerStudioMS.jar:L/Y.class */
public class Y extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1609a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1610b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1611c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1612d;

    /* renamed from: e, reason: collision with root package name */
    double f1613e = 0.0d;

    /* renamed from: f, reason: collision with root package name */
    double f1614f = 0.0d;

    /* renamed from: g, reason: collision with root package name */
    double f1615g = -1.0d;

    /* renamed from: h, reason: collision with root package name */
    double f1616h = 0.0d;

    /* renamed from: i, reason: collision with root package name */
    double f1617i = 0.0d;

    /* renamed from: j, reason: collision with root package name */
    double f1618j = 0.0d;

    /* renamed from: k, reason: collision with root package name */
    boolean f1619k = true;

    /* renamed from: l, reason: collision with root package name */
    int f1620l = 0;

    protected Y(List list) {
        this.f1612d = null;
        this.f1609a = (ax.ab) list.get(0);
        this.f1610b = (ax.ab) list.get(1);
        if (list.size() > 2) {
            this.f1611c = (ax.ab) list.get(2);
        }
        if (list.size() > 3) {
            this.f1612d = (ax.ab) list.get(3);
        }
    }

    public synchronized double a(ax.S s2) {
        if (this.f1615g < 0.0d) {
            this.f1615g = c(s2);
        }
        double dB = this.f1610b.b(s2);
        if (dB <= 0.0d) {
            this.f1613e = -1.0d;
            this.f1615g = c(s2);
            this.f1620l = 0;
            return this.f1618j;
        }
        if (this.f1613e <= 0.0d) {
            this.f1613e = c(s2);
            this.f1614f = dB;
            this.f1618j = 0.0d;
        }
        if (this.f1618j > 0.0d) {
            this.f1615g = c(s2);
            this.f1620l = 0;
            return this.f1618j;
        }
        double dC = c(s2);
        if (this.f1620l == 1) {
            this.f1613e -= (c(s2) - this.f1615g) / 2.0d;
        }
        this.f1620l++;
        double dB2 = this.f1609a.b(s2);
        if (dB2 > dB) {
            this.f1615g = dC;
            if (this.f1612d == null) {
                this.f1616h = dB;
                return this.f1615g - this.f1613e;
            }
            this.f1617i = this.f1612d.b(s2);
            return this.f1617i;
        }
        if (this.f1612d == null) {
            this.f1618j = (this.f1615g - this.f1613e) + (((dB2 - this.f1616h) / (dB - this.f1616h)) * (dC - this.f1615g));
            if (this.f1618j < 0.0d) {
                this.f1618j = 0.0d;
            }
        } else {
            this.f1618j = this.f1617i + (((dB2 - this.f1616h) / (dB - this.f1616h)) * (this.f1612d.b(s2) - this.f1617i));
        }
        this.f1619k = false;
        return this.f1618j;
    }

    private double c(ax.S s2) {
        return this.f1611c != null ? this.f1611c.b(s2) : System.currentTimeMillis() / 1000.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "timeToExceed( " + this.f1609a.toString() + ", " + this.f1610b.toString() + ")";
    }
}
