package sun.security.krb5.internal.rcache;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.KrbApErrException;

/* loaded from: rt.jar:sun/security/krb5/internal/rcache/AuthList.class */
public class AuthList {
    private final int lifespan;
    private volatile int oldestTime = Integer.MIN_VALUE;
    private final LinkedList<AuthTimeWithHash> entries = new LinkedList<>();

    public AuthList(int i2) {
        this.lifespan = i2;
    }

    public synchronized void put(AuthTimeWithHash authTimeWithHash, KerberosTime kerberosTime) throws KrbApErrException {
        int iCompareTo;
        if (this.entries.isEmpty()) {
            this.entries.addFirst(authTimeWithHash);
            this.oldestTime = authTimeWithHash.ctime;
            return;
        }
        int iCompareTo2 = this.entries.getFirst().compareTo(authTimeWithHash);
        if (iCompareTo2 < 0) {
            this.entries.addFirst(authTimeWithHash);
        } else {
            if (iCompareTo2 == 0) {
                throw new KrbApErrException(34);
            }
            ListIterator<AuthTimeWithHash> listIterator = this.entries.listIterator(1);
            boolean z2 = false;
            do {
                if (listIterator.hasNext()) {
                    AuthTimeWithHash next = listIterator.next();
                    iCompareTo = next.compareTo(authTimeWithHash);
                    if (iCompareTo < 0) {
                        this.entries.add(this.entries.indexOf(next), authTimeWithHash);
                        z2 = true;
                    }
                }
                if (!z2) {
                    this.entries.addLast(authTimeWithHash);
                }
            } while (iCompareTo != 0);
            throw new KrbApErrException(34);
        }
        long seconds = kerberosTime.getSeconds() - this.lifespan;
        if (this.oldestTime > seconds - 5) {
            return;
        }
        while (!this.entries.isEmpty()) {
            AuthTimeWithHash authTimeWithHashRemoveLast = this.entries.removeLast();
            if (authTimeWithHashRemoveLast.ctime >= seconds) {
                this.entries.addLast(authTimeWithHashRemoveLast);
                this.oldestTime = authTimeWithHashRemoveLast.ctime;
                return;
            }
        }
        this.oldestTime = Integer.MIN_VALUE;
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<AuthTimeWithHash> itDescendingIterator = this.entries.descendingIterator();
        int size = this.entries.size();
        while (itDescendingIterator.hasNext()) {
            int i2 = size;
            size--;
            sb.append('#').append(i2).append(": ").append(itDescendingIterator.next().toString()).append('\n');
        }
        return sb.toString();
    }
}
