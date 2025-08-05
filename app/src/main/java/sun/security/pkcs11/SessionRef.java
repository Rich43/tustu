package sun.security.pkcs11;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.security.ProviderException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* compiled from: Session.java */
/* loaded from: sunpkcs11.jar:sun/security/pkcs11/SessionRef.class */
final class SessionRef extends PhantomReference<Session> implements Comparable<SessionRef> {
    private static ReferenceQueue<Session> refQueue = new ReferenceQueue<>();
    private static Set<SessionRef> refList = Collections.synchronizedSortedSet(new TreeSet());
    private long id;
    private Token token;

    static ReferenceQueue<Session> referenceQueue() {
        return refQueue;
    }

    static int totalCount() {
        return refList.size();
    }

    private static void drainRefQueueBounded() {
        while (true) {
            SessionRef sessionRef = (SessionRef) refQueue.poll();
            if (sessionRef != null) {
                sessionRef.dispose();
            } else {
                return;
            }
        }
    }

    SessionRef(Session session, long j2, Token token) {
        super(session, refQueue);
        this.id = j2;
        this.token = token;
        refList.add(this);
        drainRefQueueBounded();
    }

    void dispose() {
        refList.remove(this);
        try {
            if (this.token.isPresent(this.id)) {
                this.token.p11.C_CloseSession(this.id);
            }
        } catch (ProviderException e2) {
        } catch (PKCS11Exception e3) {
        } finally {
            clear();
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(SessionRef sessionRef) {
        if (this.id == sessionRef.id) {
            return 0;
        }
        return this.id < sessionRef.id ? -1 : 1;
    }
}
