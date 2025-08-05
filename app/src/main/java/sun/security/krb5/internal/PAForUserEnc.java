package sun.security.krb5.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.Checksum;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.util.KerberosString;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/PAForUserEnc.class */
public class PAForUserEnc {
    public final PrincipalName name;
    private final EncryptionKey key;
    public static final String AUTH_PACKAGE = "Kerberos";

    public PAForUserEnc(PrincipalName principalName, EncryptionKey encryptionKey) {
        this.name = principalName;
        this.key = encryptionKey;
    }

    public PAForUserEnc(DerValue derValue, EncryptionKey encryptionKey) throws IOException, KrbException {
        this.key = encryptionKey;
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        PrincipalName principalName = null;
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            try {
                principalName = new PrincipalName(derValue2.getData().getDerValue(), new Realm("PLACEHOLDER"));
            } catch (RealmException e2) {
            }
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 1) {
                try {
                    this.name = new PrincipalName(principalName.getNameType(), principalName.getNameStrings(), new Realm(derValue3.getData().getDerValue()));
                    if ((derValue.getData().getDerValue().getTag() & 31) != 2) {
                        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                    }
                    DerValue derValue4 = derValue.getData().getDerValue();
                    if ((derValue4.getTag() & 31) == 3) {
                        if (!new KerberosString(derValue4.getData().getDerValue()).toString().equalsIgnoreCase(AUTH_PACKAGE)) {
                            throw new IOException("Incorrect auth-package");
                        }
                        if (derValue.getData().available() > 0) {
                            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                        }
                        return;
                    }
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                } catch (RealmException e3) {
                    throw new IOException(e3);
                }
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.name.asn1Encode());
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), this.name.getRealm().asn1Encode());
        try {
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), new Checksum(Checksum.CKSUMTYPE_HMAC_MD5_ARCFOUR, getS4UByteArray(), this.key, 17).asn1Encode());
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.putDerValue(new KerberosString(AUTH_PACKAGE).toDerValue());
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream2);
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.write((byte) 48, derOutputStream);
            return derOutputStream3.toByteArray();
        } catch (KrbException e2) {
            throw new IOException(e2);
        }
    }

    public byte[] getS4UByteArray() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(new byte[4]);
            for (String str : this.name.getNameStrings()) {
                byteArrayOutputStream.write(str.getBytes("UTF-8"));
            }
            byteArrayOutputStream.write(this.name.getRealm().toString().getBytes("UTF-8"));
            byteArrayOutputStream.write(AUTH_PACKAGE.getBytes("UTF-8"));
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            int nameType = this.name.getNameType();
            byteArray[0] = (byte) (nameType & 255);
            byteArray[1] = (byte) ((nameType >> 8) & 255);
            byteArray[2] = (byte) ((nameType >> 16) & 255);
            byteArray[3] = (byte) ((nameType >> 24) & 255);
            return byteArray;
        } catch (IOException e2) {
            throw new AssertionError("Cannot write ByteArrayOutputStream", e2);
        }
    }

    public PrincipalName getName() {
        return this.name;
    }

    public String toString() {
        return "PA-FOR-USER: " + ((Object) this.name);
    }
}
