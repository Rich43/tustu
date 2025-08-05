package org.omg.DynamicAny;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

/* loaded from: rt.jar:org/omg/DynamicAny/DynEnumOperations.class */
public interface DynEnumOperations extends DynAnyOperations {
    String get_as_string();

    void set_as_string(String str) throws InvalidValue;

    int get_as_ulong();

    void set_as_ulong(int i2) throws InvalidValue;
}
