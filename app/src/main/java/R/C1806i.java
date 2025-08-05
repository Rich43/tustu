package r;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import s.C1818g;

/* renamed from: r.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/i.class */
public class C1806i {

    /* renamed from: a, reason: collision with root package name */
    List f13439a = new ArrayList();

    /* renamed from: z, reason: collision with root package name */
    private static C1806i f13438z = null;

    /* renamed from: b, reason: collision with root package name */
    public static String f13440b = "(Beta)";

    /* renamed from: c, reason: collision with root package name */
    public static String f13441c = " Lite!";

    /* renamed from: d, reason: collision with root package name */
    public static String f13442d = " Trial";

    /* renamed from: e, reason: collision with root package name */
    public static String f13443e = "QJ";

    /* renamed from: f, reason: collision with root package name */
    public static String f13444f = "MS";

    /* renamed from: g, reason: collision with root package name */
    public static String f13445g = "MS Ultra";

    /* renamed from: h, reason: collision with root package name */
    public static String f13446h = "MS Dev";

    /* renamed from: i, reason: collision with root package name */
    public static String f13447i = "MS Dev(Beta)";

    /* renamed from: j, reason: collision with root package name */
    public static String f13448j = "EFI";

    /* renamed from: k, reason: collision with root package name */
    public static String f13449k = "Tuner";

    /* renamed from: l, reason: collision with root package name */
    public static String f13450l = "MS(Beta)";

    /* renamed from: m, reason: collision with root package name */
    public static String f13451m = "MS Ultra(Beta)";

    /* renamed from: n, reason: collision with root package name */
    public static String f13452n = "Super";

    /* renamed from: o, reason: collision with root package name */
    public static String f13453o = "Pro";

    /* renamed from: p, reason: collision with root package name */
    public static String f13454p = "Pro Ultra";

    /* renamed from: q, reason: collision with root package name */
    public static String f13455q = "Pro Dev";

    /* renamed from: r, reason: collision with root package name */
    public static String f13456r = "Pro Single";

    /* renamed from: A, reason: collision with root package name */
    private static byte[] f13457A = {68, 101, 118};

    /* renamed from: s, reason: collision with root package name */
    public static String f13458s = "BigComm";

    /* renamed from: t, reason: collision with root package name */
    public static String f13459t = C1798a.f13338at;

    /* renamed from: u, reason: collision with root package name */
    public static String f13460u = "TunerStudio";

    /* renamed from: v, reason: collision with root package name */
    public static String f13461v = "ShadowTuner.com";

    /* renamed from: w, reason: collision with root package name */
    public static String f13462w = C1798a.f13340av;

    /* renamed from: x, reason: collision with root package name */
    public static String f13463x = C1798a.f13341aw;

    /* renamed from: y, reason: collision with root package name */
    public static String f13464y = C1798a.f13336ar;

    private C1806i() {
    }

    public static C1806i a() {
        if (f13438z == null) {
            f13438z = new C1806i();
        }
        return f13438z;
    }

    public boolean a(String str) {
        return this.f13439a.contains(str);
    }

    public boolean a(String str, String str2) {
        return str2.contains(new String(f13457A));
    }

    public String b() {
        String strC = C1798a.a().c(C1798a.cF, "");
        return (strC == null || strC.isEmpty()) ? "" : "https://www.efianalytics.com/register/upgradeSoftware.jsp?regKey=" + strC;
    }

    public String c() {
        if (!a("87gdjkjd98fes")) {
            return "";
        }
        String strC = C1798a.a().c(C1798a.cF, "");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 7, 3);
        return calendar.getTime().getTime() > System.currentTimeMillis() ? "Try new: <a href=https://www.efianalytics.com/register/upgradeSoftware.jsp?regKey=" + strC + "\">" + C1818g.b("TunerStudio MS Ultra") + "</a><br>" + C1818g.b("For a limited time, free trial for registered users!") : "<br>Upgrade to:<br><a href=https://www.efianalytics.com/register/upgradeSoftware.jsp?regKey=" + strC + "\">" + C1818g.b("TunerStudio MS Ultra") + "</a> " + C1818g.b("Now!") + "<br>" + C1818g.b("Featuring Tuning and Dyno Views!");
    }

    public void a(List list) {
        this.f13439a = list;
    }

    public List d() {
        return this.f13439a;
    }
}
