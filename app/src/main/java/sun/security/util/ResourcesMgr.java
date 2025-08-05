package sun.security.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ResourceBundle;

/* loaded from: rt.jar:sun/security/util/ResourcesMgr.class */
public class ResourcesMgr {
    private static ResourceBundle bundle;
    private static ResourceBundle altBundle;

    public static String getString(String str) {
        if (bundle == null) {
            bundle = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: sun.security.util.ResourcesMgr.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ResourceBundle run() {
                    return ResourceBundle.getBundle("sun.security.util.Resources");
                }
            });
        }
        return bundle.getString(str);
    }

    public static String getString(String str, final String str2) {
        if (altBundle == null) {
            altBundle = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: sun.security.util.ResourcesMgr.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ResourceBundle run() {
                    return ResourceBundle.getBundle(str2);
                }
            });
        }
        return altBundle.getString(str);
    }
}
