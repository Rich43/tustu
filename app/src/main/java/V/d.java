package V;

/* loaded from: TunerStudioMS.jar:V/d.class */
public class d extends Exception {

    /* renamed from: a, reason: collision with root package name */
    public static int f1900a = 2;

    /* renamed from: b, reason: collision with root package name */
    public static int f1901b = 1;

    /* renamed from: c, reason: collision with root package name */
    private int f1902c;

    /* renamed from: d, reason: collision with root package name */
    private int[] f1903d;

    /* renamed from: e, reason: collision with root package name */
    private int f1904e;

    /* renamed from: f, reason: collision with root package name */
    private int f1905f;

    /* renamed from: g, reason: collision with root package name */
    private String f1906g;

    /* renamed from: h, reason: collision with root package name */
    private String f1907h;

    public d(String str) {
        super(str);
        this.f1902c = 0;
        this.f1903d = null;
        this.f1904e = 0;
        this.f1905f = f1900a;
        this.f1906g = null;
        this.f1907h = null;
        this.f1906g = str;
    }

    public int a() {
        return this.f1902c;
    }

    public void a(int i2) {
        this.f1902c = i2;
    }

    public void a(int[] iArr) {
        this.f1903d = iArr;
    }

    public void b(int i2) {
        this.f1904e = i2;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.f1906g;
    }

    public void a(String str) {
        this.f1906g = str;
    }

    public void b(String str) {
        this.f1907h = str;
    }

    public String b() {
        return this.f1907h;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "Error Code: 0x" + Integer.toHexString(this.f1902c).toUpperCase() + ", message: " + this.f1906g;
    }
}
