package bt;

import com.efiAnalytics.ui.InterfaceC1662et;
import r.C1798a;

/* renamed from: bt.E, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/E.class */
public class C1277E implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    G.R f8661a;

    public C1277E(G.R r2) {
        this.f8661a = null;
        this.f8661a = r2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        try {
            aE.g gVarI = aE.a.A().i();
            if (str.equals("displacement")) {
                gVarI.setProperty("engineDisplacement", str2);
            } else if (str.equals("injectorFlow")) {
                gVarI.setProperty("injectorSize", str2);
            } else {
                C1798a.a().b("reqFuelCalc" + str, str2);
            }
        } catch (V.a e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return str.equals("displacement") ? c("engineDisplacement", "350") : str.equals("injectorFlow") ? c("injectorSize", "30") : C1798a.a().c("reqFuelCalc" + str, "");
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return str2;
    }

    private String c(String str, String str2) {
        try {
            String property = aE.a.A().i().getProperty(str, "");
            if (property.equals("")) {
                property = C1798a.a().c("reqFuelCalc" + str, str2);
            }
            return property;
        } catch (V.a e2) {
            e2.printStackTrace();
            return str2;
        }
    }
}
