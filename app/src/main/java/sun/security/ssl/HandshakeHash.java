package sun.security.ssl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import javax.crypto.SecretKey;
import sun.security.util.MessageDigestSpi2;

/* loaded from: jsse.jar:sun/security/ssl/HandshakeHash.class */
final class HandshakeHash {
    private TranscriptHash transcriptHash = new CacheOnlyHash();
    private LinkedList<byte[]> reserves = new LinkedList<>();
    private boolean hasBeenUsed = false;

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$TranscriptHash.class */
    interface TranscriptHash {
        void update(byte[] bArr, int i2, int i3);

        byte[] digest();

        byte[] archived();
    }

    HandshakeHash() {
    }

    void determine(ProtocolVersion protocolVersion, CipherSuite cipherSuite) {
        if (!(this.transcriptHash instanceof CacheOnlyHash)) {
            throw new IllegalStateException("Not expected instance of transcript hash");
        }
        CacheOnlyHash cacheOnlyHash = (CacheOnlyHash) this.transcriptHash;
        if (protocolVersion.useTLS13PlusSpec()) {
            this.transcriptHash = new T13HandshakeHash(cipherSuite);
        } else if (protocolVersion.useTLS12PlusSpec()) {
            this.transcriptHash = new T12HandshakeHash(cipherSuite);
        } else if (protocolVersion.useTLS10PlusSpec()) {
            this.transcriptHash = new T10HandshakeHash(cipherSuite);
        } else {
            this.transcriptHash = new S30HandshakeHash(cipherSuite);
        }
        byte[] byteArray = cacheOnlyHash.baos.toByteArray();
        if (byteArray.length != 0) {
            this.transcriptHash.update(byteArray, 0, byteArray.length);
        }
    }

    HandshakeHash copy() {
        if (this.transcriptHash instanceof CacheOnlyHash) {
            HandshakeHash handshakeHash = new HandshakeHash();
            handshakeHash.transcriptHash = ((CacheOnlyHash) this.transcriptHash).copy();
            handshakeHash.reserves = new LinkedList<>(this.reserves);
            handshakeHash.hasBeenUsed = this.hasBeenUsed;
            return handshakeHash;
        }
        throw new IllegalStateException("Hash does not support copying");
    }

    void receive(byte[] bArr) {
        this.reserves.add(Arrays.copyOf(bArr, bArr.length));
    }

    void receive(ByteBuffer byteBuffer, int i2) {
        if (byteBuffer.hasArray()) {
            int iPosition = byteBuffer.position() + byteBuffer.arrayOffset();
            this.reserves.add(Arrays.copyOfRange(byteBuffer.array(), iPosition, iPosition + i2));
        } else {
            int iPosition2 = byteBuffer.position();
            byte[] bArr = new byte[i2];
            byteBuffer.get(bArr);
            byteBuffer.position(iPosition2);
            this.reserves.add(Arrays.copyOf(bArr, bArr.length));
        }
    }

    void receive(ByteBuffer byteBuffer) {
        receive(byteBuffer, byteBuffer.remaining());
    }

    void push(byte[] bArr) {
        this.reserves.push(Arrays.copyOf(bArr, bArr.length));
    }

    byte[] removeLastReceived() {
        return this.reserves.removeLast();
    }

    void deliver(byte[] bArr) {
        update();
        this.transcriptHash.update(bArr, 0, bArr.length);
    }

    void deliver(byte[] bArr, int i2, int i3) {
        update();
        this.transcriptHash.update(bArr, i2, i3);
    }

    void deliver(ByteBuffer byteBuffer) {
        update();
        if (byteBuffer.hasArray()) {
            this.transcriptHash.update(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), byteBuffer.remaining());
            return;
        }
        int iPosition = byteBuffer.position();
        byte[] bArr = new byte[byteBuffer.remaining()];
        byteBuffer.get(bArr);
        byteBuffer.position(iPosition);
        this.transcriptHash.update(bArr, 0, bArr.length);
    }

    void utilize() {
        if (!this.hasBeenUsed && this.reserves.size() != 0) {
            byte[] bArrRemove = this.reserves.remove();
            this.transcriptHash.update(bArrRemove, 0, bArrRemove.length);
            this.hasBeenUsed = true;
        }
    }

    void consume() {
        if (this.hasBeenUsed) {
            this.hasBeenUsed = false;
        } else if (this.reserves.size() != 0) {
            byte[] bArrRemove = this.reserves.remove();
            this.transcriptHash.update(bArrRemove, 0, bArrRemove.length);
        }
    }

    void update() {
        while (this.reserves.size() != 0) {
            byte[] bArrRemove = this.reserves.remove();
            this.transcriptHash.update(bArrRemove, 0, bArrRemove.length);
        }
        this.hasBeenUsed = false;
    }

    byte[] digest() {
        return this.transcriptHash.digest();
    }

    void finish() {
        this.transcriptHash = new CacheOnlyHash();
        this.reserves = new LinkedList<>();
        this.hasBeenUsed = false;
    }

    byte[] archived() {
        return this.transcriptHash.archived();
    }

    byte[] digest(String str) {
        return ((T10HandshakeHash) this.transcriptHash).digest(str);
    }

    byte[] digest(String str, SecretKey secretKey) {
        return ((S30HandshakeHash) this.transcriptHash).digest(str, secretKey);
    }

    byte[] digest(boolean z2, SecretKey secretKey) {
        return ((S30HandshakeHash) this.transcriptHash).digest(z2, secretKey);
    }

    public boolean isHashable(byte b2) {
        return b2 != SSLHandshake.HELLO_REQUEST.id;
    }

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$CacheOnlyHash.class */
    private static final class CacheOnlyHash implements TranscriptHash {
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        CacheOnlyHash() {
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public void update(byte[] bArr, int i2, int i3) {
            this.baos.write(bArr, i2, i3);
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] digest() {
            throw new IllegalStateException("Not expected call to handshake hash digest");
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] archived() {
            return this.baos.toByteArray();
        }

        CacheOnlyHash copy() {
            CacheOnlyHash cacheOnlyHash = new CacheOnlyHash();
            try {
                this.baos.writeTo(cacheOnlyHash.baos);
                return cacheOnlyHash;
            } catch (IOException e2) {
                throw new RuntimeException("unable to to clone hash state");
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$S30HandshakeHash.class */
    static final class S30HandshakeHash implements TranscriptHash {
        static final byte[] MD5_pad1 = genPad(54, 48);
        static final byte[] MD5_pad2 = genPad(92, 48);
        static final byte[] SHA_pad1 = genPad(54, 40);
        static final byte[] SHA_pad2 = genPad(92, 40);
        private static final byte[] SSL_CLIENT = {67, 76, 78, 84};
        private static final byte[] SSL_SERVER = {83, 82, 86, 82};
        private final MessageDigest mdMD5 = JsseJce.getMessageDigest("MD5");
        private final MessageDigest mdSHA = JsseJce.getMessageDigest("SHA");
        private final TranscriptHash md5;
        private final TranscriptHash sha;
        private final ByteArrayOutputStream baos;

        S30HandshakeHash(CipherSuite cipherSuite) {
            boolean z2 = false;
            if (this.mdMD5 instanceof Cloneable) {
                this.md5 = new CloneableHash(this.mdMD5);
            } else {
                z2 = true;
                this.md5 = new NonCloneableHash(this.mdMD5);
            }
            if (this.mdSHA instanceof Cloneable) {
                this.sha = new CloneableHash(this.mdSHA);
            } else {
                z2 = true;
                this.sha = new NonCloneableHash(this.mdSHA);
            }
            if (z2) {
                this.baos = null;
            } else {
                this.baos = new ByteArrayOutputStream();
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public void update(byte[] bArr, int i2, int i3) {
            this.md5.update(bArr, i2, i3);
            this.sha.update(bArr, i2, i3);
            if (this.baos != null) {
                this.baos.write(bArr, i2, i3);
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] digest() {
            byte[] bArr = new byte[36];
            System.arraycopy(this.md5.digest(), 0, bArr, 0, 16);
            System.arraycopy(this.sha.digest(), 0, bArr, 16, 20);
            return bArr;
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] archived() {
            if (this.baos != null) {
                return this.baos.toByteArray();
            }
            if (this.md5 instanceof NonCloneableHash) {
                return this.md5.archived();
            }
            return this.sha.archived();
        }

        byte[] digest(boolean z2, SecretKey secretKey) throws Exception {
            MessageDigest messageDigestCloneMd5 = cloneMd5();
            MessageDigest messageDigestCloneSha = cloneSha();
            if (z2) {
                messageDigestCloneMd5.update(SSL_CLIENT);
                messageDigestCloneSha.update(SSL_CLIENT);
            } else {
                messageDigestCloneMd5.update(SSL_SERVER);
                messageDigestCloneSha.update(SSL_SERVER);
            }
            updateDigest(messageDigestCloneMd5, MD5_pad1, MD5_pad2, secretKey);
            updateDigest(messageDigestCloneSha, SHA_pad1, SHA_pad2, secretKey);
            byte[] bArr = new byte[36];
            System.arraycopy(messageDigestCloneMd5.digest(), 0, bArr, 0, 16);
            System.arraycopy(messageDigestCloneSha.digest(), 0, bArr, 16, 20);
            return bArr;
        }

        byte[] digest(String str, SecretKey secretKey) throws Exception {
            if ("RSA".equalsIgnoreCase(str)) {
                MessageDigest messageDigestCloneMd5 = cloneMd5();
                MessageDigest messageDigestCloneSha = cloneSha();
                updateDigest(messageDigestCloneMd5, MD5_pad1, MD5_pad2, secretKey);
                updateDigest(messageDigestCloneSha, SHA_pad1, SHA_pad2, secretKey);
                byte[] bArr = new byte[36];
                System.arraycopy(messageDigestCloneMd5.digest(), 0, bArr, 0, 16);
                System.arraycopy(messageDigestCloneSha.digest(), 0, bArr, 16, 20);
                return bArr;
            }
            MessageDigest messageDigestCloneSha2 = cloneSha();
            updateDigest(messageDigestCloneSha2, SHA_pad1, SHA_pad2, secretKey);
            return messageDigestCloneSha2.digest();
        }

        private static byte[] genPad(int i2, int i3) {
            byte[] bArr = new byte[i3];
            Arrays.fill(bArr, (byte) i2);
            return bArr;
        }

        private MessageDigest cloneMd5() {
            MessageDigest messageDigest;
            if (this.mdMD5 instanceof Cloneable) {
                try {
                    messageDigest = (MessageDigest) this.mdMD5.clone();
                } catch (CloneNotSupportedException e2) {
                    throw new RuntimeException("MessageDigest does no support clone operation");
                }
            } else {
                messageDigest = JsseJce.getMessageDigest("MD5");
                messageDigest.update(this.md5.archived());
            }
            return messageDigest;
        }

        private MessageDigest cloneSha() {
            MessageDigest messageDigest;
            if (this.mdSHA instanceof Cloneable) {
                try {
                    messageDigest = (MessageDigest) this.mdSHA.clone();
                } catch (CloneNotSupportedException e2) {
                    throw new RuntimeException("MessageDigest does no support clone operation");
                }
            } else {
                messageDigest = JsseJce.getMessageDigest("SHA");
                messageDigest.update(this.sha.archived());
            }
            return messageDigest;
        }

        private static void updateDigest(MessageDigest messageDigest, byte[] bArr, byte[] bArr2, SecretKey secretKey) throws Exception {
            byte[] encoded = "RAW".equals(secretKey.getFormat()) ? secretKey.getEncoded() : null;
            if (encoded != null) {
                messageDigest.update(encoded);
            } else {
                digestKey(messageDigest, secretKey);
            }
            messageDigest.update(bArr);
            byte[] bArrDigest = messageDigest.digest();
            if (encoded != null) {
                messageDigest.update(encoded);
            } else {
                digestKey(messageDigest, secretKey);
            }
            messageDigest.update(bArr2);
            messageDigest.update(bArrDigest);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static void digestKey(MessageDigest messageDigest, SecretKey secretKey) throws Exception {
            try {
                if (messageDigest instanceof MessageDigestSpi2) {
                    ((MessageDigestSpi2) messageDigest).engineUpdate(secretKey);
                    return;
                }
                throw new Exception("Digest does not support implUpdate(SecretKey)");
            } catch (Exception e2) {
                throw new RuntimeException("Could not obtain encoded key and MessageDigest cannot digest key", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$T10HandshakeHash.class */
    static final class T10HandshakeHash implements TranscriptHash {
        private final TranscriptHash md5;
        private final TranscriptHash sha;
        private final ByteArrayOutputStream baos;

        T10HandshakeHash(CipherSuite cipherSuite) {
            MessageDigest messageDigest = JsseJce.getMessageDigest("MD5");
            MessageDigest messageDigest2 = JsseJce.getMessageDigest("SHA");
            boolean z2 = false;
            if (messageDigest instanceof Cloneable) {
                this.md5 = new CloneableHash(messageDigest);
            } else {
                z2 = true;
                this.md5 = new NonCloneableHash(messageDigest);
            }
            if (messageDigest2 instanceof Cloneable) {
                this.sha = new CloneableHash(messageDigest2);
            } else {
                z2 = true;
                this.sha = new NonCloneableHash(messageDigest2);
            }
            if (z2) {
                this.baos = null;
            } else {
                this.baos = new ByteArrayOutputStream();
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public void update(byte[] bArr, int i2, int i3) {
            this.md5.update(bArr, i2, i3);
            this.sha.update(bArr, i2, i3);
            if (this.baos != null) {
                this.baos.write(bArr, i2, i3);
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] digest() {
            byte[] bArr = new byte[36];
            System.arraycopy(this.md5.digest(), 0, bArr, 0, 16);
            System.arraycopy(this.sha.digest(), 0, bArr, 16, 20);
            return bArr;
        }

        byte[] digest(String str) {
            if ("RSA".equalsIgnoreCase(str)) {
                return digest();
            }
            return this.sha.digest();
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] archived() {
            if (this.baos != null) {
                return this.baos.toByteArray();
            }
            if (this.md5 instanceof NonCloneableHash) {
                return this.md5.archived();
            }
            return this.sha.archived();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$T12HandshakeHash.class */
    static final class T12HandshakeHash implements TranscriptHash {
        private final TranscriptHash transcriptHash;
        private final ByteArrayOutputStream baos;

        T12HandshakeHash(CipherSuite cipherSuite) {
            MessageDigest messageDigest = JsseJce.getMessageDigest(cipherSuite.hashAlg.name);
            if (messageDigest instanceof Cloneable) {
                this.transcriptHash = new CloneableHash(messageDigest);
                this.baos = new ByteArrayOutputStream();
            } else {
                this.transcriptHash = new NonCloneableHash(messageDigest);
                this.baos = null;
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public void update(byte[] bArr, int i2, int i3) {
            this.transcriptHash.update(bArr, i2, i3);
            if (this.baos != null) {
                this.baos.write(bArr, i2, i3);
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] digest() {
            return this.transcriptHash.digest();
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] archived() {
            if (this.baos != null) {
                return this.baos.toByteArray();
            }
            return this.transcriptHash.archived();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$T13HandshakeHash.class */
    static final class T13HandshakeHash implements TranscriptHash {
        private final TranscriptHash transcriptHash;

        T13HandshakeHash(CipherSuite cipherSuite) {
            MessageDigest messageDigest = JsseJce.getMessageDigest(cipherSuite.hashAlg.name);
            if (messageDigest instanceof Cloneable) {
                this.transcriptHash = new CloneableHash(messageDigest);
            } else {
                this.transcriptHash = new NonCloneableHash(messageDigest);
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public void update(byte[] bArr, int i2, int i3) {
            this.transcriptHash.update(bArr, i2, i3);
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] digest() {
            return this.transcriptHash.digest();
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] archived() {
            throw new UnsupportedOperationException("TLS 1.3 does not require archived.");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$CloneableHash.class */
    static final class CloneableHash implements TranscriptHash {
        private final MessageDigest md;

        CloneableHash(MessageDigest messageDigest) {
            this.md = messageDigest;
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public void update(byte[] bArr, int i2, int i3) {
            this.md.update(bArr, i2, i3);
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] digest() {
            try {
                return ((MessageDigest) this.md.clone()).digest();
            } catch (CloneNotSupportedException e2) {
                return new byte[0];
            }
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] archived() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HandshakeHash$NonCloneableHash.class */
    static final class NonCloneableHash implements TranscriptHash {
        private final MessageDigest md;
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        NonCloneableHash(MessageDigest messageDigest) {
            this.md = messageDigest;
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public void update(byte[] bArr, int i2, int i3) {
            this.baos.write(bArr, i2, i3);
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] digest() {
            byte[] byteArray = this.baos.toByteArray();
            this.md.reset();
            return this.md.digest(byteArray);
        }

        @Override // sun.security.ssl.HandshakeHash.TranscriptHash
        public byte[] archived() {
            return this.baos.toByteArray();
        }
    }
}
