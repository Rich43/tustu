package com.sun.security.auth;

import java.io.Serializable;
import java.security.Principal;
import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/NTSid.class */
public class NTSid implements Principal, Serializable {
    private static final long serialVersionUID = 4412290580770249885L;
    private String sid;

    public NTSid(String str) {
        if (str == null) {
            throw new NullPointerException(new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources")).format(new Object[]{"stringSid"}));
        }
        if (str.length() == 0) {
            throw new IllegalArgumentException(ResourcesMgr.getString("Invalid.NTSid.value", "sun.security.util.AuthResources"));
        }
        this.sid = new String(str);
    }

    @Override // java.security.Principal
    public String getName() {
        return this.sid;
    }

    @Override // java.security.Principal
    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("NTSid.name", "sun.security.util.AuthResources")).format(new Object[]{this.sid});
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if ((obj instanceof NTSid) && this.sid.equals(((NTSid) obj).sid)) {
            return true;
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.sid.hashCode();
    }
}
