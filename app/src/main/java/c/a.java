package C;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:C/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public static String f220a = "MS3 Verified B&G";

    /* renamed from: b, reason: collision with root package name */
    public static String f221b = "Comm time out: Code 17743";

    /* renamed from: c, reason: collision with root package name */
    public static String f222c = "General Exception Code 453432";

    /* renamed from: d, reason: collision with root package name */
    public static String f223d = "Warning!!\n\n Communication Established!\n\nHowever, there is a confguration error with your firmware.\nCheck to make sure your MS3 firmware is correctly loaded.\n\nGoing offline.\n";

    /* renamed from: i, reason: collision with root package name */
    private static a f224i = null;

    /* renamed from: e, reason: collision with root package name */
    Map f225e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    Map f226f = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    Locale f227g = Locale.US;

    /* renamed from: h, reason: collision with root package name */
    Locale f228h = this.f227g;

    /* renamed from: j, reason: collision with root package name */
    private e f229j = null;

    protected a() {
    }

    public static a a(e eVar) {
        a aVarA = a();
        aVarA.b(eVar);
        return aVarA;
    }

    public static a a() {
        if (f224i == null) {
            f224i = new a();
        }
        return f224i;
    }

    public void a(Locale locale) {
        this.f228h = locale;
        this.f225e.clear();
    }

    public String a(String str, String str2) {
        if (this.f228h.getLanguage().equals(this.f227g.getLanguage())) {
            return str2;
        }
        Map mapA = a(str);
        String str3 = str2;
        if (mapA == null) {
            return str2;
        }
        String str4 = (String) mapA.get(str2);
        if (str4 != null && str4.length() > 0) {
            str3 = str4;
        }
        return str3;
    }

    private Map a(String str) {
        Map mapA = (Map) this.f225e.get(str);
        if (mapA == null && !this.f228h.getLanguage().equals(this.f227g.getLanguage())) {
            mapA = this.f229j.a(str, this.f228h);
            this.f225e.put(str, mapA);
            HashMap map = new HashMap();
            for (String str2 : mapA.keySet()) {
                map.put((String) mapA.get(str2), str2);
            }
            this.f226f.put(str, map);
        }
        return mapA;
    }

    private Map b(String str) {
        Map map = (Map) this.f226f.get(str);
        if (map == null) {
            return null;
        }
        return map;
    }

    public String b(String str, String str2) {
        Map mapB;
        if (!this.f228h.equals(this.f227g) && (mapB = b(str)) != null) {
            String str3 = (String) mapB.get(str2);
            return (str3 == null || str3.equals("")) ? str2 : str3;
        }
        return str2;
    }

    public void b(e eVar) {
        this.f229j = eVar;
    }

    public Locale b() {
        return this.f228h;
    }

    public Locale c() {
        return this.f227g;
    }
}
