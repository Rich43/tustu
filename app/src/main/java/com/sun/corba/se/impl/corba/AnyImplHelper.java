package com.sun.corba.se.impl.corba;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/AnyImplHelper.class */
public abstract class AnyImplHelper {
    private static String _id = "IDL:omg.org/CORBA/Any:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Any any2) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, any2);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Any extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_any);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Any read(InputStream inputStream) {
        return inputStream.read_any();
    }

    public static void write(OutputStream outputStream, Any any) {
        outputStream.write_any(any);
    }
}
