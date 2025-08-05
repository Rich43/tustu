package com.sun.security.auth;

import java.io.Serializable;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.ResourceBundle;
import jdk.Exported;

@Exported(false)
@Deprecated
/* loaded from: rt.jar:com/sun/security/auth/SolarisPrincipal.class */
public class SolarisPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -7840670002439379038L;
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.security.auth.SolarisPrincipal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private String name;

    public SolarisPrincipal(String str) {
        if (str == null) {
            throw new NullPointerException(rb.getString("provided.null.name"));
        }
        this.name = str;
    }

    @Override // java.security.Principal
    public String getName() {
        return this.name;
    }

    @Override // java.security.Principal
    public String toString() {
        return rb.getString("SolarisPrincipal.") + this.name;
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if ((obj instanceof SolarisPrincipal) && getName().equals(((SolarisPrincipal) obj).getName())) {
            return true;
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.name.hashCode();
    }
}
