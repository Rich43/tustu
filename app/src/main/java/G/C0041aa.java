package G;

import java.io.Serializable;

/* renamed from: G.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/aa.class */
class C0041aa implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    private int f684b;

    /* renamed from: c, reason: collision with root package name */
    private int f685c;

    /* renamed from: d, reason: collision with root package name */
    private int[] f686d;

    /* renamed from: e, reason: collision with root package name */
    private long f687e;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Y f688a;

    private C0041aa(Y y2) {
        this.f688a = y2;
        this.f684b = -1;
        this.f685c = -1;
        this.f686d = null;
        this.f687e = System.currentTimeMillis();
    }

    public int a() {
        return this.f684b;
    }

    public void a(int i2) {
        this.f684b = i2;
    }

    public int b() {
        return this.f685c;
    }

    public void b(int i2) {
        this.f685c = i2;
    }

    public int[] c() {
        return this.f686d;
    }

    public void a(int[] iArr) {
        this.f686d = iArr;
    }

    public int d() {
        return (this.f684b * 10) ^ (4 + this.f685c);
    }

    public int e() {
        return (this.f684b * 10) ^ (((4 + this.f685c) + this.f686d.length) - 1);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof C0041aa) || obj == null) {
            return false;
        }
        C0041aa c0041aa = (C0041aa) obj;
        if (c0041aa.d() != d() || c0041aa.e() != e()) {
            return false;
        }
        for (int i2 = 0; i2 < c().length; i2++) {
            if (c()[i2] != c0041aa.c()[i2]) {
                return false;
            }
        }
        return true;
    }

    public long f() {
        return this.f687e;
    }

    public void a(long j2) {
        this.f687e = j2;
    }
}
