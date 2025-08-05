package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import javax.net.ssl.SSLHandshakeException;
import sun.security.ssl.SSLCipher;

/* loaded from: jsse.jar:sun/security/ssl/SSLEngineOutputRecord.class */
final class SSLEngineOutputRecord extends OutputRecord implements SSLRecord {
    private HandshakeFragment fragmenter;
    private boolean isTalkingToV2;
    private ByteBuffer v2ClientHello;
    private volatile boolean isCloseWaiting;

    SSLEngineOutputRecord(HandshakeHash handshakeHash) {
        super(handshakeHash, SSLCipher.SSLWriteCipher.nullTlsWriteCipher());
        this.fragmenter = null;
        this.isTalkingToV2 = false;
        this.v2ClientHello = null;
        this.isCloseWaiting = false;
        this.packetSize = SSLRecord.maxRecordSize;
        this.protocolVersion = ProtocolVersion.NONE;
    }

    @Override // sun.security.ssl.OutputRecord, java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.isClosed) {
            if (this.fragmenter != null && this.fragmenter.hasAlert()) {
                this.isCloseWaiting = true;
            } else {
                super.close();
            }
        }
    }

    @Override // sun.security.ssl.OutputRecord
    boolean isClosed() {
        return this.isClosed || this.isCloseWaiting;
    }

    @Override // sun.security.ssl.OutputRecord
    void encodeAlert(byte b2, byte b3) throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound alert message: " + Alert.nameOf(b3), new Object[0]);
                return;
            }
            return;
        }
        if (this.fragmenter == null) {
            this.fragmenter = new HandshakeFragment();
        }
        this.fragmenter.queueUpAlert(b2, b3);
    }

    @Override // sun.security.ssl.OutputRecord
    void encodeHandshake(byte[] bArr, int i2, int i3) throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound handshake message", ByteBuffer.wrap(bArr, i2, i3));
                return;
            }
            return;
        }
        if (this.fragmenter == null) {
            this.fragmenter = new HandshakeFragment();
        }
        if (this.firstMessage) {
            this.firstMessage = false;
            if (this.helloVersion == ProtocolVersion.SSL20Hello && bArr[i2] == SSLHandshake.CLIENT_HELLO.id && bArr[i2 + 4 + 2 + 32] == 0) {
                this.v2ClientHello = encodeV2ClientHello(bArr, i2 + 4, i3 - 4);
                this.v2ClientHello.position(2);
                this.handshakeHash.deliver(this.v2ClientHello);
                this.v2ClientHello.position(0);
                return;
            }
        }
        if (this.handshakeHash.isHashable(bArr[i2])) {
            this.handshakeHash.deliver(bArr, i2, i3);
        }
        this.fragmenter.queueUpFragment(bArr, i2, i3);
    }

    @Override // sun.security.ssl.OutputRecord
    void encodeChangeCipherSpec() throws IOException {
        if (isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound change_cipher_spec message", new Object[0]);
                return;
            }
            return;
        }
        if (this.fragmenter == null) {
            this.fragmenter = new HandshakeFragment();
        }
        this.fragmenter.queueUpChangeCipherSpec();
    }

    @Override // sun.security.ssl.OutputRecord
    void encodeV2NoCipher() throws IOException {
        this.isTalkingToV2 = true;
    }

    @Override // sun.security.ssl.OutputRecord
    Ciphertext encode(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws IOException {
        if (this.isClosed) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound application data or cached messages", new Object[0]);
                return null;
            }
            return null;
        }
        if (this.isCloseWaiting) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound application data", new Object[0]);
            }
            byteBufferArr = null;
        }
        return encode(byteBufferArr, i2, i3, byteBufferArr2[0]);
    }

    private Ciphertext encode(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer byteBuffer) throws IOException {
        int iMin;
        int iCalculateFragmentSize;
        if (this.writeCipher.authenticator.seqNumOverflow()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.fine("sequence number extremely close to overflow (2^64-1 packets). Closing connection.", new Object[0]);
            }
            throw new SSLHandshakeException("sequence number overflow");
        }
        Ciphertext ciphertextAcquireCiphertext = acquireCiphertext(byteBuffer);
        if (ciphertextAcquireCiphertext != null) {
            return ciphertextAcquireCiphertext;
        }
        if (byteBufferArr == null || byteBufferArr.length == 0) {
            return null;
        }
        int iRemaining = 0;
        for (int i4 = i2; i4 < i2 + i3; i4++) {
            iRemaining += byteBufferArr[i4].remaining();
        }
        if (iRemaining == 0) {
            return null;
        }
        int iLimit = byteBuffer.limit();
        boolean z2 = true;
        int iMin2 = Math.min(SSLRecord.maxRecordSize, this.packetSize);
        boolean z3 = true;
        long jEncrypt = 0;
        while (z3) {
            if (z2 && needToSplitPayload()) {
                z3 = true;
                iCalculateFragmentSize = 1;
                z2 = false;
            } else {
                z3 = false;
                if (iMin2 > 0) {
                    iMin = Math.min(this.writeCipher.calculateFragmentSize(iMin2, 5), 16384);
                } else {
                    iMin = 16384;
                }
                iCalculateFragmentSize = calculateFragmentSize(iMin);
            }
            int iPosition = byteBuffer.position();
            int explicitNonceSize = iPosition + 5 + this.writeCipher.getExplicitNonceSize();
            byteBuffer.position(explicitNonceSize);
            int iMin3 = Math.min(iCalculateFragmentSize, byteBuffer.remaining());
            int i5 = 0;
            int i6 = i2 + i3;
            for (int i7 = i2; i7 < i6 && iMin3 > 0; i7++) {
                int iMin4 = Math.min(byteBufferArr[i7].remaining(), iMin3);
                int iLimit2 = byteBufferArr[i7].limit();
                byteBufferArr[i7].limit(byteBufferArr[i7].position() + iMin4);
                byteBuffer.put(byteBufferArr[i7]);
                byteBufferArr[i7].limit(iLimit2);
                iMin3 -= iMin4;
                i5 += iMin4;
                if (iMin3 > 0) {
                    i2++;
                    i3--;
                }
            }
            byteBuffer.limit(byteBuffer.position());
            byteBuffer.position(explicitNonceSize);
            if (SSLLogger.isOn && SSLLogger.isOn("record")) {
                SSLLogger.fine("WRITE: " + ((Object) this.protocolVersion) + " " + ContentType.APPLICATION_DATA.name + ", length = " + byteBuffer.remaining(), new Object[0]);
            }
            jEncrypt = encrypt(this.writeCipher, ContentType.APPLICATION_DATA.id, byteBuffer, iPosition, iLimit, 5, this.protocolVersion);
            if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                byteBufferDuplicate.limit(byteBufferDuplicate.position());
                byteBufferDuplicate.position(iPosition);
                SSLLogger.fine("Raw write", byteBufferDuplicate);
            }
            iMin2 -= byteBuffer.position() - iPosition;
            byteBuffer.limit(iLimit);
            if (this.isFirstAppOutputRecord) {
                this.isFirstAppOutputRecord = false;
            }
        }
        return new Ciphertext(ContentType.APPLICATION_DATA.id, SSLHandshake.NOT_APPLICABLE.id, jEncrypt);
    }

    private Ciphertext acquireCiphertext(ByteBuffer byteBuffer) throws IOException {
        if (this.isTalkingToV2) {
            byteBuffer.put(SSLRecord.v2NoCipher);
            if (SSLLogger.isOn && SSLLogger.isOn("packet")) {
                SSLLogger.fine("Raw write", SSLRecord.v2NoCipher);
            }
            this.isTalkingToV2 = false;
            return new Ciphertext(ContentType.ALERT.id, SSLHandshake.NOT_APPLICABLE.id, -1L);
        }
        if (this.v2ClientHello != null) {
            if (SSLLogger.isOn) {
                if (SSLLogger.isOn("record")) {
                    SSLLogger.fine(Thread.currentThread().getName() + ", WRITE: SSLv2 ClientHello message, length = " + this.v2ClientHello.remaining(), new Object[0]);
                }
                if (SSLLogger.isOn("packet")) {
                    SSLLogger.fine("Raw write", this.v2ClientHello);
                }
            }
            byteBuffer.put(this.v2ClientHello);
            this.v2ClientHello = null;
            return new Ciphertext(ContentType.HANDSHAKE.id, SSLHandshake.CLIENT_HELLO.id, -1L);
        }
        if (this.fragmenter != null) {
            return this.fragmenter.acquireCiphertext(byteBuffer);
        }
        return null;
    }

    @Override // sun.security.ssl.OutputRecord
    boolean isEmpty() {
        return !this.isTalkingToV2 && this.v2ClientHello == null && (this.fragmenter == null || this.fragmenter.isEmpty());
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLEngineOutputRecord$RecordMemo.class */
    private static class RecordMemo {
        byte contentType;
        byte majorVersion;
        byte minorVersion;
        SSLCipher.SSLWriteCipher encodeCipher;
        byte[] fragment;

        private RecordMemo() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLEngineOutputRecord$HandshakeMemo.class */
    private static class HandshakeMemo extends RecordMemo {
        byte handshakeType;
        int acquireOffset;

        private HandshakeMemo() {
            super();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLEngineOutputRecord$HandshakeFragment.class */
    final class HandshakeFragment {
        private LinkedList<RecordMemo> handshakeMemos = new LinkedList<>();

        HandshakeFragment() {
        }

        void queueUpFragment(byte[] bArr, int i2, int i3) throws IOException {
            HandshakeMemo handshakeMemo = new HandshakeMemo();
            handshakeMemo.contentType = ContentType.HANDSHAKE.id;
            handshakeMemo.majorVersion = SSLEngineOutputRecord.this.protocolVersion.major;
            handshakeMemo.minorVersion = SSLEngineOutputRecord.this.protocolVersion.minor;
            handshakeMemo.encodeCipher = SSLEngineOutputRecord.this.writeCipher;
            handshakeMemo.handshakeType = bArr[i2];
            handshakeMemo.acquireOffset = 0;
            handshakeMemo.fragment = new byte[i3 - 4];
            System.arraycopy(bArr, i2 + 4, handshakeMemo.fragment, 0, i3 - 4);
            this.handshakeMemos.add(handshakeMemo);
        }

        void queueUpChangeCipherSpec() {
            RecordMemo recordMemo = new RecordMemo();
            recordMemo.contentType = ContentType.CHANGE_CIPHER_SPEC.id;
            recordMemo.majorVersion = SSLEngineOutputRecord.this.protocolVersion.major;
            recordMemo.minorVersion = SSLEngineOutputRecord.this.protocolVersion.minor;
            recordMemo.encodeCipher = SSLEngineOutputRecord.this.writeCipher;
            recordMemo.fragment = new byte[1];
            recordMemo.fragment[0] = 1;
            this.handshakeMemos.add(recordMemo);
        }

        void queueUpAlert(byte b2, byte b3) {
            RecordMemo recordMemo = new RecordMemo();
            recordMemo.contentType = ContentType.ALERT.id;
            recordMemo.majorVersion = SSLEngineOutputRecord.this.protocolVersion.major;
            recordMemo.minorVersion = SSLEngineOutputRecord.this.protocolVersion.minor;
            recordMemo.encodeCipher = SSLEngineOutputRecord.this.writeCipher;
            recordMemo.fragment = new byte[2];
            recordMemo.fragment[0] = b2;
            recordMemo.fragment[1] = b3;
            this.handshakeMemos.add(recordMemo);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0116  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x015b A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        sun.security.ssl.Ciphertext acquireCiphertext(java.nio.ByteBuffer r10) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 643
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.security.ssl.SSLEngineOutputRecord.HandshakeFragment.acquireCiphertext(java.nio.ByteBuffer):sun.security.ssl.Ciphertext");
        }

        boolean isEmpty() {
            return this.handshakeMemos.isEmpty();
        }

        boolean hasAlert() {
            Iterator<RecordMemo> it = this.handshakeMemos.iterator();
            while (it.hasNext()) {
                if (it.next().contentType == ContentType.ALERT.id) {
                    return true;
                }
            }
            return false;
        }
    }

    boolean needToSplitPayload() {
        return !this.protocolVersion.useTLS11PlusSpec() && this.writeCipher.isCBCMode() && !this.isFirstAppOutputRecord && Record.enableCBCProtection;
    }
}
