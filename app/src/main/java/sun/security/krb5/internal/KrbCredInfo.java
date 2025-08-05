package sun.security.krb5.internal;

import java.io.IOException;
import java.util.Vector;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KrbCredInfo.class */
public class KrbCredInfo {
    public EncryptionKey key;
    public PrincipalName pname;
    public TicketFlags flags;
    public KerberosTime authtime;
    public KerberosTime starttime;
    public KerberosTime endtime;
    public KerberosTime renewTill;
    public PrincipalName sname;
    public HostAddresses caddr;

    private KrbCredInfo() {
    }

    public KrbCredInfo(EncryptionKey encryptionKey, PrincipalName principalName, TicketFlags ticketFlags, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, PrincipalName principalName2, HostAddresses hostAddresses) {
        this.key = encryptionKey;
        this.pname = principalName;
        this.flags = ticketFlags;
        this.authtime = kerberosTime;
        this.starttime = kerberosTime2;
        this.endtime = kerberosTime3;
        this.renewTill = kerberosTime4;
        this.sname = principalName2;
        this.caddr = hostAddresses;
    }

    public KrbCredInfo(DerValue derValue) throws Asn1Exception, IOException, RealmException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.pname = null;
        this.flags = null;
        this.authtime = null;
        this.starttime = null;
        this.endtime = null;
        this.renewTill = null;
        this.sname = null;
        this.caddr = null;
        this.key = EncryptionKey.parse(derValue.getData(), (byte) 0, false);
        Realm realm = null;
        Realm realm2 = derValue.getData().available() > 0 ? Realm.parse(derValue.getData(), (byte) 1, true) : null;
        if (derValue.getData().available() > 0) {
            this.pname = PrincipalName.parse(derValue.getData(), (byte) 2, true, realm2);
        }
        if (derValue.getData().available() > 0) {
            this.flags = TicketFlags.parse(derValue.getData(), (byte) 3, true);
        }
        if (derValue.getData().available() > 0) {
            this.authtime = KerberosTime.parse(derValue.getData(), (byte) 4, true);
        }
        if (derValue.getData().available() > 0) {
            this.starttime = KerberosTime.parse(derValue.getData(), (byte) 5, true);
        }
        if (derValue.getData().available() > 0) {
            this.endtime = KerberosTime.parse(derValue.getData(), (byte) 6, true);
        }
        if (derValue.getData().available() > 0) {
            this.renewTill = KerberosTime.parse(derValue.getData(), (byte) 7, true);
        }
        realm = derValue.getData().available() > 0 ? Realm.parse(derValue.getData(), (byte) 8, true) : realm;
        if (derValue.getData().available() > 0) {
            this.sname = PrincipalName.parse(derValue.getData(), (byte) 9, true, realm);
        }
        if (derValue.getData().available() > 0) {
            this.caddr = HostAddresses.parse(derValue.getData(), (byte) 10, true);
        }
        if (derValue.getData().available() > 0) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        Vector vector = new Vector();
        vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.key.asn1Encode()));
        if (this.pname != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.pname.getRealm().asn1Encode()));
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.pname.asn1Encode()));
        }
        if (this.flags != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.flags.asn1Encode()));
        }
        if (this.authtime != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.authtime.asn1Encode()));
        }
        if (this.starttime != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), this.starttime.asn1Encode()));
        }
        if (this.endtime != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 6), this.endtime.asn1Encode()));
        }
        if (this.renewTill != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 7), this.renewTill.asn1Encode()));
        }
        if (this.sname != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 8), this.sname.getRealm().asn1Encode()));
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 9), this.sname.asn1Encode()));
        }
        if (this.caddr != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 10), this.caddr.asn1Encode()));
        }
        DerValue[] derValueArr = new DerValue[vector.size()];
        vector.copyInto(derValueArr);
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putSequence(derValueArr);
        return derOutputStream.toByteArray();
    }

    public Object clone() {
        KrbCredInfo krbCredInfo = new KrbCredInfo();
        krbCredInfo.key = (EncryptionKey) this.key.clone();
        if (this.pname != null) {
            krbCredInfo.pname = (PrincipalName) this.pname.clone();
        }
        if (this.flags != null) {
            krbCredInfo.flags = (TicketFlags) this.flags.clone();
        }
        krbCredInfo.authtime = this.authtime;
        krbCredInfo.starttime = this.starttime;
        krbCredInfo.endtime = this.endtime;
        krbCredInfo.renewTill = this.renewTill;
        if (this.sname != null) {
            krbCredInfo.sname = (PrincipalName) this.sname.clone();
        }
        if (this.caddr != null) {
            krbCredInfo.caddr = (HostAddresses) this.caddr.clone();
        }
        return krbCredInfo;
    }
}
