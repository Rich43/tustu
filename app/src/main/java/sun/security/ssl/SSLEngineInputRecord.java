package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLCipher;

/* loaded from: jsse.jar:sun/security/ssl/SSLEngineInputRecord.class */
final class SSLEngineInputRecord extends InputRecord implements SSLRecord {
    private boolean formatVerified;
    private ByteBuffer handshakeBuffer;

    SSLEngineInputRecord(HandshakeHash handshakeHash) {
        super(handshakeHash, SSLCipher.SSLReadCipher.nullTlsReadCipher());
        this.formatVerified = false;
        this.handshakeBuffer = null;
    }

    @Override // sun.security.ssl.InputRecord
    int estimateFragmentSize(int i2) {
        if (i2 > 0) {
            return this.readCipher.estimateFragmentSize(i2, 5);
        }
        return 16384;
    }

    @Override // sun.security.ssl.InputRecord
    int bytesInCompletePacket(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        return bytesInCompletePacket(byteBufferArr[i2]);
    }

    private int bytesInCompletePacket(ByteBuffer byteBuffer) throws SSLException {
        int i2;
        if (byteBuffer.remaining() < 5) {
            return -1;
        }
        int iPosition = byteBuffer.position();
        byte b2 = byteBuffer.get(iPosition);
        if (this.formatVerified || b2 == ContentType.HANDSHAKE.id || b2 == ContentType.ALERT.id) {
            byte b3 = byteBuffer.get(iPosition + 1);
            byte b4 = byteBuffer.get(iPosition + 2);
            if (!ProtocolVersion.isNegotiable(b3, b4, false)) {
                throw new SSLException("Unrecognized record version " + ProtocolVersion.nameOf(b3, b4) + " , plaintext connection?");
            }
            this.formatVerified = true;
            i2 = ((byteBuffer.get(iPosition + 3) & 255) << 8) + (byteBuffer.get(iPosition + 4) & 255) + 5;
        } else {
            boolean z2 = (b2 & 128) != 0;
            if (z2 && (byteBuffer.get(iPosition + 2) == 1 || byteBuffer.get(iPosition + 2) == 4)) {
                byte b5 = byteBuffer.get(iPosition + 3);
                byte b6 = byteBuffer.get(iPosition + 4);
                if (!ProtocolVersion.isNegotiable(b5, b6, false)) {
                    throw new SSLException("Unrecognized record version " + ProtocolVersion.nameOf(b5, b6) + " , plaintext connection?");
                }
                i2 = ((b2 & (z2 ? Byte.MAX_VALUE : (byte) 63)) << 8) + (byteBuffer.get(iPosition + 1) & 255) + (z2 ? 2 : 3);
            } else {
                throw new SSLException("Unrecognized SSL message, plaintext connection?");
            }
        }
        return i2;
    }

    @Override // sun.security.ssl.InputRecord
    Plaintext[] decode(ByteBuffer[] byteBufferArr, int i2, int i3) throws BadPaddingException, IOException {
        if (byteBufferArr == null || byteBufferArr.length == 0 || i3 == 0) {
            return new Plaintext[0];
        }
        if (i3 == 1) {
            return decode(byteBufferArr[i2]);
        }
        return decode(extract(byteBufferArr, i2, i3, 5));
    }

    private Plaintext[] decode(ByteBuffer byteBuffer) throws BadPaddingException, IOException {
        if (this.isClosed) {
            return null;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
            SSLLogger.fine("Raw read", byteBuffer);
        }
        if (!this.formatVerified) {
            this.formatVerified = true;
            byte b2 = byteBuffer.get(byteBuffer.position());
            if (b2 != ContentType.HANDSHAKE.id && b2 != ContentType.ALERT.id) {
                return handleUnknownRecord(byteBuffer);
            }
        }
        return decodeInputRecord(byteBuffer);
    }

    private Plaintext[] decodeInputRecord(ByteBuffer byteBuffer) throws BadPaddingException, IOException {
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        byte b2 = byteBuffer.get();
        byte b3 = byteBuffer.get();
        byte b4 = byteBuffer.get();
        int int16 = Record.getInt16(byteBuffer);
        if (SSLLogger.isOn && SSLLogger.isOn("record")) {
            SSLLogger.fine("READ: " + ProtocolVersion.nameOf(b3, b4) + " " + ContentType.nameOf(b2) + ", length = " + int16, new Object[0]);
        }
        if (int16 < 0 || int16 > 33088) {
            throw new SSLProtocolException("Bad input record size, TLSCiphertext.length = " + int16);
        }
        int i2 = iPosition + 5 + int16;
        byteBuffer.limit(i2);
        byteBuffer.position(iPosition + 5);
        try {
            try {
                Plaintext plaintextDecrypt = this.readCipher.decrypt(b2, byteBuffer, null);
                ByteBuffer byteBuffer2 = plaintextDecrypt.fragment;
                byte b5 = plaintextDecrypt.contentType;
                byteBuffer.limit(iLimit);
                byteBuffer.position(i2);
                if (b5 != ContentType.HANDSHAKE.id && this.handshakeBuffer != null && this.handshakeBuffer.hasRemaining()) {
                    throw new SSLProtocolException("Expecting a handshake fragment, but received " + ContentType.nameOf(b5));
                }
                if (b5 == ContentType.HANDSHAKE.id) {
                    ByteBuffer byteBuffer3 = byteBuffer2;
                    if (this.handshakeBuffer != null && this.handshakeBuffer.remaining() != 0) {
                        ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[this.handshakeBuffer.remaining() + byteBuffer2.remaining()]);
                        byteBufferWrap.put(this.handshakeBuffer);
                        byteBufferWrap.put(byteBuffer2);
                        byteBuffer3 = (ByteBuffer) byteBufferWrap.rewind();
                        this.handshakeBuffer = null;
                    }
                    ArrayList arrayList = new ArrayList(5);
                    while (true) {
                        if (!byteBuffer3.hasRemaining()) {
                            break;
                        }
                        int iRemaining = byteBuffer3.remaining();
                        if (iRemaining < 4) {
                            this.handshakeBuffer = ByteBuffer.wrap(new byte[iRemaining]);
                            this.handshakeBuffer.put(byteBuffer3);
                            this.handshakeBuffer.rewind();
                            break;
                        }
                        byteBuffer3.mark();
                        byte b6 = byteBuffer3.get();
                        if (!SSLHandshake.isKnown(b6)) {
                            throw new SSLProtocolException("Unknown handshake type size, Handshake.msg_type = " + (b6 & 255));
                        }
                        int int24 = Record.getInt24(byteBuffer3);
                        if (int24 > SSLConfiguration.maxHandshakeMessageSize) {
                            throw new SSLProtocolException("The size of the handshake message (" + int24 + ") exceeds the maximum allowed size (" + SSLConfiguration.maxHandshakeMessageSize + ")");
                        }
                        byteBuffer3.reset();
                        int i3 = 4 + int24;
                        if (iRemaining < i3) {
                            this.handshakeBuffer = ByteBuffer.wrap(new byte[iRemaining]);
                            this.handshakeBuffer.put(byteBuffer3);
                            this.handshakeBuffer.rewind();
                            break;
                        }
                        if (iRemaining == i3) {
                            if (this.handshakeHash.isHashable(b6)) {
                                this.handshakeHash.receive(byteBuffer3);
                            }
                            arrayList.add(new Plaintext(b5, b3, b4, -1, -1L, byteBuffer3));
                        } else {
                            int iPosition2 = byteBuffer3.position();
                            int iLimit2 = byteBuffer3.limit();
                            int i4 = iPosition2 + i3;
                            byteBuffer3.limit(i4);
                            if (this.handshakeHash.isHashable(b6)) {
                                this.handshakeHash.receive(byteBuffer3);
                            }
                            arrayList.add(new Plaintext(b5, b3, b4, -1, -1L, byteBuffer3.slice()));
                            byteBuffer3.position(i4);
                            byteBuffer3.limit(iLimit2);
                        }
                    }
                    return (Plaintext[]) arrayList.toArray(new Plaintext[0]);
                }
                return new Plaintext[]{new Plaintext(b5, b3, b4, -1, -1L, byteBuffer2)};
            } catch (BadPaddingException e2) {
                throw e2;
            } catch (GeneralSecurityException e3) {
                throw ((SSLProtocolException) new SSLProtocolException("Unexpected exception").initCause(e3));
            }
        } catch (Throwable th) {
            byteBuffer.limit(iLimit);
            byteBuffer.position(i2);
            throw th;
        }
    }

    private Plaintext[] handleUnknownRecord(ByteBuffer byteBuffer) throws BadPaddingException, IOException {
        int iPosition = byteBuffer.position();
        byteBuffer.limit();
        byte b2 = byteBuffer.get(iPosition);
        byte b3 = byteBuffer.get(iPosition + 2);
        if ((b2 & 128) != 0 && b3 == 1) {
            if (this.helloVersion != ProtocolVersion.SSL20Hello) {
                throw new SSLHandshakeException("SSLv2Hello is not enabled");
            }
            byte b4 = byteBuffer.get(iPosition + 3);
            byte b5 = byteBuffer.get(iPosition + 4);
            if (b4 == ProtocolVersion.SSL20Hello.major && b5 == ProtocolVersion.SSL20Hello.minor) {
                if (SSLLogger.isOn && SSLLogger.isOn("record")) {
                    SSLLogger.fine("Requested to negotiate unsupported SSLv2!", new Object[0]);
                }
                throw new UnsupportedOperationException("Unsupported SSL v2.0 ClientHello");
            }
            byteBuffer.position(iPosition + 2);
            this.handshakeHash.receive(byteBuffer);
            byteBuffer.position(iPosition);
            ByteBuffer byteBufferConvertToClientHello = convertToClientHello(byteBuffer);
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
}
