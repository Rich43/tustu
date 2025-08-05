package com.sun.jmx.mbeanserver;

import java.security.PrivilegedAction;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/GetPropertyAction.class */
public class GetPropertyAction implements PrivilegedAction<String> {
    private final String key;

    public GetPropertyAction(String str) {
        this.key = str;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public String run() {
        return System.getProperty(this.key);
    }
}
