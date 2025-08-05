package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/Context.class */
public abstract class Context {
    public abstract String context_name();

    public abstract Context parent();

    public abstract Context create_child(String str);

    public abstract void set_one_value(String str, Any any);

    public abstract void set_values(NVList nVList);

    public abstract void delete_values(String str);

    public abstract NVList get_values(String str, int i2, String str2);
}
