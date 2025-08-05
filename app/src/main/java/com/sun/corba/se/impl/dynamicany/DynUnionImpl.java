package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.CORBA.portable.InputStream;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
import org.omg.DynamicAny.DynUnion;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynUnionImpl.class */
public class DynUnionImpl extends DynAnyConstructedImpl implements DynUnion {
    DynAny discriminator;
    DynAny currentMember;
    int currentMemberIndex;

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ DynAny get_dyn_any() throws TypeMismatch, InvalidValue {
        return super.get_dyn_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Any get_any() throws TypeMismatch, InvalidValue {
        return super.get_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ String get_wstring() throws TypeMismatch, InvalidValue {
        return super.get_wstring();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ char get_wchar() throws TypeMismatch, InvalidValue {
        return super.get_wchar();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ long get_ulonglong() throws TypeMismatch, InvalidValue {
        return super.get_ulonglong();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ long get_longlong() throws TypeMismatch, InvalidValue {
        return super.get_longlong();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ TypeCode get_typecode() throws TypeMismatch, InvalidValue {
        return super.get_typecode();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Object get_reference() throws TypeMismatch, InvalidValue {
        return super.get_reference();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ String get_string() throws TypeMismatch, InvalidValue {
        return super.get_string();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ double get_double() throws TypeMismatch, InvalidValue {
        return super.get_double();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ float get_float() throws TypeMismatch, InvalidValue {
        return super.get_float();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ int get_ulong() throws TypeMismatch, InvalidValue {
        return super.get_ulong();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ int get_long() throws TypeMismatch, InvalidValue {
        return super.get_long();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ short get_ushort() throws TypeMismatch, InvalidValue {
        return super.get_ushort();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ short get_short() throws TypeMismatch, InvalidValue {
        return super.get_short();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ char get_char() throws TypeMismatch, InvalidValue {
        return super.get_char();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ byte get_octet() throws TypeMismatch, InvalidValue {
        return super.get_octet();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean get_boolean() throws TypeMismatch, InvalidValue {
        return super.get_boolean();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Serializable get_val() throws TypeMismatch, InvalidValue {
        return super.get_val();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_val(Serializable serializable) throws TypeMismatch, InvalidValue {
        super.insert_val(serializable);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_dyn_any(DynAny dynAny) throws TypeMismatch, InvalidValue {
        super.insert_dyn_any(dynAny);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_any(Any any) throws TypeMismatch, InvalidValue {
        super.insert_any(any);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_wstring(String str) throws TypeMismatch, InvalidValue {
        super.insert_wstring(str);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_wchar(char c2) throws TypeMismatch, InvalidValue {
        super.insert_wchar(c2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_ulonglong(long j2) throws TypeMismatch, InvalidValue {
        super.insert_ulonglong(j2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_longlong(long j2) throws TypeMismatch, InvalidValue {
        super.insert_longlong(j2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_typecode(TypeCode typeCode) throws TypeMismatch, InvalidValue {
        super.insert_typecode(typeCode);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_reference(Object object) throws TypeMismatch, InvalidValue {
        super.insert_reference(object);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_string(String str) throws TypeMismatch, InvalidValue {
        super.insert_string(str);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_double(double d2) throws TypeMismatch, InvalidValue {
        super.insert_double(d2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_float(float f2) throws TypeMismatch, InvalidValue {
        super.insert_float(f2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_ulong(int i2) throws TypeMismatch, InvalidValue {
        super.insert_ulong(i2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_long(int i2) throws TypeMismatch, InvalidValue {
        super.insert_long(i2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_ushort(short s2) throws TypeMismatch, InvalidValue {
        super.insert_ushort(s2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_short(short s2) throws TypeMismatch, InvalidValue {
        super.insert_short(s2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_char(char c2) throws TypeMismatch, InvalidValue {
        super.insert_char(c2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_octet(byte b2) throws TypeMismatch, InvalidValue {
        super.insert_octet(b2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_boolean(boolean z2) throws TypeMismatch, InvalidValue {
        super.insert_boolean(z2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ DynAny copy() {
        return super.copy();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void destroy() {
        super.destroy();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean equal(DynAny dynAny) {
        return super.equal(dynAny);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Any to_any() {
        return super.to_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void from_any(Any any) throws TypeMismatch, InvalidValue {
        super.from_any(any);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void assign(DynAny dynAny) throws TypeMismatch {
        super.assign(dynAny);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void rewind() {
        super.rewind();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean seek(int i2) {
        return super.seek(i2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean next() {
        return super.next();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ int component_count() {
        return super.component_count();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ DynAny current_component() throws TypeMismatch {
        return super.current_component();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl
    public /* bridge */ /* synthetic */ String[] _ids() {
        return super._ids();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ TypeCode type() {
        return super.type();
    }

    private DynUnionImpl() {
        this(null, (Any) null, false);
    }

    protected DynUnionImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
        this.discriminator = null;
        this.currentMember = null;
        this.currentMemberIndex = -1;
    }

    protected DynUnionImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.discriminator = null;
        this.currentMember = null;
        this.currentMemberIndex = -1;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
    protected boolean initializeComponentsFromAny() {
        try {
            InputStream inputStreamCreate_input_stream = this.any.create_input_stream();
            Any anyExtractAnyFromStream = DynAnyUtil.extractAnyFromStream(discriminatorType(), inputStreamCreate_input_stream, this.orb);
            this.discriminator = DynAnyUtil.createMostDerivedDynAny(anyExtractAnyFromStream, this.orb, false);
            this.currentMemberIndex = currentUnionMemberIndex(anyExtractAnyFromStream);
            this.currentMember = DynAnyUtil.createMostDerivedDynAny(DynAnyUtil.extractAnyFromStream(memberType(this.currentMemberIndex), inputStreamCreate_input_stream, this.orb), this.orb, false);
            this.components = new DynAny[]{this.discriminator, this.currentMember};
            return true;
        } catch (InconsistentTypeCode e2) {
            return true;
        }
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
    protected boolean initializeComponentsFromTypeCode() {
        try {
            this.discriminator = DynAnyUtil.createMostDerivedDynAny(memberLabel(0), this.orb, false);
            this.index = 0;
            this.currentMemberIndex = 0;
            this.currentMember = DynAnyUtil.createMostDerivedDynAny(memberType(0), this.orb);
            this.components = new DynAny[]{this.discriminator, this.currentMember};
            return true;
        } catch (InconsistentTypeCode e2) {
            return true;
        }
    }

    private TypeCode discriminatorType() {
        TypeCode typeCodeDiscriminator_type = null;
        try {
            typeCodeDiscriminator_type = this.any.type().discriminator_type();
        } catch (BadKind e2) {
        }
        return typeCodeDiscriminator_type;
    }

    private int memberCount() {
        int iMember_count = 0;
        try {
            iMember_count = this.any.type().member_count();
        } catch (BadKind e2) {
        }
        return iMember_count;
    }

    private Any memberLabel(int i2) {
        Any anyMember_label = null;
        try {
            anyMember_label = this.any.type().member_label(i2);
        } catch (BadKind e2) {
        } catch (Bounds e3) {
        }
        return anyMember_label;
    }

    private TypeCode memberType(int i2) {
        TypeCode typeCodeMember_type = null;
        try {
            typeCodeMember_type = this.any.type().member_type(i2);
        } catch (BadKind e2) {
        } catch (Bounds e3) {
        }
        return typeCodeMember_type;
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

    private int defaultIndex() {
        int iDefault_index = -1;
        try {
            iDefault_index = this.any.type().default_index();
        } catch (BadKind e2) {
        }
        return iDefault_index;
    }

    private int currentUnionMemberIndex(Any any) {
        int iMemberCount = memberCount();
        for (int i2 = 0; i2 < iMemberCount; i2++) {
            if (memberLabel(i2).equal(any)) {
                return i2;
            }
        }
        if (defaultIndex() != -1) {
            return defaultIndex();
        }
        return -1;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl
    protected void clearData() {
        super.clearData();
        this.discriminator = null;
        this.currentMember.destroy();
        this.currentMember = null;
        this.currentMemberIndex = -1;
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public DynAny get_discriminator() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (checkInitComponents()) {
            return this.discriminator;
        }
        return null;
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public void set_discriminator(DynAny dynAny) throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (!dynAny.type().equal(discriminatorType())) {
            throw new TypeMismatch();
        }
        DynAny dynAnyConvertToNative = DynAnyUtil.convertToNative(dynAny, this.orb);
        int iCurrentUnionMemberIndex = currentUnionMemberIndex(getAny(dynAnyConvertToNative));
        if (iCurrentUnionMemberIndex == -1) {
            clearData();
            this.index = 0;
            return;
        }
        checkInitComponents();
        if (this.currentMemberIndex == -1 || iCurrentUnionMemberIndex != this.currentMemberIndex) {
            clearData();
            this.index = 1;
            this.currentMemberIndex = iCurrentUnionMemberIndex;
            try {
                this.currentMember = DynAnyUtil.createMostDerivedDynAny(memberType(this.currentMemberIndex), this.orb);
            } catch (InconsistentTypeCode e2) {
            }
            this.discriminator = dynAnyConvertToNative;
            this.components = new DynAny[]{this.discriminator, this.currentMember};
            this.representations = (byte) 4;
        }
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public void set_to_default_member() throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        int iDefaultIndex = defaultIndex();
        if (iDefaultIndex == -1) {
            throw new TypeMismatch();
        }
        try {
            clearData();
            this.index = 1;
            this.currentMemberIndex = iDefaultIndex;
            this.currentMember = DynAnyUtil.createMostDerivedDynAny(memberType(iDefaultIndex), this.orb);
            this.components = new DynAny[]{this.discriminator, this.currentMember};
            Any anyCreate_any = this.orb.create_any();
            anyCreate_any.insert_octet((byte) 0);
            this.discriminator = DynAnyUtil.createMostDerivedDynAny(anyCreate_any, this.orb, false);
            this.representations = (byte) 4;
        } catch (InconsistentTypeCode e2) {
        }
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public void set_to_no_active_member() throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (defaultIndex() != -1) {
            throw new TypeMismatch();
        }
        checkInitComponents();
        Any any = getAny(this.discriminator);
        any.type(any.type());
        this.index = 0;
        this.currentMemberIndex = -1;
        this.currentMember.destroy();
        this.currentMember = null;
        this.components[0] = this.discriminator;
        this.representations = (byte) 4;
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public boolean has_no_active_member() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (defaultIndex() != -1) {
            return false;
        }
        checkInitComponents();
        return checkInitComponents() && this.currentMemberIndex == -1;
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public TCKind discriminator_kind() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        return discriminatorType().kind();
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public DynAny member() throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (!checkInitComponents() || this.currentMemberIndex == -1) {
            throw new InvalidValue();
        }
        return this.currentMember;
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public String member_name() throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (!checkInitComponents() || this.currentMemberIndex == -1) {
            throw new InvalidValue();
        }
        String strMemberName = memberName(this.currentMemberIndex);
        return strMemberName == null ? "" : strMemberName;
    }

    @Override // org.omg.DynamicAny.DynUnionOperations
    public TCKind member_kind() throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (!checkInitComponents() || this.currentMemberIndex == -1) {
            throw new InvalidValue();
        }
        return memberType(this.currentMemberIndex).kind();
    }
}
