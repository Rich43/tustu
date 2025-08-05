package sun.security.tools.keytool;

import java.util.Objects;

/* compiled from: Main.java */
/* loaded from: rt.jar:sun/security/tools/keytool/Pair.class */
class Pair<A, B> {
    public final A fst;
    public final B snd;

    public Pair(A a2, B b2) {
        this.fst = a2;
        this.snd = b2;
    }

    public String toString() {
        return "Pair[" + ((Object) this.fst) + "," + ((Object) this.snd) + "]";
    }

    public boolean equals(Object obj) {
        return (obj instanceof Pair) && Objects.equals(this.fst, ((Pair) obj).fst) && Objects.equals(this.snd, ((Pair) obj).snd);
    }

    public int hashCode() {
        if (this.fst != null) {
            return this.snd == null ? this.fst.hashCode() + 2 : (this.fst.hashCode() * 17) + this.snd.hashCode();
        }
        if (this.snd == null) {
            return 0;
        }
        return this.snd.hashCode() + 1;
    }

    public static <A, B> Pair<A, B> of(A a2, B b2) {
        return new Pair<>(a2, b2);
    }
}
