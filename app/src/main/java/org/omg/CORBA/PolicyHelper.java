package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/PolicyHelper.class */
public abstract class PolicyHelper {
    private static String _id = "IDL:omg.org/CORBA/Policy:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Policy policy) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, policy);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Policy extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "Policy");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Policy read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_PolicyStub.class));
    }

    public static void write(OutputStream outputStream, Policy policy) {
        outputStream.write_Object(policy);
    }

    public static Policy narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Policy) {
            return (Policy) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        return new _PolicyStub(((ObjectImpl) object)._get_delegate());
    }
}
