package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Vector;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.RealmException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KRBCred.class */
public class KRBCred {
    public Ticket[] tickets;
    public EncryptedData encPart;
    private int pvno;
    private int msgType;

    public KRBCred(Ticket[] ticketArr, EncryptedData encryptedData) throws IOException {
        this.tickets = null;
        this.pvno = 5;
        this.msgType = 22;
        if (ticketArr != null) {
            this.tickets = new Ticket[ticketArr.length];
            for (int i2 = 0; i2 < ticketArr.length; i2++) {
                if (ticketArr[i2] == null) {
                    throw new IOException("Cannot create a KRBCred");
                }
                this.tickets[i2] = (Ticket) ticketArr[i2].clone();
            }
        }
        this.encPart = encryptedData;
    }

    public KRBCred(byte[] bArr) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        this.tickets = null;
        init(new DerValue(bArr));
    }

    public KRBCred(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        this.tickets = null;
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        if ((derValue.getTag() & 31) != 22 || !derValue.isApplication() || !derValue.isConstructed()) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) == 0) {
            this.pvno = derValue3.getData().getBigInteger().intValue();
            if (this.pvno != 5) {
                throw new KrbApErrException(39);
            }
            DerValue derValue4 = derValue2.getData().getDerValue();
            if ((derValue4.getTag() & 31) == 1) {
                this.msgType = derValue4.getData().getBigInteger().intValue();
                if (this.msgType != 22) {
                    throw new KrbApErrException(40);
                }
                DerValue derValue5 = derValue2.getData().getDerValue();
                if ((derValue5.getTag() & 31) == 2) {
                    DerValue derValue6 = derValue5.getData().getDerValue();
                    if (derValue6.getTag() != 48) {
                        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                    }
                    Vector vector = new Vector();
                    while (derValue6.getData().available() > 0) {
                        vector.addElement(new Ticket(derValue6.getData().getDerValue()));
                    }
                    if (vector.size() > 0) {
                        this.tickets = new Ticket[vector.size()];
                        vector.copyInto(this.tickets);
                    }
                    this.encPart = EncryptedData.parse(derValue2.getData(), (byte) 3, false);
                    if (derValue2.getData().available() > 0) {
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

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(BigInteger.valueOf(this.pvno));
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putInteger(BigInteger.valueOf(this.msgType));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        for (int i2 = 0; i2 < this.tickets.length; i2++) {
            derOutputStream4.write(this.tickets[i2].asn1Encode());
        }
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write((byte) 48, derOutputStream4);
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream5);
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.encPart.asn1Encode());
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write((byte) 48, derOutputStream2);
        DerOutputStream derOutputStream7 = new DerOutputStream();
        derOutputStream7.write(DerValue.createTag((byte) 64, true, (byte) 22), derOutputStream6);
        return derOutputStream7.toByteArray();
    }
}
