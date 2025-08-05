package sun.security.krb5.internal.ktab;

import java.io.IOException;
import java.io.InputStream;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.Realm;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.util.KrbDataInputStream;

/* loaded from: rt.jar:sun/security/krb5/internal/ktab/KeyTabInputStream.class */
public class KeyTabInputStream extends KrbDataInputStream implements KeyTabConstants {
    boolean DEBUG;
    int index;

    public KeyTabInputStream(InputStream inputStream) {
        super(inputStream);
        this.DEBUG = Krb5.DEBUG;
    }

    int readEntryLength() throws IOException {
        return read(4);
    }

    KeyTabEntry readEntry(int i2, int i3) throws IOException, RealmException {
        this.index = i2;
        if (this.index == 0) {
            return null;
        }
        if (this.index < 0) {
            skip(Math.abs(this.index));
            return null;
        }
        int i4 = read(2);
        this.index -= 2;
        if (i3 == 1281) {
            i4--;
        }
        Realm realm = new Realm(readName());
        String[] strArr = new String[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            strArr[i5] = readName();
        }
        int i6 = read(4);
        this.index -= 4;
        PrincipalName principalName = new PrincipalName(i6, strArr, realm);
        KerberosTime timeStamp = readTimeStamp();
        int i7 = read() & 255;
        this.index--;
        int i8 = read(2);
        this.index -= 2;
        int i9 = read(2);
        this.index -= 2;
        byte[] key = readKey(i9);
        this.index -= i9;
        if (this.index >= 4) {
            int i10 = read(4);
            if (i10 != 0) {
                i7 = i10;
            }
            this.index -= 4;
        }
        if (this.index < 0) {
            throw new RealmException("Keytab is corrupted");
        }
        skip(this.index);
        return new KeyTabEntry(principalName, realm, timeStamp, i7, i8, key);
    }

    byte[] readKey(int i2) throws IOException {
        byte[] bArr = new byte[i2];
        read(bArr, 0, i2);
        return bArr;
    }

    KerberosTime readTimeStamp() throws IOException {
        this.index -= 4;
        return new KerberosTime(read(4) * 1000);
    }

    String readName() throws IOException {
        int i2 = read(2);
        this.index -= 2;
        byte[] bArr = new byte[i2];
        read(bArr, 0, i2);
        this.index -= i2;
        String str = new String(bArr);
        if (this.DEBUG) {
            System.out.println(">>> KeyTabInputStream, readName(): " + str);
        }
        return str;
    }
}
