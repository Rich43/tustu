package java.lang.reflect;

import java.security.BasicPermission;

/* loaded from: rt.jar:java/lang/reflect/ReflectPermission.class */
public final class ReflectPermission extends BasicPermission {
    private static final long serialVersionUID = 7412737110241507485L;

    public ReflectPermission(String str) {
        super(str);
    }

    public ReflectPermission(String str, String str2) {
        super(str, str2);
    }
}
