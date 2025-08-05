package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/EncKDCRepPart.class */
public class EncKDCRepPart {
    public EncryptionKey key;
    public LastReq lastReq;
    public int nonce;
    public KerberosTime keyExpiration;
    public TicketFlags flags;
    public KerberosTime authtime;
    public KerberosTime starttime;
    public KerberosTime endtime;
    public KerberosTime renewTill;
    public PrincipalName sname;
    public HostAddresses caddr;
    public PAData[] pAData;
    public int msgType;

    public EncKDCRepPart(EncryptionKey encryptionKey, LastReq lastReq, int i2, KerberosTime kerberosTime, TicketFlags ticketFlags, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, KerberosTime kerberosTime5, PrincipalName principalName, HostAddresses hostAddresses, PAData[] pADataArr, int i3) {
        this.key = encryptionKey;
        this.lastReq = lastReq;
        this.nonce = i2;
        this.keyExpiration = kerberosTime;
        this.flags = ticketFlags;
        this.authtime = kerberosTime2;
        this.starttime = kerberosTime3;
        this.endtime = kerberosTime4;
        this.renewTill = kerberosTime5;
        this.sname = principalName;
        this.caddr = hostAddresses;
        this.pAData = pADataArr;
        this.msgType = i3;
    }

    public EncKDCRepPart() {
    }

    public EncKDCRepPart(byte[] bArr, int i2) throws Asn1Exception, IOException, RealmException {
        init(new DerValue(bArr), i2);
    }

    public EncKDCRepPart(DerValue derValue, int i2) throws Asn1Exception, IOException, RealmException {
        init(derValue, i2);
    }

    protected void init(DerValue derValue, int i2) throws Asn1Exception, IOException, RealmException {
        this.msgType = derValue.getTag() & 31;
        if (this.msgType != 25 && this.msgType != 26) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.key = EncryptionKey.parse(derValue2.getData(), (byte) 0, false);
        this.lastReq = LastReq.parse(derValue2.getData(), (byte) 1, false);
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) == 2) {
            this.nonce = derValue3.getData().getBigInteger().intValue();
            this.keyExpiration = KerberosTime.parse(derValue2.getData(), (byte) 3, true);
            this.flags = TicketFlags.parse(derValue2.getData(), (byte) 4, false);
            this.authtime = KerberosTime.parse(derValue2.getData(), (byte) 5, false);
            this.starttime = KerberosTime.parse(derValue2.getData(), (byte) 6, true);
            this.endtime = KerberosTime.parse(derValue2.getData(), (byte) 7, false);
            this.renewTill = KerberosTime.parse(derValue2.getData(), (byte) 8, true);
            this.sname = PrincipalName.parse(derValue2.getData(), (byte) 10, false, Realm.parse(derValue2.getData(), (byte) 9, false));
            if (derValue2.getData().available() > 0) {
                this.caddr = HostAddresses.parse(derValue2.getData(), (byte) 11, true);
            }
            if (derValue2.getData().available() > 0) {
                this.pAData = PAData.parseSequence(derValue2.getData(), (byte) 12, true);
                return;
            }
            return;
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode(int i2) throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.key.asn1Encode());
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.lastReq.asn1Encode());
        derOutputStream.putInteger(BigInteger.valueOf(this.nonce));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream);
        if (this.keyExpiration != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.keyExpiration.asn1Encode());
        }
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.flags.asn1Encode());
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), this.authtime.asn1Encode());
        if (this.starttime != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 6), this.starttime.asn1Encode());
        }
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 7), this.endtime.asn1Encode());
        if (this.renewTill != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 8), this.renewTill.asn1Encode());
        }
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 9), this.sname.getRealm().asn1Encode());
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 10), this.sname.asn1Encode());
        if (this.caddr != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 11), this.caddr.asn1Encode());
        }
        if (this.pAData != null && this.pAData.length > 0) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            for (int i3 = 0; i3 < this.pAData.length; i3++) {
                derOutputStream3.write(this.pAData[i3].asn1Encode());
            }
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.write((byte) 48, derOutputStream3);
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 12), derOutputStream4);
        }
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write((byte) 48, derOutputStream2);
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write(DerValue.createTag((byte) 64, true, (byte) this.msgType), derOutputStream5);
        return derOutputStream6.toByteArray();
    }
}
