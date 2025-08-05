package java.sql;

import java.security.BasicPermission;

/* loaded from: rt.jar:java/sql/SQLPermission.class */
public final class SQLPermission extends BasicPermission {
    static final long serialVersionUID = -1439323187199563495L;

    public SQLPermission(String str) {
        super(str);
    }

    public SQLPermission(String str, String str2) {
        super(str, str2);
    }
}
