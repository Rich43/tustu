package com.sun.jndi.ldap;

import javax.naming.Binding;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.HasControls;

/* loaded from: rt.jar:com/sun/jndi/ldap/BindingWithControls.class */
class BindingWithControls extends Binding implements HasControls {
    private Control[] controls;
    private static final long serialVersionUID = 9117274533692320040L;

    public BindingWithControls(String str, Object obj, Control[] controlArr) {
        super(str, obj);
        this.controls = controlArr;
    }

    @Override // javax.naming.ldap.HasControls
    public Control[] getControls() throws NamingException {
        return this.controls;
    }
}
