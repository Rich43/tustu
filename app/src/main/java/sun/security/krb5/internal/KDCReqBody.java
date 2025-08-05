package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Vector;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KDCReqBody.class */
public class KDCReqBody {
    public KDCOptions kdcOptions;
    public PrincipalName cname;
    public PrincipalName sname;
    public KerberosTime from;
    public KerberosTime till;
    public KerberosTime rtime;
    public HostAddresses addresses;
    private int nonce;
    private int[] eType;
    private EncryptedData encAuthorizationData;
    private Ticket[] additionalTickets;

    public KDCReqBody(KDCOptions kDCOptions, PrincipalName principalName, PrincipalName principalName2, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, int i2, int[] iArr, HostAddresses hostAddresses, EncryptedData encryptedData, Ticket[] ticketArr) throws IOException {
        this.eType = null;
        this.kdcOptions = kDCOptions;
        this.cname = principalName;
        this.sname = principalName2;
        this.from = kerberosTime;
        this.till = kerberosTime2;
        this.rtime = kerberosTime3;
        this.nonce = i2;
        if (iArr != null) {
            this.eType = (int[]) iArr.clone();
        }
        this.addresses = hostAddresses;
        this.encAuthorizationData = encryptedData;
        if (ticketArr != null) {
            this.additionalTickets = new Ticket[ticketArr.length];
            for (int i3 = 0; i3 < ticketArr.length; i3++) {
                if (ticketArr[i3] == null) {
                    throw new IOException("Cannot create a KDCReqBody");
                }
                this.additionalTickets[i3] = (Ticket) ticketArr[i3].clone();
            }
        }
    }

    public KDCReqBody(DerValue derValue, int i2) throws IOException, KrbException {
        this.eType = null;
        this.addresses = null;
        this.encAuthorizationData = null;
        this.additionalTickets = null;
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        this.kdcOptions = KDCOptions.parse(derValue.getData(), (byte) 0, false);
        this.cname = PrincipalName.parse(derValue.getData(), (byte) 1, true, new Realm("PLACEHOLDER"));
        if (i2 != 10 && this.cname != null) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        Realm realm = Realm.parse(derValue.getData(), (byte) 2, false);
        if (this.cname != null) {
            this.cname = new PrincipalName(this.cname.getNameType(), this.cname.getNameStrings(), realm);
        }
        this.sname = PrincipalName.parse(derValue.getData(), (byte) 3, true, realm);
        this.from = KerberosTime.parse(derValue.getData(), (byte) 4, true);
        this.till = KerberosTime.parse(derValue.getData(), (byte) 5, false);
        this.rtime = KerberosTime.parse(derValue.getData(), (byte) 6, true);
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 7) {
            this.nonce = derValue2.getData().getBigInteger().intValue();
            DerValue derValue3 = derValue.getData().getDerValue();
            Vector vector = new Vector();
            if ((derValue3.getTag() & 31) == 8) {
                DerValue derValue4 = derValue3.getData().getDerValue();
                if (derValue4.getTag() == 48) {
                    while (derValue4.getData().available() > 0) {
                        vector.addElement(Integer.valueOf(derValue4.getData().getBigInteger().intValue()));
                    }
                    this.eType = new int[vector.size()];
                    for (int i3 = 0; i3 < vector.size(); i3++) {
                        this.eType[i3] = ((Integer) vector.elementAt(i3)).intValue();
                    }
                    if (derValue.getData().available() > 0) {
                        this.addresses = HostAddresses.parse(derValue.getData(), (byte) 9, true);
                    }
                    if (derValue.getData().available() > 0) {
                        this.encAuthorizationData = EncryptedData.parse(derValue.getData(), (byte) 10, true);
                    }
                    if (derValue.getData().available() > 0) {
                        Vector vector2 = new Vector();
                        DerValue derValue5 = derValue.getData().getDerValue();
                        if ((derValue5.getTag() & 31) == 11) {
                            DerValue derValue6 = derValue5.getData().getDerValue();
                            if (derValue6.getTag() == 48) {
                                while (derValue6.getData().available() > 0) {
                                    vector2.addElement(new Ticket(derValue6.getData().getDerValue()));
                                }
                                if (vector2.size() > 0) {
                                    this.additionalTickets = new Ticket[vector2.size()];
                                    vector2.copyInto(this.additionalTickets);
                                }
                            } else {
                                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                            }
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
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode(int i2) throws Asn1Exception, IOException {
        Vector vector = new Vector();
        vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.kdcOptions.asn1Encode()));
        if (i2 == 10 && this.cname != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.cname.asn1Encode()));
        }
        if (this.sname != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.sname.getRealm().asn1Encode()));
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.sname.asn1Encode()));
        } else if (this.cname != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.cname.getRealm().asn1Encode()));
        }
        if (this.from != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.from.asn1Encode()));
        }
        vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), this.till.asn1Encode()));
        if (this.rtime != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 6), this.rtime.asn1Encode()));
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(BigInteger.valueOf(this.nonce));
        vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 7), derOutputStream.toByteArray()));
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (int i3 = 0; i3 < this.eType.length; i3++) {
            derOutputStream2.putInteger(BigInteger.valueOf(this.eType[i3]));
        }
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream2);
        vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 8), derOutputStream3.toByteArray()));
        if (this.addresses != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 9), this.addresses.asn1Encode()));
        }
        if (this.encAuthorizationData != null) {
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 10), this.encAuthorizationData.asn1Encode()));
        }
        if (this.additionalTickets != null && this.additionalTickets.length > 0) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            for (int i4 = 0; i4 < this.additionalTickets.length; i4++) {
                derOutputStream4.write(this.additionalTickets[i4].asn1Encode());
            }
            DerOutputStream derOutputStream5 = new DerOutputStream();
            derOutputStream5.write((byte) 48, derOutputStream4);
            vector.addElement(new DerValue(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 11), derOutputStream5.toByteArray()));
        }
        DerValue[] derValueArr = new DerValue[vector.size()];
        vector.copyInto(derValueArr);
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.putSequence(derValueArr);
        return derOutputStream6.toByteArray();
    }

    public int getNonce() {
        return this.nonce;
    }
}
