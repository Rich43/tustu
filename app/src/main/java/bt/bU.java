package bt;

import G.C0072be;
import G.C0126i;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bt/bU.class */
class bU implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    int f8965a = bO.f8932i;

    /* renamed from: b, reason: collision with root package name */
    C1701s f8966b;

    /* renamed from: c, reason: collision with root package name */
    G.aM f8967c;

    /* renamed from: d, reason: collision with root package name */
    G.aM f8968d;

    /* renamed from: e, reason: collision with root package name */
    G.aM f8969e;

    /* renamed from: f, reason: collision with root package name */
    G.R f8970f;

    /* renamed from: g, reason: collision with root package name */
    int f8971g;

    /* renamed from: h, reason: collision with root package name */
    int f8972h;

    /* renamed from: i, reason: collision with root package name */
    int f8973i;

    /* renamed from: j, reason: collision with root package name */
    int f8974j;

    /* renamed from: k, reason: collision with root package name */
    final /* synthetic */ bO f8975k;

    public bU(bO bOVar, G.R r2, String str, C1701s c1701s) throws V.g {
        this.f8975k = bOVar;
        this.f8966b = c1701s;
        this.f8970f = r2;
        C0072be c0072be = (C0072be) r2.e().c(str);
        if (c0072be == null) {
            throw new V.g(str + " not found in current Configuration, can not create Model.");
        }
        this.f8969e = r2.c(c0072be.c());
        this.f8968d = r2.c(c0072be.b());
        this.f8967c = r2.c(c0072be.a());
        a();
    }

    private void a() {
        this.f8971g = this.f8967c.a();
        this.f8972h = this.f8968d.a();
        this.f8973i = this.f8969e.m();
        this.f8974j = this.f8969e.a();
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (this.f8975k.f8929n) {
            if (this.f8971g == this.f8967c.a() && this.f8972h == this.f8968d.a() && this.f8973i == this.f8969e.m() && this.f8974j == this.f8969e.a()) {
                return;
            }
            b();
        }
    }

    private void b() {
        try {
            C1701s c1701sB = C1677fh.b(this.f8966b);
            G.aL.b(this.f8970f, this.f8969e.d());
            int iG = e() ? this.f8969e.g() + (this.f8969e.e() * this.f8974j * this.f8973i) + (this.f8967c.e() * this.f8973i) + (this.f8968d.e() * this.f8974j) : this.f8969e.g() + (this.f8969e.e() * this.f8974j * this.f8973i);
            this.f8970f.h().a(this.f8969e.d(), iG, G.aJ.a(this.f8970f, this.f8969e.d(), iG));
            a();
            this.f8966b.a(this.f8972h, this.f8971g);
            double[][] dArrI = this.f8969e.i(this.f8970f.p());
            if (1 != 0) {
                C1677fh.a(c1701sB, this.f8966b);
            } else {
                this.f8966b.a(dArrI);
                this.f8966b.d(this.f8975k.a(this.f8968d.i(this.f8970f.p()), this.f8968d.u()));
                this.f8966b.c(this.f8975k.a(this.f8967c.i(this.f8970f.p()), this.f8967c.u()));
            }
            this.f8966b.f(this.f8972h, this.f8971g);
            bH.C.c("TableModelManager Resize table. Rows: " + this.f8972h + ", Cols: " + this.f8971g);
        } catch (V.g e2) {
            bH.C.a("Error resizing table model:");
            Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        G.aR.a().a(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            C0126i.a(this.f8970f.c(), this.f8967c.c().d(), this);
            C0126i.a(this.f8970f.c(), this.f8968d.c().d(), this);
        } catch (V.a e2) {
            Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private boolean e() {
        if (this.f8965a == bO.f8932i) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.f8967c);
            arrayList.add(this.f8968d);
            arrayList.add(this.f8969e);
            if (this.f8967c.n() && this.f8968d.n() && this.f8967c.d() == this.f8969e.d() && this.f8968d.d() == this.f8969e.d() && G.aJ.a(this.f8970f, arrayList)) {
                this.f8965a = bO.f8930g + bO.f8931h;
            } else {
                this.f8965a = 0;
            }
        }
        return this.f8965a == bO.f8930g + bO.f8931h;
    }
}
