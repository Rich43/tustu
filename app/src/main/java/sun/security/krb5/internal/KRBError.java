package sun.security.krb5.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import sun.misc.HexDumpEncoder;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.Checksum;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.util.KerberosString;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KRBError.class */
public class KRBError implements Serializable {
    static final long serialVersionUID = 3643809337475284503L;
    private int pvno;
    private int msgType;
    private KerberosTime cTime;
    private Integer cuSec;
    private KerberosTime sTime;
    private Integer suSec;
    private int errorCode;
    private Realm crealm;
    private PrincipalName cname;
    private PrincipalName sname;
    private String eText;
    private byte[] eData;
    private Checksum eCksum;
    private PAData[] pa;
    private static boolean DEBUG = Krb5.DEBUG;

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            init(new DerValue((byte[]) objectInputStream.readObject()));
            parseEData(this.eData);
        } catch (Exception e2) {
            throw new IOException(e2);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            objectOutputStream.writeObject(asn1Encode());
        } catch (Exception e2) {
            throw new IOException(e2);
        }
    }

    public KRBError(APOptions aPOptions, KerberosTime kerberosTime, Integer num, KerberosTime kerberosTime2, Integer num2, int i2, PrincipalName principalName, PrincipalName principalName2, String str, byte[] bArr) throws Asn1Exception, IOException {
        this.pvno = 5;
        this.msgType = 30;
        this.cTime = kerberosTime;
        this.cuSec = num;
        this.sTime = kerberosTime2;
        this.suSec = num2;
        this.errorCode = i2;
        this.crealm = principalName != null ? principalName.getRealm() : null;
        this.cname = principalName;
        this.sname = principalName2;
        this.eText = str;
        this.eData = bArr;
        parseEData(this.eData);
    }

    public KRBError(APOptions aPOptions, KerberosTime kerberosTime, Integer num, KerberosTime kerberosTime2, Integer num2, int i2, PrincipalName principalName, PrincipalName principalName2, String str, byte[] bArr, Checksum checksum) throws Asn1Exception, IOException {
        this.pvno = 5;
        this.msgType = 30;
        this.cTime = kerberosTime;
        this.cuSec = num;
        this.sTime = kerberosTime2;
        this.suSec = num2;
        this.errorCode = i2;
        this.crealm = principalName != null ? principalName.getRealm() : null;
        this.cname = principalName;
        this.sname = principalName2;
        this.eText = str;
        this.eData = bArr;
        this.eCksum = checksum;
        parseEData(this.eData);
    }

    public KRBError(byte[] bArr) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        init(new DerValue(bArr));
        parseEData(this.eData);
    }

    public KRBError(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        init(derValue);
        showDebug();
        parseEData(this.eData);
    }

    private void parseEData(byte[] bArr) throws IOException {
        if (bArr == null) {
            return;
        }
        if (this.errorCode == 25 || this.errorCode == 24) {
            try {
                parsePAData(bArr);
                return;
            } catch (Exception e2) {
                if (DEBUG) {
                    System.out.println("Unable to parse eData field of KRB-ERROR:\n" + new HexDumpEncoder().encodeBuffer(bArr));
                }
                IOException iOException = new IOException("Unable to parse eData field of KRB-ERROR");
                iOException.initCause(e2);
                throw iOException;
            }
        }
        if (DEBUG) {
            System.out.println("Unknown eData field of KRB-ERROR:\n" + new HexDumpEncoder().encodeBuffer(bArr));
        }
    }

    private void parsePAData(byte[] bArr) throws Asn1Exception, IOException {
        DerValue derValue = new DerValue(bArr);
        ArrayList arrayList = new ArrayList();
        while (derValue.data.available() > 0) {
            PAData pAData = new PAData(derValue.data.getDerValue());
            arrayList.add(pAData);
            if (DEBUG) {
                System.out.println(pAData);
            }
        }
        this.pa = (PAData[]) arrayList.toArray(new PAData[arrayList.size()]);
    }

    public final Realm getClientRealm() {
        return this.crealm;
    }

    public final KerberosTime getServerTime() {
        return this.sTime;
    }

    public final KerberosTime getClientTime() {
        return this.cTime;
    }

    public final Integer getServerMicroSeconds() {
        return this.suSec;
    }

    public final Integer getClientMicroSeconds() {
        return this.cuSec;
    }

    public final int getErrorCode() {
        return this.errorCode;
    }

    public final PAData[] getPA() {
        return this.pa;
    }

    public final String getErrorString() {
        return this.eText;
    }

    private void init(DerValue derValue) throws Asn1Exception, KrbApErrException, IOException, RealmException {
        if ((derValue.getTag() & 31) != 30 || !derValue.isApplication() || !derValue.isConstructed()) {
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
                if (this.msgType != 30) {
                    throw new KrbApErrException(40);
                }
                this.cTime = KerberosTime.parse(derValue2.getData(), (byte) 2, true);
                if ((derValue2.getData().peekByte() & 31) == 3) {
                    this.cuSec = new Integer(derValue2.getData().getDerValue().getData().getBigInteger().intValue());
                } else {
                    this.cuSec = null;
                }
                this.sTime = KerberosTime.parse(derValue2.getData(), (byte) 4, false);
                DerValue derValue5 = derValue2.getData().getDerValue();
                if ((derValue5.getTag() & 31) == 5) {
                    this.suSec = new Integer(derValue5.getData().getBigInteger().intValue());
                    DerValue derValue6 = derValue2.getData().getDerValue();
                    if ((derValue6.getTag() & 31) == 6) {
                        this.errorCode = derValue6.getData().getBigInteger().intValue();
                        this.crealm = Realm.parse(derValue2.getData(), (byte) 7, true);
                        this.cname = PrincipalName.parse(derValue2.getData(), (byte) 8, true, this.crealm);
                        this.sname = PrincipalName.parse(derValue2.getData(), (byte) 10, false, Realm.parse(derValue2.getData(), (byte) 9, false));
                        this.eText = null;
                        this.eData = null;
                        this.eCksum = null;
                        if (derValue2.getData().available() > 0 && (derValue2.getData().peekByte() & 31) == 11) {
                            this.eText = new KerberosString(derValue2.getData().getDerValue().getData().getDerValue()).toString();
                        }
                        if (derValue2.getData().available() > 0 && (derValue2.getData().peekByte() & 31) == 12) {
                            this.eData = derValue2.getData().getDerValue().getData().getOctetString();
                        }
                        if (derValue2.getData().available() > 0) {
                            this.eCksum = Checksum.parse(derValue2.getData(), (byte) 13, true);
                        }
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
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    private void showDebug() {
        if (DEBUG) {
            System.out.println(">>>KRBError:");
            if (this.cTime != null) {
                System.out.println("\t cTime is " + this.cTime.toDate().toString() + " " + this.cTime.toDate().getTime());
            }
            if (this.cuSec != null) {
                System.out.println("\t cuSec is " + this.cuSec.intValue());
            }
            System.out.println("\t sTime is " + this.sTime.toDate().toString() + " " + this.sTime.toDate().getTime());
            System.out.println("\t suSec is " + ((Object) this.suSec));
            System.out.println("\t error code is " + this.errorCode);
            System.out.println("\t error Message is " + Krb5.getErrorMessage(this.errorCode));
            if (this.crealm != null) {
                System.out.println("\t crealm is " + this.crealm.toString());
            }
            if (this.cname != null) {
                System.out.println("\t cname is " + this.cname.toString());
            }
            if (this.sname != null) {
                System.out.println("\t sname is " + this.sname.toString());
            }
            if (this.eData != null) {
                System.out.println("\t eData provided.");
            }
            if (this.eCksum != null) {
                System.out.println("\t checksum provided.");
            }
            System.out.println("\t msgType is " + this.msgType);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putInteger(BigInteger.valueOf(this.pvno));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putInteger(BigInteger.valueOf(this.msgType));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        if (this.cTime != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), this.cTime.asn1Encode());
        }
        if (this.cuSec != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putInteger(BigInteger.valueOf(this.cuSec.intValue()));
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream4);
        }
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.sTime.asn1Encode());
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.putInteger(BigInteger.valueOf(this.suSec.intValue()));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 5), derOutputStream5);
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.putInteger(BigInteger.valueOf(this.errorCode));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 6), derOutputStream6);
        if (this.crealm != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 7), this.crealm.asn1Encode());
        }
        if (this.cname != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 8), this.cname.asn1Encode());
        }
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 9), this.sname.getRealm().asn1Encode());
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 10), this.sname.asn1Encode());
        if (this.eText != null) {
            DerOutputStream derOutputStream7 = new DerOutputStream();
            derOutputStream7.putDerValue(new KerberosString(this.eText).toDerValue());
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 11), derOutputStream7);
        }
        if (this.eData != null) {
            DerOutputStream derOutputStream8 = new DerOutputStream();
            derOutputStream8.putOctetString(this.eData);
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 12), derOutputStream8);
        }
        if (this.eCksum != null) {
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 13), this.eCksum.asn1Encode());
        }
        DerOutputStream derOutputStream9 = new DerOutputStream();
        derOutputStream9.write((byte) 48, derOutputStream2);
        DerOutputStream derOutputStream10 = new DerOutputStream();
        derOutputStream10.write(DerValue.createTag((byte) 64, true, (byte) 30), derOutputStream9);
        return derOutputStream10.toByteArray();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KRBError)) {
            return false;
        }
        KRBError kRBError = (KRBError) obj;
        return this.pvno == kRBError.pvno && this.msgType == kRBError.msgType && isEqual(this.cTime, kRBError.cTime) && isEqual(this.cuSec, kRBError.cuSec) && isEqual(this.sTime, kRBError.sTime) && isEqual(this.suSec, kRBError.suSec) && this.errorCode == kRBError.errorCode && isEqual(this.crealm, kRBError.crealm) && isEqual(this.cname, kRBError.cname) && isEqual(this.sname, kRBError.sname) && isEqual(this.eText, kRBError.eText) && Arrays.equals(this.eData, kRBError.eData) && isEqual(this.eCksum, kRBError.eCksum);
    }

    private static boolean isEqual(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    public int hashCode() {
        int iHashCode = (37 * ((37 * 17) + this.pvno)) + this.msgType;
        if (this.cTime != null) {
            iHashCode = (37 * iHashCode) + this.cTime.hashCode();
        }
        if (this.cuSec != null) {
            iHashCode = (37 * iHashCode) + this.cuSec.hashCode();
        }
        if (this.sTime != null) {
            iHashCode = (37 * iHashCode) + this.sTime.hashCode();
        }
        if (this.suSec != null) {
            iHashCode = (37 * iHashCode) + this.suSec.hashCode();
        }
        int iHashCode2 = (37 * iHashCode) + this.errorCode;
        if (this.crealm != null) {
            iHashCode2 = (37 * iHashCode2) + this.crealm.hashCode();
        }
        if (this.cname != null) {
            iHashCode2 = (37 * iHashCode2) + this.cname.hashCode();
        }
        if (this.sname != null) {
            iHashCode2 = (37 * iHashCode2) + this.sname.hashCode();
        }
        if (this.eText != null) {
            iHashCode2 = (37 * iHashCode2) + this.eText.hashCode();
        }
        int iHashCode3 = (37 * iHashCode2) + Arrays.hashCode(this.eData);
        if (this.eCksum != null) {
            iHashCode3 = (37 * iHashCode3) + this.eCksum.hashCode();
        }
        return iHashCode3;
    }
}
