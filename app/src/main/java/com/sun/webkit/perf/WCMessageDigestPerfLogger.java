package com.sun.webkit.perf;

import com.sun.webkit.security.WCMessageDigest;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/perf/WCMessageDigestPerfLogger.class */
public class WCMessageDigestPerfLogger extends WCMessageDigest {
    private static final Logger log = Logger.getLogger(WCMessageDigestPerfLogger.class.getName());
    private static final PerfLogger logger = PerfLogger.getLogger(log);
    private final WCMessageDigest digest;

    public WCMessageDigestPerfLogger(WCMessageDigest digest) {
        this.digest = digest;
    }

    public static synchronized boolean isEnabled() {
        return logger.isEnabled();
    }

    @Override // com.sun.webkit.security.WCMessageDigest
    public void addBytes(ByteBuffer input) {
        logger.resumeCount("ADDBYTES");
        this.digest.addBytes(input);
        logger.suspendCount("ADDBYTES");
    }

    @Override // com.sun.webkit.security.WCMessageDigest
    public byte[] computeHash() {
        logger.resumeCount("COMPUTEHASH");
        byte[] result = this.digest.computeHash();
        logger.suspendCount("COMPUTEHASH");
        return result;
    }
}
