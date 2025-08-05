package L;

/* renamed from: L.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/y.class */
public class C0168y extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    public static long f1727a = -1;

    public double a(ax.S s2) {
        if (f1727a >= 0) {
            return (System.currentTimeMillis() - f1727a) / 1000.0d;
        }
        return -1.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public static void a() {
        f1727a = System.currentTimeMillis();
    }

    public static void b() {
        f1727a = -1L;
    }

    public static double c() {
        return (System.currentTimeMillis() - f1727a) / 1000.0d;
    }

    public String toString() {
        return "getLogTime()";
    }
}
