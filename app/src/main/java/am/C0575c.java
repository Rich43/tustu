package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import org.icepdf.core.util.PdfOps;

/* renamed from: am.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:am/c.class */
public class C0575c extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f4966b = "##CG";

    /* renamed from: c, reason: collision with root package name */
    private long f4967c;

    /* renamed from: d, reason: collision with root package name */
    private long f4968d;

    /* renamed from: e, reason: collision with root package name */
    private long f4969e;

    /* renamed from: f, reason: collision with root package name */
    private long f4970f;

    /* renamed from: g, reason: collision with root package name */
    private long f4971g;

    /* renamed from: h, reason: collision with root package name */
    private long f4972h;

    /* renamed from: i, reason: collision with root package name */
    private long f4973i;

    /* renamed from: j, reason: collision with root package name */
    private long f4974j;

    /* renamed from: k, reason: collision with root package name */
    private int f4975k;

    /* renamed from: l, reason: collision with root package name */
    private int f4976l;

    /* renamed from: m, reason: collision with root package name */
    private long f4977m;

    /* renamed from: n, reason: collision with root package name */
    private long f4978n;

    private C0575c(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
    }

    public long e() {
        return this.f4974j;
    }

    public long f() {
        return this.f4977m;
    }

    private void c(long j2) {
        this.f4967c = j2;
    }

    private void d(long j2) {
        this.f4968d = j2;
    }

    private void e(long j2) {
        this.f4969e = j2;
    }

    private void f(long j2) {
        this.f4970f = j2;
    }

    private void g(long j2) {
        this.f4971g = j2;
    }

    private void h(long j2) {
        this.f4972h = j2;
    }

    private void i(long j2) {
        this.f4973i = j2;
    }

    private void j(long j2) {
        this.f4974j = j2;
    }

    private void a(int i2) {
        this.f4975k = i2;
    }

    private void b(int i2) {
        this.f4976l = i2;
    }

    private void k(long j2) {
        this.f4977m = j2;
    }

    private void l(long j2) {
        this.f4978n = j2;
    }

    public C0576d g() {
        if (this.f4968d > 0) {
            return C0576d.b(this.f4951a, this.f4968d);
        }
        return null;
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "CGBLOCK [lnkCgNext=" + this.f4967c + ", lnkCnFirst=" + this.f4968d + ", lnkTxAcqName=" + this.f4969e + ", lnkSiAcqSource=" + this.f4970f + ", lnkSrFirst=" + this.f4971g + ", lnkMdComment=" + this.f4972h + ", recordId=" + this.f4973i + ", cycleCount=" + this.f4974j + ", flags=" + this.f4975k + ", pathSeparator=" + this.f4976l + ", dataBytes=" + this.f4977m + ", invalBytes=" + this.f4978n + "]";
    }

    public static C0575c b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        C0575c c0575c = new C0575c(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(104);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        c0575c.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!c0575c.b().equals(f4966b)) {
            throw new IOException("Wrong block type - expected '" + f4966b + "', found '" + c0575c.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        c0575c.a(AbstractC0570d.e(byteBufferAllocate));
        c0575c.b(AbstractC0570d.e(byteBufferAllocate));
        c0575c.c(AbstractC0570d.g(byteBufferAllocate));
        c0575c.d(AbstractC0570d.g(byteBufferAllocate));
        c0575c.e(AbstractC0570d.g(byteBufferAllocate));
        c0575c.f(AbstractC0570d.g(byteBufferAllocate));
        c0575c.g(AbstractC0570d.g(byteBufferAllocate));
        c0575c.h(AbstractC0570d.g(byteBufferAllocate));
        c0575c.i(AbstractC0570d.e(byteBufferAllocate));
        c0575c.j(AbstractC0570d.e(byteBufferAllocate));
        c0575c.a(AbstractC0570d.b(byteBufferAllocate));
        c0575c.b(AbstractC0570d.b(byteBufferAllocate));
        byteBufferAllocate.get(new byte[4]);
        c0575c.k(AbstractC0570d.d(byteBufferAllocate));
        c0575c.l(AbstractC0570d.d(byteBufferAllocate));
        return c0575c;
    }
}
