package java.util.zip;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:java/util/zip/InflaterOutputStream.class */
public class InflaterOutputStream extends FilterOutputStream {
    protected final Inflater inf;
    protected final byte[] buf;
    private final byte[] wbuf;
    private boolean usesDefaultInflater;
    private boolean closed;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public InflaterOutputStream(OutputStream outputStream) {
        this(outputStream, new Inflater());
        this.usesDefaultInflater = true;
    }

    public InflaterOutputStream(OutputStream outputStream, Inflater inflater) {
        this(outputStream, inflater, 512);
    }

    public InflaterOutputStream(OutputStream outputStream, Inflater inflater, int i2) {
        super(outputStream);
        this.wbuf = new byte[1];
        this.usesDefaultInflater = false;
        this.closed = false;
        if (outputStream == null) {
            throw new NullPointerException("Null output");
        }
        if (inflater == null) {
            throw new NullPointerException("Null inflater");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("Buffer size < 1");
        }
        this.inf = inflater;
        this.buf = new byte[i2];
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            try {
                finish();
            } finally {
                this.out.close();
                this.closed = true;
            }
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        int iInflate;
        ensureOpen();
        if (!this.inf.finished()) {
            while (!this.inf.finished() && !this.inf.needsInput() && (iInflate = this.inf.inflate(this.buf, 0, this.buf.length)) >= 1) {
                try {
                    this.out.write(this.buf, 0, iInflate);
                } catch (DataFormatException e2) {
                    String message = e2.getMessage();
                    if (message == null) {
                        message = "Invalid ZLIB data format";
                    }
                    throw new ZipException(message);
                }
            }
            super.flush();
        }
    }

    public void finish() throws IOException {
        ensureOpen();
        flush();
        if (this.usesDefaultInflater) {
            this.inf.end();
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        this.wbuf[0] = (byte) i2;
        write(this.wbuf, 0, 1);
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x007e A[Catch: DataFormatException -> 0x00b8, TryCatch #0 {DataFormatException -> 0x00b8, blocks: (B:17:0x002f, B:26:0x004f, B:27:0x0066, B:29:0x007e, B:32:0x0091, B:35:0x009e, B:37:0x00a8, B:38:0x00b1), top: B:48:0x002f }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009e A[Catch: DataFormatException -> 0x00b8, TryCatch #0 {DataFormatException -> 0x00b8, blocks: (B:17:0x002f, B:26:0x004f, B:27:0x0066, B:29:0x007e, B:32:0x0091, B:35:0x009e, B:37:0x00a8, B:38:0x00b1), top: B:48:0x002f }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x009b A[SYNTHETIC] */
    @Override // java.io.FilterOutputStream, java.io.OutputStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void write(byte[] r6, int r7, int r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 213
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.zip.InflaterOutputStream.write(byte[], int, int):void");
    }
}
