package com.sun.jndi.ldap;

import java.util.Vector;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.Control;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapResult.class */
public final class LdapResult {
    int msgId;
    public int status;
    String matchedDN;
    String errorMessage;
    Vector<Vector<String>> referrals = null;
    LdapReferralException refEx = null;
    Vector<LdapEntry> entries = null;
    Vector<Control> resControls = null;
    public byte[] serverCreds = null;
    String extensionId = null;
    byte[] extensionValue = null;

    boolean compareToSearchResult(String str) {
        boolean z2;
        switch (this.status) {
            case 5:
                this.status = 0;
                this.entries = new Vector<>(0);
                z2 = true;
                break;
            case 6:
                this.status = 0;
                this.entries = new Vector<>(1, 1);
                this.entries.addElement(new LdapEntry(str, new BasicAttributes(true)));
                z2 = true;
                break;
            default:
                z2 = false;
                break;
        }
        return z2;
    }
}
