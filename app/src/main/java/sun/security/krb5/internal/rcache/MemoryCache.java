package sun.security.krb5.internal.rcache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.ReplayCache;

/* loaded from: rt.jar:sun/security/krb5/internal/rcache/MemoryCache.class */
public class MemoryCache extends ReplayCache {
    private static final int lifespan = KerberosTime.getDefaultSkew();
    private static final boolean DEBUG = Krb5.DEBUG;
    private final Map<String, AuthList> content = new ConcurrentHashMap();

    @Override // sun.security.krb5.internal.ReplayCache
    public synchronized void checkAndStore(KerberosTime kerberosTime, AuthTimeWithHash authTimeWithHash) throws KrbApErrException {
        String str = authTimeWithHash.client + CallSiteDescriptor.OPERATOR_DELIMITER + authTimeWithHash.server;
        this.content.computeIfAbsent(str, str2 -> {
            return new AuthList(lifespan);
        }).put(authTimeWithHash, kerberosTime);
        if (DEBUG) {
            System.out.println("MemoryCache: add " + ((Object) authTimeWithHash) + " to " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<AuthList> it = this.content.values().iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        return sb.toString();
    }
}
