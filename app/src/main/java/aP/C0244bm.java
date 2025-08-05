package aP;

import G.C0088bu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import x.C1891a;

/* renamed from: aP.bm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bm.class */
class C0244bm implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ G.R f3084a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0088bu f3085b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1891a f3086c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ bA.e f3087d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C0240bi f3088e;

    C0244bm(C0240bi c0240bi, G.R r2, C0088bu c0088bu, C1891a c1891a, bA.e eVar) {
        this.f3088e = c0240bi;
        this.f3084a = r2;
        this.f3085b = c0088bu;
        this.f3086c = c1891a;
        this.f3087d = eVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        G.aM aMVarC = this.f3084a.c(this.f3085b.aa());
        if (aMVarC == null) {
            com.efiAnalytics.ui.bV.d("Invalid Password Parameter.", this.f3086c);
            return;
        }
        try {
            aMVarC.a(this.f3084a.h(), 0.0d);
            this.f3087d.setEnabled(false);
        } catch (V.g e2) {
            Logger.getLogger(C0240bi.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (V.j e3) {
            Logger.getLogger(C0240bi.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            com.efiAnalytics.ui.bV.d("Invalid Password?", this.f3086c);
        }
        com.efiAnalytics.tuningwidgets.panels.W.a();
    }
}
