package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/EncKrbPrivPart.class */
public class EncKrbPrivPart {
    public byte[] userData;
    public KerberosTime timestamp;
    public Integer usec;
    public Integer seqNumber;
    public HostAddress sAddress;
    public HostAddress rAddress;

    public EncKrbPrivPart(byte[] bArr, KerberosTime kerberosTime, Integer num, Integer num2, HostAddress hostAddress, HostAddress hostAddress2) {
        this.userData = null;
        if (bArr != null) {
            this.userData = (byte[]) bArr.clone();
        }
        this.timestamp = kerberosTime;
        this.usec = num;
        this.seqNumber = num2;
        this.sAddress = hostAddress;
        this.rAddress = hostAddress2;
    }

    public EncKrbPrivPart(byte[] bArr) throws Asn1Exception, IOException {
        this.userData = null;
        init(new DerValue(bArr));
    }

    public EncKrbPrivPart(DerValue derValue) throws Asn1Exception, IOException {
        this.userData = null;
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, IOException {
        if ((derValue.getTag() & 31) != 28 || !derValue.isApplication() || !derValue.isConstructed()) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) == 0) {
            this.userData = derValue3.getData().getOctetString();
            this.timestamp = KerberosTime.parse(derValue2.getData(), (byte) 1, true);
            if ((derValue2.getData().peekByte() & 31) == 2) {
                this.usec = new Integer(derValue2.getData().getDerValue().getData().getBigInteger().intValue());
            } else {
                this.usec = null;
            }
            if ((derValue2.getData().peekByte() & 31) == 3) {
                this.seqNumber = new Integer(derValue2.getData().getDerValue().getData().getBigInteger().intValue());
            } else {
                this.seqNumber = null;
            }
            this.sAddress = HostAddress.parse(derValue2.getData(), (byte) 4, false);
            if (derValue2.getData().available() > 0) {
                this.rAddress = HostAddress.parse(derValue2.getData(), (byte) 5, true);
            }
            if (derValue2.getData().available() > 0) {
                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
            }
            return;
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putOctetString(this.userData);
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream);
        if (this.timestamp != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.timestamp.asn1Encode());
        }
        if (this.usec != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putInteger(BigInteger.valueOf(this.usec.intValue()));
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream3);
        }
        if (this.seqNumber != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putInteger(BigInteger.valueOf(this.seqNumber.longValue()));
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream4);
        }
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.sAddress.asn1Encode());
        if (this.rAddress != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), this.rAddress.asn1Encode());
        }
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write((byte) 48, derOutputStream2);
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write(DerValue.createTag((byte) 64, true, (byte) 28), derOutputStream5);
        return derOutputStream6.toByteArray();
    }
}
