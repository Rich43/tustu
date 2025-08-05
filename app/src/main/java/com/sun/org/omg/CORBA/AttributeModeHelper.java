package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/AttributeModeHelper.class */
public final class AttributeModeHelper {
    private static String _id = "IDL:omg.org/CORBA/AttributeMode:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, AttributeMode attributeMode) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, attributeMode);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static AttributeMode extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_enum_tc(id(), "AttributeMode", new String[]{"ATTR_NORMAL", "ATTR_READONLY"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static AttributeMode read(InputStream inputStream) {
        return AttributeMode.from_int(inputStream.read_long());
    }

    public static void write(OutputStream outputStream, AttributeMode attributeMode) {
        outputStream.write_long(attributeMode.value());
    }
}
