package sun.security.x509;

import java.io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/CertificatePolicyId.class */
public class CertificatePolicyId {
    private ObjectIdentifier id;

    public CertificatePolicyId(ObjectIdentifier objectIdentifier) {
        this.id = objectIdentifier;
    }

    public CertificatePolicyId(DerValue derValue) throws IOException {
        this.id = derValue.getOID();
    }

    public ObjectIdentifier getIdentifier() {
        return this.id;
    }

    public String toString() {
        return "CertificatePolicyId: [" + this.id.toString() + "]\n";
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOID(this.id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CertificatePolicyId) {
            return this.id.equals((Object) ((CertificatePolicyId) obj).getIdentifier());
        }
        return false;
    }

    public int hashCode() {
        return this.id.hashCode();
    }
}
