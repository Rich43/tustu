package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KRBSafeBody.class */
public class KRBSafeBody {
    public byte[] userData;
    public KerberosTime timestamp;
    public Integer usec;
    public Integer seqNumber;
    public HostAddress sAddress;
    public HostAddress rAddress;

    public KRBSafeBody(byte[] bArr, KerberosTime kerberosTime, Integer num, Integer num2, HostAddress hostAddress, HostAddress hostAddress2) {
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

    public KRBSafeBody(DerValue derValue) throws Asn1Exception, IOException {
        this.userData = null;
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.userData = derValue2.getData().getOctetString();
            this.timestamp = KerberosTime.parse(derValue.getData(), (byte) 1, true);
            if ((derValue.getData().peekByte() & 31) == 2) {
                this.usec = new Integer(derValue.getData().getDerValue().getData().getBigInteger().intValue());
            }
            if ((derValue.getData().peekByte() & 31) == 3) {
                this.seqNumber = new Integer(derValue.getData().getDerValue().getData().getBigInteger().intValue());
            }
            this.sAddress = HostAddress.parse(derValue.getData(), (byte) 4, false);
            if (derValue.getData().available() > 0) {
                this.rAddress = HostAddress.parse(derValue.getData(), (byte) 5, true);
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
        derOutputStream2.putOctetString(this.userData);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        if (this.timestamp != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.timestamp.asn1Encode());
        }
        if (this.usec != null) {
            derOutputStream2 = new DerOutputStream();
            derOutputStream2.putInteger(BigInteger.valueOf(this.usec.intValue()));
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream2);
        }
        if (this.seqNumber != null) {
            derOutputStream2 = new DerOutputStream();
            derOutputStream2.putInteger(BigInteger.valueOf(this.seqNumber.longValue()));
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream2);
        }
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.sAddress.asn1Encode());
        if (this.rAddress != null) {
            derOutputStream2 = new DerOutputStream();
        }
        derOutputStream2.write((byte) 48, derOutputStream);
        return derOutputStream2.toByteArray();
    }

    public static KRBSafeBody parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new KRBSafeBody(derValue.getData().getDerValue());
    }
}
