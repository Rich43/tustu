package com.sun.crypto.provider;

import java.security.NoSuchAlgorithmException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacMD5.class */
public final class HmacMD5 extends HmacCore {
    @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
    public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public HmacMD5() throws NoSuchAlgorithmException {
        super("MD5", 64);
    }
}
