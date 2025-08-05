package sun.security.pkcs11;

import java.security.ProviderException;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/Session.class */
final class Session implements Comparable<Session> {
    private static final long MAX_IDLE_TIME = 180000;
    final Token token;
    private final long id;
    private final AtomicInteger createdObjects = new AtomicInteger();
    private long lastAccess;
    private final SessionRef sessionRef;

    Session(Token token, long j2) {
        this.token = token;
        this.id = j2;
        id();
        this.sessionRef = new SessionRef(this, j2, token);
    }

    @Override // java.lang.Comparable
    public int compareTo(Session session) {
        if (this.lastAccess == session.lastAccess) {
            return 0;
        }
        return this.lastAccess < session.lastAccess ? -1 : 1;
    }

    boolean isLive(long j2) {
        return j2 - this.lastAccess < MAX_IDLE_TIME;
    }

    long idInternal() {
        return this.id;
    }

    long id() {
        if (!this.token.isPresent(this.id)) {
            throw new ProviderException("Token has been removed");
        }
        this.lastAccess = System.currentTimeMillis();
        return this.id;
    }

    void addObject() {
        this.createdObjects.incrementAndGet();
    }

    void removeObject() {
        int iDecrementAndGet = this.createdObjects.decrementAndGet();
        if (iDecrementAndGet == 0) {
            this.token.sessionManager.demoteObjSession(this);
        } else if (iDecrementAndGet < 0) {
            throw new ProviderException("Internal error: objects created " + iDecrementAndGet);
        }
    }

    boolean hasObjects() {
        return this.createdObjects.get() != 0;
    }

    void close() {
        if (hasObjects()) {
            throw new ProviderException("Internal error: close session with active objects");
        }
        this.sessionRef.dispose();
    }
}
