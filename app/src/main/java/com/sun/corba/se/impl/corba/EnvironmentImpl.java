package com.sun.corba.se.impl.corba;

import org.omg.CORBA.Environment;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/EnvironmentImpl.class */
public class EnvironmentImpl extends Environment {
    private Exception _exc;

    @Override // org.omg.CORBA.Environment
    public Exception exception() {
        return this._exc;
    }

    @Override // org.omg.CORBA.Environment
    public void exception(Exception exc) {
        this._exc = exc;
    }

    @Override // org.omg.CORBA.Environment
    public void clear() {
        this._exc = null;
    }
}
