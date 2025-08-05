package sun.security.provider;

import java.net.MalformedURLException;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.PolicySpi;
import java.security.ProtectionDomain;
import java.security.URIParameter;

/* loaded from: rt.jar:sun/security/provider/PolicySpiFile.class */
public final class PolicySpiFile extends PolicySpi {
    private PolicyFile pf;

    public PolicySpiFile(Policy.Parameters parameters) {
        if (parameters == null) {
            this.pf = new PolicyFile();
        } else {
            if (!(parameters instanceof URIParameter)) {
                throw new IllegalArgumentException("Unrecognized policy parameter: " + ((Object) parameters));
            }
            try {
                this.pf = new PolicyFile(((URIParameter) parameters).getURI().toURL());
            } catch (MalformedURLException e2) {
                throw new IllegalArgumentException("Invalid URIParameter", e2);
            }
        }
    }

    @Override // java.security.PolicySpi
    protected PermissionCollection engineGetPermissions(CodeSource codeSource) {
        return this.pf.getPermissions(codeSource);
    }

    @Override // java.security.PolicySpi
    protected PermissionCollection engineGetPermissions(ProtectionDomain protectionDomain) {
        return this.pf.getPermissions(protectionDomain);
    }

    @Override // java.security.PolicySpi
    protected boolean engineImplies(ProtectionDomain protectionDomain, Permission permission) {
        return this.pf.implies(protectionDomain, permission);
    }

    @Override // java.security.PolicySpi
    protected void engineRefresh() {
        this.pf.refresh();
    }
}
