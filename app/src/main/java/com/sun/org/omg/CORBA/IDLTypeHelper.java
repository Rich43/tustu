package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.IDLType;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/IDLTypeHelper.class */
public final class IDLTypeHelper {
    private static String _id = "IDL:omg.org/CORBA/IDLType:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, IDLType iDLType) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, iDLType);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static IDLType extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "IDLType");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static IDLType read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_IDLTypeStub.class));
    }

    public static void write(OutputStream outputStream, IDLType iDLType) {
        outputStream.write_Object(iDLType);
    }

    public static IDLType narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof IDLType) {
            return (IDLType) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        return new _IDLTypeStub(((ObjectImpl) object)._get_delegate());
    }
}
