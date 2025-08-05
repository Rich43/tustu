package sun.security.pkcs;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.util.Date;
import sun.misc.HexDumpEncoder;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.KnownOIDs;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.CertificateExtensions;

/* loaded from: rt.jar:sun/security/pkcs/PKCS9Attribute.class */
public class PKCS9Attribute implements DerEncoder {
    private static final Debug debug = Debug.getInstance("jar");
    static final ObjectIdentifier[] PKCS9_OIDS = new ObjectIdentifier[18];
    private static final Class<?> BYTE_ARRAY_CLASS;
    public static final ObjectIdentifier EMAIL_ADDRESS_OID;
    public static final ObjectIdentifier UNSTRUCTURED_NAME_OID;
    public static final ObjectIdentifier CONTENT_TYPE_OID;
    public static final ObjectIdentifier MESSAGE_DIGEST_OID;
    public static final ObjectIdentifier SIGNING_TIME_OID;
    public static final ObjectIdentifier COUNTERSIGNATURE_OID;
    public static final ObjectIdentifier CHALLENGE_PASSWORD_OID;
    public static final ObjectIdentifier UNSTRUCTURED_ADDRESS_OID;
    public static final ObjectIdentifier EXTENDED_CERTIFICATE_ATTRIBUTES_OID;
    public static final ObjectIdentifier ISSUER_SERIALNUMBER_OID;
    public static final ObjectIdentifier EXTENSION_REQUEST_OID;
    public static final ObjectIdentifier SIGNING_CERTIFICATE_OID;
    public static final ObjectIdentifier SIGNATURE_TIMESTAMP_TOKEN_OID;
    private static final Byte[][] PKCS9_VALUE_TAGS;
    private static final Class<?>[] VALUE_CLASSES;
    private static final boolean[] SINGLE_VALUED;
    private ObjectIdentifier oid;
    private int index;
    private Object value;

    /* JADX WARN: Type inference failed for: r0v35, types: [java.lang.Byte[], java.lang.Byte[][]] */
    static {
        ObjectIdentifier[] objectIdentifierArr = PKCS9_OIDS;
        ObjectIdentifier[] objectIdentifierArr2 = PKCS9_OIDS;
        ObjectIdentifier[] objectIdentifierArr3 = PKCS9_OIDS;
        ObjectIdentifier[] objectIdentifierArr4 = PKCS9_OIDS;
        PKCS9_OIDS[15] = null;
        objectIdentifierArr4[13] = null;
        objectIdentifierArr3[12] = null;
        objectIdentifierArr2[11] = null;
        objectIdentifierArr[0] = null;
        try {
            BYTE_ARRAY_CLASS = Class.forName("[B");
            ObjectIdentifier[] objectIdentifierArr5 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf = ObjectIdentifier.of(KnownOIDs.EmailAddress);
            objectIdentifierArr5[1] = objectIdentifierOf;
            EMAIL_ADDRESS_OID = objectIdentifierOf;
            ObjectIdentifier[] objectIdentifierArr6 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf2 = ObjectIdentifier.of(KnownOIDs.UnstructuredName);
            objectIdentifierArr6[2] = objectIdentifierOf2;
            UNSTRUCTURED_NAME_OID = objectIdentifierOf2;
            ObjectIdentifier[] objectIdentifierArr7 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf3 = ObjectIdentifier.of(KnownOIDs.ContentType);
            objectIdentifierArr7[3] = objectIdentifierOf3;
            CONTENT_TYPE_OID = objectIdentifierOf3;
            ObjectIdentifier[] objectIdentifierArr8 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf4 = ObjectIdentifier.of(KnownOIDs.MessageDigest);
            objectIdentifierArr8[4] = objectIdentifierOf4;
            MESSAGE_DIGEST_OID = objectIdentifierOf4;
            ObjectIdentifier[] objectIdentifierArr9 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf5 = ObjectIdentifier.of(KnownOIDs.SigningTime);
            objectIdentifierArr9[5] = objectIdentifierOf5;
            SIGNING_TIME_OID = objectIdentifierOf5;
            ObjectIdentifier[] objectIdentifierArr10 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf6 = ObjectIdentifier.of(KnownOIDs.CounterSignature);
            objectIdentifierArr10[6] = objectIdentifierOf6;
            COUNTERSIGNATURE_OID = objectIdentifierOf6;
            ObjectIdentifier[] objectIdentifierArr11 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf7 = ObjectIdentifier.of(KnownOIDs.ChallengePassword);
            objectIdentifierArr11[7] = objectIdentifierOf7;
            CHALLENGE_PASSWORD_OID = objectIdentifierOf7;
            ObjectIdentifier[] objectIdentifierArr12 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf8 = ObjectIdentifier.of(KnownOIDs.UnstructuredAddress);
            objectIdentifierArr12[8] = objectIdentifierOf8;
            UNSTRUCTURED_ADDRESS_OID = objectIdentifierOf8;
            ObjectIdentifier[] objectIdentifierArr13 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf9 = ObjectIdentifier.of(KnownOIDs.ExtendedCertificateAttributes);
            objectIdentifierArr13[9] = objectIdentifierOf9;
            EXTENDED_CERTIFICATE_ATTRIBUTES_OID = objectIdentifierOf9;
            ObjectIdentifier[] objectIdentifierArr14 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf10 = ObjectIdentifier.of(KnownOIDs.IssuerAndSerialNumber);
            objectIdentifierArr14[10] = objectIdentifierOf10;
            ISSUER_SERIALNUMBER_OID = objectIdentifierOf10;
            ObjectIdentifier[] objectIdentifierArr15 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf11 = ObjectIdentifier.of(KnownOIDs.ExtensionRequest);
            objectIdentifierArr15[14] = objectIdentifierOf11;
            EXTENSION_REQUEST_OID = objectIdentifierOf11;
            ObjectIdentifier[] objectIdentifierArr16 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf12 = ObjectIdentifier.of(KnownOIDs.SigningCertificate);
            objectIdentifierArr16[16] = objectIdentifierOf12;
            SIGNING_CERTIFICATE_OID = objectIdentifierOf12;
            ObjectIdentifier[] objectIdentifierArr17 = PKCS9_OIDS;
            ObjectIdentifier objectIdentifierOf13 = ObjectIdentifier.of(KnownOIDs.SignatureTimestampToken);
            objectIdentifierArr17[17] = objectIdentifierOf13;
            SIGNATURE_TIMESTAMP_TOKEN_OID = objectIdentifierOf13;
            PKCS9_VALUE_TAGS = new Byte[]{0, new Byte[]{new Byte((byte) 22)}, new Byte[]{new Byte((byte) 22), new Byte((byte) 19)}, new Byte[]{new Byte((byte) 6)}, new Byte[]{new Byte((byte) 4)}, new Byte[]{new Byte((byte) 23)}, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 19), new Byte((byte) 20)}, new Byte[]{new Byte((byte) 19), new Byte((byte) 20)}, new Byte[]{new Byte((byte) 49)}, new Byte[]{new Byte((byte) 48)}, 0, 0, 0, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 48)}};
            VALUE_CLASSES = new Class[18];
            try {
                Class<?> cls = Class.forName("[Ljava.lang.String;");
                VALUE_CLASSES[0] = null;
                VALUE_CLASSES[1] = cls;
                VALUE_CLASSES[2] = cls;
                VALUE_CLASSES[3] = Class.forName("sun.security.util.ObjectIdentifier");
                VALUE_CLASSES[4] = BYTE_ARRAY_CLASS;
                VALUE_CLASSES[5] = Class.forName("java.util.Date");
                VALUE_CLASSES[6] = Class.forName("[Lsun.security.pkcs.SignerInfo;");
                VALUE_CLASSES[7] = Class.forName("java.lang.String");
                VALUE_CLASSES[8] = cls;
                VALUE_CLASSES[9] = null;
                VALUE_CLASSES[10] = null;
                VALUE_CLASSES[11] = null;
                VALUE_CLASSES[12] = null;
                VALUE_CLASSES[13] = null;
                VALUE_CLASSES[14] = Class.forName("sun.security.x509.CertificateExtensions");
                VALUE_CLASSES[15] = null;
                VALUE_CLASSES[16] = null;
                VALUE_CLASSES[17] = BYTE_ARRAY_CLASS;
                SINGLE_VALUED = new boolean[]{false, false, false, true, true, true, false, true, false, false, true, false, false, false, true, true, true, true};
            } catch (ClassNotFoundException e2) {
                throw new ExceptionInInitializerError(e2.toString());
            }
        } catch (ClassNotFoundException e3) {
            throw new ExceptionInInitializerError(e3.toString());
        }
    }

    public PKCS9Attribute(ObjectIdentifier objectIdentifier, Object obj) throws IllegalArgumentException {
        init(objectIdentifier, obj);
    }

    private void init(ObjectIdentifier objectIdentifier, Object obj) throws IllegalArgumentException {
        this.oid = objectIdentifier;
        this.index = indexOf(objectIdentifier, PKCS9_OIDS, 1);
        Class<?> cls = this.index == -1 ? BYTE_ARRAY_CLASS : VALUE_CLASSES[this.index];
        if (!cls.isInstance(obj)) {
            throw new IllegalArgumentException("Wrong value class  for attribute " + ((Object) objectIdentifier) + " constructing PKCS9Attribute; was " + obj.getClass().toString() + ", should be " + cls.toString());
        }
        this.value = obj;
    }

    public PKCS9Attribute(DerValue derValue) throws IOException {
        DerInputStream derInputStream = new DerInputStream(derValue.toByteArray());
        DerValue[] sequence = derInputStream.getSequence(2);
        if (derInputStream.available() != 0) {
            throw new IOException("Excess data parsing PKCS9Attribute");
        }
        if (sequence.length != 2) {
            throw new IOException("PKCS9Attribute doesn't have two components");
        }
        this.oid = sequence[0].getOID();
        byte[] byteArray = sequence[1].toByteArray();
        DerValue[] set = new DerInputStream(byteArray).getSet(1);
        this.index = indexOf(this.oid, PKCS9_OIDS, 1);
        if (this.index == -1) {
            if (debug != null) {
                debug.println("Unsupported signer attribute: " + ((Object) this.oid));
            }
            this.value = byteArray;
            return;
        }
        if (SINGLE_VALUED[this.index] && set.length > 1) {
            throwSingleValuedException();
        }
        for (DerValue derValue2 : set) {
            Byte b2 = new Byte(derValue2.tag);
            if (indexOf(b2, PKCS9_VALUE_TAGS[this.index], 0) == -1) {
                throwTagException(b2);
            }
        }
        switch (this.index) {
            case 1:
            case 2:
            case 8:
                String[] strArr = new String[set.length];
                for (int i2 = 0; i2 < set.length; i2++) {
                    strArr[i2] = set[i2].getAsString();
                }
                this.value = strArr;
                return;
            case 3:
                this.value = set[0].getOID();
                return;
            case 4:
                this.value = set[0].getOctetString();
                return;
            case 5:
                this.value = new DerInputStream(set[0].toByteArray()).getUTCTime();
                return;
            case 6:
                SignerInfo[] signerInfoArr = new SignerInfo[set.length];
                for (int i3 = 0; i3 < set.length; i3++) {
                    signerInfoArr[i3] = new SignerInfo(set[i3].toDerInputStream());
                }
                this.value = signerInfoArr;
                return;
            case 7:
                this.value = set[0].getAsString();
                return;
            case 9:
                throw new IOException("PKCS9 extended-certificate attribute not supported.");
            case 10:
                throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
            case 11:
            case 12:
                throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
            case 13:
                throw new IOException("PKCS9 attribute #13 not supported.");
            case 14:
                this.value = new CertificateExtensions(new DerInputStream(set[0].toByteArray()));
                return;
            case 15:
                throw new IOException("PKCS9 SMIMECapability attribute not supported.");
            case 16:
                this.value = new SigningCertificateInfo(set[0].toByteArray());
                return;
            case 17:
                this.value = set[0].toByteArray();
                return;
            default:
                return;
        }
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOID(this.oid);
        switch (this.index) {
            case -1:
                derOutputStream.write((byte[]) this.value);
                break;
            case 1:
            case 2:
                String[] strArr = (String[]) this.value;
                DerOutputStream[] derOutputStreamArr = new DerOutputStream[strArr.length];
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    derOutputStreamArr[i2] = new DerOutputStream();
                    derOutputStreamArr[i2].putIA5String(strArr[i2]);
                }
                derOutputStream.putOrderedSetOf((byte) 49, derOutputStreamArr);
                break;
            case 3:
                DerOutputStream derOutputStream2 = new DerOutputStream();
                derOutputStream2.putOID((ObjectIdentifier) this.value);
                derOutputStream.write((byte) 49, derOutputStream2.toByteArray());
                break;
            case 4:
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.putOctetString((byte[]) this.value);
                derOutputStream.write((byte) 49, derOutputStream3.toByteArray());
                break;
            case 5:
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.putUTCTime((Date) this.value);
                derOutputStream.write((byte) 49, derOutputStream4.toByteArray());
                break;
            case 6:
                derOutputStream.putOrderedSetOf((byte) 49, (DerEncoder[]) this.value);
                break;
            case 7:
                DerOutputStream derOutputStream5 = new DerOutputStream();
                derOutputStream5.putPrintableString((String) this.value);
                derOutputStream.write((byte) 49, derOutputStream5.toByteArray());
                break;
            case 8:
                String[] strArr2 = (String[]) this.value;
                DerOutputStream[] derOutputStreamArr2 = new DerOutputStream[strArr2.length];
                for (int i3 = 0; i3 < strArr2.length; i3++) {
                    derOutputStreamArr2[i3] = new DerOutputStream();
                    derOutputStreamArr2[i3].putPrintableString(strArr2[i3]);
                }
                derOutputStream.putOrderedSetOf((byte) 49, derOutputStreamArr2);
                break;
            case 9:
                throw new IOException("PKCS9 extended-certificate attribute not supported.");
            case 10:
                throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
            case 11:
            case 12:
                throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
            case 13:
                throw new IOException("PKCS9 attribute #13 not supported.");
            case 14:
                DerOutputStream derOutputStream6 = new DerOutputStream();
                try {
                    ((CertificateExtensions) this.value).encode(derOutputStream6, true);
                    derOutputStream.write((byte) 49, derOutputStream6.toByteArray());
                    break;
                } catch (CertificateException e2) {
                    throw new IOException(e2.toString());
                }
            case 15:
                throw new IOException("PKCS9 attribute #15 not supported.");
            case 16:
                throw new IOException("PKCS9 SigningCertificate attribute not supported.");
            case 17:
                derOutputStream.write((byte) 49, (byte[]) this.value);
                break;
        }
        DerOutputStream derOutputStream7 = new DerOutputStream();
        derOutputStream7.write((byte) 48, derOutputStream.toByteArray());
        outputStream.write(derOutputStream7.toByteArray());
    }

    public boolean isKnown() {
        return this.index != -1;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isSingleValued() {
        return this.index == -1 || SINGLE_VALUED[this.index];
    }

    public ObjectIdentifier getOID() {
        return this.oid;
    }

    public String getName() {
        String string = this.oid.toString();
        KnownOIDs knownOIDsFindMatch = KnownOIDs.findMatch(string);
        return knownOIDsFindMatch == null ? string : knownOIDsFindMatch.stdName();
    }

    public static ObjectIdentifier getOID(String str) {
        KnownOIDs knownOIDsFindMatch = KnownOIDs.findMatch(str);
        if (knownOIDsFindMatch != null) {
            return ObjectIdentifier.of(knownOIDsFindMatch);
        }
        return null;
    }

    public static String getName(ObjectIdentifier objectIdentifier) {
        return KnownOIDs.findMatch(objectIdentifier.toString()).stdName();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append("[");
        if (this.index == -1) {
            stringBuffer.append(this.oid.toString());
        } else {
            stringBuffer.append(getName(this.oid));
        }
        stringBuffer.append(": ");
        if (this.index == -1 || SINGLE_VALUED[this.index]) {
            if (this.value instanceof byte[]) {
                stringBuffer.append(new HexDumpEncoder().encodeBuffer((byte[]) this.value));
            } else {
                stringBuffer.append(this.value.toString());
            }
            stringBuffer.append("]");
            return stringBuffer.toString();
        }
        boolean z2 = true;
        for (Object obj : (Object[]) this.value) {
            if (z2) {
                z2 = false;
            } else {
                stringBuffer.append(", ");
            }
            stringBuffer.append(obj.toString());
        }
        return stringBuffer.toString();
    }

    static int indexOf(Object obj, Object[] objArr, int i2) {
        for (int i3 = i2; i3 < objArr.length; i3++) {
            if (obj.equals(objArr[i3])) {
                return i3;
            }
        }
        return -1;
    }

    private void throwSingleValuedException() throws IOException {
        throw new IOException("Single-value attribute " + ((Object) this.oid) + " (" + getName() + ") has multiple values.");
    }

    private void throwTagException(Byte b2) throws IOException {
        Byte[] bArr = PKCS9_VALUE_TAGS[this.index];
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append("Value of attribute ");
        stringBuffer.append(this.oid.toString());
        stringBuffer.append(" (");
        stringBuffer.append(getName());
        stringBuffer.append(") has wrong tag: ");
        stringBuffer.append(b2.toString());
        stringBuffer.append(".  Expected tags: ");
        stringBuffer.append(bArr[0].toString());
        for (int i2 = 1; i2 < bArr.length; i2++) {
            stringBuffer.append(", ");
            stringBuffer.append(bArr[i2].toString());
        }
        stringBuffer.append(".");
        throw new IOException(stringBuffer.toString());
    }
}
