package com.sun.jndi.ldap;

import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.HasControls;

/* loaded from: rt.jar:com/sun/jndi/ldap/NameClassPairWithControls.class */
class NameClassPairWithControls extends NameClassPair implements HasControls {
    private Control[] controls;
    private static final long serialVersionUID = 2010738921219112944L;

    public NameClassPairWithControls(String str, String str2, Control[] controlArr) {
        super(str, str2);
        this.controls = controlArr;
    }

    @Override // javax.naming.ldap.HasControls
    public Control[] getControls() throws NamingException {
        return this.controls;
    }
}
