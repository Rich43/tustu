package javax.net.ssl;

import java.security.BasicPermission;

/* loaded from: rt.jar:javax/net/ssl/SSLPermission.class */
public final class SSLPermission extends BasicPermission {
    private static final long serialVersionUID = -3456898025505876775L;

    public SSLPermission(String str) {
        super(str);
    }

    public SSLPermission(String str, String str2) {
        super(str, str2);
    }
}
