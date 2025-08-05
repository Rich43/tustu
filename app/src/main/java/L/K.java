package L;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:L/K.class */
public class K extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1550a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1551b;

    /* renamed from: c, reason: collision with root package name */
    int f1552c = Integer.MIN_VALUE;

    /* renamed from: d, reason: collision with root package name */
    List f1553d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    double f1554e = Double.NaN;

    /* renamed from: f, reason: collision with root package name */
    int f1555f = 1;

    protected K(ax.ab abVar, ax.ab abVar2) {
        this.f1551b = null;
        this.f1550a = abVar;
        this.f1551b = abVar2;
    }

    public double a(ax.S s2) {
        int iC = c(s2);
        if (iC > this.f1555f) {
            this.f1555f = iC + 1;
        }
        double dB = this.f1550a.b(s2);
        if (Double.isNaN(this.f1554e) || this.f1554e != dB) {
            this.f1553d.add(0, Double.valueOf(dB));
            while (this.f1553d.size() > this.f1555f + 1) {
                this.f1553d.remove(this.f1553d.size() - 1);
            }
            this.f1554e = dB;
        }
        if (this.f1553d.size() > iC) {
            return ((Double) this.f1553d.get(iC)).doubleValue();
        }
        return Double.NaN;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    private int c(ax.S s2) {
        if (this.f1552c > Integer.MIN_VALUE) {
            return this.f1552c;
        }
        if (this.f1551b == null) {
            return 1;
        }
        this.f1552c = (int) this.f1551b.b(s2);
        return this.f1552c;
    }

    public String toString() {
        return this.f1551b != null ? "lastValue( " + this.f1550a.toString() + ", " + this.f1551b.toString() + " )" : "lastValue( " + this.f1550a.toString() + " )";
    }
}
