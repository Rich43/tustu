package sun.security.x509;

import java.io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/OIDName.class */
public class OIDName implements GeneralNameInterface {
    private ObjectIdentifier oid;

    public OIDName(DerValue derValue) throws IOException {
        this.oid = derValue.getOID();
    }

    public OIDName(ObjectIdentifier objectIdentifier) {
        this.oid = objectIdentifier;
    }

    public OIDName(String str) throws IOException {
        try {
            this.oid = new ObjectIdentifier(str);
        } catch (Exception e2) {
            throw new IOException("Unable to create OIDName: " + ((Object) e2));
        }
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 8;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOID(this.oid);
    }

    public String toString() {
        return "OIDName: " + this.oid.toString();
    }

    public ObjectIdentifier getOID() {
        return this.oid;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OIDName)) {
            return false;
        }
        return this.oid.equals((Object) ((OIDName) obj).oid);
    }

    public int hashCode() {
        return this.oid.hashCode();
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        int i2;
        if (generalNameInterface == null || generalNameInterface.getType() != 8) {
            i2 = -1;
        } else if (equals((OIDName) generalNameInterface)) {
            i2 = 0;
        } else {
            throw new UnsupportedOperationException("Narrowing and widening are not supported for OIDNames");
        }
        return i2;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("subtreeDepth() not supported for OIDName.");
    }
}
