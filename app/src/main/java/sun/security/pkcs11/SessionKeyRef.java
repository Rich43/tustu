package sun.security.pkcs11;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.security.ProviderException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* compiled from: P11Key.java */
/* loaded from: sunpkcs11.jar:sun/security/pkcs11/SessionKeyRef.class */
final class SessionKeyRef extends PhantomReference<P11Key> {
    private static ReferenceQueue<P11Key> refQueue;
    private static Set<SessionKeyRef> refSet;
    private long keyID;
    private Session session;
    private boolean wrapperKeyUsed;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SessionKeyRef.class.desiredAssertionStatus();
        refQueue = new ReferenceQueue<>();
        refSet = Collections.synchronizedSet(new HashSet());
    }

    static ReferenceQueue<P11Key> referenceQueue() {
        return refQueue;
    }

    private static void drainRefQueueBounded() {
        while (true) {
            SessionKeyRef sessionKeyRef = (SessionKeyRef) refQueue.poll();
            if (sessionKeyRef != null) {
                sessionKeyRef.dispose();
            } else {
                return;
            }
        }
    }

    SessionKeyRef(P11Key p11Key, long j2, boolean z2, Session session) {
        super(p11Key, refQueue);
        if (session == null) {
            throw new ProviderException("key must be associated with a session");
        }
        registerNativeKey(j2, session);
        this.wrapperKeyUsed = z2;
        refSet.add(this);
        drainRefQueueBounded();
    }

    void registerNativeKey(long j2, Session session) {
        if (!$assertionsDisabled && j2 == 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && session == null) {
            throw new AssertionError();
        }
        updateNativeKey(j2, session);
    }

    void removeNativeKey() {
        if (!$assertionsDisabled && this.session == null) {
            throw new AssertionError();
        }
        updateNativeKey(0L, null);
    }

    private void updateNativeKey(long j2, Session session) {
        if (j2 == 0) {
            if (!$assertionsDisabled && session != null) {
                throw new AssertionError();
            }
            Token token = this.session.token;
            if (token.isValid()) {
                Session opSession = null;
                try {
                    opSession = token.getOpSession();
                    token.p11.C_DestroyObject(opSession.id(), this.keyID);
                    token.releaseSession(opSession);
                } catch (PKCS11Exception e2) {
                    token.releaseSession(opSession);
                } catch (Throwable th) {
                    token.releaseSession(opSession);
                    throw th;
                }
            }
            this.session.removeObject();
        } else {
            session.addObject();
        }
        this.keyID = j2;
        this.session = session;
    }

    void dispose() {
        if (this.wrapperKeyUsed) {
            NativeKeyHolder.decWrapperKeyRef();
        }
        if (this.keyID != 0) {
            removeNativeKey();
        }
        refSet.remove(this);
        clear();
    }
}
