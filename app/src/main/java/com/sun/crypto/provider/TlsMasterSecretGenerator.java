package com.sun.crypto.provider;

import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import sun.security.internal.interfaces.TlsMasterSecret;
import sun.security.internal.spec.TlsMasterSecretParameterSpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/TlsMasterSecretGenerator.class */
public final class TlsMasterSecretGenerator extends KeyGeneratorSpi {
    private static final String MSG = "TlsMasterSecretGenerator must be initialized using a TlsMasterSecretParameterSpec";
    private TlsMasterSecretParameterSpec spec;
    private int protocolVersion;

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof TlsMasterSecretParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsMasterSecretParameterSpec) algorithmParameterSpec;
        if (!"RAW".equals(this.spec.getPremasterSecret().getFormat())) {
            throw new InvalidAlgorithmParameterException("Key format must be RAW");
        }
        this.protocolVersion = (this.spec.getMajorVersion() << 8) | this.spec.getMinorVersion();
        if (this.protocolVersion < 768 || this.protocolVersion > 771) {
            throw new InvalidAlgorithmParameterException("Only SSL 3.0, TLS 1.0/1.1/1.2 supported");
        }
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        int i2;
        int i3;
        byte[] bArr;
        byte[] bArr2;
        byte[] bArrConcat;
        byte[] bArrDoTLS10PRF;
        if (this.spec == null) {
            throw new IllegalStateException("TlsMasterSecretGenerator must be initialized");
        }
        SecretKey premasterSecret = this.spec.getPremasterSecret();
        byte[] encoded = premasterSecret.getEncoded();
        if (premasterSecret.getAlgorithm().equals("TlsRsaPremasterSecret")) {
            i2 = encoded[0] & 255;
            i3 = encoded[1] & 255;
        } else {
            i2 = -1;
            i3 = -1;
        }
        try {
            if (this.protocolVersion >= 769) {
                byte[] extendedMasterSecretSessionHash = this.spec.getExtendedMasterSecretSessionHash();
                if (extendedMasterSecretSessionHash.length != 0) {
                    bArr2 = TlsPrfGenerator.LABEL_EXTENDED_MASTER_SECRET;
                    bArrConcat = extendedMasterSecretSessionHash;
                } else {
                    byte[] clientRandom = this.spec.getClientRandom();
                    byte[] serverRandom = this.spec.getServerRandom();
                    bArr2 = TlsPrfGenerator.LABEL_MASTER_SECRET;
                    bArrConcat = TlsPrfGenerator.concat(clientRandom, serverRandom);
                }
                if (this.protocolVersion >= 771) {
                    bArrDoTLS10PRF = TlsPrfGenerator.doTLS12PRF(encoded, bArr2, bArrConcat, 48, this.spec.getPRFHashAlg(), this.spec.getPRFHashLength(), this.spec.getPRFBlockSize());
                } else {
                    bArrDoTLS10PRF = TlsPrfGenerator.doTLS10PRF(encoded, bArr2, bArrConcat, 48);
                }
                bArr = bArrDoTLS10PRF;
            } else {
                bArr = new byte[48];
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                MessageDigest messageDigest2 = MessageDigest.getInstance("SHA");
                byte[] clientRandom2 = this.spec.getClientRandom();
                byte[] serverRandom2 = this.spec.getServerRandom();
                byte[] bArr3 = new byte[20];
                for (int i4 = 0; i4 < 3; i4++) {
                    messageDigest2.update(TlsPrfGenerator.SSL3_CONST[i4]);
                    messageDigest2.update(encoded);
                    messageDigest2.update(clientRandom2);
                    messageDigest2.update(serverRandom2);
                    messageDigest2.digest(bArr3, 0, 20);
                    messageDigest.update(encoded);
                    messageDigest.update(bArr3);
                    messageDigest.digest(bArr, i4 << 4, 16);
                }
            }
            return new TlsMasterSecretKey(bArr, i2, i3);
        } catch (DigestException e2) {
            throw new ProviderException(e2);
        } catch (NoSuchAlgorithmException e3) {
            throw new ProviderException(e3);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/TlsMasterSecretGenerator$TlsMasterSecretKey.class */
    private static final class TlsMasterSecretKey implements TlsMasterSecret {
        private static final long serialVersionUID = 1019571680375368880L;
        private byte[] key;
        private final int majorVersion;
        private final int minorVersion;

        TlsMasterSecretKey(byte[] bArr, int i2, int i3) {
            this.key = bArr;
            this.majorVersion = i2;
            this.minorVersion = i3;
        }

        @Override // sun.security.internal.interfaces.TlsMasterSecret
        public int getMajorVersion() {
            return this.majorVersion;
        }

        @Override // sun.security.internal.interfaces.TlsMasterSecret
        public int getMinorVersion() {
            return this.minorVersion;
        }

        @Override // java.security.Key
        public String getAlgorithm() {
            return "TlsMasterSecret";
        }

        @Override // java.security.Key
        public String getFormat() {
            return "RAW";
        }

        @Override // java.security.Key
        public byte[] getEncoded() {
            return (byte[]) this.key.clone();
        }
    }
}
