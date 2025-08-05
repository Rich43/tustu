package org.omg.PortableServer;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/IdUniquenessPolicyValue.class */
public class IdUniquenessPolicyValue implements IDLEntity {
    private int __value;
    public static final int _UNIQUE_ID = 0;
    public static final int _MULTIPLE_ID = 1;
    private static int __size = 2;
    private static IdUniquenessPolicyValue[] __array = new IdUniquenessPolicyValue[__size];
    public static final IdUniquenessPolicyValue UNIQUE_ID = new IdUniquenessPolicyValue(0);
    public static final IdUniquenessPolicyValue MULTIPLE_ID = new IdUniquenessPolicyValue(1);

    public int value() {
        return this.__value;
    }

    public static IdUniquenessPolicyValue from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected IdUniquenessPolicyValue(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
