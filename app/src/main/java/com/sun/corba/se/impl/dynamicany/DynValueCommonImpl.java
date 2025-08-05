package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
import org.omg.DynamicAny.DynValueCommon;
import org.omg.DynamicAny.NameDynAnyPair;
import org.omg.DynamicAny.NameValuePair;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynValueCommonImpl.class */
abstract class DynValueCommonImpl extends DynAnyComplexImpl implements DynValueCommon {
    protected boolean isNull;

    private DynValueCommonImpl() {
        this(null, (Any) null, false);
        this.isNull = true;
    }

    protected DynValueCommonImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
        this.isNull = checkInitComponents();
    }

    protected DynValueCommonImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.isNull = true;
    }

    public boolean is_null() {
        return this.isNull;
    }

    public void set_to_null() {
        this.isNull = true;
        clearData();
    }

    public void set_to_value() {
        if (this.isNull) {
            this.isNull = false;
        }
    }

    public NameValuePair[] get_members() throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.isNull) {
            throw new InvalidValue();
        }
        checkInitComponents();
        return this.nameValuePairs;
    }

    public NameDynAnyPair[] get_members_as_dyn_any() throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.isNull) {
            throw new InvalidValue();
        }
        checkInitComponents();
        return this.nameDynAnyPairs;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyComplexImpl, org.omg.DynamicAny.DynStructOperations
    public void set_members(NameValuePair[] nameValuePairArr) throws TypeMismatch, InvalidValue {
        super.set_members(nameValuePairArr);
        this.isNull = false;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyComplexImpl, org.omg.DynamicAny.DynStructOperations
    public void set_members_as_dyn_any(NameDynAnyPair[] nameDynAnyPairArr) throws TypeMismatch, InvalidValue {
        super.set_members_as_dyn_any(nameDynAnyPairArr);
        this.isNull = false;
    }
}
