package com.sun.crypto.provider;

import com.sun.glass.ui.Platform;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.security.internal.spec.TlsKeyMaterialParameterSpec;
import sun.security.internal.spec.TlsKeyMaterialSpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/TlsKeyMaterialGenerator.class */
public final class TlsKeyMaterialGenerator extends KeyGeneratorSpi {
    private static final String MSG = "TlsKeyMaterialGenerator must be initialized using a TlsKeyMaterialParameterSpec";
    private TlsKeyMaterialParameterSpec spec;
    private int protocolVersion;

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof TlsKeyMaterialParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsKeyMaterialParameterSpec) algorithmParameterSpec;
        if (!"RAW".equals(this.spec.getMasterSecret().getFormat())) {
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
        if (this.spec == null) {
            throw new IllegalStateException("TlsKeyMaterialGenerator must be initialized");
        }
        try {
            return engineGenerateKey0();
        } catch (GeneralSecurityException e2) {
            throw new ProviderException(e2);
        }
    }

    private SecretKey engineGenerateKey0() throws GeneralSecurityException {
        byte[] bArrDoTLS10PRF;
        SecretKeySpec secretKeySpec;
        SecretKeySpec secretKeySpec2;
        byte[] encoded = this.spec.getMasterSecret().getEncoded();
        byte[] clientRandom = this.spec.getClientRandom();
        byte[] serverRandom = this.spec.getServerRandom();
        SecretKeySpec secretKeySpec3 = null;
        SecretKeySpec secretKeySpec4 = null;
        IvParameterSpec ivParameterSpec = null;
        IvParameterSpec ivParameterSpec2 = null;
        int macKeyLength = this.spec.getMacKeyLength();
        int expandedCipherKeyLength = this.spec.getExpandedCipherKeyLength();
        boolean z2 = expandedCipherKeyLength != 0;
        int cipherKeyLength = this.spec.getCipherKeyLength();
        int ivLength = this.spec.getIvLength();
        int i2 = ((macKeyLength + cipherKeyLength) + (z2 ? 0 : ivLength)) << 1;
        byte[] bArr = new byte[i2];
        MessageDigest messageDigest = null;
        MessageDigest messageDigest2 = null;
        if (this.protocolVersion >= 771) {
            bArrDoTLS10PRF = TlsPrfGenerator.doTLS12PRF(encoded, TlsPrfGenerator.LABEL_KEY_EXPANSION, TlsPrfGenerator.concat(serverRandom, clientRandom), i2, this.spec.getPRFHashAlg(), this.spec.getPRFHashLength(), this.spec.getPRFBlockSize());
        } else if (this.protocolVersion >= 769) {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest2 = MessageDigest.getInstance("SHA1");
            bArrDoTLS10PRF = TlsPrfGenerator.doTLS10PRF(encoded, TlsPrfGenerator.LABEL_KEY_EXPANSION, TlsPrfGenerator.concat(serverRandom, clientRandom), i2, messageDigest, messageDigest2);
        } else {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest2 = MessageDigest.getInstance("SHA1");
            bArrDoTLS10PRF = new byte[i2];
            byte[] bArr2 = new byte[20];
            int i3 = 0;
            for (int i4 = i2; i4 > 0; i4 -= 16) {
                messageDigest2.update(TlsPrfGenerator.SSL3_CONST[i3]);
                messageDigest2.update(encoded);
                messageDigest2.update(serverRandom);
                messageDigest2.update(clientRandom);
                messageDigest2.digest(bArr2, 0, 20);
                messageDigest.update(encoded);
                messageDigest.update(bArr2);
                if (i4 >= 16) {
                    messageDigest.digest(bArrDoTLS10PRF, i3 << 4, 16);
                } else {
                    messageDigest.digest(bArr2, 0, 16);
                    System.arraycopy(bArr2, 0, bArrDoTLS10PRF, i3 << 4, i4);
                }
                i3++;
            }
        }
        int i5 = 0;
        if (macKeyLength != 0) {
            byte[] bArr3 = new byte[macKeyLength];
            System.arraycopy(bArrDoTLS10PRF, 0, bArr3, 0, macKeyLength);
            int i6 = 0 + macKeyLength;
            secretKeySpec3 = new SecretKeySpec(bArr3, Platform.MAC);
            System.arraycopy(bArrDoTLS10PRF, i6, bArr3, 0, macKeyLength);
            i5 = i6 + macKeyLength;
            secretKeySpec4 = new SecretKeySpec(bArr3, Platform.MAC);
        }
        if (cipherKeyLength == 0) {
            return new TlsKeyMaterialSpec(secretKeySpec3, secretKeySpec4);
        }
        String cipherAlgorithm = this.spec.getCipherAlgorithm();
        byte[] bArr4 = new byte[cipherKeyLength];
        System.arraycopy(bArrDoTLS10PRF, i5, bArr4, 0, cipherKeyLength);
        int i7 = i5 + cipherKeyLength;
        byte[] bArr5 = new byte[cipherKeyLength];
        System.arraycopy(bArrDoTLS10PRF, i7, bArr5, 0, cipherKeyLength);
        int i8 = i7 + cipherKeyLength;
        if (!z2) {
            secretKeySpec = new SecretKeySpec(bArr4, cipherAlgorithm);
            secretKeySpec2 = new SecretKeySpec(bArr5, cipherAlgorithm);
            if (ivLength != 0) {
                byte[] bArr6 = new byte[ivLength];
                System.arraycopy(bArrDoTLS10PRF, i8, bArr6, 0, ivLength);
                int i9 = i8 + ivLength;
                ivParameterSpec = new IvParameterSpec(bArr6);
                System.arraycopy(bArrDoTLS10PRF, i9, bArr6, 0, ivLength);
                int i10 = i9 + ivLength;
                ivParameterSpec2 = new IvParameterSpec(bArr6);
            }
        } else {
            if (this.protocolVersion >= 770) {
                throw new RuntimeException("Internal Error:  TLS 1.1+ should not be negotiatingexportable ciphersuites");
            }
            if (this.protocolVersion == 769) {
                byte[] bArrConcat = TlsPrfGenerator.concat(clientRandom, serverRandom);
                secretKeySpec = new SecretKeySpec(TlsPrfGenerator.doTLS10PRF(bArr4, TlsPrfGenerator.LABEL_CLIENT_WRITE_KEY, bArrConcat, expandedCipherKeyLength, messageDigest, messageDigest2), cipherAlgorithm);
                secretKeySpec2 = new SecretKeySpec(TlsPrfGenerator.doTLS10PRF(bArr5, TlsPrfGenerator.LABEL_SERVER_WRITE_KEY, bArrConcat, expandedCipherKeyLength, messageDigest, messageDigest2), cipherAlgorithm);
                if (ivLength != 0) {
                    byte[] bArr7 = new byte[ivLength];
                    byte[] bArrDoTLS10PRF2 = TlsPrfGenerator.doTLS10PRF(null, TlsPrfGenerator.LABEL_IV_BLOCK, bArrConcat, ivLength << 1, messageDigest, messageDigest2);
                    System.arraycopy(bArrDoTLS10PRF2, 0, bArr7, 0, ivLength);
                    ivParameterSpec = new IvParameterSpec(bArr7);
                    System.arraycopy(bArrDoTLS10PRF2, ivLength, bArr7, 0, ivLength);
                    ivParameterSpec2 = new IvParameterSpec(bArr7);
                }
            } else {
                byte[] bArr8 = new byte[expandedCipherKeyLength];
                messageDigest.update(bArr4);
                messageDigest.update(clientRandom);
                messageDigest.update(serverRandom);
                System.arraycopy(messageDigest.digest(), 0, bArr8, 0, expandedCipherKeyLength);
                secretKeySpec = new SecretKeySpec(bArr8, cipherAlgorithm);
                messageDigest.update(bArr5);
                messageDigest.update(serverRandom);
                messageDigest.update(clientRandom);
                System.arraycopy(messageDigest.digest(), 0, bArr8, 0, expandedCipherKeyLength);
                secretKeySpec2 = new SecretKeySpec(bArr8, cipherAlgorithm);
                if (ivLength != 0) {
                    byte[] bArr9 = new byte[ivLength];
                    messageDigest.update(clientRandom);
                    messageDigest.update(serverRandom);
                    System.arraycopy(messageDigest.digest(), 0, bArr9, 0, ivLength);
                    ivParameterSpec = new IvParameterSpec(bArr9);
                    messageDigest.update(serverRandom);
                    messageDigest.update(clientRandom);
                    System.arraycopy(messageDigest.digest(), 0, bArr9, 0, ivLength);
                    ivParameterSpec2 = new IvParameterSpec(bArr9);
                }
            }
        }
        return new TlsKeyMaterialSpec(secretKeySpec3, secretKeySpec4, secretKeySpec, ivParameterSpec, secretKeySpec2, ivParameterSpec2);
    }
}
