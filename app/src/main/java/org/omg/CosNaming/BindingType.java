package org.omg.CosNaming;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CosNaming/BindingType.class */
public class BindingType implements IDLEntity {
    private int __value;
    public static final int _nobject = 0;
    public static final int _ncontext = 1;
    private static int __size = 2;
    private static BindingType[] __array = new BindingType[__size];
    public static final BindingType nobject = new BindingType(0);
    public static final BindingType ncontext = new BindingType(1);

    public int value() {
        return this.__value;
    }

    public static BindingType from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected BindingType(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
