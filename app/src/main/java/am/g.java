package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:am/g.class */
public class g extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f5020b = "##DT";

    /* renamed from: c, reason: collision with root package name */
    ByteBuffer f5021c;

    private g(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
        this.f5021c = null;
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "DTBLOCK [pos=" + a() + "]";
    }

    public static g b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        g gVar = new g(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        gVar.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!gVar.b().equals(f5020b)) {
            throw new IOException("Wrong block type - expected '" + f5020b + "', found '" + gVar.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        gVar.a(AbstractC0570d.e(byteBufferAllocate));
        gVar.b(AbstractC0570d.e(byteBufferAllocate));
        return gVar;
    }

    public ByteBuffer a(SeekableByteChannel seekableByteChannel) throws IOException {
        if (this.f5021c == null) {
            int iC = (int) (c() - 24);
            seekableByteChannel.position(a() + 24);
            this.f5021c = ByteBuffer.allocate(iC);
            int i2 = seekableByteChannel.read(this.f5021c);
            if (i2 != iC) {
                throw new IOException("Short Data read! expected: " + iC + ", only read: " + i2);
            }
        }
        return this.f5021c;
    }
}
