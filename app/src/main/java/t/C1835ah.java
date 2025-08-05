package t;

import com.efiAnalytics.ui.InterfaceC1662et;
import r.C1798a;

/* renamed from: t.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/ah.class */
class C1835ah implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    String f13783a;

    /* renamed from: b, reason: collision with root package name */
    String f13784b = "dashCompProperty_";

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1875w f13785c;

    C1835ah(C1875w c1875w, String str) {
        this.f13785c = c1875w;
        this.f13783a = "";
        this.f13783a = str;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        C1798a.a().b(this.f13784b + this.f13783a + "_" + str, str2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return b(str, "");
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return C1798a.a().c(this.f13784b + this.f13783a + "_" + str, str2);
    }
}
