package com.sun.corba.se.spi.activation;

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

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerHelper.class */
public abstract class ServerHelper {
    private static String _id = "IDL:activation/Server:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Server server) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, server);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Server extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "Server");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Server read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_ServerStub.class));
    }

    public static void write(OutputStream outputStream, Server server) {
        outputStream.write_Object(server);
    }

    public static Server narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Server) {
            return (Server) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _ServerStub _serverstub = new _ServerStub();
        _serverstub._set_delegate(delegate_get_delegate);
        return _serverstub;
    }

    public static Server unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Server) {
            return (Server) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _ServerStub _serverstub = new _ServerStub();
        _serverstub._set_delegate(delegate_get_delegate);
        return _serverstub;
    }
}
