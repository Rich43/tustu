package com.sun.security.jgss;

import javax.security.auth.Subject;
import jdk.Exported;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSName;

@Exported
/* loaded from: rt.jar:com/sun/security/jgss/GSSUtil.class */
public class GSSUtil {
    public static Subject createSubject(GSSName gSSName, GSSCredential gSSCredential) {
        return sun.security.jgss.GSSUtil.getSubject(gSSName, gSSCredential);
    }
}
