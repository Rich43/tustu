package java.security;

/* loaded from: rt.jar:java/security/SecurityPermission.class */
public final class SecurityPermission extends BasicPermission {
    private static final long serialVersionUID = 5236109936224050470L;

    public SecurityPermission(String str) {
        super(str);
    }

    public SecurityPermission(String str, String str2) {
        super(str, str2);
    }
}
