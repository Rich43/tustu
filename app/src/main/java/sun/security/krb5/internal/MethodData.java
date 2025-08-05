package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/MethodData.class */
public class MethodData {
    private int methodType;
    private byte[] methodData;

    public MethodData(int i2, byte[] bArr) {
        this.methodData = null;
        this.methodType = i2;
        if (bArr != null) {
            this.methodData = (byte[]) bArr.clone();
        }
    }

    public MethodData(DerValue derValue) throws Asn1Exception, IOException {
        this.methodData = null;
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.methodType = derValue2.getData().getBigInteger().intValue();
            if (derValue.getData().available() > 0) {
                DerValue derValue3 = derValue.getData().getDerValue();
                if ((derValue3.getTag() & 31) == 1) {
                    this.methodData = derValue3.getData().getOctetString();
                } else {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
            }
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
        derOutputStream2.putInteger(BigInteger.valueOf(this.methodType));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        if (this.methodData != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putOctetString(this.methodData);
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        }
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }
}
