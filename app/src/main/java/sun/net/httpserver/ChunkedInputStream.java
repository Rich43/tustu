package sun.net.httpserver;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/net/httpserver/ChunkedInputStream.class */
class ChunkedInputStream extends LeftOverInputStream {
    private int remaining;
    private boolean needToReadHeader;
    static final char CR = '\r';
    static final char LF = '\n';
    private static final int MAX_CHUNK_HEADER_SIZE = 2050;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ChunkedInputStream.class.desiredAssertionStatus();
    }

    ChunkedInputStream(ExchangeImpl exchangeImpl, InputStream inputStream) {
        super(exchangeImpl, inputStream);
        this.needToReadHeader = true;
    }

    private int numeric(char[] cArr, int i2) throws IOException {
        int i3;
        if (!$assertionsDisabled && cArr.length < i2) {
            throw new AssertionError();
        }
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            char c2 = cArr[i5];
            if (c2 >= '0' && c2 <= '9') {
                i3 = c2 - '0';
            } else if (c2 >= 'a' && c2 <= 'f') {
                i3 = (c2 - 'a') + 10;
            } else if (c2 >= 'A' && c2 <= 'F') {
                i3 = (c2 - 'A') + 10;
            } else {
                throw new IOException("invalid chunk length");
            }
            i4 = (i4 * 16) + i3;
        }
        return i4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003e, code lost:
    
        throw new java.io.IOException("invalid chunk header");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int readChunkHeader() throws java.io.IOException {
        /*
            r4 = this;
            r0 = 0
            r5 = r0
            r0 = 16
            char[] r0 = new char[r0]
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
            r0 = 0
            r10 = r0
        L10:
            r0 = r4
            java.io.InputStream r0 = r0.in
            int r0 = r0.read()
            r1 = r0
            r6 = r1
            r1 = -1
            if (r0 == r1) goto L93
            r0 = r6
            char r0 = (char) r0
            r11 = r0
            int r10 = r10 + 1
            r0 = r8
            r1 = r7
            int r1 = r1.length
            r2 = 1
            int r1 = r1 - r2
            if (r0 == r1) goto L35
            r0 = r10
            r1 = 2050(0x802, float:2.873E-42)
            if (r0 <= r1) goto L3f
        L35:
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.String r2 = "invalid chunk header"
            r1.<init>(r2)
            throw r0
        L3f:
            r0 = r5
            if (r0 == 0) goto L69
            r0 = r11
            r1 = 10
            if (r0 != r1) goto L56
            r0 = r4
            r1 = r7
            r2 = r8
            int r0 = r0.numeric(r1, r2)
            r12 = r0
            r0 = r12
            return r0
        L56:
            r0 = 0
            r5 = r0
            r0 = r9
            if (r0 != 0) goto L90
            r0 = r7
            r1 = r8
            int r8 = r8 + 1
            r2 = r11
            r0[r1] = r2
            goto L90
        L69:
            r0 = r11
            r1 = 13
            if (r0 != r1) goto L75
            r0 = 1
            r5 = r0
            goto L90
        L75:
            r0 = r11
            r1 = 59
            if (r0 != r1) goto L82
            r0 = 1
            r9 = r0
            goto L90
        L82:
            r0 = r9
            if (r0 != 0) goto L90
            r0 = r7
            r1 = r8
            int r8 = r8 + 1
            r2 = r11
            r0[r1] = r2
        L90:
            goto L10
        L93:
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.String r2 = "end of stream reading chunk header"
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.httpserver.ChunkedInputStream.readChunkHeader():int");
    }

    @Override // sun.net.httpserver.LeftOverInputStream
    protected int readImpl(byte[] bArr, int i2, int i3) throws IOException {
        if (this.eof) {
            return -1;
        }
        if (this.needToReadHeader) {
            this.remaining = readChunkHeader();
            if (this.remaining == 0) {
                this.eof = true;
                consumeCRLF();
                this.f13582t.getServerImpl().requestCompleted(this.f13582t.getConnection());
                return -1;
            }
            this.needToReadHeader = false;
        }
        if (i3 > this.remaining) {
            i3 = this.remaining;
        }
        int i4 = this.in.read(bArr, i2, i3);
        if (i4 > -1) {
            this.remaining -= i4;
        }
        if (this.remaining == 0) {
            this.needToReadHeader = true;
            consumeCRLF();
        }
        if (i4 < 0 && !this.eof) {
            throw new IOException("connection closed before all data received");
        }
        return i4;
    }

    private void consumeCRLF() throws IOException {
        if (((char) this.in.read()) != '\r') {
            throw new IOException("invalid chunk end");
        }
        if (((char) this.in.read()) != '\n') {
            throw new IOException("invalid chunk end");
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (this.eof || this.closed) {
            return 0;
        }
        int iAvailable = this.in.available();
        return iAvailable > this.remaining ? this.remaining : iAvailable;
    }

    @Override // sun.net.httpserver.LeftOverInputStream
    public boolean isDataBuffered() throws IOException {
        if ($assertionsDisabled || this.eof) {
            return this.in.available() > 0;
        }
        throw new AssertionError();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i2) {
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
