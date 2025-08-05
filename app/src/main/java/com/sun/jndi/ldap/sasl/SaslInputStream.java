package com.sun.jndi.ldap.sasl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/* loaded from: rt.jar:com/sun/jndi/ldap/sasl/SaslInputStream.class */
public class SaslInputStream extends InputStream {
    private static final boolean debug = false;
    private byte[] saslBuffer;
    private byte[] lenBuf = new byte[4];
    private byte[] buf = new byte[0];
    private int bufPos = 0;
    private InputStream in;
    private SaslClient sc;
    private int recvMaxBufSize;

    SaslInputStream(SaslClient saslClient, InputStream inputStream) throws SaslException {
        this.recvMaxBufSize = 65536;
        this.in = inputStream;
        this.sc = saslClient;
        String str = (String) saslClient.getNegotiatedProperty(Sasl.MAX_BUFFER);
        if (str != null) {
            try {
                this.recvMaxBufSize = Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                throw new SaslException("javax.security.sasl.maxbuffer property must be numeric string: " + str);
            }
        }
        this.saslBuffer = new byte[this.recvMaxBufSize];
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        byte[] bArr = new byte[1];
        if (read(bArr, 0, 1) > 0) {
            return bArr[0];
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4;
        if (this.bufPos >= this.buf.length) {
            int iFill = fill();
            while (true) {
                i4 = iFill;
                if (i4 != 0) {
                    break;
                }
                iFill = fill();
            }
            if (i4 == -1) {
                return -1;
            }
        }
        int length = this.buf.length - this.bufPos;
        if (i3 > length) {
            System.arraycopy(this.buf, this.bufPos, bArr, i2, length);
            this.bufPos = this.buf.length;
            return length;
        }
        System.arraycopy(this.buf, this.bufPos, bArr, i2, i3);
        this.bufPos += i3;
        return i3;
    }

    private int fill() throws IOException {
        if (readFully(this.lenBuf, 4) != 4) {
            return -1;
        }
        int iNetworkByteOrderToInt = networkByteOrderToInt(this.lenBuf, 0, 4);
        if (iNetworkByteOrderToInt > this.recvMaxBufSize) {
            throw new IOException(iNetworkByteOrderToInt + "exceeds the negotiated receive buffer size limit:" + this.recvMaxBufSize);
        }
        int fully = readFully(this.saslBuffer, iNetworkByteOrderToInt);
        if (fully != iNetworkByteOrderToInt) {
            throw new EOFException("Expecting to read " + iNetworkByteOrderToInt + " bytes but got " + fully + " bytes before EOF");
        }
        this.buf = this.sc.unwrap(this.saslBuffer, 0, iNetworkByteOrderToInt);
        this.bufPos = 0;
        return this.buf.length;
    }

    private int readFully(byte[] bArr, int i2) throws IOException {
        int i3 = 0;
        while (i2 > 0) {
            int i4 = this.in.read(bArr, i3, i2);
            if (i4 == -1) {
                if (i3 == 0) {
                    return -1;
                }
                return i3;
            }
            i3 += i4;
            i2 -= i4;
        }
        return i3;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.buf.length - this.bufPos;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        SaslException saslException = null;
        try {
            this.sc.dispose();
        } catch (SaslException e2) {
            saslException = e2;
        }
        this.in.close();
        if (saslException != null) {
            throw saslException;
        }
    }

    private static int networkByteOrderToInt(byte[] bArr, int i2, int i3) {
        if (i3 > 4) {
            throw new IllegalArgumentException("Cannot handle more than 4 bytes");
        }
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            i4 = (i4 << 8) | (bArr[i2 + i5] & 255);
        }
        return i4;
    }
}
