package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
import org.omg.DynamicAny.DynEnum;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynEnumImpl.class */
public class DynEnumImpl extends DynAnyBasicImpl implements DynEnum {
    int currentEnumeratorIndex;

    private DynEnumImpl() {
        this(null, (Any) null, false);
    }

    protected DynEnumImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
        this.currentEnumeratorIndex = -1;
        this.index = -1;
        try {
            this.currentEnumeratorIndex = this.any.extract_long();
        } catch (BAD_OPERATION e2) {
            this.currentEnumeratorIndex = 0;
            this.any.type(this.any.type());
            this.any.insert_long(0);
        }
    }

    protected DynEnumImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.currentEnumeratorIndex = -1;
        this.index = -1;
        this.currentEnumeratorIndex = 0;
        this.any.insert_long(0);
    }

    private int memberCount() {
        int iMember_count = 0;
        try {
            iMember_count = this.any.type().member_count();
        } catch (BadKind e2) {
        }
        return iMember_count;
    }

    private String memberName(int i2) {
        String strMember_name = null;
        try {
            strMember_name = this.any.type().member_name(i2);
        } catch (BadKind e2) {
        } catch (Bounds e3) {
        }
        return strMember_name;
    }

    private int computeCurrentEnumeratorIndex(String str) {
        int iMemberCount = memberCount();
        for (int i2 = 0; i2 < iMemberCount; i2++) {
            if (memberName(i2).equals(str)) {
                return i2;
            }
        }
        return -1;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyBasicImpl, org.omg.DynamicAny.DynAnyOperations
    public int component_count() {
        return 0;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyBasicImpl, org.omg.DynamicAny.DynAnyOperations
    public DynAny current_component() throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        throw new TypeMismatch();
    }

    @Override // org.omg.DynamicAny.DynEnumOperations
    public String get_as_string() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        return memberName(this.currentEnumeratorIndex);
    }

    @Override // org.omg.DynamicAny.DynEnumOperations
    public void set_as_string(String str) throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        int iComputeCurrentEnumeratorIndex = computeCurrentEnumeratorIndex(str);
        if (iComputeCurrentEnumeratorIndex == -1) {
            throw new InvalidValue();
        }
        this.currentEnumeratorIndex = iComputeCurrentEnumeratorIndex;
        this.any.insert_long(iComputeCurrentEnumeratorIndex);
    }

    @Override // org.omg.DynamicAny.DynEnumOperations
    public int get_as_ulong() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        return this.currentEnumeratorIndex;
    }

    @Override // org.omg.DynamicAny.DynEnumOperations
    public void set_as_ulong(int i2) throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (i2 < 0 || i2 >= memberCount()) {
            throw new InvalidValue();
        }
        this.currentEnumeratorIndex = i2;
        this.any.insert_long(i2);
    }
}
