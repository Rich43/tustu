package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidSeq;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynArray.class */
public interface DynArray extends Object, DynAny {
    Any[] get_elements();

    void set_elements(Any[] anyArr) throws InvalidSeq;
}
