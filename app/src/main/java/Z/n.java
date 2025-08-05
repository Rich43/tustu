package z;

import jssc.SerialPort;

/* loaded from: TunerStudioMS.jar:z/n.class */
public class n {

    /* renamed from: a, reason: collision with root package name */
    private String f14133a;

    /* renamed from: b, reason: collision with root package name */
    private int f14134b;

    /* renamed from: c, reason: collision with root package name */
    private int f14135c;

    /* renamed from: d, reason: collision with root package name */
    private int f14136d;

    /* renamed from: e, reason: collision with root package name */
    private int f14137e;

    /* renamed from: f, reason: collision with root package name */
    private int f14138f;

    /* renamed from: g, reason: collision with root package name */
    private int f14139g;

    public n() {
        this("", SerialPort.BAUDRATE_9600, 0, 0, 8, 1, 0);
    }

    public n(String str, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.f14133a = str;
        this.f14134b = i2;
        this.f14135c = i3;
        this.f14136d = i4;
        this.f14137e = i5;
        this.f14138f = i6;
        this.f14139g = i7;
    }

    public void a(String str) {
        this.f14133a = str;
    }

    public String a() {
        return this.f14133a;
    }

    public void a(int i2) {
        this.f14134b = i2;
    }

    public void b(String str) {
        try {
            this.f14134b = Integer.parseInt(str);
        } catch (Exception e2) {
            this.f14134b = SerialPort.BAUDRATE_9600;
        }
    }

    public int b() {
        return this.f14134b;
    }

    public String c() {
        return Integer.toString(this.f14134b);
    }

    public int d() {
        return this.f14135c;
    }

    public int e() {
        return this.f14136d;
    }

    public void b(int i2) {
        this.f14137e = i2;
    }

    public int f() {
        return this.f14137e;
    }

    public void c(int i2) {
        this.f14138f = i2;
    }

    public int g() {
        return this.f14138f;
    }

    public void d(int i2) {
        this.f14139g = i2;
    }

    public int h() {
        return this.f14139g;
    }
}
