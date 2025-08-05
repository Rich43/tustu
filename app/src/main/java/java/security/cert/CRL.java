package java.security.cert;

/* loaded from: rt.jar:java/security/cert/CRL.class */
public abstract class CRL {
    private String type;

    public abstract String toString();

    public abstract boolean isRevoked(Certificate certificate);

    protected CRL(String str) {
        this.type = str;
    }

    public final String getType() {
        return this.type;
    }
}
