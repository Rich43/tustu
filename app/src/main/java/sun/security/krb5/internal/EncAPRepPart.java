package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Vector;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptionKey;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/EncAPRepPart.class */
public class EncAPRepPart {
    public KerberosTime ctime;
    public int cusec;
    EncryptionKey subKey;
    Integer seqNumber;

    public EncAPRepPart(KerberosTime kerberosTime, int i2, EncryptionKey encryptionKey, Integer num) {
        this.ctime = kerberosTime;
        this.cusec = i2;
        this.subKey = encryptionKey;
        this.seqNumber = num;
    }

    public EncAPRepPart(byte[] bArr) throws Asn1Exception, IOException {
        init(new DerValue(bArr));
    }

    public EncAPRepPart(DerValue derValue) throws Asn1Exception, IOException {
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, IOException {
        if ((derValue.getTag() & 31) != 27 || !derValue.isApplication() || !derValue.isConstructed()) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.ctime = KerberosTime.parse(derValue2.getData(), (byte) 0, true);
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) == 1) {
            this.cusec = derValue3.getData().getBigInteger().intValue();
            if (derValue2.getData().available() > 0) {
                this.subKey = EncryptionKey.parse(derValue2.getData(), (byte) 2, true);
            } else {
                this.subKey = null;
                this.seqNumber = null;
            }
            if (derValue2.getData().available() > 0) {
                DerValue derValue4 = derValue2.getData().getDerValue();
                if ((derValue4.getTag() & 31) != 3) {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
                this.seqNumber = new Integer(derValue4.getData().getBigInteger().intValue());
            } else {
                this.seqNumber = null;
            }
            if (derValue2.getData().available() > 0) {
                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
            }
            return;
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        Vector vector = new Vector();
        DerOutputStream derOutputStream = new DerOutputStream();
        vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.ctime.asn1Encode()));
        derOutputStream.putInteger(BigInteger.valueOf(this.cusec));
        vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream.toByteArray()));
        if (this.subKey != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.subKey.asn1Encode()));
        }
        if (this.seqNumber != null) {
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.putInteger(BigInteger.valueOf(this.seqNumber.longValue()));
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream2.toByteArray()));
        }
        DerValue[] derValueArr = new DerValue[vector.size()];
        vector.copyInto(derValueArr);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putSequence(derValueArr);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write(DerValue.createTag((byte) 64, true, (byte) 27), derOutputStream3);
        return derOutputStream4.toByteArray();
    }

    public final EncryptionKey getSubKey() {
        return this.subKey;
    }

    public final Integer getSeqNumber() {
        return this.seqNumber;
    }
}
