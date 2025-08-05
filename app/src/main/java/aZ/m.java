package aZ;

/* loaded from: TunerStudioMS.jar:aZ/m.class */
class m {

    /* renamed from: b, reason: collision with root package name */
    private int f4163b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f4164c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f4165d = -1;

    /* renamed from: e, reason: collision with root package name */
    private int f4166e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f4167f = 0;

    /* renamed from: g, reason: collision with root package name */
    private String f4168g = "";

    /* renamed from: h, reason: collision with root package name */
    private int f4169h = 0;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ j f4170a;

    m(j jVar) {
        this.f4170a = jVar;
    }

    public void a(int i2) {
        this.f4163b = i2 & 1023;
        this.f4166e = (i2 & 4095) >>> 12;
        d((i2 >>> 10) & 65532);
    }

    public int a() {
        return this.f4164c;
    }

    public void b(int i2) {
        this.f4164c = i2;
    }

    public int b() {
        return this.f4165d;
    }

    public void c(int i2) {
        this.f4165d = i2;
    }

    public int c() {
        return this.f4167f;
    }

    public void d(int i2) {
        this.f4167f = i2;
    }

    public int d() {
        return this.f4169h;
    }

    public void e(int i2) {
        this.f4169h = i2;
    }

    public String e() {
        return this.f4168g;
    }

    public void a(String str) {
        this.f4168g = str;
    }
}
