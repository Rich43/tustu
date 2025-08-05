package com.sun.net.ssl;

import java.security.BasicPermission;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/SSLPermission.class */
public final class SSLPermission extends BasicPermission {
    private static final long serialVersionUID = -2583684302506167542L;

    public SSLPermission(String str) {
        super(str);
    }

    public SSLPermission(String str, String str2) {
        super(str, str2);
    }
}
