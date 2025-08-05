package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:org/omg/DynamicAny/DynSequenceOperations.class */
public interface DynSequenceOperations extends DynAnyOperations {
    int get_length();

    void set_length(int i2) throws InvalidValue;

    Any[] get_elements();

    void set_elements(Any[] anyArr) throws TypeMismatch, InvalidValue;

    DynAny[] get_elements_as_dyn_any();

    void set_elements_as_dyn_any(DynAny[] dynAnyArr) throws TypeMismatch, InvalidValue;
}
