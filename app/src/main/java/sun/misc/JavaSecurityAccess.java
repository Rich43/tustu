package sun.misc;

import java.security.AccessControlContext;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/misc/JavaSecurityAccess.class */
public interface JavaSecurityAccess {
    <T> T doIntersectionPrivilege(PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext, AccessControlContext accessControlContext2);

    <T> T doIntersectionPrivilege(PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext);
}
