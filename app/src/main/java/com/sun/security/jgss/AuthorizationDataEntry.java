package com.sun.security.jgss;

import jdk.Exported;
import sun.misc.HexDumpEncoder;

@Exported
/* loaded from: rt.jar:com/sun/security/jgss/AuthorizationDataEntry.class */
public final class AuthorizationDataEntry {
    private final int type;
    private final byte[] data;

    public AuthorizationDataEntry(int i2, byte[] bArr) {
        this.type = i2;
        this.data = (byte[]) bArr.clone();
    }

    public int getType() {
        return this.type;
    }

    public byte[] getData() {
        return (byte[]) this.data.clone();
    }

    public String toString() {
        return "AuthorizationDataEntry: type=" + this.type + ", data=" + this.data.length + " bytes:\n" + new HexDumpEncoder().encodeBuffer(this.data);
    }
}
