package n;

import G.bS;
import ay.C0935l;
import ay.C0937n;
import ay.InterfaceC0938o;
import bH.W;
import com.efiAnalytics.ui.bV;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:n/k.class */
public class k implements InterfaceC0938o {

    /* renamed from: a, reason: collision with root package name */
    private static k f12944a = null;

    private k() {
        try {
            for (String str : C1798a.a().f(C1798a.f13315W)) {
                C0935l.a().a(W.b(str, C1798a.f13315W, ""), this);
            }
        } catch (V.a e2) {
            j.a().b("Failed to queue downloaded ECU Definitions! \nError: " + e2.getLocalizedMessage());
        }
    }

    public static k a() {
        if (f12944a == null) {
            f12944a = new k();
        }
        return f12944a;
    }

    public void a(bS bSVar) {
        C1798a.a().b(C1798a.f13315W + bSVar.b(), bSVar.b());
        C1798a.a().b(C1798a.f13316X + bSVar.b(), bSVar.c());
        try {
            C0935l.a().a(bSVar.b(), this);
        } catch (V.a e2) {
            Logger.getLogger(k.class.getName()).log(Level.WARNING, "Shoudn't have happened...", (Throwable) e2);
        }
    }

    @Override // ay.InterfaceC0938o
    public void a(C0937n c0937n) {
        if (c0937n.b() != 0) {
            j.a().b("ECU Definition Failed for " + c0937n.a() + "! \nError: " + c0937n.c());
            C1798a.a().e(C1798a.f13315W + c0937n.a());
            C1798a.a().e(C1798a.f13316X + c0937n.a());
            return;
        }
        String strC = C1798a.a().c(C1798a.f13316X + c0937n.a(), "");
        if (strC.isEmpty()) {
            strC = c0937n.a();
        }
        j.a().b(a("The correct ECU Definition has been downloaded for the Firmware") + ":\n" + strC + "\n\n" + a("You may now connect to your Controller with this firmware.") + "\n" + a("When prompted for a signature mismatch, select 'Update ECU Definition'"));
        C1798a.a().e(C1798a.f13315W + c0937n.a());
        C1798a.a().e(C1798a.f13316X + c0937n.a());
    }

    private String a(String str) {
        return bV.a() != null ? bV.a().a(str) : str;
    }
}
