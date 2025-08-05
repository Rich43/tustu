package J;

import G.F;
import G.InterfaceC0139v;
import G.bX;
import G.cM;
import bH.C;
import bH.C0995c;
import java.io.Serializable;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:J/d.class */
public class d implements cM, Serializable {

    /* renamed from: a, reason: collision with root package name */
    bX f1421a;

    /* renamed from: b, reason: collision with root package name */
    int f1422b = -1;

    /* renamed from: c, reason: collision with root package name */
    e f1423c = null;

    public d() {
        this.f1421a = null;
        this.f1421a = new bX();
        this.f1421a.a(PdfOps.F_TOKEN);
        this.f1421a.e("I");
        this.f1421a.d("f%cId");
    }

    @Override // G.cM
    public void a() {
        this.f1422b = -1;
        this.f1423c = null;
    }

    @Override // G.cM
    public int a(InterfaceC0139v interfaceC0139v) {
        int[] iArrA = interfaceC0139v.a(this.f1421a.a().a((int[]) null), 250, 3);
        if (iArrA == null) {
            return 0;
        }
        try {
            int i2 = Integer.parseInt(new String(C0995c.a(iArrA)));
            if (i2 > 3) {
                C.b("Unsupported Protocol Version Detected " + i2 + ", using Highest known 003.");
            }
            return i2;
        } catch (NumberFormatException e2) {
            return 0;
        }
    }

    @Override // G.cM
    public boolean a(InterfaceC0139v interfaceC0139v, F f2, int i2) {
        e eVar;
        if (this.f1422b < 0) {
            this.f1422b = interfaceC0139v.a(this.f1421a.e().b(), 10, 1)[0];
        }
        if (this.f1423c == null) {
            this.f1421a.d().a((byte) this.f1422b);
            int[] iArrA = interfaceC0139v.a(this.f1421a.d().a((int[]) null), 10, 5);
            this.f1423c = new e(this);
            this.f1423c.a(iArrA);
        }
        C.d("Local on CAN ID:" + this.f1422b + ", " + this.f1423c.toString());
        f2.w(this.f1422b);
        if (this.f1422b != i2) {
            this.f1421a.d().a(i2);
            if (f2.D() instanceof f) {
                ((f) f2.D()).b(false);
            }
            try {
                try {
                    int[] iArrA2 = interfaceC0139v.a(this.f1421a.d().a((int[]) null), 10, 5);
                    eVar = new e(this);
                    eVar.a(iArrA2);
                    C.d("Target on CAN ID:" + i2 + ", " + eVar.toString());
                    if (f2.D() instanceof f) {
                        ((f) f2.D()).b(true);
                    }
                } catch (Exception e2) {
                    try {
                        eVar = new e(this);
                        eVar.f1425b = f2.ax();
                        eVar.f1424a = f2.G(0);
                        C.d("Failed to get Target BlockingFactors, using ini values on CAN ID:" + i2 + ", " + eVar.toString());
                    } catch (Exception e3) {
                        eVar = new e(this);
                        eVar.f1425b = 0;
                        eVar.f1424a = 64;
                        C.d("Failed to get Target BlockingFactors, using default values on CAN ID:" + i2 + ", " + eVar.toString());
                    }
                    if (f2.D() instanceof f) {
                        ((f) f2.D()).b(true);
                    }
                }
            } catch (Throwable th) {
                if (f2.D() instanceof f) {
                    ((f) f2.D()).b(true);
                }
                throw th;
            }
        } else {
            eVar = this.f1423c;
            C.d("Target device is local, using localBlockingFactors");
        }
        f2.I(Math.min(eVar.f1424a, this.f1423c.f1424a));
        f2.H(Math.min(eVar.f1425b, this.f1423c.f1425b));
        return true;
    }
}
