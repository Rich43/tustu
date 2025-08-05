package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/SetOverrideTypeHelper.class */
public abstract class SetOverrideTypeHelper {
    private static String _id = "IDL:omg.org/CORBA/SetOverrideType:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, SetOverrideType setOverrideType) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, setOverrideType);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static SetOverrideType extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_enum_tc(id(), "SetOverrideType", new String[]{"SET_OVERRIDE", "ADD_OVERRIDE"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static SetOverrideType read(InputStream inputStream) {
        return SetOverrideType.from_int(inputStream.read_long());
    }

    public static void write(OutputStream outputStream, SetOverrideType setOverrideType) {
        outputStream.write_long(setOverrideType.value());
    }
}
