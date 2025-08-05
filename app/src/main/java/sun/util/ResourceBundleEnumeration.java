package sun.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: rt.jar:sun/util/ResourceBundleEnumeration.class */
public class ResourceBundleEnumeration implements Enumeration<String> {
    Set<String> set;
    Iterator<String> iterator;
    Enumeration<String> enumeration;
    String next = null;

    public ResourceBundleEnumeration(Set<String> set, Enumeration<String> enumeration) {
        this.set = set;
        this.iterator = set.iterator();
        this.enumeration = enumeration;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        if (this.next == null) {
            if (this.iterator.hasNext()) {
                this.next = this.iterator.next();
            } else if (this.enumeration != null) {
                while (this.next == null && this.enumeration.hasMoreElements()) {
                    this.next = this.enumeration.nextElement();
                    if (this.set.contains(this.next)) {
                        this.next = null;
                    }
                }
            }
        }
        return this.next != null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Enumeration
    public String nextElement() {
        if (hasMoreElements()) {
            String str = this.next;
            this.next = null;
            return str;
        }
        throw new NoSuchElementException();
    }
}
