package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ParameterModeHelper.class */
public final class ParameterModeHelper {
    private static String _id = "IDL:omg.org/CORBA/ParameterMode:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, ParameterMode parameterMode) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, parameterMode);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ParameterMode extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_enum_tc(id(), "ParameterMode", new String[]{"PARAM_IN", "PARAM_OUT", "PARAM_INOUT"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ParameterMode read(InputStream inputStream) {
        return ParameterMode.from_int(inputStream.read_long());
    }

    public static void write(OutputStream outputStream, ParameterMode parameterMode) {
        outputStream.write_long(parameterMode.value());
    }
}
