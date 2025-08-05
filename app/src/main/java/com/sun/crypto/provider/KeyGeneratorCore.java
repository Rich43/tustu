package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore.class */
final class KeyGeneratorCore {
    private final String name;
    private final int defaultKeySize;
    private int keySize;
    private SecureRandom random;

    KeyGeneratorCore(String str, int i2) {
        this.name = str;
        this.defaultKeySize = i2;
        implInit(null);
    }

    void implInit(SecureRandom secureRandom) {
        this.keySize = this.defaultKeySize;
        this.random = secureRandom;
    }

    void implInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException(this.name + " key generation does not take any parameters");
    }

    void implInit(int i2, SecureRandom secureRandom) {
        if (i2 < 40) {
            throw new InvalidParameterException("Key length must be at least 40 bits");
        }
        this.keySize = i2;
        this.random = secureRandom;
    }

    SecretKey implGenerateKey() {
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        byte[] bArr = new byte[(this.keySize + 7) >> 3];
        this.random.nextBytes(bArr);
        return new SecretKeySpec(bArr, this.name);
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore$HmacSHA2KG.class */
    static abstract class HmacSHA2KG extends KeyGeneratorSpi {
        private final KeyGeneratorCore core;

        protected HmacSHA2KG(String str, int i2) {
            this.core = new KeyGeneratorCore(str, i2);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(SecureRandom secureRandom) {
            this.core.implInit(secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            this.core.implInit(algorithmParameterSpec, secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(int i2, SecureRandom secureRandom) {
            this.core.implInit(i2, secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected SecretKey engineGenerateKey() {
            return this.core.implGenerateKey();
        }

        /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore$HmacSHA2KG$SHA224.class */
        public static final class SHA224 extends HmacSHA2KG {
            public SHA224() {
                super("HmacSHA224", 224);
            }
        }

        /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore$HmacSHA2KG$SHA256.class */
        public static final class SHA256 extends HmacSHA2KG {
            public SHA256() {
                super("HmacSHA256", 256);
            }
        }

        /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore$HmacSHA2KG$SHA384.class */
        public static final class SHA384 extends HmacSHA2KG {
            public SHA384() {
                super("HmacSHA384", 384);
            }
        }

        /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore$HmacSHA2KG$SHA512.class */
        public static final class SHA512 extends HmacSHA2KG {
            public SHA512() {
                super("HmacSHA512", 512);
            }
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore$RC2KeyGenerator.class */
    public static final class RC2KeyGenerator extends KeyGeneratorSpi {
        private final KeyGeneratorCore core = new KeyGeneratorCore("RC2", 128);

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(SecureRandom secureRandom) {
            this.core.implInit(secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            this.core.implInit(algorithmParameterSpec, secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(int i2, SecureRandom secureRandom) {
            if (i2 < 40 || i2 > 1024) {
                throw new InvalidParameterException("Key length for RC2 must be between 40 and 1024 bits");
            }
            this.core.implInit(i2, secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected SecretKey engineGenerateKey() {
            return this.core.implGenerateKey();
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/KeyGeneratorCore$ARCFOURKeyGenerator.class */
    public static final class ARCFOURKeyGenerator extends KeyGeneratorSpi {
        private final KeyGeneratorCore core = new KeyGeneratorCore("ARCFOUR", 128);

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(SecureRandom secureRandom) {
            this.core.implInit(secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            this.core.implInit(algorithmParameterSpec, secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected void engineInit(int i2, SecureRandom secureRandom) {
            if (i2 < 40 || i2 > 1024) {
                throw new InvalidParameterException("Key length for ARCFOUR must be between 40 and 1024 bits");
            }
            this.core.implInit(i2, secureRandom);
        }

        @Override // javax.crypto.KeyGeneratorSpi
        protected SecretKey engineGenerateKey() {
            return this.core.implGenerateKey();
        }
    }
}
