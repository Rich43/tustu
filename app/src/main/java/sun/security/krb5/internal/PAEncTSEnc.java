package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/PAEncTSEnc.class */
public class PAEncTSEnc {
    public KerberosTime pATimeStamp;
    public Integer pAUSec;

    public PAEncTSEnc(KerberosTime kerberosTime, Integer num) {
        this.pATimeStamp = kerberosTime;
        this.pAUSec = num;
    }

    public PAEncTSEnc() {
        KerberosTime kerberosTimeNow = KerberosTime.now();
        this.pATimeStamp = kerberosTimeNow;
        this.pAUSec = new Integer(kerberosTimeNow.getMicroSeconds());
    }

    public PAEncTSEnc(DerValue derValue) throws Asn1Exception, IOException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.pATimeStamp = KerberosTime.parse(derValue.getData(), (byte) 0, false);
        if (derValue.getData().available() > 0) {
            DerValue derValue2 = derValue.getData().getDerValue();
            if ((derValue2.getTag() & 31) == 1) {
                this.pAUSec = new Integer(derValue2.getData().getBigInteger().intValue());
            } else {
                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
            }
        }
        if (derValue.getData().available() > 0) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        new DerOutputStream();
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.pATimeStamp.asn1Encode());
        if (this.pAUSec != null) {
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.putInteger(BigInteger.valueOf(this.pAUSec.intValue()));
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream2);
        }
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        return derOutputStream3.toByteArray();
    }
}
