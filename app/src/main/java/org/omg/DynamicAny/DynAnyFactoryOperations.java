package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;

/* loaded from: rt.jar:org/omg/DynamicAny/DynAnyFactoryOperations.class */
public interface DynAnyFactoryOperations {
    DynAny create_dyn_any(Any any) throws InconsistentTypeCode;

    DynAny create_dyn_any_from_type_code(TypeCode typeCode) throws InconsistentTypeCode;
}
