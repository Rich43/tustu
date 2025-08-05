package G;

/* renamed from: G.ch, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ch.class */
public class C0102ch {

    /* renamed from: a, reason: collision with root package name */
    private int f1122a;

    /* renamed from: b, reason: collision with root package name */
    private int f1123b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f1124c = 0;

    /* renamed from: d, reason: collision with root package name */
    private boolean f1125d = false;

    public C0102ch(int i2) {
        this.f1122a = i2;
    }

    public int a() {
        return this.f1122a;
    }

    public int b() {
        return this.f1123b;
    }

    public void a(int i2) {
        this.f1123b = i2;
    }

    public int c() {
        return this.f1124c;
    }

    public void b(int i2) {
        this.f1124c = i2;
    }

    public boolean a(int i2, int i3) {
        return i2 <= this.f1123b + this.f1124c && i2 + i3 > this.f1123b;
    }

    public void b(int i2, int i3) {
        int iMax = Math.max(this.f1123b + this.f1124c, i2 + i3);
        this.f1123b = Math.min(this.f1123b, i2);
        this.f1124c = iMax - this.f1123b;
    }

    public String toString() {
        return "page: " + this.f1122a + ", offset: " + this.f1123b + ", len: " + this.f1124c;
    }

    public boolean d() {
        return this.f1125d;
    }

    public void a(boolean z2) {
        this.f1125d = z2;
    }
}
