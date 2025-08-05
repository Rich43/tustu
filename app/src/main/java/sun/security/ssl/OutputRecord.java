package sun.security.ssl;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import sun.security.ssl.SSLCipher;

/* loaded from: jsse.jar:sun/security/ssl/OutputRecord.class */
abstract class OutputRecord extends ByteArrayOutputStream implements Record, Closeable {
    SSLCipher.SSLWriteCipher writeCipher;
    TransportContext tc;
    final HandshakeHash handshakeHash;
    ProtocolVersion protocolVersion;
    ProtocolVersion helloVersion;
    int packetSize;
    volatile boolean isClosed;
    private static final int[] V3toV2CipherMap1 = {-1, -1, -1, 2, 1, -1, 4, 5, -1, 6, 7};
    private static final int[] V3toV2CipherMap3 = {-1, -1, -1, 128, 128, -1, 128, 128, -1, 64, 192};
    private static final byte[] HANDSHAKE_MESSAGE_KEY_UPDATE = {SSLHandshake.KEY_UPDATE.id, 0, 0, 1, 0};
    boolean isFirstAppOutputRecord = true;
    boolean firstMessage = true;
    private int fragmentSize = 16384;

    abstract void encodeAlert(byte b2, byte b3) throws IOException;

    abstract void encodeHandshake(byte[] bArr, int i2, int i3) throws IOException;

    abstract void encodeChangeCipherSpec() throws IOException;

    OutputRecord(HandshakeHash handshakeHash, SSLCipher.SSLWriteCipher sSLWriteCipher) {
        this.writeCipher = sSLWriteCipher;
        this.handshakeHash = handshakeHash;
    }

    synchronized void setVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    synchronized void setHelloVersion(ProtocolVersion protocolVersion) {
        this.helloVersion = protocolVersion;
    }

    boolean isEmpty() {
        return false;
    }

    synchronized boolean seqNumIsHuge() {
        return this.writeCipher.authenticator != null && this.writeCipher.authenticator.seqNumIsHuge();
    }

    Ciphertext encode(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws IOException {
        throw new UnsupportedOperationException();
    }

    void encodeV2NoCipher() throws IOException {
        throw new UnsupportedOperationException();
    }

    void deliver(byte[] bArr, int i2, int i3) throws IOException {
        throw new UnsupportedOperationException();
    }

    void setDeliverStream(OutputStream outputStream) {
        throw new UnsupportedOperationException();
    }

    synchronized void changeWriteCiphers(SSLCipher.SSLWriteCipher sSLWriteCipher, boolean z2) throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound change_cipher_spec message", new Object[0]);
                return;
            }
            return;
        }
        if (z2) {
            encodeChangeCipherSpec();
        }
        sSLWriteCipher.dispose();
        this.writeCipher = sSLWriteCipher;
        this.isFirstAppOutputRecord = true;
    }

    synchronized void changeWriteCiphers(SSLCipher.SSLWriteCipher sSLWriteCipher, byte b2) throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound key_update handshake message", new Object[0]);
                return;
            }
            return;
        }
        byte[] bArr = (byte[]) HANDSHAKE_MESSAGE_KEY_UPDATE.clone();
        bArr[bArr.length - 1] = b2;
        encodeHandshake(bArr, 0, bArr.length);
        flush();
        sSLWriteCipher.dispose();
        this.writeCipher = sSLWriteCipher;
        this.isFirstAppOutputRecord = true;
    }

    synchronized void changePacketSize(int i2) {
        this.packetSize = i2;
    }

    synchronized void changeFragmentSize(int i2) {
        this.fragmentSize = i2;
    }

    synchronized int getMaxPacketSize() {
        return this.packetSize;
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.isClosed) {
            return;
        }
        this.isClosed = true;
        this.writeCipher.dispose();
    }

    boolean isClosed() {
        return this.isClosed;
    }

    /* loaded from: jsse.jar:sun/security/ssl/OutputRecord$T13PaddingHolder.class */
    private static final class T13PaddingHolder {
        private static final byte[] zeros = new byte[16];

        private T13PaddingHolder() {
        }
    }

    int calculateFragmentSize(int i2) {
        if (this.fragmentSize > 0) {
            i2 = Math.min(i2, this.fragmentSize);
        }
        if (this.protocolVersion.useTLS13PlusSpec()) {
            return (i2 - T13PaddingHolder.zeros.length) - 1;
        }
        return i2;
    }

    static long encrypt(SSLCipher.SSLWriteCipher sSLWriteCipher, byte b2, ByteBuffer byteBuffer, int i2, int i3, int i4, ProtocolVersion protocolVersion) {
        if (protocolVersion.useTLS13PlusSpec()) {
            return t13Encrypt(sSLWriteCipher, b2, byteBuffer, i2, i3, i4, protocolVersion);
        }
        return t10Encrypt(sSLWriteCipher, b2, byteBuffer, i2, i3, i4, protocolVersion);
    }

    private static long t13Encrypt(SSLCipher.SSLWriteCipher sSLWriteCipher, byte b2, ByteBuffer byteBuffer, int i2, int i3, int i4, ProtocolVersion protocolVersion) {
        if (!sSLWriteCipher.isNullCipher()) {
            int iLimit = byteBuffer.limit();
            int iPosition = byteBuffer.position();
            byteBuffer.position(iLimit);
            byteBuffer.limit(iLimit + 1 + T13PaddingHolder.zeros.length);
            byteBuffer.put(b2);
            byteBuffer.put(T13PaddingHolder.zeros);
            byteBuffer.position(iPosition);
        }
        ProtocolVersion protocolVersion2 = protocolVersion;
        if (!sSLWriteCipher.isNullCipher()) {
            protocolVersion2 = ProtocolVersion.TLS12;
            b2 = ContentType.APPLICATION_DATA.id;
        } else if (protocolVersion.useTLS13PlusSpec()) {
            protocolVersion2 = ProtocolVersion.TLS12;
        }
        byte[] bArrSequenceNumber = sSLWriteCipher.authenticator.sequenceNumber();
        sSLWriteCipher.encrypt(b2, byteBuffer);
        int iLimit2 = (byteBuffer.limit() - i2) - i4;
        byteBuffer.put(i2, b2);
        byteBuffer.put(i2 + 1, protocolVersion2.major);
        byteBuffer.put(i2 + 2, protocolVersion2.minor);
        byteBuffer.put(i2 + 3, (byte) (iLimit2 >> 8));
        byteBuffer.put(i2 + 4, (byte) iLimit2);
        byteBuffer.position(byteBuffer.limit());
        return Authenticator.toLong(bArrSequenceNumber);
    }

    private static long t10Encrypt(SSLCipher.SSLWriteCipher sSLWriteCipher, byte b2, ByteBuffer byteBuffer, int i2, int i3, int i4, ProtocolVersion protocolVersion) {
        byte[] bArrSequenceNumber = sSLWriteCipher.authenticator.sequenceNumber();
        sSLWriteCipher.encrypt(b2, byteBuffer);
        int iLimit = (byteBuffer.limit() - i2) - i4;
        byteBuffer.put(i2, b2);
        byteBuffer.put(i2 + 1, protocolVersion.major);
        byteBuffer.put(i2 + 2, protocolVersion.minor);
        byteBuffer.put(i2 + 3, (byte) (iLimit >> 8));
        byteBuffer.put(i2 + 4, (byte) iLimit);
        byteBuffer.position(byteBuffer.limit());
        return Authenticator.toLong(bArrSequenceNumber);
    }

    long encrypt(SSLCipher.SSLWriteCipher sSLWriteCipher, byte b2, int i2) {
        if (this.protocolVersion.useTLS13PlusSpec()) {
            return t13Encrypt(sSLWriteCipher, b2, i2);
        }
        return t10Encrypt(sSLWriteCipher, b2, i2);
    }

    private long t13Encrypt(SSLCipher.SSLWriteCipher sSLWriteCipher, byte b2, int i2) {
        ProtocolVersion protocolVersion;
        if (!sSLWriteCipher.isNullCipher()) {
            write(b2);
            write(T13PaddingHolder.zeros, 0, T13PaddingHolder.zeros.length);
        }
        byte[] bArrSequenceNumber = sSLWriteCipher.authenticator.sequenceNumber();
        int i3 = this.count - i2;
        int iCalculatePacketSize = sSLWriteCipher.calculatePacketSize(i3, i2);
        if (iCalculatePacketSize > this.buf.length) {
            byte[] bArr = new byte[iCalculatePacketSize];
            System.arraycopy(this.buf, 0, bArr, 0, this.count);
            this.buf = bArr;
        }
        ProtocolVersion protocolVersion2 = this.protocolVersion;
        if (!sSLWriteCipher.isNullCipher()) {
            protocolVersion = ProtocolVersion.TLS12;
            b2 = ContentType.APPLICATION_DATA.id;
        } else {
            protocolVersion = ProtocolVersion.TLS12;
        }
        this.count = i2 + sSLWriteCipher.encrypt(b2, ByteBuffer.wrap(this.buf, i2, i3));
        int i4 = this.count - i2;
        this.buf[0] = b2;
        this.buf[1] = protocolVersion.major;
        this.buf[2] = protocolVersion.minor;
        this.buf[3] = (byte) ((i4 >> 8) & 255);
        this.buf[4] = (byte) (i4 & 255);
        return Authenticator.toLong(bArrSequenceNumber);
    }

    private long t10Encrypt(SSLCipher.SSLWriteCipher sSLWriteCipher, byte b2, int i2) {
        byte[] bArrSequenceNumber = sSLWriteCipher.authenticator.sequenceNumber();
        int explicitNonceSize = i2 + this.writeCipher.getExplicitNonceSize();
        int i3 = this.count - explicitNonceSize;
        int iCalculatePacketSize = sSLWriteCipher.calculatePacketSize(i3, i2);
        if (iCalculatePacketSize > this.buf.length) {
            byte[] bArr = new byte[iCalculatePacketSize];
            System.arraycopy(this.buf, 0, bArr, 0, this.count);
            this.buf = bArr;
        }
        this.count = i2 + sSLWriteCipher.encrypt(b2, ByteBuffer.wrap(this.buf, explicitNonceSize, i3));
        int i4 = this.count - i2;
        this.buf[0] = b2;
        this.buf[1] = this.protocolVersion.major;
        this.buf[2] = this.protocolVersion.minor;
        this.buf[3] = (byte) ((i4 >> 8) & 255);
        this.buf[4] = (byte) (i4 & 255);
        return Authenticator.toLong(bArrSequenceNumber);
    }

    static ByteBuffer encodeV2ClientHello(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = i2 + 34;
        int i5 = i4 + 1 + bArr[i4];
        int i6 = (((bArr[i5] & 255) << 8) + (bArr[i5 + 1] & 255)) / 2;
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[11 + (i6 * 6) + 3 + 32]);
        int i7 = i5 + 2;
        int iV3toV2CipherSuite = 0;
        byteBufferWrap.position(11);
        boolean z2 = false;
        for (int i8 = 0; i8 < i6; i8++) {
            int i9 = i7;
            int i10 = i7 + 1;
            byte b2 = bArr[i9];
            i7 = i10 + 1;
            byte b3 = bArr[i10];
            iV3toV2CipherSuite += V3toV2CipherSuite(byteBufferWrap, b2, b3);
            if (!z2 && b2 == 0 && b3 == -1) {
                z2 = true;
            }
        }
        if (!z2) {
            iV3toV2CipherSuite += V3toV2CipherSuite(byteBufferWrap, (byte) 0, (byte) -1);
        }
        byteBufferWrap.put(bArr, i2 + 2, 32);
        int iPosition = byteBufferWrap.position() - 2;
        byteBufferWrap.position(0);
        byteBufferWrap.put((byte) (128 | ((iPosition >>> 8) & 255)));
        byteBufferWrap.put((byte) (iPosition & 255));
        byteBufferWrap.put(SSLHandshake.CLIENT_HELLO.id);
        byteBufferWrap.put(bArr[i2]);
        byteBufferWrap.put(bArr[i2 + 1]);
        byteBufferWrap.put((byte) (iV3toV2CipherSuite >>> 8));
        byteBufferWrap.put((byte) (iV3toV2CipherSuite & 255));
        byteBufferWrap.put((byte) 0);
        byteBufferWrap.put((byte) 0);
        byteBufferWrap.put((byte) 0);
        byteBufferWrap.put((byte) 32);
        byteBufferWrap.position(0);
        byteBufferWrap.limit(iPosition + 2);
        return byteBufferWrap;
    }

    private static int V3toV2CipherSuite(ByteBuffer byteBuffer, byte b2, byte b3) {
        byteBuffer.put((byte) 0);
        byteBuffer.put(b2);
        byteBuffer.put(b3);
        if ((b3 & 255) > 10 || V3toV2CipherMap1[b3] == -1) {
            return 3;
        }
        byteBuffer.put((byte) V3toV2CipherMap1[b3]);
        byteBuffer.put((byte) 0);
        byteBuffer.put((byte) V3toV2CipherMap3[b3]);
        return 6;
    }
}
