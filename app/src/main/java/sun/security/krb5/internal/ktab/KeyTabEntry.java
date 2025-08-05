package sun.security.krb5.internal.ktab;

import java.io.UnsupportedEncodingException;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;

/* loaded from: rt.jar:sun/security/krb5/internal/ktab/KeyTabEntry.class */
public class KeyTabEntry implements KeyTabConstants {
    PrincipalName service;
    Realm realm;
    KerberosTime timestamp;
    int keyVersion;
    int keyType;
    byte[] keyblock;
    boolean DEBUG = Krb5.DEBUG;

    public KeyTabEntry(PrincipalName principalName, Realm realm, KerberosTime kerberosTime, int i2, int i3, byte[] bArr) {
        this.keyblock = null;
        this.service = principalName;
        this.realm = realm;
        this.timestamp = kerberosTime;
        this.keyVersion = i2;
        this.keyType = i3;
        if (bArr != null) {
            this.keyblock = (byte[]) bArr.clone();
        }
    }

    public PrincipalName getService() {
        return this.service;
    }

    public EncryptionKey getKey() {
        return new EncryptionKey(this.keyblock, this.keyType, new Integer(this.keyVersion));
    }

    public String getKeyString() {
        StringBuffer stringBuffer = new StringBuffer("0x");
        for (int i2 = 0; i2 < this.keyblock.length; i2++) {
            stringBuffer.append(String.format("%02x", Integer.valueOf(this.keyblock[i2] & 255)));
        }
        return stringBuffer.toString();
    }

    public int entryLength() {
        int length = 0;
        for (String str : this.service.getNameStrings()) {
            try {
                length += 2 + str.getBytes("8859_1").length;
            } catch (UnsupportedEncodingException e2) {
            }
        }
        int length2 = 0;
        try {
            length2 = this.realm.toString().getBytes("8859_1").length;
        } catch (UnsupportedEncodingException e3) {
        }
        int length3 = 4 + length2 + length + 4 + 4 + 1 + 2 + 2 + this.keyblock.length;
        if (this.DEBUG) {
            System.out.println(">>> KeyTabEntry: key tab entry size is " + length3);
        }
        return length3;
    }

    public KerberosTime getTimeStamp() {
        return this.timestamp;
    }
}
