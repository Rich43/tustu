package org.omg.DynamicAny;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:org/omg/DynamicAny/DynFixedOperations.class */
public interface DynFixedOperations extends DynAnyOperations {
    String get_value();

    boolean set_value(String str) throws TypeMismatch, InvalidValue;
}
