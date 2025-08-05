package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptedData;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/APRep.class */
public class APRep {
    public int pvno;
    public int msgType;
    public EncryptedData encPart;

    public APRep(EncryptedData encryptedData) {
        this.pvno = 5;
        this.msgType = 15;
        this.encPart = encryptedData;
    }

    public APRep(byte[] bArr) throws Asn1Exception, KrbApErrException, IOException {
        init(new DerValue(bArr));
    }

    public APRep(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException {
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException {
        if ((derValue.getTag() & 31) != 15 || !derValue.isApplication() || !derValue.isConstructed()) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) != 0) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.pvno = derValue3.getData().getBigInteger().intValue();
        if (this.pvno != 5) {
            throw new KrbApErrException(39);
        }
        DerValue derValue4 = derValue2.getData().getDerValue();
        if ((derValue4.getTag() & 31) != 1) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.msgType = derValue4.getData().getBigInteger().intValue();
        if (this.msgType != 15) {
            throw new KrbApErrException(40);
        }
        this.encPart = EncryptedData.parse(derValue2.getData(), (byte) 2, false);
        if (derValue2.getData().available() > 0) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(BigInteger.valueOf(this.pvno));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putInteger(BigInteger.valueOf(this.msgType));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.encPart.asn1Encode());
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write(DerValue.createTag((byte) 64, true, (byte) 15), derOutputStream4);
        return derOutputStream5.toByteArray();
    }
}
