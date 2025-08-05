package jdk.net;

import java.security.BasicPermission;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:jdk/net/NetworkPermission.class */
public final class NetworkPermission extends BasicPermission {
    private static final long serialVersionUID = -2012939586906722291L;

    public NetworkPermission(String str) {
        super(str);
    }

    public NetworkPermission(String str, String str2) {
        super(str, str2);
    }
}
