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

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/LocatorHelper.class */
public abstract class LocatorHelper {
    private static String _id = "IDL:activation/Locator:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Locator locator) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, locator);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Locator extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "Locator");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Locator read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_LocatorStub.class));
    }

    public static void write(OutputStream outputStream, Locator locator) {
        outputStream.write_Object(locator);
    }

    public static Locator narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Locator) {
            return (Locator) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _LocatorStub _locatorstub = new _LocatorStub();
        _locatorstub._set_delegate(delegate_get_delegate);
        return _locatorstub;
    }

    public static Locator unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Locator) {
            return (Locator) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _LocatorStub _locatorstub = new _LocatorStub();
        _locatorstub._set_delegate(delegate_get_delegate);
        return _locatorstub;
    }
}
