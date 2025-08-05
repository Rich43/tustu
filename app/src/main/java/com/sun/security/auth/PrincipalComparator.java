package com.sun.security.auth;

import javax.security.auth.Subject;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/PrincipalComparator.class */
public interface PrincipalComparator {
    boolean implies(Subject subject);
}
