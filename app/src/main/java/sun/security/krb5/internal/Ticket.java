package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/Ticket.class */
public class Ticket implements Cloneable {
    public int tkt_vno;
    public PrincipalName sname;
    public EncryptedData encPart;

    private Ticket() {
    }

    public Object clone() {
        Ticket ticket = new Ticket();
        ticket.sname = (PrincipalName) this.sname.clone();
        ticket.encPart = (EncryptedData) this.encPart.clone();
        ticket.tkt_vno = this.tkt_vno;
        return ticket;
    }

    public Ticket(PrincipalName principalName, EncryptedData encryptedData) {
        this.tkt_vno = 5;
        this.sname = principalName;
        this.encPart = encryptedData;
    }

    public Ticket(byte[] bArr) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        init(new DerValue(bArr));
    }

    public Ticket(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        if ((derValue.getTag() & 31) != 1 || !derValue.isApplication() || !derValue.isConstructed()) {
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
        this.tkt_vno = derValue3.getData().getBigInteger().intValue();
        if (this.tkt_vno != 5) {
            throw new KrbApErrException(39);
        }
        this.sname = PrincipalName.parse(derValue2.getData(), (byte) 2, false, Realm.parse(derValue2.getData(), (byte) 1, false));
        this.encPart = EncryptedData.parse(derValue2.getData(), (byte) 3, false);
        if (derValue2.getData().available() > 0) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        DerValue[] derValueArr = new DerValue[4];
        derOutputStream2.putInteger(BigInteger.valueOf(this.tkt_vno));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.sname.getRealm().asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.sname.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.encPart.asn1Encode());
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write(DerValue.createTag((byte) 64, true, (byte) 1), derOutputStream3);
        return derOutputStream4.toByteArray();
    }

    public static Ticket parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new Ticket(derValue.getData().getDerValue());
    }
}
