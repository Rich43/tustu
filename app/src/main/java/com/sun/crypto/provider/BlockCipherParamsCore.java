package com.sun.crypto.provider;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import sun.misc.HexDumpEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/BlockCipherParamsCore.class */
final class BlockCipherParamsCore {
    private int block_size;
    private byte[] iv = null;

    BlockCipherParamsCore(int i2) {
        this.block_size = 0;
        this.block_size = i2;
    }

    void init(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        byte[] iv = ((IvParameterSpec) algorithmParameterSpec).getIV();
        if (iv.length != this.block_size) {
            throw new InvalidParameterSpecException("IV not " + this.block_size + " bytes long");
        }
        this.iv = (byte[]) iv.clone();
    }

    void init(byte[] bArr) throws IOException {
        DerInputStream derInputStream = new DerInputStream(bArr);
        byte[] octetString = derInputStream.getOctetString();
        if (derInputStream.available() != 0) {
            throw new IOException("IV parsing error: extra data");
        }
        if (octetString.length != this.block_size) {
            throw new IOException("IV not " + this.block_size + " bytes long");
        }
        this.iv = octetString;
    }

    void init(byte[] bArr, String str) throws IOException {
        if (str != null && !str.equalsIgnoreCase("ASN.1")) {
            throw new IllegalArgumentException("Only support ASN.1 format");
        }
        init(bArr);
    }

    <T extends AlgorithmParameterSpec> T getParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (IvParameterSpec.class.isAssignableFrom(cls)) {
            return cls.cast(new IvParameterSpec(this.iv));
        }
        throw new InvalidParameterSpecException("Inappropriate parameter specification");
    }

    byte[] getEncoded() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOctetString(this.iv);
        return derOutputStream.toByteArray();
    }

    byte[] getEncoded(String str) throws IOException {
        return getEncoded();
    }

    public String toString() {
        String property = System.getProperty("line.separator");
        return ((property + "    iv:" + property + "[") + new HexDumpEncoder().encodeBuffer(this.iv)) + "]" + property;
    }
}
