package java.security.cert;

/* loaded from: rt.jar:java/security/cert/CertSelector.class */
public interface CertSelector extends Cloneable {
    boolean match(Certificate certificate);

    Object clone();
}
