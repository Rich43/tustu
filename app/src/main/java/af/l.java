package af;

/* loaded from: TunerStudioMS.jar:af/l.class */
public class l {

    /* renamed from: a, reason: collision with root package name */
    private int f4489a;

    /* renamed from: b, reason: collision with root package name */
    private int f4490b = 65535;

    /* renamed from: c, reason: collision with root package name */
    private int f4491c = 0;

    public l(int i2) {
        this.f4489a = i2;
    }

    public void a(int i2) {
        int i3 = i2 & 65535;
        int i4 = (i3 / 1024) * 1024;
        int i5 = ((i3 + 1024) / 1024) * 1024;
        if (i4 < this.f4490b) {
            this.f4490b = i4;
        }
        if (i5 > this.f4491c) {
            this.f4491c = i5;
        }
    }

    public int a() {
        return this.f4489a;
    }

    public int b() {
        return this.f4490b;
    }

    public int c() {
        return this.f4491c;
    }
}
