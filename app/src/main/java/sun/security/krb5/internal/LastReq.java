package sun.security.krb5.internal;

import java.io.IOException;
import java.util.Vector;
import sun.security.krb5.Asn1Exception;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/LastReq.class */
public class LastReq {
    private LastReqEntry[] entry;

    public LastReq(LastReqEntry[] lastReqEntryArr) throws IOException {
        this.entry = null;
        if (lastReqEntryArr != null) {
            this.entry = new LastReqEntry[lastReqEntryArr.length];
            for (int i2 = 0; i2 < lastReqEntryArr.length; i2++) {
                if (lastReqEntryArr[i2] == null) {
                    throw new IOException("Cannot create a LastReqEntry");
                }
                this.entry[i2] = (LastReqEntry) lastReqEntryArr[i2].clone();
            }
        }
    }

    public LastReq(DerValue derValue) throws Asn1Exception, IOException {
        this.entry = null;
        Vector vector = new Vector();
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        while (derValue.getData().available() > 0) {
            vector.addElement(new LastReqEntry(derValue.getData().getDerValue()));
        }
        if (vector.size() > 0) {
            this.entry = new LastReqEntry[vector.size()];
            vector.copyInto(this.entry);
        }
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.entry != null && this.entry.length > 0) {
            DerOutputStream derOutputStream2 = new DerOutputStream();
            for (int i2 = 0; i2 < this.entry.length; i2++) {
                derOutputStream2.write(this.entry[i2].asn1Encode());
            }
            derOutputStream.write((byte) 48, derOutputStream2);
            return derOutputStream.toByteArray();
        }
        return null;
    }

    public static LastReq parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new LastReq(derValue.getData().getDerValue());
    }
}
