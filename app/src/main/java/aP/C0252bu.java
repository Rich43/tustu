package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.bu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bu.class */
class C0252bu implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    G.R f3099a;

    /* renamed from: b, reason: collision with root package name */
    G.bH f3100b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0240bi f3101c;

    C0252bu(C0240bi c0240bi, G.R r2, G.bH bHVar) {
        this.f3101c = c0240bi;
        this.f3099a = r2;
        this.f3100b = bHVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            C0338f.a().a(this.f3099a, this.f3100b, com.efiAnalytics.ui.bV.b(this.f3101c.getComponent()));
        } catch (Exception e2) {
            bH.C.a("Error showing dialog:\n" + e2.getMessage(), e2, null);
        }
    }
}
