package com.sun.jndi.ldap;

import java.io.IOException;

/* loaded from: rt.jar:com/sun/jndi/ldap/PersistentSearchControl.class */
public final class PersistentSearchControl extends BasicControl {
    public static final String OID = "2.16.840.1.113730.3.4.3";
    public static final int ADD = 1;
    public static final int DELETE = 2;
    public static final int MODIFY = 4;
    public static final int RENAME = 8;
    public static final int ANY = 15;
    private int changeTypes;
    private boolean changesOnly;
    private boolean returnControls;
    private static final long serialVersionUID = 6335140491154854116L;

    public PersistentSearchControl() throws IOException {
        super(OID);
        this.changeTypes = 15;
        this.changesOnly = false;
        this.returnControls = true;
        this.value = setEncodedValue();
    }

    public PersistentSearchControl(int i2, boolean z2, boolean z3, boolean z4) throws IOException {
        super(OID, z4, null);
        this.changeTypes = 15;
        this.changesOnly = false;
        this.returnControls = true;
        this.changeTypes = i2;
        this.changesOnly = z2;
        this.returnControls = z3;
        this.value = setEncodedValue();
    }

    private byte[] setEncodedValue() throws IOException {
        BerEncoder berEncoder = new BerEncoder(32);
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(this.changeTypes);
        berEncoder.encodeBoolean(this.changesOnly);
        berEncoder.encodeBoolean(this.returnControls);
        berEncoder.endSeq();
        return berEncoder.getTrimmedBuf();
    }
}
