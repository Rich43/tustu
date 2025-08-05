package z;

import G.C0129l;
import G.C0130m;
import G.F;
import bH.C;
import gnu.io.PortInUseException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: z.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:z/b.class */
class C1898b extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private boolean f14078b = false;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1897a f14079a;

    C1898b(C1897a c1897a) {
        this.f14079a = c1897a;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        b();
    }

    private void b() {
        try {
            try {
                try {
                    F fE = this.f14079a.f14070b.e();
                    this.f14079a.f14071c = this.f14079a.f14070b.e().r() + "";
                    this.f14079a.f14072d = this.f14079a.f14070b.e().av();
                    fE.j(this.f14079a.d());
                    fE.E(1000);
                    this.f14079a.f14070b.h();
                    this.f14079a.f14070b.a("BurstLogger", this.f14079a.e());
                    a(true);
                    int i2 = 10;
                    int i3 = this.f14079a.f() ? 40 : 100;
                    while (this.f14078b) {
                        byte[] bArr = new byte[1];
                        try {
                            byte[] bArrA = this.f14079a.f() ? this.f14079a.f14070b.a(new byte[]{65}, i2, i3, fE.n(), (C0130m) null) : this.f14079a.f14070b.a(fE.w().d(), i2, i3, fE.n(), (C0130m) null);
                            if (bArrA.length == fE.n()) {
                                this.f14079a.a(bArrA);
                            } else if (bArrA.length % fE.n() == 0) {
                                byte[] bArr2 = new byte[fE.n()];
                                C.c("Doing a loop: Expected:" + fE.n() + ", recieved:" + bArrA.length);
                                for (int i4 = 0; i4 * bArr2.length < bArrA.length; i4++) {
                                    System.arraycopy(bArrA, i4 * bArr2.length, bArr2, 0, bArr2.length);
                                    this.f14079a.a(bArr2);
                                }
                            } else {
                                C.b("Unexpected data packet length. Expected:" + fE.n() + ", recieved:" + bArrA.length);
                            }
                        } catch (V.b e2) {
                            i2++;
                            this.f14079a.a((byte[]) null);
                        } catch (V.d e3) {
                            i2++;
                            this.f14079a.a((byte[]) null);
                        } catch (IOException e4) {
                            Logger.getLogger(C1897a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                            C.b("IOException during Burst logging.");
                        }
                    }
                    this.f14079a.f14070b.e().j(this.f14079a.f14071c);
                    this.f14079a.f14070b.e().E(this.f14079a.f14072d);
                    a(false);
                    this.f14079a.f14070b.h();
                } catch (PortInUseException e5) {
                    C.a("COMM Port in use by " + e5.currentOwner, e5, null);
                    this.f14079a.f14070b.e().j(this.f14079a.f14071c);
                    this.f14079a.f14070b.e().E(this.f14079a.f14072d);
                    a(false);
                    this.f14079a.f14070b.h();
                }
            } catch (C0129l e6) {
                C.a("COMM Error occurred during burst logging.", e6, null);
                this.f14079a.f14070b.e().j(this.f14079a.f14071c);
                this.f14079a.f14070b.e().E(this.f14079a.f14072d);
                a(false);
                this.f14079a.f14070b.h();
            }
        } catch (Throwable th) {
            this.f14079a.f14070b.e().j(this.f14079a.f14071c);
            this.f14079a.f14070b.e().E(this.f14079a.f14072d);
            a(false);
            this.f14079a.f14070b.h();
            throw th;
        }
    }

    public boolean a() {
        return this.f14078b;
    }

    public void a(boolean z2) {
        this.f14078b = z2;
    }
}
