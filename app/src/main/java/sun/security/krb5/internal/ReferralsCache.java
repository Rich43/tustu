package sun.security.krb5.internal;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import sun.security.krb5.Credentials;
import sun.security.krb5.PrincipalName;

/* loaded from: rt.jar:sun/security/krb5/internal/ReferralsCache.class */
final class ReferralsCache {
    private static Map<ReferralCacheKey, Map<String, ReferralCacheEntry>> referralsMap = new HashMap();

    ReferralsCache() {
    }

    /* loaded from: rt.jar:sun/security/krb5/internal/ReferralsCache$ReferralCacheKey.class */
    private static final class ReferralCacheKey {
        private PrincipalName cname;
        private PrincipalName sname;
        private PrincipalName user;
        private byte[] userSvcTicketEnc;

        ReferralCacheKey(PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, Ticket ticket) {
            this.cname = principalName;
            this.sname = principalName2;
            this.user = principalName3;
            if (ticket != null && ticket.encPart != null) {
                byte[] bytes = ticket.encPart.getBytes();
                if (bytes.length > 0) {
                    this.userSvcTicketEnc = bytes;
                }
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ReferralCacheKey)) {
                return false;
            }
            ReferralCacheKey referralCacheKey = (ReferralCacheKey) obj;
            return this.cname.equals(referralCacheKey.cname) && this.sname.equals(referralCacheKey.sname) && Objects.equals(this.user, referralCacheKey.user) && Arrays.equals(this.userSvcTicketEnc, referralCacheKey.userSvcTicketEnc);
        }

        public int hashCode() {
            return this.cname.hashCode() + this.sname.hashCode() + Objects.hashCode(this.user) + Arrays.hashCode(this.userSvcTicketEnc);
        }
    }

    /* loaded from: rt.jar:sun/security/krb5/internal/ReferralsCache$ReferralCacheEntry.class */
    static final class ReferralCacheEntry {
        private final Credentials creds;
        private final String toRealm;

        ReferralCacheEntry(Credentials credentials, String str) {
            this.creds = credentials;
            this.toRealm = str;
        }

        Credentials getCreds() {
            return this.creds;
        }

        String getToRealm() {
            return this.toRealm;
        }
    }

    static synchronized void put(PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, Ticket[] ticketArr, String str, String str2, Credentials credentials) {
        ReferralCacheKey referralCacheKey = new ReferralCacheKey(principalName, principalName2, principalName3, ticketArr != null ? ticketArr[0] : null);
        pruneExpired(referralCacheKey);
        if (credentials.getEndTime().before(new Date())) {
            return;
        }
        Map<String, ReferralCacheEntry> map = referralsMap.get(referralCacheKey);
        if (map == null) {
            map = new HashMap();
            referralsMap.put(referralCacheKey, map);
        }
        map.remove(str);
        ReferralCacheEntry referralCacheEntry = new ReferralCacheEntry(credentials, str2);
        map.put(str, referralCacheEntry);
        LinkedList linkedList = new LinkedList();
        for (ReferralCacheEntry referralCacheEntry2 = referralCacheEntry; referralCacheEntry2 != null; referralCacheEntry2 = map.get(referralCacheEntry2.getToRealm())) {
            if (linkedList.contains(referralCacheEntry2)) {
                map.remove(referralCacheEntry.getToRealm());
                return;
            }
            linkedList.add(referralCacheEntry2);
        }
    }

    static synchronized ReferralCacheEntry get(PrincipalName principalName, PrincipalName principalName2, PrincipalName principalName3, Ticket[] ticketArr, String str) {
        ReferralCacheEntry referralCacheEntry;
        ReferralCacheKey referralCacheKey = new ReferralCacheKey(principalName, principalName2, principalName3, ticketArr != null ? ticketArr[0] : null);
        pruneExpired(referralCacheKey);
        Map<String, ReferralCacheEntry> map = referralsMap.get(referralCacheKey);
        if (map != null && (referralCacheEntry = map.get(str)) != null) {
            return referralCacheEntry;
        }
        return null;
    }

    private static void pruneExpired(ReferralCacheKey referralCacheKey) {
        Date date = new Date();
        Map<String, ReferralCacheEntry> map = referralsMap.get(referralCacheKey);
        if (map != null) {
            for (Map.Entry<String, ReferralCacheEntry> entry : map.entrySet()) {
                if (entry.getValue().getCreds().getEndTime().before(date)) {
                    map.remove(entry.getKey());
                }
            }
        }
    }
}
