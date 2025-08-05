package sun.security.acl;

import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;

/* compiled from: AclImpl.java */
/* loaded from: rt.jar:sun/security/acl/AclEnumerator.class */
final class AclEnumerator implements Enumeration<AclEntry> {
    Acl acl;
    Enumeration<AclEntry> u1;
    Enumeration<AclEntry> u2;
    Enumeration<AclEntry> g1;
    Enumeration<AclEntry> g2;

    AclEnumerator(Acl acl, Hashtable<?, AclEntry> hashtable, Hashtable<?, AclEntry> hashtable2, Hashtable<?, AclEntry> hashtable3, Hashtable<?, AclEntry> hashtable4) {
        this.acl = acl;
        this.u1 = hashtable.elements();
        this.u2 = hashtable3.elements();
        this.g1 = hashtable2.elements();
        this.g2 = hashtable4.elements();
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.u1.hasMoreElements() || this.u2.hasMoreElements() || this.g1.hasMoreElements() || this.g2.hasMoreElements();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Enumeration
    /* renamed from: nextElement */
    public AclEntry nextElement2() {
        synchronized (this.acl) {
            if (this.u1.hasMoreElements()) {
                return this.u1.nextElement2();
            }
            if (this.u2.hasMoreElements()) {
                return this.u2.nextElement2();
            }
            if (this.g1.hasMoreElements()) {
                return this.g1.nextElement2();
            }
            if (this.g2.hasMoreElements()) {
                return this.g2.nextElement2();
            }
            throw new NoSuchElementException("Acl Enumerator");
        }
    }
}
