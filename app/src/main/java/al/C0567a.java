package al;

import am.C0576d;
import am.k;
import an.AbstractC0578a;
import java.util.List;

/* renamed from: al.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:al/a.class */
public class C0567a {

    /* renamed from: b, reason: collision with root package name */
    private C0576d f4920b;

    /* renamed from: c, reason: collision with root package name */
    private String f4921c;

    /* renamed from: d, reason: collision with root package name */
    private String f4922d;

    /* renamed from: a, reason: collision with root package name */
    AbstractC0578a f4923a;

    public C0567a(C0576d c0576d) {
        this.f4922d = null;
        this.f4920b = c0576d;
        this.f4921c = c0576d.o().e();
        if (c0576d.q() instanceof k) {
            this.f4922d = ((k) c0576d.q()).e();
        }
        this.f4923a = AbstractC0578a.a(c0576d);
    }

    public double a(byte[] bArr) {
        return this.f4923a.a(bArr);
    }

    public C0576d a() {
        return this.f4920b;
    }

    public List b() {
        return this.f4923a.a();
    }

    public double c() {
        return this.f4923a.a(this.f4920b.l());
    }

    public double d() {
        return this.f4923a.a(this.f4920b.m());
    }

    public String e() {
        return this.f4921c;
    }

    public String f() {
        return this.f4922d;
    }

    public int g() {
        if ((this.f4920b.i() & 4) != 0) {
            return this.f4920b.j();
        }
        return -1;
    }
}
