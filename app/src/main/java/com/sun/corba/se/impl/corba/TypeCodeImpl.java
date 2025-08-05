package com.sun.corba.se.impl.corba;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.impl.encoding.TypeCodeInputStream;
import com.sun.corba.se.impl.encoding.TypeCodeOutputStream;
import com.sun.corba.se.impl.encoding.TypeCodeReader;
import com.sun.corba.se.impl.encoding.WrapperInputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import javafx.fxml.FXMLLoader;
import org.omg.CORBA.Any;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.CORBA.UnionMember;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.OutputStreamFactory;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/TypeCodeImpl.class */
public final class TypeCodeImpl extends TypeCode {
    protected static final int tk_indirect = -1;
    private static final int EMPTY = 0;
    private static final int SIMPLE = 1;
    private static final int COMPLEX = 2;
    private static final int[] typeTable = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 2, 2, 2, 2, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2};
    static final String[] kindNames = {FXMLLoader.NULL_KEYWORD, "void", SchemaSymbols.ATTVAL_SHORT, SchemaSymbols.ATTVAL_LONG, "ushort", "ulong", SchemaSymbols.ATTVAL_FLOAT, SchemaSymbols.ATTVAL_DOUBLE, "boolean", "char", "octet", "any", "typecode", "principal", "objref", "struct", "union", "enum", "string", "sequence", ControllerParameter.PARAM_CLASS_ARRAY, "alias", "exception", "longlong", "ulonglong", "longdouble", "wchar", "wstring", "fixed", "value", "valueBox", "native", "abstractInterface"};
    private int _kind;
    private String _id;
    private String _name;
    private int _memberCount;
    private String[] _memberNames;
    private TypeCodeImpl[] _memberTypes;
    private AnyImpl[] _unionLabels;
    private TypeCodeImpl _discriminator;
    private int _defaultIndex;
    private int _length;
    private TypeCodeImpl _contentType;
    private short _digits;
    private short _scale;
    private short _type_modifier;
    private TypeCodeImpl _concrete_base;
    private short[] _memberAccess;
    private TypeCodeImpl _parent;
    private int _parentOffset;
    private TypeCodeImpl _indirectType;
    private byte[] outBuffer;
    private boolean cachingEnabled;
    private ORB _orb;
    private ORBUtilSystemException wrapper;

    public TypeCodeImpl(ORB orb) {
        this._kind = 0;
        this._id = "";
        this._name = "";
        this._memberCount = 0;
        this._memberNames = null;
        this._memberTypes = null;
        this._unionLabels = null;
        this._discriminator = null;
        this._defaultIndex = -1;
        this._length = 0;
        this._contentType = null;
        this._digits = (short) 0;
        this._scale = (short) 0;
        this._type_modifier = (short) -1;
        this._concrete_base = null;
        this._memberAccess = null;
        this._parent = null;
        this._parentOffset = 0;
        this._indirectType = null;
        this.outBuffer = null;
        this.cachingEnabled = false;
        this._orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PRESENTATION);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0109 A[Catch: Bounds -> 0x0256, BadKind -> 0x025a, LOOP:1: B:26:0x0100->B:28:0x0109, LOOP_END, TryCatch #2 {BadKind -> 0x025a, Bounds -> 0x0256, blocks: (B:15:0x0044, B:16:0x0048, B:17:0x00a4, B:19:0x00b5, B:21:0x00c9, B:22:0x00d6, B:24:0x00df, B:20:0x00c4, B:25:0x00f2, B:26:0x0100, B:28:0x0109, B:29:0x012e, B:30:0x013c, B:32:0x0145, B:33:0x0158, B:34:0x0160, B:35:0x0170, B:36:0x0174, B:37:0x0188, B:38:0x01ac, B:40:0x01b4, B:41:0x01d0, B:42:0x01d4, B:43:0x020c, B:44:0x0214, B:45:0x0218, B:46:0x0244), top: B:52:0x0044 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0145 A[Catch: Bounds -> 0x0256, BadKind -> 0x025a, LOOP:2: B:30:0x013c->B:32:0x0145, LOOP_END, TryCatch #2 {BadKind -> 0x025a, Bounds -> 0x0256, blocks: (B:15:0x0044, B:16:0x0048, B:17:0x00a4, B:19:0x00b5, B:21:0x00c9, B:22:0x00d6, B:24:0x00df, B:20:0x00c4, B:25:0x00f2, B:26:0x0100, B:28:0x0109, B:29:0x012e, B:30:0x013c, B:32:0x0145, B:33:0x0158, B:34:0x0160, B:35:0x0170, B:36:0x0174, B:37:0x0188, B:38:0x01ac, B:40:0x01b4, B:41:0x01d0, B:42:0x01d4, B:43:0x020c, B:44:0x0214, B:45:0x0218, B:46:0x0244), top: B:52:0x0044 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB r9, org.omg.CORBA.TypeCode r10) {
        /*
            Method dump skipped, instructions count: 604
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.corba.TypeCodeImpl.<init>(com.sun.corba.se.spi.orb.ORB, org.omg.CORBA.TypeCode):void");
    }

    public TypeCodeImpl(ORB orb, int i2) {
        this(orb);
        this._kind = i2;
        switch (this._kind) {
            case 14:
                setId("IDL:omg.org/CORBA/Object:1.0");
                this._name = "Object";
                break;
            case 18:
            case 27:
                this._length = 0;
                break;
            case 29:
                this._concrete_base = null;
                break;
        }
    }

    public TypeCodeImpl(ORB orb, int i2, String str, String str2, StructMember[] structMemberArr) {
        this(orb);
        if (i2 == 15 || i2 == 22) {
            this._kind = i2;
            setId(str);
            this._name = str2;
            this._memberCount = structMemberArr.length;
            this._memberNames = new String[this._memberCount];
            this._memberTypes = new TypeCodeImpl[this._memberCount];
            for (int i3 = 0; i3 < this._memberCount; i3++) {
                this._memberNames[i3] = structMemberArr[i3].name;
                this._memberTypes[i3] = convertToNative(this._orb, structMemberArr[i3].type);
                this._memberTypes[i3].setParent(this);
            }
        }
    }

    public TypeCodeImpl(ORB orb, int i2, String str, String str2, TypeCode typeCode, UnionMember[] unionMemberArr) {
        this(orb);
        if (i2 == 16) {
            this._kind = i2;
            setId(str);
            this._name = str2;
            this._memberCount = unionMemberArr.length;
            this._discriminator = convertToNative(this._orb, typeCode);
            this._memberNames = new String[this._memberCount];
            this._memberTypes = new TypeCodeImpl[this._memberCount];
            this._unionLabels = new AnyImpl[this._memberCount];
            for (int i3 = 0; i3 < this._memberCount; i3++) {
                this._memberNames[i3] = unionMemberArr[i3].name;
                this._memberTypes[i3] = convertToNative(this._orb, unionMemberArr[i3].type);
                this._memberTypes[i3].setParent(this);
                this._unionLabels[i3] = new AnyImpl(this._orb, unionMemberArr[i3].label);
                if (this._unionLabels[i3].type().kind() == TCKind.tk_octet && this._unionLabels[i3].extract_octet() == 0) {
                    this._defaultIndex = i3;
                }
            }
        }
    }

    public TypeCodeImpl(ORB orb, int i2, String str, String str2, short s2, TypeCode typeCode, ValueMember[] valueMemberArr) {
        this(orb);
        if (i2 == 29) {
            this._kind = i2;
            setId(str);
            this._name = str2;
            this._type_modifier = s2;
            if (typeCode != null) {
                this._concrete_base = convertToNative(this._orb, typeCode);
            }
            this._memberCount = valueMemberArr.length;
            this._memberNames = new String[this._memberCount];
            this._memberTypes = new TypeCodeImpl[this._memberCount];
            this._memberAccess = new short[this._memberCount];
            for (int i3 = 0; i3 < this._memberCount; i3++) {
                this._memberNames[i3] = valueMemberArr[i3].name;
                this._memberTypes[i3] = convertToNative(this._orb, valueMemberArr[i3].type);
                this._memberTypes[i3].setParent(this);
                this._memberAccess[i3] = valueMemberArr[i3].access;
            }
        }
    }

    public TypeCodeImpl(ORB orb, int i2, String str, String str2, String[] strArr) {
        this(orb);
        if (i2 == 17) {
            this._kind = i2;
            setId(str);
            this._name = str2;
            this._memberCount = strArr.length;
            this._memberNames = new String[this._memberCount];
            for (int i3 = 0; i3 < this._memberCount; i3++) {
                this._memberNames[i3] = strArr[i3];
            }
        }
    }

    public TypeCodeImpl(ORB orb, int i2, String str, String str2, TypeCode typeCode) {
        this(orb);
        if (i2 == 21 || i2 == 30) {
            this._kind = i2;
            setId(str);
            this._name = str2;
            this._contentType = convertToNative(this._orb, typeCode);
        }
    }

    public TypeCodeImpl(ORB orb, int i2, String str, String str2) {
        this(orb);
        if (i2 == 14 || i2 == 31 || i2 == 32) {
            this._kind = i2;
            setId(str);
            this._name = str2;
        }
    }

    public TypeCodeImpl(ORB orb, int i2, int i3) {
        this(orb);
        if (i3 < 0) {
            throw this.wrapper.negativeBounds();
        }
        if (i2 == 18 || i2 == 27) {
            this._kind = i2;
            this._length = i3;
        }
    }

    public TypeCodeImpl(ORB orb, int i2, int i3, TypeCode typeCode) {
        this(orb);
        if (i2 == 19 || i2 == 20) {
            this._kind = i2;
            this._length = i3;
            this._contentType = convertToNative(this._orb, typeCode);
        }
    }

    public TypeCodeImpl(ORB orb, int i2, int i3, int i4) {
        this(orb);
        if (i2 == 19) {
            this._kind = i2;
            this._length = i3;
            this._parentOffset = i4;
        }
    }

    public TypeCodeImpl(ORB orb, String str) {
        this(orb);
        this._kind = -1;
        this._id = str;
        tryIndirectType();
    }

    public TypeCodeImpl(ORB orb, int i2, short s2, short s3) {
        this(orb);
        if (i2 == 28) {
            this._kind = i2;
            this._digits = s2;
            this._scale = s3;
        }
    }

    protected static TypeCodeImpl convertToNative(ORB orb, TypeCode typeCode) {
        if (typeCode instanceof TypeCodeImpl) {
            return (TypeCodeImpl) typeCode;
        }
        return new TypeCodeImpl(orb, typeCode);
    }

    public static CDROutputStream newOutputStream(ORB orb) {
        return OutputStreamFactory.newTypeCodeOutputStream(orb);
    }

    private TypeCodeImpl indirectType() {
        this._indirectType = tryIndirectType();
        if (this._indirectType == null) {
            throw this.wrapper.unresolvedRecursiveTypecode();
        }
        return this._indirectType;
    }

    private TypeCodeImpl tryIndirectType() {
        if (this._indirectType != null) {
            return this._indirectType;
        }
        setIndirectType(this._orb.getTypeCode(this._id));
        return this._indirectType;
    }

    private void setIndirectType(TypeCodeImpl typeCodeImpl) {
        this._indirectType = typeCodeImpl;
        if (this._indirectType != null) {
            try {
                this._id = this._indirectType.id();
            } catch (BadKind e2) {
                throw this.wrapper.badkindCannotOccur();
            }
        }
    }

    private void setId(String str) {
        this._id = str;
        if (this._orb instanceof TypeCodeFactory) {
            this._orb.setTypeCode(this._id, this);
        }
    }

    private void setParent(TypeCodeImpl typeCodeImpl) {
        this._parent = typeCodeImpl;
    }

    private TypeCodeImpl getParentAtLevel(int i2) {
        if (i2 == 0) {
            return this;
        }
        if (this._parent == null) {
            throw this.wrapper.unresolvedRecursiveTypecode();
        }
        return this._parent.getParentAtLevel(i2 - 1);
    }

    private TypeCodeImpl lazy_content_type() {
        TypeCodeImpl parentAtLevel;
        if (this._contentType == null && this._kind == 19 && this._parentOffset > 0 && this._parent != null && (parentAtLevel = getParentAtLevel(this._parentOffset)) != null && parentAtLevel._id != null) {
            this._contentType = new TypeCodeImpl(this._orb, parentAtLevel._id);
        }
        return this._contentType;
    }

    private TypeCode realType(TypeCode typeCode) {
        TypeCode typeCodeContent_type = typeCode;
        while (typeCodeContent_type.kind().value() == 21) {
            try {
                typeCodeContent_type = typeCodeContent_type.content_type();
            } catch (BadKind e2) {
                throw this.wrapper.badkindCannotOccur();
            }
        }
        return typeCodeContent_type;
    }

    @Override // org.omg.CORBA.TypeCode
    public final boolean equal(TypeCode typeCode) {
        if (typeCode == this) {
            return true;
        }
        try {
            if (this._kind == -1) {
                if (this._id == null || typeCode.id() == null) {
                    return this._id == null && typeCode.id() == null;
                }
                return this._id.equals(typeCode.id());
            }
            if (this._kind != typeCode.kind().value()) {
                return false;
            }
            switch (typeTable[this._kind]) {
                case 0:
                    return true;
                case 1:
                    switch (this._kind) {
                        case 18:
                        case 27:
                            return this._length == typeCode.length();
                        case 28:
                            return this._digits == typeCode.fixed_digits() && this._scale == typeCode.fixed_scale();
                        default:
                            return false;
                    }
                case 2:
                    switch (this._kind) {
                        case 14:
                            if (this._id.compareTo(typeCode.id()) == 0 || this._id.compareTo(this._orb.get_primitive_tc(this._kind).id()) == 0 || typeCode.id().compareTo(this._orb.get_primitive_tc(this._kind).id()) == 0) {
                                return true;
                            }
                            return false;
                        case 15:
                        case 22:
                            if (this._memberCount != typeCode.member_count() || this._id.compareTo(typeCode.id()) != 0) {
                                return false;
                            }
                            for (int i2 = 0; i2 < this._memberCount; i2++) {
                                if (!this._memberTypes[i2].equal(typeCode.member_type(i2))) {
                                    return false;
                                }
                            }
                            return true;
                        case 16:
                            if (this._memberCount != typeCode.member_count() || this._id.compareTo(typeCode.id()) != 0 || this._defaultIndex != typeCode.default_index() || !this._discriminator.equal(typeCode.discriminator_type())) {
                                return false;
                            }
                            for (int i3 = 0; i3 < this._memberCount; i3++) {
                                if (!this._unionLabels[i3].equal(typeCode.member_label(i3))) {
                                    return false;
                                }
                            }
                            for (int i4 = 0; i4 < this._memberCount; i4++) {
                                if (!this._memberTypes[i4].equal(typeCode.member_type(i4))) {
                                    return false;
                                }
                            }
                            return true;
                        case 17:
                            if (this._id.compareTo(typeCode.id()) != 0 || this._memberCount != typeCode.member_count()) {
                                return false;
                            }
                            return true;
                        case 18:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        default:
                            return false;
                        case 19:
                        case 20:
                            if (this._length != typeCode.length() || !lazy_content_type().equal(typeCode.content_type())) {
                                return false;
                            }
                            return true;
                        case 21:
                        case 30:
                            if (this._id.compareTo(typeCode.id()) != 0) {
                                return false;
                            }
                            return this._contentType.equal(typeCode.content_type());
                        case 29:
                            if (this._memberCount != typeCode.member_count() || this._id.compareTo(typeCode.id()) != 0) {
                                return false;
                            }
                            for (int i5 = 0; i5 < this._memberCount; i5++) {
                                if (this._memberAccess[i5] != typeCode.member_visibility(i5) || !this._memberTypes[i5].equal(typeCode.member_type(i5))) {
                                    return false;
                                }
                            }
                            if (this._type_modifier == typeCode.type_modifier()) {
                                return false;
                            }
                            TypeCode typeCodeConcrete_base_type = typeCode.concrete_base_type();
                            if (this._concrete_base != null || typeCodeConcrete_base_type == null) {
                                if ((this._concrete_base != null && typeCodeConcrete_base_type == null) || !this._concrete_base.equal(typeCodeConcrete_base_type)) {
                                    return false;
                                }
                                return true;
                            }
                            return false;
                        case 31:
                        case 32:
                            if (this._id.compareTo(typeCode.id()) != 0) {
                                return false;
                            }
                            return true;
                    }
                default:
                    return false;
            }
        } catch (BadKind e2) {
            return false;
        } catch (Bounds e3) {
            return false;
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public boolean equivalent(TypeCode typeCode) {
        if (typeCode == this) {
            return true;
        }
        TypeCode typeCodeRealType = realType(this._kind == -1 ? indirectType() : this);
        TypeCode typeCodeRealType2 = realType(typeCode);
        if (typeCodeRealType.kind().value() != typeCodeRealType2.kind().value()) {
            return false;
        }
        try {
            String strId = id();
            String strId2 = typeCode.id();
            if (strId != null && strId2 != null) {
                return strId.equals(strId2);
            }
        } catch (BadKind e2) {
        }
        int iValue = typeCodeRealType.kind().value();
        if (iValue == 15 || iValue == 16 || iValue == 17 || iValue == 22 || iValue == 29) {
            try {
                if (typeCodeRealType.member_count() != typeCodeRealType2.member_count()) {
                    return false;
                }
            } catch (BadKind e3) {
                throw this.wrapper.badkindCannotOccur();
            } catch (Bounds e4) {
                throw this.wrapper.boundsCannotOccur();
            }
        }
        if (iValue == 16 && typeCodeRealType.default_index() != typeCodeRealType2.default_index()) {
            return false;
        }
        if ((iValue == 18 || iValue == 27 || iValue == 19 || iValue == 20) && typeCodeRealType.length() != typeCodeRealType2.length()) {
            return false;
        }
        if (iValue == 28 && (typeCodeRealType.fixed_digits() != typeCodeRealType2.fixed_digits() || typeCodeRealType.fixed_scale() != typeCodeRealType2.fixed_scale())) {
            return false;
        }
        if (iValue == 16) {
            for (int i2 = 0; i2 < typeCodeRealType.member_count(); i2++) {
                if (typeCodeRealType.member_label(i2) != typeCodeRealType2.member_label(i2)) {
                    return false;
                }
            }
            if (!typeCodeRealType.discriminator_type().equivalent(typeCodeRealType2.discriminator_type())) {
                return false;
            }
        }
        if ((iValue == 21 || iValue == 30 || iValue == 19 || iValue == 20) && !typeCodeRealType.content_type().equivalent(typeCodeRealType2.content_type())) {
            return false;
        }
        if (iValue == 15 || iValue == 16 || iValue == 22 || iValue == 29) {
            for (int i3 = 0; i3 < typeCodeRealType.member_count(); i3++) {
                if (!typeCodeRealType.member_type(i3).equivalent(typeCodeRealType2.member_type(i3))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // org.omg.CORBA.TypeCode
    public TypeCode get_compact_typecode() {
        return this;
    }

    @Override // org.omg.CORBA.TypeCode
    public TCKind kind() {
        if (this._kind == -1) {
            return indirectType().kind();
        }
        return TCKind.from_int(this._kind);
    }

    public boolean is_recursive() {
        return this._kind == -1;
    }

    @Override // org.omg.CORBA.TypeCode
    public String id() throws BadKind {
        switch (this._kind) {
            case -1:
            case 14:
            case 15:
            case 16:
            case 17:
            case 21:
            case 22:
            case 29:
            case 30:
            case 31:
            case 32:
                return this._id;
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public String name() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().name();
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            default:
                throw new BadKind();
            case 14:
            case 15:
            case 16:
            case 17:
            case 21:
            case 22:
            case 29:
            case 30:
            case 31:
            case 32:
                return this._name;
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public int member_count() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().member_count();
            case 15:
            case 16:
            case 17:
            case 22:
            case 29:
                return this._memberCount;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public String member_name(int i2) throws BadKind, Bounds {
        switch (this._kind) {
            case -1:
                return indirectType().member_name(i2);
            case 15:
            case 16:
            case 17:
            case 22:
            case 29:
                try {
                    return this._memberNames[i2];
                } catch (ArrayIndexOutOfBoundsException e2) {
                    throw new Bounds();
                }
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public TypeCode member_type(int i2) throws BadKind, Bounds {
        switch (this._kind) {
            case -1:
                return indirectType().member_type(i2);
            case 15:
            case 16:
            case 22:
            case 29:
                try {
                    return this._memberTypes[i2];
                } catch (ArrayIndexOutOfBoundsException e2) {
                    throw new Bounds();
                }
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public Any member_label(int i2) throws BadKind, Bounds {
        switch (this._kind) {
            case -1:
                return indirectType().member_label(i2);
            case 16:
                try {
                    return new AnyImpl(this._orb, this._unionLabels[i2]);
                } catch (ArrayIndexOutOfBoundsException e2) {
                    throw new Bounds();
                }
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public TypeCode discriminator_type() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().discriminator_type();
            case 16:
                return this._discriminator;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public int default_index() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().default_index();
            case 16:
                return this._defaultIndex;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public int length() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().length();
            case 18:
            case 19:
            case 20:
            case 27:
                return this._length;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public TypeCode content_type() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().content_type();
            case 19:
                return lazy_content_type();
            case 20:
            case 21:
            case 30:
                return this._contentType;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public short fixed_digits() throws BadKind {
        switch (this._kind) {
            case 28:
                return this._digits;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public short fixed_scale() throws BadKind {
        switch (this._kind) {
            case 28:
                return this._scale;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public short member_visibility(int i2) throws BadKind, Bounds {
        switch (this._kind) {
            case -1:
                return indirectType().member_visibility(i2);
            case 29:
                try {
                    return this._memberAccess[i2];
                } catch (ArrayIndexOutOfBoundsException e2) {
                    throw new Bounds();
                }
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public short type_modifier() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().type_modifier();
            case 29:
                return this._type_modifier;
            default:
                throw new BadKind();
        }
    }

    @Override // org.omg.CORBA.TypeCode
    public TypeCode concrete_base_type() throws BadKind {
        switch (this._kind) {
            case -1:
                return indirectType().concrete_base_type();
            case 29:
                return this._concrete_base;
            default:
                throw new BadKind();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void read_value(InputStream inputStream) {
        if (inputStream instanceof TypeCodeReader) {
            if (read_value_kind((TypeCodeReader) inputStream)) {
                read_value_body(inputStream);
            }
        } else {
            if (inputStream instanceof CDRInputStream) {
                WrapperInputStream wrapperInputStream = new WrapperInputStream((CDRInputStream) inputStream);
                if (read_value_kind((TypeCodeReader) wrapperInputStream)) {
                    read_value_body(wrapperInputStream);
                    return;
                }
                return;
            }
            read_value_kind(inputStream);
            read_value_body(inputStream);
        }
    }

    private void read_value_recursive(TypeCodeInputStream typeCodeInputStream) {
        if (typeCodeInputStream instanceof TypeCodeReader) {
            if (read_value_kind((TypeCodeReader) typeCodeInputStream)) {
                read_value_body(typeCodeInputStream);
            }
        } else {
            read_value_kind((InputStream) typeCodeInputStream);
            read_value_body(typeCodeInputStream);
        }
    }

    boolean read_value_kind(TypeCodeReader typeCodeReader) {
        this._kind = typeCodeReader.read_long();
        int topLevelPosition = typeCodeReader.getTopLevelPosition() - 4;
        if ((this._kind < 0 || this._kind > typeTable.length) && this._kind != -1) {
            throw this.wrapper.cannotMarshalBadTckind();
        }
        if (this._kind == 31) {
            throw this.wrapper.cannotMarshalNative();
        }
        TypeCodeReader topLevelStream = typeCodeReader.getTopLevelStream();
        if (this._kind == -1) {
            int i2 = typeCodeReader.read_long();
            if (i2 > -4) {
                throw this.wrapper.invalidIndirection(new Integer(i2));
            }
            int topLevelPosition2 = (typeCodeReader.getTopLevelPosition() - 4) + i2;
            TypeCodeImpl typeCodeAtPosition = topLevelStream.getTypeCodeAtPosition(topLevelPosition2);
            if (typeCodeAtPosition == null) {
                throw this.wrapper.indirectionNotFound(new Integer(topLevelPosition2));
            }
            setIndirectType(typeCodeAtPosition);
            return false;
        }
        topLevelStream.addTypeCodeAtPosition(this, topLevelPosition);
        return true;
    }

    void read_value_kind(InputStream inputStream) {
        this._kind = inputStream.read_long();
        if ((this._kind < 0 || this._kind > typeTable.length) && this._kind != -1) {
            throw this.wrapper.cannotMarshalBadTckind();
        }
        if (this._kind == 31) {
            throw this.wrapper.cannotMarshalNative();
        }
        if (this._kind == -1) {
            throw this.wrapper.recursiveTypecodeError();
        }
    }

    void read_value_body(InputStream inputStream) {
        switch (typeTable[this._kind]) {
            case 0:
            default:
                return;
            case 1:
                switch (this._kind) {
                    case 18:
                    case 27:
                        this._length = inputStream.read_long();
                        return;
                    case 28:
                        this._digits = inputStream.read_ushort();
                        this._scale = inputStream.read_short();
                        return;
                    default:
                        throw this.wrapper.invalidSimpleTypecode();
                }
            case 2:
                TypeCodeInputStream encapsulation = TypeCodeInputStream.readEncapsulation(inputStream, inputStream.orb());
                switch (this._kind) {
                    case 14:
                    case 32:
                        setId(encapsulation.read_string());
                        this._name = encapsulation.read_string();
                        return;
                    case 15:
                    case 22:
                        setId(encapsulation.read_string());
                        this._name = encapsulation.read_string();
                        this._memberCount = encapsulation.read_long();
                        this._memberNames = new String[this._memberCount];
                        this._memberTypes = new TypeCodeImpl[this._memberCount];
                        for (int i2 = 0; i2 < this._memberCount; i2++) {
                            this._memberNames[i2] = encapsulation.read_string();
                            this._memberTypes[i2] = new TypeCodeImpl((ORB) inputStream.orb());
                            this._memberTypes[i2].read_value_recursive(encapsulation);
                            this._memberTypes[i2].setParent(this);
                        }
                        return;
                    case 16:
                        setId(encapsulation.read_string());
                        this._name = encapsulation.read_string();
                        this._discriminator = new TypeCodeImpl((ORB) inputStream.orb());
                        this._discriminator.read_value_recursive(encapsulation);
                        this._defaultIndex = encapsulation.read_long();
                        this._memberCount = encapsulation.read_long();
                        this._unionLabels = new AnyImpl[this._memberCount];
                        this._memberNames = new String[this._memberCount];
                        this._memberTypes = new TypeCodeImpl[this._memberCount];
                        for (int i3 = 0; i3 < this._memberCount; i3++) {
                            this._unionLabels[i3] = new AnyImpl((ORB) inputStream.orb());
                            if (i3 == this._defaultIndex) {
                                this._unionLabels[i3].insert_octet(encapsulation.read_octet());
                            } else {
                                switch (realType(this._discriminator).kind().value()) {
                                    case 2:
                                        this._unionLabels[i3].insert_short(encapsulation.read_short());
                                        break;
                                    case 3:
                                        this._unionLabels[i3].insert_long(encapsulation.read_long());
                                        break;
                                    case 4:
                                        this._unionLabels[i3].insert_ushort(encapsulation.read_short());
                                        break;
                                    case 5:
                                        this._unionLabels[i3].insert_ulong(encapsulation.read_long());
                                        break;
                                    case 6:
                                        this._unionLabels[i3].insert_float(encapsulation.read_float());
                                        break;
                                    case 7:
                                        this._unionLabels[i3].insert_double(encapsulation.read_double());
                                        break;
                                    case 8:
                                        this._unionLabels[i3].insert_boolean(encapsulation.read_boolean());
                                        break;
                                    case 9:
                                        this._unionLabels[i3].insert_char(encapsulation.read_char());
                                        break;
                                    case 10:
                                    case 11:
                                    case 12:
                                    case 13:
                                    case 14:
                                    case 15:
                                    case 16:
                                    case 18:
                                    case 19:
                                    case 20:
                                    case 21:
                                    case 22:
                                    case 25:
                                    default:
                                        throw this.wrapper.invalidComplexTypecode();
                                    case 17:
                                        this._unionLabels[i3].type(this._discriminator);
                                        this._unionLabels[i3].insert_long(encapsulation.read_long());
                                        break;
                                    case 23:
                                        this._unionLabels[i3].insert_longlong(encapsulation.read_longlong());
                                        break;
                                    case 24:
                                        this._unionLabels[i3].insert_ulonglong(encapsulation.read_longlong());
                                        break;
                                    case 26:
                                        this._unionLabels[i3].insert_wchar(encapsulation.read_wchar());
                                        break;
                                }
                            }
                            this._memberNames[i3] = encapsulation.read_string();
                            this._memberTypes[i3] = new TypeCodeImpl((ORB) inputStream.orb());
                            this._memberTypes[i3].read_value_recursive(encapsulation);
                            this._memberTypes[i3].setParent(this);
                        }
                        return;
                    case 17:
                        setId(encapsulation.read_string());
                        this._name = encapsulation.read_string();
                        this._memberCount = encapsulation.read_long();
                        this._memberNames = new String[this._memberCount];
                        for (int i4 = 0; i4 < this._memberCount; i4++) {
                            this._memberNames[i4] = encapsulation.read_string();
                        }
                        return;
                    case 18:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 31:
                    default:
                        throw this.wrapper.invalidTypecodeKindMarshal();
                    case 19:
                        this._contentType = new TypeCodeImpl((ORB) inputStream.orb());
                        this._contentType.read_value_recursive(encapsulation);
                        this._length = encapsulation.read_long();
                        return;
                    case 20:
                        this._contentType = new TypeCodeImpl((ORB) inputStream.orb());
                        this._contentType.read_value_recursive(encapsulation);
                        this._length = encapsulation.read_long();
                        return;
                    case 21:
                    case 30:
                        setId(encapsulation.read_string());
                        this._name = encapsulation.read_string();
                        this._contentType = new TypeCodeImpl((ORB) inputStream.orb());
                        this._contentType.read_value_recursive(encapsulation);
                        return;
                    case 29:
                        setId(encapsulation.read_string());
                        this._name = encapsulation.read_string();
                        this._type_modifier = encapsulation.read_short();
                        this._concrete_base = new TypeCodeImpl((ORB) inputStream.orb());
                        this._concrete_base.read_value_recursive(encapsulation);
                        if (this._concrete_base.kind().value() == 0) {
                            this._concrete_base = null;
                        }
                        this._memberCount = encapsulation.read_long();
                        this._memberNames = new String[this._memberCount];
                        this._memberTypes = new TypeCodeImpl[this._memberCount];
                        this._memberAccess = new short[this._memberCount];
                        for (int i5 = 0; i5 < this._memberCount; i5++) {
                            this._memberNames[i5] = encapsulation.read_string();
                            this._memberTypes[i5] = new TypeCodeImpl((ORB) inputStream.orb());
                            this._memberTypes[i5].read_value_recursive(encapsulation);
                            this._memberTypes[i5].setParent(this);
                            this._memberAccess[i5] = encapsulation.read_short();
                        }
                        return;
                }
        }
    }

    public void write_value(OutputStream outputStream) {
        if (outputStream instanceof TypeCodeOutputStream) {
            write_value((TypeCodeOutputStream) outputStream);
            return;
        }
        TypeCodeOutputStream typeCodeOutputStreamWrapOutputStream = null;
        if (this.outBuffer == null) {
            typeCodeOutputStreamWrapOutputStream = TypeCodeOutputStream.wrapOutputStream(outputStream);
            write_value(typeCodeOutputStreamWrapOutputStream);
            if (this.cachingEnabled) {
                this.outBuffer = typeCodeOutputStreamWrapOutputStream.getTypeCodeBuffer();
            }
        }
        if (this.cachingEnabled && this.outBuffer != null) {
            outputStream.write_long(this._kind);
            outputStream.write_octet_array(this.outBuffer, 0, this.outBuffer.length);
        } else {
            typeCodeOutputStreamWrapOutputStream.writeRawBuffer(outputStream, this._kind);
        }
    }

    public void write_value(TypeCodeOutputStream typeCodeOutputStream) {
        if (this._kind == 31) {
            throw this.wrapper.cannotMarshalNative();
        }
        TypeCodeOutputStream topLevelStream = typeCodeOutputStream.getTopLevelStream();
        if (this._kind == -1) {
            int positionForID = topLevelStream.getPositionForID(this._id);
            typeCodeOutputStream.getTopLevelPosition();
            typeCodeOutputStream.writeIndirection(-1, positionForID);
            return;
        }
        typeCodeOutputStream.write_long(this._kind);
        topLevelStream.addIDAtPosition(this._id, typeCodeOutputStream.getTopLevelPosition() - 4);
        switch (typeTable[this._kind]) {
            case 0:
            default:
                return;
            case 1:
                switch (this._kind) {
                    case 18:
                    case 27:
                        typeCodeOutputStream.write_long(this._length);
                        return;
                    case 28:
                        typeCodeOutputStream.write_ushort(this._digits);
                        typeCodeOutputStream.write_short(this._scale);
                        return;
                    default:
                        throw this.wrapper.invalidSimpleTypecode();
                }
            case 2:
                TypeCodeOutputStream typeCodeOutputStreamCreateEncapsulation = typeCodeOutputStream.createEncapsulation(typeCodeOutputStream.orb());
                switch (this._kind) {
                    case 14:
                    case 32:
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._id);
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._name);
                        break;
                    case 15:
                    case 22:
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._id);
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._name);
                        typeCodeOutputStreamCreateEncapsulation.write_long(this._memberCount);
                        for (int i2 = 0; i2 < this._memberCount; i2++) {
                            typeCodeOutputStreamCreateEncapsulation.write_string(this._memberNames[i2]);
                            this._memberTypes[i2].write_value(typeCodeOutputStreamCreateEncapsulation);
                        }
                        break;
                    case 16:
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._id);
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._name);
                        this._discriminator.write_value(typeCodeOutputStreamCreateEncapsulation);
                        typeCodeOutputStreamCreateEncapsulation.write_long(this._defaultIndex);
                        typeCodeOutputStreamCreateEncapsulation.write_long(this._memberCount);
                        for (int i3 = 0; i3 < this._memberCount; i3++) {
                            if (i3 == this._defaultIndex) {
                                typeCodeOutputStreamCreateEncapsulation.write_octet(this._unionLabels[i3].extract_octet());
                            } else {
                                switch (realType(this._discriminator).kind().value()) {
                                    case 2:
                                        typeCodeOutputStreamCreateEncapsulation.write_short(this._unionLabels[i3].extract_short());
                                        break;
                                    case 3:
                                        typeCodeOutputStreamCreateEncapsulation.write_long(this._unionLabels[i3].extract_long());
                                        break;
                                    case 4:
                                        typeCodeOutputStreamCreateEncapsulation.write_short(this._unionLabels[i3].extract_ushort());
                                        break;
                                    case 5:
                                        typeCodeOutputStreamCreateEncapsulation.write_long(this._unionLabels[i3].extract_ulong());
                                        break;
                                    case 6:
                                        typeCodeOutputStreamCreateEncapsulation.write_float(this._unionLabels[i3].extract_float());
                                        break;
                                    case 7:
                                        typeCodeOutputStreamCreateEncapsulation.write_double(this._unionLabels[i3].extract_double());
                                        break;
                                    case 8:
                                        typeCodeOutputStreamCreateEncapsulation.write_boolean(this._unionLabels[i3].extract_boolean());
                                        break;
                                    case 9:
                                        typeCodeOutputStreamCreateEncapsulation.write_char(this._unionLabels[i3].extract_char());
                                        break;
                                    case 10:
                                    case 11:
                                    case 12:
                                    case 13:
                                    case 14:
                                    case 15:
                                    case 16:
                                    case 18:
                                    case 19:
                                    case 20:
                                    case 21:
                                    case 22:
                                    case 25:
                                    default:
                                        throw this.wrapper.invalidComplexTypecode();
                                    case 17:
                                        typeCodeOutputStreamCreateEncapsulation.write_long(this._unionLabels[i3].extract_long());
                                        break;
                                    case 23:
                                        typeCodeOutputStreamCreateEncapsulation.write_longlong(this._unionLabels[i3].extract_longlong());
                                        break;
                                    case 24:
                                        typeCodeOutputStreamCreateEncapsulation.write_longlong(this._unionLabels[i3].extract_ulonglong());
                                        break;
                                    case 26:
                                        typeCodeOutputStreamCreateEncapsulation.write_wchar(this._unionLabels[i3].extract_wchar());
                                        break;
                                }
                            }
                            typeCodeOutputStreamCreateEncapsulation.write_string(this._memberNames[i3]);
                            this._memberTypes[i3].write_value(typeCodeOutputStreamCreateEncapsulation);
                        }
                        break;
                    case 17:
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._id);
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._name);
                        typeCodeOutputStreamCreateEncapsulation.write_long(this._memberCount);
                        for (int i4 = 0; i4 < this._memberCount; i4++) {
                            typeCodeOutputStreamCreateEncapsulation.write_string(this._memberNames[i4]);
                        }
                        break;
                    case 18:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 31:
                    default:
                        throw this.wrapper.invalidTypecodeKindMarshal();
                    case 19:
                        lazy_content_type().write_value(typeCodeOutputStreamCreateEncapsulation);
                        typeCodeOutputStreamCreateEncapsulation.write_long(this._length);
                        break;
                    case 20:
                        this._contentType.write_value(typeCodeOutputStreamCreateEncapsulation);
                        typeCodeOutputStreamCreateEncapsulation.write_long(this._length);
                        break;
                    case 21:
                    case 30:
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._id);
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._name);
                        this._contentType.write_value(typeCodeOutputStreamCreateEncapsulation);
                        break;
                    case 29:
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._id);
                        typeCodeOutputStreamCreateEncapsulation.write_string(this._name);
                        typeCodeOutputStreamCreateEncapsulation.write_short(this._type_modifier);
                        if (this._concrete_base == null) {
                            this._orb.get_primitive_tc(0).write_value(typeCodeOutputStreamCreateEncapsulation);
                        } else {
                            this._concrete_base.write_value(typeCodeOutputStreamCreateEncapsulation);
                        }
                        typeCodeOutputStreamCreateEncapsulation.write_long(this._memberCount);
                        for (int i5 = 0; i5 < this._memberCount; i5++) {
                            typeCodeOutputStreamCreateEncapsulation.write_string(this._memberNames[i5]);
                            this._memberTypes[i5].write_value(typeCodeOutputStreamCreateEncapsulation);
                            typeCodeOutputStreamCreateEncapsulation.write_short(this._memberAccess[i5]);
                        }
                        break;
                }
                typeCodeOutputStreamCreateEncapsulation.writeOctetSequenceTo(typeCodeOutputStream);
                return;
        }
    }

    protected void copy(org.omg.CORBA.portable.InputStream inputStream, org.omg.CORBA.portable.OutputStream outputStream) throws MARSHAL, DATA_CONVERSION {
        switch (this._kind) {
            case -1:
                indirectType().copy(inputStream, outputStream);
                return;
            case 0:
            case 1:
            case 31:
            case 32:
                return;
            case 2:
            case 4:
                outputStream.write_short(inputStream.read_short());
                return;
            case 3:
            case 5:
                outputStream.write_long(inputStream.read_long());
                return;
            case 6:
                outputStream.write_float(inputStream.read_float());
                return;
            case 7:
                outputStream.write_double(inputStream.read_double());
                return;
            case 8:
                outputStream.write_boolean(inputStream.read_boolean());
                return;
            case 9:
                outputStream.write_char(inputStream.read_char());
                return;
            case 10:
                outputStream.write_octet(inputStream.read_octet());
                return;
            case 11:
                Any anyCreate_any = ((CDRInputStream) inputStream).orb().create_any();
                TypeCodeImpl typeCodeImpl = new TypeCodeImpl((ORB) outputStream.orb());
                typeCodeImpl.read_value((InputStream) inputStream);
                typeCodeImpl.write_value((OutputStream) outputStream);
                anyCreate_any.read_value(inputStream, typeCodeImpl);
                anyCreate_any.write_value(outputStream);
                return;
            case 12:
                outputStream.write_TypeCode(inputStream.read_TypeCode());
                return;
            case 13:
                outputStream.write_Principal(inputStream.read_Principal());
                return;
            case 14:
                outputStream.write_Object(inputStream.read_Object());
                return;
            case 15:
            case 29:
                break;
            case 16:
                AnyImpl anyImpl = new AnyImpl((ORB) inputStream.orb());
                switch (realType(this._discriminator).kind().value()) {
                    case 2:
                        short s2 = inputStream.read_short();
                        anyImpl.insert_short(s2);
                        outputStream.write_short(s2);
                        break;
                    case 3:
                        int i2 = inputStream.read_long();
                        anyImpl.insert_long(i2);
                        outputStream.write_long(i2);
                        break;
                    case 4:
                        short s3 = inputStream.read_short();
                        anyImpl.insert_ushort(s3);
                        outputStream.write_short(s3);
                        break;
                    case 5:
                        int i3 = inputStream.read_long();
                        anyImpl.insert_ulong(i3);
                        outputStream.write_long(i3);
                        break;
                    case 6:
                        float f2 = inputStream.read_float();
                        anyImpl.insert_float(f2);
                        outputStream.write_float(f2);
                        break;
                    case 7:
                        double d2 = inputStream.read_double();
                        anyImpl.insert_double(d2);
                        outputStream.write_double(d2);
                        break;
                    case 8:
                        boolean z2 = inputStream.read_boolean();
                        anyImpl.insert_boolean(z2);
                        outputStream.write_boolean(z2);
                        break;
                    case 9:
                        char c2 = inputStream.read_char();
                        anyImpl.insert_char(c2);
                        outputStream.write_char(c2);
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 25:
                    default:
                        throw this.wrapper.illegalUnionDiscriminatorType();
                    case 17:
                        int i4 = inputStream.read_long();
                        anyImpl.type(this._discriminator);
                        anyImpl.insert_long(i4);
                        outputStream.write_long(i4);
                        break;
                    case 23:
                        long j2 = inputStream.read_longlong();
                        anyImpl.insert_longlong(j2);
                        outputStream.write_longlong(j2);
                        break;
                    case 24:
                        long j3 = inputStream.read_longlong();
                        anyImpl.insert_ulonglong(j3);
                        outputStream.write_longlong(j3);
                        break;
                    case 26:
                        char c3 = inputStream.read_wchar();
                        anyImpl.insert_wchar(c3);
                        outputStream.write_wchar(c3);
                        break;
                }
                int i5 = 0;
                while (true) {
                    if (i5 < this._unionLabels.length) {
                        if (!anyImpl.equal(this._unionLabels[i5])) {
                            i5++;
                        } else {
                            this._memberTypes[i5].copy(inputStream, outputStream);
                        }
                    }
                }
                if (i5 == this._unionLabels.length && this._defaultIndex != -1) {
                    this._memberTypes[this._defaultIndex].copy(inputStream, outputStream);
                    return;
                }
                return;
            case 17:
                outputStream.write_long(inputStream.read_long());
                return;
            case 18:
                String str = inputStream.read_string();
                if (this._length != 0 && str.length() > this._length) {
                    throw this.wrapper.badStringBounds(new Integer(str.length()), new Integer(this._length));
                }
                outputStream.write_string(str);
                return;
            case 19:
                int i6 = inputStream.read_long();
                if (this._length != 0 && i6 > this._length) {
                    throw this.wrapper.badSequenceBounds(new Integer(i6), new Integer(this._length));
                }
                outputStream.write_long(i6);
                lazy_content_type();
                for (int i7 = 0; i7 < i6; i7++) {
                    this._contentType.copy(inputStream, outputStream);
                }
                return;
            case 20:
                for (int i8 = 0; i8 < this._length; i8++) {
                    this._contentType.copy(inputStream, outputStream);
                }
                return;
            case 21:
            case 30:
                this._contentType.copy(inputStream, outputStream);
                return;
            case 22:
                outputStream.write_string(inputStream.read_string());
                break;
            case 23:
            case 24:
                outputStream.write_longlong(inputStream.read_longlong());
                return;
            case 25:
                throw this.wrapper.tkLongDoubleNotSupported();
            case 26:
                outputStream.write_wchar(inputStream.read_wchar());
                return;
            case 27:
                String str2 = inputStream.read_wstring();
                if (this._length != 0 && str2.length() > this._length) {
                    throw this.wrapper.badStringBounds(new Integer(str2.length()), new Integer(this._length));
                }
                outputStream.write_wstring(str2);
                return;
            case 28:
                outputStream.write_ushort(inputStream.read_ushort());
                outputStream.write_short(inputStream.read_short());
                return;
            default:
                throw this.wrapper.invalidTypecodeKindMarshal();
        }
        for (int i9 = 0; i9 < this._memberTypes.length; i9++) {
            this._memberTypes[i9].copy(inputStream, outputStream);
        }
    }

    protected static short digits(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return (short) 0;
        }
        short length = (short) bigDecimal.unscaledValue().toString().length();
        if (bigDecimal.signum() == -1) {
            length = (short) (length - 1);
        }
        return length;
    }

    protected static short scale(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return (short) 0;
        }
        return (short) bigDecimal.scale();
    }

    int currentUnionMemberIndex(Any any) throws BadKind {
        if (this._kind != 16) {
            throw new BadKind();
        }
        for (int i2 = 0; i2 < member_count(); i2++) {
            try {
                if (member_label(i2).equal(any)) {
                    return i2;
                }
            } catch (BadKind e2) {
                return -1;
            } catch (Bounds e3) {
                return -1;
            }
        }
        if (this._defaultIndex != -1) {
            return this._defaultIndex;
        }
        return -1;
    }

    public String description() {
        return "TypeCodeImpl with kind " + this._kind + " and id " + this._id;
    }

    public String toString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        printStream(new PrintStream((java.io.OutputStream) byteArrayOutputStream, true));
        return super.toString() + " =\n" + byteArrayOutputStream.toString();
    }

    public void printStream(PrintStream printStream) {
        printStream(printStream, 0);
    }

    private void printStream(PrintStream printStream, int i2) {
        if (this._kind == -1) {
            printStream.print("indirect " + this._id);
        }
        switch (this._kind) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 23:
            case 24:
            case 25:
            case 26:
            case 31:
                printStream.print(kindNames[this._kind] + " " + this._name);
                break;
            case 15:
            case 22:
            case 29:
                printStream.println(kindNames[this._kind] + " " + this._name + " = {");
                for (int i3 = 0; i3 < this._memberCount; i3++) {
                    printStream.print(indent(i2 + 1));
                    if (this._memberTypes[i3] != null) {
                        this._memberTypes[i3].printStream(printStream, i2 + 1);
                    } else {
                        printStream.print("<unknown type>");
                    }
                    printStream.println(" " + this._memberNames[i3] + ";");
                }
                printStream.print(indent(i2) + "}");
                break;
            case 16:
                printStream.print("union " + this._name + "...");
                break;
            case 17:
                printStream.print("enum " + this._name + "...");
                break;
            case 18:
                if (this._length == 0) {
                    printStream.print("unbounded string " + this._name);
                    break;
                } else {
                    printStream.print("bounded string(" + this._length + ") " + this._name);
                    break;
                }
            case 19:
            case 20:
                printStream.println(kindNames[this._kind] + "[" + this._length + "] " + this._name + " = {");
                printStream.print(indent(i2 + 1));
                if (lazy_content_type() != null) {
                    lazy_content_type().printStream(printStream, i2 + 1);
                }
                printStream.println(indent(i2) + "}");
                break;
            case 21:
                printStream.print("alias " + this._name + " = " + (this._contentType != null ? this._contentType._name : "<unresolved>"));
                break;
            case 27:
                printStream.print("wstring[" + this._length + "] " + this._name);
                break;
            case 28:
                printStream.print("fixed(" + ((int) this._digits) + ", " + ((int) this._scale) + ") " + this._name);
                break;
            case 30:
                printStream.print("valueBox " + this._name + "...");
                break;
            case 32:
                printStream.print("abstractInterface " + this._name + "...");
                break;
            default:
                printStream.print("<unknown type>");
                break;
        }
    }

    private String indent(int i2) {
        String str = "";
        for (int i3 = 0; i3 < i2; i3++) {
            str = str + Constants.INDENT;
        }
        return str;
    }

    protected void setCaching(boolean z2) {
        this.cachingEnabled = z2;
        if (!z2) {
            this.outBuffer = null;
        }
    }
}
