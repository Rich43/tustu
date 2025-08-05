package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidSeq;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynSequence.class */
public interface DynSequence extends Object, DynAny {
    int length();

    void length(int i2);

    Any[] get_elements();

    void set_elements(Any[] anyArr) throws InvalidSeq;
}
