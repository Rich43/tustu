package com.sun.security.auth;

import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/NTSidGroupPrincipal.class */
public class NTSidGroupPrincipal extends NTSid {
    private static final long serialVersionUID = -1373347438636198229L;

    public NTSidGroupPrincipal(String str) {
        super(str);
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("NTSidGroupPrincipal.name", "sun.security.util.AuthResources")).format(new Object[]{getName()});
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NTSidGroupPrincipal)) {
            return false;
        }
        return super.equals(obj);
    }
}
