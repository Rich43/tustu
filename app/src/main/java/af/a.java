package aF;

import G.C0129l;
import G.R;
import G.aD;
import G.aG;
import G.aH;
import G.bS;
import V.g;
import bH.C;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import z.C1901e;

/* loaded from: TunerStudioMS.jar:aF/a.class */
public class a implements aD, aG {

    /* renamed from: a, reason: collision with root package name */
    R f2377a;

    /* renamed from: b, reason: collision with root package name */
    aH f2378b;

    /* renamed from: c, reason: collision with root package name */
    int f2379c = -1;

    /* renamed from: d, reason: collision with root package name */
    int f2380d = 0;

    /* renamed from: e, reason: collision with root package name */
    int f2381e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f2382f = 100;

    public a(R r2) {
        this.f2377a = null;
        this.f2378b = null;
        this.f2377a = r2;
        this.f2378b = r2.g("secl");
        if (this.f2378b == null) {
            C.d("No secl OutputChannel, och sync monitor disabled.");
        }
    }

    @Override // G.aD
    public boolean a(String str, byte[] bArr) {
        if (!str.equals(this.f2377a.c())) {
            return true;
        }
        try {
            if (this.f2378b == null) {
                return true;
            }
            int iB = ((int) this.f2378b.b(bArr)) % 256;
            if (this.f2379c == -1) {
                this.f2379c = a() - iB;
                return true;
            }
            int iA = a(this.f2379c);
            int iAbs = Math.abs(iB - iA);
            if (iAbs <= 2 || iAbs >= 254) {
                this.f2380d = 0;
                this.f2381e++;
                int iAv = this.f2377a.O().av();
                if (this.f2381e % this.f2382f == 0 && this.f2377a.O().F(iAv) < this.f2377a.O().t()) {
                    int iZ = this.f2377a.O().z() > 0 ? this.f2377a.O().z() - 1 : this.f2377a.O().z();
                    this.f2377a.O().s(iZ);
                    int iT = this.f2377a.O().t() - 2;
                    this.f2377a.O().q(iT);
                    C.c(this.f2382f + " good och reads, decreased delay to " + iT + ", set extra wait between to " + iZ);
                }
                if (iB >= 255 || iA >= 255) {
                    return true;
                }
                this.f2379c = a() - iB;
                return true;
            }
            C.c("expected secl =" + iA + ", secl = " + iB + ", secDelta = " + iAbs);
            this.f2380d++;
            this.f2381e = 0;
            if (this.f2380d > 1) {
                this.f2379c = -1;
            }
            if (iB > 1 && this.f2380d == 1) {
                if (this.f2377a.C() instanceof C1901e) {
                    ((C1901e) this.f2377a.C()).l();
                }
                int iZ2 = this.f2377a.O().z() + 1;
                this.f2377a.O().s(iZ2);
                int iT2 = this.f2377a.O().t() + 2;
                this.f2377a.O().q(iT2);
                C.c("Detected Bad och read on " + str + ", increased delay to " + iT2 + ", increase extra wait between to " + iZ2 + ", secl = " + iB);
            } else if (this.f2380d > 5) {
                C.b("Och read corrupt with failure count of " + this.f2380d + ", restarting comms.");
                this.f2377a.C().c();
                if (this.f2377a.C() instanceof C1901e) {
                    ((C1901e) this.f2377a.C()).l();
                }
                try {
                    this.f2377a.C().d();
                } catch (C0129l e2) {
                    C.a("Failed to go online");
                }
            } else {
                C.c("Reset " + str + ", secl = " + iB + ", expectedSecl=" + iA + ", secDelta=" + iAbs);
                this.f2379c = a() - iB;
            }
            Toolkit.getDefaultToolkit().beep();
            return false;
        } catch (g e3) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return true;
        }
    }

    private int a() {
        return ((int) (System.currentTimeMillis() / 1000)) % 256;
    }

    private int a(int i2) {
        return ((a() + 255) - i2) % 256;
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        if (!str.equals(this.f2377a.c())) {
            return true;
        }
        this.f2379c = -1;
        this.f2377a.O().s(0);
        return true;
    }

    @Override // G.aG
    public void a(String str) {
    }
}
