package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidValue;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynFixed.class */
public interface DynFixed extends Object, DynAny {
    byte[] get_value();

    void set_value(byte[] bArr) throws InvalidValue;
}
