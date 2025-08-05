package java.security.cert;

/* loaded from: rt.jar:java/security/cert/CRLSelector.class */
public interface CRLSelector extends Cloneable {
    boolean match(CRL crl);

    Object clone();
}
