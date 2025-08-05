package W;

/* loaded from: TunerStudioMS.jar:W/M.class */
public class M {

    /* renamed from: a, reason: collision with root package name */
    private String f1936a;

    /* renamed from: b, reason: collision with root package name */
    private String f1937b;

    /* renamed from: c, reason: collision with root package name */
    private String f1938c;

    /* renamed from: d, reason: collision with root package name */
    private int f1939d;

    /* renamed from: e, reason: collision with root package name */
    private String f1940e;

    public M() {
        this.f1936a = null;
        this.f1937b = null;
        this.f1938c = null;
        this.f1939d = -1;
        this.f1940e = null;
    }

    public M(String str) {
        this.f1936a = null;
        this.f1937b = null;
        this.f1938c = null;
        this.f1939d = -1;
        this.f1940e = null;
        this.f1940e = str;
    }

    public String a() {
        return this.f1936a;
    }

    public void a(String str, int i2) {
        this.f1936a = str;
        this.f1939d = i2;
        int iA = bH.W.a(str, ';');
        if (iA == -1) {
            this.f1937b = str.trim();
            return;
        }
        this.f1937b = str.substring(0, iA).trim();
        if (str.length() > iA + 1) {
            this.f1938c = str.substring(iA + 1);
        }
    }

    public String b() {
        return this.f1937b;
    }

    public int c() {
        return this.f1939d;
    }

    public boolean d() {
        return b() == null || b().equals("");
    }

    public String e() {
        return Q.a(b());
    }

    public String f() {
        try {
            return Q.b(b());
        } catch (Exception e2) {
            return "";
        }
    }

    public String toString() {
        String str = "";
        if (g() != null && !this.f1940e.equals("")) {
            str = str + "[" + g() + "]:";
        }
        if (c() > 0) {
            str = str + "[Line:" + c() + "]: ";
        }
        return b().trim().equals("") ? "BLANK ROW" : str + a();
    }

    public String g() {
        return this.f1940e;
    }
}
