package java.awt;

import java.security.BasicPermission;

/* loaded from: rt.jar:java/awt/AWTPermission.class */
public final class AWTPermission extends BasicPermission {
    private static final long serialVersionUID = 8890392402588814465L;

    public AWTPermission(String str) {
        super(str);
    }

    public AWTPermission(String str, String str2) {
        super(str, str2);
    }
}
