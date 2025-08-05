package J;

import G.F;

/* loaded from: TunerStudioMS.jar:J/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private static c f1420a = null;

    private c() {
    }

    public static c a() {
        if (f1420a == null) {
            f1420a = new c();
        }
        return f1420a;
    }

    public void a(F f2, int i2) {
        if (i2 > 0 && !(f2.D() instanceof f)) {
            f2.a(new f());
        }
        if (i2 <= 0 || (f2.C() instanceof g)) {
            return;
        }
        f2.a(new g());
    }
}
