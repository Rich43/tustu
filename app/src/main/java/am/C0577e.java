package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import org.icepdf.core.util.PdfOps;

/* renamed from: am.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:am/e.class */
public class C0577e extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f5006b = "##DG";

    /* renamed from: c, reason: collision with root package name */
    private int f5007c;

    /* renamed from: d, reason: collision with root package name */
    private long f5008d;

    /* renamed from: e, reason: collision with root package name */
    private long f5009e;

    /* renamed from: f, reason: collision with root package name */
    private long f5010f;

    /* renamed from: g, reason: collision with root package name */
    private long f5011g;

    /* renamed from: h, reason: collision with root package name */
    private byte f5012h;

    private C0577e(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
        this.f5007c = 0;
    }

    private void c(long j2) {
        this.f5008d = j2;
    }

    private void d(long j2) {
        this.f5009e = j2;
    }

    private void e(long j2) {
        this.f5010f = j2;
    }

    private void f(long j2) {
        this.f5011g = j2;
    }

    private void a(byte b2) {
        this.f5012h = b2;
    }

    public C0577e e() {
        if (this.f5008d > 0) {
            return b(this.f4951a, this.f5008d);
        }
        return null;
    }

    public C0575c f() {
        if (this.f5009e > 0) {
            return C0575c.b(this.f4951a, this.f5009e);
        }
        return null;
    }

    public AbstractC0573a g() throws IOException {
        if (this.f5010f <= 0) {
            return null;
        }
        String strA = a(this.f4951a, this.f5010f);
        if (strA.equals("##DT")) {
            return g.b(this.f4951a, this.f5010f);
        }
        if (strA.equals("##DZ")) {
            return null;
        }
        if (strA.equals(f.f5013b)) {
            return f.b(this.f4951a, this.f5010f);
        }
        if (strA.equals("##HL")) {
            return null;
        }
        throw new IOException("Unsupported block type for data: " + strA);
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "DGBLOCK [lnkDgNext=" + this.f5008d + ", lnkCgFirst=" + this.f5009e + ", lnkData=" + this.f5010f + ", lnkMdComment=" + this.f5011g + ", recIdSize=" + ((int) this.f5012h) + "]";
    }

    public static C0577e b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        C0577e c0577e = new C0577e(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(64);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        c0577e.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!c0577e.b().equals(f5006b)) {
            throw new IOException("Wrong block type - expected '" + f5006b + "', found '" + c0577e.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        c0577e.a(AbstractC0570d.e(byteBufferAllocate));
        c0577e.b(AbstractC0570d.e(byteBufferAllocate));
        c0577e.c(AbstractC0570d.g(byteBufferAllocate));
        c0577e.d(AbstractC0570d.g(byteBufferAllocate));
        c0577e.e(AbstractC0570d.g(byteBufferAllocate));
        c0577e.f(AbstractC0570d.g(byteBufferAllocate));
        c0577e.a(AbstractC0570d.a(byteBufferAllocate));
        return c0577e;
    }

    public int h() {
        return this.f5007c;
    }

    public void a(int i2) {
        this.f5007c = i2;
    }
}
