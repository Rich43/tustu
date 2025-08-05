package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import org.icepdf.core.util.PdfOps;

/* renamed from: am.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:am/b.class */
public class C0574b extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f4952b = "##CC";

    /* renamed from: c, reason: collision with root package name */
    private long f4953c;

    /* renamed from: d, reason: collision with root package name */
    private long f4954d;

    /* renamed from: e, reason: collision with root package name */
    private long f4955e;

    /* renamed from: f, reason: collision with root package name */
    private long f4956f;

    /* renamed from: g, reason: collision with root package name */
    private long[] f4957g;

    /* renamed from: h, reason: collision with root package name */
    private byte f4958h;

    /* renamed from: i, reason: collision with root package name */
    private byte f4959i;

    /* renamed from: j, reason: collision with root package name */
    private int f4960j;

    /* renamed from: k, reason: collision with root package name */
    private int f4961k;

    /* renamed from: l, reason: collision with root package name */
    private int f4962l;

    /* renamed from: m, reason: collision with root package name */
    private double f4963m;

    /* renamed from: n, reason: collision with root package name */
    private double f4964n;

    /* renamed from: o, reason: collision with root package name */
    private double[] f4965o;

    private C0574b(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
    }

    public byte e() {
        return this.f4958h;
    }

    public int f() {
        return this.f4961k;
    }

    public int g() {
        return this.f4962l;
    }

    public double[] h() {
        return this.f4965o;
    }

    private void c(long j2) {
        this.f4953c = j2;
    }

    private void d(long j2) {
        this.f4954d = j2;
    }

    private void e(long j2) {
        this.f4955e = j2;
    }

    private void f(long j2) {
        this.f4956f = j2;
    }

    private void a(long[] jArr) {
        this.f4957g = jArr;
    }

    private void a(byte b2) {
        this.f4958h = b2;
    }

    private void b(byte b2) {
        this.f4959i = b2;
    }

    private void a(int i2) {
        this.f4960j = i2;
    }

    private void b(int i2) {
        this.f4961k = i2;
    }

    private void c(int i2) {
        this.f4962l = i2;
    }

    private void a(double d2) {
        this.f4963m = d2;
    }

    private void b(double d2) {
        this.f4964n = d2;
    }

    private void a(double[] dArr) {
        this.f4965o = dArr;
    }

    public k[] i() {
        if (this.f4957g.length <= 0) {
            return null;
        }
        k[] kVarArr = new k[this.f4957g.length];
        for (int i2 = 0; i2 < kVarArr.length; i2++) {
            if (this.f4957g[i2] > 0) {
                kVarArr[i2] = k.b(this.f4951a, this.f4957g[i2]);
            }
        }
        return kVarArr;
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "CCBLOCK [lnkTxName=" + this.f4953c + ", lnkMdUnit=" + this.f4954d + ", lnkMdComment=" + this.f4955e + ", lnkCcInverse=" + this.f4956f + ", lnkCcRef=" + Arrays.toString(this.f4957g) + ", type=" + ((int) this.f4958h) + ", precision=" + ((int) this.f4959i) + ", flags=" + this.f4960j + ", refCount=" + this.f4961k + ", valCount=" + this.f4962l + ", phyRangeMin=" + this.f4963m + ", phyRangeMax=" + this.f4964n + ", val=" + Arrays.toString(this.f4965o) + "]";
    }

    public static C0574b b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        C0574b c0574b = new C0574b(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        c0574b.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!c0574b.b().equals(f4952b)) {
            throw new IOException("Wrong block type - expected '" + f4952b + "', found '" + c0574b.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        c0574b.a(AbstractC0570d.e(byteBufferAllocate));
        c0574b.b(AbstractC0570d.e(byteBufferAllocate));
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(((int) c0574b.c()) + 24);
        byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2 + 24);
        seekableByteChannel.read(byteBufferAllocate2);
        byteBufferAllocate2.rewind();
        long[] jArr = new long[(int) c0574b.d()];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            jArr[i2] = AbstractC0570d.g(byteBufferAllocate2);
        }
        c0574b.a(AbstractC0570d.a(byteBufferAllocate2));
        c0574b.b(AbstractC0570d.a(byteBufferAllocate2));
        c0574b.a(AbstractC0570d.b(byteBufferAllocate2));
        c0574b.b(AbstractC0570d.b(byteBufferAllocate2));
        c0574b.c(AbstractC0570d.b(byteBufferAllocate2));
        c0574b.a(AbstractC0570d.f(byteBufferAllocate2));
        c0574b.b(AbstractC0570d.f(byteBufferAllocate2));
        double[] dArr = new double[c0574b.g()];
        for (int i3 = 0; i3 < dArr.length; i3++) {
            dArr[i3] = AbstractC0570d.f(byteBufferAllocate2);
        }
        c0574b.a(dArr);
        c0574b.c(jArr[0]);
        c0574b.d(jArr[1]);
        c0574b.e(jArr[2]);
        c0574b.f(jArr[3]);
        long[] jArr2 = new long[c0574b.f()];
        for (int i4 = 0; i4 < jArr2.length; i4++) {
            jArr2[i4] = jArr[i4 + 4];
        }
        c0574b.a(jArr2);
        return c0574b;
    }
}
