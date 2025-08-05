package com.sun.corba.se.spi.activation;

import com.sun.corba.se.impl.orbutil.ORBConstants;
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

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/InitialNameServiceHelper.class */
public abstract class InitialNameServiceHelper {
    private static String _id = "IDL:activation/InitialNameService:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, InitialNameService initialNameService) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, initialNameService);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static InitialNameService extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), ORBConstants.INITIAL_NAME_SERVICE_NAME);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static InitialNameService read(InputStream inputStream) {
        return narrow(inputStream.read_Object(_InitialNameServiceStub.class));
    }

    public static void write(OutputStream outputStream, InitialNameService initialNameService) {
        outputStream.write_Object(initialNameService);
    }

    public static InitialNameService narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof InitialNameService) {
            return (InitialNameService) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _InitialNameServiceStub _initialnameservicestub = new _InitialNameServiceStub();
        _initialnameservicestub._set_delegate(delegate_get_delegate);
        return _initialnameservicestub;
    }

    public static InitialNameService unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof InitialNameService) {
            return (InitialNameService) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _InitialNameServiceStub _initialnameservicestub = new _InitialNameServiceStub();
        _initialnameservicestub._set_delegate(delegate_get_delegate);
        return _initialnameservicestub;
    }
}
