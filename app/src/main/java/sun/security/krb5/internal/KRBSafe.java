package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.Checksum;
import sun.security.krb5.RealmException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KRBSafe.class */
public class KRBSafe {
    public int pvno;
    public int msgType;
    public KRBSafeBody safeBody;
    public Checksum cksum;

    public KRBSafe(KRBSafeBody kRBSafeBody, Checksum checksum) {
        this.pvno = 5;
        this.msgType = 20;
        this.safeBody = kRBSafeBody;
        this.cksum = checksum;
    }

    public KRBSafe(byte[] bArr) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        init(new DerValue(bArr));
    }

    public KRBSafe(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        if ((derValue.getTag() & 31) != 20 || !derValue.isApplication() || !derValue.isConstructed()) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) == 0) {
            this.pvno = derValue3.getData().getBigInteger().intValue();
            if (this.pvno != 5) {
                throw new KrbApErrException(39);
            }
            DerValue derValue4 = derValue2.getData().getDerValue();
            if ((derValue4.getTag() & 31) == 1) {
                this.msgType = derValue4.getData().getBigInteger().intValue();
                if (this.msgType != 20) {
                    throw new KrbApErrException(40);
                }
                this.safeBody = KRBSafeBody.parse(derValue2.getData(), (byte) 2, false);
                this.cksum = Checksum.parse(derValue2.getData(), (byte) 3, false);
                if (derValue2.getData().available() > 0) {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
                return;
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putInteger(BigInteger.valueOf(this.pvno));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putInteger(BigInteger.valueOf(this.msgType));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.safeBody.asn1Encode());
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.cksum.asn1Encode());
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream2);
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write(DerValue.createTag((byte) 64, true, (byte) 20), derOutputStream4);
        return derOutputStream5.toByteArray();
    }
}
