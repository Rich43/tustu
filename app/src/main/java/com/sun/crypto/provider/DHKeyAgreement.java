package com.sun.crypto.provider;

import java.math.BigInteger;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.security.util.KeyUtil;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHKeyAgreement.class */
public final class DHKeyAgreement extends KeyAgreementSpi {
    private boolean generateSecret = false;
    private BigInteger init_p = null;
    private BigInteger init_g = null;

    /* renamed from: x, reason: collision with root package name */
    private BigInteger f11810x = BigInteger.ZERO;

    /* renamed from: y, reason: collision with root package name */
    private BigInteger f11811y = BigInteger.ZERO;

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHKeyAgreement$AllowKDF.class */
    private static class AllowKDF {
        private static final boolean VALUE = getValue();

        private AllowKDF() {
        }

        private static boolean getValue() {
            return ((Boolean) AccessController.doPrivileged(() -> {
                return Boolean.valueOf(Boolean.getBoolean("jdk.crypto.KeyAgreement.legacyKDF"));
            })).booleanValue();
        }
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            engineInit(key, null, secureRandom);
        } catch (InvalidAlgorithmParameterException e2) {
        }
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.generateSecret = false;
        this.init_p = null;
        this.init_g = null;
        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Diffie-Hellman parameters expected");
        }
        if (!(key instanceof javax.crypto.interfaces.DHPrivateKey)) {
            throw new InvalidKeyException("Diffie-Hellman private key expected");
        }
        javax.crypto.interfaces.DHPrivateKey dHPrivateKey = (javax.crypto.interfaces.DHPrivateKey) key;
        if (algorithmParameterSpec != null) {
            this.init_p = ((DHParameterSpec) algorithmParameterSpec).getP();
            this.init_g = ((DHParameterSpec) algorithmParameterSpec).getG();
        }
        BigInteger p2 = dHPrivateKey.getParams().getP();
        BigInteger g2 = dHPrivateKey.getParams().getG();
        if (this.init_p != null && p2 != null && !this.init_p.equals(p2)) {
            throw new InvalidKeyException("Incompatible parameters");
        }
        if (this.init_g != null && g2 != null && !this.init_g.equals(g2)) {
            throw new InvalidKeyException("Incompatible parameters");
        }
        if ((this.init_p == null && p2 == null) || (this.init_g == null && g2 == null)) {
            throw new InvalidKeyException("Missing parameters");
        }
        this.init_p = p2;
        this.init_g = g2;
        this.f11810x = dHPrivateKey.getX();
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected Key engineDoPhase(Key key, boolean z2) throws IllegalStateException, InvalidKeyException {
        if (!(key instanceof javax.crypto.interfaces.DHPublicKey)) {
            throw new InvalidKeyException("Diffie-Hellman public key expected");
        }
        javax.crypto.interfaces.DHPublicKey dHPublicKey = (javax.crypto.interfaces.DHPublicKey) key;
        if (this.init_p == null || this.init_g == null) {
            throw new IllegalStateException("Not initialized");
        }
        BigInteger p2 = dHPublicKey.getParams().getP();
        BigInteger g2 = dHPublicKey.getParams().getG();
        if (p2 != null && !this.init_p.equals(p2)) {
            throw new InvalidKeyException("Incompatible parameters");
        }
        if (g2 != null && !this.init_g.equals(g2)) {
            throw new InvalidKeyException("Incompatible parameters");
        }
        KeyUtil.validate(dHPublicKey);
        this.f11811y = dHPublicKey.getY();
        this.generateSecret = true;
        if (!z2) {
            return new DHPublicKey(new BigInteger(1, engineGenerateSecret()), this.init_p, this.init_g);
        }
        return null;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        byte[] bArr = new byte[(this.init_p.bitLength() + 7) >>> 3];
        try {
            engineGenerateSecret(bArr, 0);
        } catch (ShortBufferException e2) {
        }
        return bArr;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected int engineGenerateSecret(byte[] bArr, int i2) throws IllegalStateException, ShortBufferException {
        if (!this.generateSecret) {
            throw new IllegalStateException("Key agreement has not been completed yet");
        }
        if (bArr == null) {
            throw new ShortBufferException("No buffer provided for shared secret");
        }
        BigInteger bigInteger = this.init_p;
        int iBitLength = (bigInteger.bitLength() + 7) >>> 3;
        if (bArr.length - i2 < iBitLength) {
            throw new ShortBufferException("Buffer too short for shared secret");
        }
        this.generateSecret = false;
        byte[] byteArray = this.f11811y.modPow(this.f11810x, bigInteger).toByteArray();
        if (byteArray.length == iBitLength) {
            System.arraycopy(byteArray, 0, bArr, i2, byteArray.length);
        } else if (byteArray.length < iBitLength) {
            System.arraycopy(byteArray, 0, bArr, i2 + (iBitLength - byteArray.length), byteArray.length);
        } else if (byteArray.length == iBitLength + 1 && byteArray[0] == 0) {
            System.arraycopy(byteArray, 1, bArr, i2, iBitLength);
        } else {
            throw new ProviderException("Generated secret is out-of-range");
        }
        return iBitLength;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected SecretKey engineGenerateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        if (str == null) {
            throw new NoSuchAlgorithmException("null algorithm");
        }
        if (!str.equalsIgnoreCase("TlsPremasterSecret") && !AllowKDF.VALUE) {
            throw new NoSuchAlgorithmException("Unsupported secret key algorithm: " + str);
        }
        byte[] bArrEngineGenerateSecret = engineGenerateSecret();
        if (str.equalsIgnoreCase("DES")) {
            return new DESKey(bArrEngineGenerateSecret);
        }
        if (str.equalsIgnoreCase("DESede") || str.equalsIgnoreCase("TripleDES")) {
            return new DESedeKey(bArrEngineGenerateSecret);
        }
        if (str.equalsIgnoreCase("Blowfish")) {
            int length = bArrEngineGenerateSecret.length;
            if (length >= 56) {
                length = 56;
            }
            return new SecretKeySpec(bArrEngineGenerateSecret, 0, length, "Blowfish");
        }
        if (str.equalsIgnoreCase("AES")) {
            int length2 = bArrEngineGenerateSecret.length;
            SecretKeySpec secretKeySpec = null;
            for (int length3 = AESConstants.AES_KEYSIZES.length - 1; secretKeySpec == null && length3 >= 0; length3--) {
                if (length2 >= AESConstants.AES_KEYSIZES[length3]) {
                    length2 = AESConstants.AES_KEYSIZES[length3];
                    secretKeySpec = new SecretKeySpec(bArrEngineGenerateSecret, 0, length2, "AES");
                }
            }
            if (secretKeySpec == null) {
                throw new InvalidKeyException("Key material is too short");
            }
            return secretKeySpec;
        }
        if (str.equals("TlsPremasterSecret")) {
            return new SecretKeySpec(KeyUtil.trimZeroes(bArrEngineGenerateSecret), "TlsPremasterSecret");
        }
        throw new NoSuchAlgorithmException("Unsupported secret key algorithm: " + str);
    }
}
