package t;

/* loaded from: TunerStudioMS.jar:t/bm.class */
class bm {

    /* renamed from: b, reason: collision with root package name */
    private String f13846b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f13847c = "";

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bi f13848a;

    public bm(bi biVar, String str, String str2) {
        this.f13848a = biVar;
        a(str);
        b(str2);
    }

    public void a(String str) {
        this.f13846b = str;
    }

    public String a() {
        return this.f13847c;
    }

    public void b(String str) {
        this.f13847c = str;
    }

    public String toString() {
        return this.f13846b == null ? this.f13847c : this.f13846b;
    }
}
