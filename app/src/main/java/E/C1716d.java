package e;

import ac.h;
import d.InterfaceC1711c;
import d.i;
import d.k;
import java.util.Properties;

/* renamed from: e.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:e/d.class */
public class C1716d implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f12138a = "MARK_Comment";

    @Override // d.InterfaceC1711c
    public String b() {
        return "Mark Data Log Comment";
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
    public void a(Properties properties) {
        h hVarK = h.k();
        if (hVarK != null) {
            String property = properties.getProperty(f12138a, null);
            if (property == null || property.isEmpty()) {
                hVarK.d("Action Triggered");
            } else {
                hVarK.d(property);
            }
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "markDataLogComment";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
    }

    @Override // d.InterfaceC1711c
    public k e() {
        k kVar = new k();
        kVar.add(new i(f12138a, null));
        return kVar;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }
}
