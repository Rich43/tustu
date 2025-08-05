package sun.security.x509;

import java.io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/X400Address.class */
public class X400Address implements GeneralNameInterface {
    byte[] nameValue;

    public X400Address(byte[] bArr) {
        this.nameValue = null;
        this.nameValue = bArr;
    }

    public X400Address(DerValue derValue) throws IOException {
        this.nameValue = null;
        this.nameValue = derValue.toByteArray();
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 3;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putDerValue(new DerValue(this.nameValue));
    }

    public String toString() {
        return "X400Address: <DER-encoded value>";
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 3) {
            int i2 = -1;
            return i2;
        }
        throw new UnsupportedOperationException("Narrowing, widening, and match are not supported for X400Address.");
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("subtreeDepth not supported for X400Address");
    }
}
