package com.sun.security.auth;

import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/NTSidUserPrincipal.class */
public class NTSidUserPrincipal extends NTSid {
    private static final long serialVersionUID = -5573239889517749525L;

    public NTSidUserPrincipal(String str) {
        super(str);
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("NTSidUserPrincipal.name", "sun.security.util.AuthResources")).format(new Object[]{getName()});
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NTSidUserPrincipal)) {
            return false;
        }
        return super.equals(obj);
    }
}
