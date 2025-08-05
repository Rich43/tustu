package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import org.icepdf.core.util.PdfOps;

/* renamed from: am.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:am/d.class */
public class C0576d extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f4979b = "##CN";

    /* renamed from: c, reason: collision with root package name */
    private long f4980c;

    /* renamed from: d, reason: collision with root package name */
    private long f4981d;

    /* renamed from: e, reason: collision with root package name */
    private long f4982e;

    /* renamed from: f, reason: collision with root package name */
    private long f4983f;

    /* renamed from: g, reason: collision with root package name */
    private long f4984g;

    /* renamed from: h, reason: collision with root package name */
    private long f4985h;

    /* renamed from: i, reason: collision with root package name */
    private long f4986i;

    /* renamed from: j, reason: collision with root package name */
    private long f4987j;

    /* renamed from: k, reason: collision with root package name */
    private long[] f4988k;

    /* renamed from: l, reason: collision with root package name */
    private long[] f4989l;

    /* renamed from: m, reason: collision with root package name */
    private byte f4990m;

    /* renamed from: n, reason: collision with root package name */
    private byte f4991n;

    /* renamed from: o, reason: collision with root package name */
    private byte f4992o;

    /* renamed from: p, reason: collision with root package name */
    private byte f4993p;

    /* renamed from: q, reason: collision with root package name */
    private long f4994q;

    /* renamed from: r, reason: collision with root package name */
    private long f4995r;

    /* renamed from: s, reason: collision with root package name */
    private long f4996s;

    /* renamed from: t, reason: collision with root package name */
    private long f4997t;

    /* renamed from: u, reason: collision with root package name */
    private byte f4998u;

    /* renamed from: v, reason: collision with root package name */
    private int f4999v;

    /* renamed from: w, reason: collision with root package name */
    private double f5000w;

    /* renamed from: x, reason: collision with root package name */
    private double f5001x;

    /* renamed from: y, reason: collision with root package name */
    private double f5002y;

    /* renamed from: z, reason: collision with root package name */
    private double f5003z;

    /* renamed from: A, reason: collision with root package name */
    private double f5004A;

    /* renamed from: B, reason: collision with root package name */
    private double f5005B;

    private C0576d(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
    }

    public byte e() {
        return this.f4992o;
    }

    public byte f() {
        return this.f4993p;
    }

    public long g() {
        return this.f4994q;
    }

    public long h() {
        return this.f4995r;
    }

    public long i() {
        return this.f4996s;
    }

    public byte j() {
        return this.f4998u;
    }

    public int k() {
        return this.f4999v;
    }

    public double l() {
        return this.f5000w;
    }

    public double m() {
        return this.f5001x;
    }

    private void c(long j2) {
        this.f4980c = j2;
    }

    private void d(long j2) {
        this.f4981d = j2;
    }

    private void e(long j2) {
        this.f4982e = j2;
    }

    private void f(long j2) {
        this.f4983f = j2;
    }

    private void g(long j2) {
        this.f4984g = j2;
    }

    private void h(long j2) {
        this.f4985h = j2;
    }

    private void i(long j2) {
        this.f4986i = j2;
    }

    private void j(long j2) {
        this.f4987j = j2;
    }

    private void a(long[] jArr) {
        this.f4988k = jArr;
    }

    private void b(long[] jArr) {
        this.f4989l = jArr;
    }

    private void a(byte b2) {
        this.f4990m = b2;
    }

    private void b(byte b2) {
        this.f4991n = b2;
    }

    private void c(byte b2) {
        this.f4992o = b2;
    }

    private void d(byte b2) {
        this.f4993p = b2;
    }

    private void k(long j2) {
        this.f4994q = j2;
    }

    private void l(long j2) {
        this.f4995r = j2;
    }

    private void m(long j2) {
        this.f4996s = j2;
    }

    private void n(long j2) {
        this.f4997t = j2;
    }

    private void e(byte b2) {
        this.f4998u = b2;
    }

    private void a(int i2) {
        this.f4999v = i2;
    }

    private void a(double d2) {
        this.f5000w = d2;
    }

    private void b(double d2) {
        this.f5001x = d2;
    }

    private void c(double d2) {
        this.f5002y = d2;
    }

    private void d(double d2) {
        this.f5003z = d2;
    }

    private void e(double d2) {
        this.f5004A = d2;
    }

    private void f(double d2) {
        this.f5005B = d2;
    }

    public C0576d n() {
        if (this.f4980c > 0) {
            return b(this.f4951a, this.f4980c);
        }
        return null;
    }

    public k o() {
        if (this.f4982e > 0) {
            return k.b(this.f4951a, this.f4982e);
        }
        return null;
    }

    public C0574b p() {
        if (this.f4984g > 0) {
            return C0574b.b(this.f4951a, this.f4984g);
        }
        return null;
    }

    public AbstractC0573a q() throws IOException {
        if (this.f4986i <= 0) {
            return null;
        }
        String strA = a(this.f4951a, this.f4986i);
        if (strA.equals(j.f5044b)) {
            return j.b(this.f4951a, this.f4986i);
        }
        if (strA.equals(k.f5046b)) {
            return k.b(this.f4951a, this.f4986i);
        }
        throw new IOException("Unsupported block type for MdUnit: " + strA);
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "CNBLOCK [lnkCnNext=" + this.f4980c + ", lnkComposition=" + this.f4981d + ", lnkTxName=" + this.f4982e + ", lnkSiSource=" + this.f4983f + ", lnkCcConversion=" + this.f4984g + ", lnkData=" + this.f4985h + ", lnkMdUnit=" + this.f4986i + ", lnkMdComment=" + this.f4987j + ", lnkAtReference=" + Arrays.toString(this.f4988k) + ", lnkDefaultX=" + Arrays.toString(this.f4989l) + ", channelType=" + ((int) this.f4990m) + ", syncType=" + ((int) this.f4991n) + ", dataType=" + ((int) this.f4992o) + ", bitOffset=" + ((int) this.f4993p) + ", byteOffset=" + this.f4994q + ", bitCount=" + this.f4995r + ", flags=" + this.f4996s + ", invalBitPos=" + this.f4997t + ", precision=" + ((int) this.f4998u) + ", attachmentCount=" + this.f4999v + ", valRangeMin=" + this.f5000w + ", valRangeMax=" + this.f5001x + ", limitMin=" + this.f5002y + ", limitMax=" + this.f5003z + ", limitExtMin=" + this.f5004A + ", limitExtMax=" + this.f5005B + "]";
    }

    public static C0576d b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        C0576d c0576d = new C0576d(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        c0576d.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!c0576d.b().equals(f4979b)) {
            throw new IOException("Wrong block type - expected '" + f4979b + "', found '" + c0576d.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        c0576d.a(AbstractC0570d.e(byteBufferAllocate));
        c0576d.b(AbstractC0570d.e(byteBufferAllocate));
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(((int) c0576d.c()) - 24);
        byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2 + 24);
        seekableByteChannel.read(byteBufferAllocate2);
        byteBufferAllocate2.rewind();
        long[] jArr = new long[(int) c0576d.d()];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            jArr[i2] = AbstractC0570d.g(byteBufferAllocate2);
        }
        c0576d.a(AbstractC0570d.a(byteBufferAllocate2));
        c0576d.b(AbstractC0570d.a(byteBufferAllocate2));
        c0576d.c(AbstractC0570d.a(byteBufferAllocate2));
        c0576d.d(AbstractC0570d.a(byteBufferAllocate2));
        c0576d.k(AbstractC0570d.d(byteBufferAllocate2));
        c0576d.l(AbstractC0570d.d(byteBufferAllocate2));
        c0576d.m(AbstractC0570d.d(byteBufferAllocate2));
        c0576d.n(AbstractC0570d.d(byteBufferAllocate2));
        c0576d.e(AbstractC0570d.a(byteBufferAllocate2));
        byteBufferAllocate2.get();
        c0576d.a(AbstractC0570d.b(byteBufferAllocate2));
        c0576d.a(AbstractC0570d.f(byteBufferAllocate2));
        c0576d.b(AbstractC0570d.f(byteBufferAllocate2));
        c0576d.c(AbstractC0570d.f(byteBufferAllocate2));
        c0576d.d(AbstractC0570d.f(byteBufferAllocate2));
        c0576d.e(AbstractC0570d.f(byteBufferAllocate2));
        c0576d.f(AbstractC0570d.f(byteBufferAllocate2));
        c0576d.c(jArr[0]);
        c0576d.d(jArr[1]);
        c0576d.e(jArr[2]);
        c0576d.f(jArr[3]);
        c0576d.g(jArr[4]);
        c0576d.h(jArr[5]);
        c0576d.i(jArr[6]);
        c0576d.j(jArr[7]);
        long[] jArr2 = new long[c0576d.k()];
        for (int i3 = 0; i3 < c0576d.k(); i3++) {
            jArr2[i3] = jArr[i3 + 8];
        }
        c0576d.a(jArr2);
        long[] jArr3 = new long[3];
        if (jArr.length > c0576d.k() + 8) {
            jArr3[0] = jArr[c0576d.k() + 8];
            jArr3[1] = jArr[c0576d.k() + 9];
            jArr3[2] = jArr[c0576d.k() + 10];
        }
        c0576d.b(jArr3);
        return c0576d;
    }
}
