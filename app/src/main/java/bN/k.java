package bN;

import bH.C;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:bN/k.class */
public class k {

    /* renamed from: b, reason: collision with root package name */
    private long f7230b = 0;

    /* renamed from: d, reason: collision with root package name */
    private int f7232d = 1;

    /* renamed from: e, reason: collision with root package name */
    private int f7233e = 1;

    /* renamed from: f, reason: collision with root package name */
    private int f7234f = 0;

    /* renamed from: g, reason: collision with root package name */
    private int f7235g = 1;

    /* renamed from: h, reason: collision with root package name */
    private int f7236h = 50;

    /* renamed from: i, reason: collision with root package name */
    private int f7237i = ((int) Math.pow(2.0d, 16.0d)) - 1;

    /* renamed from: j, reason: collision with root package name */
    private int f7238j = 10000;

    /* renamed from: k, reason: collision with root package name */
    private boolean f7239k = false;

    /* renamed from: l, reason: collision with root package name */
    private int f7240l = 10;

    /* renamed from: m, reason: collision with root package name */
    private int f7241m = 1;

    /* renamed from: n, reason: collision with root package name */
    private int f7242n = 55;

    /* renamed from: o, reason: collision with root package name */
    private int f7243o = 255;

    /* renamed from: p, reason: collision with root package name */
    private int f7244p = 0;

    /* renamed from: q, reason: collision with root package name */
    private boolean f7245q = false;

    /* renamed from: r, reason: collision with root package name */
    private boolean f7246r = false;

    /* renamed from: s, reason: collision with root package name */
    private int f7247s = 250;

    /* renamed from: t, reason: collision with root package name */
    private int f7248t;

    /* renamed from: u, reason: collision with root package name */
    private final bT.i f7249u;

    /* renamed from: v, reason: collision with root package name */
    private int f7250v;

    /* renamed from: w, reason: collision with root package name */
    private final j f7251w;

    /* renamed from: x, reason: collision with root package name */
    private final a f7252x;

    /* renamed from: y, reason: collision with root package name */
    private int f7253y;

    /* renamed from: z, reason: collision with root package name */
    private int f7254z;

    /* renamed from: A, reason: collision with root package name */
    private int f7255A;

    /* renamed from: B, reason: collision with root package name */
    private int f7256B;

    /* renamed from: C, reason: collision with root package name */
    private int f7257C;

    /* renamed from: D, reason: collision with root package name */
    private int f7258D;

    /* renamed from: E, reason: collision with root package name */
    private int f7259E;

    /* renamed from: a, reason: collision with root package name */
    private static boolean f7229a = false;

    /* renamed from: c, reason: collision with root package name */
    private static long f7231c = -1;

    public k() {
        this.f7248t = f7229a ? 2000 : 1000;
        this.f7249u = new bT.i();
        this.f7250v = 0;
        this.f7251w = new j();
        this.f7252x = new a();
        this.f7253y = 50;
        this.f7254z = 25;
        this.f7255A = 25;
        this.f7256B = 25;
        this.f7257C = 25;
        this.f7258D = 25;
        this.f7259E = 25;
    }

    public static void a(boolean z2) {
        f7229a = z2;
    }

    public int a() {
        return this.f7244p;
    }

    public void a(int i2) {
        this.f7244p = i2;
    }

    public int b() {
        return this.f7241m;
    }

    public void b(int i2) {
        this.f7241m = i2;
        C.d("Setting Transport");
        if (i2 == 2) {
            c(2);
            d(2);
            e(0);
        } else {
            if (i2 != 1) {
                throw new IOException("Unsupported XCP Transport: " + i2);
            }
            c(1);
            d(1);
            e(1);
        }
        bS.d.a().b(this);
    }

    public int c() {
        return this.f7232d;
    }

    public void c(int i2) {
        this.f7232d = i2;
    }

    public int d() {
        return this.f7233e;
    }

    public void d(int i2) {
        this.f7233e = i2;
    }

    public int e() {
        return this.f7234f;
    }

    public int f() {
        return this.f7235g;
    }

    public void e(int i2) {
        this.f7235g = i2;
    }

    public boolean g() {
        return this.f7252x.c();
    }

    public int h() {
        return this.f7238j;
    }

    public int i() {
        return this.f7236h;
    }

    public void f(int i2) {
        this.f7236h = i2;
    }

    public int j() {
        return this.f7237i;
    }

    public void g(int i2) {
        this.f7237i = i2;
    }

    public int k() {
        return this.f7242n;
    }

    public void h(int i2) {
        this.f7242n = i2;
    }

    public int l() {
        return this.f7243o;
    }

    public boolean m() {
        return this.f7246r;
    }

    public int n() {
        return this.f7240l;
    }

    public bT.i o() {
        return this.f7249u;
    }

    public j p() {
        return this.f7251w;
    }

    public int q() {
        return this.f7248t;
    }

    public int r() {
        return this.f7253y;
    }

    public int s() {
        return this.f7259E;
    }

    public a t() {
        return this.f7252x;
    }

    public int u() {
        return this.f7252x.b();
    }

    public static float v() {
        if (f7231c == -1) {
            f7231c = System.currentTimeMillis();
        }
        return (System.currentTimeMillis() - f7231c) / 1000.0f;
    }

    public long w() {
        if (this.f7230b <= 0) {
            this.f7230b = System.nanoTime();
        }
        return System.nanoTime() - this.f7230b;
    }
}
