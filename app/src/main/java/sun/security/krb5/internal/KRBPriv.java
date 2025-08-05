package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptedData;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KRBPriv.class */
public class KRBPriv {
    public int pvno;
    public int msgType;
    public EncryptedData encPart;

    public KRBPriv(EncryptedData encryptedData) {
        this.pvno = 5;
        this.msgType = 21;
        this.encPart = encryptedData;
    }

    public KRBPriv(byte[] bArr) throws Asn1Exception, KrbApErrException, IOException {
        init(new DerValue(bArr));
    }

    public KRBPriv(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException {
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException {
        if ((derValue.getTag() & 31) != 21 || !derValue.isApplication() || !derValue.isConstructed()) {
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
                if (this.msgType != 21) {
                    throw new KrbApErrException(40);
                }
                this.encPart = EncryptedData.parse(derValue2.getData(), (byte) 3, false);
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
        derOutputStream.putInteger(BigInteger.valueOf(this.pvno));
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putInteger(BigInteger.valueOf(this.msgType));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.encPart.asn1Encode());
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream2);
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write(DerValue.createTag((byte) 64, true, (byte) 21), derOutputStream4);
        return derOutputStream5.toByteArray();
    }
}
