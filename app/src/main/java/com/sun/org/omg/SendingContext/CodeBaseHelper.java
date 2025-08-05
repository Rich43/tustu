package com.sun.org.omg.SendingContext;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/SendingContext/CodeBaseHelper.class */
public final class CodeBaseHelper {
    private static String _id = "IDL:omg.org/SendingContext/CodeBase:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, CodeBase codeBase) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, codeBase);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static CodeBase extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "CodeBase");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static CodeBase read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_CodeBaseStub.class));
    }

    public static void write(OutputStream outputStream, CodeBase codeBase) {
        outputStream.write_Object(codeBase);
    }

    public static CodeBase narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof CodeBase) {
            return (CodeBase) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        return new _CodeBaseStub(((ObjectImpl) object)._get_delegate());
    }
}
