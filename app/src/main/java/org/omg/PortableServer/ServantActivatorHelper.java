package org.omg.PortableServer;

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

/* loaded from: rt.jar:org/omg/PortableServer/ServantActivatorHelper.class */
public abstract class ServantActivatorHelper {
    private static String _id = "IDL:omg.org/PortableServer/ServantActivator:2.3";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, ServantActivator servantActivator) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, servantActivator);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ServantActivator extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "ServantActivator");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ServantActivator read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, ServantActivator servantActivator) {
        throw new MARSHAL();
    }

    public static ServantActivator narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof ServantActivator) {
            return (ServantActivator) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _ServantActivatorStub _servantactivatorstub = new _ServantActivatorStub();
        _servantactivatorstub._set_delegate(delegate_get_delegate);
        return _servantactivatorstub;
    }

    public static ServantActivator unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof ServantActivator) {
            return (ServantActivator) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _ServantActivatorStub _servantactivatorstub = new _ServantActivatorStub();
        _servantactivatorstub._set_delegate(delegate_get_delegate);
        return _servantactivatorstub;
    }
}
