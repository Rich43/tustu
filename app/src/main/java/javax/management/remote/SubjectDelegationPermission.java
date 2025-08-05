package javax.management.remote;

import java.security.BasicPermission;

/* loaded from: rt.jar:javax/management/remote/SubjectDelegationPermission.class */
public final class SubjectDelegationPermission extends BasicPermission {
    private static final long serialVersionUID = 1481618113008682343L;

    public SubjectDelegationPermission(String str) {
        super(str);
    }

    public SubjectDelegationPermission(String str, String str2) {
        super(str, str2);
        if (str2 != null) {
            throw new IllegalArgumentException("Non-null actions");
        }
    }
}
