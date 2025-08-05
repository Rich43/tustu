package bE;

import W.C0184j;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bE/a.class */
public class a implements p {

    /* renamed from: c, reason: collision with root package name */
    C0184j f6707c = new C0184j();

    /* renamed from: d, reason: collision with root package name */
    C0184j f6708d = new C0184j();

    /* renamed from: e, reason: collision with root package name */
    C0184j f6709e = new C0184j();

    /* renamed from: f, reason: collision with root package name */
    List f6710f = new ArrayList();

    public void a(float f2, float f3, float f4) {
        this.f6707c.a(f2);
        this.f6708d.a(f3);
        this.f6709e.a(f4);
    }

    public void a(double d2) {
        this.f6707c.d((float) d2);
    }

    public void b(double d2) {
        this.f6707c.e((float) d2);
    }

    public void c(double d2) {
        this.f6708d.d((float) d2);
    }

    public void d(double d2) {
        this.f6708d.e((float) d2);
    }

    public void e(double d2) {
        this.f6709e.d((float) d2);
    }

    public void f(double d2) {
        this.f6709e.e((float) d2);
    }

    @Override // bE.p
    public double a() {
        return this.f6707c.e();
    }

    @Override // bE.p
    public double b() {
        return this.f6707c.f();
    }

    @Override // bE.p
    public double c() {
        return this.f6708d.e();
    }

    @Override // bE.p
    public double d() {
        return this.f6708d.f();
    }

    @Override // bE.p
    public double h() {
        return this.f6709e.e();
    }

    @Override // bE.p
    public double i() {
        return this.f6709e.f();
    }

    @Override // bE.p
    public q a(int i2) {
        return 0 != 0 ? new c(this.f6707c.c(i2), this.f6708d.c(i2), this.f6709e.c(i2)) : new b(this.f6707c.c(i2), this.f6708d.c(i2), this.f6709e.c(i2));
    }

    @Override // bE.p
    public int e() {
        return this.f6708d.i();
    }

    @Override // bE.p
    public int f() {
        return 0;
    }

    @Override // bE.p
    public void a(l lVar) {
        this.f6710f.add(lVar);
    }

    @Override // bE.p
    public void b(l lVar) {
        this.f6710f.remove(lVar);
    }
}
