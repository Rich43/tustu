package bE;

import java.awt.geom.Point2D;

/* loaded from: TunerStudioMS.jar:bE/b.class */
public class b extends Point2D.Double implements q {

    /* renamed from: a, reason: collision with root package name */
    double f6711a;

    public b(double d2, double d3, double d4) {
        super(d2, d3);
        this.f6711a = Double.NaN;
        this.f6711a = d4;
    }

    @Override // bE.q
    public double a() {
        return this.f6711a;
    }

    @Override // bE.q
    public boolean b() {
        return false;
    }
}
