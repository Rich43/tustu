package sun.security.ssl;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.action.GetIntegerAction;
import sun.security.krb5.internal.Krb5;
import sun.security.util.Cache;

/* loaded from: jsse.jar:sun/security/ssl/SSLSessionContextImpl.class */
final class SSLSessionContextImpl implements SSLSessionContext {
    private static final int DEFAULT_MAX_CACHE_SIZE = 20480;
    private int cacheLimit = getDefaultCacheLimit();
    private int timeout = Krb5.DEFAULT_MAXIMUM_TICKET_LIFETIME;
    private final Cache<SessionId, SSLSessionImpl> sessionCache = Cache.newSoftMemoryCache(this.cacheLimit, this.timeout);
    private final Cache<String, SSLSessionImpl> sessionHostPortCache = Cache.newSoftMemoryCache(this.cacheLimit, this.timeout);

    SSLSessionContextImpl() {
    }

    @Override // javax.net.ssl.SSLSessionContext
    public SSLSession getSession(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("session id cannot be null");
        }
        SSLSessionImpl sSLSessionImpl = this.sessionCache.get(new SessionId(bArr));
        if (!isTimedout(sSLSessionImpl)) {
            return sSLSessionImpl;
        }
        return null;
    }

    @Override // javax.net.ssl.SSLSessionContext
    public Enumeration<byte[]> getIds() {
        SessionCacheVisitor sessionCacheVisitor = new SessionCacheVisitor();
        this.sessionCache.accept(sessionCacheVisitor);
        return sessionCacheVisitor.getSessionIds();
    }

    @Override // javax.net.ssl.SSLSessionContext
    public void setSessionTimeout(int i2) throws IllegalArgumentException {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        if (this.timeout != i2) {
            this.sessionCache.setTimeout(i2);
            this.sessionHostPortCache.setTimeout(i2);
            this.timeout = i2;
        }
    }

    @Override // javax.net.ssl.SSLSessionContext
    public int getSessionTimeout() {
        return this.timeout;
    }

    @Override // javax.net.ssl.SSLSessionContext
    public void setSessionCacheSize(int i2) throws IllegalArgumentException {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        if (this.cacheLimit != i2) {
            this.sessionCache.setCapacity(i2);
            this.sessionHostPortCache.setCapacity(i2);
            this.cacheLimit = i2;
        }
    }

    @Override // javax.net.ssl.SSLSessionContext
    public int getSessionCacheSize() {
        return this.cacheLimit;
    }

    SSLSessionImpl get(byte[] bArr) {
        return (SSLSessionImpl) getSession(bArr);
    }

    SSLSessionImpl pull(byte[] bArr) {
        if (bArr != null) {
            return this.sessionCache.pull(new SessionId(bArr));
        }
        return null;
    }

    SSLSessionImpl get(String str, int i2) {
        if (str == null && i2 == -1) {
            return null;
        }
        SSLSessionImpl sSLSessionImpl = this.sessionHostPortCache.get(getKey(str, i2));
        if (!isTimedout(sSLSessionImpl)) {
            return sSLSessionImpl;
        }
        return null;
    }

    private static String getKey(String str, int i2) {
        return (str + CallSiteDescriptor.TOKEN_DELIMITER + String.valueOf(i2)).toLowerCase(Locale.ENGLISH);
    }

    void put(SSLSessionImpl sSLSessionImpl) {
        this.sessionCache.put(sSLSessionImpl.getSessionId(), sSLSessionImpl);
        if (sSLSessionImpl.getPeerHost() != null && sSLSessionImpl.getPeerPort() != -1) {
            this.sessionHostPortCache.put(getKey(sSLSessionImpl.getPeerHost(), sSLSessionImpl.getPeerPort()), sSLSessionImpl);
        }
        sSLSessionImpl.setContext(this);
    }

    void remove(SessionId sessionId) {
        SSLSessionImpl sSLSessionImpl = this.sessionCache.get(sessionId);
        if (sSLSessionImpl != null) {
            this.sessionCache.remove(sessionId);
            this.sessionHostPortCache.remove(getKey(sSLSessionImpl.getPeerHost(), sSLSessionImpl.getPeerPort()));
        }
    }

    private static int getDefaultCacheLimit() {
        try {
            int iIntValue = ((Integer) AccessController.doPrivileged(new GetIntegerAction("javax.net.ssl.sessionCacheSize", 20480))).intValue();
            if (iIntValue >= 0) {
                return iIntValue;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("invalid System Property javax.net.ssl.sessionCacheSize, use the default session cache size (20480) instead", new Object[0]);
            }
            return 20480;
        } catch (Exception e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("the System Property javax.net.ssl.sessionCacheSize is not available, use the default value (20480) instead", new Object[0]);
                return 20480;
            }
            return 20480;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTimedout(SSLSession sSLSession) {
        if (this.timeout != 0 && sSLSession != null && sSLSession.getCreationTime() + (this.timeout * 1000) <= System.currentTimeMillis()) {
            sSLSession.invalidate();
            return true;
        }
        return false;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLSessionContextImpl$SessionCacheVisitor.class */
    private final class SessionCacheVisitor implements Cache.CacheVisitor<SessionId, SSLSessionImpl> {
        ArrayList<byte[]> ids;

        private SessionCacheVisitor() {
            this.ids = null;
        }

        @Override // sun.security.util.Cache.CacheVisitor
        public void visit(Map<SessionId, SSLSessionImpl> map) {
            this.ids = new ArrayList<>(map.size());
            for (SessionId sessionId : map.keySet()) {
                if (!SSLSessionContextImpl.this.isTimedout(map.get(sessionId))) {
                    this.ids.add(sessionId.getId());
                }
            }
        }

        Enumeration<byte[]> getSessionIds() {
            return this.ids != null ? Collections.enumeration(this.ids) : Collections.emptyEnumeration();
        }
    }
}
