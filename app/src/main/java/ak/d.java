package aK;

import G.C0113cs;
import java.util.HashMap;

/* loaded from: TunerStudioMS.jar:aK/d.class */
public class d implements g {

    /* renamed from: a, reason: collision with root package name */
    float f2562a = 0.0f;

    /* renamed from: c, reason: collision with root package name */
    private f f2563c = null;

    /* renamed from: d, reason: collision with root package name */
    private f f2564d = null;

    /* renamed from: e, reason: collision with root package name */
    private f f2565e = null;

    /* renamed from: f, reason: collision with root package name */
    private int f2566f = 0;

    /* renamed from: g, reason: collision with root package name */
    private long f2567g = -1;

    /* renamed from: h, reason: collision with root package name */
    private long f2568h = -1;

    /* renamed from: i, reason: collision with root package name */
    private float f2569i = 0.0f;

    /* renamed from: j, reason: collision with root package name */
    private float f2570j = 0.0f;

    /* renamed from: k, reason: collision with root package name */
    private int f2571k = 0;

    /* renamed from: l, reason: collision with root package name */
    private int f2572l = 0;

    /* renamed from: b, reason: collision with root package name */
    e f2573b = null;

    @Override // aK.g
    public void a(f fVar) {
        if (this.f2564d == null) {
            this.f2564d = fVar;
        } else {
            this.f2564d = this.f2565e;
        }
        int i2 = this.f2566f;
        this.f2566f = i2 + 1;
        if (i2 == 5) {
            this.f2563c = fVar;
        }
        this.f2565e = fVar;
        t().a();
        if (this.f2567g > 0) {
            long jA = fVar.a();
            long j2 = jA - this.f2567g;
            if (this.f2569i > 0.0f) {
                this.f2569i = ((this.f2569i * 19.0f) + (1000.0f / j2)) / 20.0f;
                this.f2570j = (((fVar.e() - this.f2562a) / j2) + this.f2570j) / 2.0f;
            } else {
                this.f2569i = 1000.0f / j2;
                this.f2570j = (fVar.e() - this.f2562a) / j2;
            }
            this.f2567g = jA;
            this.f2568h = System.currentTimeMillis();
        } else {
            this.f2567g = fVar.a();
        }
        this.f2562a = fVar.e();
    }

    private e t() {
        if (this.f2573b == null) {
            this.f2573b = new e(this);
            this.f2573b.start();
        }
        return this.f2573b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u() {
        if (I.c.a().c()) {
            C0113cs.a().a(I.h.f1366d, o());
            C0113cs.a().a(I.h.f1364b, g());
            C0113cs.a().a(I.h.f1363a, f());
            C0113cs.a().a(I.h.f1365c, e());
            C0113cs.a().a(I.h.f1367e, n());
            C0113cs.a().a(I.h.f1368f, i());
            C0113cs.a().a(I.h.f1369g, h());
            C0113cs.a().a(I.h.f1370h, p());
            C0113cs.a().a(I.h.f1371i, j());
            C0113cs.a().a(I.h.f1372j, l());
            C0113cs.a().a(I.h.f1373k, m());
            C0113cs.a().a(I.h.f1374l, k());
            C0113cs.a().a(I.h.f1375m, 1.0d);
            C0113cs.a().a(I.h.f1376n, 1.0d);
            C0113cs.a().a(I.h.f1380r, s());
            C0113cs.a().a(I.h.f1378p, this.f2569i);
        }
    }

    @Override // aK.g
    public void a(String str) {
        this.f2569i = 0.0f;
    }

    @Override // aK.g
    public void b(String str) {
        this.f2569i = 0.0f;
    }

    @Override // aK.g
    public void a(String str, int i2, HashMap map) throws NumberFormatException {
        Object obj;
        if (i2 == a.f2537b) {
            C0113cs.a().a(I.h.f1376n, 1.0d);
            C0113cs.a().a(I.h.f1375m, 0.0d);
        } else if (i2 == a.f2536a || i2 == a.f2538c) {
            C0113cs.a().a(I.h.f1376n, 0.0d);
            this.f2569i = 0.0f;
            C0113cs.a().a(I.h.f1379q, 0.0d);
            this.f2572l = 0;
            C0113cs.a().a(I.h.f1378p, this.f2569i);
            C0113cs.a().a(I.h.f1375m, 0.0d);
        }
        if (map == null || !map.containsKey("satellites") || (obj = map.get("satellites")) == null) {
            return;
        }
        int i3 = Integer.parseInt(obj.toString());
        C0113cs.a().a(I.h.f1379q, i3);
        this.f2572l = i3;
    }

    public boolean a() {
        return this.f2565e != null;
    }

    f b() {
        return this.f2565e;
    }

    f c() {
        return this.f2563c;
    }

    public int d() {
        return this.f2566f;
    }

    public float e() {
        if (a()) {
            return b().e() + (this.f2570j * (this.f2568h > 0 ? (int) (System.currentTimeMillis() - this.f2568h) : 0));
        }
        return 0.0f;
    }

    public float f() {
        return e() * 2.2369363f;
    }

    public float g() {
        return e() * 3.6f;
    }

    public double h() {
        if (a()) {
            return b().c();
        }
        return Double.NaN;
    }

    public double i() {
        if (a()) {
            return b().b();
        }
        return Double.NaN;
    }

    public double j() {
        if (a()) {
            return b().g();
        }
        return Double.NaN;
    }

    public long k() {
        if (a()) {
            return b().a();
        }
        return 0L;
    }

    public float l() {
        if (!a() || c() == null) {
            return 0.0f;
        }
        return (float) (Math.cos(((b().b(c()) * 2.0f) * 3.141592653589793d) / 360.0d) * b().a(c()));
    }

    public float m() {
        if (!a() || c() == null) {
            return 0.0f;
        }
        return (float) (Math.sin(((b().b(c()) * 2.0f) * 3.141592653589793d) / 360.0d) * b().a(c()));
    }

    public double n() {
        if (a()) {
            return b().d() * 3.2808399200439453d;
        }
        return 0.0d;
    }

    public double o() {
        if (a()) {
            return b().d();
        }
        return 0.0d;
    }

    public float p() {
        if (a()) {
            return b().f();
        }
        return 0.0f;
    }

    public float q() {
        return this.f2569i;
    }

    public int r() {
        return this.f2572l;
    }

    public int s() {
        return this.f2571k;
    }
}
