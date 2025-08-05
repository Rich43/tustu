package sun.security.ssl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLCipher;

/* loaded from: jsse.jar:sun/security/ssl/SSLSocketInputRecord.class */
final class SSLSocketInputRecord extends InputRecord implements SSLRecord {
    private InputStream is;
    private OutputStream os;
    private final byte[] temporary;
    private boolean formatVerified;
    private ByteBuffer handshakeBuffer;
    private boolean hasHeader;

    SSLSocketInputRecord(HandshakeHash handshakeHash) {
        super(handshakeHash, SSLCipher.SSLReadCipher.nullTlsReadCipher());
        this.is = null;
        this.os = null;
        this.temporary = new byte[1024];
        this.formatVerified = false;
        this.handshakeBuffer = null;
        this.hasHeader = false;
    }

    @Override // sun.security.ssl.InputRecord
    int bytesInCompletePacket() throws IOException {
        int i2;
        if (!this.hasHeader) {
            try {
                if (read(this.is, this.temporary, 0, 5) < 0) {
                    return -1;
                }
                this.hasHeader = true;
            } catch (EOFException e2) {
                return -1;
            }
        }
        byte b2 = this.temporary[0];
        if (this.formatVerified || b2 == ContentType.HANDSHAKE.id || b2 == ContentType.ALERT.id) {
            if (!ProtocolVersion.isNegotiable(this.temporary[1], this.temporary[2], false)) {
                throw new SSLException("Unrecognized record version " + ProtocolVersion.nameOf(this.temporary[1], this.temporary[2]) + " , plaintext connection?");
            }
            this.formatVerified = true;
            i2 = ((this.temporary[3] & 255) << 8) + (this.temporary[4] & 255) + 5;
        } else {
            if (((b2 & 128) != 0) && (this.temporary[2] == 1 || this.temporary[2] == 4)) {
                if (!ProtocolVersion.isNegotiable(this.temporary[3], this.temporary[4], false)) {
                    throw new SSLException("Unrecognized record version " + ProtocolVersion.nameOf(this.temporary[3], this.temporary[4]) + " , plaintext connection?");
                }
                i2 = ((b2 & Byte.MAX_VALUE) << 8) + (this.temporary[1] & 255) + 2;
            } else {
                throw new SSLException("Unrecognized SSL message, plaintext connection?");
            }
        }
        return i2;
    }

    @Override // sun.security.ssl.InputRecord
    Plaintext[] decode(ByteBuffer[] byteBufferArr, int i2, int i3) throws BadPaddingException, IOException {
        if (this.isClosed) {
            return null;
        }
        if (!this.hasHeader) {
            if (read(this.is, this.temporary, 0, 5) < 0) {
                throw new EOFException("SSL peer shut down incorrectly");
            }
            this.hasHeader = true;
        }
        if (!this.formatVerified) {
            this.formatVerified = true;
            if (this.temporary[0] != ContentType.HANDSHAKE.id && this.temporary[0] != ContentType.ALERT.id) {
                this.hasHeader = false;
                return handleUnknownRecord(this.temporary);
            }
        }
        this.hasHeader = false;
        return decodeInputRecord(this.temporary);
    }

    @Override // sun.security.ssl.InputRecord
    void setReceiverStream(InputStream inputStream) {
        this.is = inputStream;
    }

    @Override // sun.security.ssl.InputRecord
    void setDeliverStream(OutputStream outputStream) {
        this.os = outputStream;
    }

    private Plaintext[] decodeInputRecord(byte[] bArr) throws BadPaddingException, IOException {
        byte b2 = bArr[0];
        byte b3 = bArr[1];
        byte b4 = bArr[2];
        int i2 = ((bArr[3] & 255) << 8) + (bArr[4] & 255);
        if (SSLLogger.isOn && SSLLogger.isOn("record")) {
            SSLLogger.fine("READ: " + ProtocolVersion.nameOf(b3, b4) + " " + ContentType.nameOf(b2) + ", length = " + i2, new Object[0]);
        }
        if (i2 < 0 || i2 > 33088) {
            throw new SSLProtocolException("Bad input record size, TLSCiphertext.length = " + i2);
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(5 + i2);
        int iPosition = byteBufferAllocate.position();
        byteBufferAllocate.put(this.temporary, 0, 5);
        while (i2 > 0) {
            int iMin = Math.min(this.temporary.length, i2);
            if (read(this.is, this.temporary, 0, iMin) < 0) {
                throw new EOFException("SSL peer shut down incorrectly");
            }
            byteBufferAllocate.put(this.temporary, 0, iMin);
            i2 -= iMin;
        }
        byteBufferAllocate.flip();
        byteBufferAllocate.position(iPosition + 5);
        if (SSLLogger.isOn && SSLLogger.isOn("record")) {
            SSLLogger.fine("READ: " + ProtocolVersion.nameOf(b3, b4) + " " + ContentType.nameOf(b2) + ", length = " + byteBufferAllocate.remaining(), new Object[0]);
        }
        try {
            Plaintext plaintextDecrypt = this.readCipher.decrypt(b2, byteBufferAllocate, null);
            ByteBuffer byteBuffer = plaintextDecrypt.fragment;
            byte b5 = plaintextDecrypt.contentType;
            if (b5 != ContentType.HANDSHAKE.id && this.handshakeBuffer != null && this.handshakeBuffer.hasRemaining()) {
                throw new SSLProtocolException("Expecting a handshake fragment, but received " + ContentType.nameOf(b5));
            }
            if (b5 == ContentType.HANDSHAKE.id) {
                ByteBuffer byteBuffer2 = byteBuffer;
                if (this.handshakeBuffer != null && this.handshakeBuffer.remaining() != 0) {
                    ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[this.handshakeBuffer.remaining() + byteBuffer.remaining()]);
                    byteBufferWrap.put(this.handshakeBuffer);
                    byteBufferWrap.put(byteBuffer);
                    byteBuffer2 = (ByteBuffer) byteBufferWrap.rewind();
                    this.handshakeBuffer = null;
                }
                ArrayList arrayList = new ArrayList(5);
                while (true) {
                    if (!byteBuffer2.hasRemaining()) {
                        break;
                    }
                    int iRemaining = byteBuffer2.remaining();
                    if (iRemaining < 4) {
                        this.handshakeBuffer = ByteBuffer.wrap(new byte[iRemaining]);
                        this.handshakeBuffer.put(byteBuffer2);
                        this.handshakeBuffer.rewind();
                        break;
                    }
                    byteBuffer2.mark();
                    byte b6 = byteBuffer2.get();
                    if (!SSLHandshake.isKnown(b6)) {
                        throw new SSLProtocolException("Unknown handshake type size, Handshake.msg_type = " + (b6 & 255));
                    }
                    int int24 = Record.getInt24(byteBuffer2);
                    if (int24 > SSLConfiguration.maxHandshakeMessageSize) {
                        throw new SSLProtocolException("The size of the handshake message (" + int24 + ") exceeds the maximum allowed size (" + SSLConfiguration.maxHandshakeMessageSize + ")");
                    }
                    byteBuffer2.reset();
                    int i3 = 4 + int24;
                    if (iRemaining < i3) {
                        this.handshakeBuffer = ByteBuffer.wrap(new byte[iRemaining]);
                        this.handshakeBuffer.put(byteBuffer2);
                        this.handshakeBuffer.rewind();
                        break;
                    }
                    if (iRemaining == i3) {
                        if (this.handshakeHash.isHashable(b6)) {
                            this.handshakeHash.receive(byteBuffer2);
                        }
                        arrayList.add(new Plaintext(b5, b3, b4, -1, -1L, byteBuffer2));
                    } else {
                        int iPosition2 = byteBuffer2.position();
                        int iLimit = byteBuffer2.limit();
                        int i4 = iPosition2 + i3;
                        byteBuffer2.limit(i4);
                        if (this.handshakeHash.isHashable(b6)) {
                            this.handshakeHash.receive(byteBuffer2);
                        }
                        arrayList.add(new Plaintext(b5, b3, b4, -1, -1L, byteBuffer2.slice()));
                        byteBuffer2.position(i4);
                        byteBuffer2.limit(iLimit);
                    }
                }
                return (Plaintext[]) arrayList.toArray(new Plaintext[0]);
            }
            return new Plaintext[]{new Plaintext(b5, b3, b4, -1, -1L, byteBuffer)};
        } catch (BadPaddingException e2) {
            throw e2;
        } catch (GeneralSecurityException e3) {
            throw ((SSLProtocolException) new SSLProtocolException("Unexpected exception").initCause(e3));
        }
    }

    private Plaintext[] handleUnknownRecord(byte[] bArr) throws BadPaddingException, IOException {
        byte b2 = bArr[0];
        byte b3 = bArr[2];
        if ((b2 & 128) != 0 && b3 == 1) {
            if (this.helloVersion != ProtocolVersion.SSL20Hello) {
                throw new SSLHandshakeException("SSLv2Hello is not enabled");
            }
            byte b4 = bArr[3];
            byte b5 = bArr[4];
            if (b4 == ProtocolVersion.SSL20Hello.major && b5 == ProtocolVersion.SSL20Hello.minor) {
                this.os.write(SSLRecord.v2NoCipher);
                if (SSLLogger.isOn) {
                    if (SSLLogger.isOn("record")) {
                        SSLLogger.fine("Requested to negotiate unsupported SSLv2!", new Object[0]);
                    }
                    if (SSLLogger.isOn("packet")) {
                        SSLLogger.fine("Raw write", SSLRecord.v2NoCipher);
                    }
                }
                throw new SSLException("Unsupported SSL v2.0 ClientHello");
            }
            int i2 = ((bArr[0] & Byte.MAX_VALUE) << 8) | (bArr[1] & 255);
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(5 + i2);
            byteBufferAllocate.put(this.temporary, 0, 5);
            int i3 = i2 - 3;
            while (i3 > 0) {
                int iMin = Math.min(this.temporary.length, i3);
                if (read(this.is, this.temporary, 0, iMin) < 0) {
                    throw new EOFException("SSL peer shut down incorrectly");
                }
                byteBufferAllocate.put(this.temporary, 0, iMin);
                i3 -= iMin;
            }
            byteBufferAllocate.flip();
            byteBufferAllocate.position(2);
            this.handshakeHash.receive(byteBufferAllocate);
            byteBufferAllocate.position(0);
            ByteBuffer byteBufferConvertToClientHello = convertToClientHello(byteBufferAllocate);
            if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                SSLLogger.fine("[Converted] ClientHello", byteBufferConvertToClientHello);
            }
            return new Plaintext[]{new Plaintext(ContentType.HANDSHAKE.id, b4, b5, -1, -1L, byteBufferConvertToClientHello)};
        }
        if ((b2 & 128) != 0 && b3 == 4) {
            throw new SSLException("SSL V2.0 servers are not supported.");
        }
        throw new SSLException("Unsupported or unrecognized SSL message");
    }

    private static int read(InputStream inputStream, byte[] bArr, int i2, int i3) throws IOException {
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < i3) {
                int i6 = inputStream.read(bArr, i2 + i5, i3 - i5);
                if (i6 < 0) {
                    if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                        SSLLogger.fine("Raw read: EOF", new Object[0]);
                        return -1;
                    }
                    return -1;
                }
                if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                    SSLLogger.fine("Raw read", ByteBuffer.wrap(bArr, i2 + i5, i6));
                }
                i4 = i5 + i6;
            } else {
                return i5;
            }
        }
    }

    void deplete(boolean z2) throws IOException {
        int iAvailable = this.is.available();
        if (z2 && iAvailable == 0) {
            this.is.read();
        }
        while (true) {
            int iAvailable2 = this.is.available();
            if (iAvailable2 != 0) {
                this.is.skip(iAvailable2);
            } else {
                return;
            }
        }
    }
}
