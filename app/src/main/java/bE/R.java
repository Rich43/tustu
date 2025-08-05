package be;

/* loaded from: TunerStudioMS.jar:be/R.class */
public class R {

    /* renamed from: a, reason: collision with root package name */
    public static int f7938a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f7939b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static int f7940c = 2;

    /* renamed from: d, reason: collision with root package name */
    private int f7941d = f7938a;

    /* renamed from: e, reason: collision with root package name */
    private String f7942e = null;

    public boolean a() {
        return (this.f7941d & f7939b) != 0;
    }

    public void b() {
        this.f7941d = f7939b;
    }

    public void c() {
        this.f7941d = f7940c;
    }

    public String d() {
        return this.f7942e;
    }

    public void a(String str) {
        this.f7942e = str;
    }
}
