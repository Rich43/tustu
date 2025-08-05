package aR;

import aP.C0338f;
import d.InterfaceC1711c;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:aR/m.class */
public class m implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3872a = "ecuConfigName";

    /* renamed from: b, reason: collision with root package name */
    String f3873b = null;

    @Override // d.InterfaceC1711c
    public String b() {
        return "Save Current Tune";
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
        C0338f.a().m();
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "saveCurrentTune";
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
