package com.sun.net.ssl.internal.ssl;

import sun.security.ssl.SunJSSE;

/* loaded from: jsse.jar:com/sun/net/ssl/internal/ssl/Provider.class */
public final class Provider extends SunJSSE {
    private static final long serialVersionUID = 3231825739635378733L;

    public Provider() {
    }

    public Provider(java.security.Provider provider) {
        super(provider);
    }

    public Provider(String str) {
        super(str);
    }

    public static synchronized boolean isFIPS() {
        return SunJSSE.isFIPS();
    }

    public static synchronized void install() {
    }
}
