package bt;

import G.C0075bh;
import G.C0113cs;
import G.InterfaceC0109co;
import bF.C0973d;

/* renamed from: bt.F, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/F.class */
public class C1278F implements InterfaceC0109co, bN {

    /* renamed from: a, reason: collision with root package name */
    double f8662a = Double.NaN;

    /* renamed from: b, reason: collision with root package name */
    C1290R f8663b;

    /* renamed from: c, reason: collision with root package name */
    C0973d f8664c;

    /* renamed from: d, reason: collision with root package name */
    G.R f8665d;

    /* renamed from: e, reason: collision with root package name */
    C0075bh f8666e;

    public C1278F(G.R r2, C0075bh c0075bh, C1290R c1290r) {
        this.f8665d = r2;
        this.f8666e = c0075bh;
        this.f8663b = c1290r;
        this.f8664c = c1290r.f8698b;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (this.f8663b.isEnabled()) {
            this.f8664c.a(d2);
            this.f8663b.c();
        }
    }

    @Override // bt.bN
    public void a() {
        if (this.f8666e.d() != null) {
            try {
                C0113cs.a().a(this.f8665d.c(), this.f8666e.d(), this);
            } catch (V.a e2) {
                bH.C.a("Unable to subscribe x or y axis for hightlights.", e2, this);
            }
        }
    }

    @Override // bt.bN
    public void b() {
        C0113cs.a().a(this);
    }
}
