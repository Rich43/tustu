package aR;

import aP.C0338f;
import aP.cZ;
import d.InterfaceC1711c;
import java.awt.Window;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aR/k.class */
public class k implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    d.k f3867a = null;

    /* renamed from: b, reason: collision with root package name */
    public static String f3868b = "projectPath";

    @Override // d.InterfaceC1711c
    public String b() {
        return "Open a Project";
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
    public void a(Properties properties) throws d.e {
        String property = properties.getProperty(f3868b);
        if (property == null || property.isEmpty()) {
            throw new d.e("projectPath is a required parameter for openProject action.");
        }
        try {
            property = URLDecoder.decode(property, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            C0338f.a().a((Window) cZ.a().c(), property);
        } catch (Exception e3) {
            throw new d.e(e3.getLocalizedMessage());
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "openProject";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f3868b);
        if (property == null || property.isEmpty()) {
            throw new d.e("projectPath is a required parameter for openProject action.");
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        if (this.f3867a == null) {
            this.f3867a = new d.k();
            d.i iVar = new d.i(f3868b, "");
            iVar.a(7);
            iVar.c("Full Path to the project folder.");
            this.f3867a.add(iVar);
        }
        return this.f3867a;
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
