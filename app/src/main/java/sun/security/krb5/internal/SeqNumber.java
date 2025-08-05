package sun.security.krb5.internal;

/* loaded from: rt.jar:sun/security/krb5/internal/SeqNumber.class */
public interface SeqNumber {
    void randInit();

    void init(int i2);

    int current();

    int next();

    int step();
}
