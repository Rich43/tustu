package G;

import java.io.Serializable;

/* renamed from: G.bp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bp.class */
public class C0083bp extends AbstractC0093bz implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f982a = null;

    /* renamed from: b, reason: collision with root package name */
    private cZ f983b = null;

    /* renamed from: c, reason: collision with root package name */
    private boolean f984c = false;

    /* renamed from: d, reason: collision with root package name */
    private boolean f985d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f986e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f987f = false;

    /* renamed from: g, reason: collision with root package name */
    private boolean f988g = false;

    /* renamed from: h, reason: collision with root package name */
    private int f989h = 0;

    /* renamed from: i, reason: collision with root package name */
    private int f990i = 0;

    @Override // G.AbstractC0093bz
    public String b() {
        return this.f982a;
    }

    public String[] a() {
        if (this.f983b != null && this.f987f) {
            return this.f983b.b();
        }
        if (this.f982a == null) {
            return null;
        }
        return new String[]{this.f982a};
    }

    public void a(String str) {
        this.f982a = str;
    }

    public boolean c() {
        return this.f984c;
    }

    public void a(boolean z2) {
        this.f984c = z2;
    }

    public boolean d() {
        return this.f985d;
    }

    public void b(boolean z2) {
        this.f985d = z2;
    }

    public int e() {
        return this.f989h;
    }

    public void a(int i2) {
        this.f989h = i2;
    }

    public boolean f() {
        return this.f986e;
    }

    public void c(boolean z2) {
        this.f986e = z2;
    }

    public int g() {
        return this.f990i;
    }

    public void b(int i2) {
        this.f990i = i2;
    }

    public void d(boolean z2) {
        this.f987f = z2;
    }

    public boolean h() {
        return this.f987f;
    }

    public void a(cZ cZVar) {
        this.f983b = cZVar;
    }

    public cZ i() {
        return this.f983b;
    }

    public boolean j() {
        return this.f988g;
    }

    public void e(boolean z2) {
        this.f988g = z2;
    }
}
