package org.omg.PortableServer;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/IdAssignmentPolicyValue.class */
public class IdAssignmentPolicyValue implements IDLEntity {
    private int __value;
    public static final int _USER_ID = 0;
    public static final int _SYSTEM_ID = 1;
    private static int __size = 2;
    private static IdAssignmentPolicyValue[] __array = new IdAssignmentPolicyValue[__size];
    public static final IdAssignmentPolicyValue USER_ID = new IdAssignmentPolicyValue(0);
    public static final IdAssignmentPolicyValue SYSTEM_ID = new IdAssignmentPolicyValue(1);

    public int value() {
        return this.__value;
    }

    public static IdAssignmentPolicyValue from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected IdAssignmentPolicyValue(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
