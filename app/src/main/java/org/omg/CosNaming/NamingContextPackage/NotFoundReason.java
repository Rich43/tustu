package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/NotFoundReason.class */
public class NotFoundReason implements IDLEntity {
    private int __value;
    public static final int _missing_node = 0;
    public static final int _not_context = 1;
    public static final int _not_object = 2;
    private static int __size = 3;
    private static NotFoundReason[] __array = new NotFoundReason[__size];
    public static final NotFoundReason missing_node = new NotFoundReason(0);
    public static final NotFoundReason not_context = new NotFoundReason(1);
    public static final NotFoundReason not_object = new NotFoundReason(2);

    public int value() {
        return this.__value;
    }

    public static NotFoundReason from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected NotFoundReason(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
