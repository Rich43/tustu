package bT;

/* loaded from: TunerStudioMS.jar:bT/f.class */
class f {

    /* renamed from: b, reason: collision with root package name */
    private int f7581b = 0;

    /* renamed from: c, reason: collision with root package name */
    private int f7582c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f7583d = 0;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f7584a;

    f(e eVar) {
        this.f7584a = eVar;
    }

    public int a() {
        return this.f7581b;
    }

    public void a(int i2) {
        this.f7581b = i2;
    }

    public int b() {
        return this.f7582c;
    }

    public int c() {
        return this.f7583d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        if (this.f7582c == -1) {
            this.f7582c = i2;
            this.f7583d = i3;
        } else {
            int i4 = this.f7582c + this.f7583d;
            this.f7582c = Math.min(i2, this.f7582c);
            this.f7583d = Math.max(i2 + i3, i4) - this.f7582c;
        }
    }
}
