package L;

/* loaded from: TunerStudioMS.jar:L/X.class */
public class X extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    private static long f1606a = System.currentTimeMillis();

    /* renamed from: b, reason: collision with root package name */
    private static boolean f1607b = false;

    /* renamed from: c, reason: collision with root package name */
    private static long f1608c = 0;

    public static long a() {
        return f1608c;
    }

    public static void a(long j2) {
        f1608c = j2;
    }

    public static boolean b() {
        return f1607b;
    }

    public static void a(boolean z2) {
        f1607b = z2;
    }

    public static long c() {
        return f1606a;
    }

    public static void b(long j2) {
        f1606a = j2;
    }

    public double a(ax.S s2) {
        return b() ? (a() - f1606a) / 1000.0d : (System.currentTimeMillis() - f1606a) / 1000.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "timeNow()";
    }
}
