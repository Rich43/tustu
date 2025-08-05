package sun.security.ssl;

import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.ssl.Authenticator;

/* loaded from: jsse.jar:sun/security/ssl/SSLCipher.class */
enum SSLCipher {
    B_NULL("NULL", CipherType.NULL_CIPHER, 0, 0, 0, 0, true, true, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new NullReadCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_NONE), new AbstractMap.SimpleImmutableEntry(new NullReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new NullWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_NONE), new AbstractMap.SimpleImmutableEntry(new NullWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_13)}),
    B_RC4_40("RC4", CipherType.STREAM_CIPHER, 5, 16, 0, 0, true, true, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new StreamReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new StreamWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10)}),
    B_RC2_40("RC2", CipherType.BLOCK_CIPHER, 5, 16, 8, 0, false, true, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new StreamReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new StreamWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10)}),
    B_DES_40("DES/CBC/NoPadding", CipherType.BLOCK_CIPHER, 5, 8, 8, 0, true, true, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10)}),
    B_RC4_128("RC4", CipherType.STREAM_CIPHER, 16, 16, 0, 0, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new StreamReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new StreamWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_12)}),
    B_DES("DES/CBC/NoPadding", CipherType.BLOCK_CIPHER, 8, 8, 8, 0, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_11)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_11)}),
    B_3DES("DESede/CBC/NoPadding", CipherType.BLOCK_CIPHER, 24, 24, 8, 0, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_11_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_11_12)}),
    B_IDEA("IDEA", CipherType.BLOCK_CIPHER, 16, 16, 8, 0, false, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(null, ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(null, ProtocolVersion.PROTOCOLS_TO_12)}),
    B_AES_128("AES/CBC/NoPadding", CipherType.BLOCK_CIPHER, 16, 16, 16, 0, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_11_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_11_12)}),
    B_AES_256("AES/CBC/NoPadding", CipherType.BLOCK_CIPHER, 32, 32, 16, 0, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockReadCipherGenerator(), ProtocolVersion.PROTOCOLS_11_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T10BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_TO_10), new AbstractMap.SimpleImmutableEntry(new T11BlockWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_11_12)}),
    B_AES_128_GCM("AES/GCM/NoPadding", CipherType.AEAD_CIPHER, 16, 16, 12, 4, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T12GcmReadCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T12GcmWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_12)}),
    B_AES_256_GCM("AES/GCM/NoPadding", CipherType.AEAD_CIPHER, 32, 32, 12, 4, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T12GcmReadCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T12GcmWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_12)}),
    B_AES_128_GCM_IV("AES/GCM/NoPadding", CipherType.AEAD_CIPHER, 16, 16, 12, 0, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T13GcmReadCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T13GcmWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_13)}),
    B_AES_256_GCM_IV("AES/GCM/NoPadding", CipherType.AEAD_CIPHER, 32, 32, 12, 0, true, false, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T13GcmReadCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(new T13GcmWriteCipherGenerator(), ProtocolVersion.PROTOCOLS_OF_13)});

    final String description;
    final String transformation;
    final String algorithm;
    final boolean allowed;
    final int keySize;
    final int expandedKeySize;
    final int ivSize;
    final int fixedIvSize;
    final boolean exportable;
    final CipherType cipherType;
    final int tagSize = 16;
    private final boolean isAvailable;
    private final Map.Entry<ReadCipherGenerator, ProtocolVersion[]>[] readCipherGenerators;
    private final Map.Entry<WriteCipherGenerator, ProtocolVersion[]>[] writeCipherGenerators;
    private static final HashMap<String, Long> cipherLimits = new HashMap<>();
    static final String[] tag = {"KEYUPDATE"};

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$ReadCipherGenerator.class */
    interface ReadCipherGenerator {
        SSLReadCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$WriteCipherGenerator.class */
    interface WriteCipherGenerator {
        SSLWriteCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException;
    }

    static {
        long jPow;
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.ssl.SSLCipher.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("jdk.tls.keyLimits");
            }
        });
        if (str != null) {
            for (String str2 : str.split(",")) {
                String[] strArrSplit = str2.trim().toUpperCase().split(" ");
                if (!strArrSplit[1].contains(tag[0])) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.fine("jdk.tls.keyLimits:  Unknown action:  " + str2, new Object[0]);
                    }
                } else {
                    if (strArrSplit[2].indexOf("^") >= 0) {
                        try {
                            jPow = (long) Math.pow(2.0d, Integer.parseInt(strArrSplit[2].substring(r0 + 1)));
                        } catch (NumberFormatException e2) {
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                                SSLLogger.fine("jdk.tls.keyLimits:  " + e2.getMessage() + ":  " + str2, new Object[0]);
                            }
                        }
                    } else {
                        jPow = Long.parseLong(strArrSplit[2]);
                    }
                    if (jPow < 1 || jPow > 4611686018427387904L) {
                        throw new NumberFormatException("Length exceeded limits");
                    }
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.fine("jdk.tls.keyLimits:  entry = " + str2 + ". " + strArrSplit[0] + CallSiteDescriptor.TOKEN_DELIMITER + tag[0] + " = " + jPow, new Object[0]);
                    }
                    cipherLimits.put(strArrSplit[0] + CallSiteDescriptor.TOKEN_DELIMITER + tag[0], Long.valueOf(jPow));
                }
            }
        }
    }

    SSLCipher(String str, CipherType cipherType, int i2, int i3, int i4, int i5, boolean z2, boolean z3, Map.Entry[] entryArr, Map.Entry[] entryArr2) {
        this.transformation = str;
        this.algorithm = str.split("/")[0];
        this.cipherType = cipherType;
        this.description = this.algorithm + "/" + (i2 << 3);
        this.keySize = i2;
        this.ivSize = i4;
        this.fixedIvSize = i5;
        this.allowed = z2;
        this.expandedKeySize = i3;
        this.exportable = z3;
        this.isAvailable = z2 && isUnlimited(i2, str) && isTransformationAvailable(str);
        this.readCipherGenerators = entryArr;
        this.writeCipherGenerators = entryArr2;
    }

    private static boolean isTransformationAvailable(String str) {
        if (str.equals("NULL")) {
            return true;
        }
        try {
            JsseJce.getCipher(str);
            return true;
        } catch (NoSuchAlgorithmException e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.fine("Transformation " + str + " is not available.", new Object[0]);
                return false;
            }
            return false;
        }
    }

    SSLReadCipher createReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SecretKey secretKey, IvParameterSpec ivParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
        if (this.readCipherGenerators.length == 0) {
            return null;
        }
        ReadCipherGenerator key = null;
        for (Map.Entry<ReadCipherGenerator, ProtocolVersion[]> entry : this.readCipherGenerators) {
            for (ProtocolVersion protocolVersion2 : entry.getValue()) {
                if (protocolVersion == protocolVersion2) {
                    key = entry.getKey();
                }
            }
        }
        if (key != null) {
            return key.createCipher(this, authenticator, protocolVersion, this.transformation, secretKey, ivParameterSpec, secureRandom);
        }
        return null;
    }

    SSLWriteCipher createWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SecretKey secretKey, IvParameterSpec ivParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
        if (this.writeCipherGenerators.length == 0) {
            return null;
        }
        WriteCipherGenerator key = null;
        for (Map.Entry<WriteCipherGenerator, ProtocolVersion[]> entry : this.writeCipherGenerators) {
            for (ProtocolVersion protocolVersion2 : entry.getValue()) {
                if (protocolVersion == protocolVersion2) {
                    key = entry.getKey();
                }
            }
        }
        if (key != null) {
            return key.createCipher(this, authenticator, protocolVersion, this.transformation, secretKey, ivParameterSpec, secureRandom);
        }
        return null;
    }

    boolean isAvailable() {
        return this.isAvailable;
    }

    private static boolean isUnlimited(int i2, String str) {
        int i3 = i2 * 8;
        if (i3 > 128) {
            try {
                if (Cipher.getMaxAllowedKeyLength(str) < i3) {
                    return false;
                }
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
        return true;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.description;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$SSLReadCipher.class */
    static abstract class SSLReadCipher {
        final Authenticator authenticator;
        final ProtocolVersion protocolVersion;
        boolean keyLimitEnabled = false;
        long keyLimitCountdown = 0;
        SecretKey baseSecret;

        abstract Plaintext decrypt(byte b2, ByteBuffer byteBuffer, byte[] bArr) throws GeneralSecurityException;

        abstract int estimateFragmentSize(int i2, int i3);

        SSLReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion) {
            this.authenticator = authenticator;
            this.protocolVersion = protocolVersion;
        }

        static final SSLReadCipher nullTlsReadCipher() {
            try {
                return SSLCipher.B_NULL.createReadCipher(Authenticator.nullTlsMac(), ProtocolVersion.NONE, null, null, null);
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("Cannot create NULL SSLCipher", e2);
            }
        }

        void dispose() {
        }

        boolean isNullCipher() {
            return false;
        }

        public boolean atKeyLimit() {
            if (this.keyLimitCountdown >= 0) {
                return false;
            }
            this.keyLimitEnabled = false;
            return true;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$SSLWriteCipher.class */
    static abstract class SSLWriteCipher {
        final Authenticator authenticator;
        final ProtocolVersion protocolVersion;
        boolean keyLimitEnabled = false;
        long keyLimitCountdown = 0;
        SecretKey baseSecret;

        abstract int encrypt(byte b2, ByteBuffer byteBuffer);

        abstract int getExplicitNonceSize();

        abstract int calculateFragmentSize(int i2, int i3);

        abstract int calculatePacketSize(int i2, int i3);

        SSLWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion) {
            this.authenticator = authenticator;
            this.protocolVersion = protocolVersion;
        }

        static final SSLWriteCipher nullTlsWriteCipher() {
            try {
                return SSLCipher.B_NULL.createWriteCipher(Authenticator.nullTlsMac(), ProtocolVersion.NONE, null, null, null);
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("Cannot create NULL SSL write Cipher", e2);
            }
        }

        void dispose() {
        }

        boolean isCBCMode() {
            return false;
        }

        boolean isNullCipher() {
            return false;
        }

        public boolean atKeyLimit() {
            if (this.keyLimitCountdown >= 0) {
                return false;
            }
            this.keyLimitEnabled = false;
            return true;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$NullReadCipherGenerator.class */
    private static final class NullReadCipherGenerator implements ReadCipherGenerator {
        private NullReadCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.ReadCipherGenerator
        public SSLReadCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new NullReadCipher(authenticator, protocolVersion);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$NullReadCipherGenerator$NullReadCipher.class */
        static final class NullReadCipher extends SSLReadCipher {
            NullReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion) {
                super(authenticator, protocolVersion);
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            public Plaintext decrypt(byte b2, ByteBuffer byteBuffer, byte[] bArr) throws GeneralSecurityException {
                Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                if (mac.macAlg().size != 0) {
                    SSLCipher.checkStreamMac(mac, byteBuffer, b2, bArr);
                } else {
                    this.authenticator.increaseSequenceNumber();
                }
                return new Plaintext(b2, ProtocolVersion.NONE.major, ProtocolVersion.NONE.minor, -1, -1L, byteBuffer.slice());
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            int estimateFragmentSize(int i2, int i3) {
                return (i2 - i3) - ((Authenticator.MAC) this.authenticator).macAlg().size;
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            boolean isNullCipher() {
                return true;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$NullWriteCipherGenerator.class */
    private static final class NullWriteCipherGenerator implements WriteCipherGenerator {
        private NullWriteCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.WriteCipherGenerator
        public SSLWriteCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new NullWriteCipher(authenticator, protocolVersion);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$NullWriteCipherGenerator$NullWriteCipher.class */
        static final class NullWriteCipher extends SSLWriteCipher {
            NullWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion) {
                super(authenticator, protocolVersion);
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            public int encrypt(byte b2, ByteBuffer byteBuffer) {
                Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                if (mac.macAlg().size != 0) {
                    SSLCipher.addMac(mac, byteBuffer, b2);
                } else {
                    this.authenticator.increaseSequenceNumber();
                }
                int iRemaining = byteBuffer.remaining();
                byteBuffer.position(byteBuffer.limit());
                return iRemaining;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int getExplicitNonceSize() {
                return 0;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculateFragmentSize(int i2, int i3) {
                return (i2 - i3) - ((Authenticator.MAC) this.authenticator).macAlg().size;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculatePacketSize(int i2, int i3) {
                return i2 + i3 + ((Authenticator.MAC) this.authenticator).macAlg().size;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            boolean isNullCipher() {
                return true;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$StreamReadCipherGenerator.class */
    private static final class StreamReadCipherGenerator implements ReadCipherGenerator {
        private StreamReadCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.ReadCipherGenerator
        public SSLReadCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new StreamReadCipher(authenticator, protocolVersion, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$StreamReadCipherGenerator$StreamReadCipher.class */
        static final class StreamReadCipher extends SSLReadCipher {
            private final Cipher cipher;

            StreamReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                this.cipher.init(2, key, algorithmParameterSpec, secureRandom);
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            public Plaintext decrypt(byte b2, ByteBuffer byteBuffer, byte[] bArr) throws GeneralSecurityException {
                int iRemaining = byteBuffer.remaining();
                int iPosition = byteBuffer.position();
                ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                try {
                    if (iRemaining != this.cipher.update(byteBufferDuplicate, byteBuffer)) {
                        throw new RuntimeException("Unexpected number of plaintext bytes");
                    }
                    if (byteBuffer.position() != byteBufferDuplicate.position()) {
                        throw new RuntimeException("Unexpected ByteBuffer position");
                    }
                    byteBuffer.position(iPosition);
                    if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                        SSLLogger.fine("Plaintext after DECRYPTION", byteBuffer.duplicate());
                    }
                    Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                    if (mac.macAlg().size != 0) {
                        SSLCipher.checkStreamMac(mac, byteBuffer, b2, bArr);
                    } else {
                        this.authenticator.increaseSequenceNumber();
                    }
                    return new Plaintext(b2, ProtocolVersion.NONE.major, ProtocolVersion.NONE.minor, -1, -1L, byteBuffer.slice());
                } catch (ShortBufferException e2) {
                    throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e2);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            int estimateFragmentSize(int i2, int i3) {
                return (i2 - i3) - ((Authenticator.MAC) this.authenticator).macAlg().size;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$StreamWriteCipherGenerator.class */
    private static final class StreamWriteCipherGenerator implements WriteCipherGenerator {
        private StreamWriteCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.WriteCipherGenerator
        public SSLWriteCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new StreamWriteCipher(authenticator, protocolVersion, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$StreamWriteCipherGenerator$StreamWriteCipher.class */
        static final class StreamWriteCipher extends SSLWriteCipher {
            private final Cipher cipher;

            StreamWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                this.cipher.init(1, key, algorithmParameterSpec, secureRandom);
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            public int encrypt(byte b2, ByteBuffer byteBuffer) {
                Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                if (mac.macAlg().size != 0) {
                    SSLCipher.addMac(mac, byteBuffer, b2);
                } else {
                    this.authenticator.increaseSequenceNumber();
                }
                if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                    SSLLogger.finest("Padded plaintext before ENCRYPTION", byteBuffer.duplicate());
                }
                int iRemaining = byteBuffer.remaining();
                ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                try {
                    if (iRemaining != this.cipher.update(byteBufferDuplicate, byteBuffer)) {
                        throw new RuntimeException("Unexpected number of plaintext bytes");
                    }
                    if (byteBuffer.position() != byteBufferDuplicate.position()) {
                        throw new RuntimeException("Unexpected ByteBuffer position");
                    }
                    return iRemaining;
                } catch (ShortBufferException e2) {
                    throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e2);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int getExplicitNonceSize() {
                return 0;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculateFragmentSize(int i2, int i3) {
                return (i2 - i3) - ((Authenticator.MAC) this.authenticator).macAlg().size;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculatePacketSize(int i2, int i3) {
                return i2 + i3 + ((Authenticator.MAC) this.authenticator).macAlg().size;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T10BlockReadCipherGenerator.class */
    private static final class T10BlockReadCipherGenerator implements ReadCipherGenerator {
        private T10BlockReadCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.ReadCipherGenerator
        public SSLReadCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new BlockReadCipher(authenticator, protocolVersion, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T10BlockReadCipherGenerator$BlockReadCipher.class */
        static final class BlockReadCipher extends SSLReadCipher {
            private final Cipher cipher;

            BlockReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                this.cipher.init(2, key, algorithmParameterSpec, secureRandom);
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            public Plaintext decrypt(byte b2, ByteBuffer byteBuffer, byte[] bArr) throws GeneralSecurityException {
                BadPaddingException badPaddingException = null;
                Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                int iRemaining = byteBuffer.remaining();
                int i2 = mac.macAlg().size;
                if (i2 != 0 && !sanityCheck(i2, byteBuffer.remaining())) {
                    badPaddingException = new BadPaddingException("ciphertext sanity check failed");
                }
                int iRemaining2 = byteBuffer.remaining();
                int iPosition = byteBuffer.position();
                ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                try {
                    if (iRemaining2 != this.cipher.update(byteBufferDuplicate, byteBuffer)) {
                        throw new RuntimeException("Unexpected number of plaintext bytes");
                    }
                    if (byteBuffer.position() != byteBufferDuplicate.position()) {
                        throw new RuntimeException("Unexpected ByteBuffer position");
                    }
                    if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                        SSLLogger.fine("Padded plaintext after DECRYPTION", byteBuffer.duplicate().position(iPosition));
                    }
                    int blockSize = this.cipher.getBlockSize();
                    byteBuffer.position(iPosition);
                    try {
                        SSLCipher.removePadding(byteBuffer, i2, blockSize, this.protocolVersion);
                    } catch (BadPaddingException e2) {
                        if (badPaddingException == null) {
                            badPaddingException = e2;
                        }
                    }
                    try {
                        if (i2 != 0) {
                            SSLCipher.checkCBCMac(mac, byteBuffer, b2, iRemaining, bArr);
                        } else {
                            this.authenticator.increaseSequenceNumber();
                        }
                    } catch (BadPaddingException e3) {
                        if (badPaddingException == null) {
                            badPaddingException = e3;
                        }
                    }
                    if (badPaddingException != null) {
                        throw badPaddingException;
                    }
                    return new Plaintext(b2, ProtocolVersion.NONE.major, ProtocolVersion.NONE.minor, -1, -1L, byteBuffer.slice());
                } catch (ShortBufferException e4) {
                    throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e4);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            int estimateFragmentSize(int i2, int i3) {
                return ((i2 - i3) - ((Authenticator.MAC) this.authenticator).macAlg().size) - 1;
            }

            private boolean sanityCheck(int i2, int i3) {
                int blockSize = this.cipher.getBlockSize();
                if (i3 % blockSize == 0) {
                    int i4 = i2 + 1;
                    return i3 >= (i4 >= blockSize ? i4 : blockSize);
                }
                return false;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T10BlockWriteCipherGenerator.class */
    private static final class T10BlockWriteCipherGenerator implements WriteCipherGenerator {
        private T10BlockWriteCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.WriteCipherGenerator
        public SSLWriteCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new BlockWriteCipher(authenticator, protocolVersion, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T10BlockWriteCipherGenerator$BlockWriteCipher.class */
        static final class BlockWriteCipher extends SSLWriteCipher {
            private final Cipher cipher;

            BlockWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                this.cipher.init(1, key, algorithmParameterSpec, secureRandom);
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            public int encrypt(byte b2, ByteBuffer byteBuffer) {
                int iPosition = byteBuffer.position();
                Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                if (mac.macAlg().size != 0) {
                    SSLCipher.addMac(mac, byteBuffer, b2);
                } else {
                    this.authenticator.increaseSequenceNumber();
                }
                int iAddPadding = SSLCipher.addPadding(byteBuffer, this.cipher.getBlockSize());
                byteBuffer.position(iPosition);
                if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                    SSLLogger.fine("Padded plaintext before ENCRYPTION", byteBuffer.duplicate());
                }
                ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                try {
                    if (iAddPadding != this.cipher.update(byteBufferDuplicate, byteBuffer)) {
                        throw new RuntimeException("Unexpected number of plaintext bytes");
                    }
                    if (byteBuffer.position() != byteBufferDuplicate.position()) {
                        throw new RuntimeException("Unexpected ByteBuffer position");
                    }
                    return iAddPadding;
                } catch (ShortBufferException e2) {
                    throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e2);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int getExplicitNonceSize() {
                return 0;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculateFragmentSize(int i2, int i3) {
                int i4 = i2 - i3;
                return ((i4 - (i4 % this.cipher.getBlockSize())) - 1) - ((Authenticator.MAC) this.authenticator).macAlg().size;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculatePacketSize(int i2, int i3) {
                int i4 = ((Authenticator.MAC) this.authenticator).macAlg().size;
                int blockSize = this.cipher.getBlockSize();
                int i5 = i2 + i4 + 1;
                if (i5 % blockSize != 0) {
                    int i6 = i5 + (blockSize - 1);
                    i5 = i6 - (i6 % blockSize);
                }
                return i3 + i5;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            boolean isCBCMode() {
                return true;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T11BlockReadCipherGenerator.class */
    private static final class T11BlockReadCipherGenerator implements ReadCipherGenerator {
        private T11BlockReadCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.ReadCipherGenerator
        public SSLReadCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new BlockReadCipher(authenticator, protocolVersion, sSLCipher, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T11BlockReadCipherGenerator$BlockReadCipher.class */
        static final class BlockReadCipher extends SSLReadCipher {
            private final Cipher cipher;

            BlockReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SSLCipher sSLCipher, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                this.cipher.init(2, key, algorithmParameterSpec == null ? new IvParameterSpec(new byte[sSLCipher.ivSize]) : algorithmParameterSpec, secureRandom);
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            public Plaintext decrypt(byte b2, ByteBuffer byteBuffer, byte[] bArr) throws GeneralSecurityException {
                BadPaddingException badPaddingException = null;
                Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                int iRemaining = byteBuffer.remaining();
                int i2 = mac.macAlg().size;
                if (i2 != 0 && !sanityCheck(i2, byteBuffer.remaining())) {
                    badPaddingException = new BadPaddingException("ciphertext sanity check failed");
                }
                int iRemaining2 = byteBuffer.remaining();
                int iPosition = byteBuffer.position();
                ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                try {
                    if (iRemaining2 != this.cipher.update(byteBufferDuplicate, byteBuffer)) {
                        throw new RuntimeException("Unexpected number of plaintext bytes");
                    }
                    if (byteBuffer.position() != byteBufferDuplicate.position()) {
                        throw new RuntimeException("Unexpected ByteBuffer position");
                    }
                    if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                        SSLLogger.fine("Padded plaintext after DECRYPTION", byteBuffer.duplicate().position(iPosition));
                    }
                    byteBuffer.position(iPosition + this.cipher.getBlockSize());
                    int iPosition2 = byteBuffer.position();
                    int blockSize = this.cipher.getBlockSize();
                    byteBuffer.position(iPosition2);
                    try {
                        SSLCipher.removePadding(byteBuffer, i2, blockSize, this.protocolVersion);
                    } catch (BadPaddingException e2) {
                        if (badPaddingException == null) {
                            badPaddingException = e2;
                        }
                    }
                    try {
                        if (i2 != 0) {
                            SSLCipher.checkCBCMac(mac, byteBuffer, b2, iRemaining, bArr);
                        } else {
                            this.authenticator.increaseSequenceNumber();
                        }
                    } catch (BadPaddingException e3) {
                        if (badPaddingException == null) {
                            badPaddingException = e3;
                        }
                    }
                    if (badPaddingException != null) {
                        throw badPaddingException;
                    }
                    return new Plaintext(b2, ProtocolVersion.NONE.major, ProtocolVersion.NONE.minor, -1, -1L, byteBuffer.slice());
                } catch (ShortBufferException e4) {
                    throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e4);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            int estimateFragmentSize(int i2, int i3) {
                return (((i2 - i3) - this.cipher.getBlockSize()) - ((Authenticator.MAC) this.authenticator).macAlg().size) - 1;
            }

            private boolean sanityCheck(int i2, int i3) {
                int blockSize = this.cipher.getBlockSize();
                if (i3 % blockSize == 0) {
                    int i4 = i2 + 1;
                    return i3 >= (i4 >= blockSize ? i4 : blockSize) + blockSize;
                }
                return false;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T11BlockWriteCipherGenerator.class */
    private static final class T11BlockWriteCipherGenerator implements WriteCipherGenerator {
        private T11BlockWriteCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.WriteCipherGenerator
        public SSLWriteCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new BlockWriteCipher(authenticator, protocolVersion, sSLCipher, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T11BlockWriteCipherGenerator$BlockWriteCipher.class */
        static final class BlockWriteCipher extends SSLWriteCipher {
            private final Cipher cipher;
            private final SecureRandom random;

            BlockWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SSLCipher sSLCipher, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                this.random = secureRandom;
                this.cipher.init(1, key, algorithmParameterSpec == null ? new IvParameterSpec(new byte[sSLCipher.ivSize]) : algorithmParameterSpec, secureRandom);
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            public int encrypt(byte b2, ByteBuffer byteBuffer) {
                int iPosition = byteBuffer.position();
                Authenticator.MAC mac = (Authenticator.MAC) this.authenticator;
                if (mac.macAlg().size != 0) {
                    SSLCipher.addMac(mac, byteBuffer, b2);
                } else {
                    this.authenticator.increaseSequenceNumber();
                }
                byte[] bArr = new byte[this.cipher.getBlockSize()];
                this.random.nextBytes(bArr);
                int length = iPosition - bArr.length;
                byteBuffer.position(length);
                byteBuffer.put(bArr);
                byteBuffer.position(length);
                int iAddPadding = SSLCipher.addPadding(byteBuffer, this.cipher.getBlockSize());
                byteBuffer.position(length);
                if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                    SSLLogger.fine("Padded plaintext before ENCRYPTION", byteBuffer.duplicate());
                }
                ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                try {
                    if (iAddPadding != this.cipher.update(byteBufferDuplicate, byteBuffer)) {
                        throw new RuntimeException("Unexpected number of plaintext bytes");
                    }
                    if (byteBuffer.position() != byteBufferDuplicate.position()) {
                        throw new RuntimeException("Unexpected ByteBuffer position");
                    }
                    return iAddPadding;
                } catch (ShortBufferException e2) {
                    throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e2);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int getExplicitNonceSize() {
                return this.cipher.getBlockSize();
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculateFragmentSize(int i2, int i3) {
                int i4 = ((Authenticator.MAC) this.authenticator).macAlg().size;
                int blockSize = (i2 - i3) - this.cipher.getBlockSize();
                return ((blockSize - (blockSize % r0)) - 1) - i4;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculatePacketSize(int i2, int i3) {
                int i4 = ((Authenticator.MAC) this.authenticator).macAlg().size;
                int blockSize = this.cipher.getBlockSize();
                int i5 = i2 + i4 + 1;
                if (i5 % blockSize != 0) {
                    int i6 = i5 + (blockSize - 1);
                    i5 = i6 - (i6 % blockSize);
                }
                return i3 + blockSize + i5;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            boolean isCBCMode() {
                return true;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T12GcmReadCipherGenerator.class */
    private static final class T12GcmReadCipherGenerator implements ReadCipherGenerator {
        private T12GcmReadCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.ReadCipherGenerator
        public SSLReadCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new GcmReadCipher(authenticator, protocolVersion, sSLCipher, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T12GcmReadCipherGenerator$GcmReadCipher.class */
        static final class GcmReadCipher extends SSLReadCipher {
            private final Cipher cipher;
            private final int tagSize;
            private final Key key;
            private final byte[] fixedIv;
            private final int recordIvSize;
            private final SecureRandom random;

            GcmReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SSLCipher sSLCipher, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                sSLCipher.getClass();
                this.tagSize = 16;
                this.key = key;
                this.fixedIv = ((IvParameterSpec) algorithmParameterSpec).getIV();
                this.recordIvSize = sSLCipher.ivSize - sSLCipher.fixedIvSize;
                this.random = secureRandom;
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            public Plaintext decrypt(byte b2, ByteBuffer byteBuffer, byte[] bArr) throws GeneralSecurityException {
                if (byteBuffer.remaining() < this.recordIvSize + this.tagSize) {
                    throw new BadPaddingException("Insufficient buffer remaining for AEAD cipher fragment (" + byteBuffer.remaining() + "). Needs to be more than or equal to IV size (" + this.recordIvSize + ") + tag size (" + this.tagSize + ")");
                }
                byte[] bArrCopyOf = Arrays.copyOf(this.fixedIv, this.fixedIv.length + this.recordIvSize);
                byteBuffer.get(bArrCopyOf, this.fixedIv.length, this.recordIvSize);
                try {
                    this.cipher.init(2, this.key, new GCMParameterSpec(this.tagSize * 8, bArrCopyOf), this.random);
                    this.cipher.updateAAD(this.authenticator.acquireAuthenticationBytes(b2, byteBuffer.remaining() - this.tagSize, bArr));
                    int iPosition = byteBuffer.position();
                    try {
                        int iDoFinal = this.cipher.doFinal(byteBuffer.duplicate(), byteBuffer);
                        byteBuffer.position(iPosition);
                        byteBuffer.limit(iPosition + iDoFinal);
                        if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                            SSLLogger.fine("Plaintext after DECRYPTION", byteBuffer.duplicate());
                        }
                        return new Plaintext(b2, ProtocolVersion.NONE.major, ProtocolVersion.NONE.minor, -1, -1L, byteBuffer.slice());
                    } catch (IllegalBlockSizeException e2) {
                        throw new RuntimeException("Cipher error in AEAD mode \"" + e2.getMessage() + " \"in JCE provider " + this.cipher.getProvider().getName());
                    } catch (ShortBufferException e3) {
                        throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e3);
                    }
                } catch (InvalidAlgorithmParameterException | InvalidKeyException e4) {
                    throw new RuntimeException("invalid key or spec in GCM mode", e4);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            int estimateFragmentSize(int i2, int i3) {
                return ((i2 - i3) - this.recordIvSize) - this.tagSize;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T12GcmWriteCipherGenerator.class */
    private static final class T12GcmWriteCipherGenerator implements WriteCipherGenerator {
        private T12GcmWriteCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.WriteCipherGenerator
        public SSLWriteCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new GcmWriteCipher(authenticator, protocolVersion, sSLCipher, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T12GcmWriteCipherGenerator$GcmWriteCipher.class */
        private static final class GcmWriteCipher extends SSLWriteCipher {
            private final Cipher cipher;
            private final int tagSize;
            private final Key key;
            private final byte[] fixedIv;
            private final int recordIvSize;
            private final SecureRandom random;

            GcmWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SSLCipher sSLCipher, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                sSLCipher.getClass();
                this.tagSize = 16;
                this.key = key;
                this.fixedIv = ((IvParameterSpec) algorithmParameterSpec).getIV();
                this.recordIvSize = sSLCipher.ivSize - sSLCipher.fixedIvSize;
                this.random = secureRandom;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            public int encrypt(byte b2, ByteBuffer byteBuffer) {
                byte[] bArrSequenceNumber = this.authenticator.sequenceNumber();
                byte[] bArrCopyOf = Arrays.copyOf(this.fixedIv, this.fixedIv.length + bArrSequenceNumber.length);
                System.arraycopy(bArrSequenceNumber, 0, bArrCopyOf, this.fixedIv.length, bArrSequenceNumber.length);
                try {
                    this.cipher.init(1, this.key, new GCMParameterSpec(this.tagSize * 8, bArrCopyOf), this.random);
                    this.cipher.updateAAD(this.authenticator.acquireAuthenticationBytes(b2, byteBuffer.remaining(), null));
                    byteBuffer.position(byteBuffer.position() - bArrSequenceNumber.length);
                    byteBuffer.put(bArrSequenceNumber);
                    int iPosition = byteBuffer.position();
                    if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                        SSLLogger.fine("Plaintext before ENCRYPTION", byteBuffer.duplicate());
                    }
                    ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                    int outputSize = this.cipher.getOutputSize(byteBufferDuplicate.remaining());
                    if (outputSize > byteBuffer.remaining()) {
                        byteBuffer.limit(iPosition + outputSize);
                    }
                    try {
                        int iDoFinal = this.cipher.doFinal(byteBufferDuplicate, byteBuffer);
                        if (iDoFinal != outputSize) {
                            throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName());
                        }
                        return iDoFinal + bArrSequenceNumber.length;
                    } catch (BadPaddingException | IllegalBlockSizeException | ShortBufferException e2) {
                        throw new RuntimeException("Cipher error in AEAD mode in JCE provider " + this.cipher.getProvider().getName(), e2);
                    }
                } catch (InvalidAlgorithmParameterException | InvalidKeyException e3) {
                    throw new RuntimeException("invalid key or spec in GCM mode", e3);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int getExplicitNonceSize() {
                return this.recordIvSize;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculateFragmentSize(int i2, int i3) {
                return ((i2 - i3) - this.recordIvSize) - this.tagSize;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculatePacketSize(int i2, int i3) {
                return i2 + i3 + this.recordIvSize + this.tagSize;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T13GcmReadCipherGenerator.class */
    private static final class T13GcmReadCipherGenerator implements ReadCipherGenerator {
        private T13GcmReadCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.ReadCipherGenerator
        public SSLReadCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new GcmReadCipher(authenticator, protocolVersion, sSLCipher, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T13GcmReadCipherGenerator$GcmReadCipher.class */
        static final class GcmReadCipher extends SSLReadCipher {
            private final Cipher cipher;
            private final int tagSize;
            private final Key key;
            private final byte[] iv;
            private final SecureRandom random;

            GcmReadCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SSLCipher sSLCipher, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                sSLCipher.getClass();
                this.tagSize = 16;
                this.key = key;
                this.iv = ((IvParameterSpec) algorithmParameterSpec).getIV();
                this.random = secureRandom;
                this.keyLimitCountdown = ((Long) SSLCipher.cipherLimits.getOrDefault(str.toUpperCase() + CallSiteDescriptor.TOKEN_DELIMITER + SSLCipher.tag[0], 0L)).longValue();
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine("KeyLimit read side: algorithm = " + str.toUpperCase() + CallSiteDescriptor.TOKEN_DELIMITER + SSLCipher.tag[0] + "\ncountdown value = " + this.keyLimitCountdown, new Object[0]);
                }
                if (this.keyLimitCountdown > 0) {
                    this.keyLimitEnabled = true;
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            public Plaintext decrypt(byte b2, ByteBuffer byteBuffer, byte[] bArr) throws GeneralSecurityException {
                if (b2 == ContentType.CHANGE_CIPHER_SPEC.id) {
                    return new Plaintext(b2, ProtocolVersion.NONE.major, ProtocolVersion.NONE.minor, -1, -1L, byteBuffer.slice());
                }
                if (byteBuffer.remaining() <= this.tagSize) {
                    throw new BadPaddingException("Insufficient buffer remaining for AEAD cipher fragment (" + byteBuffer.remaining() + "). Needs to be more than tag size (" + this.tagSize + ")");
                }
                byte[] bArrSequenceNumber = bArr;
                if (bArrSequenceNumber == null) {
                    bArrSequenceNumber = this.authenticator.sequenceNumber();
                }
                byte[] bArr2 = (byte[]) this.iv.clone();
                int length = bArr2.length - bArrSequenceNumber.length;
                for (int i2 = 0; i2 < bArrSequenceNumber.length; i2++) {
                    int i3 = length + i2;
                    bArr2[i3] = (byte) (bArr2[i3] ^ bArrSequenceNumber[i2]);
                }
                try {
                    this.cipher.init(2, this.key, new GCMParameterSpec(this.tagSize * 8, bArr2), this.random);
                    this.cipher.updateAAD(this.authenticator.acquireAuthenticationBytes(b2, byteBuffer.remaining(), bArrSequenceNumber));
                    int iPosition = byteBuffer.position();
                    try {
                        int iDoFinal = this.cipher.doFinal(byteBuffer.duplicate(), byteBuffer);
                        byteBuffer.position(iPosition);
                        byteBuffer.limit(iPosition + iDoFinal);
                        int iLimit = byteBuffer.limit() - 1;
                        while (iLimit > 0 && byteBuffer.get(iLimit) == 0) {
                            iLimit--;
                        }
                        if (iLimit < iPosition + 1) {
                            throw new BadPaddingException("Incorrect inner plaintext: no content type");
                        }
                        byte b3 = byteBuffer.get(iLimit);
                        byteBuffer.limit(iLimit);
                        if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                            SSLLogger.fine("Plaintext after DECRYPTION", byteBuffer.duplicate());
                        }
                        if (this.keyLimitEnabled) {
                            this.keyLimitCountdown -= iDoFinal;
                        }
                        return new Plaintext(b3, ProtocolVersion.NONE.major, ProtocolVersion.NONE.minor, -1, -1L, byteBuffer.slice());
                    } catch (IllegalBlockSizeException e2) {
                        throw new RuntimeException("Cipher error in AEAD mode \"" + e2.getMessage() + " \"in JCE provider " + this.cipher.getProvider().getName());
                    } catch (ShortBufferException e3) {
                        throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName(), e3);
                    }
                } catch (InvalidAlgorithmParameterException | InvalidKeyException e4) {
                    throw new RuntimeException("invalid key or spec in GCM mode", e4);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLReadCipher
            int estimateFragmentSize(int i2, int i3) {
                return (i2 - i3) - this.tagSize;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T13GcmWriteCipherGenerator.class */
    private static final class T13GcmWriteCipherGenerator implements WriteCipherGenerator {
        private T13GcmWriteCipherGenerator() {
        }

        @Override // sun.security.ssl.SSLCipher.WriteCipherGenerator
        public SSLWriteCipher createCipher(SSLCipher sSLCipher, Authenticator authenticator, ProtocolVersion protocolVersion, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
            return new GcmWriteCipher(authenticator, protocolVersion, sSLCipher, str, key, algorithmParameterSpec, secureRandom);
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLCipher$T13GcmWriteCipherGenerator$GcmWriteCipher.class */
        private static final class GcmWriteCipher extends SSLWriteCipher {
            private final Cipher cipher;
            private final int tagSize;
            private final Key key;
            private final byte[] iv;
            private final SecureRandom random;

            GcmWriteCipher(Authenticator authenticator, ProtocolVersion protocolVersion, SSLCipher sSLCipher, String str, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws GeneralSecurityException {
                super(authenticator, protocolVersion);
                this.cipher = JsseJce.getCipher(str);
                sSLCipher.getClass();
                this.tagSize = 16;
                this.key = key;
                this.iv = ((IvParameterSpec) algorithmParameterSpec).getIV();
                this.random = secureRandom;
                this.keyLimitCountdown = ((Long) SSLCipher.cipherLimits.getOrDefault(str.toUpperCase() + CallSiteDescriptor.TOKEN_DELIMITER + SSLCipher.tag[0], 0L)).longValue();
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine("KeyLimit write side: algorithm = " + str.toUpperCase() + CallSiteDescriptor.TOKEN_DELIMITER + SSLCipher.tag[0] + "\ncountdown value = " + this.keyLimitCountdown, new Object[0]);
                }
                if (this.keyLimitCountdown > 0) {
                    this.keyLimitEnabled = true;
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            public int encrypt(byte b2, ByteBuffer byteBuffer) {
                byte[] bArrSequenceNumber = this.authenticator.sequenceNumber();
                byte[] bArr = (byte[]) this.iv.clone();
                int length = bArr.length - bArrSequenceNumber.length;
                for (int i2 = 0; i2 < bArrSequenceNumber.length; i2++) {
                    int i3 = length + i2;
                    bArr[i3] = (byte) (bArr[i3] ^ bArrSequenceNumber[i2]);
                }
                try {
                    this.cipher.init(1, this.key, new GCMParameterSpec(this.tagSize * 8, bArr), this.random);
                    int outputSize = this.cipher.getOutputSize(byteBuffer.remaining());
                    this.cipher.updateAAD(this.authenticator.acquireAuthenticationBytes(b2, outputSize, bArrSequenceNumber));
                    int iPosition = byteBuffer.position();
                    if (SSLLogger.isOn && SSLLogger.isOn("plaintext")) {
                        SSLLogger.fine("Plaintext before ENCRYPTION", byteBuffer.duplicate());
                    }
                    ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
                    if (outputSize > byteBuffer.remaining()) {
                        byteBuffer.limit(iPosition + outputSize);
                    }
                    try {
                        int iDoFinal = this.cipher.doFinal(byteBufferDuplicate, byteBuffer);
                        if (iDoFinal != outputSize) {
                            throw new RuntimeException("Cipher buffering error in JCE provider " + this.cipher.getProvider().getName());
                        }
                        if (this.keyLimitEnabled) {
                            this.keyLimitCountdown -= iDoFinal;
                        }
                        return iDoFinal;
                    } catch (BadPaddingException | IllegalBlockSizeException | ShortBufferException e2) {
                        throw new RuntimeException("Cipher error in AEAD mode in JCE provider " + this.cipher.getProvider().getName(), e2);
                    }
                } catch (InvalidAlgorithmParameterException | InvalidKeyException e3) {
                    throw new RuntimeException("invalid key or spec in GCM mode", e3);
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            void dispose() {
                if (this.cipher != null) {
                    try {
                        this.cipher.doFinal();
                    } catch (Exception e2) {
                    }
                }
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int getExplicitNonceSize() {
                return 0;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculateFragmentSize(int i2, int i3) {
                return (i2 - i3) - this.tagSize;
            }

            @Override // sun.security.ssl.SSLCipher.SSLWriteCipher
            int calculatePacketSize(int i2, int i3) {
                return i2 + i3 + this.tagSize;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addMac(Authenticator.MAC mac, ByteBuffer byteBuffer, byte b2) {
        if (mac.macAlg().size != 0) {
            int iPosition = byteBuffer.position();
            byte[] bArrCompute = mac.compute(b2, byteBuffer, false);
            byteBuffer.limit(byteBuffer.limit() + bArrCompute.length);
            byteBuffer.put(bArrCompute);
            byteBuffer.position(iPosition);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkStreamMac(Authenticator.MAC mac, ByteBuffer byteBuffer, byte b2, byte[] bArr) throws BadPaddingException {
        int i2 = mac.macAlg().size;
        if (i2 != 0) {
            if (byteBuffer.remaining() - i2 < 0) {
                throw new BadPaddingException("bad record");
            }
            if (checkMacTags(b2, byteBuffer, mac, bArr, false)) {
                throw new BadPaddingException("bad record MAC");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkCBCMac(Authenticator.MAC mac, ByteBuffer byteBuffer, byte b2, int i2, byte[] bArr) throws BadPaddingException {
        BadPaddingException badPaddingException = null;
        int i3 = mac.macAlg().size;
        int iPosition = byteBuffer.position();
        if (i3 != 0) {
            int iRemaining = byteBuffer.remaining() - i3;
            if (iRemaining < 0) {
                badPaddingException = new BadPaddingException("bad record");
                iRemaining = i2 - i3;
                byteBuffer.limit(iPosition + i2);
            }
            if (checkMacTags(b2, byteBuffer, mac, bArr, false) && badPaddingException == null) {
                badPaddingException = new BadPaddingException("bad record MAC");
            }
            checkMacTags(b2, ByteBuffer.allocate(calculateRemainingLen(mac, i2, iRemaining) + mac.macAlg().size), mac, bArr, true);
        }
        if (badPaddingException != null) {
            throw badPaddingException;
        }
    }

    private static boolean checkMacTags(byte b2, ByteBuffer byteBuffer, Authenticator.MAC mac, byte[] bArr, boolean z2) {
        int i2 = mac.macAlg().size;
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        int i3 = iLimit - i2;
        byteBuffer.limit(i3);
        byte[] bArrCompute = mac.compute(b2, byteBuffer, bArr, z2);
        if (bArrCompute == null || i2 != bArrCompute.length) {
            throw new RuntimeException("Internal MAC error");
        }
        byteBuffer.position(i3);
        byteBuffer.limit(iLimit);
        try {
            return compareMacTags(byteBuffer, bArrCompute)[0] != 0;
        } finally {
            byteBuffer.position(iPosition);
            byteBuffer.limit(i3);
        }
    }

    private static int[] compareMacTags(ByteBuffer byteBuffer, byte[] bArr) {
        int[] iArr = {0, 0};
        for (byte b2 : bArr) {
            if (byteBuffer.get() != b2) {
                iArr[0] = iArr[0] + 1;
            } else {
                iArr[1] = iArr[1] + 1;
            }
        }
        return iArr;
    }

    private static int calculateRemainingLen(Authenticator.MAC mac, int i2, int i3) {
        int i4 = mac.macAlg().hashBlockSize;
        int i5 = mac.macAlg().minimalPaddingSize;
        return 1 + (((int) (Math.ceil((i2 + (13 - (i4 - i5))) / (1.0d * i4)) - Math.ceil((i3 + (13 - (i4 - i5))) / (1.0d * i4)))) * i4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int addPadding(ByteBuffer byteBuffer, int i2) {
        int iRemaining = byteBuffer.remaining();
        int iPosition = byteBuffer.position();
        int i3 = iRemaining + 1;
        if (i3 % i2 != 0) {
            int i4 = i3 + (i2 - 1);
            i3 = i4 - (i4 % i2);
        }
        int i5 = (byte) (i3 - iRemaining);
        byteBuffer.limit(i3 + iPosition);
        int i6 = iPosition + iRemaining;
        for (int i7 = 0; i7 < i5; i7++) {
            int i8 = i6;
            i6++;
            byteBuffer.put(i8, (byte) (i5 - 1));
        }
        byteBuffer.position(i6);
        byteBuffer.limit(i6);
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int removePadding(ByteBuffer byteBuffer, int i2, int i3, ProtocolVersion protocolVersion) throws BadPaddingException {
        int iRemaining = byteBuffer.remaining();
        int iPosition = byteBuffer.position();
        int i4 = byteBuffer.get((iPosition + iRemaining) - 1) & 255;
        int i5 = iRemaining - (i4 + 1);
        if (i5 - i2 < 0) {
            checkPadding(byteBuffer.duplicate(), (byte) (i4 & 255));
            throw new BadPaddingException("Invalid Padding length: " + i4);
        }
        int[] iArrCheckPadding = checkPadding((ByteBuffer) byteBuffer.duplicate().position(iPosition + i5), (byte) (i4 & 255));
        if (protocolVersion.useTLS10PlusSpec()) {
            if (iArrCheckPadding[0] != 0) {
                throw new BadPaddingException("Invalid TLS padding data");
            }
        } else if (i4 > i3) {
            throw new BadPaddingException("Padding length (" + i4 + ") of SSLv3 message should not be bigger than the block size (" + i3 + ")");
        }
        byteBuffer.limit(iPosition + i5);
        return i5;
    }

    private static int[] checkPadding(ByteBuffer byteBuffer, byte b2) {
        if (!byteBuffer.hasRemaining()) {
            throw new RuntimeException("hasRemaining() must be positive");
        }
        int[] iArr = {0, 0};
        byteBuffer.mark();
        int i2 = 0;
        while (i2 <= 256) {
            while (byteBuffer.hasRemaining() && i2 <= 256) {
                if (byteBuffer.get() != b2) {
                    iArr[0] = iArr[0] + 1;
                } else {
                    iArr[1] = iArr[1] + 1;
                }
                i2++;
            }
            byteBuffer.reset();
        }
        return iArr;
    }
}
