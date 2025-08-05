package org.omg.PortableServer.POAManagerPackage;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/PortableServer/POAManagerPackage/State.class */
public class State implements IDLEntity {
    private int __value;
    public static final int _HOLDING = 0;
    public static final int _ACTIVE = 1;
    public static final int _DISCARDING = 2;
    public static final int _INACTIVE = 3;
    private static int __size = 4;
    private static State[] __array = new State[__size];
    public static final State HOLDING = new State(0);
    public static final State ACTIVE = new State(1);
    public static final State DISCARDING = new State(2);
    public static final State INACTIVE = new State(3);

    public int value() {
        return this.__value;
    }

    public static State from_int(int i2) {
        if (i2 >= 0 && i2 < __size) {
            return __array[i2];
        }
        throw new BAD_PARAM();
    }

    protected State(int i2) {
        this.__value = i2;
        __array[this.__value] = this;
    }
}
