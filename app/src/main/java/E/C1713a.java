package e;

import G.R;
import G.T;
import d.InterfaceC1711c;
import d.k;
import java.util.Properties;

/* renamed from: e.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:e/a.class */
public class C1713a implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String b() {
        return "Send Burn Command";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Settings Dialogs";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) {
        R rC = T.a().c();
        if (rC != null) {
            rC.I();
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "burnData";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
    }

    @Override // d.InterfaceC1711c
    public k e() {
        return null;
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
