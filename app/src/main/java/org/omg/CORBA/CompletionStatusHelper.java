package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/CompletionStatusHelper.class */
public abstract class CompletionStatusHelper {
    private static String _id = "IDL:omg.org/CORBA/CompletionStatus:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, CompletionStatus completionStatus) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, completionStatus);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static CompletionStatus extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_enum_tc(id(), "CompletionStatus", new String[]{"COMPLETED_YES", "COMPLETED_NO", "COMPLETED_MAYBE"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static CompletionStatus read(InputStream inputStream) {
        return CompletionStatus.from_int(inputStream.read_long());
    }

    public static void write(OutputStream outputStream, CompletionStatus completionStatus) {
        outputStream.write_long(completionStatus.value());
    }
}
