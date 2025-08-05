package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableInterceptor/CurrentHelper.class */
public abstract class CurrentHelper {
    private static String _id = "IDL:omg.org/PortableInterceptor/Current:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Current current) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, current);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Current extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "Current");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Current read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, Current current) {
        throw new MARSHAL();
    }

    public static Current narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Current) {
            return (Current) object;
        }
        throw new BAD_PARAM();
    }

    public static Current unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Current) {
            return (Current) object;
        }
        throw new BAD_PARAM();
    }
}
