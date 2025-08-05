package com.efiAnalytics.tuningwidgets.panels;

import G.C0075bh;
import G.bL;
import W.C0188n;
import bF.C0973d;
import bt.C1290R;
import bt.bN;
import i.C1743c;
import i.InterfaceC1741a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/P.class */
public class P implements bN, InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    C1290R f10277a;

    /* renamed from: b, reason: collision with root package name */
    C0973d f10278b;

    /* renamed from: c, reason: collision with root package name */
    G.R f10279c;

    /* renamed from: d, reason: collision with root package name */
    C0075bh f10280d;

    /* renamed from: e, reason: collision with root package name */
    String f10281e = null;

    /* renamed from: f, reason: collision with root package name */
    String f10282f = null;

    /* renamed from: g, reason: collision with root package name */
    int f10283g = -1000;

    /* renamed from: h, reason: collision with root package name */
    String f10284h = "UNINITIALIZED";

    public P(G.R r2, C0075bh c0075bh, C1290R c1290r) {
        this.f10279c = r2;
        this.f10280d = c0075bh;
        this.f10277a = c1290r;
        this.f10278b = c1290r.f();
        c();
    }

    public void a(boolean z2) {
        if (z2) {
            C1743c.a().a(this);
        } else {
            C1743c.a().b(this);
        }
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        if (this.f10277a.isEnabled()) {
            c();
            this.f10278b.a(C1743c.a().e().b(this.f10281e) != null ? r0.d(i2) : Double.NaN);
            this.f10277a.c();
        }
    }

    @Override // bt.bN
    public void a() {
        a(true);
    }

    @Override // bt.bN
    public void b() {
        a(false);
    }

    private void c() {
        String strD = this.f10280d.d();
        if (strD == null || strD.isEmpty()) {
            this.f10281e = null;
            return;
        }
        C0188n c0188nE = C1743c.a().e();
        this.f10281e = bL.j(this.f10279c, strD);
        if (c0188nE != null) {
            if (this.f10281e == null || (this.f10281e.isEmpty() && c0188nE.a(strD) != null)) {
                this.f10281e = strD;
            }
            if (this.f10281e == null && strD.equals("Load")) {
                String strE = this.f10279c.g("Load").e();
                if (strE.equalsIgnoreCase("kpa") && c0188nE.b("MAP") != null) {
                    this.f10281e = "MAP";
                } else if (strE.equalsIgnoreCase("TPS") && c0188nE.b("TPS") != null) {
                    this.f10281e = "TPS";
                } else if (strE.equalsIgnoreCase("TPS") && c0188nE.b("TPS_Pct") != null) {
                    this.f10281e = "TPS_Pct";
                } else if (strE.equalsIgnoreCase("psi") && c0188nE.b("Boost psi") != null) {
                    this.f10281e = "Boost psi";
                } else if (strE.equalsIgnoreCase("psi") && c0188nE.b("Boost") != null) {
                    this.f10281e = "Boost";
                }
            }
        }
        if (this.f10281e == null) {
            bH.C.b("No Data Log field defined for X axis of table: " + this.f10280d.aJ());
        }
    }
}
