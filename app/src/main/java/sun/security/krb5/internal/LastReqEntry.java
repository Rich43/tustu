package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/LastReqEntry.class */
public class LastReqEntry {
    private int lrType;
    private KerberosTime lrValue;

    private LastReqEntry() {
    }

    public LastReqEntry(int i2, KerberosTime kerberosTime) {
        this.lrType = i2;
        this.lrValue = kerberosTime;
    }

    public LastReqEntry(DerValue derValue) throws Asn1Exception, IOException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.lrType = derValue2.getData().getBigInteger().intValue();
            this.lrValue = KerberosTime.parse(derValue.getData(), (byte) 1, false);
            if (derValue.getData().available() > 0) {
                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
            }
            return;
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.lrType);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.lrValue.asn1Encode());
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        return derOutputStream3.toByteArray();
    }

    public Object clone() {
        LastReqEntry lastReqEntry = new LastReqEntry();
        lastReqEntry.lrType = this.lrType;
        lastReqEntry.lrValue = this.lrValue;
        return lastReqEntry;
    }
}
