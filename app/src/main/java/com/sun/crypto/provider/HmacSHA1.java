package com.sun.crypto.provider;

import java.security.NoSuchAlgorithmException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacSHA1.class */
public final class HmacSHA1 extends HmacCore {
    @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
    public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public HmacSHA1() throws NoSuchAlgorithmException {
        super("SHA1", 64);
    }
}
