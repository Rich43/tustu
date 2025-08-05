package com.sun.security.auth;

import java.security.CodeSource;
import java.security.PermissionCollection;
import javax.security.auth.Policy;
import javax.security.auth.Subject;
import jdk.Exported;
import sun.security.provider.AuthPolicyFile;

@Exported(false)
@Deprecated
/* loaded from: rt.jar:com/sun/security/auth/PolicyFile.class */
public class PolicyFile extends Policy {
    private final AuthPolicyFile apf = new AuthPolicyFile();

    @Override // javax.security.auth.Policy
    public void refresh() {
        this.apf.refresh();
    }

    @Override // javax.security.auth.Policy
    public PermissionCollection getPermissions(Subject subject, CodeSource codeSource) {
        return this.apf.getPermissions(subject, codeSource);
    }
}
