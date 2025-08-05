package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/EncTicketPart.class */
public class EncTicketPart {
    public TicketFlags flags;
    public EncryptionKey key;
    public PrincipalName cname;
    public TransitedEncoding transited;
    public KerberosTime authtime;
    public KerberosTime starttime;
    public KerberosTime endtime;
    public KerberosTime renewTill;
    public HostAddresses caddr;
    public AuthorizationData authorizationData;

    public EncTicketPart(TicketFlags ticketFlags, EncryptionKey encryptionKey, PrincipalName principalName, TransitedEncoding transitedEncoding, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, HostAddresses hostAddresses, AuthorizationData authorizationData) {
        this.flags = ticketFlags;
        this.key = encryptionKey;
        this.cname = principalName;
        this.transited = transitedEncoding;
        this.authtime = kerberosTime;
        this.starttime = kerberosTime2;
        this.endtime = kerberosTime3;
        this.renewTill = kerberosTime4;
        this.caddr = hostAddresses;
        this.authorizationData = authorizationData;
    }

    public EncTicketPart(byte[] bArr) throws IOException, KrbException {
        init(new DerValue(bArr));
    }

    public EncTicketPart(DerValue derValue) throws IOException, KrbException {
        init(derValue);
    }

    private static String getHexBytes(byte[] bArr, int i2) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = (bArr[i3] >> 4) & 15;
            int i5 = bArr[i3] & 15;
            stringBuffer.append(Integer.toHexString(i4));
            stringBuffer.append(Integer.toHexString(i5));
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }

    private void init(DerValue derValue) throws Asn1Exception, IOException, RealmException {
        this.renewTill = null;
        this.caddr = null;
        this.authorizationData = null;
        if ((derValue.getTag() & 31) != 3 || !derValue.isApplication() || !derValue.isConstructed()) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.flags = TicketFlags.parse(derValue2.getData(), (byte) 0, false);
        this.key = EncryptionKey.parse(derValue2.getData(), (byte) 1, false);
        this.cname = PrincipalName.parse(derValue2.getData(), (byte) 3, false, Realm.parse(derValue2.getData(), (byte) 2, false));
        this.transited = TransitedEncoding.parse(derValue2.getData(), (byte) 4, false);
        this.authtime = KerberosTime.parse(derValue2.getData(), (byte) 5, false);
        this.starttime = KerberosTime.parse(derValue2.getData(), (byte) 6, true);
        this.endtime = KerberosTime.parse(derValue2.getData(), (byte) 7, false);
        if (derValue2.getData().available() > 0) {
            this.renewTill = KerberosTime.parse(derValue2.getData(), (byte) 8, true);
        }
        if (derValue2.getData().available() > 0) {
            this.caddr = HostAddresses.parse(derValue2.getData(), (byte) 9, true);
        }
        if (derValue2.getData().available() > 0) {
            this.authorizationData = AuthorizationData.parse(derValue2.getData(), (byte) 10, true);
        }
        if (derValue2.getData().available() > 0) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.flags.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.key.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.cname.getRealm().asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.cname.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.transited.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), this.authtime.asn1Encode());
        if (this.starttime != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 6), this.starttime.asn1Encode());
        }
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 7), this.endtime.asn1Encode());
        if (this.renewTill != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 8), this.renewTill.asn1Encode());
        }
        if (this.caddr != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 9), this.caddr.asn1Encode());
        }
        if (this.authorizationData != null) {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 10), this.authorizationData.asn1Encode());
        }
        derOutputStream2.write((byte) 48, derOutputStream);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write(DerValue.createTag((byte) 64, true, (byte) 3), derOutputStream2);
        return derOutputStream3.toByteArray();
    }
}
