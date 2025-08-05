package org.omg.DynamicAny;

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

/* loaded from: rt.jar:org/omg/DynamicAny/DynAnyFactoryHelper.class */
public abstract class DynAnyFactoryHelper {
    private static String _id = "IDL:omg.org/DynamicAny/DynAnyFactory:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, DynAnyFactory dynAnyFactory) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, dynAnyFactory);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static DynAnyFactory extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), ORBConstants.DYN_ANY_FACTORY_NAME);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static DynAnyFactory read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, DynAnyFactory dynAnyFactory) {
        throw new MARSHAL();
    }

    public static DynAnyFactory narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynAnyFactory) {
            return (DynAnyFactory) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynAnyFactoryStub _dynanyfactorystub = new _DynAnyFactoryStub();
        _dynanyfactorystub._set_delegate(delegate_get_delegate);
        return _dynanyfactorystub;
    }

    public static DynAnyFactory unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynAnyFactory) {
            return (DynAnyFactory) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynAnyFactoryStub _dynanyfactorystub = new _DynAnyFactoryStub();
        _dynanyfactorystub._set_delegate(delegate_get_delegate);
        return _dynanyfactorystub;
    }
}
