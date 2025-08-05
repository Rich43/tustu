package G;

/* renamed from: G.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/l.class */
public class C0129l extends Exception {

    /* renamed from: a, reason: collision with root package name */
    public static int f1278a = -1;

    /* renamed from: b, reason: collision with root package name */
    private int f1279b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f1280c;

    public C0129l(String str) {
        super(str);
        this.f1279b = f1278a;
        this.f1280c = true;
    }

    public void a(int i2) {
        this.f1279b = i2;
    }

    public int a() {
        return this.f1279b;
    }

    public void a(boolean z2) {
        this.f1280c = z2;
    }

    public boolean b() {
        return this.f1280c;
    }
}
