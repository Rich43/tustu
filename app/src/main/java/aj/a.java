package aJ;

/* loaded from: TunerStudioMS.jar:aJ/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private int f2527a = -1;

    public int a() {
        return this.f2527a >> 16;
    }

    public int b() {
        return (this.f2527a >> 8) & 63;
    }

    public int c() {
        return this.f2527a & 255;
    }

    public void a(int i2) {
        this.f2527a = i2;
    }

    public String toString() {
        return "Head: " + a() + ", sector: " + b() + ", cylinder: " + c();
    }
}
