package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/ValueBaseHelper.class */
public abstract class ValueBaseHelper {
    private static String _id = "IDL:omg.org/CORBA/ValueBase:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Serializable serializable) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, serializable);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Serializable extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_value);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Serializable read(InputStream inputStream) {
        return ((org.omg.CORBA_2_3.portable.InputStream) inputStream).read_value();
    }

    public static void write(OutputStream outputStream, Serializable serializable) {
        ((org.omg.CORBA_2_3.portable.OutputStream) outputStream).write_value(serializable);
    }
}
