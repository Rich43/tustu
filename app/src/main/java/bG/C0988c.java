package bG;

import G.R;
import G.aM;
import G.aN;
import G.aR;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bG.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bG/c.class */
public class C0988c implements aN, E, G, p {

    /* renamed from: a, reason: collision with root package name */
    R f6921a;

    /* renamed from: b, reason: collision with root package name */
    m f6922b;

    /* renamed from: c, reason: collision with root package name */
    P.a f6923c;

    /* renamed from: g, reason: collision with root package name */
    private C0987b f6924g;

    /* renamed from: d, reason: collision with root package name */
    aM f6925d;

    /* renamed from: e, reason: collision with root package name */
    aM f6926e;

    /* renamed from: f, reason: collision with root package name */
    aM f6927f;

    public C0988c(R r2, P.a aVar, m mVar) throws V.g {
        this.f6925d = null;
        this.f6926e = null;
        this.f6927f = null;
        this.f6921a = r2;
        this.f6922b = mVar;
        this.f6923c = aVar;
        this.f6925d = r2.c(aVar.b());
        if (this.f6925d == null) {
            throw new V.g("BitArrayParameter not found.");
        }
        try {
            aR.a().a(r2.c(), this.f6925d.aJ(), this);
        } catch (V.a e2) {
            Logger.getLogger(C0988c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (aVar.d() != null) {
            this.f6926e = r2.c(aVar.d());
            if (this.f6926e != null) {
                try {
                    aR.a().a(r2.c(), this.f6926e.aJ(), this);
                } catch (V.a e3) {
                    Logger.getLogger(C0988c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
        if (aVar.c() != null) {
            this.f6927f = r2.c(aVar.c());
            if (this.f6927f != null) {
                try {
                    aR.a().a(r2.c(), this.f6927f.aJ(), this);
                } catch (V.a e4) {
                    Logger.getLogger(C0988c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                }
            }
        }
        if (mVar.e() instanceof C0987b) {
            this.f6924g = (C0987b) mVar.e();
        } else {
            this.f6924g = new C0987b();
        }
        e();
        mVar.a(this.f6924g);
        mVar.a(this);
        this.f6924g.a(this);
    }

    private void e() {
        try {
            byte[] bArr = new byte[this.f6925d.h(this.f6921a.h()).length];
            for (int i2 = 0; i2 < bArr.length; i2++) {
                bArr[i2] = (byte) r0[i2];
            }
            this.f6924g.a(bArr);
        } catch (Exception e2) {
            Logger.getLogger(C0988c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (this.f6926e != null) {
            try {
                this.f6922b.a(this.f6926e.j(this.f6921a.h()));
            } catch (V.g e3) {
                Logger.getLogger(C0988c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        if (this.f6927f != null) {
            try {
                this.f6922b.a((int) this.f6927f.j(this.f6921a.h()));
            } catch (V.g e4) {
                Logger.getLogger(C0988c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            }
        }
    }

    public void a() {
        byte[] bArrC = this.f6924g.c();
        double[] dArr = new double[bArrC.length];
        for (int i2 = 0; i2 < bArrC.length; i2++) {
            dArr[i2] = bArrC[i2];
        }
        try {
            this.f6925d.a(this.f6921a.h(), dArr);
        } catch (V.g e2) {
            e2.printStackTrace();
        } catch (V.j e3) {
            e3.printStackTrace();
        }
    }

    public void b() {
        e();
        this.f6922b.repaint();
    }

    @Override // bG.G
    public void c() {
        aR.a().a(this);
        this.f6922b.b(this);
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (str2.equals(this.f6925d.aJ())) {
            b();
        }
    }

    @Override // bG.p
    public void a(double d2) {
        if (this.f6926e != null) {
            try {
                this.f6926e.a(this.f6921a.h(), d2);
            } catch (V.g e2) {
                Logger.getLogger(C0988c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (V.j e3) {
                if (e3.a() == 2) {
                    this.f6922b.a(this.f6926e.q());
                    bH.C.d("Trigger Offset Below Min, setting to Min");
                } else if (e3.a() == 1) {
                    this.f6922b.a(this.f6926e.r());
                    bH.C.d("Trigger Offset Exceeds Max, setting to Max");
                }
            }
        }
    }

    @Override // bG.E
    public void d() {
        a();
    }
}
