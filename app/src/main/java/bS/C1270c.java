package bs;

import G.C0126i;
import G.aI;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.fx;
import java.awt.Component;

/* renamed from: bs.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/c.class */
class C1270c implements fx {

    /* renamed from: a, reason: collision with root package name */
    Component f8574a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1268a f8575b;

    public C1270c(C1268a c1268a, Component component) {
        this.f8575b = c1268a;
        this.f8574a = null;
        this.f8574a = component;
    }

    @Override // com.efiAnalytics.ui.fx
    public boolean a(String str) {
        if (str.equals("")) {
            return true;
        }
        try {
            C0126i.a((aI) this.f8575b.f8570a, " " + str + " ");
            return true;
        } catch (V.g e2) {
            bV.d("Error:" + e2.getMessage(), this.f8574a);
            return false;
        }
    }
}
