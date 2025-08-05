package e;

import ac.h;
import d.InterfaceC1711c;
import d.k;
import java.util.Properties;

/* renamed from: e.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:e/c.class */
public class C1715c implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    String f12137a = null;

    @Override // d.InterfaceC1711c
    public String b() {
        return "Mark Data Log";
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
            hVarK.d("Action Triggered");
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "markDataLog";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
    }

    @Override // d.InterfaceC1711c
    public k e() {
        return new k();
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
