package aP;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.Dialog;
import java.awt.Rectangle;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/H.class */
class H implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    Dialog f2750a;

    /* renamed from: b, reason: collision with root package name */
    String f2751b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0338f f2752c;

    H(C0338f c0338f, Dialog dialog, String str) {
        this.f2752c = c0338f;
        this.f2750a = null;
        this.f2751b = "";
        this.f2750a = dialog;
        this.f2751b = str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v18, types: [java.util.Properties] */
    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        String str = this.f2751b + this.f2750a.getTitle();
        Rectangle bounds = this.f2750a.getBounds();
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            aVarA = C1798a.a().d();
        }
        aVarA.setProperty(str + "_X", com.efiAnalytics.ui.eJ.b(bounds.f12372x) + "");
        aVarA.setProperty(str + "_Y", com.efiAnalytics.ui.eJ.b(bounds.f12373y) + "");
        aVarA.setProperty(str + "_width", com.efiAnalytics.ui.eJ.b(bounds.width) + "");
        aVarA.setProperty(str + "_height", com.efiAnalytics.ui.eJ.b(bounds.height) + "");
    }
}
