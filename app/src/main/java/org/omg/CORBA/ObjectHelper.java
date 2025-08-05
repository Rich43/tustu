package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/ObjectHelper.class */
public abstract class ObjectHelper {
    private static String _id = "";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Object object) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, object);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Object extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_objref);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Object read(InputStream inputStream) {
        return inputStream.read_Object();
    }

    public static void write(OutputStream outputStream, Object object) {
        outputStream.write_Object(object);
    }
}
