package G;

import java.io.Serializable;

/* renamed from: G.bk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bk.class */
public class C0078bk extends AbstractC0093bz implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    public static int f935a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f936b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static int f937c = 2;

    /* renamed from: d, reason: collision with root package name */
    public static int f938d = 4;

    /* renamed from: e, reason: collision with root package name */
    public static int f939e = 8;

    /* renamed from: f, reason: collision with root package name */
    public static int f940f = f936b + f937c;

    /* renamed from: g, reason: collision with root package name */
    private String f941g = null;

    /* renamed from: h, reason: collision with root package name */
    private int f942h = f935a;

    /* renamed from: i, reason: collision with root package name */
    private String f943i = "";

    @Override // G.AbstractC0093bz
    public String b() {
        return null;
    }

    public String a() {
        return this.f941g;
    }

    public void a(String str) {
        this.f941g = str;
    }

    public int c() {
        return this.f942h;
    }

    public void a(int i2) {
        this.f942h |= i2;
    }

    public boolean b(int i2) {
        return this.f942h > 0 && (this.f942h | i2) == this.f942h;
    }

    public String d() {
        return this.f943i;
    }

    public void b(String str) {
        this.f943i = str;
    }
}
