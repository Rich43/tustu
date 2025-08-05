package ak;

import W.InterfaceC0185k;

/* renamed from: ak.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/c.class */
public class C0543c implements W.T {

    /* renamed from: b, reason: collision with root package name */
    private String f4811b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f4812c = "";

    /* renamed from: d, reason: collision with root package name */
    private String f4813d = null;

    /* renamed from: e, reason: collision with root package name */
    private int f4814e = -1;

    /* renamed from: f, reason: collision with root package name */
    private int f4815f = 0;

    /* renamed from: g, reason: collision with root package name */
    private float f4816g = 1.0f;

    /* renamed from: h, reason: collision with root package name */
    private float f4817h = 0.0f;

    /* renamed from: i, reason: collision with root package name */
    private float f4818i = Float.NaN;

    /* renamed from: j, reason: collision with root package name */
    private float f4819j = Float.NaN;

    /* renamed from: a, reason: collision with root package name */
    InterfaceC0185k f4820a = null;

    public C0543c() {
    }

    public C0543c(String str, String str2) {
        a(str);
        b(str2);
    }

    @Override // W.T
    public String a() {
        return this.f4811b;
    }

    public void a(String str) {
        this.f4811b = str;
    }

    @Override // W.T
    public String b() {
        return this.f4812c;
    }

    public void b(String str) {
        String strReplace = str.replace("�", "°");
        if (strReplace.length() > 8) {
            strReplace = strReplace.substring(0, 8);
        }
        this.f4812c = strReplace;
    }

    @Override // W.T
    public int e() {
        return this.f4814e;
    }

    public void a(int i2) {
        this.f4814e = i2;
    }

    @Override // W.T
    public int f() {
        return this.f4815f;
    }

    public void b(int i2) {
        this.f4815f = i2;
    }

    @Override // W.T
    public float d() {
        return this.f4816g;
    }

    @Override // W.T
    public float c() {
        return this.f4817h;
    }

    public void a(float f2) {
        this.f4816g = f2;
    }

    public void b(float f2) {
        this.f4817h = f2;
    }

    public void a(InterfaceC0185k interfaceC0185k) {
        this.f4820a = interfaceC0185k;
    }

    @Override // W.T
    public InterfaceC0185k g() {
        return this.f4820a;
    }

    public String toString() {
        return "BasicLogFieldDescriptor{headerName=" + this.f4811b + ", units='" + this.f4812c + "'}";
    }

    @Override // W.T
    public float h() {
        return this.f4818i;
    }

    public void c(float f2) {
        this.f4818i = f2;
    }

    @Override // W.T
    public float i() {
        return this.f4819j;
    }

    public void d(float f2) {
        this.f4819j = f2;
    }

    @Override // W.T
    public String j() {
        return this.f4813d;
    }

    public void c(String str) {
        this.f4813d = str;
    }
}
