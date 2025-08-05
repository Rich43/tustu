package sun.security.x509;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/CertificatePolicySet.class */
public class CertificatePolicySet {
    private final Vector<CertificatePolicyId> ids;

    public CertificatePolicySet(Vector<CertificatePolicyId> vector) {
        this.ids = vector;
    }

    public CertificatePolicySet(DerInputStream derInputStream) throws IOException {
        this.ids = new Vector<>();
        for (DerValue derValue : derInputStream.getSequence(5)) {
            this.ids.addElement(new CertificatePolicyId(derValue));
        }
    }

    public String toString() {
        return "CertificatePolicySet:[\n" + this.ids.toString() + "]\n";
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (int i2 = 0; i2 < this.ids.size(); i2++) {
            this.ids.elementAt(i2).encode(derOutputStream2);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public List<CertificatePolicyId> getCertPolicyIds() {
        return Collections.unmodifiableList(this.ids);
    }
}
