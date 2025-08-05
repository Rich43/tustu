package sun.rmi.transport.proxy;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.icepdf.core.util.PdfOps;
import sun.rmi.runtime.Log;

/* loaded from: rt.jar:sun/rmi/transport/proxy/HttpInputStream.class */
class HttpInputStream extends FilterInputStream {
    protected int bytesLeft;
    protected int bytesLeftAtMark;

    public HttpInputStream(InputStream inputStream) throws IOException {
        String line;
        super(inputStream);
        if (inputStream.markSupported()) {
            inputStream.mark(0);
        }
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String lowerCase = "Content-length:".toLowerCase();
        boolean z2 = false;
        do {
            line = dataInputStream.readLine();
            if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.VERBOSE)) {
                RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "received header line: \"" + line + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            if (line == null) {
                throw new EOFException();
            }
            if (line.toLowerCase().startsWith(lowerCase)) {
                if (z2) {
                    throw new IOException("Multiple Content-length entries found.");
                }
                this.bytesLeft = Integer.parseInt(line.substring(lowerCase.length()).trim());
                z2 = true;
            }
            if (line.length() == 0 || line.charAt(0) == '\r') {
                break;
            }
        } while (line.charAt(0) != '\n');
        if (!z2 || this.bytesLeft < 0) {
            this.bytesLeft = Integer.MAX_VALUE;
        }
        this.bytesLeftAtMark = this.bytesLeft;
        if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.VERBOSE)) {
            RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "content length: " + this.bytesLeft);
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        int iAvailable = this.in.available();
        if (iAvailable > this.bytesLeft) {
            iAvailable = this.bytesLeft;
        }
        return iAvailable;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.bytesLeft > 0) {
            int i2 = this.in.read();
            if (i2 != -1) {
                this.bytesLeft--;
            }
            if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.VERBOSE)) {
                RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "received byte: '" + ((i2 & 127) < 32 ? " " : String.valueOf((char) i2)) + "' " + i2);
            }
            return i2;
        }
        RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "read past content length");
        return -1;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.bytesLeft == 0 && i3 > 0) {
            RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "read past content length");
            return -1;
        }
        if (i3 > this.bytesLeft) {
            i3 = this.bytesLeft;
        }
        int i4 = this.in.read(bArr, i2, i3);
        this.bytesLeft -= i4;
        if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.VERBOSE)) {
            RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "read " + i4 + " bytes, " + this.bytesLeft + " remaining");
        }
        return i4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i2) {
        this.in.mark(i2);
        if (this.in.markSupported()) {
            this.bytesLeftAtMark = this.bytesLeft;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        this.in.reset();
        this.bytesLeft = this.bytesLeftAtMark;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        if (j2 > this.bytesLeft) {
            j2 = this.bytesLeft;
        }
        long jSkip = this.in.skip(j2);
        this.bytesLeft = (int) (this.bytesLeft - jSkip);
        return jSkip;
    }
}
