package e;

import bH.C;
import d.InterfaceC1711c;
import d.i;
import d.k;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: e.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:e/b.class */
public class C1714b implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String a() {
        return "shellCommand";
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return "Execute a Shell Command";
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
        String property = properties.getProperty("Shell_Command");
        if (property == null) {
            C.b("Execute a Shell Command: Command not defined! Cannont execute process.");
            return;
        }
        try {
            Runtime.getRuntime().exec(property);
        } catch (IOException e2) {
            Logger.getLogger(C1714b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
    }

    @Override // d.InterfaceC1711c
    public k e() {
        k kVar = new k();
        i iVar = new i("Shell_Command", null);
        iVar.c("Set to the command you want executed. This would be a command as you would type in to a terminal or prompt for the OS you are using.");
        kVar.add(iVar);
        return kVar;
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
