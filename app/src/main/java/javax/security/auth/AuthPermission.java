package javax.security.auth;

import java.security.BasicPermission;

/* loaded from: rt.jar:javax/security/auth/AuthPermission.class */
public final class AuthPermission extends BasicPermission {
    private static final long serialVersionUID = 5806031445061587174L;

    public AuthPermission(String str) {
        super("createLoginContext".equals(str) ? "createLoginContext.*" : str);
    }

    public AuthPermission(String str, String str2) {
        super("createLoginContext".equals(str) ? "createLoginContext.*" : str, str2);
    }
}
