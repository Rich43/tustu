package com.sun.org.omg.CORBA;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/OperationMode.class */
public class OperationMode implements IDLEntity {
    private int __value;
    public static final int _OP_NORMAL = 0;
    public static final int _OP_ONEWAY = 1;
    private static int __size = 2;
    private static OperationMode[] __array = new OperationMode[__size];
    public static final OperationMode OP_NORMAL = new OperationMode(0);
    public static final OperationMode OP_ONEWAY = new OperationMode(1);

    public int value() {
        return this.__value;
    }

    public static OperationMode from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected OperationMode(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
