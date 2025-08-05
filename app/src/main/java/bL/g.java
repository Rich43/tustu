package bL;

import bH.C;

/* loaded from: TunerStudioMS.jar:bL/g.class */
public class g {

    /* renamed from: a, reason: collision with root package name */
    h[] f7142a = null;

    public g(int i2, double d2) {
        a(i2, d2);
    }

    public void a(int i2, double d2) {
        this.f7142a = new h[i2];
        for (int i3 = 0; i3 < this.f7142a.length; i3++) {
            this.f7142a[i3] = new h(this);
            this.f7142a[i3].a(d2);
        }
    }

    public void a(double d2, long j2) {
        h hVar = this.f7142a[this.f7142a.length - 1];
        for (int length = this.f7142a.length - 1; length > 0; length--) {
            this.f7142a[length] = this.f7142a[length - 1];
        }
        this.f7142a[0] = hVar;
        this.f7142a[0].a(d2);
        this.f7142a[0].a(j2);
    }

    public double a(long j2, int i2) {
        h hVarA = a(j2);
        if (Math.abs(j2 - hVarA.b()) > i2) {
            return Double.NaN;
        }
        return hVarA.a();
    }

    private h a(long j2) {
        long jB = Long.MAX_VALUE;
        for (int i2 = 0; i2 < this.f7142a.length; i2++) {
            if (this.f7142a[i2].b() < j2) {
                return Math.abs(this.f7142a[i2].b() - j2) > Math.abs(jB - j2) ? this.f7142a[i2 - 1] : this.f7142a[i2];
            }
            jB = this.f7142a[i2].b();
        }
        C.c("timestamp is older than we have returning the oldest double I have. timestamp=" + j2);
        return this.f7142a[this.f7142a.length - 1];
    }

    public int a() {
        return this.f7142a.length;
    }
}
