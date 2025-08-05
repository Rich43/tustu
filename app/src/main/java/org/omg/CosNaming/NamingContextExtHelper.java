package org.omg.CosNaming;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextExtHelper.class */
public abstract class NamingContextExtHelper {
    private static String _id = "IDL:omg.org/CosNaming/NamingContextExt:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, NamingContextExt namingContextExt) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, namingContextExt);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NamingContextExt extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "NamingContextExt");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NamingContextExt read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_NamingContextExtStub.class));
    }

    public static void write(OutputStream outputStream, NamingContextExt namingContextExt) {
        outputStream.write_Object(namingContextExt);
    }

    public static NamingContextExt narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof NamingContextExt) {
            return (NamingContextExt) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _NamingContextExtStub _namingcontextextstub = new _NamingContextExtStub();
        _namingcontextextstub._set_delegate(delegate_get_delegate);
        return _namingcontextextstub;
    }

    public static NamingContextExt unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof NamingContextExt) {
            return (NamingContextExt) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _NamingContextExtStub _namingcontextextstub = new _NamingContextExtStub();
        _namingcontextextstub._set_delegate(delegate_get_delegate);
        return _namingcontextextstub;
    }
}
