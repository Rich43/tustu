package com.sun.security.jgss;

import jdk.Exported;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;

@Exported
/* loaded from: rt.jar:com/sun/security/jgss/ExtendedGSSContext.class */
public interface ExtendedGSSContext extends GSSContext {
    Object inquireSecContext(InquireType inquireType) throws GSSException;

    void requestDelegPolicy(boolean z2) throws GSSException;

    boolean getDelegPolicyState();
}
