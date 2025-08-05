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

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextHelper.class */
public abstract class NamingContextHelper {
    private static String _id = "IDL:omg.org/CosNaming/NamingContext:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, NamingContext namingContext) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, namingContext);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NamingContext extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "NamingContext");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NamingContext read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_NamingContextStub.class));
    }

    public static void write(OutputStream outputStream, NamingContext namingContext) {
        outputStream.write_Object(namingContext);
    }

    public static NamingContext narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof NamingContext) {
            return (NamingContext) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _NamingContextStub _namingcontextstub = new _NamingContextStub();
        _namingcontextstub._set_delegate(delegate_get_delegate);
        return _namingcontextstub;
    }

    public static NamingContext unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof NamingContext) {
            return (NamingContext) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _NamingContextStub _namingcontextstub = new _NamingContextStub();
        _namingcontextstub._set_delegate(delegate_get_delegate);
        return _namingcontextstub;
    }
}
