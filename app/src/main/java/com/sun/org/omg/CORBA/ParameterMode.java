package com.sun.org.omg.CORBA;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ParameterMode.class */
public class ParameterMode implements IDLEntity {
    private int __value;
    public static final int _PARAM_IN = 0;
    public static final int _PARAM_OUT = 1;
    public static final int _PARAM_INOUT = 2;
    private static int __size = 3;
    private static ParameterMode[] __array = new ParameterMode[__size];
    public static final ParameterMode PARAM_IN = new ParameterMode(0);
    public static final ParameterMode PARAM_OUT = new ParameterMode(1);
    public static final ParameterMode PARAM_INOUT = new ParameterMode(2);

    public int value() {
        return this.__value;
    }

    public static ParameterMode from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected ParameterMode(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
