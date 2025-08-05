package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/ContextList.class */
public abstract class ContextList {
    public abstract int count();

    public abstract void add(String str);

    public abstract String item(int i2) throws Bounds;

    public abstract void remove(int i2) throws Bounds;
}
