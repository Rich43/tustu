package java.util.zip;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:java/util/zip/GZIPOutputStream.class */
public class GZIPOutputStream extends DeflaterOutputStream {
    protected CRC32 crc;
    private static final int GZIP_MAGIC = 35615;
    private static final int TRAILER_SIZE = 8;

    public GZIPOutputStream(OutputStream outputStream, int i2) throws IOException {
        this(outputStream, i2, false);
    }

    public GZIPOutputStream(OutputStream outputStream, int i2, boolean z2) throws IOException {
        super(outputStream, new Deflater(-1, true), i2, z2);
        this.crc = new CRC32();
        this.usesDefaultDeflater = true;
        writeHeader();
        this.crc.reset();
    }

    public GZIPOutputStream(OutputStream outputStream) throws IOException {
        this(outputStream, 512, false);
    }

    public GZIPOutputStream(OutputStream outputStream, boolean z2) throws IOException {
        this(outputStream, 512, z2);
    }

    @Override // java.util.zip.DeflaterOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
        super.write(bArr, i2, i3);
        this.crc.update(bArr, i2, i3);
    }

    @Override // java.util.zip.DeflaterOutputStream
    public void finish() throws IOException {
        if (!this.def.finished()) {
            this.def.finish();
            while (!this.def.finished()) {
                int iDeflate = this.def.deflate(this.buf, 0, this.buf.length);
                if (this.def.finished() && iDeflate <= this.buf.length - 8) {
                    writeTrailer(this.buf, iDeflate);
                    this.out.write(this.buf, 0, iDeflate + 8);
                    return;
                } else if (iDeflate > 0) {
                    this.out.write(this.buf, 0, iDeflate);
                }
            }
            byte[] bArr = new byte[8];
            writeTrailer(bArr, 0);
            this.out.write(bArr);
        }
    }

    private void writeHeader() throws IOException {
        this.out.write(new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0});
    }

    private void writeTrailer(byte[] bArr, int i2) throws IOException {
        writeInt((int) this.crc.getValue(), bArr, i2);
        writeInt(this.def.getTotalIn(), bArr, i2 + 4);
    }

    private void writeInt(int i2, byte[] bArr, int i3) throws IOException {
        writeShort(i2 & 65535, bArr, i3);
        writeShort((i2 >> 16) & 65535, bArr, i3 + 2);
    }

    private void writeShort(int i2, byte[] bArr, int i3) throws IOException {
        bArr[i3] = (byte) (i2 & 255);
        bArr[i3 + 1] = (byte) ((i2 >> 8) & 255);
    }
}
