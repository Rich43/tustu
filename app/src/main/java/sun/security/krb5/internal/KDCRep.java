package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KDCRep.class */
public class KDCRep {
    public PrincipalName cname;
    public Ticket ticket;
    public EncryptedData encPart;
    public EncKDCRepPart encKDCRepPart;
    private int pvno;
    private int msgType;
    public PAData[] pAData;
    private boolean DEBUG;

    public KDCRep(PAData[] pADataArr, PrincipalName principalName, Ticket ticket, EncryptedData encryptedData, int i2) throws IOException {
        this.pAData = null;
        this.DEBUG = Krb5.DEBUG;
        this.pvno = 5;
        this.msgType = i2;
        if (pADataArr != null) {
            this.pAData = new PAData[pADataArr.length];
            for (int i3 = 0; i3 < pADataArr.length; i3++) {
                if (pADataArr[i3] == null) {
                    throw new IOException("Cannot create a KDCRep");
                }
                this.pAData[i3] = (PAData) pADataArr[i3].clone();
            }
        }
        this.cname = principalName;
        this.ticket = ticket;
        this.encPart = encryptedData;
    }

    public KDCRep() {
        this.pAData = null;
        this.DEBUG = Krb5.DEBUG;
    }

    public KDCRep(byte[] bArr, int i2) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        this.pAData = null;
        this.DEBUG = Krb5.DEBUG;
        init(new DerValue(bArr), i2);
    }

    public KDCRep(DerValue derValue, int i2) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        this.pAData = null;
        this.DEBUG = Krb5.DEBUG;
        init(derValue, i2);
    }

    protected void init(DerValue derValue, int i2) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        if ((derValue.getTag() & 31) != i2) {
            if (this.DEBUG) {
                System.out.println(">>> KDCRep: init() encoding tag is " + ((int) derValue.getTag()) + " req type is " + i2);
            }
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
                if (this.msgType != i2) {
                    throw new KrbApErrException(40);
                }
                if ((derValue2.getData().peekByte() & 31) == 2) {
                    DerValue[] sequence = derValue2.getData().getDerValue().getData().getSequence(1);
                    this.pAData = new PAData[sequence.length];
                    for (int i3 = 0; i3 < sequence.length; i3++) {
                        this.pAData[i3] = new PAData(sequence[i3]);
                    }
                } else {
                    this.pAData = null;
                }
                this.cname = PrincipalName.parse(derValue2.getData(), (byte) 4, false, Realm.parse(derValue2.getData(), (byte) 3, false));
                this.ticket = Ticket.parse(derValue2.getData(), (byte) 5, false);
                this.encPart = EncryptedData.parse(derValue2.getData(), (byte) 6, false);
                if (derValue2.getData().available() > 0) {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
                return;
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(BigInteger.valueOf(this.pvno));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putInteger(BigInteger.valueOf(this.msgType));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        if (this.pAData != null && this.pAData.length > 0) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            for (int i2 = 0; i2 < this.pAData.length; i2++) {
                derOutputStream4.write(this.pAData[i2].asn1Encode());
            }
            DerOutputStream derOutputStream5 = new DerOutputStream();
            derOutputStream5.write((byte) 48, derOutputStream4);
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream5);
        }
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), this.cname.getRealm().asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.cname.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), this.ticket.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 6), this.encPart.asn1Encode());
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write((byte) 48, derOutputStream);
        return derOutputStream6.toByteArray();
    }
}
