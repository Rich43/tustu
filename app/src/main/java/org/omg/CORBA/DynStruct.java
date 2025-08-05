package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidSeq;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynStruct.class */
public interface DynStruct extends Object, DynAny {
    String current_member_name();

    TCKind current_member_kind();

    NameValuePair[] get_members();

    void set_members(NameValuePair[] nameValuePairArr) throws InvalidSeq;
}
