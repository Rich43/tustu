package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableInterceptor/IORInterceptor_3_0Helper.class */
public abstract class IORInterceptor_3_0Helper {
    private static String _id = "IDL:omg.org/PortableInterceptor/IORInterceptor_3_0:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, IORInterceptor_3_0 iORInterceptor_3_0) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, iORInterceptor_3_0);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static IORInterceptor_3_0 extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "IORInterceptor_3_0");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static IORInterceptor_3_0 read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, IORInterceptor_3_0 iORInterceptor_3_0) {
        throw new MARSHAL();
    }

    public static IORInterceptor_3_0 narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof IORInterceptor_3_0) {
            return (IORInterceptor_3_0) object;
        }
        throw new BAD_PARAM();
    }

    public static IORInterceptor_3_0 unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof IORInterceptor_3_0) {
            return (IORInterceptor_3_0) object;
        }
        throw new BAD_PARAM();
    }
}
