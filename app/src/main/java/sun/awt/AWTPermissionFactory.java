package sun.awt;

import java.awt.AWTPermission;
import sun.security.util.PermissionFactory;

/* loaded from: rt.jar:sun/awt/AWTPermissionFactory.class */
public class AWTPermissionFactory implements PermissionFactory<AWTPermission> {
    @Override // sun.security.util.PermissionFactory
    public AWTPermission newPermission(String str) {
        return new AWTPermission(str);
    }
}
