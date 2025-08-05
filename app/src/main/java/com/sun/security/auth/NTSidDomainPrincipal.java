package com.sun.security.auth;

import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/NTSidDomainPrincipal.class */
public class NTSidDomainPrincipal extends NTSid {
    private static final long serialVersionUID = 5247810785821650912L;

    public NTSidDomainPrincipal(String str) {
        super(str);
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("NTSidDomainPrincipal.name", "sun.security.util.AuthResources")).format(new Object[]{getName()});
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NTSidDomainPrincipal)) {
            return false;
        }
        return super.equals(obj);
    }
}
