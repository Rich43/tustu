package com.efiAnalytics.tuningwidgets.panels;

import G.C0079bl;
import G.bL;
import W.C0188n;
import bt.C1303al;
import bt.bN;
import i.C1743c;
import i.InterfaceC1741a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/O.class */
public class O implements bN, InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    C1303al f10270a;

    /* renamed from: b, reason: collision with root package name */
    G.R f10271b;

    /* renamed from: c, reason: collision with root package name */
    C0079bl f10272c;

    /* renamed from: d, reason: collision with root package name */
    String f10273d = null;

    /* renamed from: e, reason: collision with root package name */
    String f10274e = null;

    /* renamed from: f, reason: collision with root package name */
    int f10275f = -1000;

    /* renamed from: g, reason: collision with root package name */
    String f10276g = "UNINITIALIZED";

    public O(G.R r2, C0079bl c0079bl, C1303al c1303al) {
        this.f10271b = r2;
        this.f10270a = c1303al;
        this.f10272c = c0079bl;
        c();
        d();
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
        d();
        this.f10270a.i().f(C1743c.a().e().b(this.f10273d) != null ? r0.d(i2) : Double.NaN);
        c();
        this.f10270a.i().k(C1743c.a().e().b(this.f10274e) != null ? r0.d(i2) : Double.NaN);
        this.f10270a.i().repaint();
        this.f10270a.n();
        this.f10275f = i2;
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
        String strF = this.f10272c.f();
        if (strF == null || strF.isEmpty()) {
            this.f10274e = null;
            return;
        }
        C0188n c0188nE = C1743c.a().e();
        this.f10274e = bL.j(this.f10271b, strF);
        if (c0188nE != null) {
            if (this.f10274e == null || (this.f10274e.isEmpty() && c0188nE.a(strF) != null)) {
                this.f10274e = strF;
            }
            if (this.f10274e == null && strF.equals("Load")) {
                String strE = this.f10271b.g("Load").e();
                if (strE.equalsIgnoreCase("kpa") && c0188nE.b("MAP") != null) {
                    this.f10274e = "MAP";
                } else if (strE.equalsIgnoreCase("TPS") && c0188nE.b("TPS") != null) {
                    this.f10274e = "TPS";
                } else if (strE.equalsIgnoreCase("TPS") && c0188nE.b("TPS_Pct") != null) {
                    this.f10274e = "TPS_Pct";
                } else if (strE.equalsIgnoreCase("psi") && c0188nE.b("Boost psi") != null) {
                    this.f10274e = "Boost psi";
                } else if (strE.equalsIgnoreCase("psi") && c0188nE.b("Boost") != null) {
                    this.f10274e = "Boost";
                }
            }
        }
        if (this.f10274e == null) {
            bH.C.b("No Data Log field defined for Y axis of table: " + this.f10272c.aJ());
        }
    }

    private void d() {
        String strL = this.f10272c.l();
        C0188n c0188nE = C1743c.a().e();
        if (c0188nE == null || strL == null || strL.isEmpty()) {
            this.f10273d = null;
            return;
        }
        if (c0188nE.a(strL) != null) {
            this.f10273d = strL;
        } else {
            this.f10273d = bL.j(this.f10271b, strL);
        }
        if (c0188nE != null) {
            if (this.f10273d == null || (this.f10273d.isEmpty() && c0188nE.a(strL) != null)) {
                this.f10273d = strL;
            }
            if (this.f10273d == null && strL.equals("Load")) {
                String strE = this.f10271b.g("Load").e();
                if (strE.equalsIgnoreCase("kpa") && c0188nE.b("MAP") != null) {
                    this.f10273d = "MAP";
                } else if (strE.equalsIgnoreCase("TPS") && c0188nE.b("TPS") != null) {
                    this.f10273d = "TPS";
                } else if (strE.equalsIgnoreCase("TPS") && c0188nE.b("TPS_Pct") != null) {
                    this.f10273d = "TPS_Pct";
                } else if (strE.equalsIgnoreCase("psi") && c0188nE.b("Boost psi") != null) {
                    this.f10273d = "Boost psi";
                } else if (strE.equalsIgnoreCase("psi") && c0188nE.b("Boost") != null) {
                    this.f10273d = "Boost";
                }
            }
        }
        if (this.f10273d == null) {
            bH.C.b("No Data Log field defined for X axis of table: " + this.f10272c.aJ());
        }
    }
}
