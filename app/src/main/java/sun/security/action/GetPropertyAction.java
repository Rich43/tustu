package sun.security.action;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/security/action/GetPropertyAction.class */
public class GetPropertyAction implements PrivilegedAction<String> {
    private String theProp;
    private String defaultVal;

    public GetPropertyAction(String str) {
        this.theProp = str;
    }

    public GetPropertyAction(String str, String str2) {
        this.theProp = str;
        this.defaultVal = str2;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public String run() {
        String property = System.getProperty(this.theProp);
        return property == null ? this.defaultVal : property;
    }

    public static String privilegedGetProperty(String str) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(str);
        }
        return (String) AccessController.doPrivileged(new GetPropertyAction(str));
    }

    public static String privilegedGetProperty(String str, String str2) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(str, str2);
        }
        return (String) AccessController.doPrivileged(new GetPropertyAction(str, str2));
    }
}
