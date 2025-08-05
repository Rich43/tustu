package com.sun.corba.se.impl.orb;

/* compiled from: ORBImpl.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/orb/SynchVariable.class */
class SynchVariable {
    public boolean _flag = false;

    SynchVariable() {
    }

    public void set() {
        this._flag = true;
    }

    public boolean value() {
        return this._flag;
    }

    public void reset() {
        this._flag = false;
    }
}
