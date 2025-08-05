package r;

import i.C1743c;

/* renamed from: r.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/m.class */
public class C1810m {

    /* renamed from: a, reason: collision with root package name */
    private static C1810m f13500a = null;

    /* renamed from: b, reason: collision with root package name */
    private boolean f13501b = false;

    private C1810m() {
    }

    public static C1810m a() {
        if (f13500a == null) {
            f13500a = new C1810m();
        }
        return f13500a;
    }

    public boolean b() {
        return this.f13501b || C1743c.a().d();
    }

    public void a(boolean z2) {
        this.f13501b = z2;
        if (z2) {
            C1743c.a().b();
        } else {
            C1743c.a().c();
        }
    }
}
