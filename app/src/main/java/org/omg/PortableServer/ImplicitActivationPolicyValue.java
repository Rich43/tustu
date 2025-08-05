package org.omg.PortableServer;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/ImplicitActivationPolicyValue.class */
public class ImplicitActivationPolicyValue implements IDLEntity {
    private int __value;
    public static final int _IMPLICIT_ACTIVATION = 0;
    public static final int _NO_IMPLICIT_ACTIVATION = 1;
    private static int __size = 2;
    private static ImplicitActivationPolicyValue[] __array = new ImplicitActivationPolicyValue[__size];
    public static final ImplicitActivationPolicyValue IMPLICIT_ACTIVATION = new ImplicitActivationPolicyValue(0);
    public static final ImplicitActivationPolicyValue NO_IMPLICIT_ACTIVATION = new ImplicitActivationPolicyValue(1);

    public int value() {
        return this.__value;
    }

    public static ImplicitActivationPolicyValue from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected ImplicitActivationPolicyValue(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
