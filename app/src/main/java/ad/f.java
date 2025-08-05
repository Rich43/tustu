package aD;

import jssc.SerialPort;

/* loaded from: TunerStudioMS.jar:aD/f.class */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private String f2334a;

    /* renamed from: b, reason: collision with root package name */
    private int f2335b;

    /* renamed from: c, reason: collision with root package name */
    private int f2336c;

    /* renamed from: d, reason: collision with root package name */
    private int f2337d;

    /* renamed from: e, reason: collision with root package name */
    private int f2338e;

    /* renamed from: f, reason: collision with root package name */
    private int f2339f;

    /* renamed from: g, reason: collision with root package name */
    private int f2340g;

    public f() {
        this("", SerialPort.BAUDRATE_115200, 0, 0, 8, 1, 0);
    }

    public f(String str, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.f2334a = str;
        this.f2335b = i2;
        this.f2336c = i3;
        this.f2337d = i4;
        this.f2338e = i5;
        this.f2339f = i6;
        this.f2340g = i7;
    }

    public void a(String str) {
        try {
            this.f2335b = Integer.parseInt(str);
        } catch (Exception e2) {
            this.f2335b = SerialPort.BAUDRATE_9600;
        }
    }

    public int a() {
        return this.f2335b;
    }

    public int b() {
        return this.f2336c;
    }

    public int c() {
        return this.f2337d;
    }

    public int d() {
        return this.f2338e;
    }

    public int e() {
        return this.f2339f;
    }

    public int f() {
        return this.f2340g;
    }
}
