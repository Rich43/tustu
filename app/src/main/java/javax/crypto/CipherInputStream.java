package javax.crypto;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jce.jar:javax/crypto/CipherInputStream.class */
public class CipherInputStream extends FilterInputStream {
    private Cipher cipher;
    private InputStream input;
    private byte[] ibuffer;
    private boolean done;
    private byte[] obuffer;
    private int ostart;
    private int ofinish;
    private boolean closed;

    private int getMoreData() throws IOException {
        if (this.done) {
            return -1;
        }
        int i2 = this.input.read(this.ibuffer);
        if (i2 == -1) {
            this.done = true;
            try {
                this.obuffer = this.cipher.doFinal();
                if (this.obuffer == null) {
                    return -1;
                }
                this.ostart = 0;
                this.ofinish = this.obuffer.length;
                return this.ofinish;
            } catch (BadPaddingException | IllegalBlockSizeException e2) {
                this.obuffer = null;
                throw new IOException(e2);
            }
        }
        try {
            this.obuffer = this.cipher.update(this.ibuffer, 0, i2);
            this.ostart = 0;
            if (this.obuffer == null) {
                this.ofinish = 0;
            } else {
                this.ofinish = this.obuffer.length;
            }
            return this.ofinish;
        } catch (IllegalStateException e3) {
            this.obuffer = null;
            throw e3;
        }
    }

    public CipherInputStream(InputStream inputStream, Cipher cipher) {
        super(inputStream);
        this.ibuffer = new byte[512];
        this.done = false;
        this.ostart = 0;
        this.ofinish = 0;
        this.closed = false;
        this.input = inputStream;
        this.cipher = cipher;
    }

    protected CipherInputStream(InputStream inputStream) {
        super(inputStream);
        this.ibuffer = new byte[512];
        this.done = false;
        this.ostart = 0;
        this.ofinish = 0;
        this.closed = false;
        this.input = inputStream;
        this.cipher = new NullCipher();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2;
        if (this.ostart >= this.ofinish) {
            int moreData = 0;
            while (true) {
                i2 = moreData;
                if (i2 != 0) {
                    break;
                }
                moreData = getMoreData();
            }
            if (i2 == -1) {
                return -1;
            }
        }
        byte[] bArr = this.obuffer;
        int i3 = this.ostart;
        this.ostart = i3 + 1;
        return bArr[i3] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4;
        if (this.ostart >= this.ofinish) {
            int moreData = 0;
            while (true) {
                i4 = moreData;
                if (i4 != 0) {
                    break;
                }
                moreData = getMoreData();
            }
            if (i4 == -1) {
                return -1;
            }
        }
        if (i3 <= 0) {
            return 0;
        }
        int i5 = this.ofinish - this.ostart;
        if (i3 < i5) {
            i5 = i3;
        }
        if (bArr != null) {
            System.arraycopy(this.obuffer, this.ostart, bArr, i2, i5);
        }
        this.ostart += i5;
        return i5;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        int i2 = this.ofinish - this.ostart;
        if (j2 > i2) {
            j2 = i2;
        }
        if (j2 < 0) {
            return 0L;
        }
        this.ostart = (int) (this.ostart + j2);
        return j2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return this.ofinish - this.ostart;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.input.close();
        if (!this.done) {
            try {
                this.cipher.doFinal();
            } catch (BadPaddingException | IllegalBlockSizeException e2) {
            }
        }
        this.ostart = 0;
        this.ofinish = 0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }
}
