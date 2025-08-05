package com.sun.javafx.webkit;

import com.sun.webkit.security.WCMessageDigest;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/WCMessageDigestImpl.class */
public class WCMessageDigestImpl extends WCMessageDigest {
    private final MessageDigest digest;

    public WCMessageDigestImpl(String algorithm) throws NoSuchAlgorithmException {
        this.digest = MessageDigest.getInstance(algorithm);
    }

    @Override // com.sun.webkit.security.WCMessageDigest
    public void addBytes(ByteBuffer input) {
        this.digest.update(input);
    }

    @Override // com.sun.webkit.security.WCMessageDigest
    public byte[] computeHash() {
        return this.digest.digest();
    }
}
