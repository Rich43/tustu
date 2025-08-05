package bt;

import com.efiAnalytics.ui.InterfaceC1662et;
import r.C1798a;

/* renamed from: bt.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/i.class */
public class C1350i implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    String f9098a;

    public C1350i(String str) {
        this.f9098a = "";
        this.f9098a = str;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        C1798a.a().b(this.f9098a + "_" + str, str2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return b(str, "");
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return C1798a.a().c(this.f9098a + "_" + str, str2);
    }
}
