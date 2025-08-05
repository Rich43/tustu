package bK;

/* loaded from: TunerStudioMS.jar:bK/b.class */
class b {

    /* renamed from: a, reason: collision with root package name */
    byte[] f7106a;

    /* renamed from: b, reason: collision with root package name */
    private char[] f7107b;

    /* renamed from: c, reason: collision with root package name */
    private int f7108c;

    /* renamed from: d, reason: collision with root package name */
    private int f7109d;

    public b(int i2) {
        this.f7106a = new byte[i2];
        this.f7107b = new char[i2];
        this.f7109d = 0;
        for (int i3 = 0; i3 < 256; i3++) {
            this.f7106a[i3] = (byte) i3;
        }
        this.f7109d = 257;
        this.f7108c = this.f7109d;
    }

    public void a() {
        this.f7109d = this.f7108c;
    }

    public int b() {
        return this.f7109d;
    }

    public char a(char c2, byte b2) {
        if (c2 >= this.f7109d) {
            throw new ArrayIndexOutOfBoundsException(Integer.toString(c2));
        }
        this.f7106a[this.f7109d] = b2;
        this.f7107b[this.f7109d] = c2;
        this.f7109d++;
        return (char) (this.f7109d - 1);
    }

    public void a(char c2, a aVar) {
        if (c2 >= this.f7109d) {
            throw new ArrayIndexOutOfBoundsException(Integer.toString(c2));
        }
        aVar.a();
        while (c2 > 256) {
            aVar.a(this.f7106a[c2]);
            c2 = this.f7107b[c2];
        }
        aVar.a(this.f7106a[c2]);
        aVar.d();
    }
}
