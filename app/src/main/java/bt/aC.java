package bt;

import com.efiAnalytics.tuningwidgets.panels.InterfaceC1508f;

/* loaded from: TunerStudioMS.jar:bt/aC.class */
class aC implements InterfaceC1508f {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8743a;

    aC(C1303al c1303al) {
        this.f8743a = c1303al;
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.InterfaceC1508f
    public void a(String str) {
        if (str == null) {
            str = "";
        }
        this.f8743a.f8871z.a("xPlotColumnName", str);
        this.f8743a.s();
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.InterfaceC1508f
    public void b(String str) {
        if (str == null) {
            str = "";
        }
        this.f8743a.f8871z.a("yPlotColumnName", str);
        this.f8743a.s();
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.InterfaceC1508f
    public void a() {
        this.f8743a.r();
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.InterfaceC1508f
    public boolean c(String str) {
        if (!this.f8743a.c(str)) {
            return false;
        }
        this.f8743a.b(str);
        this.f8743a.f8871z.a("xyPlotFilterExp", str);
        return true;
    }
}
