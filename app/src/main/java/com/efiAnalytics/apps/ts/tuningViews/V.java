package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.InterfaceC1535a;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import javax.swing.SwingUtilities;
import v.C1887g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/V.class */
class V implements InterfaceC1535a {

    /* renamed from: b, reason: collision with root package name */
    private C1429b f9760b;

    /* renamed from: c, reason: collision with root package name */
    private String[] f9761c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9762a;

    V(J j2, C1429b c1429b, String[] strArr) {
        this.f9762a = j2;
        this.f9760b = null;
        this.f9761c = null;
        this.f9760b = c1429b;
        this.f9761c = strArr;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public boolean a() {
        c();
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void b() {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void c() {
        SwingUtilities.invokeLater(new W(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            F fA = new C1887g().a(this.f9760b.a());
            if (this.f9760b.b() && !a(fA.a())) {
                String str = this.f9761c[0];
                for (int i2 = 1; i2 < this.f9761c.length; i2++) {
                    str = str + " & " + this.f9761c[i2];
                }
                if (!bV.a("Warning: Gauge Cluster firmware signature (" + fA.a() + ")\ndoes not match current firmware (" + str + ").\nYou may need to edit some components of the layout.\n \nContinue Loading?", (Component) this.f9762a.getParent(), true)) {
                    return;
                }
            }
            fA.a(this.f9761c[0]);
            this.f9762a.a(fA);
        } catch (V.a e2) {
            bH.C.a("Unable to load dash file:\n" + ((Object) this.f9760b.a()), e2, this.f9762a.getParent());
        }
    }

    private boolean a(String str) {
        for (int i2 = 0; i2 < this.f9761c.length; i2++) {
            if (this.f9761c[i2].equals(str)) {
                return true;
            }
        }
        return false;
    }
}
