package bK;

/* loaded from: TunerStudioMS.jar:bK/a.class */
class a {

    /* renamed from: a, reason: collision with root package name */
    private byte[] f7104a;

    /* renamed from: b, reason: collision with root package name */
    private int f7105b;

    public a(int i2) {
        this.f7104a = new byte[i2];
    }

    public void a() {
        this.f7105b = 0;
    }

    public int b() {
        return this.f7105b;
    }

    public byte[] c() {
        return this.f7104a;
    }

    public void a(byte b2) {
        if (this.f7105b >= this.f7104a.length) {
            byte[] bArr = this.f7104a;
            this.f7104a = new byte[bArr.length + 16];
            System.arraycopy(bArr, 0, this.f7104a, 0, this.f7105b);
        }
        byte[] bArr2 = this.f7104a;
        int i2 = this.f7105b;
        this.f7105b = i2 + 1;
        bArr2[i2] = b2;
    }

    public void d() {
        for (int i2 = 0; i2 < this.f7105b / 2; i2++) {
            byte b2 = this.f7104a[i2];
            this.f7104a[i2] = this.f7104a[(this.f7105b - 1) - i2];
            this.f7104a[(this.f7105b - 1) - i2] = b2;
        }
    }
}
