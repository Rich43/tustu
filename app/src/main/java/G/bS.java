package G;

/* loaded from: TunerStudioMS.jar:G/bS.class */
public class bS {

    /* renamed from: a, reason: collision with root package name */
    private byte[] f867a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f868b = null;

    /* renamed from: c, reason: collision with root package name */
    private ae.m f869c = null;

    /* renamed from: d, reason: collision with root package name */
    private int f870d = -1;

    public byte[] a() {
        return this.f867a;
    }

    public void a(byte[] bArr) {
        this.f867a = bArr;
    }

    public void a(String str) {
        this.f867a = str.getBytes();
    }

    public String b() {
        if (this.f867a == null) {
            return null;
        }
        return this.f867a.length == 1 ? ((int) this.f867a[0]) + "" : bH.W.k(new String(this.f867a));
    }

    public static bS b(byte[] bArr) {
        bS bSVar = new bS();
        bSVar.a(bArr);
        return bSVar;
    }

    public String c() {
        return this.f868b == null ? this.f867a == null ? "Unknown" : b() : this.f868b;
    }

    public void b(String str) {
        this.f868b = str;
    }

    public String toString() {
        return "Signature:" + b() + ", firmware:" + this.f868b;
    }

    public boolean d() {
        byte[] bArrA = a();
        return bArrA != null && bArrA.length >= 3 && (bArrA[0] & 224) == 224 && (bArrA[1] & 240) == 0 && bArrA[2] == 62;
    }

    public ae.m e() {
        return this.f869c;
    }

    public void a(ae.m mVar) {
        this.f869c = mVar;
    }

    public boolean f() {
        return this.f867a != null && this.f867a.length == 3 && this.f867a[0] == -31;
    }

    public void a(int i2) {
        this.f870d = i2;
    }
}
