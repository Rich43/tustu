package com.sun.jndi.ldap;

import javax.naming.ldap.Control;

/* loaded from: rt.jar:com/sun/jndi/ldap/BasicControl.class */
public class BasicControl implements Control {
    protected String id;
    protected boolean criticality;
    protected byte[] value;
    private static final long serialVersionUID = -5914033725246428413L;

    public BasicControl(String str) {
        this.criticality = false;
        this.value = null;
        this.id = str;
    }

    public BasicControl(String str, boolean z2, byte[] bArr) {
        this.criticality = false;
        this.value = null;
        this.id = str;
        this.criticality = z2;
        if (bArr != null) {
            this.value = (byte[]) bArr.clone();
        }
    }

    @Override // javax.naming.ldap.Control
    public String getID() {
        return this.id;
    }

    @Override // javax.naming.ldap.Control
    public boolean isCritical() {
        return this.criticality;
    }

    @Override // javax.naming.ldap.Control
    public byte[] getEncodedValue() {
        if (this.value == null) {
            return null;
        }
        return (byte[]) this.value.clone();
    }
}
