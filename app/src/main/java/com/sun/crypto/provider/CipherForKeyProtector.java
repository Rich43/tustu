package com.sun.crypto.provider;

import java.security.Provider;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;

/* compiled from: KeyProtector.java */
/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/CipherForKeyProtector.class */
final class CipherForKeyProtector extends Cipher {
    protected CipherForKeyProtector(CipherSpi cipherSpi, Provider provider, String str) {
        super(cipherSpi, provider, str);
    }
}
