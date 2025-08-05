package com.sun.corba.se.impl.corba;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.io.ValueUtility;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.RepositoryIdFactory;
import com.sun.corba.se.impl.orbutil.RepositoryIdStrings;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import org.omg.CORBA.Any;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/AnyImpl.class */
public class AnyImpl extends Any {
    private TypeCodeImpl typeCode;
    protected ORB orb;
    private ORBUtilSystemException wrapper;
    private CDRInputStream stream;
    private long value;
    private Object object;
    private boolean isInitialized;
    private static final int DEFAULT_BUFFER_SIZE = 32;
    static boolean[] isStreamed = {false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, true, false, false, true, true, true, true, false, false, false, false, false, false, false, false, false, false};

    /* loaded from: rt.jar:com/sun/corba/se/impl/corba/AnyImpl$AnyInputStream.class */
    private static final class AnyInputStream extends EncapsInputStream {
        public AnyInputStream(EncapsInputStream encapsInputStream) {
            super(encapsInputStream);
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/corba/AnyImpl$AnyOutputStream.class */
    private static final class AnyOutputStream extends EncapsOutputStream {
        public AnyOutputStream(ORB orb) {
            super(orb);
        }

        @Override // com.sun.corba.se.impl.encoding.EncapsOutputStream, com.sun.corba.se.impl.encoding.CDROutputStream, org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream
        public InputStream create_input_stream() {
            final InputStream inputStreamCreate_input_stream = super.create_input_stream();
            return (AnyInputStream) AccessController.doPrivileged(new PrivilegedAction<AnyInputStream>() { // from class: com.sun.corba.se.impl.corba.AnyImpl.AnyOutputStream.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public AnyInputStream run() {
                    return new AnyInputStream((EncapsInputStream) inputStreamCreate_input_stream);
                }
            });
        }
    }

    static AnyImpl convertToNative(ORB orb, Any any) {
        if (any instanceof AnyImpl) {
            return (AnyImpl) any;
        }
        AnyImpl anyImpl = new AnyImpl(orb, any);
        anyImpl.typeCode = TypeCodeImpl.convertToNative(orb, anyImpl.typeCode);
        return anyImpl;
    }

    public AnyImpl(ORB orb) {
        this.isInitialized = false;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PRESENTATION);
        this.typeCode = orb.get_primitive_tc(0);
        this.stream = null;
        this.object = null;
        this.value = 0L;
        this.isInitialized = true;
    }

    public AnyImpl(ORB orb, Any any) throws MARSHAL, DATA_CONVERSION {
        this(orb);
        if (any instanceof AnyImpl) {
            AnyImpl anyImpl = (AnyImpl) any;
            this.typeCode = anyImpl.typeCode;
            this.value = anyImpl.value;
            this.object = anyImpl.object;
            this.isInitialized = anyImpl.isInitialized;
            if (anyImpl.stream != null) {
                this.stream = anyImpl.stream.dup();
                return;
            }
            return;
        }
        read_value(any.create_input_stream(), any.type());
    }

    @Override // org.omg.CORBA.Any
    public TypeCode type() {
        return this.typeCode;
    }

    private TypeCode realType() {
        return realType(this.typeCode);
    }

    private TypeCode realType(TypeCode typeCode) {
        TypeCode typeCodeContent_type = typeCode;
        while (typeCodeContent_type.kind().value() == 21) {
            try {
                typeCodeContent_type = typeCodeContent_type.content_type();
            } catch (BadKind e2) {
                throw this.wrapper.badkindCannotOccur(e2);
            }
        }
        return typeCodeContent_type;
    }

    @Override // org.omg.CORBA.Any
    public void type(TypeCode typeCode) {
        this.typeCode = TypeCodeImpl.convertToNative(this.orb, typeCode);
        this.stream = null;
        this.value = 0L;
        this.object = null;
        this.isInitialized = typeCode.kind().value() == 0;
    }

    @Override // org.omg.CORBA.Any
    public boolean equal(Any any) {
        if (any == this) {
            return true;
        }
        if (!this.typeCode.equal(any.type())) {
            return false;
        }
        TypeCode typeCodeRealType = realType();
        switch (typeCodeRealType.kind().value()) {
            case 0:
            case 1:
                return true;
            case 2:
                return extract_short() == any.extract_short();
            case 3:
                return extract_long() == any.extract_long();
            case 4:
                return extract_ushort() == any.extract_ushort();
            case 5:
                return extract_ulong() == any.extract_ulong();
            case 6:
                return extract_float() == any.extract_float();
            case 7:
                return extract_double() == any.extract_double();
            case 8:
                return extract_boolean() == any.extract_boolean();
            case 9:
                return extract_char() == any.extract_char();
            case 10:
                return extract_octet() == any.extract_octet();
            case 11:
                return extract_any().equal(any.extract_any());
            case 12:
                return extract_TypeCode().equal(any.extract_TypeCode());
            case 13:
                return extract_Principal().equals(any.extract_Principal());
            case 14:
                return extract_Object().equals(any.extract_Object());
            case 15:
            case 16:
            case 19:
            case 20:
            case 22:
                return equalMember(typeCodeRealType, create_input_stream(), any.create_input_stream());
            case 17:
                return extract_long() == any.extract_long();
            case 18:
                return extract_string().equals(any.extract_string());
            case 21:
                throw this.wrapper.errorResolvingAlias();
            case 23:
                return extract_longlong() == any.extract_longlong();
            case 24:
                return extract_ulonglong() == any.extract_ulonglong();
            case 25:
                throw this.wrapper.tkLongDoubleNotSupported();
            case 26:
                return extract_wchar() == any.extract_wchar();
            case 27:
                return extract_wstring().equals(any.extract_wstring());
            case 28:
                return extract_fixed().compareTo(any.extract_fixed()) == 0;
            case 29:
            case 30:
                return extract_Value().equals(any.extract_Value());
            default:
                throw this.wrapper.typecodeNotSupported();
        }
    }

    private boolean equalMember(TypeCode typeCode, InputStream inputStream, InputStream inputStream2) throws MARSHAL {
        TypeCode typeCodeRealType = realType(typeCode);
        try {
            switch (typeCodeRealType.kind().value()) {
                case 0:
                case 1:
                    return true;
                case 2:
                    return inputStream.read_short() == inputStream2.read_short();
                case 3:
                    return inputStream.read_long() == inputStream2.read_long();
                case 4:
                    return inputStream.read_ushort() == inputStream2.read_ushort();
                case 5:
                    return inputStream.read_ulong() == inputStream2.read_ulong();
                case 6:
                    return inputStream.read_float() == inputStream2.read_float();
                case 7:
                    return inputStream.read_double() == inputStream2.read_double();
                case 8:
                    return inputStream.read_boolean() == inputStream2.read_boolean();
                case 9:
                    return inputStream.read_char() == inputStream2.read_char();
                case 10:
                    return inputStream.read_octet() == inputStream2.read_octet();
                case 11:
                    return inputStream.read_any().equal(inputStream2.read_any());
                case 12:
                    return inputStream.read_TypeCode().equal(inputStream2.read_TypeCode());
                case 13:
                    return inputStream.read_Principal().equals(inputStream2.read_Principal());
                case 14:
                    return inputStream.read_Object().equals(inputStream2.read_Object());
                case 15:
                case 22:
                    int iMember_count = typeCodeRealType.member_count();
                    for (int i2 = 0; i2 < iMember_count; i2++) {
                        if (!equalMember(typeCodeRealType.member_type(i2), inputStream, inputStream2)) {
                            return false;
                        }
                    }
                    return true;
                case 16:
                    Any anyCreate_any = this.orb.create_any();
                    Any anyCreate_any2 = this.orb.create_any();
                    anyCreate_any.read_value(inputStream, typeCodeRealType.discriminator_type());
                    anyCreate_any2.read_value(inputStream2, typeCodeRealType.discriminator_type());
                    if (!anyCreate_any.equal(anyCreate_any2)) {
                        return false;
                    }
                    int iCurrentUnionMemberIndex = TypeCodeImpl.convertToNative(this.orb, typeCodeRealType).currentUnionMemberIndex(anyCreate_any);
                    if (iCurrentUnionMemberIndex == -1) {
                        throw this.wrapper.unionDiscriminatorError();
                    }
                    if (!equalMember(typeCodeRealType.member_type(iCurrentUnionMemberIndex), inputStream, inputStream2)) {
                        return false;
                    }
                    return true;
                case 17:
                    return inputStream.read_long() == inputStream2.read_long();
                case 18:
                    return inputStream.read_string().equals(inputStream2.read_string());
                case 19:
                    int i3 = inputStream.read_long();
                    inputStream2.read_long();
                    for (int i4 = 0; i4 < i3; i4++) {
                        if (!equalMember(typeCodeRealType.content_type(), inputStream, inputStream2)) {
                            return false;
                        }
                    }
                    return true;
                case 20:
                    int iMember_count2 = typeCodeRealType.member_count();
                    for (int i5 = 0; i5 < iMember_count2; i5++) {
                        if (!equalMember(typeCodeRealType.content_type(), inputStream, inputStream2)) {
                            return false;
                        }
                    }
                    return true;
                case 21:
                    throw this.wrapper.errorResolvingAlias();
                case 23:
                    return inputStream.read_longlong() == inputStream2.read_longlong();
                case 24:
                    return inputStream.read_ulonglong() == inputStream2.read_ulonglong();
                case 25:
                    throw this.wrapper.tkLongDoubleNotSupported();
                case 26:
                    return inputStream.read_wchar() == inputStream2.read_wchar();
                case 27:
                    return inputStream.read_wstring().equals(inputStream2.read_wstring());
                case 28:
                    return inputStream.read_fixed().compareTo(inputStream2.read_fixed()) == 0;
                case 29:
                case 30:
                    return ((org.omg.CORBA_2_3.portable.InputStream) inputStream).read_value().equals(((org.omg.CORBA_2_3.portable.InputStream) inputStream2).read_value());
                default:
                    throw this.wrapper.typecodeNotSupported();
            }
        } catch (BadKind e2) {
            throw this.wrapper.badkindCannotOccur();
        } catch (Bounds e3) {
            throw this.wrapper.boundsCannotOccur();
        }
    }

    @Override // org.omg.CORBA.Any
    public OutputStream create_output_stream() {
        final ORB orb = this.orb;
        return (OutputStream) AccessController.doPrivileged(new PrivilegedAction<AnyOutputStream>() { // from class: com.sun.corba.se.impl.corba.AnyImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public AnyOutputStream run() {
                return new AnyOutputStream(orb);
            }
        });
    }

    @Override // org.omg.CORBA.Any
    public InputStream create_input_stream() {
        if (isStreamed[realType().kind().value()]) {
            return this.stream.dup();
        }
        OutputStream outputStreamCreate_output_stream = this.orb.create_output_stream();
        TCUtility.marshalIn(outputStreamCreate_output_stream, realType(), this.value, this.object);
        return outputStreamCreate_output_stream.create_input_stream();
    }

    @Override // org.omg.CORBA.Any
    public void read_value(InputStream inputStream, TypeCode typeCode) throws MARSHAL, DATA_CONVERSION {
        this.typeCode = TypeCodeImpl.convertToNative(this.orb, typeCode);
        int iValue = realType().kind().value();
        if (iValue >= isStreamed.length) {
            throw this.wrapper.invalidIsstreamedTckind(CompletionStatus.COMPLETED_MAYBE, new Integer(iValue));
        }
        if (isStreamed[iValue]) {
            if (inputStream instanceof AnyInputStream) {
                this.stream = (CDRInputStream) inputStream;
            } else {
                org.omg.CORBA_2_3.portable.OutputStream outputStream = (org.omg.CORBA_2_3.portable.OutputStream) this.orb.create_output_stream();
                this.typeCode.copy((org.omg.CORBA_2_3.portable.InputStream) inputStream, outputStream);
                this.stream = (CDRInputStream) outputStream.create_input_stream();
            }
        } else {
            Object[] objArr = {this.object};
            long[] jArr = new long[1];
            TCUtility.unmarshalIn(inputStream, realType(), jArr, objArr);
            this.value = jArr[0];
            this.object = objArr[0];
            this.stream = null;
        }
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public void write_value(OutputStream outputStream) throws MARSHAL, DATA_CONVERSION {
        if (isStreamed[realType().kind().value()]) {
            this.typeCode.copy(this.stream.dup(), outputStream);
        } else {
            TCUtility.marshalIn(outputStream, realType(), this.value, this.object);
        }
    }

    @Override // org.omg.CORBA.Any
    public void insert_Streamable(Streamable streamable) {
        this.typeCode = TypeCodeImpl.convertToNative(this.orb, streamable._type());
        this.object = streamable;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public Streamable extract_Streamable() {
        return (Streamable) this.object;
    }

    @Override // org.omg.CORBA.Any
    public void insert_short(short s2) {
        this.typeCode = this.orb.get_primitive_tc(2);
        this.value = s2;
        this.isInitialized = true;
    }

    private String getTCKindName(int i2) {
        if (i2 >= 0 && i2 < TypeCodeImpl.kindNames.length) {
            return TypeCodeImpl.kindNames[i2];
        }
        return "UNKNOWN(" + i2 + ")";
    }

    private void checkExtractBadOperation(int i2) {
        if (!this.isInitialized) {
            throw this.wrapper.extractNotInitialized();
        }
        int iValue = realType().kind().value();
        if (iValue != i2) {
            String tCKindName = getTCKindName(iValue);
            throw this.wrapper.extractWrongType(getTCKindName(i2), tCKindName);
        }
    }

    private void checkExtractBadOperationList(int[] iArr) {
        if (!this.isInitialized) {
            throw this.wrapper.extractNotInitialized();
        }
        int iValue = realType().kind().value();
        for (int i2 : iArr) {
            if (iValue == i2) {
                return;
            }
        }
        ArrayList arrayList = new ArrayList();
        for (int i3 : iArr) {
            arrayList.add(getTCKindName(i3));
        }
        throw this.wrapper.extractWrongTypeList(arrayList, getTCKindName(iValue));
    }

    @Override // org.omg.CORBA.Any
    public short extract_short() {
        checkExtractBadOperation(2);
        return (short) this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_long(int i2) {
        int iValue = realType().kind().value();
        if (iValue != 3 && iValue != 17) {
            this.typeCode = this.orb.get_primitive_tc(3);
        }
        this.value = i2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public int extract_long() {
        checkExtractBadOperationList(new int[]{3, 17});
        return (int) this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_ushort(short s2) {
        this.typeCode = this.orb.get_primitive_tc(4);
        this.value = s2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public short extract_ushort() {
        checkExtractBadOperation(4);
        return (short) this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_ulong(int i2) {
        this.typeCode = this.orb.get_primitive_tc(5);
        this.value = i2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public int extract_ulong() {
        checkExtractBadOperation(5);
        return (int) this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_float(float f2) {
        this.typeCode = this.orb.get_primitive_tc(6);
        this.value = Float.floatToIntBits(f2);
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public float extract_float() {
        checkExtractBadOperation(6);
        return Float.intBitsToFloat((int) this.value);
    }

    @Override // org.omg.CORBA.Any
    public void insert_double(double d2) {
        this.typeCode = this.orb.get_primitive_tc(7);
        this.value = Double.doubleToLongBits(d2);
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public double extract_double() {
        checkExtractBadOperation(7);
        return Double.longBitsToDouble(this.value);
    }

    @Override // org.omg.CORBA.Any
    public void insert_longlong(long j2) {
        this.typeCode = this.orb.get_primitive_tc(23);
        this.value = j2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public long extract_longlong() {
        checkExtractBadOperation(23);
        return this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_ulonglong(long j2) {
        this.typeCode = this.orb.get_primitive_tc(24);
        this.value = j2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public long extract_ulonglong() {
        checkExtractBadOperation(24);
        return this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_boolean(boolean z2) {
        this.typeCode = this.orb.get_primitive_tc(8);
        this.value = z2 ? 1L : 0L;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public boolean extract_boolean() {
        checkExtractBadOperation(8);
        return this.value != 0;
    }

    @Override // org.omg.CORBA.Any
    public void insert_char(char c2) {
        this.typeCode = this.orb.get_primitive_tc(9);
        this.value = c2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public char extract_char() {
        checkExtractBadOperation(9);
        return (char) this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_wchar(char c2) {
        this.typeCode = this.orb.get_primitive_tc(26);
        this.value = c2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public char extract_wchar() {
        checkExtractBadOperation(26);
        return (char) this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_octet(byte b2) {
        this.typeCode = this.orb.get_primitive_tc(10);
        this.value = b2;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public byte extract_octet() {
        checkExtractBadOperation(10);
        return (byte) this.value;
    }

    @Override // org.omg.CORBA.Any
    public void insert_string(String str) {
        if (this.typeCode.kind() == TCKind.tk_string) {
            try {
                int length = this.typeCode.length();
                if (length != 0 && str != null && str.length() > length) {
                    throw this.wrapper.badStringBounds(new Integer(str.length()), new Integer(length));
                }
            } catch (BadKind e2) {
                throw this.wrapper.badkindCannotOccur();
            }
        } else {
            this.typeCode = this.orb.get_primitive_tc(18);
        }
        this.object = str;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public String extract_string() {
        checkExtractBadOperation(18);
        return (String) this.object;
    }

    @Override // org.omg.CORBA.Any
    public void insert_wstring(String str) {
        if (this.typeCode.kind() == TCKind.tk_wstring) {
            try {
                int length = this.typeCode.length();
                if (length != 0 && str != null && str.length() > length) {
                    throw this.wrapper.badStringBounds(new Integer(str.length()), new Integer(length));
                }
            } catch (BadKind e2) {
                throw this.wrapper.badkindCannotOccur();
            }
        } else {
            this.typeCode = this.orb.get_primitive_tc(27);
        }
        this.object = str;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public String extract_wstring() {
        checkExtractBadOperation(27);
        return (String) this.object;
    }

    @Override // org.omg.CORBA.Any
    public void insert_any(Any any) {
        this.typeCode = this.orb.get_primitive_tc(11);
        this.object = any;
        this.stream = null;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public Any extract_any() {
        checkExtractBadOperation(11);
        return (Any) this.object;
    }

    @Override // org.omg.CORBA.Any
    public void insert_Object(Object object) {
        if (object == null) {
            this.typeCode = this.orb.get_primitive_tc(14);
        } else if (StubAdapter.isStub(object)) {
            this.typeCode = new TypeCodeImpl(this.orb, 14, StubAdapter.getTypeIds(object)[0], "");
        } else {
            throw this.wrapper.badInsertobjParam(CompletionStatus.COMPLETED_MAYBE, object.getClass().getName());
        }
        this.object = object;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public void insert_Object(Object object, TypeCode typeCode) {
        try {
            if (typeCode.id().equals("IDL:omg.org/CORBA/Object:1.0") || object._is_a(typeCode.id())) {
                this.typeCode = TypeCodeImpl.convertToNative(this.orb, typeCode);
                this.object = object;
                this.isInitialized = true;
                return;
            }
            throw this.wrapper.insertObjectIncompatible();
        } catch (Exception e2) {
            throw this.wrapper.insertObjectFailed(e2);
        }
    }

    @Override // org.omg.CORBA.Any
    public Object extract_Object() {
        if (!this.isInitialized) {
            throw this.wrapper.extractNotInitialized();
        }
        try {
            Object object = (Object) this.object;
            if (this.typeCode.id().equals("IDL:omg.org/CORBA/Object:1.0") || object._is_a(this.typeCode.id())) {
                return object;
            }
            throw this.wrapper.extractObjectIncompatible();
        } catch (Exception e2) {
            throw this.wrapper.extractObjectFailed(e2);
        }
    }

    @Override // org.omg.CORBA.Any
    public void insert_TypeCode(TypeCode typeCode) {
        this.typeCode = this.orb.get_primitive_tc(12);
        this.object = typeCode;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public TypeCode extract_TypeCode() {
        checkExtractBadOperation(12);
        return (TypeCode) this.object;
    }

    @Override // org.omg.CORBA.Any
    @Deprecated
    public void insert_Principal(Principal principal) {
        this.typeCode = this.orb.get_primitive_tc(13);
        this.object = principal;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    @Deprecated
    public Principal extract_Principal() {
        checkExtractBadOperation(13);
        return (Principal) this.object;
    }

    @Override // org.omg.CORBA.Any
    public Serializable extract_Value() {
        checkExtractBadOperationList(new int[]{29, 30, 32});
        return (Serializable) this.object;
    }

    @Override // org.omg.CORBA.Any
    public void insert_Value(Serializable serializable) {
        TypeCode typeCodeCreateTypeCodeForClass;
        this.object = serializable;
        if (serializable == null) {
            typeCodeCreateTypeCodeForClass = this.orb.get_primitive_tc(TCKind.tk_value);
        } else {
            typeCodeCreateTypeCodeForClass = createTypeCodeForClass(serializable.getClass(), (ORB) ORB.init());
        }
        this.typeCode = TypeCodeImpl.convertToNative(this.orb, typeCodeCreateTypeCodeForClass);
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public void insert_Value(Serializable serializable, TypeCode typeCode) {
        this.object = serializable;
        this.typeCode = TypeCodeImpl.convertToNative(this.orb, typeCode);
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public void insert_fixed(BigDecimal bigDecimal) {
        this.typeCode = TypeCodeImpl.convertToNative(this.orb, this.orb.create_fixed_tc(TypeCodeImpl.digits(bigDecimal), TypeCodeImpl.scale(bigDecimal)));
        this.object = bigDecimal;
        this.isInitialized = true;
    }

    @Override // org.omg.CORBA.Any
    public void insert_fixed(BigDecimal bigDecimal, TypeCode typeCode) {
        try {
            if (TypeCodeImpl.digits(bigDecimal) > typeCode.fixed_digits() || TypeCodeImpl.scale(bigDecimal) > typeCode.fixed_scale()) {
                throw this.wrapper.fixedNotMatch();
            }
            this.typeCode = TypeCodeImpl.convertToNative(this.orb, typeCode);
            this.object = bigDecimal;
            this.isInitialized = true;
        } catch (BadKind e2) {
            throw this.wrapper.fixedBadTypecode(e2);
        }
    }

    @Override // org.omg.CORBA.Any
    public BigDecimal extract_fixed() {
        checkExtractBadOperation(28);
        return (BigDecimal) this.object;
    }

    public TypeCode createTypeCodeForClass(Class cls, ORB orb) {
        TypeCode typeCodeCreateTypeCodeForClass;
        TypeCodeImpl typeCodeForClass = orb.getTypeCodeForClass(cls);
        if (typeCodeForClass != null) {
            return typeCodeForClass;
        }
        RepositoryIdStrings repIdStringsFactory = RepositoryIdFactory.getRepIdStringsFactory();
        if (cls.isArray()) {
            Class<?> componentType = cls.getComponentType();
            if (componentType.isPrimitive()) {
                typeCodeCreateTypeCodeForClass = getPrimitiveTypeCodeForClass(componentType, orb);
            } else {
                typeCodeCreateTypeCodeForClass = createTypeCodeForClass(componentType, orb);
            }
            return orb.create_value_box_tc(repIdStringsFactory.createForJavaType(cls), "Sequence", orb.create_sequence_tc(0, typeCodeCreateTypeCodeForClass));
        }
        if (cls == String.class) {
            return orb.create_value_box_tc(repIdStringsFactory.createForJavaType(cls), "StringValue", orb.create_string_tc(0));
        }
        TypeCodeImpl typeCodeImpl = (TypeCodeImpl) ValueUtility.createTypeCodeForClass(orb, cls, ORBUtility.createValueHandler());
        typeCodeImpl.setCaching(true);
        orb.setTypeCodeForClass(cls, typeCodeImpl);
        return typeCodeImpl;
    }

    private TypeCode getPrimitiveTypeCodeForClass(Class cls, ORB orb) {
        if (cls == Integer.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_long);
        }
        if (cls == Byte.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_octet);
        }
        if (cls == Long.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_longlong);
        }
        if (cls == Float.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_float);
        }
        if (cls == Double.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_double);
        }
        if (cls == Short.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_short);
        }
        if (cls == Character.TYPE) {
            if (ORBVersionFactory.getFOREIGN().compareTo(orb.getORBVersion()) == 0 || ORBVersionFactory.getNEWER().compareTo(orb.getORBVersion()) <= 0) {
                return orb.get_primitive_tc(TCKind.tk_wchar);
            }
            return orb.get_primitive_tc(TCKind.tk_char);
        }
        if (cls == Boolean.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_boolean);
        }
        return orb.get_primitive_tc(TCKind.tk_any);
    }

    public Any extractAny(TypeCode typeCode, ORB orb) throws MARSHAL, DATA_CONVERSION {
        Any anyCreate_any = orb.create_any();
        OutputStream outputStreamCreate_output_stream = anyCreate_any.create_output_stream();
        TypeCodeImpl.convertToNative(orb, typeCode).copy(this.stream, outputStreamCreate_output_stream);
        anyCreate_any.read_value(outputStreamCreate_output_stream.create_input_stream(), typeCode);
        return anyCreate_any;
    }

    public static Any extractAnyFromStream(TypeCode typeCode, InputStream inputStream, ORB orb) throws MARSHAL, DATA_CONVERSION {
        Any anyCreate_any = orb.create_any();
        OutputStream outputStreamCreate_output_stream = anyCreate_any.create_output_stream();
        TypeCodeImpl.convertToNative(orb, typeCode).copy(inputStream, outputStreamCreate_output_stream);
        anyCreate_any.read_value(outputStreamCreate_output_stream.create_input_stream(), typeCode);
        return anyCreate_any;
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }
}
