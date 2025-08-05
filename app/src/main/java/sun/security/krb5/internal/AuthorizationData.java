package sun.security.krb5.internal;

import java.io.IOException;
import java.util.Vector;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.ccache.CCacheOutputStream;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/AuthorizationData.class */
public class AuthorizationData implements Cloneable {
    private AuthorizationDataEntry[] entry;

    private AuthorizationData() {
        this.entry = null;
    }

    public AuthorizationData(AuthorizationDataEntry[] authorizationDataEntryArr) throws IOException {
        this.entry = null;
        if (authorizationDataEntryArr != null) {
            this.entry = new AuthorizationDataEntry[authorizationDataEntryArr.length];
            for (int i2 = 0; i2 < authorizationDataEntryArr.length; i2++) {
                if (authorizationDataEntryArr[i2] == null) {
                    throw new IOException("Cannot create an AuthorizationData");
                }
                this.entry[i2] = (AuthorizationDataEntry) authorizationDataEntryArr[i2].clone();
            }
        }
    }

    public AuthorizationData(AuthorizationDataEntry authorizationDataEntry) {
        this.entry = null;
        this.entry = new AuthorizationDataEntry[1];
        this.entry[0] = authorizationDataEntry;
    }

    public Object clone() {
        AuthorizationData authorizationData = new AuthorizationData();
        if (this.entry != null) {
            authorizationData.entry = new AuthorizationDataEntry[this.entry.length];
            for (int i2 = 0; i2 < this.entry.length; i2++) {
                authorizationData.entry[i2] = (AuthorizationDataEntry) this.entry[i2].clone();
            }
        }
        return authorizationData;
    }

    public AuthorizationData(DerValue derValue) throws Asn1Exception, IOException {
        this.entry = null;
        Vector vector = new Vector();
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        while (derValue.getData().available() > 0) {
            vector.addElement(new AuthorizationDataEntry(derValue.getData().getDerValue()));
        }
        if (vector.size() > 0) {
            this.entry = new AuthorizationDataEntry[vector.size()];
            vector.copyInto(this.entry);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerValue[] derValueArr = new DerValue[this.entry.length];
        for (int i2 = 0; i2 < this.entry.length; i2++) {
            derValueArr[i2] = new DerValue(this.entry[i2].asn1Encode());
        }
        derOutputStream.putSequence(derValueArr);
        return derOutputStream.toByteArray();
    }

    public static AuthorizationData parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new AuthorizationData(derValue.getData().getDerValue());
    }

    public void writeAuth(CCacheOutputStream cCacheOutputStream) throws IOException {
        for (int i2 = 0; i2 < this.entry.length; i2++) {
            this.entry[i2].writeEntry(cCacheOutputStream);
        }
    }

    public String toString() {
        String str = "AuthorizationData:\n";
        for (int i2 = 0; i2 < this.entry.length; i2++) {
            str = str + this.entry[i2].toString();
        }
        return str;
    }

    public int count() {
        return this.entry.length;
    }

    public AuthorizationDataEntry item(int i2) {
        return (AuthorizationDataEntry) this.entry[i2].clone();
    }
}
