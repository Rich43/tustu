package sun.security.krb5.internal;

import java.io.IOException;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.util.KerberosString;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/ETypeInfo.class */
public class ETypeInfo {
    private int etype;
    private String salt;
    private static final byte TAG_TYPE = 0;
    private static final byte TAG_VALUE = 1;

    private ETypeInfo() {
        this.salt = null;
    }

    public ETypeInfo(int i2, String str) {
        this.salt = null;
        this.etype = i2;
        this.salt = str;
    }

    public Object clone() {
        return new ETypeInfo(this.etype, this.salt);
    }

    public ETypeInfo(DerValue derValue) throws Asn1Exception, IOException {
        this.salt = null;
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.etype = derValue2.getData().getBigInteger().intValue();
            if (derValue.getData().available() > 0) {
                DerValue derValue3 = derValue.getData().getDerValue();
                if ((derValue3.getTag() & 31) == 1) {
                    byte[] octetString = derValue3.getData().getOctetString();
                    if (KerberosString.MSNAME) {
                        this.salt = new String(octetString, InternalZipConstants.CHARSET_UTF8);
                    } else {
                        this.salt = new String(octetString);
                    }
                }
            }
            if (derValue.getData().available() > 0) {
                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
            }
            return;
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.etype);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        if (this.salt != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            if (KerberosString.MSNAME) {
                derOutputStream3.putOctetString(this.salt.getBytes(InternalZipConstants.CHARSET_UTF8));
            } else {
                derOutputStream3.putOctetString(this.salt.getBytes());
            }
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        }
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public int getEType() {
        return this.etype;
    }

    public String getSalt() {
        return this.salt;
    }
}
