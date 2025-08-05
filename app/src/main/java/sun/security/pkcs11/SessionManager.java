package sun.security.pkcs11;

import java.security.ProviderException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.util.Debug;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/SessionManager.class */
final class SessionManager {
    private static final int DEFAULT_MAX_SESSIONS = 32;
    private static final Debug debug = Debug.getInstance("pkcs11");
    private final Token token;
    private final int maxSessions;
    private AtomicInteger activeSessions = new AtomicInteger();
    private final Pool objSessions;
    private final Pool opSessions;
    private int maxActiveSessions;
    private Object maxActiveSessionsLock;
    private final long openSessionFlags;

    SessionManager(Token token) {
        long j2;
        if (token.isWriteProtected()) {
            this.openSessionFlags = 4L;
            j2 = token.tokenInfo.ulMaxSessionCount;
        } else {
            this.openSessionFlags = 6L;
            j2 = token.tokenInfo.ulMaxRwSessionCount;
        }
        if (j2 == 0) {
            j2 = 2147483647L;
        } else if (j2 == -1 || j2 < 0) {
            j2 = 32;
        }
        this.maxSessions = (int) Math.min(j2, 2147483647L);
        this.token = token;
        this.objSessions = new Pool(this);
        this.opSessions = new Pool(this);
        if (debug != null) {
            this.maxActiveSessionsLock = new Object();
        }
    }

    boolean lowMaxSessions() {
        return this.maxSessions <= 32;
    }

    Session getObjSession() throws PKCS11Exception {
        Session sessionPoll = this.objSessions.poll();
        if (sessionPoll != null) {
            return ensureValid(sessionPoll);
        }
        Session sessionPoll2 = this.opSessions.poll();
        if (sessionPoll2 != null) {
            return ensureValid(sessionPoll2);
        }
        return ensureValid(openSession());
    }

    Session getOpSession() throws PKCS11Exception {
        Session sessionPoll = this.opSessions.poll();
        if (sessionPoll != null) {
            return ensureValid(sessionPoll);
        }
        if (this.maxSessions == Integer.MAX_VALUE || this.activeSessions.get() < this.maxSessions) {
            return ensureValid(openSession());
        }
        Session sessionPoll2 = this.objSessions.poll();
        if (sessionPoll2 != null) {
            return ensureValid(sessionPoll2);
        }
        throw new ProviderException("Could not obtain session");
    }

    private Session ensureValid(Session session) {
        session.id();
        return session;
    }

    Session killSession(Session session) {
        if (session == null || !this.token.isValid()) {
            return null;
        }
        if (debug != null) {
            System.out.println("Killing session (" + new Exception().getStackTrace()[2].toString() + ") active: " + this.activeSessions.get());
        }
        closeSession(session);
        return null;
    }

    Session releaseSession(Session session) {
        if (session == null || !this.token.isValid()) {
            return null;
        }
        if (session.hasObjects()) {
            this.objSessions.release(session);
            return null;
        }
        this.opSessions.release(session);
        return null;
    }

    void demoteObjSession(Session session) {
        if (!this.token.isValid()) {
            return;
        }
        if (debug != null) {
            System.out.println("Demoting session, active: " + this.activeSessions.get());
        }
        if (!this.objSessions.remove(session)) {
            return;
        }
        releaseSession(session);
    }

    private Session openSession() throws PKCS11Exception {
        if (this.maxSessions != Integer.MAX_VALUE && this.activeSessions.get() >= this.maxSessions) {
            throw new ProviderException("No more sessions available");
        }
        Session session = new Session(this.token, this.token.p11.C_OpenSession(this.token.provider.slotID, this.openSessionFlags, null, null));
        this.activeSessions.incrementAndGet();
        if (debug != null) {
            synchronized (this.maxActiveSessionsLock) {
                if (this.activeSessions.get() > this.maxActiveSessions) {
                    this.maxActiveSessions = this.activeSessions.get();
                    if (this.maxActiveSessions % 10 == 0) {
                        System.out.println("Open sessions: " + this.maxActiveSessions);
                    }
                }
            }
        }
        return session;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSession(Session session) {
        session.close();
        this.activeSessions.decrementAndGet();
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/SessionManager$Pool.class */
    public static final class Pool {
        private final SessionManager mgr;
        private final ConcurrentLinkedDeque<Session> pool = new ConcurrentLinkedDeque<>();

        Pool(SessionManager sessionManager) {
            this.mgr = sessionManager;
        }

        boolean remove(Session session) {
            return this.pool.remove(session);
        }

        Session poll() {
            return this.pool.pollLast();
        }

        void release(Session session) {
            int size;
            this.pool.offer(session);
            if (session.hasObjects() || (size = this.pool.size()) < 5) {
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            int i2 = 0;
            do {
                Session sessionPeek = this.pool.peek();
                if (sessionPeek == null || sessionPeek.isLive(jCurrentTimeMillis) || !this.pool.remove(sessionPeek)) {
                    break;
                }
                i2++;
                this.mgr.closeSession(sessionPeek);
            } while (size - i2 > 1);
            if (SessionManager.debug != null) {
                System.out.println("Closing " + i2 + " idle sessions, active: " + ((Object) this.mgr.activeSessions));
            }
        }
    }
}
