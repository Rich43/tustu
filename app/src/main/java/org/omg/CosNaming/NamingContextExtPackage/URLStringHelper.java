package org.omg.CosNaming.NamingContextExtPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextExtPackage/URLStringHelper.class */
public abstract class URLStringHelper {
    private static String _id = "IDL:omg.org/CosNaming/NamingContextExt/URLString:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, String str) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, str);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static String extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_string_tc(0);
            __typeCode = ORB.init().create_alias_tc(id(), "URLString", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static String read(InputStream inputStream) {
        return inputStream.read_string();
    }

    public static void write(OutputStream outputStream, String str) {
        outputStream.write_string(str);
    }
}
