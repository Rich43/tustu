package com.sun.crypto.provider;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.MacSpi;
import javax.crypto.SecretKey;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/SslMacCore.class */
final class SslMacCore {
    private final MessageDigest md;
    private final byte[] pad1;
    private final byte[] pad2;
    private boolean first = true;
    private byte[] secret;

    SslMacCore(String str, byte[] bArr, byte[] bArr2) throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance(str);
        this.pad1 = bArr;
        this.pad2 = bArr2;
    }

    int getDigestLength() {
        return this.md.getDigestLength();
    }

    void init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("SslMac does not use parameters");
        }
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Secret key expected");
        }
        this.secret = key.getEncoded();
        if (this.secret == null || this.secret.length == 0) {
            throw new InvalidKeyException("Missing key data");
        }
        reset();
    }

    void update(byte b2) {
        if (this.first) {
            this.md.update(this.secret);
            this.md.update(this.pad1);
            this.first = false;
        }
        this.md.update(b2);
    }

    void update(byte[] bArr, int i2, int i3) {
        if (this.first) {
            this.md.update(this.secret);
            this.md.update(this.pad1);
            this.first = false;
        }
        this.md.update(bArr, i2, i3);
    }

    void update(ByteBuffer byteBuffer) {
        if (this.first) {
            this.md.update(this.secret);
            this.md.update(this.pad1);
            this.first = false;
        }
        this.md.update(byteBuffer);
    }

    byte[] doFinal() {
        if (this.first) {
            this.md.update(this.secret);
            this.md.update(this.pad1);
        } else {
            this.first = true;
        }
        try {
            byte[] bArrDigest = this.md.digest();
            this.md.update(this.secret);
            this.md.update(this.pad2);
            this.md.update(bArrDigest);
            this.md.digest(bArrDigest, 0, bArrDigest.length);
            return bArrDigest;
        } catch (DigestException e2) {
            throw new ProviderException(e2);
        }
    }

    void reset() {
        if (!this.first) {
            this.md.reset();
            this.first = true;
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/SslMacCore$SslMacMD5.class */
    public static final class SslMacMD5 extends MacSpi {
        private final SslMacCore core = new SslMacCore("MD5", md5Pad1, md5Pad2);
        static final byte[] md5Pad1 = TlsPrfGenerator.genPad((byte) 54, 48);
        static final byte[] md5Pad2 = TlsPrfGenerator.genPad((byte) 92, 48);

        @Override // javax.crypto.MacSpi
        protected int engineGetMacLength() {
            return this.core.getDigestLength();
        }

        @Override // javax.crypto.MacSpi
        protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.init(key, algorithmParameterSpec);
        }

        @Override // javax.crypto.MacSpi
        protected void engineUpdate(byte b2) {
            this.core.update(b2);
        }

        @Override // javax.crypto.MacSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) {
            this.core.update(bArr, i2, i3);
        }

        @Override // javax.crypto.MacSpi
        protected void engineUpdate(ByteBuffer byteBuffer) {
            this.core.update(byteBuffer);
        }

        @Override // javax.crypto.MacSpi
        protected byte[] engineDoFinal() {
            return this.core.doFinal();
        }

        @Override // javax.crypto.MacSpi
        protected void engineReset() {
            this.core.reset();
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/SslMacCore$SslMacSHA1.class */
    public static final class SslMacSHA1 extends MacSpi {
        private final SslMacCore core = new SslMacCore("SHA", shaPad1, shaPad2);
        static final byte[] shaPad1 = TlsPrfGenerator.genPad((byte) 54, 40);
        static final byte[] shaPad2 = TlsPrfGenerator.genPad((byte) 92, 40);

        @Override // javax.crypto.MacSpi
        protected int engineGetMacLength() {
            return this.core.getDigestLength();
        }

        @Override // javax.crypto.MacSpi
        protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.init(key, algorithmParameterSpec);
        }

        @Override // javax.crypto.MacSpi
        protected void engineUpdate(byte b2) {
            this.core.update(b2);
        }

        @Override // javax.crypto.MacSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) {
            this.core.update(bArr, i2, i3);
        }

        @Override // javax.crypto.MacSpi
        protected void engineUpdate(ByteBuffer byteBuffer) {
            this.core.update(byteBuffer);
        }

        @Override // javax.crypto.MacSpi
        protected byte[] engineDoFinal() {
            return this.core.doFinal();
        }

        @Override // javax.crypto.MacSpi
        protected void engineReset() {
            this.core.reset();
        }
    }
}
