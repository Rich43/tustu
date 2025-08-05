package k;

import ax.S;
import ax.ab;
import ax.ac;

/* renamed from: k.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:k/f.class */
public class C1758f extends ac {

    /* renamed from: e, reason: collision with root package name */
    private ab f12900e;

    /* renamed from: a, reason: collision with root package name */
    double f12901a = Double.NaN;

    /* renamed from: b, reason: collision with root package name */
    double f12902b = Double.NaN;

    /* renamed from: c, reason: collision with root package name */
    int f12903c = 0;

    /* renamed from: d, reason: collision with root package name */
    double f12904d = 0.0d;

    public C1758f(ab abVar) {
        this.f12900e = abVar;
    }

    public double a(S s2) {
        double dB = this.f12900e.b(s2);
        if (Double.isNaN(this.f12901a)) {
            this.f12902b = dB;
            this.f12903c = 1;
            this.f12901a = dB;
        } else if (dB != this.f12902b) {
            this.f12904d = (dB - this.f12902b) / this.f12903c;
            this.f12901a = dB;
            this.f12902b = dB;
            this.f12903c = 1;
        } else {
            this.f12901a = dB + (this.f12903c * this.f12904d);
            this.f12903c++;
        }
        return this.f12901a;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }
}
