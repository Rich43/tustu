package ao;

import az.InterfaceC0943d;
import java.awt.Window;
import java.io.File;

/* loaded from: TunerStudioMS.jar:ao/bI.class */
public class bI implements InterfaceC0943d {
    @Override // az.InterfaceC0943d
    public String a() {
        return h.i.f12255b + " " + h.i.f12256c;
    }

    @Override // az.InterfaceC0943d
    public String b() {
        return System.getProperty("user.home") + File.separator;
    }

    @Override // az.InterfaceC0943d
    public String c() {
        return h.i.e("activationKey", "");
    }

    @Override // az.InterfaceC0943d
    public void a(String str) throws V.a {
        h.i.d("activationKey", str);
        h.i.h();
    }

    @Override // az.InterfaceC0943d
    public String d() {
        return h.i.e("registrationKeyV2", "");
    }

    @Override // az.InterfaceC0943d
    public String e() {
        return h.i.e("uid", "");
    }

    @Override // az.InterfaceC0943d
    public String f() {
        return h.i.f12255b;
    }

    @Override // az.InterfaceC0943d
    public String g() {
        return h.i.f12256c;
    }

    @Override // az.InterfaceC0943d
    public String h() {
        return h.i.e("userEmail", "");
    }

    @Override // az.InterfaceC0943d
    public Window i() {
        return C0645bi.a().b();
    }

    @Override // az.InterfaceC0943d
    public boolean j() {
        return true;
    }
}
