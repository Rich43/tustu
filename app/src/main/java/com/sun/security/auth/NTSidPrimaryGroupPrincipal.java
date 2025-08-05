package com.sun.security.auth;

import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/NTSidPrimaryGroupPrincipal.class */
public class NTSidPrimaryGroupPrincipal extends NTSid {
    private static final long serialVersionUID = 8011978367305190527L;

    public NTSidPrimaryGroupPrincipal(String str) {
        super(str);
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("NTSidPrimaryGroupPrincipal.name", "sun.security.util.AuthResources")).format(new Object[]{getName()});
    }

    @Override // com.sun.security.auth.NTSid, java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NTSidPrimaryGroupPrincipal)) {
            return false;
        }
        return super.equals(obj);
    }
}
