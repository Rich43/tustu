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
import java.util.Arrays;
import javax.crypto.MacSpi;
import javax.crypto.SecretKey;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacCore.class */
abstract class HmacCore extends MacSpi implements Cloneable {
    private MessageDigest md;
    private byte[] k_ipad;
    private byte[] k_opad;
    private boolean first;
    private final int blockLen;

    HmacCore(MessageDigest messageDigest, int i2) {
        this.md = messageDigest;
        this.blockLen = i2;
        this.k_ipad = new byte[this.blockLen];
        this.k_opad = new byte[this.blockLen];
        this.first = true;
    }

    HmacCore(String str, int i2) throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance(str), i2);
    }

    @Override // javax.crypto.MacSpi
    protected int engineGetMacLength() {
        return this.md.getDigestLength();
    }

    @Override // javax.crypto.MacSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("HMAC does not use parameters");
        }
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Secret key expected");
        }
        byte[] encoded = key.getEncoded();
        if (encoded == null) {
            throw new InvalidKeyException("Missing key data");
        }
        if (encoded.length > this.blockLen) {
            byte[] bArrDigest = this.md.digest(encoded);
            Arrays.fill(encoded, (byte) 0);
            encoded = bArrDigest;
        }
        int i2 = 0;
        while (i2 < this.blockLen) {
            byte b2 = i2 < encoded.length ? encoded[i2] : (byte) 0;
            this.k_ipad[i2] = (byte) (b2 ^ 54);
            this.k_opad[i2] = (byte) (b2 ^ 92);
            i2++;
        }
        Arrays.fill(encoded, (byte) 0);
        engineReset();
    }

    @Override // javax.crypto.MacSpi
    protected void engineUpdate(byte b2) {
        if (this.first) {
            this.md.update(this.k_ipad);
            this.first = false;
        }
        this.md.update(b2);
    }

    @Override // javax.crypto.MacSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) {
        if (this.first) {
            this.md.update(this.k_ipad);
            this.first = false;
        }
        this.md.update(bArr, i2, i3);
    }

    @Override // javax.crypto.MacSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        if (this.first) {
            this.md.update(this.k_ipad);
            this.first = false;
        }
        this.md.update(byteBuffer);
    }

    @Override // javax.crypto.MacSpi
    protected byte[] engineDoFinal() {
        if (this.first) {
            this.md.update(this.k_ipad);
        } else {
            this.first = true;
        }
        try {
            byte[] bArrDigest = this.md.digest();
            this.md.update(this.k_opad);
            this.md.update(bArrDigest);
            this.md.digest(bArrDigest, 0, bArrDigest.length);
            return bArrDigest;
        } catch (DigestException e2) {
            throw new ProviderException(e2);
        }
    }

    @Override // javax.crypto.MacSpi
    protected void engineReset() {
        if (!this.first) {
            this.md.reset();
            this.first = true;
        }
    }

    @Override // javax.crypto.MacSpi
    public Object clone() throws CloneNotSupportedException {
        HmacCore hmacCore = (HmacCore) super.clone();
        hmacCore.md = (MessageDigest) this.md.clone();
        hmacCore.k_ipad = (byte[]) this.k_ipad.clone();
        hmacCore.k_opad = (byte[]) this.k_opad.clone();
        return hmacCore;
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacCore$HmacSHA224.class */
    public static final class HmacSHA224 extends HmacCore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA224() throws NoSuchAlgorithmException {
            super("SHA-224", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacCore$HmacSHA256.class */
    public static final class HmacSHA256 extends HmacCore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA256() throws NoSuchAlgorithmException {
            super("SHA-256", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacCore$HmacSHA384.class */
    public static final class HmacSHA384 extends HmacCore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA384() throws NoSuchAlgorithmException {
            super("SHA-384", 128);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacCore$HmacSHA512.class */
    public static final class HmacSHA512 extends HmacCore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA512() throws NoSuchAlgorithmException {
            super("SHA-512", 128);
        }
    }
}
