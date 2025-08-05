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

/* loaded from: rt.jar:org/omg/DynamicAny/DynUnionHelper.class */
public abstract class DynUnionHelper {
    private static String _id = "IDL:omg.org/DynamicAny/DynUnion:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, DynUnion dynUnion) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, dynUnion);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static DynUnion extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "DynUnion");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static DynUnion read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, DynUnion dynUnion) {
        throw new MARSHAL();
    }

    public static DynUnion narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynUnion) {
            return (DynUnion) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynUnionStub _dynunionstub = new _DynUnionStub();
        _dynunionstub._set_delegate(delegate_get_delegate);
        return _dynunionstub;
    }

    public static DynUnion unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynUnion) {
            return (DynUnion) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynUnionStub _dynunionstub = new _DynUnionStub();
        _dynunionstub._set_delegate(delegate_get_delegate);
        return _dynunionstub;
    }
}
