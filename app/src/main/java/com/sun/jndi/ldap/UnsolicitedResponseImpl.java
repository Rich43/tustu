package com.sun.jndi.ldap;

import java.util.Vector;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.UnsolicitedNotification;

/* loaded from: rt.jar:com/sun/jndi/ldap/UnsolicitedResponseImpl.class */
final class UnsolicitedResponseImpl implements UnsolicitedNotification {
    private String oid;
    private String[] referrals;
    private byte[] extensionValue;
    private NamingException exception;
    private Control[] controls;
    private static final long serialVersionUID = 5913778898401784775L;

    UnsolicitedResponseImpl(String str, byte[] bArr, Vector<Vector<String>> vector, int i2, String str2, String str3, Control[] controlArr) {
        this.oid = str;
        this.extensionValue = bArr;
        if (vector != null && vector.size() > 0) {
            int size = vector.size();
            this.referrals = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                this.referrals[i3] = vector.elementAt(i3).elementAt(0);
            }
        }
        this.exception = LdapCtx.mapErrorCode(i2, str2);
        this.controls = controlArr;
    }

    @Override // javax.naming.ldap.ExtendedResponse
    public String getID() {
        return this.oid;
    }

    @Override // javax.naming.ldap.ExtendedResponse
    public byte[] getEncodedValue() {
        return this.extensionValue;
    }

    @Override // javax.naming.ldap.UnsolicitedNotification
    public String[] getReferrals() {
        return this.referrals;
    }

    @Override // javax.naming.ldap.UnsolicitedNotification
    public NamingException getException() {
        return this.exception;
    }

    @Override // javax.naming.ldap.HasControls
    public Control[] getControls() throws NamingException {
        return this.controls;
    }
}
