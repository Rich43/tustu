package am;

import al.AbstractC0570d;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:am/i.class */
public class i extends AbstractC0573a {

    /* renamed from: b, reason: collision with root package name */
    private final Path f5037b;

    /* renamed from: c, reason: collision with root package name */
    private String f5038c;

    /* renamed from: d, reason: collision with root package name */
    private String f5039d;

    /* renamed from: e, reason: collision with root package name */
    private String f5040e;

    /* renamed from: f, reason: collision with root package name */
    private int f5041f;

    /* renamed from: g, reason: collision with root package name */
    private int f5042g;

    /* renamed from: h, reason: collision with root package name */
    private int f5043h;

    private i(Path path, SeekableByteChannel seekableByteChannel) {
        super(seekableByteChannel, 0L);
        this.f5037b = path;
    }

    public String e() {
        return this.f5038c;
    }

    private void b(String str) {
        this.f5038c = str;
    }

    public String f() {
        return this.f5039d;
    }

    private void c(String str) {
        this.f5039d = str;
    }

    private void d(String str) {
        this.f5040e = str;
    }

    public int g() {
        return this.f5041f;
    }

    private void a(int i2) {
        this.f5041f = i2;
    }

    public int h() {
        return this.f5042g;
    }

    private void b(int i2) {
        this.f5042g = i2;
    }

    public int i() {
        return this.f5043h;
    }

    private void c(int i2) {
        this.f5043h = i2;
    }

    public h j() {
        return h.a(this.f4951a);
    }

    @Override // am.AbstractC0573a
    public String toString() {
        return "IDBLOCK [mdfFilePath=" + ((Object) this.f5037b) + ", idFile=" + this.f5038c + ", idVers=" + this.f5039d + ", idProg=" + this.f5040e + ", idVer=" + this.f5041f + ", idUnfinFlags=" + this.f5042g + ", idCustomUnfinFlags=" + this.f5043h + "]";
    }

    public static i a(Path path, SeekableByteChannel seekableByteChannel) throws IOException {
        i iVar = new i(path, seekableByteChannel);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(64);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(0L);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        iVar.b(AbstractC0570d.a(byteBufferAllocate, 8));
        if (!iVar.e().equals("MDF     ")) {
            throw new IOException("Invalid or corrupt MDF4 file: " + iVar.e());
        }
        iVar.c(AbstractC0570d.a(byteBufferAllocate, 8));
        if (!iVar.f().startsWith("4")) {
            throw new IOException("Unsupported MDF4 format: " + iVar.f());
        }
        iVar.d(AbstractC0570d.a(byteBufferAllocate, 8));
        byteBufferAllocate.get(new byte[4]);
        iVar.a(AbstractC0570d.b(byteBufferAllocate));
        if (iVar.g() < 400) {
            throw new IOException("Unsupported MDF4 version, must be >400: " + iVar.g());
        }
        iVar.b(AbstractC0570d.b(byteBufferAllocate));
        if (iVar.i() != 0) {
            throw new IOException("Only finalized MDF file can be read, found unfinalized standard flag '" + iVar.h() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        iVar.c(AbstractC0570d.b(byteBufferAllocate));
        if (iVar.i() != 0) {
            throw new IOException("Only finalized MDF file can be read, found unfinalized custom flag '" + iVar.i() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        return iVar;
    }
}
