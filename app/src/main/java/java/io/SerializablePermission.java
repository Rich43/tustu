package java.io;

import java.security.BasicPermission;

/* loaded from: rt.jar:java/io/SerializablePermission.class */
public final class SerializablePermission extends BasicPermission {
    private static final long serialVersionUID = 8537212141160296410L;
    private String actions;

    public SerializablePermission(String str) {
        super(str);
    }

    public SerializablePermission(String str, String str2) {
        super(str, str2);
    }
}
