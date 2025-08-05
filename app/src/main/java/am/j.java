package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:am/j.class */
public class j extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f5044b = "##MD";

    /* renamed from: c, reason: collision with root package name */
    private String f5045c;

    private j(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
    }

    private void b(String str) {
        this.f5045c = str;
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "MDBLOCK [mdData=" + this.f5045c + "]";
    }

    public static j b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        j jVar = new j(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        jVar.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!jVar.b().equals(f5044b)) {
            throw new IOException("Wrong block type - expected '" + f5044b + "', found '" + jVar.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        jVar.a(AbstractC0570d.e(byteBufferAllocate));
        jVar.b(AbstractC0570d.e(byteBufferAllocate));
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(((int) jVar.c()) + 24);
        byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2 + 24);
        seekableByteChannel.read(byteBufferAllocate2);
        byteBufferAllocate2.rewind();
        jVar.b(AbstractC0570d.b(byteBufferAllocate2, (int) (jVar.c() - 24)));
        return jVar;
    }
}
