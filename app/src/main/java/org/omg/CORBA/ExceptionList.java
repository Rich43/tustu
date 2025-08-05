package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/ExceptionList.class */
public abstract class ExceptionList {
    public abstract int count();

    public abstract void add(TypeCode typeCode);

    public abstract TypeCode item(int i2) throws Bounds;

    public abstract void remove(int i2) throws Bounds;
}
