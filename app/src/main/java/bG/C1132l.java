package bg;

import bH.C;
import com.efiAnalytics.apps.ts.tuningViews.C1429b;
import com.efiAnalytics.apps.ts.tuningViews.F;
import com.efiAnalytics.ui.InterfaceC1535a;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import javax.swing.SwingUtilities;
import v.C1887g;

/* renamed from: bg.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/l.class */
class C1132l implements InterfaceC1535a {

    /* renamed from: b, reason: collision with root package name */
    private C1429b f8082b;

    /* renamed from: c, reason: collision with root package name */
    private String[] f8083c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1121a f8084a;

    C1132l(C1121a c1121a, C1429b c1429b, String[] strArr) {
        this.f8084a = c1121a;
        this.f8082b = null;
        this.f8083c = null;
        this.f8082b = c1429b;
        this.f8083c = strArr;
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
        SwingUtilities.invokeLater(new RunnableC1133m(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            F fA = new C1887g().a(this.f8082b.a());
            if (this.f8082b.b() && !a(fA.a())) {
                String str = this.f8083c[0];
                for (int i2 = 1; i2 < this.f8083c.length; i2++) {
                    str = str + " & " + this.f8083c[i2];
                }
                if (!bV.a("Warning: Tune View firmware signature (" + fA.a() + ")\ndoes not match current firmware (" + str + ").\nYou may need to edit some components of the layout.\n \nContinue Loading?", (Component) this.f8084a.getParent(), true)) {
                    return;
                }
            }
            fA.a(this.f8083c[0]);
            this.f8084a.a(fA);
        } catch (V.a e2) {
            C.a("Unable to load dash file:\n" + ((Object) this.f8082b.a()), e2, this.f8084a.getParent());
        }
    }

    private boolean a(String str) {
        for (int i2 = 0; i2 < this.f8083c.length; i2++) {
            if (this.f8083c[i2].equals(str)) {
                return true;
            }
        }
        return false;
    }
}
