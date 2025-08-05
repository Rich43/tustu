package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.ccache.CCacheOutputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/AuthorizationDataEntry.class */
public class AuthorizationDataEntry implements Cloneable {
    public int adType;
    public byte[] adData;

    private AuthorizationDataEntry() {
    }

    public AuthorizationDataEntry(int i2, byte[] bArr) {
        this.adType = i2;
        this.adData = bArr;
    }

    public Object clone() {
        AuthorizationDataEntry authorizationDataEntry = new AuthorizationDataEntry();
        authorizationDataEntry.adType = this.adType;
        if (this.adData != null) {
            authorizationDataEntry.adData = new byte[this.adData.length];
            System.arraycopy(this.adData, 0, authorizationDataEntry.adData, 0, this.adData.length);
        }
        return authorizationDataEntry;
    }

    public AuthorizationDataEntry(DerValue derValue) throws Asn1Exception, IOException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.adType = derValue2.getData().getBigInteger().intValue();
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 1) {
                this.adData = derValue3.getData().getOctetString();
                if (derValue.getData().available() > 0) {
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
        derOutputStream2.putInteger(this.adType);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putOctetString(this.adData);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public void writeEntry(CCacheOutputStream cCacheOutputStream) throws IOException {
        cCacheOutputStream.write16(this.adType);
        cCacheOutputStream.write32(this.adData.length);
        cCacheOutputStream.write(this.adData, 0, this.adData.length);
    }

    public String toString() {
        return "adType=" + this.adType + " adData.length=" + this.adData.length;
    }
}
