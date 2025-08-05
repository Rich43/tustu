package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/Environment.class */
public abstract class Environment {
    public abstract Exception exception();

    public abstract void exception(Exception exc);

    public abstract void clear();
}
