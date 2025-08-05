package sun.security.krb5.internal.ktab;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import sun.security.krb5.internal.util.KrbDataOutputStream;

/* loaded from: rt.jar:sun/security/krb5/internal/ktab/KeyTabOutputStream.class */
public class KeyTabOutputStream extends KrbDataOutputStream implements KeyTabConstants {
    private KeyTabEntry entry;
    private int keyType;
    private byte[] keyValue;
    public int version;

    public KeyTabOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void writeVersion(int i2) throws IOException {
        this.version = i2;
        write16(i2);
    }

    public void writeEntry(KeyTabEntry keyTabEntry) throws IOException {
        write32(keyTabEntry.entryLength());
        String[] nameStrings = keyTabEntry.service.getNameStrings();
        int length = nameStrings.length;
        if (this.version == 1281) {
            write16(length + 1);
        } else {
            write16(length);
        }
        byte[] bytes = null;
        try {
            bytes = keyTabEntry.service.getRealmString().getBytes("8859_1");
        } catch (UnsupportedEncodingException e2) {
        }
        write16(bytes.length);
        write(bytes);
        for (int i2 = 0; i2 < length; i2++) {
            try {
                write16(nameStrings[i2].getBytes("8859_1").length);
                write(nameStrings[i2].getBytes("8859_1"));
            } catch (UnsupportedEncodingException e3) {
            }
        }
        write32(keyTabEntry.service.getNameType());
        write32((int) (keyTabEntry.timestamp.getTime() / 1000));
        write8(keyTabEntry.keyVersion % 256);
        write16(keyTabEntry.keyType);
        write16(keyTabEntry.keyblock.length);
        write(keyTabEntry.keyblock);
    }
}
