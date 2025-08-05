package com.sun.jndi.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.HasControls;

/* loaded from: rt.jar:com/sun/jndi/ldap/SearchResultWithControls.class */
class SearchResultWithControls extends SearchResult implements HasControls {
    private Control[] controls;
    private static final long serialVersionUID = 8476983938747908202L;

    public SearchResultWithControls(String str, Object obj, Attributes attributes, boolean z2, Control[] controlArr) {
        super(str, obj, attributes, z2);
        this.controls = controlArr;
    }

    @Override // javax.naming.ldap.HasControls
    public Control[] getControls() throws NamingException {
        return this.controls;
    }
}
