package java.net;

import java.security.BasicPermission;

/* loaded from: rt.jar:java/net/NetPermission.class */
public final class NetPermission extends BasicPermission {
    private static final long serialVersionUID = -8343910153355041693L;

    public NetPermission(String str) {
        super(str);
    }

    public NetPermission(String str, String str2) {
        super(str, str2);
    }
}
