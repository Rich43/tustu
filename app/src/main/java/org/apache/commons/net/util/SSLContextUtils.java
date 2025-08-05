package org.apache.commons.net.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/SSLContextUtils.class */
public class SSLContextUtils {
    private SSLContextUtils() {
    }

    public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager) throws IOException {
        return createSSLContext(protocol, keyManager == null ? null : new KeyManager[]{keyManager}, trustManager == null ? null : new TrustManager[]{trustManager});
    }

    public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) throws IOException {
        try {
            SSLContext ctx = SSLContext.getInstance(protocol);
            ctx.init(keyManagers, trustManagers, null);
            return ctx;
        } catch (GeneralSecurityException e2) {
            IOException ioe = new IOException("Could not initialize SSL context");
            ioe.initCause(e2);
            throw ioe;
        }
    }
}
