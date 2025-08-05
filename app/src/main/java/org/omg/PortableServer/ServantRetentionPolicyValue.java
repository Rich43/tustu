package org.omg.PortableServer;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/ServantRetentionPolicyValue.class */
public class ServantRetentionPolicyValue implements IDLEntity {
    private int __value;
    public static final int _RETAIN = 0;
    public static final int _NON_RETAIN = 1;
    private static int __size = 2;
    private static ServantRetentionPolicyValue[] __array = new ServantRetentionPolicyValue[__size];
    public static final ServantRetentionPolicyValue RETAIN = new ServantRetentionPolicyValue(0);
    public static final ServantRetentionPolicyValue NON_RETAIN = new ServantRetentionPolicyValue(1);

    public int value() {
        return this.__value;
    }

    public static ServantRetentionPolicyValue from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected ServantRetentionPolicyValue(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
