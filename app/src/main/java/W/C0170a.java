package W;

import bH.C0995c;

/* renamed from: W.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/a.class */
public class C0170a implements T {

    /* renamed from: a, reason: collision with root package name */
    private String f2021a = "";

    /* renamed from: b, reason: collision with root package name */
    private String f2022b = "";

    /* renamed from: c, reason: collision with root package name */
    private int f2023c = 0;

    /* renamed from: d, reason: collision with root package name */
    private int f2024d = 0;

    /* renamed from: e, reason: collision with root package name */
    private int f2025e = 0;

    /* renamed from: f, reason: collision with root package name */
    private float f2026f = 0.0f;

    /* renamed from: g, reason: collision with root package name */
    private float f2027g = 1.0f;

    /* renamed from: h, reason: collision with root package name */
    private int f2028h = 0;

    /* renamed from: i, reason: collision with root package name */
    private String f2029i = null;

    /* renamed from: j, reason: collision with root package name */
    private int f2030j = 50;

    /* renamed from: k, reason: collision with root package name */
    private int f2031k = -1;

    @Override // W.T
    public String a() {
        return this.f2021a;
    }

    public void a(String str) {
        this.f2021a = str;
    }

    @Override // W.T
    public String b() {
        return this.f2022b;
    }

    public void b(String str) {
        this.f2022b = str;
    }

    public void a(int i2) {
        this.f2023c = i2;
    }

    public void b(int i2) {
        this.f2024d = i2;
    }

    public void c(int i2) {
        this.f2025e = i2;
    }

    @Override // W.T
    public float c() {
        return this.f2026f;
    }

    public void a(double d2) {
        this.f2026f = (float) d2;
    }

    @Override // W.T
    public float d() {
        return this.f2027g;
    }

    public void b(double d2) {
        this.f2027g = (float) d2;
    }

    @Override // W.T
    public int e() {
        return this.f2031k;
    }

    public void d(int i2) {
        this.f2031k = i2;
    }

    public float a(byte[] bArr) {
        return (C0995c.a(bArr, this.f2024d, this.f2025e, this.f2023c == 0, false) * this.f2027g) + this.f2026f;
    }

    @Override // W.T
    public int f() {
        return this.f2028h;
    }

    public void e(int i2) {
        this.f2028h = i2;
    }

    @Override // W.T
    public InterfaceC0185k g() {
        return null;
    }

    @Override // W.T
    public float h() {
        return Float.NaN;
    }

    @Override // W.T
    public float i() {
        return Float.NaN;
    }

    @Override // W.T
    public String j() {
        return this.f2029i;
    }
}
