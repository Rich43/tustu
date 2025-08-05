package com.sun.corba.se.impl.corba;

import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.NamedValue;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/NamedValueImpl.class */
public class NamedValueImpl extends NamedValue {
    private String _name;
    private Any _value;
    private int _flags;
    private ORB _orb;

    public NamedValueImpl(ORB orb) {
        this._orb = orb;
        this._value = new AnyImpl(this._orb);
    }

    public NamedValueImpl(ORB orb, String str, Any any, int i2) {
        this._orb = orb;
        this._name = str;
        this._value = any;
        this._flags = i2;
    }

    @Override // org.omg.CORBA.NamedValue
    public String name() {
        return this._name;
    }

    @Override // org.omg.CORBA.NamedValue
    public Any value() {
        return this._value;
    }

    @Override // org.omg.CORBA.NamedValue
    public int flags() {
        return this._flags;
    }
}
