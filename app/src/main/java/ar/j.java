package aR;

import aP.C0338f;
import aP.cZ;
import d.InterfaceC1711c;
import java.awt.Frame;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:aR/j.class */
public class j implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String b() {
        return "Open Last Project";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Project";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) {
        C0338f.a().k((Frame) cZ.a().c());
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "openLastProject";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        return null;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return false;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return false;
    }
}
