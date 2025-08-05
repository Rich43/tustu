package ao;

/* loaded from: TunerStudioMS.jar:ao/eX.class */
public class eX {

    /* renamed from: b, reason: collision with root package name */
    private String f5602b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f5603c = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5604a;

    public eX(C0737eu c0737eu, String str, String str2) {
        this.f5604a = c0737eu;
        a(str);
        b(str2);
    }

    public String toString() {
        return this.f5602b != null ? this.f5602b : this.f5603c;
    }

    public void a(String str) {
        this.f5602b = str;
    }

    public String a() {
        return this.f5603c;
    }

    public void b(String str) {
        this.f5603c = str;
    }

    public boolean equals(Object obj) {
        return obj instanceof eX ? ((eX) obj).a().equals(this.f5603c) : obj instanceof String ? obj.equals(this.f5603c) : super.equals(obj);
    }
}
