package org.omg.PortableServer;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/RequestProcessingPolicyValue.class */
public class RequestProcessingPolicyValue implements IDLEntity {
    private int __value;
    public static final int _USE_ACTIVE_OBJECT_MAP_ONLY = 0;
    public static final int _USE_DEFAULT_SERVANT = 1;
    public static final int _USE_SERVANT_MANAGER = 2;
    private static int __size = 3;
    private static RequestProcessingPolicyValue[] __array = new RequestProcessingPolicyValue[__size];
    public static final RequestProcessingPolicyValue USE_ACTIVE_OBJECT_MAP_ONLY = new RequestProcessingPolicyValue(0);
    public static final RequestProcessingPolicyValue USE_DEFAULT_SERVANT = new RequestProcessingPolicyValue(1);
    public static final RequestProcessingPolicyValue USE_SERVANT_MANAGER = new RequestProcessingPolicyValue(2);

    public int value() {
        return this.__value;
    }

    public static RequestProcessingPolicyValue from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected RequestProcessingPolicyValue(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
