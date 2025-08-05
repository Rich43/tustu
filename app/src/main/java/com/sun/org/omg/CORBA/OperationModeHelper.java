package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/OperationModeHelper.class */
public final class OperationModeHelper {
    private static String _id = "IDL:omg.org/CORBA/OperationMode:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, OperationMode operationMode) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, operationMode);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static OperationMode extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_enum_tc(id(), "OperationMode", new String[]{"OP_NORMAL", "OP_ONEWAY"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static OperationMode read(InputStream inputStream) {
        return OperationMode.from_int(inputStream.read_long());
    }

    public static void write(OutputStream outputStream, OperationMode operationMode) {
        outputStream.write_long(operationMode.value());
    }
}
