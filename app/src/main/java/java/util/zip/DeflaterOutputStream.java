package java.util.zip;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:java/util/zip/DeflaterOutputStream.class */
public class DeflaterOutputStream extends FilterOutputStream {
    protected Deflater def;
    protected byte[] buf;
    private boolean closed;
    private final boolean syncFlush;
    boolean usesDefaultDeflater;

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater, int i2, boolean z2) {
        super(outputStream);
        this.closed = false;
        this.usesDefaultDeflater = false;
        if (outputStream == null || deflater == null) {
            throw new NullPointerException();
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("buffer size <= 0");
        }
        this.def = deflater;
        this.buf = new byte[i2];
        this.syncFlush = z2;
    }

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater, int i2) {
        this(outputStream, deflater, i2, false);
    }

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater, boolean z2) {
        this(outputStream, deflater, 512, z2);
    }

    public DeflaterOutputStream(OutputStream outputStream, Deflater deflater) {
        this(outputStream, deflater, 512, false);
    }

    public DeflaterOutputStream(OutputStream outputStream, boolean z2) {
        this(outputStream, new Deflater(), 512, z2);
        this.usesDefaultDeflater = true;
    }

    public DeflaterOutputStream(OutputStream outputStream) {
        this(outputStream, false);
        this.usesDefaultDeflater = true;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        write(new byte[]{(byte) (i2 & 255)}, 0, 1);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.def.finished()) {
            throw new IOException("write beyond end of stream");
        }
        if ((i2 | i3 | (i2 + i3) | (bArr.length - (i2 + i3))) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 != 0 && !this.def.finished()) {
            this.def.setInput(bArr, i2, i3);
            while (!this.def.needsInput()) {
                deflate();
            }
        }
    }

    public void finish() throws IOException {
        if (!this.def.finished()) {
            this.def.finish();
            while (!this.def.finished()) {
                deflate();
            }
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            finish();
            if (this.usesDefaultDeflater) {
                this.def.end();
            }
            this.out.close();
            this.closed = true;
        }
    }

    protected void deflate() throws IOException {
        int iDeflate = this.def.deflate(this.buf, 0, this.buf.length);
        if (iDeflate > 0) {
            this.out.write(this.buf, 0, iDeflate);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        int iDeflate;
        if (this.syncFlush && !this.def.finished()) {
            do {
                iDeflate = this.def.deflate(this.buf, 0, this.buf.length, 2);
                if (iDeflate <= 0) {
                    break;
                } else {
                    this.out.write(this.buf, 0, iDeflate);
                }
            } while (iDeflate >= this.buf.length);
        }
        this.out.flush();
    }
}
