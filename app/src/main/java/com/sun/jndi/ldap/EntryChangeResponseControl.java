package com.sun.jndi.ldap;

import java.io.IOException;

/* loaded from: rt.jar:com/sun/jndi/ldap/EntryChangeResponseControl.class */
public final class EntryChangeResponseControl extends BasicControl {
    public static final String OID = "2.16.840.1.113730.3.4.7";
    public static final int ADD = 1;
    public static final int DELETE = 2;
    public static final int MODIFY = 4;
    public static final int RENAME = 8;
    private int changeType;
    private String previousDN;
    private long changeNumber;
    private static final long serialVersionUID = -2087354136750180511L;

    public EntryChangeResponseControl(String str, boolean z2, byte[] bArr) throws IOException {
        super(str, z2, bArr);
        this.previousDN = null;
        this.changeNumber = -1L;
        if (bArr != null && bArr.length > 0) {
            BerDecoder berDecoder = new BerDecoder(bArr, 0, bArr.length);
            berDecoder.parseSeq(null);
            this.changeType = berDecoder.parseEnumeration();
            if (berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 4) {
                this.previousDN = berDecoder.parseString(true);
            }
            if (berDecoder.bytesLeft() > 0 && berDecoder.peekByte() == 2) {
                this.changeNumber = berDecoder.parseInt();
            }
        }
    }

    public int getChangeType() {
        return this.changeType;
    }

    public String getPreviousDN() {
        return this.previousDN;
    }

    public long getChangeNumber() {
        return this.changeNumber;
    }
}
