package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.util.KerberosFlags;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/APOptions.class */
public class APOptions extends KerberosFlags {
    public APOptions() {
        super(32);
    }

    public APOptions(int i2) throws Asn1Exception {
        super(32);
        set(i2, true);
    }

    public APOptions(int i2, byte[] bArr) throws Asn1Exception {
        super(i2, bArr);
        if (i2 > bArr.length * 8 || i2 > 32) {
            throw new Asn1Exception(502);
        }
    }

    public APOptions(boolean[] zArr) throws Asn1Exception {
        super(zArr);
        if (zArr.length > 32) {
            throw new Asn1Exception(502);
        }
    }

    public APOptions(DerValue derValue) throws Asn1Exception, IOException {
        this(derValue.getUnalignedBitString(true).toBooleanArray());
    }

    public static APOptions parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new APOptions(derValue.getData().getDerValue());
    }
}
