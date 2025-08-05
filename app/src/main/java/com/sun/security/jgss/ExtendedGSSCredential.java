package com.sun.security.jgss;

import jdk.Exported;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;

@Exported
/* loaded from: rt.jar:com/sun/security/jgss/ExtendedGSSCredential.class */
public interface ExtendedGSSCredential extends GSSCredential {
    GSSCredential impersonate(GSSName gSSName) throws GSSException;
}
