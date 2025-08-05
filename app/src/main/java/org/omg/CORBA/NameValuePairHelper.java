package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/NameValuePairHelper.class */
public abstract class NameValuePairHelper {
    private static String _id = "IDL:omg.org/CORBA/NameValuePair:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, NameValuePair nameValuePair) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, nameValuePair);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NameValuePair extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            synchronized (TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    __typeCode = ORB.init().create_struct_tc(id(), "NameValuePair", new StructMember[]{new StructMember("id", ORB.init().create_alias_tc(FieldNameHelper.id(), "FieldName", ORB.init().create_string_tc(0)), null), new StructMember("value", ORB.init().get_primitive_tc(TCKind.tk_any), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NameValuePair read(InputStream inputStream) {
        NameValuePair nameValuePair = new NameValuePair();
        nameValuePair.id = inputStream.read_string();
        nameValuePair.value = inputStream.read_any();
        return nameValuePair;
    }

    public static void write(OutputStream outputStream, NameValuePair nameValuePair) {
        outputStream.write_string(nameValuePair.id);
        outputStream.write_any(nameValuePair.value);
    }
}
