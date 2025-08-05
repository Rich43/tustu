package bR;

import G.R;

/* loaded from: TunerStudioMS.jar:bR/m.class */
class m {

    /* renamed from: b, reason: collision with root package name */
    private R f7528b;

    /* renamed from: c, reason: collision with root package name */
    private int f7529c;

    /* renamed from: d, reason: collision with root package name */
    private int f7530d;

    /* renamed from: e, reason: collision with root package name */
    private int f7531e;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f7532a;

    m(k kVar, R r2, int i2) {
        this.f7532a = kVar;
        this.f7530d = 0;
        this.f7531e = Integer.MAX_VALUE;
        this.f7528b = r2;
        this.f7529c = i2;
    }

    m(k kVar, R r2, int i2, int i3, int i4) {
        this.f7532a = kVar;
        this.f7530d = 0;
        this.f7531e = Integer.MAX_VALUE;
        this.f7528b = r2;
        this.f7529c = i2;
        this.f7530d = i3;
        this.f7531e = i4;
    }

    public R a() {
        return this.f7528b;
    }

    public int b() {
        return this.f7529c;
    }

    public int c() {
        return this.f7530d;
    }

    public int d() {
        return this.f7531e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean e() {
        return this.f7530d == 0 && this.f7531e == Integer.MAX_VALUE;
    }

    public boolean a(R r2, int i2, int i3, int i4) {
        return r2.equals(this.f7528b) && i2 == this.f7529c && (e() || (this.f7530d <= i3 && this.f7530d + this.f7531e >= i3 + i4));
    }
}
