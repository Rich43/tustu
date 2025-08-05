package G;

/* loaded from: TunerStudioMS.jar:G/cN.class */
public class cN {

    /* renamed from: a, reason: collision with root package name */
    private static cN f1056a = null;

    /* renamed from: b, reason: collision with root package name */
    private cM f1057b = null;

    private cN() {
    }

    public static cN a() {
        if (f1056a == null) {
            f1056a = new cN();
        }
        return f1056a;
    }

    public cM b() {
        return this.f1057b;
    }

    public void a(cM cMVar) {
        this.f1057b = cMVar;
    }
}
