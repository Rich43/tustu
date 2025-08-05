package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactory;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynAnyFactoryImpl.class */
public class DynAnyFactoryImpl extends LocalObject implements DynAnyFactory {
    private ORB orb;
    private String[] __ids;

    private DynAnyFactoryImpl() {
        this.__ids = new String[]{"IDL:omg.org/DynamicAny/DynAnyFactory:1.0"};
        this.orb = null;
    }

    public DynAnyFactoryImpl(ORB orb) {
        this.__ids = new String[]{"IDL:omg.org/DynamicAny/DynAnyFactory:1.0"};
        this.orb = orb;
    }

    @Override // org.omg.DynamicAny.DynAnyFactoryOperations
    public DynAny create_dyn_any(Any any) throws InconsistentTypeCode {
        return DynAnyUtil.createMostDerivedDynAny(any, this.orb, true);
    }

    @Override // org.omg.DynamicAny.DynAnyFactoryOperations
    public DynAny create_dyn_any_from_type_code(TypeCode typeCode) throws InconsistentTypeCode {
        return DynAnyUtil.createMostDerivedDynAny(typeCode, this.orb);
    }

    public String[] _ids() {
        return (String[]) this.__ids.clone();
    }
}
