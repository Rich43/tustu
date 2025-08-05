package bQ;

import G.R;
import G.T;
import W.ap;
import bH.I;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/j.class */
public class j {

    /* renamed from: d, reason: collision with root package name */
    private ap f7426d = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f7427a = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private static j f7425c = null;

    /* renamed from: b, reason: collision with root package name */
    static String f7428b = "controllerInterfaceId";

    private j() {
    }

    public static j a() {
        if (f7425c == null) {
            f7425c = new j();
        }
        return f7425c;
    }

    public l a(R r2) {
        l lVar = (l) this.f7427a.get(r2.c());
        if (lVar == null) {
            lVar = new l(r2.O());
            this.f7427a.put(r2.c(), lVar);
        }
        return lVar;
    }

    public void a(String str) {
        l lVar = (l) this.f7427a.remove(str);
        if (lVar != null) {
            lVar.j();
        }
    }

    public void a(String str, String str2) {
        b(b(str) + f7428b, str2);
        b(c(str) + f7428b, str2);
    }

    public String b(R r2) {
        return c(c(r2.c()) + f7428b, c(b(r2.c()) + f7428b, null));
    }

    public void a(String str, A.f fVar) {
        String strB = b(str);
        String strC = c(str);
        String str2 = strB + fVar.h();
        String str3 = strC + fVar.h();
        for (A.r rVar : fVar.l()) {
            Object objA = fVar.a(rVar.c());
            if (objA != null) {
                b(str2 + rVar.c(), objA.toString());
                b(str3 + rVar.c(), objA.toString());
            }
        }
    }

    public void b(String str, A.f fVar) {
        String strB = b(str);
        String strC = c(str);
        String str2 = strB + fVar.h();
        String str3 = strC + fVar.h();
        for (A.r rVar : fVar.l()) {
            Object objA = fVar.a(rVar.c());
            try {
                fVar.a(rVar.c(), c(str3 + rVar.c(), c(str2 + rVar.c(), c(rVar.c(), (objA != null ? objA : "").toString()).toString()).toString()));
            } catch (A.s e2) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    private String b(String str) {
        String str2 = "XCP_MasterCommDriver.";
        if (T.a().c() != null && !T.a().c().c().equals(str)) {
            str2 = str2 + str + ".";
        }
        return str2;
    }

    private String c(String str) {
        String str2 = b() + "MasterCommDriver.";
        if (T.a().c() != null && !T.a().c().c().equals(str)) {
            str2 = str2 + str + ".";
        }
        return str2;
    }

    public static String b() {
        String hostName = I.a() ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
        if (hostName == null || hostName.isEmpty()) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (Exception e2) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        if (hostName == null || hostName.isEmpty()) {
            hostName = "UnknownHost";
        }
        return hostName;
    }

    public void a(ap apVar) {
        this.f7426d = apVar;
    }

    private void b(String str, String str2) {
        if (this.f7426d != null) {
            this.f7426d.a(str, str2);
        }
    }

    private String c(String str, String str2) {
        return this.f7426d != null ? this.f7426d.b(str, str2) : str2;
    }
}
