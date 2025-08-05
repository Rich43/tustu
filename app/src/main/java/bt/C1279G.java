package bt;

import G.C0079bl;
import G.C0113cs;
import G.InterfaceC0109co;

/* renamed from: bt.G, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/G.class */
public class C1279G implements InterfaceC0109co, bN {

    /* renamed from: a, reason: collision with root package name */
    double f8667a = Double.NaN;

    /* renamed from: b, reason: collision with root package name */
    com.efiAnalytics.ui.bN f8668b;

    /* renamed from: c, reason: collision with root package name */
    G.R f8669c;

    /* renamed from: d, reason: collision with root package name */
    C0079bl f8670d;

    /* renamed from: e, reason: collision with root package name */
    C1303al f8671e;

    public C1279G(G.R r2, C0079bl c0079bl, C1303al c1303al) {
        this.f8669c = r2;
        this.f8670d = c0079bl;
        this.f8668b = c1303al.f8861p;
        this.f8671e = c1303al;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (this.f8671e.isEnabled()) {
            if (!str.equals(this.f8670d.l())) {
                if (str.equals(this.f8670d.f())) {
                    this.f8668b.k(d2);
                    if (this.f8668b.isEnabled()) {
                        this.f8671e.n();
                        return;
                    }
                    return;
                }
                return;
            }
            this.f8668b.f(d2);
            if (this.f8668b.H() < d2 || System.currentTimeMillis() - this.f8668b.J() > this.f8668b.G()) {
                this.f8668b.l(d2);
            }
            if (this.f8668b.isEnabled()) {
                this.f8671e.n();
            }
        }
    }

    @Override // bt.bN
    public void a() {
        if (this.f8670d.l() != null) {
            try {
                C0113cs.a().a(this.f8669c.c(), this.f8670d.l(), this);
            } catch (V.a e2) {
                bH.C.a("Unable to subscribe x or y axis for hightlights.", e2, this);
            }
        }
        if (this.f8670d.f() != null) {
            try {
                C0113cs.a().a(this.f8669c.c(), this.f8670d.f(), this);
            } catch (V.a e3) {
                bH.C.a("Unable to subscribe x or y axis for hightlights.", e3, this);
            }
        }
    }

    @Override // bt.bN
    public void b() {
        C0113cs.a().a(this);
    }
}
