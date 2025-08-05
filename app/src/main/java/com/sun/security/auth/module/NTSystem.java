package com.sun.security.auth.module;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/module/NTSystem.class */
public class NTSystem {
    private String userName;
    private String domain;
    private String domainSID;
    private String userSID;
    private String[] groupIDs;
    private String primaryGroupID;
    private long impersonationToken;

    private native void getCurrent(boolean z2);

    private native long getImpersonationToken0();

    public NTSystem() {
        this(false);
    }

    NTSystem(boolean z2) {
        loadNative();
        getCurrent(z2);
    }

    public String getName() {
        return this.userName;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getDomainSID() {
        return this.domainSID;
    }

    public String getUserSID() {
        return this.userSID;
    }

    public String getPrimaryGroupID() {
        return this.primaryGroupID;
    }

    public String[] getGroupIDs() {
        if (this.groupIDs == null) {
            return null;
        }
        return (String[]) this.groupIDs.clone();
    }

    public synchronized long getImpersonationToken() {
        if (this.impersonationToken == 0) {
            this.impersonationToken = getImpersonationToken0();
        }
        return this.impersonationToken;
    }

    private void loadNative() {
        System.loadLibrary("jaas_nt");
    }
}
