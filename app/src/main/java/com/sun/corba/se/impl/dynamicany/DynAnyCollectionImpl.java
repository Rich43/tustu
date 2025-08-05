package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynAnyCollectionImpl.class */
abstract class DynAnyCollectionImpl extends DynAnyConstructedImpl {
    Any[] anys;

    protected abstract void checkValue(Object[] objArr) throws InvalidValue;

    private DynAnyCollectionImpl() {
        this(null, (Any) null, false);
    }

    protected DynAnyCollectionImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
        this.anys = null;
    }

    protected DynAnyCollectionImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.anys = null;
    }

    protected void createDefaultComponentAt(int i2, TypeCode typeCode) {
        try {
            this.components[i2] = DynAnyUtil.createMostDerivedDynAny(typeCode, this.orb);
        } catch (InconsistentTypeCode e2) {
        }
        this.anys[i2] = getAny(this.components[i2]);
    }

    protected TypeCode getContentType() {
        try {
            return this.any.type().content_type();
        } catch (BadKind e2) {
            return null;
        }
    }

    protected int getBound() {
        try {
            return this.any.type().length();
        } catch (BadKind e2) {
            return 0;
        }
    }

    public Any[] get_elements() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (checkInitComponents()) {
            return this.anys;
        }
        return null;
    }

    public void set_elements(Any[] anyArr) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        checkValue(anyArr);
        this.components = new DynAny[anyArr.length];
        this.anys = anyArr;
        TypeCode contentType = getContentType();
        for (int i2 = 0; i2 < anyArr.length; i2++) {
            if (anyArr[i2] != null) {
                if (!anyArr[i2].type().equal(contentType)) {
                    clearData();
                    throw new TypeMismatch();
                }
                try {
                    this.components[i2] = DynAnyUtil.createMostDerivedDynAny(anyArr[i2], this.orb, false);
                } catch (InconsistentTypeCode e2) {
                    throw new InvalidValue();
                }
            } else {
                clearData();
                throw new InvalidValue();
            }
        }
        this.index = anyArr.length == 0 ? -1 : 0;
        this.representations = (byte) 4;
    }

    public DynAny[] get_elements_as_dyn_any() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (checkInitComponents()) {
            return this.components;
        }
        return null;
    }

    public void set_elements_as_dyn_any(DynAny[] dynAnyArr) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        checkValue(dynAnyArr);
        this.components = dynAnyArr == null ? emptyComponents : dynAnyArr;
        this.anys = new Any[dynAnyArr.length];
        TypeCode contentType = getContentType();
        for (int i2 = 0; i2 < dynAnyArr.length; i2++) {
            if (dynAnyArr[i2] != null) {
                if (!dynAnyArr[i2].type().equal(contentType)) {
                    clearData();
                    throw new TypeMismatch();
                }
                this.anys[i2] = getAny(dynAnyArr[i2]);
            } else {
                clearData();
                throw new InvalidValue();
            }
        }
        this.index = dynAnyArr.length == 0 ? -1 : 0;
        this.representations = (byte) 4;
    }
}
