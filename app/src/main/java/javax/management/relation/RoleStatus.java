package javax.management.relation;

/* loaded from: rt.jar:javax/management/relation/RoleStatus.class */
public class RoleStatus {
    public static final int NO_ROLE_WITH_NAME = 1;
    public static final int ROLE_NOT_READABLE = 2;
    public static final int ROLE_NOT_WRITABLE = 3;
    public static final int LESS_THAN_MIN_ROLE_DEGREE = 4;
    public static final int MORE_THAN_MAX_ROLE_DEGREE = 5;
    public static final int REF_MBEAN_OF_INCORRECT_CLASS = 6;
    public static final int REF_MBEAN_NOT_REGISTERED = 7;

    public static boolean isRoleStatus(int i2) {
        if (i2 != 1 && i2 != 2 && i2 != 3 && i2 != 4 && i2 != 5 && i2 != 6 && i2 != 7) {
            return false;
        }
        return true;
    }
}
