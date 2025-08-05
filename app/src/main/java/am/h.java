package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:am/h.class */
public class h extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f5022b = "##HD";

    /* renamed from: c, reason: collision with root package name */
    private long f5023c;

    /* renamed from: d, reason: collision with root package name */
    private long f5024d;

    /* renamed from: e, reason: collision with root package name */
    private long f5025e;

    /* renamed from: f, reason: collision with root package name */
    private long f5026f;

    /* renamed from: g, reason: collision with root package name */
    private long f5027g;

    /* renamed from: h, reason: collision with root package name */
    private long f5028h;

    /* renamed from: i, reason: collision with root package name */
    private long f5029i;

    /* renamed from: j, reason: collision with root package name */
    private short f5030j;

    /* renamed from: k, reason: collision with root package name */
    private short f5031k;

    /* renamed from: l, reason: collision with root package name */
    private byte f5032l;

    /* renamed from: m, reason: collision with root package name */
    private byte f5033m;

    /* renamed from: n, reason: collision with root package name */
    private byte f5034n;

    /* renamed from: o, reason: collision with root package name */
    private double f5035o;

    /* renamed from: p, reason: collision with root package name */
    private double f5036p;

    public h(SeekableByteChannel seekableByteChannel) {
        super(seekableByteChannel, 64L);
    }

    private void c(long j2) {
        this.f5023c = j2;
    }

    private void d(long j2) {
        this.f5024d = j2;
    }

    private void e(long j2) {
        this.f5025e = j2;
    }

    private void f(long j2) {
        this.f5026f = j2;
    }

    private void g(long j2) {
        this.f5027g = j2;
    }

    private void h(long j2) {
        this.f5028h = j2;
    }

    private void i(long j2) {
        this.f5029i = j2;
    }

    private void a(short s2) {
        this.f5030j = s2;
    }

    private void b(short s2) {
        this.f5031k = s2;
    }

    private void a(byte b2) {
        this.f5032l = b2;
    }

    private void b(byte b2) {
        this.f5033m = b2;
    }

    public byte e() {
        return this.f5034n;
    }

    private void c(byte b2) {
        this.f5034n = b2;
    }

    private void a(double d2) {
        this.f5035o = d2;
    }

    private void b(double d2) {
        this.f5036p = d2;
    }

    public AbstractC0573a f() throws IOException {
        if (this.f5028h <= 0) {
            return null;
        }
        String strA = a(this.f4951a, this.f5028h);
        if (strA.equals(j.f5044b)) {
            return j.b(this.f4951a, this.f5028h);
        }
        if (strA.equals(k.f5046b)) {
            return k.b(this.f4951a, this.f5028h);
        }
        throw new IOException("Unsupported block type for MdComment: " + strA);
    }

    public C0577e g() {
        if (this.f5023c > 0) {
            return C0577e.b(this.f4951a, this.f5023c);
        }
        return null;
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "HDBLOCK [lnkDgFirst=" + this.f5023c + ", lnkFhFirst=" + this.f5024d + ", lnkChFirst=" + this.f5025e + ", lnkAtFirst=" + this.f5026f + ", lnkEvFirst=" + this.f5027g + ", lnkMdComment=" + this.f5028h + ", startTimeNs=" + this.f5029i + ", tzOffsetMin=" + ((int) this.f5030j) + ", dstOffsetMin=" + ((int) this.f5031k) + ", timeFlags=" + ((int) this.f5032l) + ", timeClass=" + ((int) this.f5033m) + ", flags=" + ((int) this.f5034n) + ", startAngleRad=" + this.f5035o + ", startDistanceM=" + this.f5036p + "]";
    }

    public static h a(SeekableByteChannel seekableByteChannel) throws IOException {
        h hVar = new h(seekableByteChannel);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(112);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(64L);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        hVar.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!hVar.b().equals(f5022b)) {
            throw new IOException("Wrong block type - expected '" + f5022b + "', found '" + hVar.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        hVar.a(AbstractC0570d.e(byteBufferAllocate));
        hVar.b(AbstractC0570d.e(byteBufferAllocate));
        hVar.c(AbstractC0570d.g(byteBufferAllocate));
        hVar.d(AbstractC0570d.g(byteBufferAllocate));
        hVar.e(AbstractC0570d.g(byteBufferAllocate));
        hVar.f(AbstractC0570d.g(byteBufferAllocate));
        hVar.g(AbstractC0570d.g(byteBufferAllocate));
        hVar.h(AbstractC0570d.g(byteBufferAllocate));
        hVar.i(AbstractC0570d.e(byteBufferAllocate));
        hVar.a(AbstractC0570d.c(byteBufferAllocate));
        hVar.b(AbstractC0570d.c(byteBufferAllocate));
        hVar.a(AbstractC0570d.a(byteBufferAllocate));
        hVar.b(AbstractC0570d.a(byteBufferAllocate));
        hVar.c(AbstractC0570d.a(byteBufferAllocate));
        if (hVar.e() != 0) {
            throw new IOException("HDBLOCK hd_flags!=0 not yet supported");
        }
        byteBufferAllocate.get();
        hVar.a(AbstractC0570d.f(byteBufferAllocate));
        hVar.b(AbstractC0570d.f(byteBufferAllocate));
        return hVar;
    }
}
