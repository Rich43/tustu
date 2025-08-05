package aJ;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:aJ/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private int f2529a = 0;

    /* renamed from: b, reason: collision with root package name */
    private a f2530b = null;

    /* renamed from: c, reason: collision with root package name */
    private int f2531c = -1;

    /* renamed from: d, reason: collision with root package name */
    private a f2532d = null;

    /* renamed from: e, reason: collision with root package name */
    private long f2533e = -1;

    /* renamed from: f, reason: collision with root package name */
    private long f2534f = -1;

    public void a(byte[] bArr) {
        if (bArr.length != 16) {
            throw new IndexOutOfBoundsException("Partition Entry expected to be 16 in length");
        }
        this.f2529a = C0995c.a(bArr[0]);
        this.f2530b = new a();
        this.f2530b.a(C0995c.a(bArr, 1, 3, false, false));
        this.f2531c = C0995c.a(bArr[4]);
        this.f2532d = new a();
        this.f2532d.a(C0995c.a(bArr, 5, 3, false, false));
        this.f2533e = C0995c.b(bArr, 8, 4, false, false);
        this.f2534f = C0995c.b(bArr, 12, 4, false, false);
    }

    public long a() {
        return this.f2533e;
    }

    public String toString() {
        return "status= " + this.f2529a + "\n\t firstSector=(" + ((Object) this.f2530b) + ")\n\t partitionType=" + this.f2531c + "\n\t lastSector=(" + ((Object) this.f2532d) + ")\n\t lbaFirstSectorInPartition=" + this.f2533e + "\n\t numSectorsInPartition=" + this.f2534f;
    }
}
