package sun.security.ssl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLHandshakeException;
import sun.security.ssl.SSLCipher;

/* loaded from: jsse.jar:sun/security/ssl/SSLSocketOutputRecord.class */
final class SSLSocketOutputRecord extends OutputRecord implements SSLRecord {
    private OutputStream deliverStream;

    SSLSocketOutputRecord(HandshakeHash handshakeHash) {
        this(handshakeHash, null);
    }

    SSLSocketOutputRecord(HandshakeHash handshakeHash, TransportContext transportContext) {
        super(handshakeHash, SSLCipher.SSLWriteCipher.nullTlsWriteCipher());
        this.deliverStream = null;
        this.tc = transportContext;
        this.packetSize = SSLRecord.maxRecordSize;
        this.protocolVersion = ProtocolVersion.NONE;
    }

    @Override // sun.security.ssl.OutputRecord
    synchronized void encodeAlert(byte b2, byte b3) throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound alert message: " + Alert.nameOf(b3), new Object[0]);
                return;
            }
            return;
        }
        this.count = 5 + this.writeCipher.getExplicitNonceSize();
        write(b2);
        write(b3);
        if (SSLLogger.isOn && SSLLogger.isOn("record")) {
            SSLLogger.fine("WRITE: " + ((Object) this.protocolVersion) + " " + ContentType.ALERT.name + "(" + Alert.nameOf(b3) + "), length = " + (this.count - 5), new Object[0]);
        }
        encrypt(this.writeCipher, ContentType.ALERT.id, 5);
        this.deliverStream.write(this.buf, 0, this.count);
        this.deliverStream.flush();
        if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
            SSLLogger.fine("Raw write", new ByteArrayInputStream(this.buf, 0, this.count));
        }
        this.count = 0;
    }

    @Override // sun.security.ssl.OutputRecord
    synchronized void encodeHandshake(byte[] bArr, int i2, int i3) throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound handshake message", ByteBuffer.wrap(bArr, i2, i3));
                return;
            }
            return;
        }
        if (this.firstMessage) {
            this.firstMessage = false;
            if (this.helloVersion == ProtocolVersion.SSL20Hello && bArr[i2] == SSLHandshake.CLIENT_HELLO.id && bArr[i2 + 4 + 2 + 32] == 0) {
                ByteBuffer byteBufferEncodeV2ClientHello = encodeV2ClientHello(bArr, i2 + 4, i3 - 4);
                byte[] bArrArray = byteBufferEncodeV2ClientHello.array();
                int iLimit = byteBufferEncodeV2ClientHello.limit();
                this.handshakeHash.deliver(bArrArray, 2, iLimit - 2);
                if (SSLLogger.isOn && SSLLogger.isOn("record")) {
                    SSLLogger.fine("WRITE: SSLv2 ClientHello message, length = " + iLimit, new Object[0]);
                }
                this.deliverStream.write(bArrArray, 0, iLimit);
                this.deliverStream.flush();
                if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                    SSLLogger.fine("Raw write", new ByteArrayInputStream(bArrArray, 0, iLimit));
                    return;
                }
                return;
            }
        }
        if (this.handshakeHash.isHashable(bArr[0])) {
            this.handshakeHash.deliver(bArr, i2, i3);
        }
        int fragLimit = getFragLimit();
        int explicitNonceSize = 5 + this.writeCipher.getExplicitNonceSize();
        if (this.count == 0) {
            this.count = explicitNonceSize;
        }
        if (this.count - explicitNonceSize < fragLimit - i3) {
            write(bArr, i2, i3);
            return;
        }
        int i4 = i2 + i3;
        while (i2 < i4) {
            int i5 = (i4 - i2) + (this.count - explicitNonceSize);
            int iMin = Math.min(fragLimit, i5);
            write(bArr, i2, iMin);
            if (i5 < fragLimit) {
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("record")) {
                SSLLogger.fine("WRITE: " + ((Object) this.protocolVersion) + " " + ContentType.HANDSHAKE.name + ", length = " + (this.count - 5), new Object[0]);
            }
            encrypt(this.writeCipher, ContentType.HANDSHAKE.id, 5);
            this.deliverStream.write(this.buf, 0, this.count);
            this.deliverStream.flush();
            if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                SSLLogger.fine("Raw write", new ByteArrayInputStream(this.buf, 0, this.count));
            }
            i2 += iMin;
            this.count = explicitNonceSize;
        }
    }

    @Override // sun.security.ssl.OutputRecord
    synchronized void encodeChangeCipherSpec() throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound change_cipher_spec message", new Object[0]);
                return;
            }
            return;
        }
        this.count = 5 + this.writeCipher.getExplicitNonceSize();
        write(1);
        encrypt(this.writeCipher, ContentType.CHANGE_CIPHER_SPEC.id, 5);
        this.deliverStream.write(this.buf, 0, this.count);
        if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
            SSLLogger.fine("Raw write", new ByteArrayInputStream(this.buf, 0, this.count));
        }
        this.count = 0;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        if (this.count <= 5 + this.writeCipher.getExplicitNonceSize()) {
            return;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("record")) {
            SSLLogger.fine("WRITE: " + ((Object) this.protocolVersion) + " " + ContentType.HANDSHAKE.name + ", length = " + (this.count - 5), new Object[0]);
        }
        encrypt(this.writeCipher, ContentType.HANDSHAKE.id, 5);
        this.deliverStream.write(this.buf, 0, this.count);
        this.deliverStream.flush();
        if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
            SSLLogger.fine("Raw write", new ByteArrayInputStream(this.buf, 0, this.count));
        }
        this.count = 0;
    }

    @Override // sun.security.ssl.OutputRecord
    synchronized void deliver(byte[] bArr, int i2, int i3) throws IOException {
        int iMin;
        int iMin2;
        if (isClosed()) {
            throw new SocketException("Connection or outbound has been closed");
        }
        if (this.writeCipher.authenticator.seqNumOverflow()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.fine("sequence number extremely close to overflow (2^64-1 packets). Closing connection.", new Object[0]);
            }
            throw new SSLHandshakeException("sequence number overflow");
        }
        boolean z2 = true;
        int i4 = i2 + i3;
        while (i2 < i4) {
            if (this.packetSize > 0) {
                iMin = Math.min(this.writeCipher.calculateFragmentSize(Math.min(SSLRecord.maxRecordSize, this.packetSize), 5), 16384);
            } else {
                iMin = 16384;
            }
            int iCalculateFragmentSize = calculateFragmentSize(iMin);
            if (z2 && needToSplitPayload()) {
                iMin2 = 1;
                z2 = false;
            } else {
                iMin2 = Math.min(iCalculateFragmentSize, i4 - i2);
            }
            int explicitNonceSize = 5 + this.writeCipher.getExplicitNonceSize();
            this.count = explicitNonceSize;
            write(bArr, i2, iMin2);
            if (SSLLogger.isOn && SSLLogger.isOn("record")) {
                SSLLogger.fine("WRITE: " + ((Object) this.protocolVersion) + " " + ContentType.APPLICATION_DATA.name + ", length = " + (this.count - explicitNonceSize), new Object[0]);
            }
            encrypt(this.writeCipher, ContentType.APPLICATION_DATA.id, 5);
            this.deliverStream.write(this.buf, 0, this.count);
            this.deliverStream.flush();
            if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                SSLLogger.fine("Raw write", new ByteArrayInputStream(this.buf, 0, this.count));
            }
            this.count = 0;
            if (this.isFirstAppOutputRecord) {
                this.isFirstAppOutputRecord = false;
            }
            i2 += iMin2;
        }
    }

    @Override // sun.security.ssl.OutputRecord
    synchronized void setDeliverStream(OutputStream outputStream) {
        this.deliverStream = outputStream;
    }

    private boolean needToSplitPayload() {
        return !this.protocolVersion.useTLS11PlusSpec() && this.writeCipher.isCBCMode() && !this.isFirstAppOutputRecord && Record.enableCBCProtection;
    }

    private int getFragLimit() {
        int iMin;
        if (this.packetSize > 0) {
            iMin = Math.min(this.writeCipher.calculateFragmentSize(Math.min(SSLRecord.maxRecordSize, this.packetSize), 5), 16384);
        } else {
            iMin = 16384;
        }
        return calculateFragmentSize(iMin);
    }
}
