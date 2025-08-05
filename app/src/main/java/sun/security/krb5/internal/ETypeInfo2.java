package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.util.KerberosString;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/ETypeInfo2.class */
public class ETypeInfo2 {
    private int etype;
    private String saltStr;
    private byte[] s2kparams;
    private static final byte TAG_TYPE = 0;
    private static final byte TAG_VALUE1 = 1;
    private static final byte TAG_VALUE2 = 2;

    private ETypeInfo2() {
        this.saltStr = null;
        this.s2kparams = null;
    }

    public ETypeInfo2(int i2, String str, byte[] bArr) {
        this.saltStr = null;
        this.s2kparams = null;
        this.etype = i2;
        this.saltStr = str;
        if (bArr != null) {
            this.s2kparams = (byte[]) bArr.clone();
        }
    }

    public Object clone() {
        ETypeInfo2 eTypeInfo2 = new ETypeInfo2();
        eTypeInfo2.etype = this.etype;
        eTypeInfo2.saltStr = this.saltStr;
        if (this.s2kparams != null) {
            eTypeInfo2.s2kparams = new byte[this.s2kparams.length];
            System.arraycopy(this.s2kparams, 0, eTypeInfo2.s2kparams, 0, this.s2kparams.length);
        }
        return eTypeInfo2;
    }

    public ETypeInfo2(DerValue derValue) throws Asn1Exception, IOException {
        this.saltStr = null;
        this.s2kparams = null;
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.etype = derValue2.getData().getBigInteger().intValue();
            if (derValue.getData().available() > 0 && (derValue.getData().peekByte() & 31) == 1) {
                this.saltStr = new KerberosString(derValue.getData().getDerValue().getData().getDerValue()).toString();
            }
            if (derValue.getData().available() > 0 && (derValue.getData().peekByte() & 31) == 2) {
                this.s2kparams = derValue.getData().getDerValue().getData().getOctetString();
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
        if (this.saltStr != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putDerValue(new KerberosString(this.saltStr).toDerValue());
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        }
        if (this.s2kparams != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putOctetString(this.s2kparams);
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream4);
        }
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write((byte) 48, derOutputStream);
        return derOutputStream5.toByteArray();
    }

    public int getEType() {
        return this.etype;
    }

    public String getSalt() {
        return this.saltStr;
    }

    public byte[] getParams() {
        if (this.s2kparams == null) {
            return null;
        }
        return (byte[]) this.s2kparams.clone();
    }
}
