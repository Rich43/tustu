package bT;

import G.F;
import G.aF;
import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bT/b.class */
public class b implements aF {

    /* renamed from: g, reason: collision with root package name */
    private bO.c f7555g;

    /* renamed from: a, reason: collision with root package name */
    F f7556a;

    /* renamed from: h, reason: collision with root package name */
    private bN.r f7557h;

    /* renamed from: b, reason: collision with root package name */
    bO.a f7558b;

    /* renamed from: c, reason: collision with root package name */
    bN.k f7559c;

    /* renamed from: d, reason: collision with root package name */
    c f7560d = null;

    /* renamed from: e, reason: collision with root package name */
    byte[] f7561e = null;

    /* renamed from: f, reason: collision with root package name */
    long f7562f = 0;

    public b(F f2, bN.r rVar, bO.a aVar) {
        this.f7556a = f2;
        this.f7557h = rVar;
        this.f7558b = aVar;
        this.f7559c = rVar.d();
    }

    public bO.c a() {
        return this.f7555g;
    }

    public void a(bO.c cVar) {
        this.f7555g = cVar;
    }

    public void b() {
        if (this.f7560d != null) {
            this.f7560d.b();
        }
    }

    public void c() {
        b();
        this.f7561e = null;
        this.f7560d = new c(this);
        this.f7560d.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x005d A[Catch: a -> 0x0068, TryCatch #1 {a -> 0x0068, blocks: (B:8:0x003b, B:10:0x004b, B:11:0x005d), top: B:33:0x003b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void e() {
        /*
            Method dump skipped, instructions count: 287
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bT.b.e():void");
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (str.equals(this.f7556a.u())) {
            if (this.f7561e == null || bArr.length != this.f7561e.length) {
                this.f7561e = new byte[bArr.length];
            }
            System.arraycopy(bArr, 0, this.f7561e, 0, bArr.length);
            if (this.f7560d != null) {
                this.f7560d.a();
            }
        }
    }

    public byte[] d() {
        byte[] bArr = new byte[this.f7558b.c().d().a()];
        long jW = this.f7559c.w();
        if (this.f7562f <= 0) {
            this.f7562f = jW;
        }
        long j2 = jW - this.f7562f;
        this.f7562f = jW;
        return C0995c.a((long) (j2 * Math.pow(10.0d, this.f7558b.c().d().c() - 9)), bArr, true);
    }
}
