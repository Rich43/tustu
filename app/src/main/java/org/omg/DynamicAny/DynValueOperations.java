package org.omg.DynamicAny;

import org.omg.CORBA.TCKind;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:org/omg/DynamicAny/DynValueOperations.class */
public interface DynValueOperations extends DynValueCommonOperations {
    String current_member_name() throws TypeMismatch, InvalidValue;

    TCKind current_member_kind() throws TypeMismatch, InvalidValue;

    NameValuePair[] get_members() throws InvalidValue;

    void set_members(NameValuePair[] nameValuePairArr) throws TypeMismatch, InvalidValue;

    NameDynAnyPair[] get_members_as_dyn_any() throws InvalidValue;

    void set_members_as_dyn_any(NameDynAnyPair[] nameDynAnyPairArr) throws TypeMismatch, InvalidValue;
}
