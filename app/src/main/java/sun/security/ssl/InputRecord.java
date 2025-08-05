package sun.security.ssl;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import javax.crypto.BadPaddingException;
import sun.security.ssl.SSLCipher;

/* loaded from: jsse.jar:sun/security/ssl/InputRecord.class */
abstract class InputRecord implements Record, Closeable {
    SSLCipher.SSLReadCipher readCipher;
    TransportContext tc;
    final HandshakeHash handshakeHash;
    ProtocolVersion helloVersion = ProtocolVersion.TLS10;
    boolean isClosed = false;
    int fragmentSize = 16384;

    abstract Plaintext[] decode(ByteBuffer[] byteBufferArr, int i2, int i3) throws BadPaddingException, IOException;

    InputRecord(HandshakeHash handshakeHash, SSLCipher.SSLReadCipher sSLReadCipher) {
        this.readCipher = sSLReadCipher;
        this.handshakeHash = handshakeHash;
    }

    void setHelloVersion(ProtocolVersion protocolVersion) {
        this.helloVersion = protocolVersion;
    }

    boolean seqNumIsHuge() {
        return this.readCipher.authenticator != null && this.readCipher.authenticator.seqNumIsHuge();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.isClosed) {
            this.isClosed = true;
            this.readCipher.dispose();
        }
    }

    synchronized boolean isClosed() {
        return this.isClosed;
    }

    void changeReadCiphers(SSLCipher.SSLReadCipher sSLReadCipher) {
        sSLReadCipher.dispose();
        this.readCipher = sSLReadCipher;
    }

    void changeFragmentSize(int i2) {
        this.fragmentSize = i2;
    }

    int bytesInCompletePacket(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    int bytesInCompletePacket() throws IOException {
        throw new UnsupportedOperationException();
    }

    void setReceiverStream(InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    void setDeliverStream(OutputStream outputStream) {
        throw new UnsupportedOperationException();
    }

    int estimateFragmentSize(int i2) {
        throw new UnsupportedOperationException();
    }

    static ByteBuffer convertToClientHello(ByteBuffer byteBuffer) {
        int i2;
        int iPosition = byteBuffer.position();
        int i3 = (((byteBuffer.get() & Byte.MAX_VALUE) << 8) | (byteBuffer.get() & 255)) + 2;
        byteBuffer.position(iPosition + 3);
        byte b2 = byteBuffer.get();
        byte b3 = byteBuffer.get();
        int i4 = ((byteBuffer.get() & 255) << 8) + (byteBuffer.get() & 255);
        int i5 = ((byteBuffer.get() & 255) << 8) + (byteBuffer.get() & 255);
        int i6 = ((byteBuffer.get() & 255) << 8) + (byteBuffer.get() & 255);
        byte[] bArr = new byte[48 + i5 + ((i4 * 2) / 3)];
        bArr[0] = ContentType.HANDSHAKE.id;
        bArr[1] = b2;
        bArr[2] = b3;
        bArr[5] = 1;
        bArr[9] = b2;
        bArr[10] = b3;
        int i7 = 11;
        int i8 = iPosition + 11 + i4 + i5;
        if (i6 < 32) {
            for (int i9 = 0; i9 < 32 - i6; i9++) {
                int i10 = i7;
                i7++;
                bArr[i10] = 0;
            }
            byteBuffer.position(i8);
            byteBuffer.get(bArr, i7, i6);
            i2 = i7 + i6;
        } else {
            byteBuffer.position((i8 + i6) - 32);
            byteBuffer.get(bArr, 11, 32);
            i2 = 11 + 32;
        }
        int i11 = i8 - i5;
        int i12 = i2;
        int i13 = i2 + 1;
        bArr[i12] = (byte) (i5 & 255);
        byteBuffer.position(i11);
        byteBuffer.get(bArr, i13, i5);
        byteBuffer.position(i11 - i4);
        int i14 = i13 + 2;
        for (int i15 = 0; i15 < i4; i15 += 3) {
            if (byteBuffer.get() != 0) {
                byteBuffer.get();
                byteBuffer.get();
            } else {
                int i16 = i14;
                int i17 = i14 + 1;
                bArr[i16] = byteBuffer.get();
                i14 = i17 + 1;
                bArr[i17] = byteBuffer.get();
            }
        }
        int i18 = i14 - (i13 + 2);
        int i19 = i13 + 1;
        bArr[i13] = (byte) ((i18 >>> 8) & 255);
        bArr[i19] = (byte) (i18 & 255);
        int i20 = i19 + 1 + i18;
        int i21 = i20 + 1;
        bArr[i20] = 1;
        int i22 = i21 + 1;
        bArr[i21] = 0;
        int i23 = i22 - 5;
        bArr[3] = (byte) ((i23 >>> 8) & 255);
        bArr[4] = (byte) (i23 & 255);
        int i24 = i22 - 9;
        bArr[6] = (byte) ((i24 >>> 16) & 255);
        bArr[7] = (byte) ((i24 >>> 8) & 255);
        bArr[8] = (byte) (i24 & 255);
        byteBuffer.position(iPosition + i3);
        return ByteBuffer.wrap(bArr, 5, i22 - 5);
    }

    static ByteBuffer extract(ByteBuffer[] byteBufferArr, int i2, int i3, int i4) {
        boolean z2 = false;
        int i5 = -1;
        int i6 = 0;
        for (int i7 = i2; i7 < i2 + i3 && i6 < i4; i7++) {
            int iRemaining = byteBufferArr[i7].remaining();
            int iPosition = byteBufferArr[i7].position();
            int i8 = 0;
            while (true) {
                if (i8 >= iRemaining || i6 >= i4) {
                    break;
                }
                byte b2 = byteBufferArr[i7].get(iPosition + i8);
                if (i6 == i4 - 2) {
                    i5 = (b2 & 255) << 8;
                } else if (i6 == i4 - 1) {
                    i5 |= b2 & 255;
                    z2 = true;
                    break;
                }
                i6++;
                i8++;
            }
        }
        if (!z2) {
            throw new BufferUnderflowException();
        }
        int i9 = i4 + i5;
        int iRemaining2 = 0;
        for (int i10 = i2; i10 < i2 + i3; i10++) {
            iRemaining2 += byteBufferArr[i10].remaining();
            if (iRemaining2 >= i9) {
                break;
            }
        }
        if (iRemaining2 < i9) {
            throw new BufferUnderflowException();
        }
        byte[] bArr = new byte[i9];
        int i11 = 0;
        int i12 = i9;
        for (int i13 = i2; i13 < i2 + i3; i13++) {
            if (byteBufferArr[i13].hasRemaining()) {
                int iMin = Math.min(i12, byteBufferArr[i13].remaining());
                byteBufferArr[i13].get(bArr, i11, iMin);
                i11 += iMin;
                i12 -= iMin;
            }
            if (i12 <= 0) {
                break;
            }
        }
        return ByteBuffer.wrap(bArr);
    }
}
