package sun.security.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

/* loaded from: rt.jar:sun/security/util/NamedCurve.class */
public final class NamedCurve extends ECParameterSpec {
    private final String name;
    private final String oid;
    private final byte[] encoded;

    NamedCurve(String str, String str2, EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger, int i2) {
        super(ellipticCurve, eCPoint, bigInteger, i2);
        this.name = str;
        this.oid = str2;
        DerOutputStream derOutputStream = new DerOutputStream();
        try {
            derOutputStream.putOID(new ObjectIdentifier(str2));
            this.encoded = derOutputStream.toByteArray();
        } catch (IOException e2) {
            throw new RuntimeException("Internal error", e2);
        }
    }

    public String getName() {
        return this.name;
    }

    public byte[] getEncoded() {
        return (byte[]) this.encoded.clone();
    }

    public String getObjectId() {
        return this.oid;
    }

    public String toString() {
        return this.name + " (" + this.oid + ")";
    }
}
