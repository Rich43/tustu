package aR;

import aP.C0404hl;
import d.InterfaceC1711c;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:aR/v.class */
public class v implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3881a = "Message";

    /* renamed from: b, reason: collision with root package name */
    public static String f3882b = "passiveMessage";

    /* renamed from: c, reason: collision with root package name */
    d.k f3883c = null;

    @Override // d.InterfaceC1711c
    public String a() {
        return f3882b;
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return "Show a Passive Message";
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
        String property = properties.getProperty(f3881a, null);
        if (property == null || property.isEmpty()) {
            throw new d.e(f3881a + " is required");
        }
        C0404hl.a().a(property);
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f3881a, null);
        if (property == null || property.isEmpty()) {
            throw new d.e(f3881a + " is required");
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        if (this.f3883c == null) {
            this.f3883c = new d.k();
            d.i iVar = new d.i(f3881a, "");
            iVar.a(1);
            iVar.c("Set the message you want displayed in the lower toolbar when this User Action is triggered.");
            this.f3883c.add(iVar);
        }
        return this.f3883c;
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
