package javax.naming;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

/* compiled from: NameImpl.java */
/* loaded from: rt.jar:javax/naming/NameImplEnumerator.class */
final class NameImplEnumerator implements Enumeration<String> {
    Vector<String> vector;
    int count;
    int limit;

    NameImplEnumerator(Vector<String> vector, int i2, int i3) {
        this.vector = vector;
        this.count = i2;
        this.limit = i3;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.count < this.limit;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Enumeration
    public String nextElement() {
        if (this.count < this.limit) {
            Vector<String> vector = this.vector;
            int i2 = this.count;
            this.count = i2 + 1;
            return vector.elementAt(i2);
        }
        throw new NoSuchElementException("NameImplEnumerator");
    }
}
