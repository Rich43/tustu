package org.omg.PortableServer;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/ThreadPolicyValue.class */
public class ThreadPolicyValue implements IDLEntity {
    private int __value;
    public static final int _ORB_CTRL_MODEL = 0;
    public static final int _SINGLE_THREAD_MODEL = 1;
    private static int __size = 2;
    private static ThreadPolicyValue[] __array = new ThreadPolicyValue[__size];
    public static final ThreadPolicyValue ORB_CTRL_MODEL = new ThreadPolicyValue(0);
    public static final ThreadPolicyValue SINGLE_THREAD_MODEL = new ThreadPolicyValue(1);

    public int value() {
        return this.__value;
    }

    public static ThreadPolicyValue from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected ThreadPolicyValue(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
