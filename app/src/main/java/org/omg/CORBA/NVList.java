package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/NVList.class */
public abstract class NVList {
    public abstract int count();

    public abstract NamedValue add(int i2);

    public abstract NamedValue add_item(String str, int i2);

    public abstract NamedValue add_value(String str, Any any, int i2);

    public abstract NamedValue item(int i2) throws Bounds;

    public abstract void remove(int i2) throws Bounds;
}
