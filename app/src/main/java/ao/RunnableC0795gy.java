package ao;

import W.C0184j;

/* renamed from: ao.gy, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gy.class */
class RunnableC0795gy implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f5997a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ fX f5998b;

    RunnableC0795gy(fX fXVar, int i2) {
        this.f5998b = fXVar;
        this.f5997a = i2;
    }

    @Override // java.lang.Runnable
    public void run() throws NumberFormatException {
        C0184j c0184jD = this.f5998b.b().d();
        C0184j c0184jE = this.f5998b.b().e();
        if (c0184jD == null || c0184jE == null || !this.f5998b.g()) {
            return;
        }
        if (this.f5997a - this.f5998b.f5791ae == 1) {
            this.f5998b.f5736l.h().a(Float.valueOf(c0184jE.d(this.f5997a)).toString(), Float.valueOf(c0184jD.d(this.f5997a)).toString());
        } else {
            if (this.f5997a - this.f5998b.f5736l.h().J() < 0) {
            }
            int iMin = Math.min(this.f5998b.f5736l.h().J() + 1, this.f5997a + 1);
            float[] fArr = new float[iMin];
            float[] fArr2 = new float[iMin];
            for (int i2 = 0; i2 < iMin; i2++) {
                fArr[i2] = c0184jD.d(this.f5997a - i2);
                fArr2[i2] = c0184jE.d(this.f5997a - i2);
            }
            this.f5998b.f5736l.h().a(fArr2, fArr);
        }
        this.f5998b.f5791ae = this.f5997a;
        this.f5998b.f5783W.a();
    }
}
