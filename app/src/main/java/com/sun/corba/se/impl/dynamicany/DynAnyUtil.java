package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.impl.corba.AnyImpl;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.util.Version;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.math.BigDecimal;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynAnyUtil.class */
public class DynAnyUtil {
    static boolean isConsistentType(TypeCode typeCode) {
        int iValue = typeCode.kind().value();
        return (iValue == 13 || iValue == 31 || iValue == 32) ? false : true;
    }

    static boolean isConstructedDynAny(DynAny dynAny) {
        int iValue = dynAny.type().kind().value();
        return iValue == 19 || iValue == 15 || iValue == 20 || iValue == 16 || iValue == 17 || iValue == 28 || iValue == 29 || iValue == 30;
    }

    static DynAny createMostDerivedDynAny(Any any, ORB orb, boolean z2) throws InconsistentTypeCode {
        if (any == null || !isConsistentType(any.type())) {
            throw new InconsistentTypeCode();
        }
        switch (any.type().kind().value()) {
            case 15:
                return new DynStructImpl(orb, any, z2);
            case 16:
                return new DynUnionImpl(orb, any, z2);
            case 17:
                return new DynEnumImpl(orb, any, z2);
            case 18:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
                return new DynAnyBasicImpl(orb, any, z2);
            case 19:
                return new DynSequenceImpl(orb, any, z2);
            case 20:
                return new DynArrayImpl(orb, any, z2);
            case 28:
                return new DynFixedImpl(orb, any, z2);
            case 29:
                return new DynValueImpl(orb, any, z2);
            case 30:
                return new DynValueBoxImpl(orb, any, z2);
        }
    }

    static DynAny createMostDerivedDynAny(TypeCode typeCode, ORB orb) throws InconsistentTypeCode {
        if (typeCode == null || !isConsistentType(typeCode)) {
            throw new InconsistentTypeCode();
        }
        switch (typeCode.kind().value()) {
            case 15:
                return new DynStructImpl(orb, typeCode);
            case 16:
                return new DynUnionImpl(orb, typeCode);
            case 17:
                return new DynEnumImpl(orb, typeCode);
            case 18:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
                return new DynAnyBasicImpl(orb, typeCode);
            case 19:
                return new DynSequenceImpl(orb, typeCode);
            case 20:
                return new DynArrayImpl(orb, typeCode);
            case 28:
                return new DynFixedImpl(orb, typeCode);
            case 29:
                return new DynValueImpl(orb, typeCode);
            case 30:
                return new DynValueBoxImpl(orb, typeCode);
        }
    }

    static Any extractAnyFromStream(TypeCode typeCode, InputStream inputStream, ORB orb) {
        return AnyImpl.extractAnyFromStream(typeCode, inputStream, orb);
    }

    static Any createDefaultAnyOfType(TypeCode typeCode, ORB orb) throws BAD_INV_ORDER, MARSHAL, DATA_CONVERSION {
        ORBUtilSystemException oRBUtilSystemException = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PRESENTATION);
        Any anyCreate_any = orb.create_any();
        switch (typeCode.kind().value()) {
            case 0:
                break;
            case 1:
            case 13:
            case 21:
            case 31:
            case 32:
                anyCreate_any.type(typeCode);
                break;
            case 2:
                anyCreate_any.insert_short((short) 0);
                break;
            case 3:
                anyCreate_any.insert_long(0);
                break;
            case 4:
                anyCreate_any.insert_ushort((short) 0);
                break;
            case 5:
                anyCreate_any.insert_ulong(0);
                break;
            case 6:
                anyCreate_any.insert_float(0.0f);
                break;
            case 7:
                anyCreate_any.insert_double(0.0d);
                break;
            case 8:
                anyCreate_any.insert_boolean(false);
                break;
            case 9:
                anyCreate_any.insert_char((char) 0);
                break;
            case 10:
                anyCreate_any.insert_octet((byte) 0);
                break;
            case 11:
                anyCreate_any.insert_any(orb.create_any());
                break;
            case 12:
                anyCreate_any.insert_TypeCode(anyCreate_any.type());
                break;
            case 14:
                anyCreate_any.insert_Object(null);
                break;
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            case 22:
            case 29:
            case 30:
                anyCreate_any.type(typeCode);
                break;
            case 18:
                anyCreate_any.type(typeCode);
                anyCreate_any.insert_string("");
                break;
            case 23:
                anyCreate_any.insert_longlong(0L);
                break;
            case 24:
                anyCreate_any.insert_ulonglong(0L);
                break;
            case 25:
                throw oRBUtilSystemException.tkLongDoubleNotSupported();
            case 26:
                anyCreate_any.insert_wchar((char) 0);
                break;
            case 27:
                anyCreate_any.type(typeCode);
                anyCreate_any.insert_wstring("");
                break;
            case 28:
                anyCreate_any.insert_fixed(new BigDecimal(Version.BUILD), typeCode);
                break;
            default:
                throw oRBUtilSystemException.typecodeNotSupported();
        }
        return anyCreate_any;
    }

    static Any copy(Any any, ORB orb) {
        return new AnyImpl(orb, any);
    }

    static DynAny convertToNative(DynAny dynAny, ORB orb) {
        if (dynAny instanceof DynAnyImpl) {
            return dynAny;
        }
        try {
            return createMostDerivedDynAny(dynAny.to_any(), orb, true);
        } catch (InconsistentTypeCode e2) {
            return null;
        }
    }

    static boolean isInitialized(Any any) {
        boolean zIsInitialized = ((AnyImpl) any).isInitialized();
        switch (any.type().kind().value()) {
            case 18:
                return zIsInitialized && any.extract_string() != null;
            case 27:
                return zIsInitialized && any.extract_wstring() != null;
            default:
                return zIsInitialized;
        }
    }

    static boolean set_current_component(DynAny dynAny, DynAny dynAny2) {
        if (dynAny2 != null) {
            try {
                dynAny.rewind();
                while (dynAny.current_component() != dynAny2) {
                    if (!dynAny.next()) {
                        return false;
                    }
                }
                return true;
            } catch (TypeMismatch e2) {
                return false;
            }
        }
        return false;
    }
}
