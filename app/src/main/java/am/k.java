package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:am/k.class */
public class k extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    public static String f5046b = "##TX";

    /* renamed from: c, reason: collision with root package name */
    private String f5047c;

    private k(SeekableByteChannel seekableByteChannel, long j2) {
        super(seekableByteChannel, j2);
    }

    public String e() {
        return this.f5047c;
    }

    private void b(String str) {
        this.f5047c = str;
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "TXBLOCK [txData=" + this.f5047c + "]";
    }

    public static k b(SeekableByteChannel seekableByteChannel, long j2) throws IOException {
        k kVar = new k(seekableByteChannel, j2);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        kVar.a(AbstractC0570d.a(byteBufferAllocate, 4));
        if (!kVar.b().equals(f5046b)) {
            throw new IOException("Wrong block type - expected '" + f5046b + "', found '" + kVar.b() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        byteBufferAllocate.get(new byte[4]);
        kVar.a(AbstractC0570d.e(byteBufferAllocate));
        kVar.b(AbstractC0570d.e(byteBufferAllocate));
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(((int) kVar.c()) + 24);
        byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(j2 + 24);
        seekableByteChannel.read(byteBufferAllocate2);
        byteBufferAllocate2.rewind();
        kVar.b(AbstractC0570d.b(byteBufferAllocate2, (int) (kVar.c() - 24)));
        return kVar;
    }
}
