package sun.security.ssl;

/* compiled from: SSLSessionImpl.java */
/* loaded from: jsse.jar:sun/security/ssl/SecureKey.class */
class SecureKey {
    private static final Object nullObject = new Object();
    private final Object appKey;
    private final Object securityCtx = getCurrentSecurityContext();

    static Object getCurrentSecurityContext() {
        SecurityManager securityManager = System.getSecurityManager();
        Object securityContext = null;
        if (securityManager != null) {
            securityContext = securityManager.getSecurityContext();
        }
        if (securityContext == null) {
            securityContext = nullObject;
        }
        return securityContext;
    }

    SecureKey(Object obj) {
        this.appKey = obj;
    }

    Object getAppKey() {
        return this.appKey;
    }

    Object getSecurityContext() {
        return this.securityCtx;
    }

    public int hashCode() {
        return this.appKey.hashCode() ^ this.securityCtx.hashCode();
    }

    public boolean equals(Object obj) {
        return (obj instanceof SecureKey) && ((SecureKey) obj).appKey.equals(this.appKey) && ((SecureKey) obj).securityCtx.equals(this.securityCtx);
    }
}
