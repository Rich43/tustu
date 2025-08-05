package com.sun.org.omg.CORBA;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/AttributeMode.class */
public class AttributeMode implements IDLEntity {
    private int __value;
    public static final int _ATTR_NORMAL = 0;
    public static final int _ATTR_READONLY = 1;
    private static int __size = 2;
    private static AttributeMode[] __array = new AttributeMode[__size];
    public static final AttributeMode ATTR_NORMAL = new AttributeMode(0);
    public static final AttributeMode ATTR_READONLY = new AttributeMode(1);

    public int value() {
        return this.__value;
    }

    public static AttributeMode from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected AttributeMode(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
