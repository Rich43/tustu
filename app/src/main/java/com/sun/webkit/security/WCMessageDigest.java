package com.sun.webkit.security;

import com.sun.javafx.webkit.WCMessageDigestImpl;
import com.sun.webkit.perf.WCMessageDigestPerfLogger;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/webkit/security/WCMessageDigest.class */
public abstract class WCMessageDigest {
    public abstract void addBytes(ByteBuffer byteBuffer);

    public abstract byte[] computeHash();

    protected static WCMessageDigest getInstance(String algorithm) {
        try {
            WCMessageDigest digest = new WCMessageDigestImpl(algorithm);
            return WCMessageDigestPerfLogger.isEnabled() ? new WCMessageDigestPerfLogger(digest) : digest;
        } catch (Exception e2) {
            return null;
        }
    }
}
