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

/* loaded from: rt.jar:org/omg/DynamicAny/DynSequenceHelper.class */
public abstract class DynSequenceHelper {
    private static String _id = "IDL:omg.org/DynamicAny/DynSequence:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, DynSequence dynSequence) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, dynSequence);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static DynSequence extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "DynSequence");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static DynSequence read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, DynSequence dynSequence) {
        throw new MARSHAL();
    }

    public static DynSequence narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynSequence) {
            return (DynSequence) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynSequenceStub _dynsequencestub = new _DynSequenceStub();
        _dynsequencestub._set_delegate(delegate_get_delegate);
        return _dynsequencestub;
    }

    public static DynSequence unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DynSequence) {
            return (DynSequence) object;
        }
        Delegate delegate_get_delegate = ((ObjectImpl) object)._get_delegate();
        _DynSequenceStub _dynsequencestub = new _DynSequenceStub();
        _dynsequencestub._set_delegate(delegate_get_delegate);
        return _dynsequencestub;
    }
}
