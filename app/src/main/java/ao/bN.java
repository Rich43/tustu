package ao;

/* loaded from: TunerStudioMS.jar:ao/bN.class */
class bN {

    /* renamed from: a, reason: collision with root package name */
    String f5338a;

    /* renamed from: b, reason: collision with root package name */
    String f5339b;

    /* renamed from: c, reason: collision with root package name */
    String f5340c;

    /* renamed from: d, reason: collision with root package name */
    int f5341d;

    /* renamed from: e, reason: collision with root package name */
    Object f5342e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ bM f5343f;

    bN(bM bMVar, String str, String str2, String str3, Object obj, int i2) {
        this.f5343f = bMVar;
        this.f5338a = "";
        this.f5339b = "";
        this.f5340c = "";
        this.f5341d = 0;
        this.f5342e = null;
        this.f5339b = str;
        this.f5338a = str2;
        this.f5342e = obj;
        this.f5340c = str3;
        this.f5341d = i2;
    }

    public Object a() {
        return this.f5342e;
    }

    public String b() {
        return this.f5339b;
    }

    public String c() {
        return this.f5340c;
    }

    public String toString() {
        return "[name=" + this.f5339b + ", page=" + this.f5338a + ", units=" + this.f5340c + ", data=" + this.f5342e + "] ";
    }
}
