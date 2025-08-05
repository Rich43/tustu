package sun.security.krb5.internal.rcache;

import java.util.Objects;

/* loaded from: rt.jar:sun/security/krb5/internal/rcache/AuthTimeWithHash.class */
public class AuthTimeWithHash extends AuthTime implements Comparable<AuthTimeWithHash> {
    final String hash;

    public AuthTimeWithHash(String str, String str2, int i2, int i3, String str3) {
        super(str, str2, i2, i3);
        this.hash = str3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthTimeWithHash)) {
            return false;
        }
        AuthTimeWithHash authTimeWithHash = (AuthTimeWithHash) obj;
        return Objects.equals(this.hash, authTimeWithHash.hash) && Objects.equals(this.client, authTimeWithHash.client) && Objects.equals(this.server, authTimeWithHash.server) && this.ctime == authTimeWithHash.ctime && this.cusec == authTimeWithHash.cusec;
    }

    public int hashCode() {
        return Objects.hash(this.hash);
    }

    @Override // sun.security.krb5.internal.rcache.AuthTime
    public String toString() {
        return String.format("%d/%06d/%s/%s", Integer.valueOf(this.ctime), Integer.valueOf(this.cusec), this.hash, this.client);
    }

    @Override // java.lang.Comparable
    public int compareTo(AuthTimeWithHash authTimeWithHash) {
        int iCompareTo;
        if (this.ctime != authTimeWithHash.ctime) {
            iCompareTo = Integer.compare(this.ctime, authTimeWithHash.ctime);
        } else if (this.cusec != authTimeWithHash.cusec) {
            iCompareTo = Integer.compare(this.cusec, authTimeWithHash.cusec);
        } else {
            iCompareTo = this.hash.compareTo(authTimeWithHash.hash);
        }
        return iCompareTo;
    }

    public boolean isSameIgnoresHash(AuthTime authTime) {
        return this.client.equals(authTime.client) && this.server.equals(authTime.server) && this.ctime == authTime.ctime && this.cusec == authTime.cusec;
    }

    @Override // sun.security.krb5.internal.rcache.AuthTime
    public byte[] encode(boolean z2) {
        String str;
        String str2;
        if (z2) {
            str = "";
            str2 = String.format("HASH:%s %d:%s %d:%s", this.hash, Integer.valueOf(this.client.length()), this.client, Integer.valueOf(this.server.length()), this.server);
        } else {
            str = this.client;
            str2 = this.server;
        }
        return encode0(str, str2);
    }
}
