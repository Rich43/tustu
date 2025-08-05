package javax.crypto;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: jce.jar:javax/crypto/CipherOutputStream.class */
public class CipherOutputStream extends FilterOutputStream {
    private Cipher cipher;
    private OutputStream output;
    private byte[] ibuffer;
    private byte[] obuffer;
    private boolean closed;

    public CipherOutputStream(OutputStream outputStream, Cipher cipher) {
        super(outputStream);
        this.ibuffer = new byte[1];
        this.closed = false;
        this.output = outputStream;
        this.cipher = cipher;
    }

    protected CipherOutputStream(OutputStream outputStream) {
        super(outputStream);
        this.ibuffer = new byte[1];
        this.closed = false;
        this.output = outputStream;
        this.cipher = new NullCipher();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        this.ibuffer[0] = (byte) i2;
        this.obuffer = this.cipher.update(this.ibuffer, 0, 1);
        if (this.obuffer != null) {
            this.output.write(this.obuffer);
            this.obuffer = null;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        this.obuffer = this.cipher.update(bArr, i2, i3);
        if (this.obuffer != null) {
            this.output.write(this.obuffer);
            this.obuffer = null;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.obuffer != null) {
            this.output.write(this.obuffer);
            this.obuffer = null;
        }
        this.output.flush();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        try {
            this.obuffer = this.cipher.doFinal();
        } catch (BadPaddingException | IllegalBlockSizeException e2) {
            this.obuffer = null;
        }
        try {
            flush();
        } catch (IOException e3) {
        }
        this.out.close();
    }
}
