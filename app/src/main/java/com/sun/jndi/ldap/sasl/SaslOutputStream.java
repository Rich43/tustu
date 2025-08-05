package com.sun.jndi.ldap.sasl;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/* loaded from: rt.jar:com/sun/jndi/ldap/sasl/SaslOutputStream.class */
class SaslOutputStream extends FilterOutputStream {
    private static final boolean debug = false;
    private byte[] lenBuf;
    private int rawSendSize;
    private SaslClient sc;

    SaslOutputStream(SaslClient saslClient, OutputStream outputStream) throws SaslException {
        super(outputStream);
        this.lenBuf = new byte[4];
        this.rawSendSize = 65536;
        this.sc = saslClient;
        String str = (String) saslClient.getNegotiatedProperty(Sasl.RAW_SEND_SIZE);
        if (str != null) {
            try {
                this.rawSendSize = Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                throw new SaslException("javax.security.sasl.rawsendsize property must be numeric string: " + str);
            }
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        write(new byte[]{(byte) i2}, 0, 1);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < i3) {
                byte[] bArrWrap = this.sc.wrap(bArr, i2 + i5, i3 - i5 < this.rawSendSize ? i3 - i5 : this.rawSendSize);
                intToNetworkByteOrder(bArrWrap.length, this.lenBuf, 0, 4);
                this.out.write(this.lenBuf, 0, 4);
                this.out.write(bArrWrap, 0, bArrWrap.length);
                i4 = i5 + this.rawSendSize;
            } else {
                return;
            }
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        SaslException saslException = null;
        try {
            this.sc.dispose();
        } catch (SaslException e2) {
            saslException = e2;
        }
        super.close();
        if (saslException != null) {
            throw saslException;
        }
    }

    private static void intToNetworkByteOrder(int i2, byte[] bArr, int i3, int i4) {
        if (i4 > 4) {
            throw new IllegalArgumentException("Cannot handle more than 4 bytes");
        }
        for (int i5 = i4 - 1; i5 >= 0; i5--) {
            bArr[i3 + i5] = (byte) (i2 & 255);
            i2 >>>= 8;
        }
    }
}
