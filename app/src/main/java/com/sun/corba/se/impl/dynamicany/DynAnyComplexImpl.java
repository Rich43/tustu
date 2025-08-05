package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.CORBA.portable.InputStream;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
import org.omg.DynamicAny.NameDynAnyPair;
import org.omg.DynamicAny.NameValuePair;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynAnyComplexImpl.class */
abstract class DynAnyComplexImpl extends DynAnyConstructedImpl {
    String[] names;
    NameValuePair[] nameValuePairs;
    NameDynAnyPair[] nameDynAnyPairs;

    private DynAnyComplexImpl() {
        this(null, (Any) null, false);
    }

    protected DynAnyComplexImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
        this.names = null;
        this.nameValuePairs = null;
        this.nameDynAnyPairs = null;
    }

    protected DynAnyComplexImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.names = null;
        this.nameValuePairs = null;
        this.nameDynAnyPairs = null;
        this.index = 0;
    }

    public String current_member_name() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (!checkInitComponents() || this.index < 0 || this.index >= this.names.length) {
            throw new InvalidValue();
        }
        return this.names[this.index];
    }

    public TCKind current_member_kind() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (!checkInitComponents() || this.index < 0 || this.index >= this.components.length) {
            throw new InvalidValue();
        }
        return this.components[this.index].type().kind();
    }

    public void set_members(NameValuePair[] nameValuePairArr) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (nameValuePairArr == null || nameValuePairArr.length == 0) {
            clearData();
            return;
        }
        TypeCode typeCodeType = this.any.type();
        int iMember_count = 0;
        try {
            iMember_count = typeCodeType.member_count();
        } catch (BadKind e2) {
        }
        if (iMember_count != nameValuePairArr.length) {
            clearData();
            throw new InvalidValue();
        }
        allocComponents(nameValuePairArr);
        for (int i2 = 0; i2 < nameValuePairArr.length; i2++) {
            if (nameValuePairArr[i2] != null) {
                String str = nameValuePairArr[i2].id;
                String strMember_name = null;
                try {
                    strMember_name = typeCodeType.member_name(i2);
                } catch (BadKind e3) {
                } catch (Bounds e4) {
                }
                if (!strMember_name.equals(str) && !str.equals("")) {
                    clearData();
                    throw new TypeMismatch();
                }
                Any any = nameValuePairArr[i2].value;
                TypeCode typeCodeMember_type = null;
                try {
                    typeCodeMember_type = typeCodeType.member_type(i2);
                } catch (BadKind e5) {
                } catch (Bounds e6) {
                }
                if (!typeCodeMember_type.equal(any.type())) {
                    clearData();
                    throw new TypeMismatch();
                }
                try {
                    addComponent(i2, str, any, DynAnyUtil.createMostDerivedDynAny(any, this.orb, false));
                } catch (InconsistentTypeCode e7) {
                    throw new InvalidValue();
                }
            } else {
                clearData();
                throw new InvalidValue();
            }
        }
        this.index = nameValuePairArr.length == 0 ? -1 : 0;
        this.representations = (byte) 4;
    }

    public void set_members_as_dyn_any(NameDynAnyPair[] nameDynAnyPairArr) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (nameDynAnyPairArr == null || nameDynAnyPairArr.length == 0) {
            clearData();
            return;
        }
        TypeCode typeCodeType = this.any.type();
        int iMember_count = 0;
        try {
            iMember_count = typeCodeType.member_count();
        } catch (BadKind e2) {
        }
        if (iMember_count != nameDynAnyPairArr.length) {
            clearData();
            throw new InvalidValue();
        }
        allocComponents(nameDynAnyPairArr);
        for (int i2 = 0; i2 < nameDynAnyPairArr.length; i2++) {
            if (nameDynAnyPairArr[i2] != null) {
                String str = nameDynAnyPairArr[i2].id;
                String strMember_name = null;
                try {
                    strMember_name = typeCodeType.member_name(i2);
                } catch (BadKind e3) {
                } catch (Bounds e4) {
                }
                if (!strMember_name.equals(str) && !str.equals("")) {
                    clearData();
                    throw new TypeMismatch();
                }
                DynAny dynAny = nameDynAnyPairArr[i2].value;
                Any any = getAny(dynAny);
                TypeCode typeCodeMember_type = null;
                try {
                    typeCodeMember_type = typeCodeType.member_type(i2);
                } catch (BadKind e5) {
                } catch (Bounds e6) {
                }
                if (!typeCodeMember_type.equal(any.type())) {
                    clearData();
                    throw new TypeMismatch();
                }
                addComponent(i2, str, any, dynAny);
            } else {
                clearData();
                throw new InvalidValue();
            }
        }
        this.index = nameDynAnyPairArr.length == 0 ? -1 : 0;
        this.representations = (byte) 4;
    }

    private void allocComponents(int i2) {
        this.components = new DynAny[i2];
        this.names = new String[i2];
        this.nameValuePairs = new NameValuePair[i2];
        this.nameDynAnyPairs = new NameDynAnyPair[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            this.nameValuePairs[i3] = new NameValuePair();
            this.nameDynAnyPairs[i3] = new NameDynAnyPair();
        }
    }

    private void allocComponents(NameValuePair[] nameValuePairArr) {
        this.components = new DynAny[nameValuePairArr.length];
        this.names = new String[nameValuePairArr.length];
        this.nameValuePairs = nameValuePairArr;
        this.nameDynAnyPairs = new NameDynAnyPair[nameValuePairArr.length];
        for (int i2 = 0; i2 < nameValuePairArr.length; i2++) {
            this.nameDynAnyPairs[i2] = new NameDynAnyPair();
        }
    }

    private void allocComponents(NameDynAnyPair[] nameDynAnyPairArr) {
        this.components = new DynAny[nameDynAnyPairArr.length];
        this.names = new String[nameDynAnyPairArr.length];
        this.nameValuePairs = new NameValuePair[nameDynAnyPairArr.length];
        for (int i2 = 0; i2 < nameDynAnyPairArr.length; i2++) {
            this.nameValuePairs[i2] = new NameValuePair();
        }
        this.nameDynAnyPairs = nameDynAnyPairArr;
    }

    private void addComponent(int i2, String str, Any any, DynAny dynAny) {
        this.components[i2] = dynAny;
        this.names[i2] = str != null ? str : "";
        this.nameValuePairs[i2].id = str;
        this.nameValuePairs[i2].value = any;
        this.nameDynAnyPairs[i2].id = str;
        this.nameDynAnyPairs[i2].value = dynAny;
        if (dynAny instanceof DynAnyImpl) {
            ((DynAnyImpl) dynAny).setStatus((byte) 1);
        }
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
    protected boolean initializeComponentsFromAny() {
        TypeCode typeCodeType = this.any.type();
        TypeCode typeCodeMember_type = null;
        DynAny dynAnyCreateMostDerivedDynAny = null;
        String strMember_name = null;
        int iMember_count = 0;
        try {
            iMember_count = typeCodeType.member_count();
        } catch (BadKind e2) {
        }
        InputStream inputStreamCreate_input_stream = this.any.create_input_stream();
        allocComponents(iMember_count);
        for (int i2 = 0; i2 < iMember_count; i2++) {
            try {
                strMember_name = typeCodeType.member_name(i2);
                typeCodeMember_type = typeCodeType.member_type(i2);
            } catch (BadKind e3) {
            } catch (Bounds e4) {
            }
            Any anyExtractAnyFromStream = DynAnyUtil.extractAnyFromStream(typeCodeMember_type, inputStreamCreate_input_stream, this.orb);
            try {
                dynAnyCreateMostDerivedDynAny = DynAnyUtil.createMostDerivedDynAny(anyExtractAnyFromStream, this.orb, false);
            } catch (InconsistentTypeCode e5) {
            }
            addComponent(i2, strMember_name, anyExtractAnyFromStream, dynAnyCreateMostDerivedDynAny);
        }
        return true;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
    protected boolean initializeComponentsFromTypeCode() {
        TypeCode typeCodeType = this.any.type();
        TypeCode typeCodeMember_type = null;
        DynAny dynAnyCreateMostDerivedDynAny = null;
        int iMember_count = 0;
        try {
            iMember_count = typeCodeType.member_count();
        } catch (BadKind e2) {
        }
        allocComponents(iMember_count);
        for (int i2 = 0; i2 < iMember_count; i2++) {
            String strMember_name = null;
            try {
                strMember_name = typeCodeType.member_name(i2);
                typeCodeMember_type = typeCodeType.member_type(i2);
            } catch (BadKind e3) {
            } catch (Bounds e4) {
            }
            try {
                dynAnyCreateMostDerivedDynAny = DynAnyUtil.createMostDerivedDynAny(typeCodeMember_type, this.orb);
            } catch (InconsistentTypeCode e5) {
            }
            addComponent(i2, strMember_name, getAny(dynAnyCreateMostDerivedDynAny), dynAnyCreateMostDerivedDynAny);
        }
        return true;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl
    protected void clearData() {
        super.clearData();
        this.names = null;
        this.nameValuePairs = null;
        this.nameDynAnyPairs = null;
    }
}
