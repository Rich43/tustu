package org.omg.PortableServer;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/LifespanPolicyValue.class */
public class LifespanPolicyValue implements IDLEntity {
    private int __value;
    public static final int _TRANSIENT = 0;
    public static final int _PERSISTENT = 1;
    private static int __size = 2;
    private static LifespanPolicyValue[] __array = new LifespanPolicyValue[__size];
    public static final LifespanPolicyValue TRANSIENT = new LifespanPolicyValue(0);
    public static final LifespanPolicyValue PERSISTENT = new LifespanPolicyValue(1);

    public int value() {
        return this.__value;
    }

    public static LifespanPolicyValue from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected LifespanPolicyValue(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
