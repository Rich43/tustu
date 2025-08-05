package A;

import G.R;
import G.T;
import W.ap;
import bH.I;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:A/v.class */
public class v {

    /* renamed from: d, reason: collision with root package name */
    private ap f82d = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f83a = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private static v f81c = null;

    /* renamed from: b, reason: collision with root package name */
    static String f84b = "controllerInterfaceId";

    private v() {
    }

    public static v a() {
        if (f81c == null) {
            f81c = new v();
        }
        return f81c;
    }

    public t a(R r2) {
        t tVar = (t) this.f83a.get(r2.c());
        if (tVar == null) {
            tVar = new t(r2.O());
            this.f83a.put(r2.c(), tVar);
        }
        return tVar;
    }

    public void a(String str) {
        this.f83a.remove(str);
    }

    public void a(String str, String str2) {
        b(c(str) + f84b, str2);
        b(d(str) + f84b, str2);
    }

    public String b(R r2) {
        String strC = c(d(r2.c()) + f84b, c(c(r2.c()) + f84b, c(b(r2.c()) + f84b, null)));
        if (strC == null && (r2.C() instanceof g)) {
            g gVar = (g) r2.C();
            if (gVar.a() != null) {
                strC = gVar.a().h();
            }
        }
        return strC;
    }

    public void a(String str, f fVar) {
        String strC = c(str);
        String strD = d(str);
        String str2 = strC + fVar.h();
        String str3 = strD + fVar.h();
        for (r rVar : fVar.l()) {
            Object objA = fVar.a(rVar.c());
            if (objA != null) {
                b(str2 + rVar.c(), objA.toString());
                b(str3 + rVar.c(), objA.toString());
            }
        }
    }

    public void b(String str, f fVar) {
        String strC = c(str);
        String strD = d(str);
        String str2 = strC + fVar.h();
        String str3 = strD + fVar.h();
        for (r rVar : fVar.l()) {
            Object objA = fVar.a(rVar.c());
            try {
                fVar.a(rVar.c(), c(str3 + rVar.c(), c(str2 + rVar.c(), c(b(str) + rVar.c(), c(rVar.c(), (objA != null ? objA : "").toString()).toString()).toString()).toString()));
            } catch (s e2) {
                Logger.getLogger(v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    private String b(String str) {
        return (T.a().c() == null || T.a().c().c().equals(str)) ? "MSCommDriver." + str : "MSCommDriver." + str + ".";
    }

    private String c(String str) {
        String str2 = "MSCommDriver.";
        if (T.a().c() != null && !T.a().c().c().equals(str)) {
            str2 = str2 + str + ".";
        }
        return str2;
    }

    private String d(String str) {
        String str2 = b() + "CommDriver.";
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
                Logger.getLogger(v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        if (hostName == null || hostName.isEmpty()) {
            hostName = "UnknownHost";
        }
        return hostName;
    }

    public void a(ap apVar) {
        this.f82d = apVar;
    }

    public ap c() {
        return this.f82d;
    }

    private void b(String str, String str2) {
        if (this.f82d != null) {
            this.f82d.a(str, str2);
        }
    }

    private String c(String str, String str2) {
        return this.f82d != null ? this.f82d.b(str, str2) : str2;
    }
}
