package com.sun.corba.se.impl.corba;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import java.math.BigDecimal;
import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/TCUtility.class */
public final class TCUtility {
    static void marshalIn(OutputStream outputStream, TypeCode typeCode, long j2, Object obj) {
        switch (typeCode.kind().value()) {
            case 0:
            case 1:
            case 31:
                return;
            case 2:
                outputStream.write_short((short) (j2 & 65535));
                return;
            case 3:
            case 17:
                outputStream.write_long((int) (j2 & 4294967295L));
                return;
            case 4:
                outputStream.write_ushort((short) (j2 & 65535));
                return;
            case 5:
                outputStream.write_ulong((int) (j2 & 4294967295L));
                return;
            case 6:
                outputStream.write_float(Float.intBitsToFloat((int) (j2 & 4294967295L)));
                return;
            case 7:
                outputStream.write_double(Double.longBitsToDouble(j2));
                return;
            case 8:
                if (j2 == 0) {
                    outputStream.write_boolean(false);
                    return;
                } else {
                    outputStream.write_boolean(true);
                    return;
                }
            case 9:
                outputStream.write_char((char) (j2 & 65535));
                return;
            case 10:
                outputStream.write_octet((byte) (j2 & 255));
                return;
            case 11:
                outputStream.write_any((Any) obj);
                return;
            case 12:
                outputStream.write_TypeCode((TypeCode) obj);
                return;
            case 13:
                outputStream.write_Principal((Principal) obj);
                return;
            case 14:
                outputStream.write_Object((Object) obj);
                return;
            case 15:
            case 16:
            case 19:
            case 20:
            case 21:
            case 22:
                ((Streamable) obj)._write(outputStream);
                return;
            case 18:
                outputStream.write_string((String) obj);
                return;
            case 23:
                outputStream.write_longlong(j2);
                return;
            case 24:
                outputStream.write_ulonglong(j2);
                return;
            case 25:
            default:
                throw ORBUtilSystemException.get((ORB) outputStream.orb(), CORBALogDomains.RPC_PRESENTATION).typecodeNotSupported();
            case 26:
                outputStream.write_wchar((char) (j2 & 65535));
                return;
            case 27:
                outputStream.write_wstring((String) obj);
                return;
            case 28:
                if (outputStream instanceof CDROutputStream) {
                    try {
                        ((CDROutputStream) outputStream).write_fixed((BigDecimal) obj, typeCode.fixed_digits(), typeCode.fixed_scale());
                        return;
                    } catch (BadKind e2) {
                        return;
                    }
                } else {
                    outputStream.write_fixed((BigDecimal) obj);
                    return;
                }
            case 29:
            case 30:
                ((org.omg.CORBA_2_3.portable.OutputStream) outputStream).write_value((Serializable) obj);
                return;
            case 32:
                ((org.omg.CORBA_2_3.portable.OutputStream) outputStream).write_abstract_interface(obj);
                return;
        }
    }

    static void unmarshalIn(InputStream inputStream, TypeCode typeCode, long[] jArr, Object[] objArr) {
        int iValue = typeCode.kind().value();
        long jDoubleToLongBits = 0;
        Object objMovePointLeft = objArr[0];
        switch (iValue) {
            case 0:
            case 1:
            case 31:
                break;
            case 2:
                jDoubleToLongBits = inputStream.read_short() & 65535;
                break;
            case 3:
            case 17:
                jDoubleToLongBits = inputStream.read_long() & 4294967295L;
                break;
            case 4:
                jDoubleToLongBits = inputStream.read_ushort() & 65535;
                break;
            case 5:
                jDoubleToLongBits = inputStream.read_ulong() & 4294967295L;
                break;
            case 6:
                jDoubleToLongBits = Float.floatToIntBits(inputStream.read_float()) & 4294967295L;
                break;
            case 7:
                jDoubleToLongBits = Double.doubleToLongBits(inputStream.read_double());
                break;
            case 8:
                if (inputStream.read_boolean()) {
                    jDoubleToLongBits = 1;
                    break;
                } else {
                    jDoubleToLongBits = 0;
                    break;
                }
            case 9:
                jDoubleToLongBits = inputStream.read_char() & 65535;
                break;
            case 10:
                jDoubleToLongBits = inputStream.read_octet() & 255;
                break;
            case 11:
                objMovePointLeft = inputStream.read_any();
                break;
            case 12:
                objMovePointLeft = inputStream.read_TypeCode();
                break;
            case 13:
                objMovePointLeft = inputStream.read_Principal();
                break;
            case 14:
                if (objMovePointLeft instanceof Streamable) {
                    ((Streamable) objMovePointLeft)._read(inputStream);
                    break;
                } else {
                    objMovePointLeft = inputStream.read_Object();
                    break;
                }
            case 15:
            case 16:
            case 19:
            case 20:
            case 21:
            case 22:
                ((Streamable) objMovePointLeft)._read(inputStream);
                break;
            case 18:
                objMovePointLeft = inputStream.read_string();
                break;
            case 23:
                jDoubleToLongBits = inputStream.read_longlong();
                break;
            case 24:
                jDoubleToLongBits = inputStream.read_ulonglong();
                break;
            case 25:
            default:
                throw ORBUtilSystemException.get((ORB) inputStream.orb(), CORBALogDomains.RPC_PRESENTATION).typecodeNotSupported();
            case 26:
                jDoubleToLongBits = inputStream.read_wchar() & 65535;
                break;
            case 27:
                objMovePointLeft = inputStream.read_wstring();
                break;
            case 28:
                try {
                    if (inputStream instanceof CDRInputStream) {
                        objMovePointLeft = ((CDRInputStream) inputStream).read_fixed(typeCode.fixed_digits(), typeCode.fixed_scale());
                    } else {
                        objMovePointLeft = inputStream.read_fixed().movePointLeft(typeCode.fixed_scale());
                    }
                    break;
                } catch (BadKind e2) {
                    break;
                }
            case 29:
            case 30:
                objMovePointLeft = ((org.omg.CORBA_2_3.portable.InputStream) inputStream).read_value();
                break;
            case 32:
                objMovePointLeft = ((org.omg.CORBA_2_3.portable.InputStream) inputStream).read_abstract_interface();
                break;
        }
        objArr[0] = objMovePointLeft;
        jArr[0] = jDoubleToLongBits;
    }
}
