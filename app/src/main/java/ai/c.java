package aI;

/* loaded from: TunerStudioMS.jar:aI/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private int f2428a = 0;

    /* renamed from: b, reason: collision with root package name */
    private int f2429b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f2430c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f2431d = -1;

    /* renamed from: e, reason: collision with root package name */
    private int f2432e = -1;

    /* renamed from: f, reason: collision with root package name */
    private int f2433f = -1;

    /* renamed from: g, reason: collision with root package name */
    private int f2434g = -1;

    /* renamed from: h, reason: collision with root package name */
    private long f2435h = -1;

    public int a() {
        return this.f2431d;
    }

    public int b() {
        return a() + (e() * f());
    }

    public int c() {
        return this.f2428a;
    }

    public void a(int i2) {
        this.f2428a = i2;
    }

    public int d() {
        return this.f2429b;
    }

    public void b(int i2) {
        this.f2429b = i2;
    }

    public void c(int i2) {
        this.f2430c = i2;
    }

    public void d(int i2) {
        this.f2431d = i2;
    }

    public int e() {
        return this.f2432e;
    }

    public void e(int i2) {
        this.f2432e = i2;
    }

    public int f() {
        return this.f2433f;
    }

    public void f(int i2) {
        this.f2433f = i2;
    }

    public void g(int i2) {
        this.f2434g = i2;
    }

    public String toString() {
        return "SD Format Info: FAT" + this.f2428a + ", bytesPerSector = 0x" + Integer.toHexString(this.f2429b).toUpperCase() + ", numberReservedSectors = 0x" + Integer.toHexString(this.f2431d).toUpperCase() + ", numberOfFileAllocTables = 0x" + Integer.toHexString(this.f2432e).toUpperCase() + ", sectorsPerFileAllocTable = 0x" + Integer.toHexString(this.f2433f).toUpperCase() + ", totalLogicalSectors = 0x" + Integer.toHexString(this.f2434g).toUpperCase() + ", getFatStartSector() = 0x" + Integer.toHexString(a()).toUpperCase() + ", getFatSectorEnd() = 0x" + Integer.toHexString(b()).toUpperCase() + ", partitionStartCluster = 0x" + Long.toHexString(this.f2435h).toUpperCase();
    }

    public long g() {
        return this.f2435h;
    }

    public void a(long j2) {
        this.f2435h = j2;
    }
}
