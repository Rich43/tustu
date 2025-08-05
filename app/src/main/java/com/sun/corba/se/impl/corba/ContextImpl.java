package com.sun.corba.se.impl.corba;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.NVList;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/ContextImpl.class */
public final class ContextImpl extends Context {
    private ORB _orb;
    private ORBUtilSystemException wrapper;

    public ContextImpl(ORB orb) {
        this._orb = orb;
        this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB) orb, CORBALogDomains.RPC_PRESENTATION);
    }

    public ContextImpl(Context context) {
        throw this.wrapper.contextNotImplemented();
    }

    @Override // org.omg.CORBA.Context
    public String context_name() {
        throw this.wrapper.contextNotImplemented();
    }

    @Override // org.omg.CORBA.Context
    public Context parent() {
        throw this.wrapper.contextNotImplemented();
    }

    @Override // org.omg.CORBA.Context
    public Context create_child(String str) {
        throw this.wrapper.contextNotImplemented();
    }

    @Override // org.omg.CORBA.Context
    public void set_one_value(String str, Any any) {
        throw this.wrapper.contextNotImplemented();
    }

    @Override // org.omg.CORBA.Context
    public void set_values(NVList nVList) {
        throw this.wrapper.contextNotImplemented();
    }

    @Override // org.omg.CORBA.Context
    public void delete_values(String str) {
        throw this.wrapper.contextNotImplemented();
    }

    @Override // org.omg.CORBA.Context
    public NVList get_values(String str, int i2, String str2) {
        throw this.wrapper.contextNotImplemented();
    }
}
