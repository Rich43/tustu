package org.omg.DynamicAny;

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

/* loaded from: rt.jar:org/omg/DynamicAny/DynAnyHelper.class */
public abstract class DynAnyHelper {
    private static String _id = "IDL:omg.org/DynamicAny/DynAny:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, DynAny dynAny) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, dynAny);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static DynAny extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "DynAny");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static DynAny read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, DynAny dynAny) {
        throw new MARSHAL();
    }

    public static DynAny narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynAny) {
            return (DynAny) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynAnyStub _dynanystub = new _DynAnyStub();
        _dynanystub._set_delegate(delegate_get_delegate);
        return _dynanystub;
    }

    public static DynAny unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynAny) {
            return (DynAny) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynAnyStub _dynanystub = new _DynAnyStub();
        _dynanystub._set_delegate(delegate_get_delegate);
        return _dynanystub;
    }
}
