package com.efiAnalytics.tuningwidgets.portEditor;

import G.C0083bp;
import G.R;
import G.aH;
import G.aM;
import G.di;
import bH.W;
import bt.aT;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/B.class */
class B extends aT {

    /* renamed from: u, reason: collision with root package name */
    final /* synthetic */ z f10513u;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public B(z zVar, R r2, C0083bp c0083bp) {
        super(r2, c0083bp);
        this.f10513u = zVar;
    }

    @Override // bt.aT, bt.bX
    public void b(String str) throws IllegalArgumentException {
        o();
        super.b(str);
    }

    protected void o() {
        aH aHVarG = this.f10513u.f10615b.c().g(this.f10513u.f10616c.a());
        if (aHVarG == null) {
            bH.C.c("Can not find OutputChannel " + this.f10513u.f10616c.a() + " in Config: " + ((Object) this.f10513u.f10615b.c()));
            return;
        }
        this.f10513u.f10613i.c(aHVarG.i());
        this.f10513u.f10613i.b(aHVarG.h());
        int iRound = (int) Math.round(Math.log10(1.0d / aHVarG.h()));
        if (iRound != this.f10513u.f10613i.u()) {
            try {
                this.f10513u.f10613i.e(di.a(iRound));
            } catch (V.g e2) {
                Logger.getLogger(z.class.getName()).log(Level.WARNING, "Failed to update digits provider threshold", (Throwable) e2);
            }
        }
        a(iRound);
        this.f10513u.f10614j.b(aHVarG.h());
        if (iRound != this.f10513u.f10614j.u()) {
            try {
                this.f10513u.f10614j.e(di.a(iRound));
            } catch (V.g e3) {
                Logger.getLogger(z.class.getName()).log(Level.WARNING, "Failed to update digits provider hysteresis", (Throwable) e3);
            }
        }
    }

    @Override // bt.aT
    public void f() {
        if (this.f8777a.b() != null) {
            o();
            aM aMVarC = this.f8778b.c(this.f8777a.b());
            if (this.f8780d != null) {
                try {
                    this.f8780d.a(aMVarC.f(this.f8778b.p()));
                    return;
                } catch (Exception e2) {
                    bH.C.b(e2.getMessage());
                    return;
                }
            }
            if (this.f8781e != null) {
                try {
                    this.f8781e.setText(W.a(aMVarC.i(n().p())[this.f8777a.e()][this.f8777a.g()]));
                } catch (V.g e3) {
                    bH.C.a(e3.getMessage(), e3, this);
                }
            }
        }
    }

    @Override // bt.aT, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        b(z2);
    }
}
