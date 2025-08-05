package aR;

import G.R;
import G.T;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import d.InterfaceC1711c;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aR/n.class */
public class n implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3874a = "ecuConfigName";

    /* renamed from: b, reason: collision with root package name */
    String f3875b = null;

    @Override // d.InterfaceC1711c
    public String b() {
        return "Set MS3 RTC";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "User Action";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) {
        String property = properties.getProperty(f3874a);
        R rC = null;
        if (property != null) {
            rC = T.a().c(property);
        }
        if (rC == null) {
            rC = T.a().c();
        }
        if (rC == null || rC.i() == null || !rC.i().startsWith("MS3")) {
            bH.C.d("No MS3 found to set RTC");
            return;
        }
        if (!rC.R()) {
            bH.C.d("Not Setting RTC on MS3, it appears to not be online.");
            return;
        }
        try {
            bH.C.d("Setting RTC on MS3");
            new aI.p(rC).a(new Date());
        } catch (V.a e2) {
            Logger.getLogger(n.class.getName()).log(Level.SEVERE, "Failed to set RTC", (Throwable) e2);
        } catch (RemoteAccessException e3) {
            Logger.getLogger(n.class.getName()).log(Level.SEVERE, "Failed to set RTC", (Throwable) e3);
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "setMs3Rtc";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        return new d.k();
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return false;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }
}
