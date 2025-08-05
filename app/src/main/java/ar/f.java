package aR;

import aP.C0338f;
import d.InterfaceC1711c;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:aR/f.class */
public class f implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3859a = "Warning_Message";

    /* renamed from: b, reason: collision with root package name */
    public static String f3860b = "globalWarning";

    /* renamed from: c, reason: collision with root package name */
    d.k f3861c = null;

    @Override // d.InterfaceC1711c
    public String a() {
        return f3860b;
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return "Show a Global Warning";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Other Actions";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e {
        String property = properties.getProperty(f3859a, null);
        if (property == null || property.isEmpty()) {
            throw new d.e(f3859a + " is required");
        }
        C0338f.a().d(property);
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f3859a, null);
        if (property == null || property.isEmpty()) {
            throw new d.e(f3859a + " is required");
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        if (this.f3861c == null) {
            this.f3861c = new d.k();
            d.i iVar = new d.i(f3859a, "");
            iVar.a(1);
            iVar.c("Set the message you want displayed throughout the application when this User Action is triggered.");
            this.f3861c.add(iVar);
        }
        return this.f3861c;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return false;
    }
}
