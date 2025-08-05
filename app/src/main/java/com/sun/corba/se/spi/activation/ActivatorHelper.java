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

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ActivatorHelper.class */
public abstract class ActivatorHelper {
    private static String _id = "IDL:activation/Activator:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Activator activator) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, activator);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Activator extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "Activator");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Activator read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_ActivatorStub.class));
    }

    public static void write(OutputStream outputStream, Activator activator) {
        outputStream.write_Object(activator);
    }

    public static Activator narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Activator) {
            return (Activator) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _ActivatorStub _activatorstub = new _ActivatorStub();
        _activatorstub._set_delegate(delegate_get_delegate);
        return _activatorstub;
    }

    public static Activator unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Activator) {
            return (Activator) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _ActivatorStub _activatorstub = new _ActivatorStub();
        _activatorstub._set_delegate(delegate_get_delegate);
        return _activatorstub;
    }
}
