package com.sun.corba.se.impl.orbutil;

import java.security.PrivilegedAction;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/GetPropertyAction.class */
public class GetPropertyAction implements PrivilegedAction {
    private String theProp;
    private String defaultVal;

    public GetPropertyAction(String str) {
        this.theProp = str;
    }

    public GetPropertyAction(String str, String str2) {
        this.theProp = str;
        this.defaultVal = str2;
    }

    @Override // java.security.PrivilegedAction
    public Object run() {
        String property = System.getProperty(this.theProp);
        return property == null ? this.defaultVal : property;
    }
}
