package com.sun.corba.se.impl.corba;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/TypeCodeImplHelper.class */
public abstract class TypeCodeImplHelper {
    private static String _id = "IDL:omg.org/CORBA/TypeCode:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, TypeCode typeCode) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, typeCode);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static TypeCode extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static TypeCode read(InputStream inputStream) {
        return inputStream.read_TypeCode();
    }

    public static void write(OutputStream outputStream, TypeCode typeCode) {
        outputStream.write_TypeCode(typeCode);
    }

    public static void write(OutputStream outputStream, TypeCodeImpl typeCodeImpl) {
        outputStream.write_TypeCode(typeCodeImpl);
    }
}
