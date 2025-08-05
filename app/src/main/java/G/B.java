package G;

import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:G/B.class */
public class B implements dh, Serializable {

    /* renamed from: a, reason: collision with root package name */
    double f294a = Double.NaN;

    public B() {
    }

    public B(double d2) {
        a(d2);
    }

    public void a(double d2) {
        this.f294a = d2;
    }

    @Override // G.dh
    public double a() {
        return this.f294a;
    }

    public String toString() {
        return Double.toString(this.f294a);
    }

    @Override // G.dh
    public double a(int i2) {
        return this.f294a;
    }
}
