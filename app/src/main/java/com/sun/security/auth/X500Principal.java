package com.sun.security.auth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.ResourceBundle;
import jdk.Exported;
import sun.security.x509.X500Name;

@Exported(false)
@Deprecated
/* loaded from: rt.jar:com/sun/security/auth/X500Principal.class */
public class X500Principal implements Principal, Serializable {
    private static final long serialVersionUID = -8222422609431628648L;
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.security.auth.X500Principal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private String name;
    private transient X500Name thisX500Name;

    public X500Principal(String str) {
        if (str == null) {
            throw new NullPointerException(rb.getString("provided.null.name"));
        }
        try {
            this.thisX500Name = new X500Name(str);
            this.name = str;
        } catch (Exception e2) {
            throw new IllegalArgumentException(e2.toString());
        }
    }

    @Override // java.security.Principal
    public String getName() {
        return this.thisX500Name.getName();
    }

    @Override // java.security.Principal
    public String toString() {
        return this.thisX500Name.toString();
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof X500Principal) {
            try {
                return this.thisX500Name.equals(new X500Name(((X500Principal) obj).getName()));
            } catch (Exception e2) {
                return false;
            }
        }
        if (obj instanceof Principal) {
            return obj.equals(this.thisX500Name);
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.thisX500Name.hashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.thisX500Name = new X500Name(this.name);
    }
}
