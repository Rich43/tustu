package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.RealmException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/EncKrbCredPart.class */
public class EncKrbCredPart {
    public KrbCredInfo[] ticketInfo;
    public KerberosTime timeStamp;
    private Integer nonce;
    private Integer usec;
    private HostAddress sAddress;
    private HostAddresses rAddress;

    public EncKrbCredPart(KrbCredInfo[] krbCredInfoArr, KerberosTime kerberosTime, Integer num, Integer num2, HostAddress hostAddress, HostAddresses hostAddresses) throws IOException {
        this.ticketInfo = null;
        if (krbCredInfoArr != null) {
            this.ticketInfo = new KrbCredInfo[krbCredInfoArr.length];
            for (int i2 = 0; i2 < krbCredInfoArr.length; i2++) {
                if (krbCredInfoArr[i2] == null) {
                    throw new IOException("Cannot create a EncKrbCredPart");
                }
                this.ticketInfo[i2] = (KrbCredInfo) krbCredInfoArr[i2].clone();
            }
        }
        this.timeStamp = kerberosTime;
        this.usec = num;
        this.nonce = num2;
        this.sAddress = hostAddress;
        this.rAddress = hostAddresses;
    }

    public EncKrbCredPart(byte[] bArr) throws Asn1Exception, IOException, RealmException {
        this.ticketInfo = null;
        init(new DerValue(bArr));
    }

    public EncKrbCredPart(DerValue derValue) throws Asn1Exception, IOException, RealmException {
        this.ticketInfo = null;
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, IOException, RealmException {
        this.nonce = null;
        this.timeStamp = null;
        this.usec = null;
        this.sAddress = null;
        this.rAddress = null;
        if ((derValue.getTag() & 31) != 29 || !derValue.isApplication() || !derValue.isConstructed()) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) == 0) {
            DerValue[] sequence = derValue3.getData().getSequence(1);
            this.ticketInfo = new KrbCredInfo[sequence.length];
            for (int i2 = 0; i2 < sequence.length; i2++) {
                this.ticketInfo[i2] = new KrbCredInfo(sequence[i2]);
            }
            if (derValue2.getData().available() > 0 && (((byte) derValue2.getData().peekByte()) & 31) == 1) {
                this.nonce = new Integer(derValue2.getData().getDerValue().getData().getBigInteger().intValue());
            }
            if (derValue2.getData().available() > 0) {
                this.timeStamp = KerberosTime.parse(derValue2.getData(), (byte) 2, true);
            }
            if (derValue2.getData().available() > 0 && (((byte) derValue2.getData().peekByte()) & 31) == 3) {
                this.usec = new Integer(derValue2.getData().getDerValue().getData().getBigInteger().intValue());
            }
            if (derValue2.getData().available() > 0) {
                this.sAddress = HostAddress.parse(derValue2.getData(), (byte) 4, true);
            }
            if (derValue2.getData().available() > 0) {
                this.rAddress = HostAddresses.parse(derValue2.getData(), (byte) 5, true);
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
        DerValue[] derValueArr = new DerValue[this.ticketInfo.length];
        for (int i2 = 0; i2 < this.ticketInfo.length; i2++) {
            derValueArr[i2] = new DerValue(this.ticketInfo[i2].asn1Encode());
        }
        derOutputStream2.putSequence(derValueArr);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        if (this.nonce != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putInteger(BigInteger.valueOf(this.nonce.intValue()));
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        }
        if (this.timeStamp != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.timeStamp.asn1Encode());
        }
        if (this.usec != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putInteger(BigInteger.valueOf(this.usec.intValue()));
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream4);
        }
        if (this.sAddress != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.sAddress.asn1Encode());
        }
        if (this.rAddress != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), this.rAddress.asn1Encode());
        }
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write((byte) 48, derOutputStream);
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write(DerValue.createTag((byte) 64, true, (byte) 29), derOutputStream5);
        return derOutputStream6.toByteArray();
    }
}
