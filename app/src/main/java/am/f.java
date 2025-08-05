package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:am/f.class */
public class f extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f5013b = "##DL";

    /* renamed from: c, reason: collision with root package name */
    private long f5014c;

    /* renamed from: d, reason: collision with root package name */
    private long[] f5015d;

    /* renamed from: e, reason: collision with root package name */
    private byte f5016e;

    /* renamed from: f, reason: collision with root package name */
    private long f5017f;

    /* renamed from: g, reason: collision with root package name */
    private long f5018g;

    /* renamed from: h, reason: collision with root package name */
    private long[] f5019h;

    private f(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
    }

    public long[] e() {
        return this.f5015d;
    }

    public long f() {
        return this.f5017f;
    }

    private void c(long j2) {
        this.f5014c = j2;
    }

    private void a(long[] jArr) {
        this.f5015d = jArr;
    }

    private void a(byte b2) {
        this.f5016e = b2;
    }

    private void d(long j2) {
        this.f5017f = j2;
    }

    private void e(long j2) {
        this.f5018g = j2;
    }

    private void b(long[] jArr) {
        this.f5019h = jArr;
    }

    private boolean g() {
        return BigInteger.valueOf(this.f5016e).testBit(0);
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "DLBLOCK [lnkDlNext=" + this.f5014c + ", lnkDlData=" + Arrays.toString(this.f5015d) + ", flags=" + ((int) this.f5016e) + ", count=" + this.f5017f + ", equalLength=" + this.f5018g + ", offset=" + Arrays.toString(this.f5019h) + "]";
    }

    public static f b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        f fVar = new f(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        fVar.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!fVar.b().equals(f5013b)) {
            throw new IOException("Wrong block type - expected '" + f5013b + "', found '" + fVar.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        fVar.a(AbstractC0570d.e(byteBufferAllocate));
        fVar.b(AbstractC0570d.e(byteBufferAllocate));
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(((int) fVar.c()) + 24);
        byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2 + 24);
        seekableByteChannel.read(byteBufferAllocate2);
        byteBufferAllocate2.rewind();
        long[] jArr = new long[(int) fVar.d()];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            jArr[i2] = AbstractC0570d.g(byteBufferAllocate2);
        }
        fVar.a(AbstractC0570d.a(byteBufferAllocate2));
        byteBufferAllocate2.get(new byte[3]);
        fVar.d(AbstractC0570d.d(byteBufferAllocate2));
        if (fVar.g()) {
            fVar.e(AbstractC0570d.e(byteBufferAllocate2));
        }
        long[] jArr2 = new long[(int) fVar.f()];
        for (int i3 = 0; i3 < jArr2.length; i3++) {
            jArr2[i3] = AbstractC0570d.e(byteBufferAllocate2);
        }
        fVar.b(jArr2);
        fVar.c(jArr[0]);
        long[] jArr3 = new long[(int) fVar.f()];
        for (int i4 = 0; i4 < jArr3.length; i4++) {
            jArr3[i4] = jArr[i4 + 1];
        }
        fVar.a(jArr3);
        return fVar;
    }
}
