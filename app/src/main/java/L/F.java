package L;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:L/F.class */
public class F extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1532a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1533b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1534c;

    /* renamed from: d, reason: collision with root package name */
    int f1535d;

    /* renamed from: e, reason: collision with root package name */
    List f1536e;

    /* renamed from: f, reason: collision with root package name */
    double f1537f;

    protected F(ax.ab abVar, ax.ab abVar2) {
        this.f1534c = null;
        this.f1535d = Integer.MIN_VALUE;
        this.f1536e = new ArrayList();
        this.f1537f = Double.NaN;
        this.f1532a = abVar2;
        this.f1533b = abVar;
        this.f1535d = 1;
    }

    protected F(ax.ab abVar, ax.ab abVar2, ax.ab abVar3) {
        this.f1534c = null;
        this.f1535d = Integer.MIN_VALUE;
        this.f1536e = new ArrayList();
        this.f1537f = Double.NaN;
        this.f1532a = abVar2;
        this.f1533b = abVar;
        this.f1534c = abVar3;
    }

    public synchronized double a(ax.S s2) {
        double dB = this.f1532a.b(s2);
        int iC = c(s2);
        if (Double.isNaN(this.f1537f) || this.f1537f != dB) {
            this.f1536e.add(0, Double.valueOf(this.f1533b.b(s2)));
            while (this.f1536e.size() > iC + 2) {
                this.f1536e.remove(this.f1536e.size() - 1);
            }
            this.f1537f = dB;
        }
        if (this.f1536e.size() > iC) {
            return ((Double) this.f1536e.get(iC)).doubleValue();
        }
        return Double.NaN;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    private int c(ax.S s2) {
        if (this.f1535d > Integer.MIN_VALUE) {
            return this.f1535d;
        }
        if (this.f1534c == null) {
            return 1;
        }
        this.f1535d = (int) this.f1534c.b(s2);
        return this.f1535d;
    }

    public String toString() {
        return this.f1534c != null ? "lastValue( " + this.f1533b.toString() + ", " + this.f1534c.toString() + " )" : "lastValue( " + this.f1533b.toString() + " )";
    }
}
