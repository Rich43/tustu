package java.security.cert;

import java.math.BigInteger;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import sun.security.x509.X509CRLEntryImpl;

/* loaded from: rt.jar:java/security/cert/X509CRLEntry.class */
public abstract class X509CRLEntry implements X509Extension {
    public abstract byte[] getEncoded() throws CRLException;

    public abstract BigInteger getSerialNumber();

    public abstract Date getRevocationDate();

    public abstract boolean hasExtensions();

    public abstract String toString();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X509CRLEntry)) {
            return false;
        }
        try {
            byte[] encoded = getEncoded();
            byte[] encoded2 = ((X509CRLEntry) obj).getEncoded();
            if (encoded.length != encoded2.length) {
                return false;
            }
            for (int i2 = 0; i2 < encoded.length; i2++) {
                if (encoded[i2] != encoded2[i2]) {
                    return false;
                }
            }
            return true;
        } catch (CRLException e2) {
            return false;
        }
    }

    public int hashCode() {
        int i2 = 0;
        try {
            byte[] encoded = getEncoded();
            for (int i3 = 1; i3 < encoded.length; i3++) {
                i2 += encoded[i3] * i3;
            }
            return i2;
        } catch (CRLException e2) {
            return i2;
        }
    }

    public X500Principal getCertificateIssuer() {
        return null;
    }

    public CRLReason getRevocationReason() {
        if (!hasExtensions()) {
            return null;
        }
        return X509CRLEntryImpl.getRevocationReason(this);
    }
}
