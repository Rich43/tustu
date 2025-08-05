package bO;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bO/f.class */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private final g f7361a = new g();

    /* renamed from: b, reason: collision with root package name */
    private int f7362b = 0;

    /* renamed from: c, reason: collision with root package name */
    private int f7363c = 0;

    /* renamed from: d, reason: collision with root package name */
    private int f7364d = 0;

    /* renamed from: e, reason: collision with root package name */
    private b f7365e = new b();

    public void a(byte b2) {
        this.f7361a.a(b2);
    }

    public void b(byte b2) {
        this.f7365e.a(b2);
    }

    public g a() {
        return this.f7361a;
    }

    public int b() {
        return this.f7362b;
    }

    public void a(int i2) {
        this.f7362b = i2;
    }

    public int c() {
        return this.f7363c;
    }

    public void b(int i2) {
        this.f7363c = i2;
    }

    public int d() {
        return this.f7364d;
    }

    public void c(int i2) {
        this.f7364d = i2;
    }

    public b e() {
        return this.f7365e;
    }

    public String toString() {
        return "maxDaq=" + this.f7362b + ", minDaq=" + this.f7364d + ", maxEventChannels=" + this.f7363c + ", keyByte=" + Integer.toBinaryString(C0995c.a(this.f7365e.b())) + ", daqProperties=" + Integer.toBinaryString(C0995c.a(this.f7361a.a()));
    }
}
