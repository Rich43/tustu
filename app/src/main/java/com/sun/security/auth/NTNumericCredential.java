package com.sun.security.auth;

import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/NTNumericCredential.class */
public class NTNumericCredential {
    private long impersonationToken;

    public NTNumericCredential(long j2) {
        this.impersonationToken = j2;
    }

    public long getToken() {
        return this.impersonationToken;
    }

    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("NTNumericCredential.name", "sun.security.util.AuthResources")).format(new Object[]{Long.toString(this.impersonationToken)});
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if ((obj instanceof NTNumericCredential) && this.impersonationToken == ((NTNumericCredential) obj).getToken()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (int) this.impersonationToken;
    }
}
